package com.keer.mydb.domain;

import java.util.Map;

/**
 * 表结构使用kv结构的存储
 */
public class BigchaindbData {
    //表名
    private String tableName;
    //表中数据
    private Map tableData;

    public BigchaindbData() {
    }

    public BigchaindbData(String tableName, Map tableData) {
        this.tableName = tableName;
        this.tableData = tableData;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Map getTableData() {
        return tableData;
    }

    public void setTableData(Map tableData) {
        this.tableData = tableData;
    }
}
