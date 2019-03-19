package com.keer.graduation.Service.Implement;

import com.bigchaindb.model.Assets;
import com.keer.graduation.BDQLParser.BDQLUtil;
import com.keer.graduation.Bigchaindb.BigchainDBRunner;
import com.keer.graduation.Bigchaindb.BigchainDBUtil;
import com.keer.graduation.Bigchaindb.KeyPairHolder;
import com.keer.graduation.Domain.MetaData;
import com.keer.graduation.Domain.ParserResult;
import com.keer.graduation.Domain.Table;
import com.keer.graduation.Service.IService;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BigchainDBServiceImp implements IService {
    private static Logger logger = LoggerFactory.getLogger(BigchainDBServiceImp.class);


    /**
     * 获取秘钥
     *
     * @return
     */
    @Override
    public ParserResult getKey() {
        ParserResult parserResult = new ParserResult();
        if (KeyPairHolder.getKeyPairFormTXT() != null) {
            parserResult.setData(KeyPairHolder.getKeyPairFormTXT());
            parserResult.setStatus(ParserResult.SUCCESS);
            parserResult.setMessage("获取秘钥成功！！！！！");
            logger.info("获取秘钥成功！！");
            return parserResult;
        } else {
            parserResult.setData(null);
            parserResult.setStatus(ParserResult.ERROR);
            parserResult.setMessage("获取秘钥失敗！！！！！");
            logger.info("获取秘钥失敗！！");
            return parserResult;
        }
    }

    @Override
    public ParserResult startConn(String url) {
        ParserResult parserResult = new ParserResult();
        if (BigchainDBRunner.StartConn(url)) {
            parserResult.setMessage("连接BigchainDB节点成功！！！");
            parserResult.setStatus(ParserResult.SUCCESS);
            parserResult.setData(true);
        } else {
            parserResult.setMessage("连接BigchainDB节点失败成功！");
            parserResult.setStatus(ParserResult.ERROR);
            parserResult.setData(false);
        }
        return parserResult;
    }

    @Override
    public ParserResult getCloumnsName(String key) {
        ParserResult parserResult = new ParserResult();
        Map<String, Table> map = null;
        try {
            map = BDQLUtil.getAlltablesByPubKey(KeyPairHolder.pubKeyToString((EdDSAPublicKey) KeyPairHolder.getKeyPairFromString(key).getPublic()));
        } catch (IOException e) {
            e.printStackTrace();
            parserResult.setData(null);
            parserResult.setMessage("表名获取失败！！");
            parserResult.setStatus(ParserResult.ERROR);
            return parserResult;
        }
        List<Map> list = buildJstreeData(map);
        parserResult.setData(list);
        parserResult.setMessage("表名获取成功！！");
        parserResult.setStatus(ParserResult.SUCCESS);
        return parserResult;
    }

    @Override
    public ParserResult getTableData(String key, String operation) {
        ParserResult parserResult = new ParserResult();
        Table table = new Table();
        table.setTableName(key);
        if (operation.equals("asset")) {
            Assets assets = BigchainDBUtil.getAssetByKey(key);

            if (assets != null) {
                table.setTableDataWithColumnName(assets);
                table.setType("CREATE");
            } else {
                parserResult.setData(null);
                parserResult.setMessage("查询表数据错误！！！");
                parserResult.setStatus(ParserResult.ERROR);
                return parserResult;//TODO 错误
            }
        } else {
            List<MetaData> metaDataList = BigchainDBUtil.getMetaDatasByKey(key);
            if (metaDataList != null) {
                table.setTableDataWithCloumnName(metaDataList);
                table.setType("TRANSFER");
            } else {
                parserResult.setData(null);
                parserResult.setMessage("查询表数据错误！！！");
                parserResult.setStatus(ParserResult.ERROR);
                return parserResult;//TODO 错误
            }
        }

        Map map = buildjqGridData(table);
        parserResult.setStatus(ParserResult.SUCCESS);
        parserResult.setMessage("表：" + key + "  数据查询成功！！");
        parserResult.setData(map);
        return parserResult;
    }

    @Override
    public ParserResult runBDQL(String BDQL) {
        ParserResult parserResult = BDQLUtil.work(BDQL);
        if (parserResult.getMessage().equals("select")) {
            parserResult.setData(buildjqGridData((Table) parserResult.getData()));
        }
        return parserResult;


    }

    private List<Map> buildJstreeData(Map<String, Table> map) {
        List<Map> list = new ArrayList<>();
        Map bigchaindb = new HashMap();
        bigchaindb.put("id", "bigchaindb");
        bigchaindb.put("parent", "#");
        bigchaindb.put("text", "bigchaindb");

        Map asset = new HashMap();
        asset.put("id", "asset");
        asset.put("parent", "bigchaindb");
        asset.put("text", "asset");

        Map metadata = new HashMap();
        metadata.put("id", "metadata");
        metadata.put("parent", "bigchaindb");
        metadata.put("text", "metadata");

        list.add(bigchaindb);
        list.add(asset);
        list.add(metadata);
        for (String key : map.keySet()) {
            Map node = new HashMap();
            if (map.get(key).getType().equals("CREATE")) {
                node.put("id", key);
                node.put("parent", "asset");
                node.put("text", key);
            } else {
                node.put("id", key);
                node.put("parent", "metadata");
                node.put("text", key);
            }
            list.add(node);
        }
        return list;
    }


    private Map buildjqGridData(Table table) {
        List cloumNames = table.getColumnName();
        List data = table.getData();
        List cloumAttr = new ArrayList();
        for (Object cloumName : cloumNames) {
            Map map = new HashMap<>();
            map.put("name", cloumName.toString());
            map.put("index", cloumName.toString());
            map.put("width", 60);
            cloumAttr.add(map);
        }
        Map map = new HashMap();
        map.put("cloumNames", cloumNames);
        map.put("data", data);
        map.put("cloumAttr", cloumAttr);
        return map;
    }
}
