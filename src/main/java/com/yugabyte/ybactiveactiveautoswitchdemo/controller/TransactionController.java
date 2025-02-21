package com.yugabyte.ybactiveactiveautoswitchdemo.controller;


import com.yugabyte.ybactiveactiveautoswitchdemo.config.DataSourceContextHolder;
import com.yugabyte.ybactiveactiveautoswitchdemo.dao.TransactionRepository;
import com.yugabyte.ybactiveactiveautoswitchdemo.model.Customer;
import com.yugabyte.ybactiveactiveautoswitchdemo.model.Order;
import com.yugabyte.ybactiveactiveautoswitchdemo.service.DataSourceSwitcher;
import com.yugabyte.ybactiveactiveautoswitchdemo.util.GeneralUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@SessionAttributes("randomCustomers")
@RequestMapping("/api")
public class TransactionController {
    private final TransactionRepository transactionRepository;
    private final DataSourceSwitcher dataSourceSwitcher;
    @Autowired
    public TransactionController(TransactionRepository transactionRepository,DataSourceSwitcher dataSourceSwitcher) {
        this.transactionRepository = transactionRepository;
        this.dataSourceSwitcher = dataSourceSwitcher;
    }

    @GetMapping("/switchToPrimary")
    public ResponseEntity<String> switchToPrimary() {
        dataSourceSwitcher.switchToPrimary();
        return ResponseEntity.ok("Switched to primary data source.");
    }

    @GetMapping("/switchToSecondary")
    public ResponseEntity<String> switchToSecondary() {
        dataSourceSwitcher.switchToSecondary();
        return ResponseEntity.ok("Switched to secondary data source.");
    }

    @GetMapping("/currentDataSource")
    public ResponseEntity<String> currentDataSource() {
        String currentDataSource = DataSourceContextHolder.getDataSourceType();
        if (currentDataSource != null) {
            return ResponseEntity.ok("Current data source: " + currentDataSource);
        } else {
            return ResponseEntity.ok("No data source set.");
        }
    }

    @GetMapping("/add")
    public ResponseEntity<String> writeData(@RequestParam(required = false, defaultValue = "1") int records){
        List<Customer> customers = GeneralUtility.getRandomCustomers(records);
        for(Customer customer: customers){
            try{
                transactionRepository.save(customer);
            }
            catch (Exception e){
                System.out.println("Error adding record: " + customer.toString());
                e.printStackTrace();
            }
        }
        return ResponseEntity.ok("done");
    }




}
