package com.keer.graduation.Bigchaindb;

import com.alibaba.fastjson.JSON;
import com.bigchaindb.api.AssetsApi;
import com.bigchaindb.api.OutputsApi;
import com.bigchaindb.api.TransactionsApi;
import com.bigchaindb.builders.BigchainDbTransactionBuilder;
import com.bigchaindb.constants.BigchainDbApi;
import com.bigchaindb.constants.Operations;


import com.bigchaindb.model.*;
import com.bigchaindb.util.NetworkUtils;
import com.google.gson.JsonSyntaxException;

import com.keer.graduation.BDQLParser.BDQLUtil;
import com.keer.graduation.Domain.BigchainDBData;
import com.keer.graduation.Domain.MetaData;
import com.keer.graduation.Domain.Table;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
@Component
public class BigchainDBUtil {
    private static Logger logger = LoggerFactory.getLogger(BigchainDBUtil.class);


    @Autowired
    KeyPairHolder keyPairHolder;


    /**
     * 创建资产数据，没有metadata数据
     *
     * @param assetDate
     * @return
     * @throws Exception
     */
    public  String createAsset(BigchainDBData assetDate) throws Exception {
        return createAsset(assetDate, null);
    }

    /**
     * 创建资产和metadata
     *
     * @param assetWrapper
     * @param metadataWrapper
     * @return
     * @throws Exception
     */
    public  String createAsset(BigchainDBData assetWrapper, BigchainDBData metadataWrapper) throws Exception {

        Transaction createTransaction = BigchainDbTransactionBuilder
                .init()
                .operation(Operations.CREATE)
                .addAssets(assetWrapper, assetWrapper.getClass())
                .addMetaData(metadataWrapper)
                .buildAndSign(
                        keyPairHolder.getPublic(),
                        keyPairHolder.getPrivate())
                .sendTransaction();
        return createTransaction.getId();
    }


//    /**
//     * obtain a asset use assetId.
//     * @param assetId  aseetId
//     * @return assetObject
//     * @throws IOException
//     * @throws ClassNotFoundException
//     * @throws InvocationTargetException
//     * @throws IllegalAccessException
//     * @throws InstantiationException
//     */
//    public static Object getAsset(String assetId) throws IOException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException {
//        Transaction createTransaction = getCreateTransaction(assetId);
//        if (createTransaction == null) {
//            return null;
//        }
//        Asset asset = createTransaction.getAsset();
//        LinkedTreeMap assetData = (LinkedTreeMap) asset.getData();
//
//        return bigchaindbDataToBean(assetData);
//    }
//
//    /**
//     * obtain a explicit type asset use assetId.
//     * @param assetId assetId
//     * @param type asset Type class
//     * @param <T> asset Type
//     * @return assetObject
//     * @throws IOException
//     * @throws ClassNotFoundException
//     * @throws InvocationTargetException
//     * @throws IllegalAccessException
//     * @throws InstantiationException
//     */
//    public static <T> T getAsset(String assetId, Class<T> type) throws IOException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException {
//        Objects.requireNonNull(type);
//
//        Transaction createTransaction = getCreateTransaction(assetId);
//        if (createTransaction == null) {
//            return null;
//        }
//        Asset asset = createTransaction.getAsset();
//        LinkedTreeMap assetData = (LinkedTreeMap) asset.getData();
//
//
//        Object bean = bigchaindbDataToBean(assetData);
//
//        if (type.isInstance(bean)) {
//            return (T) bean;
//        } else {
//            return null;
//        }
//    }
//
//    /**
//     * obtain a explicit type asset use assetId.
//     * @param transactionId assetId
//     * @param type asset Type class
//     * @param <T> asset Type
//     * @return assetObject
//     * @throws IOException
//     * @throws ClassNotFoundException
//     * @throws InvocationTargetException
//     * @throws IllegalAccessException
//     * @throws InstantiationException
//     */
//    public static <T> T getAssetByTransactionId(String transactionId, Class<T> type) throws IOException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException {
//        Objects.requireNonNull(type);
//
//        Transaction transaction = TransactionsApi.getTransactionById(transactionId);
//        if (transaction == null) {
//            return null;
//        }
//        Asset asset = transaction.getAsset();
//        LinkedTreeMap assetData = (LinkedTreeMap) asset.getData();
//
//        Object bean = bigchaindbDataToBean(assetData);
//
//        if (type.isInstance(bean)) {
//            return (T) bean;
//        } else {
//            return null;
//        }
//    }
//
//
//
//    /**
//     * convert bigchaindb LinkedTreeMap data to java object.
//     * @param bigchaindbData
//     * @return
//     * @throws ClassNotFoundException
//     * @throws IllegalAccessException
//     * @throws InstantiationException
//     * @throws InvocationTargetException
//     */
//    public static Object bigchaindbDataToBean(LinkedTreeMap bigchaindbData) throws ClassNotFoundException {
//        String type = (String) bigchaindbData.get("type");
//        Object data = bigchaindbData.get("data");
//        if (data instanceof LinkedTreeMap) {
//            LinkedTreeMap properties = (LinkedTreeMap) bigchaindbData.get("data");
//            String json = JsonUtils.toJson(properties);
//            Object bean = JsonUtils.fromJson(json, ClassUtils.getClass(type));
//            return bean;
//        } else {
//            return data;
//        }
//    }
////
//    /**
//     * determine whether asset exist.
//     *
//     * @param assetId asset Id
//     * @return whether asset exist
//     */
//    public static boolean assetIsExist(String assetId) {
//        try {
//            Object asset = getAsset(assetId);
//            return asset != null;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    /**
//     * determine whether asset with specify type exist.
//     * @param assetId asset Id
//     * @param type type class
//     * @param <T> type
//     * @return
//     */
//    public static <T> boolean assetIsExist(String assetId, Class<T> type) {
//
//        try {
//            T asset = getAsset(assetId, type);
//            return asset != null;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//

