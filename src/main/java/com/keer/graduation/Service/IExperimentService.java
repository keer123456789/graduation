package com.keer.graduation.Service;

import com.keer.graduation.Domain.ParserResult;

import java.io.IOException;


public interface IExperimentService {
    ParserResult insertExperiment(int assetTotal) throws InterruptedException, IOException;

    ParserResult updateExperiment(int metadataTotal) throws InterruptedException, IOException;

    ParserResult selectExperiment();
}
