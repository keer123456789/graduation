package com.keer.mydb.domain;

import com.alibaba.fastjson.JSONObject;
import com.bigchaindb.model.Asset;
import com.bigchaindb.model.Assets;
import com.bigchaindb.model.Transaction;
import com.google.gson.internal.LinkedTreeMap;
import com.keer.mydb.bigchaindb.BigchainDBRunner;
import com.keer.mydb.bigchaindb.BigchainDBUtil;

import java.io.IOException;
import java.util.*;

/**
 * 表结构
 */
public class Table {
    private String tableName;
    private String type;
    private List<String> columnName;
    private List<Map> data;
    private Map<String, String> rowData;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getColumnName() {
        return columnName;
    }

    public void setColumnName(List<String> columnName) {
        this.columnName = columnName;
    }

    /**
     * transactions 集合必须是表名相同
     *
     * @param transaction
     */
    public void setColumnName(Transaction transaction) {
        List<String> result = new ArrayList<String>();
        if (!(this.tableName.equals(null) && this.type.equals(null))) {
            if (this.type.equals("CREATE")) {
                LinkedTreeMap<String, Object> map = (LinkedTreeMap) transaction.getAsset().getData();
                if (map.get("tableName").toString().equals(this.tableName)) {
                    map = (LinkedTreeMap) map.get("tableData");
                    Set<String> keys = map.keySet();
                    for (String key : keys) {
                        result.add(key);
                    }
                }

            } else {
                LinkedTreeMap map = (LinkedTreeMap) transaction.getMetaData();
                if (map.get("tableName").toString().equals(this.tableName)) {
                    map = (LinkedTreeMap) map.get("tableData");
                    Set<String> keys = map.keySet();
                    for (String key : keys) {
                        result.add(key);
                    }
                }
            }
            HashSet h = new HashSet(result);
            result.clear();
            result.addAll(h);
            this.columnName = result;

        } else {
            this.columnName = null;
        }
    }

    /**
     * 通过asserts获得表数据
     *
     * @param assets
     */
    public void setTableData(Assets assets) {
        List<String> result = new ArrayList<String>();
        if (!(this.tableName.equals(null) && this.type.equals(null))) {
            for (Asset asset : assets.getAssets()) {
                LinkedTreeMap<String, Object> map = (LinkedTreeMap) asset.getData();
                map = (LinkedTreeMap) map.get("tableData");
                Set<String> keys = map.keySet();
                for (String key : keys) {
                    result.add(key);
                }
                List<Map> list = new ArrayList<Map>();
                list.add(map);
                if (this.data == null) {
                    this.data = list;
                } else {
                    list.addAll(this.data);
                    this.data = list;
                }
            }
            HashSet h = new HashSet(result);
            result.clear();
            result.addAll(h);
            this.columnName = result;
        } else {
            this.columnName = null;
        }
    }

    public void setTableData(List<MetaData> metaDatas) {
        List<String> result = new ArrayList<String>();
        if (!(this.tableName.equals(null) && this.type.equals(null))) {
            for (MetaData metadata : metaDatas) {
                TreeMap<String, Object> map = (TreeMap<String, Object>) metadata.getMetadata();
                JSONObject map1 = (JSONObject) map.get("tableData");

                Set<String> keys = map1.keySet();
                for (String key : keys) {
                    result.add(key);
                }
                List<Map> list = new ArrayList<Map>();
                list.add(map1);
                if (this.data == null) {
                    this.data = list;
                } else {
                    list.addAll(this.data);
                    this.data = list;
                }
            }
            HashSet h = new HashSet(result);
            result.clear();
            result.addAll(h);
            this.columnName = result;
        } else {
            this.columnName = null;
        }

    }


    public List<Map> getData() {
        return data;
    }

    public void setData(List<Map> data) {
        this.data = data;
    }


    public Map<String, String> getRowData() {
        return rowData;
    }

    public void setRowData(Map<String, String> rowData) {
        this.rowData = rowData;

    }

    public void setRowData(Transaction transaction) {
        LinkedTreeMap map = new LinkedTreeMap();
        if (transaction.getOperation().equals("\"CREATE\"")) {
            map = (LinkedTreeMap) transaction.getAsset().getData();
            map = (LinkedTreeMap) map.get("tableData");
        } else {
            map = (LinkedTreeMap) transaction.getMetaData();
            map = (LinkedTreeMap) map.get("tableData");
        }
        List<Map> list = new ArrayList<Map>();
        list.add(map);
        if (this.data == null) {
            this.data = list;
        } else {
            list.addAll(this.data);
            this.data = list;
        }


    }

    public static void main(String[] args) throws IOException {
        BigchainDBRunner.StartConn();
        Table table = new Table();
        table.setType("TRANSFER");
        table.setTableName("Person");

        List<MetaData> metaDatas = BigchainDBUtil.getMetaDatasByKey("Person");
        table.setTableData(metaDatas);
        System.out.println("hhh");
    }
}