    /**
     * 给资产增加metadata信息
     * <p>
     * youself is KeyPairHolder.getKeyPair() representive
     *
     * @param metaData
     * @param assetId
     * @return
     * @throws Exception
     */
    public  String transferToSelf(BigchainDBData metaData, String assetId) {

        Transaction transferTransaction = null;
        try {
            transferTransaction = BigchainDbTransactionBuilder
                    .init()
                    .operation(Operations.TRANSFER)
                    .addAssets(assetId, String.class)
                    .addMetaData(metaData)
                    .addInput(null, transferToSelfFulFill(assetId), keyPairHolder.getPublic())
                    .addOutput("1", keyPairHolder.getPublic())
                    .buildAndSign(
                            keyPairHolder.getPublic(),
                            keyPairHolder.getPrivate())
                    .sendTransaction();
        } catch (Exception e) {
            logger.error("资产ID：" + assetId + ",不存在!!!!!!!");
            return null;
        }
        return transferTransaction.getId();
    }
//
//    /**
//     * transfer asset to publicKeyHexTo.
//     * @param assetId
//     * @param publicKey
//     * @return
//     * @throws Exception
//     */
//    public static String transferTo(String assetId, PublicKey publicKey, BigchaindbData bigchaindbData) throws Exception {
//
//        Transaction transferTransaction = BigchainDbTransactionBuilder
//                .init()
//                .operation(Operations.TRANSFER)
//                .addAssets(assetId, String.class)
//                .addMetaData(bigchaindbData)
//                .addInput(null, transferToSelfFulFill(assetId), KeyPairHolder.getPublic())
//                .addOutput("1", (EdDSAPublicKey)publicKey)
//                .buildAndSign(
//                        KeyPairHolder.getPublic(),
//                        KeyPairHolder.getPrivate())
//                .sendTransaction();
//        return transferTransaction.getId();
//    }
//

//
//    /**
//     * obtain all asset's all transaction.
//     * sort by transfer order
//     * @param assetId asset Id
//     * @return transactions
//     * @throws IOException
//     */
//    public static Transactions getTransactionsByAssetId(String assetId) throws IOException {
//        Transactions transactions = new Transactions();
//        Transactions createTransactions = TransactionsApi.getTransactionsByAssetId(assetId, Operations.CREATE);
//        for (Transaction create : createTransactions.getTransactions()) {
//            transactions.addTransaction(create);
//        }
//        Transactions transfers = TransactionsApi.getTransactionsByAssetId(assetId, Operations.TRANSFER);
//        for (Transaction transfer : transfers.getTransactions()) {
//            transactions.addTransaction(transfer);
//        }
//        return transactions;
//    }
//


//
//    /**
//     * obtain whole metadata.
//     *
//     * integrate ont asset's all transactions's metadata into one metadataObject.
//     * when duplicate attributes appear, use the first one.
//     *
//     * for example:
//     * 1.
//     * {
//     *     name: 'tom'
//     * }
//     * 2.
//     * {
//     *     age: 22
//     * }
//     * 3.
//     * {
//     *     gender: 'male'
//     * }
//     * 4.
//     * {
//     *     age: 44,
//     *     height: 172
//     * }
//     * In the end, the object is:
//     * {
//     *     name: 'tom',
//     *     age: 22,
//     *     gender: 'male',
//     *     height: 172
//     * }
//     *
//     * @param assetId asset Id
//     * @param type type class
//     * @param <T> type
//     * @return metadataObject
//     * @throws IOException
//     * @throws ClassNotFoundException
//     * @throws InstantiationException
//     * @throws IllegalAccessException
//     * @throws InvocationTargetException
//     * @throws IntrospectionException
//     */
//    public static <T> T getWholeMetaData(String assetId, Class<T> type) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, IntrospectionException {
//
//        List<T> metadatas = getMetaDatas(assetId, type);
//        if (CollectionUtils.isEmpty(metadatas)) {
//            return null;
//        }
//
//        BeanInfo beanInfo = Introspector.getBeanInfo(type);
//        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
//
//        T bean = type.newInstance();
//        for (T data : metadatas) {
//            for (PropertyDescriptor pd : pds) {
//                if (pd.getWriteMethod() == null || pd.getWriteMethod() == null) {
//                    continue;
//                }
//                Object retVal = pd.getReadMethod().invoke(data);
//                if (retVal != null) {
//                    if(retVal.getClass().equals(int.class)&&((int)retVal==0)){
//                        ;
//                    }else {
//                        pd.getWriteMethod().invoke(bean, retVal);
//                    }
//                }
//            }
//        }
//        return bean;
//    }
//
//    /**
//     * obtain metadatas
//     * @param assetId asset Id
//     * @param type type class
//     * @param <T> type
//     * @return metadataObjects
//     * @throws IOException
//     * @throws ClassNotFoundException
//     * @throws InvocationTargetException
//     * @throws InstantiationException
//     * @throws IllegalAccessException
//     */
//    public static <T> List<T> getMetaDatas(String assetId, Class<T> type) throws IOException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException {
//        List<T> metadatas = new ArrayList<>();
//
//        Transactions transactions = getTransactionsByAssetId(assetId);
//        for (Transaction transaction : transactions.getTransactions()) {
//            LinkedTreeMap metaDataMap = (LinkedTreeMap) transaction.getMetaData();
//            if (metaDataMap != null) {
//                Object bean = BigchaindbUtil.bigchaindbDataToBean(metaDataMap);
//                if (bean != null && type.isInstance(bean)) {
//                    metadatas.add((T) bean);
//                }
//            }
//        }
//        return metadatas;
//    }
//
//    public static <T> List<T> getAllAssets(String key, Class<T> type) throws IOException, ClassNotFoundException {
//        List<T> listAssets =new ArrayList<>();
//        Assets assets = AssetsApi.getAssets(key);
//        for(Asset asset:assets.getAssets()){
//            LinkedTreeMap assetMap=(LinkedTreeMap)asset.getData();
//            if (assetMap != null) {
//                Object bean = BigchainDBUtil.bigchaindbDataToBean(assetMap);
//                if (bean != null && type.isInstance(bean)) {
//                    listAssets.add((T) bean);
//                }
//            }
//        }
//        return listAssets;
//
//    }
//

