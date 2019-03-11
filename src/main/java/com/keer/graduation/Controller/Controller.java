package com.keer.graduation.Controller;

import com.keer.graduation.Bigchaindb.KeyPairHolder;
import com.keer.graduation.Domain.ParserResult;
import com.keer.graduation.Service.Implement.BigchainDBServiceImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class Controller {
    private static Logger logger = LoggerFactory.getLogger(Controller.class);
    @Autowired
    BigchainDBServiceImp bigchainDBServiceImp;

    /**
     * 获取秘钥
     * @return
     */
    @GetMapping("/getKey")
    public ParserResult getKey(){
        logger.info("获取秘钥");
        return bigchainDBServiceImp.getKey();
    }

    @PostMapping("/startConn")
    public  ParserResult startConn(@RequestBody Map map){
        String ip =map.get("ip").toString();
        return bigchainDBServiceImp.startConn(ip);
    }

}
