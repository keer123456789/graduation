package com.keer.graduation.Service.Implement;

import com.bigchaindb.api.OutputsApi;
import com.bigchaindb.model.Assets;
import com.bigchaindb.model.Outputs;
import com.keer.graduation.BDQLParser.BDQLUtil;
import com.keer.graduation.Bigchaindb.BigchainDBRunner;
import com.keer.graduation.Bigchaindb.BigchainDBUtil;
import com.keer.graduation.Bigchaindb.KeyPairHolder;
import com.keer.graduation.Domain.BigchainDBData;
import com.keer.graduation.Domain.MetaData;
import com.keer.graduation.Domain.ParserResult;
import com.keer.graduation.Domain.Table;
import com.keer.graduation.Service.IExperimentService;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExperimentServiceImp implements IExperimentService {
    private static Logger logger = LoggerFactory.getLogger(BigchainDBServiceImp.class);

    private final static String Asset = "asset";
    private final static String Metadata = "metadata";


    @Autowired
    KeyPairHolder keyPairHolder;

    @Autowired
    BigchainDBRunner bigchainDBRunner;

    @Autowired
    BDQLUtil bdqlUtil;

    @Autowired
    BigchainDBUtil bigchainDBUtil;


    /**
     * insert语句测试
     *
     * @param assetTotal 实验个数
     * @return
     * @throws InterruptedException
     * @throws IOException
     */
    @Override
    public ParserResult insertExperiment(int assetTotal) throws InterruptedException, IOException {

        ParserResult result = new ParserResult();
        List<Long> insertTime = new ArrayList<>();

        long startTime = System.currentTimeMillis();//开始时间

        bigchainDBRunner.StartConn();
        for (int m = 0; m < assetTotal; m++) {
            long insertStartTime = System.currentTimeMillis();
            result = bdqlUtil.work("INSERT INTO Computer (id, ip,mac,size,cpu,ROM,RAM) VALUES ('" + (m + 1) + "','" + (m + 2) + "','Champs-Elysees','" + (m + 3) + "','i7','" + (m + 4) + "','" + (m + 5) + "')");
            insertTime.add(System.currentTimeMillis() - insertStartTime);
            String id = (String) result.getData();
            logger.info("资产ID：" + id);
            Thread.sleep(500);

        }
        long endTime = System.currentTimeMillis();
        Double avgTime = 0.0;
        long total = 0;
        for (Long a : insertTime) {
            total = total + a;
        }

        double avg = (double) total / (double) insertTime.size();

        logger.info("实验结果：");
        Outputs outputs = OutputsApi.getOutputs(keyPairHolder.pubKeyToString(keyPairHolder.getPublic()));
        logger.info("BigchainDB中实际个数：" + outputs.getOutput().size());
        logger.info("本次实验的总时间：" + (endTime - startTime));
        logger.info("本次insert语句个数：" + assetTotal);
        logger.info("本次实验中BDQL语句消耗时间：" + (total));
        logger.info("平均每次语句使用时间：" + avg);
        logger.info("本次实验中非BDQL语句消耗时间：" + ((endTime - startTime) - total));


        Map map = new HashMap();
        map.put("本次实验的总时间：", (endTime - startTime));
        map.put("本次insert语句个数", assetTotal);
        map.put("BigchainDB中实际个数：", outputs.getOutput().size());
        map.put("本次实验中BDQL语句消耗时间：", total);
        map.put("平均每次语句使用时间：", ((double) total / (double) insertTime.size()));
        map.put("本次实验中非BDQL语句消耗时间：：", ((endTime - startTime) - total));
        result.setStatus(ParserResult.SUCCESS);
        result.setData(map);
        result.setMessage(null);
        if (assetTotal != 100000) {
            buildExecl("insertByDriver", insertTime);
        }
        return result;
    }

    @Override
    public ParserResult insertByDriverExperiment(int asstTotal) throws Exception {
        ParserResult result = new ParserResult();
        bigchainDBRunner.StartConn();
        List<Long> insertTime = new ArrayList<>();
        long startTime = System.currentTimeMillis();//开始时间
        for (int i = 0; i < asstTotal; i++) {
            long insertStartTime = System.currentTimeMillis();
            Map map = new HashMap();
            map.put("id", "" + (i + 1));
            map.put("ip", "" + (i + 2));
            map.put("size", "" + (i + 3));
            map.put("ROM", "" + (i + 4));
            map.put("RAM", "" + (i + 5));
            map.put("mac", "Champs-Elysees");
            map.put("cpu", "i7");
            BigchainDBData data = new BigchainDBData("Computer", map);
            String id = bigchainDBUtil.createAsset(data);
            insertTime.add(System.currentTimeMillis() - insertStartTime);
            logger.info("第" + i + "次插入,交易ID：" + id);
            Thread.sleep(500);
        }
        long endTime = System.currentTimeMillis();
        Double avgTime = 0.0;
        long total = 0;
        for (Long a : insertTime) {
            total = total + a;
        }

        double avg = (double) total / (double) insertTime.size();

        logger.info("实验结果：");
        Outputs outputs = OutputsApi.getOutputs(keyPairHolder.pubKeyToString(keyPairHolder.getPublic()));
        logger.info("BigchainDB中实际个数：" + outputs.getOutput().size());
        logger.info("本次实验的总时间：" + (endTime - startTime));
        logger.info("本次创建资产的个数：" + asstTotal);
        logger.info("本次实验中Driver语法消耗时间：" + (total));
        logger.info("平均每次语句使用时间：" + avg);
        logger.info("本次实验中非Driver语法消耗时间：" + ((endTime - startTime) - total));


        Map map = new HashMap();
        map.put("本次实验的总时间：", (endTime - startTime));
        map.put("本次创建资产的个数", asstTotal);
        map.put("BigchainDB中实际个数：", outputs.getOutput().size());
        map.put("本次实验中Driver语法消耗时间：", total);
        map.put("平均每次语句使用时间：", ((double) total / (double) insertTime.size()));
        map.put("本次实验中非Driver语法消耗时间：：", ((endTime - startTime) - total));
        result.setStatus(ParserResult.SUCCESS);
        result.setData(map);
        result.setMessage(null);
        if (asstTotal != 100000) {
            buildExecl("insertByDriver", insertTime);
        }
        return result;
    }

    /**
     * updata实验
     *
     * @param metadataTotal 实验个数
     * @return
     * @throws InterruptedException
     * @throws IOException
     */
    @Override
    public ParserResult updateExperiment(int metadataTotal) throws InterruptedException, IOException {
        ParserResult result = new ParserResult();
        long startTime = System.currentTimeMillis();//开始时间
        List<Long> updatetTime = new ArrayList<>();
        bigchainDBRunner.StartConn();
        result = bdqlUtil.work("INSERT INTO Computer (id, ip,mac,size,cpu,ROM,RAM) VALUES ('nihao','nihao','Champs-Elysees','nihao','i7','nihao','nihao')");
        Thread.sleep(5000);
        String id = (String) result.getData();


        for (int m = 0; m < metadataTotal; m++) {
            long insertStartTime = System.currentTimeMillis();
            result = bdqlUtil.work("UPDATE Person SET FirstName = '" + m + "' , SecondName='" + (m + 1) + "',age= '" + (m + 11) + "',time='" + (m + 12) + "' WHERE ID='" + id + "'");
//            result = bdqlUtil.work("UPDATE Person SET FirstName = '8000' , SecondName='" + (m + 1) + "',age= '" + (m + 11) + "',time='" + (m + 12) + "' WHERE ID='f3d9f405ab35266da01605bf205eaae2575f4249e5dc4b2c406bd0e8f5c79dc2'");
            updatetTime.add(System.currentTimeMillis() - insertStartTime);
            String TXid = (String) result.getData();
            for (; true; ) {
                if (bigchainDBUtil.checkTransactionExit(TXid)) {
                    break;
                }
            }
            logger.info("交易ID：" + TXid);


        }
        long endTime = System.currentTimeMillis();
        long total = 0;
        for (Long a : updatetTime) {
            total = total + a;
        }

        double avg = (double) total / (double) updatetTime.size();
        logger.info("实验结果：");
        Outputs outputs = OutputsApi.getOutputs(keyPairHolder.pubKeyToString(keyPairHolder.getPublic()));
        logger.info("BigchainDB中实际个数：" + (outputs.getOutput().size() - 1));
        logger.info("本次实验的总时间：" + (endTime - startTime));
        logger.info("本次insert语句个数：" + metadataTotal);
        logger.info("本次实验中BDQL语句消耗时间：" + (total));
        logger.info("平均每次语句使用时间：" + (avg));
        logger.info("本次实验中非BDQL语句消耗时间：" + ((endTime - startTime) - total));


        Map map = new HashMap();
        map.put("本次实验的总时间：", (endTime - startTime));
        map.put("本次insert语句个数", metadataTotal);
        map.put("BigchainDB中实际个数：", (outputs.getOutput().size() - 1));
        map.put("本次实验中BDQL语句消耗时间：", total);
        map.put("平均每次语句使用时间：", avg);
        map.put("本次实验中非BDQL语句消耗时间：：", ((endTime - startTime) - total));
        map.put("每次消耗", updatetTime);
        result.setStatus(ParserResult.SUCCESS);
        result.setData(map);
        result.setMessage(null);
        if (metadataTotal != 100000) {
            buildExecl("update", updatetTime);
        }
        return result;
    }

    @Override
    public ParserResult updateByDriverExperiment(int metadataTotal) throws InterruptedException, IOException {
        ParserResult result = new ParserResult();
        bigchainDBRunner.StartConn();
        long startTime = System.currentTimeMillis();//开始时间
        List<Long> updateTime = new ArrayList<>();

        result = bdqlUtil.work("INSERT INTO Computer (id, ip,mac,size,cpu,ROM,RAM) VALUES ('nihao','nihao','Champs-Elysees','nihao','i7','nihao','nihao')");
        Thread.sleep(5000);
        String id = (String) result.getData();

        for (int m = 0; m < metadataTotal; m++) {
            long insertStartTime = System.currentTimeMillis();

            Map map = new HashMap();
            map.put("FirstName", "" + m);
            map.put("SecondName", "" + (m + 1));
            map.put("age", "" + (m + 11));
            map.put("time", "" + (m + 12));
            BigchainDBData data = new BigchainDBData("Person", map);
            String txid = bigchainDBUtil.transferToSelf(data, id);
            updateTime.add(System.currentTimeMillis() - insertStartTime);
            for (; true; ) {
                if (bigchainDBUtil.checkTransactionExit(txid)) {
                    break;
                }
            }
            logger.info("第" + (m + 1) + "次交易，交易ID：" + txid);
            Thread.sleep(1000);//停止1s

        }
        long endTime = System.currentTimeMillis();
        long total = 0;
        for (Long a : updateTime) {
            total = total + a;
        }

        double avg = (double) total / (double) updateTime.size();
        logger.info("实验结果：");
        Outputs outputs = OutputsApi.getOutputs(keyPairHolder.pubKeyToString(keyPairHolder.getPublic()));
        logger.info("BigchainDB中实际个数：" + (outputs.getOutput().size() - 1));
        logger.info("本次实验的总时间：" + (endTime - startTime));
        logger.info("本次交易个数：" + metadataTotal);
        logger.info("本次实验中Driver语法消耗时间：" + (total));
        logger.info("平均每次语句使用时间：" + (avg));
        logger.info("本次实验中非Driver语法消耗时间：" + ((endTime - startTime) - total));


        Map map = new HashMap();
        map.put("本次实验的总时间：", (endTime - startTime));
        map.put("本次交易个数", metadataTotal);
        map.put("BigchainDB中实际个数：", (outputs.getOutput().size() - 1));
        map.put("本次实验中Driver语法消耗时间：", total);
        map.put("平均每次语句使用时间：", avg);
        map.put("本次实验中非Driver语法消耗时间：：", ((endTime - startTime) - total));
        map.put("每次消耗", updateTime);
        result.setStatus(ParserResult.SUCCESS);
        result.setData(map);
        result.setMessage(null);
        if (metadataTotal != 100000) {
            buildExecl("updateByDriver", updateTime);
        }
        return result;
    }

    /**
     * @param total DB中的数
     * @param count 循环次数
     * @return
     * @throws InterruptedException
     */
    @Override
    public ParserResult selectAssetExperiment(int total, int count) throws InterruptedException {
        ParserResult result = new ParserResult();
        bigchainDBRunner.StartConn();

        for (int j = 0; j < 20; j++) {
            double random = Math.random();
            int sum = (int) (random * total);

            List<Map> listMaps = new ArrayList<>();
            for (int i = 1; i <= count; i++) {
                Map map = new HashMap();
                map.put("随机数", sum);

                /**
                 * BDQL查询全部表中数据
                 */

                result = bdqlUtil.work("select * from Computer");

                map.put("QL查询全部数据", result.getMessage());
                logger.info("QL * 查询表中全部信息的时间：" + result.getMessage());


                Thread.sleep(1000);

                result = bdqlUtil.work("select * from Computer where id=" + sum);
                map.put("QL查询=", result.getMessage());
                logger.info("QL 查询id=" + sum + "的时间：" + result.getMessage());


                Thread.sleep(1000);

                result = bdqlUtil.work("select * from Computer where id<" + sum);
                map.put("QL查询<", result.getMessage());
                logger.info("QL 查询id<" + sum + "的时间：" + result.getMessage());


                Thread.sleep(1000);

                result = bdqlUtil.work("select * from Computer where id<=" + sum);
                map.put("QL查询<=", result.getMessage());
                logger.info("QL 查询id<=" + sum + "的时间：" + result.getMessage());


                Thread.sleep(1000);

                result = bdqlUtil.work("select * from Computer where id>" + sum);
                map.put("QL查询>", result.getMessage());
                logger.info("QL 查询id>" + sum + "的时间：" + result.getMessage());


                Thread.sleep(1000);

                result = bdqlUtil.work("select * from Computer where id>=" + sum);
                map.put("QL查询>=", result.getMessage());
                logger.info("QL 查询id>=" + sum + "的时间：" + result.getMessage());

                listMaps.add(map);
            }

            buildSelectExecl(listMaps, "./selectAsset" + j + ".xls");
        }
        result.setMessage("查询成功");
        return result;
    }

    /**
     * @param total 库中数据量
     * @param count 查询次数
     * @param a     sql查询的随机数
     * @return
     * @throws InterruptedException
     */
    public ParserResult selectAssetByDriverExperiment(int total, int count, int a) throws InterruptedException {
        ParserResult result = new ParserResult();
        bigchainDBRunner.StartConn();
        List<Map> listMaps = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            Map map = new HashMap();
            map.put("随机数", a);


            long startTime = System.currentTimeMillis();//开始时间
            List list = new ArrayList();
            list.add("id");
            list.add("ip");
            list.add("mac");
            list.add("size");
            list.add("cpu");
            list.add("ROM");
            list.add("RAM");
            long endTime = System.currentTimeMillis();//结束时间
            long listtime = endTime - startTime;


            /**
             * Driver查询全部的数据
             */
            startTime = System.currentTimeMillis();//开始时间
            Assets assets = bigchainDBUtil.getAssetByKey("Computer");
            Table table1 = new Table();
            table1.setType("CREATE");
            table1.setTableName("Computer");
            table1.setColumnName(list);
            table1.setTableData(assets);
            endTime = System.currentTimeMillis();//结束时间
            map.put("Driver查询全部数据", (endTime - startTime + listtime));
            logger.info("Driver 查询全部数据的时间：" + (endTime - startTime + listtime));


            Thread.sleep(1000);
            /**
             * Driver 条件查询 “=”
             */
            startTime = System.currentTimeMillis();//开始时间
            assets = bigchainDBUtil.getAssetByKey("Computer");
            Table table2 = new Table();
            table2.setTableName("Computer");
            table2.setType("CREATE");
            table2.setColumnName(list);

            Assets newAssets = new Assets();
            for (com.bigchaindb.model.Asset asset : assets.getAssets()) {
                Map map1 = (Map) asset.getData();
                map1 = (Map) map1.get("tableData");
                int b = Integer.parseInt(map1.get("id").toString());
                if (b == a) {
                    newAssets.addAsset(asset);
                }
            }
            table2.setTableData(newAssets);
            endTime = System.currentTimeMillis();//结束时间
            map.put("Driver查询=", (endTime - startTime + listtime));
            logger.info("Driver 查询id=" + a + "的时间：" + (endTime - startTime + listtime));


            Thread.sleep(1000);
            /**
             * Driver 条件查询 “<=”
             */
            startTime = System.currentTimeMillis();//开始时间
            assets = bigchainDBUtil.getAssetByKey("Computer");
            Table table3 = new Table();
            table3.setTableName("Computer");
            table3.setType("CREATE");
            table3.setColumnName(list);

            Assets newAssets1 = new Assets();
            for (com.bigchaindb.model.Asset asset : assets.getAssets()) {
                Map map1 = (Map) asset.getData();
                map1 = (Map) map1.get("tableData");
                int b = Integer.parseInt(map1.get("id").toString());
                if (b <= a) {
                    newAssets1.addAsset(asset);
                }
            }
            table3.setTableData(newAssets1);
            endTime = System.currentTimeMillis();//结束时间
            map.put("Driver查询<=", (endTime - startTime + listtime));
            logger.info("Driver 查询id<=" + a + "的时间：" + (endTime - startTime + listtime));


            Thread.sleep(1000);
            /**
             * Driver 条件查询 “>=”
             */
            startTime = System.currentTimeMillis();//开始时间
            assets = bigchainDBUtil.getAssetByKey("Computer");
            Table table4 = new Table();
            table4.setTableName("Computer");
            table4.setType("CREATE");
            table4.setColumnName(list);

            Assets newAssets2 = new Assets();
            for (com.bigchaindb.model.Asset asset : assets.getAssets()) {
                Map map1 = (Map) asset.getData();
                map1 = (Map) map1.get("tableData");
                int b = Integer.parseInt(map1.get("id").toString());
                if (b >= a) {
                    newAssets2.addAsset(asset);
                }
            }
            table4.setTableData(newAssets2);
            endTime = System.currentTimeMillis();//结束时间
            map.put("Driver查询>=", (endTime - startTime + listtime));
            logger.info("Driver 查询id>=" + a + "的时间：" + (endTime - startTime + listtime));

            Thread.sleep(1000);
            /**
             * Driver 条件查询 “<”
             */
            startTime = System.currentTimeMillis();//开始时间
            assets = bigchainDBUtil.getAssetByKey("Computer");
            Table table5 = new Table();
            table5.setTableName("Computer");
            table5.setType("CREATE");
            table5.setColumnName(list);

            Assets newAssets3 = new Assets();
            for (com.bigchaindb.model.Asset asset : assets.getAssets()) {
                Map map1 = (Map) asset.getData();
                map1 = (Map) map1.get("tableData");
                int b = Integer.parseInt(map1.get("id").toString());
                if (b < a) {
                    newAssets3.addAsset(asset);
                }
            }
            table5.setTableData(newAssets3);
            endTime = System.currentTimeMillis();//结束时间
            map.put("Driver查询<", (endTime - startTime + listtime));
            logger.info("Driver 查询id<" + a + "的时间：" + (endTime - startTime + listtime));

            Thread.sleep(1000);
            /**
             * Driver 条件查询 “>”
             */
            startTime = System.currentTimeMillis();//开始时间
            assets = bigchainDBUtil.getAssetByKey("Computer");
            Table table6 = new Table();
            table6.setType("CREATE");
            table6.setTableName("Computer");
            table6.setColumnName(list);

            Assets newAssets4 = new Assets();
            for (com.bigchaindb.model.Asset asset : assets.getAssets()) {
                Map map1 = (Map) asset.getData();
                map1 = (Map) map1.get("tableData");
                int b = Integer.parseInt(map1.get("id").toString());
                if (b > a) {
                    newAssets4.addAsset(asset);
                }
            }
            table6.setTableData(newAssets4);
            endTime = System.currentTimeMillis();//结束时间
            map.put("Driver查询>", (endTime - startTime + listtime));
            logger.info("Driver 查询id>" + a + "的时间：" + (endTime - startTime + listtime));

            listMaps.add(map);
        }
        result.setData(listMaps);
        buildSelectByDriverExecl(listMaps, "./selectAssetByDriver_" + a + ".xls");
        return result;
    }

    @Override
    public ParserResult selectMetadataExperiment(int total, int count) throws InterruptedException {
        ParserResult result = new ParserResult();

        bigchainDBRunner.StartConn();
        for (int j = 0; j < 20; j++) {
            double random = Math.random();
            int sum = (int) (random * total);
            List<Map> mapList = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                Map map = new HashMap();
                map.put("随机数", sum);

                /**
                 * BDQL查询全部表中数据
                 */
                result = bdqlUtil.work("select * from Person");
                map.put("QL查询全部数据", result.getMessage());
                logger.info("QL * 查询表中全部信息的时间：" + result.getMessage());

                Thread.sleep(1000);

                result = bdqlUtil.work("select * from Person where FirstName=" + sum);
                map.put("QL查询=", result.getMessage());
                logger.info("QL 查询FirstName=" + sum + "的时间：" + result.getMessage());

                Thread.sleep(1000);

                result = bdqlUtil.work("select * from Person where FirstName<" + sum);
                map.put("QL查询<", result.getMessage());
                logger.info("QL 查询FirstName<" + sum + "的时间：" + result.getMessage());

                Thread.sleep(1000);

                result = bdqlUtil.work("select * from Person where FirstName<=" + sum);
                map.put("QL查询<=", result.getMessage());
                logger.info("QL 查询FirstName<=" + sum + "的时间：" + result.getMessage());

                Thread.sleep(1000);


                result = bdqlUtil.work("select * from Person where FirstName>" + sum);
                map.put("QL查询>", result.getMessage());
                logger.info("QL 查询FirstName>" + sum + "的时间：" + result.getMessage());

                Thread.sleep(1000);

                result = bdqlUtil.work("select * from Person where FirstName>=" + sum);
                map.put("QL查询>=", result.getMessage());
                logger.info("QL 查询FirstName>=" + sum + "的时间：" + result.getMessage());

                Thread.sleep(1000);


                mapList.add(map);
            }

            buildSelectExecl(mapList, "./selectMetadata_" + j + ".xls");
        }
        result.setMessage("已经完成");
        return result;
    }

    public ParserResult selectMetadataByDriverExperiment(int total, int count, int sum) throws InterruptedException {
        ParserResult result = new ParserResult();

        bigchainDBRunner.StartConn();

        List<Map> mapList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Map map = new HashMap();
            map.put("随机数", sum);

            long startTime = System.currentTimeMillis();//开始时间
            List list = new ArrayList();
            list.add("FirstName");
            list.add("SecondName");
            list.add("age");
            list.add("time");

            long endTime = System.currentTimeMillis();//结束时间
            long listtime = endTime - startTime;


            /**
             * Driver查询全部的数据
             */
            startTime = System.currentTimeMillis();//开始时间
            List<MetaData> metaDataList = bigchainDBUtil.getMetaDatasByKey("Person");
            Table table1 = new Table();
            table1.setType("TRANSFER");
            table1.setTableName("Person");
            table1.setColumnName(list);
            table1.setTableData(metaDataList);
            endTime = System.currentTimeMillis();//结束时间
            map.put("Driver查询全部数据", (endTime - startTime + listtime));
            logger.info("Driver 查询全部数据的时间：" + (endTime - startTime + listtime));

            Thread.sleep(1000);
            /**
             * Driver 条件查询 “=”
             */
            startTime = System.currentTimeMillis();//开始时间
            metaDataList = bigchainDBUtil.getMetaDatasByKey("People");
            Table table2 = new Table();
            table2.setTableName("People");
            table2.setType("TRANSFER");
            table2.setColumnName(list);

            List<MetaData> newMetaData = new ArrayList<>();
            for (MetaData metaData : metaDataList) {
                Map map1 = metaData.getMetadata();
                map1 = (Map) map1.get("tableData");
                int a = Integer.parseInt(map1.get("FirstName").toString());
                if (a == sum) {
                    newMetaData.add(metaData);
                }
            }
            table2.setTableData(newMetaData);
            endTime = System.currentTimeMillis();//结束时间
            map.put("Driver查询=", (endTime - startTime + listtime));
            logger.info("Driver 查询FirstName=" + sum + "的时间：" + (endTime - startTime + listtime));

            Thread.sleep(1000);
            /**
             * Driver 条件查询 “<=”
             */
            startTime = System.currentTimeMillis();//开始时间
            metaDataList = bigchainDBUtil.getMetaDatasByKey("People");
            Table table3 = new Table();
            table3.setTableName("People");
            table3.setType("TRANSFER");
            table3.setColumnName(list);

            List<MetaData> newMetaData1 = new ArrayList<>();
            for (MetaData metaData : metaDataList) {
                Map map1 = metaData.getMetadata();
                map1 = (Map) map1.get("tableData");
                int a = Integer.parseInt(map1.get("FirstName").toString());
                if (a <= sum) {
                    newMetaData1.add(metaData);
                }
            }
            table3.setTableData(newMetaData1);
            endTime = System.currentTimeMillis();//结束时间
            map.put("Driver查询<=", (endTime - startTime + listtime));
            logger.info("Driver 查询FirstName<=" + sum + "的时间：" + (endTime - startTime + listtime));

            Thread.sleep(1000);


            /**
             * Driver 条件查询 “>=”
             */
            startTime = System.currentTimeMillis();//开始时间
            metaDataList = bigchainDBUtil.getMetaDatasByKey("People");
            Table table4 = new Table();
            table4.setTableName("People");
            table4.setType("TRANSFER");
            table4.setColumnName(list);

            List<MetaData> newMetaData2 = new ArrayList<>();
            for (MetaData metaData : metaDataList) {
                Map map1 = metaData.getMetadata();
                map1 = (Map) map1.get("tableData");
                int a = Integer.parseInt(map1.get("FirstName").toString());
                if (a >= sum) {
                    newMetaData2.add(metaData);
                }
            }
            table4.setTableData(newMetaData2);
            endTime = System.currentTimeMillis();//结束时间
            map.put("Driver查询>=", (endTime - startTime + listtime));
            logger.info("Driver 查询FirstName>=" + sum + "的时间：" + (endTime - startTime + listtime));


            Thread.sleep(1000);
            /**
             * Driver 条件查询 “<”
             */
            startTime = System.currentTimeMillis();//开始时间
            metaDataList = bigchainDBUtil.getMetaDatasByKey("People");
            Table table5 = new Table();
            table5.setTableName("People");
            table5.setType("TRANSFER");
            table5.setColumnName(list);

            List<MetaData> newMetaData3 = new ArrayList<>();
            for (MetaData metaData : metaDataList) {
                Map map1 = metaData.getMetadata();
                map1 = (Map) map1.get("tableData");
                int a = Integer.parseInt(map1.get("FirstName").toString());
                if (a < sum) {
                    newMetaData3.add(metaData);
                }
            }
            table5.setTableData(newMetaData3);
            endTime = System.currentTimeMillis();//结束时间
            map.put("Driver查询<", (endTime - startTime + listtime));
            logger.info("Driver 查询FirstName<" + sum + "的时间：" + (endTime - startTime + listtime));

            Thread.sleep(1000);
            /**
             * Driver 条件查询 “>”
             */
            startTime = System.currentTimeMillis();//开始时间
            metaDataList = bigchainDBUtil.getMetaDatasByKey("People");
            Table table6 = new Table();
            table6.setTableName("People");
            table6.setType("TRANSFER");
            table6.setColumnName(list);

            List<MetaData> newMetaData4 = new ArrayList<>();
            for (MetaData metaData : metaDataList) {
                Map map1 = metaData.getMetadata();
                map1 = (Map) map1.get("tableData");
                int a = Integer.parseInt(map1.get("FirstName").toString());
                if (a > sum) {
                    newMetaData4.add(metaData);
                }
            }
            table6.setTableData(newMetaData4);
            endTime = System.currentTimeMillis();//结束时间
            map.put("Driver查询>", (endTime - startTime + listtime));
            logger.info("Driver 查询FirstName>" + sum + "的时间：" + (endTime - startTime + listtime));

            Thread.sleep(1000);
            mapList.add(map);
        }

        result.setData(mapList);
        buildSelectByDriverExecl(mapList, "./selectMetadataByDriver_" + sum + ".xls");
        return result;
    }

    public ParserResult test() {
        ParserResult result = new ParserResult();

        bigchainDBRunner.StartConn("http://192.168.1.102:9984");
        result = bdqlUtil.work("UPDATE Person SET FirstName = '" + 8000 + "' , SecondName='" + (8001) + "',age= '" + (8001) + "',time='" + (8001) + "' WHERE ID='f3d9f405ab35266da01605bf205eaae2575f4249e5dc4b2c406bd0e8f5c79dc2'");
        String TXid = (String) result.getData();
        for (; true; ) {
            if (bigchainDBUtil.checkTransactionExit(TXid)) {
                break;
            }
        }
        logger.info("hello");
        return null;

    }


    public void selectAsset(int total) throws InterruptedException {
        bigchainDBRunner.StartConn();

        for (int i = 0; i < 20; i++) {
            double random = Math.random();
            int sum = (int) (random * total);
            List<Map> listMaps = new ArrayList<>();
            for (int j = 0; j < 20; j++) {
                Map map = new HashMap();
                map.put("随机数", sum);

                /**
                 * BDQL查询全部表中数据
                 */
                long start=System.currentTimeMillis();
                ParserResult result = bdqlUtil.work("select * from Computer");
                long end=System.currentTimeMillis();
                String str = result.getMessage();
                String sql = str.split(",")[0];
                String driver = str.split(",")[1];

                map.put("Driver查询全部数据", driver);
                logger.info("Driver查询全部信息的时间：" + driver);
                map.put("QL查询全部数据", sql);
                logger.info("QL * 查询表中全部信息的时间：" + sql);
                map.put("QL查询1全部数据",""+(end-start));
                logger.info("QL查询1全部数据的时间"+(end-start));

                Thread.sleep(1000);
                start=System.currentTimeMillis();
                result = bdqlUtil.work("select * from Computer where id=" + sum);
                end=System.currentTimeMillis();
                str = result.getMessage();
                sql = str.split(",")[0];
                driver = str.split(",")[1];

                map.put("Driver查询=", driver);
                logger.info("Driver查询=的时间：" + driver);
                map.put("QL查询=", sql);
                logger.info("QL 查询id=" + sum + "的时间：" + sql);
                map.put("QL查询1=",""+(end-start));
                logger.info("QL查询1=的时间"+(end-start));


                Thread.sleep(1000);
                start=System.currentTimeMillis();
                result = bdqlUtil.work("select * from Computer where id<" + sum);
                end=System.currentTimeMillis();
                str = result.getMessage();
                sql = str.split(",")[0];
                driver = str.split(",")[1];

                map.put("Driver查询<", driver);
                logger.info("Driver查询<的时间：" + driver);
                map.put("QL查询<", sql);
                logger.info("QL 查询id<" + sum + "的时间：" + sql);
                map.put("QL查询1<",""+(end-start));
                logger.info("QL查询1<的时间"+(end-start));


                Thread.sleep(1000);

                start=System.currentTimeMillis();
                result = bdqlUtil.work("select * from Computer where id<=" + sum);
                end=System.currentTimeMillis();
                str = result.getMessage();
                sql = str.split(",")[0];
                driver = str.split(",")[1];

                map.put("Driver查询<=", driver);
                logger.info("Driver查询<=的时间：" + driver);
                map.put("QL查询<=", sql);
                logger.info("QL 查询id<=" + sum + "的时间：" + sql);
                map.put("QL查询1<=",""+(end-start));
                logger.info("QL查询1<=的时间"+(end-start));


                Thread.sleep(1000);
                start=System.currentTimeMillis();
                result = bdqlUtil.work("select * from Computer where id>" + sum);
                end=System.currentTimeMillis();
                str = result.getMessage();
                sql = str.split(",")[0];
                driver = str.split(",")[1];

                map.put("Driver查询>", driver);
                logger.info("Driver查询>的时间：" + driver);
                map.put("QL查询>", sql);
                logger.info("QL 查询id>" + sum + "的时间：" + sql);
                map.put("QL查询1>",""+(end-start));
                logger.info("QL查询1>的时间"+(end-start));

                Thread.sleep(1000);
                start=System.currentTimeMillis();
                result = bdqlUtil.work("select * from Computer where id>=" + sum);
                end=System.currentTimeMillis();
                str = result.getMessage();
                sql = str.split(",")[0];
                driver = str.split(",")[1];

                map.put("Driver查询>=", driver);
                logger.info("Driver查询>=的时间：" + driver);
                map.put("QL查询>=", sql);
                logger.info("QL 查询id>=" + sum + "的时间：" + sql);
                map.put("QL查询1>=",""+(end-start));
                logger.info("QL查询1>=的时间"+(end-start));

                listMaps.add(map);
            }

            buildSelect(listMaps, "./Asset" + i + ".xls");
        }

    }

    public void selectMetadata(int total) throws InterruptedException {
        bigchainDBRunner.StartConn();
        for (int j = 0; j < 20; j++) {
            double random = Math.random();
            int sum = (int) (random * total);
            List<Map> mapList = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                Map map = new HashMap();
                map.put("随机数", sum);

                /**
                 * BDQL查询全部表中数据
                 */
                long start=System.currentTimeMillis();
                ParserResult result = bdqlUtil.work("select * from Person");
                long end=System.currentTimeMillis();
                String str = result.getMessage();
                String sql = str.split(",")[0];
                String driver = str.split(",")[1];

                map.put("Driver查询全部数据", driver);
                logger.info("Driver查询全部信息的时间：" + driver);
                map.put("QL查询全部数据", sql);
                logger.info("QL * 查询表中全部信息的时间：" + sql);
                map.put("QL查询1全部数据",""+(end-start));
                logger.info("QL查询1全部数据的时间"+(end-start));


                Thread.sleep(1000);
                start=System.currentTimeMillis();
                result = bdqlUtil.work("select * from Person where FirstName=" + sum);
                end=System.currentTimeMillis();
                str = result.getMessage();
                sql = str.split(",")[0];
                driver = str.split(",")[1];

                map.put("Driver查询=", driver);
                logger.info("Driver查询=的时间：" + driver);
                map.put("QL查询=", sql);
                logger.info("QL 查询FirstName=" + sum + "的时间：" + sql);
                map.put("QL查询1=", ""+(end-start));
                logger.info("QL查询1=" + sum + "的时间：" + (end-start));

                Thread.sleep(1000);

                start=System.currentTimeMillis();
                result = bdqlUtil.work("select * from Person where FirstName<" + sum);
                end=System.currentTimeMillis();
                str = result.getMessage();
                sql = str.split(",")[0];
                driver = str.split(",")[1];

                map.put("Driver查询<", driver);
                logger.info("Driver查询<的时间：" + driver);
                map.put("QL查询<", sql);
                logger.info("QL 查询FirstName<" + sum + "的时间：" + sql);
                map.put("QL查询1<", ""+(end-start));
                logger.info("QL查询1<" + sum + "的时间：" + (end-start));

                Thread.sleep(1000);

                start=System.currentTimeMillis();
                result = bdqlUtil.work("select * from Person where FirstName<=" + sum);
                end=System.currentTimeMillis();
                str = result.getMessage();
                sql = str.split(",")[0];
                driver = str.split(",")[1];

                map.put("Driver查询<=", driver);
                logger.info("Driver查询<=的时间：" + driver);
                map.put("QL查询<=", sql);
                logger.info("QL 查询FirstName<=" + sum + "的时间：" + sql);
                map.put("QL查询1<=", ""+(end-start));
                logger.info("QL查询1<=" + sum + "的时间：" + (end-start));

                Thread.sleep(1000);

                start=System.currentTimeMillis();
                result = bdqlUtil.work("select * from Person where FirstName>" + sum);
                end=System.currentTimeMillis();
                str = result.getMessage();
                sql = str.split(",")[0];
                driver = str.split(",")[1];

                map.put("Driver查询>", driver);
                logger.info("Driver查询>的时间：" + driver);
                map.put("QL查询>", sql);
                logger.info("QL 查询FirstName>" + sum + "的时间：" + sql);
                map.put("QL查询1>", ""+(end-start));
                logger.info("QL查询1>" + sum + "的时间：" + (end-start));

                Thread.sleep(1000);

                start=System.currentTimeMillis();
                result = bdqlUtil.work("select * from Person where FirstName>=" + sum);
                end=System.currentTimeMillis();
                str = result.getMessage();
                sql = str.split(",")[0];
                driver = str.split(",")[1];

                map.put("Driver查询>=", driver);
                logger.info("Driver查询>=的时间：" + driver);
                map.put("QL查询>=", sql);
                logger.info("QL 查询FirstName>=" + sum + "的时间：" + sql);
                map.put("QL查询1>=", ""+(end-start));
                logger.info("QL查询1>=" + sum + "的时间：" + (end-start));


                Thread.sleep(1000);


                mapList.add(map);
            }

            buildSelect(mapList, "./selectMetadata_" + j + ".xls");
        }

    }


    private void buildExecl(String title, List list) {
        File file = new File("./data.xls");
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            WritableWorkbook writableWorkbook = Workbook.createWorkbook(file);
            WritableSheet sheet = writableWorkbook.createSheet("sheet1", 0);
            Label label = new Label(1, 0, title);
            Label label2 = new Label(0, 0, "id");
            sheet.addCell(label);
            sheet.addCell(label2);

            for (int i = 1; i <= list.size(); i++) {
                Label label1 = new Label(1, i, list.get(i - 1).toString());
                sheet.addCell(label1);
                Label label3 = new Label(0, i, i + "");
                sheet.addCell(label3);
            }
            writableWorkbook.write();    //写入数据
            writableWorkbook.close();  //关闭连接
            logger.info("成功写入文件，请前往查看文件！");
        } catch (Exception e) {
            logger.info("文件写入失败，报异常...");
        }


    }

    private void buildSelectExecl(List<Map> list, String name) {
        File file = new File(name);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            WritableWorkbook writableWorkbook = Workbook.createWorkbook(file);
            WritableSheet sheet = writableWorkbook.createSheet("sheet1", 0);
            Label label = new Label(1, 0, "随机数");
            Label label2 = new Label(0, 0, "id");
            Label label3 = new Label(2, 0, "QL查询全部数据");
            Label label5 = new Label(3, 0, "QL查询=");
            Label label7 = new Label(4, 0, "QL查询>");
            Label label9 = new Label(5, 0, "QL查询>=");
            Label label11 = new Label(6, 0, "QL查询<");
            Label label13 = new Label(7, 0, "QL查询<=");
            sheet.addCell(label);
            sheet.addCell(label2);
            sheet.addCell(label3);
            sheet.addCell(label5);
            sheet.addCell(label7);
            sheet.addCell(label9);
            sheet.addCell(label11);
            sheet.addCell(label13);

            for (int j = 0; j < list.size(); j++) {
                Map map = list.get(j);
                Label data = new Label(0, j + 1, "" + (j + 1));
                sheet.addCell(data);
                data = new Label(1, j + 1, map.get("随机数").toString());
                sheet.addCell(data);
                data = new Label(2, j + 1, map.get("QL查询全部数据").toString());
                sheet.addCell(data);
                data = new Label(3, j + 1, map.get("QL查询=").toString());
                sheet.addCell(data);
                data = new Label(4, j + 1, map.get("QL查询>").toString());
                sheet.addCell(data);
                data = new Label(5, j + 1, map.get("QL查询>=").toString());
                sheet.addCell(data);
                data = new Label(6, j + 1, map.get("QL查询<").toString());
                sheet.addCell(data);
                data = new Label(7, j + 1, map.get("QL查询<=").toString());
                sheet.addCell(data);
            }
            writableWorkbook.write();    //写入数据
            writableWorkbook.close();  //关闭连接
            logger.info("成功写入文件，请前往查看文件！");
        } catch (Exception e) {
            logger.info("文件写入失败，报异常...");
        }
    }

    private void buildSelect(List<Map> list, String name) {
        File file = new File(name);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            WritableWorkbook writableWorkbook = Workbook.createWorkbook(file);
            WritableSheet sheet = writableWorkbook.createSheet("sheet1", 0);
            Label label = new Label(1, 0, "随机数");
            sheet.addCell(label);
            label = new Label(0, 0, "id");
            sheet.addCell(label);
            label = new Label(2, 0, "QL查询全部数据");
            sheet.addCell(label);
            label = new Label(3, 0, "QL查询1全部数据");
            sheet.addCell(label);
            label = new Label(4, 0, "Driver查询全部数据");
            sheet.addCell(label);
            label = new Label(5, 0, "QL查询=");
            sheet.addCell(label);
            label = new Label(6, 0, "QL查询1=");
            sheet.addCell(label);
            label = new Label(7, 0, "Driver查询=");
            sheet.addCell(label);
            label = new Label(8, 0, "QL查询>");
            sheet.addCell(label);
            label = new Label(9, 0, "QL查询1>");
            sheet.addCell(label);
            label = new Label(10, 0, "Driver查询>");
            sheet.addCell(label);
            label = new Label(11, 0, "QL查询>=");
            sheet.addCell(label);
            label = new Label(12, 0, "QL查询1>=");
            sheet.addCell(label);
            label = new Label(13, 0, "Driver查询>=");
            sheet.addCell(label);
            label = new Label(14, 0, "QL查询<");
            sheet.addCell(label);
            label = new Label(15, 0, "QL查询1<");
            sheet.addCell(label);
            label = new Label(16, 0, "Driver查询<");
            sheet.addCell(label);
            label = new Label(17, 0, "QL查询<=");
            sheet.addCell(label);
            label = new Label(18, 0, "QL查询1<=");
            sheet.addCell(label);
            label = new Label(19, 0, "Driver查询<=");
            sheet.addCell(label);


            for (int j = 0; j < list.size(); j++) {
                Map map = list.get(j);
                Label data = new Label(0, j + 1, "" + (j + 1));
                sheet.addCell(data);
                data = new Label(1, j + 1, map.get("随机数").toString());
                sheet.addCell(data);
                data = new Label(2, j + 1, map.get("QL查询全部数据").toString());
                sheet.addCell(data);
                data = new Label(3, j + 1, map.get("QL查询1全部数据").toString());
                sheet.addCell(data);
                data = new Label(4, j + 1, map.get("Driver查询全部数据").toString());
                sheet.addCell(data);
                data = new Label(5, j + 1, map.get("QL查询=").toString());
                sheet.addCell(data);
                data = new Label(6, j + 1, map.get("QL查询1=").toString());
                sheet.addCell(data);
                data = new Label(7, j + 1, map.get("Driver查询=").toString());
                sheet.addCell(data);
                data = new Label(8, j + 1, map.get("QL查询>").toString());
                sheet.addCell(data);
                data = new Label(9, j + 1, map.get("QL查询1>").toString());
                sheet.addCell(data);
                data = new Label(10, j + 1, map.get("Driver查询>").toString());
                sheet.addCell(data);
                data = new Label(11, j + 1, map.get("QL查询>=").toString());
                sheet.addCell(data);
                data = new Label(12, j + 1, map.get("QL查询1>=").toString());
                sheet.addCell(data);
                data = new Label(13, j + 1, map.get("Driver查询>=").toString());
                sheet.addCell(data);
                data = new Label(14, j + 1, map.get("QL查询<").toString());
                sheet.addCell(data);
                data = new Label(15, j + 1, map.get("QL查询1<").toString());
                sheet.addCell(data);
                data = new Label(16, j + 1, map.get("Driver查询<").toString());
                sheet.addCell(data);
                data = new Label(17, j + 1, map.get("QL查询<=").toString());
                sheet.addCell(data);
                data = new Label(18, j + 1, map.get("QL查询1<=").toString());
                sheet.addCell(data);
                data = new Label(19, j + 1, map.get("Driver查询<=").toString());
                sheet.addCell(data);
            }
            writableWorkbook.write();    //写入数据
            writableWorkbook.close();  //关闭连接
            logger.info("成功写入文件，请前往查看文件！");
        } catch (Exception e) {
            logger.info("文件写入失败，报异常...");
        }
    }

    private void buildSelectByDriverExecl(List<Map> list, String name) {
        File file = new File(name);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            WritableWorkbook writableWorkbook = Workbook.createWorkbook(file);
            WritableSheet sheet = writableWorkbook.createSheet("sheet1", 0);
            Label label = new Label(1, 0, "随机数");
            Label label2 = new Label(0, 0, "id");
            Label label4 = new Label(2, 0, "Driver查询全部数据");
            Label label6 = new Label(3, 0, "Driver查询=");
            Label label8 = new Label(4, 0, "Driver查询>");
            Label label10 = new Label(5, 0, "Driver查询>=");
            Label label12 = new Label(6, 0, "Driver查询<");
            Label label14 = new Label(7, 0, "Driver查询<=");
            sheet.addCell(label);
            sheet.addCell(label2);
            sheet.addCell(label4);
            sheet.addCell(label6);
            sheet.addCell(label8);
            sheet.addCell(label10);
            sheet.addCell(label12);
            sheet.addCell(label14);

            for (int j = 0; j < list.size(); j++) {
                Map map = list.get(j);
                Label data = new Label(0, j + 1, "" + (j + 1));
                sheet.addCell(data);
                data = new Label(1, j + 1, map.get("随机数").toString());
                sheet.addCell(data);

                data = new Label(2, j + 1, map.get("Driver查询全部数据").toString());
                sheet.addCell(data);
                data = new Label(3, j + 1, map.get("Driver查询=").toString());
                sheet.addCell(data);
                data = new Label(4, j + 1, map.get("Driver查询>").toString());
                sheet.addCell(data);
                data = new Label(5, j + 1, map.get("Driver查询>=").toString());
                sheet.addCell(data);
                data = new Label(6, j + 1, map.get("Driver查询<").toString());
                sheet.addCell(data);
                data = new Label(7, j + 1, map.get("Driver查询<=").toString());
                sheet.addCell(data);
            }
            writableWorkbook.write();    //写入数据
            writableWorkbook.close();  //关闭连接
            logger.info("成功写入文件，请前往查看文件！");
        } catch (Exception e) {
            logger.info("文件写入失败，报异常...");
        }
    }

    public static void main(String[] args) throws IOException {
        BDQLUtil bdqlUtil = new BDQLUtil();
        BigchainDBUtil bigchainDBUtil = new BigchainDBUtil();
        BigchainDBRunner bigchainDBRunner = new BigchainDBRunner();
        bigchainDBRunner.StartConn("http://192.168.1.102:9984");
        ParserResult result = bdqlUtil.work("UPDATE Person SET FirstName = '" + 8000 + "' , SecondName='" + (8001) + "',age= '" + (8001) + "',time='" + (8001) + "' WHERE ID='f3d9f405ab35266da01605bf205eaae2575f4249e5dc4b2c406bd0e8f5c79dc2'");
        String TXid = (String) result.getData();
        for (; true; ) {
            if (bigchainDBUtil.checkTransactionExit(TXid)) {
                break;
            }
        }
        logger.info("hello");
    }
}
