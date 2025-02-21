package com.yugabyte.ybactiveactiveautoswitchdemo.service;

import com.yugabyte.ybactiveactiveautoswitchdemo.config.DataSourceContextHolder;
import com.yugabyte.ybactiveactiveautoswitchdemo.config.DynamicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class DataSourceSwitcher {
    @Autowired
    private ApplicationContext context;
    public void switchToPrimary() {
        DataSourceContextHolder.setDataSourceType("primary");
        reloadDataSource();
    }

    public void switchToSecondary() {
        DataSourceContextHolder.setDataSourceType("secondary");
        reloadDataSource();
        System.out.println("@@@ACTEST DataSourceContextHolder.getDataSourceType():"+DataSourceContextHolder.getDataSourceType());
    }

    private void reloadDataSource() {
        DynamicDataSource dataSource = context.getBean(DynamicDataSource.class);
        dataSource.afterPropertiesSet(); // Reload the datasource
    }

}

