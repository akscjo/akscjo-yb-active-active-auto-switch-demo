package com.yugabyte.ybactiveactiveautoswitchdemo.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
//        System.out.println("@@@@@@@@@@ACTEST Came inside determineCurrentLookupKey....DataSourceContextHolder.getDataSourceType():"+DataSourceContextHolder.getDataSourceType());
        return DataSourceContextHolder.getDataSourceType();
    }
}