    /**
     * 通过资产id获取最后交易输出
     *
     * @param assetId
     * @return
     * @throws IOException
     */
    private  FulFill transferToSelfFulFill(String assetId) throws IOException {
        final FulFill spendFrom = new FulFill();
        String transactionId = getLastTransactionId(assetId);
        spendFrom.setTransactionId(transactionId);
        spendFrom.setOutputIndex(0);
        return spendFrom;
    }

    /**
     * 通过资产id获取最后交易id
     *
     * @param assetId asset Id
     * @return last transaction id
     * @throws IOException
     */
    public  String getLastTransactionId(String assetId) throws IOException {
        return getTransactionId(getLastTransaction(assetId));
    }

    /**
     * 通过资产id获得最后交易信息
     *
     * @param assetId assetId
     * @return last transaction
     * @throws IOException
     */
    public  Transaction getLastTransaction(String assetId) throws IOException {
        List<Transaction> transfers = TransactionsApi.getTransactionsByAssetId(assetId, Operations.TRANSFER).getTransactions();

        if (transfers != null && transfers.size() > 0) {
            return transfers.get(transfers.size() - 1);
        } else {
            return getCreateTransaction(assetId);
        }
    }

    /**
     * 通过资产id得到transaction（create）
     *
     * @param assetId
     * @return
     * @throws IOException
     */
    public  Transaction getCreateTransaction(String assetId) throws IOException {
        try {
            Transactions apiTransactions = TransactionsApi.getTransactionsByAssetId(assetId, Operations.CREATE);

            List<Transaction> transactions = apiTransactions.getTransactions();
            if (transactions != null && transactions.size() == 1) {
                return transactions.get(0);
            } else {
                return null;
            }

        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    /**
     * Transaction获取交易id
     *
     * @param transaction
     * @return
     */
    private  String getTransactionId(Transaction transaction) {
        String withQuotationId = transaction.getId();
        return withQuotationId.substring(1, withQuotationId.length() - 1);
    }

    /**
     * 检查交易是否存在
     *
     * @param txID
     * @return
     */
    public  boolean checkTransactionExit(String txID) {
        try {
            Thread.sleep(2000);
            Transaction transaction = TransactionsApi.getTransactionById(txID);
            Thread.sleep(2000);
            if (!transaction.getId().equals(null)) {
                logger.info("交易存在！！ID：" + txID);
                return true;
            } else {
                logger.info("交易不存在！！ID：" + txID);
                return false;
            }
        } catch (Exception e) {
            logger.info("未知错误！！！");
            e.printStackTrace();
            return false;

        }

    }

    /**
     * 通过公钥获得全部交易
     *
     * @param publicKey
     * @return
     * @throws IOException
     */
    public  Transactions getAllTransactionByPubKey(String publicKey) throws IOException {
        Transactions transactions = new Transactions();
        Outputs outputs = OutputsApi.getOutputs(publicKey);
        for (Output output : outputs.getOutput()) {
            String assetId = output.getTransactionId();
            Transaction transaction = TransactionsApi.getTransactionById(assetId);
            transactions.addTransaction(transaction);
        }
        return transactions;
    }

    /**
     * 通过key查询资产
     *
     * @param key
     * @return
     */
    public  Assets getAssetByKey(String key) {
        try {
            return AssetsApi.getAssets(key);
        } catch (IOException e) {
            logger.error("未知错误！！！！！！！");
            return null;
        }
    }

    /**
     * 通过Key查询metadata
     *
     * @param key
     * @return
     */
//    public static MetaDatas getMetaDatasByKey(String key){
//        try {
//            return MetaDataApi.getMetaData(key);
//        } catch (IOException e) {
//            logger.error("未知错误！！！！！！！");
//            return null;
//        }
//    }
    public  List<MetaData> getMetaDatasByKey(String key) {
        logger.debug("getMetaData Call :" + key);
        Response response;
        String body = null;
        try {
            response = NetworkUtils.sendGetRequest(BigChainDBGlobals.getBaseUrl() + BigchainDbApi.METADATA + "/?search=" + key);
            body = response.body().string();
            response.close();
        } catch (Exception e) {
            logger.error("未知错误！！！！！！！");
            return null;
        }

        return  JSON.parseArray(body, MetaData.class);
    }

    public  Transaction getTransactionByTXID(String ID){
        logger.info("开始查询交易信息：TXID："+ID);
        try {
            logger.info("查询成功！！！！！！");
            return TransactionsApi.getTransactionById(ID);
        } catch (IOException e) {
            logger.error("交易不存在，TXID："+ID);
            return null;
        }
    }
//
//    /**
//     * 通过猪的id，类型查询交易id
//     * @param pigId
//     * @return
//     */
//    public static String getAssetId(String pigId,String type) {
//        String id = null;
//        try{
//            List<Asset> assets = AssetsApi.getAssets(pigId).getAssets();
//
//            LinkedTreeMap linkedTreeMap;
//            for(Asset asset : assets){
//                linkedTreeMap= (LinkedTreeMap) asset.getData();
//                if(linkedTreeMap.get("type").equals(type)){
//                    id=asset.getId();
//
//                }
//            }
//        }catch(Exception e){
//            return null;
//        }
//        return id;
//
//    }

    public static void main(String[] args) throws IOException {

//        BigchainDBRunner.StartConn();
//
//        Map<String, Table> result = null;
//        try {
//            result = BDQLUtil.getAlltablesByPubKey(KeyPairHolder.pubKeyToString(KeyPairHolder.getPublic()));
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        }
//        Object[] columnNames = result.get("Person").getColumnName().toArray();
//        logger.info(String.valueOf(columnNames.length));
//        List<Map> data = result.get("Person").getData();
//        List<Object[]> objects = new ArrayList<Object[]>();
//        for (Map map : data) {
//            Collection va = map.values();
//
//            Object[] a = va.toArray();
//            objects.add(a);
//        }
//
//        Object[][] b = (Object[][]) objects.toArray(new Object[data.size()][columnNames.length]);
//        logger.info("hhhh");
    }
}