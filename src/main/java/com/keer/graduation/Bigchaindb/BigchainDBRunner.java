package com.keer.graduation.Bigchaindb;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bigchaindb.builders.BigchainDbConfigBuilder;
import com.keer.graduation.Util.HttpUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * 连接BigchainDB
 */
public class BigchainDBRunner {
    //日志输出
    private static Logger logger = LoggerFactory.getLogger(BigchainDBRunner.class);
    //获取配置文件的BigchainDB的url

    @Value("blockchaindb.base-url")
    private static String url ;

    /**
     * 连接BigchainDB
     */
    public static void StartConn() {

        BigchainDbConfigBuilder
                .baseUrl(url)
                .setup();
        logger.info("与节点：" + url + ",连接成功");
    }

    public static boolean StartConn(String url) {

        BigchainDbConfigBuilder
                .baseUrl(url)
                .setup();
        String body = HttpUtil.httpGet(url);
        logger.info(body);
        JSONObject jsonObject = JSON.parseObject(body, JSONObject.class);
        logger.info(jsonObject.getString("version"));
        if (jsonObject.getString("version").equals("2.0.0b9")) {
            logger.info("与节点：" + url + ",连接成功");
            return true;
        } else {
            logger.info("与节点：" + url + ",连接失败");
            return false;
        }
    }

    public static void main(String[] args) {
        StartConn("http://172.16.1.197:9984");
    }

}