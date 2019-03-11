package com.keer.graduation.Service.Implement;

import com.keer.graduation.Bigchaindb.BigchainDBRunner;
import com.keer.graduation.Bigchaindb.KeyPairHolder;
import com.keer.graduation.Domain.ParserResult;
import com.keer.graduation.Service.IService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BigchainDBServiceImp implements IService {
    private static Logger logger = LoggerFactory.getLogger(BigchainDBServiceImp.class);

    /**
     * 获取秘钥
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
        }else {
            parserResult.setData(null);
            parserResult.setStatus(ParserResult.ERROR);
            parserResult.setMessage("获取秘钥失敗！！！！！");
            logger.info("获取秘钥失敗！！");
            return parserResult;
        }
    }

    @Override
    public ParserResult startConn(String url) {
        ParserResult parserResult=new ParserResult();
        if(BigchainDBRunner.StartConn(url)){
            parserResult.setMessage("连接BigchainDB节点成功！！！");
            parserResult.setStatus(ParserResult.SUCCESS);
            parserResult.setData(true);
        }else{
            parserResult.setMessage("连接BigchainDB节点失败成功！");
            parserResult.setStatus(ParserResult.ERROR);
            parserResult.setData(false);
        }
        return parserResult;
    }
}
