package com.yugabyte.ybactiveactiveautoswitchdemo.config;

public class DataSourceContextHolder {

    private static String dataSourceType;

    public static void setDataSourceType(String type) {
        dataSourceType = type;
    }

    public static String getDataSourceType() {
        return dataSourceType;
    }

    public static void clearDataSourceType() {
        dataSourceType = null;
    }
}

