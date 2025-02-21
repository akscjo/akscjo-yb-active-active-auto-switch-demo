package com.yugabyte.ybactiveactiveautoswitchdemo.dao;

import com.yugabyte.ybactiveactiveautoswitchdemo.config.DataSourceContextHolder;
import com.yugabyte.ybactiveactiveautoswitchdemo.model.Customer;
import com.yugabyte.ybactiveactiveautoswitchdemo.model.Order;
import com.yugabyte.ybactiveactiveautoswitchdemo.service.DataSourceSwitcher;
import com.yugabyte.ybactiveactiveautoswitchdemo.util.LoadGeneratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.retry.support.RetrySynchronizationManager;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Repository
public class TransactionRepository implements TransactionRepositoryInterface{
    private final JdbcTemplate jdbcTemplate;
    private final DataSourceSwitcher dataSourceSwitcher;
    private static final String SQL_CUSTOMER_INSERT = "INSERT INTO customers (customer_id, company_name, customer_state) VALUES (?, ?, ?)";
    @Autowired
    private RetryTemplate retryTemplate;
    @Autowired
    private TransactionTemplate transactionTemplate;
    private static final Logger logger = LoggerFactory.getLogger(TransactionRepository.class);


    @Autowired
    public TransactionRepository(JdbcTemplate jdbcTemplate, DataSourceSwitcher dataSourceSwitcher) {

        this.jdbcTemplate = jdbcTemplate;
        this.dataSourceSwitcher = dataSourceSwitcher;
    }

    @Override
    public void save(Customer customer) {
        retryTemplate.execute(context -> {
            // Check if retry is happening
            if (RetrySynchronizationManager.getContext().getRetryCount() > 0) {
                logger.warn("RETRY IS HAPPENING [{}]: transaction ID [{}]", RetrySynchronizationManager.getContext().getRetryCount(), customer.getCustomerId());
            }

            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    try {
                        String currentDataSource = DataSourceContextHolder.getDataSourceType();
                        logger.info("Inserting data to: {}", currentDataSource);
                        jdbcTemplate.update(
                                SQL_CUSTOMER_INSERT,
                                customer.getCustomerId(),
                                customer.getCompanyName(),
                                customer.getCustomerState()
                        );
                    }
                    catch (Exception ex) {
                        logger.error("Transaction failed, rolling back. Error: {}", ex.getMessage());
                        status.setRollbackOnly();
                        throw ex;
                    }
                }
            });
            return null;
        });



    }
    @Override
    public List<Customer> getCustomers(){
        String sql = "SELECT * from customers limit 100" ;
//        dataSourceSwitcher.switchToSecondary();
        System.out.println("@@@@@@@@@@ACTEST - CAME HERE - getCustomers");
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Customer.class));
    }
}
