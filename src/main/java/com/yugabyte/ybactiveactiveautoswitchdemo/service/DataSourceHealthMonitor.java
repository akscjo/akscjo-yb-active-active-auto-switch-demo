package com.yugabyte.ybactiveactiveautoswitchdemo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class DataSourceHealthMonitor {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceHealthMonitor.class);
    private final DataSourceSwitcher dataSourceSwitcher;
    private final JdbcTemplate jdbcTemplate;
    private final JdbcTemplate primaryJdbcTemplate; // Dedicated template for health checks

    private boolean usingSecondary = false;

    @Autowired
    public DataSourceHealthMonitor(DataSourceSwitcher dataSourceSwitcher, JdbcTemplate jdbcTemplate, DataSource primaryDataSource) {
        this.dataSourceSwitcher = dataSourceSwitcher;
        this.jdbcTemplate = jdbcTemplate;
        this.primaryJdbcTemplate = new JdbcTemplate(primaryDataSource); // Use primary datasource directly
    }

    @Scheduled(fixedDelayString = "${datasource.health-check-interval:5000}")
    public void checkPrimaryDataSourceHealth() {
        try {
            // Perform health check using dedicated connection without changing context
            primaryJdbcTemplate.queryForObject("SELECT 1", Integer.class);

            // If previously using secondary and primary is back, switch back
            if (usingSecondary) {
                logger.info("Primary datasource is back. Switching to primary.");
                dataSourceSwitcher.switchToPrimary();
                usingSecondary = false;
            } else {
                logger.info("Primary datasource is healthy.");
            }
        } catch (Exception e) {
            logger.error("Primary datasource check failed: " + e.getMessage());

            // If primary fails and we're not already using secondary, switch
            if (!usingSecondary) {
                logger.warn("Switching to secondary datasource.");
                dataSourceSwitcher.switchToSecondary();
                usingSecondary = true;
            }
        }
    }
}
