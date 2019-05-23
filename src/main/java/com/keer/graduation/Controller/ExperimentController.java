package com.keer.graduation.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bigchaindb.api.OutputsApi;
import com.bigchaindb.model.Output;
import com.bigchaindb.model.Outputs;
import com.keer.graduation.BDQLParser.BDQLUtil;
import com.keer.graduation.Bigchaindb.BigchainDBRunner;
import com.keer.graduation.Bigchaindb.BigchainDBUtil;
import com.keer.graduation.Bigchaindb.KeyPairHolder;
import com.keer.graduation.Domain.ParserResult;
import com.keer.graduation.Service.IExperimentService;
import com.keer.graduation.Util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
public class ExperimentController {
    private static Logger logger = LoggerFactory.getLogger(ExperimentController.class);

    @Autowired
    IExperimentService experimentService;

    @Autowired
    FileUtil fileUtil;

    @Autowired
    BigchainDBRunner bigchainDBRunner;

    @Autowired
    BDQLUtil bdqlUtil;

    @Autowired
    KeyPairHolder keyPairHolder;
    @Autowired
    BigchainDBUtil bigchainDBUtil;

    @RequestMapping(value = "/insertExperiment/{asset}",method = RequestMethod.GET)
    public ParserResult insertExperiment(@PathVariable int asset) throws InterruptedException, IOException {
        return experimentService.insertExperiment(asset);
    }

    @RequestMapping(value = "/insertByDriverExperiment/{asset}",method = RequestMethod.GET)
    public ParserResult insertByDriverExperiment(@PathVariable int asset) throws Exception {
        return experimentService.insertByDriverExperiment(asset);
    }


    @RequestMapping(value = "/updateExperiment/{metadata}",method = RequestMethod.GET)
    public ParserResult updateExperiment(@PathVariable int metadata) throws IOException, InterruptedException {
        return experimentService.updateExperiment(metadata);
    }

    @RequestMapping(value = "/updateByDriverExperiment/{metadata}",method = RequestMethod.GET)
    public ParserResult updateByDriverExperiment(@PathVariable int metadata) throws IOException, InterruptedException {
        return experimentService.updateByDriverExperiment(metadata);
    }

    @RequestMapping(value = "/selectAssetExperiment/{total}/{count}",method = RequestMethod.GET)
    public ParserResult selectAssetExperiment(@PathVariable int total, @PathVariable int count) throws IOException, InterruptedException {
        return experimentService.selectAssetExperiment(total,count);
    }
    @RequestMapping(value = "/selectAssetByDriverExperiment/{total}/{count}/{a}",method = RequestMethod.GET)
    public ParserResult selectAssetByDriverExperiment(@PathVariable int total, @PathVariable int count, @PathVariable int a) throws IOException, InterruptedException {
        return experimentService.selectAssetByDriverExperiment(total,count,a);
    }

    @RequestMapping(value = "/selectMetadataExperiment/{total}/{count}",method = RequestMethod.GET)
    public ParserResult selectMetadataExperiment( @PathVariable int total,@PathVariable int count) throws IOException, InterruptedException {
        return experimentService.selectMetadataExperiment(total,count);
    }

    @RequestMapping(value = "/selectMetadataByDriverExperiment/{total}/{count}/{a}",method = RequestMethod.GET)
    public ParserResult selectMetadataExperiment(@PathVariable int total, @PathVariable int count, @PathVariable int a) throws IOException, InterruptedException {
        return experimentService.selectMetadataByDriverExperiment(total,count,a);
    }

    @GetMapping("/test")
    public String  test() throws InterruptedException {
        String json=fileUtil.readFile("./data.json");
        List<Integer> list= JSON.parseArray(json,Integer.class);
        for(Integer a:list){
            experimentService.selectMetadataByDriverExperiment(10000,50,a.intValue());
        }
        return "finish";
    }

    @GetMapping("/selectAsset/{total}")
    public void selectAsset(@PathVariable int total) throws InterruptedException {
        experimentService.selectAsset(total);
    }

    @GetMapping("/selectMetadata/{total}")
    public void selectMetadata(@PathVariable int total) throws InterruptedException {
        experimentService.selectMetadata(total);
    }

    @GetMapping("/testData")
    public void testData() throws InterruptedException, IOException {
        bigchainDBRunner.StartConn();
        for (int j = 0; j < 10; j++) {
            ParserResult result = bdqlUtil.work("INSERT INTO Computer (id, ip,mac,size,cpu,ROM,RAM) VALUES ('" + (j + 1) + "','" + (j + 2) + "','Champs-Elysees','" + (j + 3) + "','i7','" + (j + 4) + "','" + (j + 5) + "')");
            String id = (String) result.getData();
            logger.info("资产ID：" + id);

            logger.info(bigchainDBUtil.checkTransactionExit(id) + "");
            ParserResult result1 = new ParserResult();
            for (int i = 0; i < 10; i++) {
                result1 = bdqlUtil.work("UPDATE Person SET FirstName = '" + i + "' , SecondName='" + j + "',age= '" + (i + j) + "',time='" + (i + j + 10) + "' WHERE ID='" + id + "'");
                logger.info("交易ID：" + result1.getData());
                Thread.sleep(500);
            }
        }
        Outputs outputs = OutputsApi.getOutputs(keyPairHolder.pubKeyToString(keyPairHolder.getPublic()));
        logger.info("交易总数1：" + outputs.getOutput().size());
        for (Output output : outputs.getOutput()) {
            logger.info("交易ID：" + output.getTransactionId() + ",密钥：" + output.getPublicKeys());
        }

    }


    public static void main(String[] args){



    }
}
