package com.keer.graduation.Controller;

import com.keer.graduation.Bigchaindb.KeyPairHolder;
import com.keer.graduation.Domain.ParserResult;
import com.keer.graduation.Service.Implement.BigchainDBServiceImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    /**
     * 连接BigchainDB节点
     * @param map
     * @return
     */
    @PostMapping("/startConn")
    public  ParserResult startConn(@RequestBody Map map){
        String ip =map.get("ip").toString();
        return bigchainDBServiceImp.startConn(ip);
    }

    /***
     * 获取数据中所有表名，组成jstree的数据格式
     * @param map
     * @return
     */
    @PostMapping("/getCloumns")
    public  ParserResult getCloumnsName(@RequestBody Map map){
        String key =map.get("key").toString();
        return bigchainDBServiceImp.getCloumnsName(key);
    }

    /**
     * 获得相应表的数据
     * @param name
     * @param operation
     * @return
     */
    @RequestMapping(value = "/getTableData/{name}/{operation}",method = RequestMethod.GET)
    public ParserResult getTableData(@PathVariable String name,@PathVariable String operation){
        return bigchainDBServiceImp.getTableData(name,operation);
    }


    @PostMapping("/runBDQL")
    public ParserResult runBDQL(@RequestBody Map map){
        String BDQL=map.get("bdql").toString();
        return bigchainDBServiceImp.runBDQL(BDQL);
    }



}
