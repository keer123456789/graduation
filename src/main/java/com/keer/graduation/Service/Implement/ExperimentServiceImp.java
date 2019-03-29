package com.keer.graduation.Service.Implement;

import com.bigchaindb.api.OutputsApi;
import com.bigchaindb.model.Outputs;
import com.keer.graduation.BDQLParser.BDQLUtil;
import com.keer.graduation.Bigchaindb.BigchainDBRunner;
import com.keer.graduation.Bigchaindb.BigchainDBUtil;
import com.keer.graduation.Bigchaindb.KeyPairHolder;
import com.keer.graduation.Domain.ParserResult;
import com.keer.graduation.Service.IExperimentService;
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
public class ExperimentServiceImp implements IExperimentService {
    private static Logger logger = LoggerFactory.getLogger(BigchainDBServiceImp.class);

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
     * @param assetTotal 实验个数
     * @return
     * @throws InterruptedException
     * @throws IOException
     */
    @Override
    public ParserResult insertExperiment(int assetTotal) throws InterruptedException, IOException {

        ParserResult result = new ParserResult();
        List<Long> insertTime=new ArrayList<>();

        long startTime=System.currentTimeMillis();//开始时间

        bigchainDBRunner.StartConn();
        for (int m = 0; m < assetTotal; m++) {
            long insertStartTime =System.currentTimeMillis();
            result = bdqlUtil.work("INSERT INTO Computer (id, ip,mac,size,cpu,ROM,RAM) VALUES ('" + (m + 1) + "','" + (m + 2) + "','Champs-Elysees','" + (m+ 3) + "','i7','" + (m + 4) + "','" + (m + 5) + "')");
            insertTime.add(System.currentTimeMillis()-insertStartTime);
            String id = (String) result.getData();
            logger.info("资产ID：" + id);
            Thread.sleep(500);

        }
        long endTime=System.currentTimeMillis();
        Double avgTime=0.0;
        long total=0;
        for (Long a:insertTime){
            total=total+a;
        }

        double avg= (double)total/(double) insertTime.size();

        logger.info("实验结果：" );
        Outputs outputs = OutputsApi.getOutputs(keyPairHolder.pubKeyToString(keyPairHolder.getPublic()));
        logger.info("BigchainDB中实际个数：" + outputs.getOutput().size());
        logger.info("本次实验的总时间："+(endTime-startTime));
        logger.info("本次insert语句个数："+assetTotal);
        logger.info("本次实验中BDQL语句消耗时间："+(total));
        logger.info("平均每次语句使用时间："+avg);
        logger.info("本次实验中非BDQL语句消耗时间："+((endTime-startTime)-total));


        Map map=new HashMap();
        map.put("本次实验的总时间：",(endTime-startTime));
        map.put("本次insert语句个数",assetTotal);
        map.put("BigchainDB中实际个数：",outputs.getOutput().size());
        map.put("本次实验中BDQL语句消耗时间：",total);
        map.put("平均每次语句使用时间：",total/insertTime.size());
        map.put("本次实验中非BDQL语句消耗时间：：",((endTime-startTime)-total));
        result.setStatus(ParserResult.SUCCESS);
        result.setData(map);
        result.setMessage(null);
        return result;
    }

    /**
     * updata实验
     * @param metadataTotal 实验个数
     * @return
     * @throws InterruptedException
     * @throws IOException
     */
    @Override
    public ParserResult updateExperiment(int metadataTotal) throws InterruptedException, IOException {
        ParserResult result = new ParserResult();
        long startTime=System.currentTimeMillis();//开始时间
        List<Long> updatetTime=new ArrayList<>();
        bigchainDBRunner.StartConn();
        result = bdqlUtil.work("INSERT INTO Computer (id, ip,mac,size,cpu,ROM,RAM) VALUES ('nihao','nihao','Champs-Elysees','nihao','i7','nihao','nihao')");
        Thread.sleep(1000);
        String id = (String) result.getData();


        for (int m = 0; m < metadataTotal; m++) {
            long insertStartTime =System.currentTimeMillis();
            result = bdqlUtil.work("UPDATE Person SET FirstName = '" + m + "' , SecondName='" + (m +1)+ "',age= '" + (m + 11) + "',time='" + (m + 12) + "' WHERE ID='" + id + "'");
            updatetTime.add(System.currentTimeMillis()-insertStartTime);
            String TXid = (String) result.getData();
            logger.info("交易ID：" + TXid);
            Thread.sleep(500);

        }
        long endTime=System.currentTimeMillis();
        long total=0;
        for (Long a:updatetTime){
            total=total+a;
        }

        double avg= (double)total/(double) updatetTime.size();
        logger.info("实验结果：" );
        Outputs outputs = OutputsApi.getOutputs(keyPairHolder.pubKeyToString(keyPairHolder.getPublic()));
        logger.info("BigchainDB中实际个数：" + (outputs.getOutput().size()-1));
        logger.info("本次实验的总时间："+(endTime-startTime));
        logger.info("本次insert语句个数："+metadataTotal);
        logger.info("本次实验中BDQL语句消耗时间："+(total));
        logger.info("平均每次语句使用时间："+(avg));
        logger.info("本次实验中非BDQL语句消耗时间："+((endTime-startTime)-total));


        Map map=new HashMap();
        map.put("本次实验的总时间：",(endTime-startTime));
        map.put("本次insert语句个数",metadataTotal);
        map.put("BigchainDB中实际个数：",(outputs.getOutput().size()-1));
        map.put("本次实验中BDQL语句消耗时间：",total);
        map.put("平均每次语句使用时间：",avg);
        map.put("本次实验中非BDQL语句消耗时间：：",((endTime-startTime)-total));
        result.setStatus(ParserResult.SUCCESS);
        result.setData(map);
        result.setMessage(null);
        return result;
    }

    @Override
    public ParserResult selectExperiment() {
        return null;
    }
}
