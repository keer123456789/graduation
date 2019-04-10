package com.keer.graduation.Controller;

import com.keer.graduation.Domain.ParserResult;
import com.keer.graduation.Service.IExperimentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;


@RestController
public class ExperimentController {
    private static Logger logger = LoggerFactory.getLogger(ExperimentController.class);

    @Autowired
    IExperimentService experimentService;

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

    @RequestMapping(value = "/selectAssetExperiment/{total}",method = RequestMethod.GET)
    public ParserResult selectAssetExperiment( @PathVariable int total) throws IOException, InterruptedException {
        return experimentService.selectAssetExperiment(total);
    }

    @RequestMapping(value = "/selectMetadataExperiment/{total}",method = RequestMethod.GET)
    public ParserResult selectMetadataExperiment( @PathVariable int total) throws IOException, InterruptedException {
        return experimentService.selectAssetExperiment(total);
    }


}