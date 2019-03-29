package com.keer.graduation.Controller;

import com.keer.graduation.Domain.ParserResult;
import com.keer.graduation.Service.IExperimentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
public class ExperimentController {
    private static Logger logger = LoggerFactory.getLogger(ExperimentController.class);

    @Autowired
    IExperimentService experimentService;

    @RequestMapping(value = "/insertExperiment/{asset}",method = RequestMethod.GET)
    public ParserResult insertExperiment(@PathVariable int asset) throws InterruptedException, IOException {
        return experimentService.insertExperiment(asset);
    }

    @RequestMapping(value = "/updateExperiment/{metadata}",method = RequestMethod.GET)
    public ParserResult updateExperiment(@PathVariable int metadata) throws IOException, InterruptedException {
        return experimentService.updateExperiment(metadata);
    }
}
