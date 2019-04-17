package com.keer.graduation.Service;

import com.keer.graduation.Domain.ParserResult;

import java.io.IOException;


public interface IExperimentService {
    ParserResult insertExperiment(int assetTotal) throws InterruptedException, IOException;

    ParserResult updateExperiment(int metadataTotal) throws InterruptedException, IOException;

    ParserResult updateByDriverExperiment(int metadataTotal) throws InterruptedException, IOException;

    ParserResult insertByDriverExperiment(int asstTotal) throws Exception;

    ParserResult selectAssetExperiment(int total,int count) throws InterruptedException;

    ParserResult selectAssetByDriverExperiment(int total,int count,int a) throws InterruptedException;

    ParserResult selectMetadataExperiment(int total,int count) throws InterruptedException;

    ParserResult selectMetadataByDriverExperiment(int total,int count,int sum) throws InterruptedException;

    ParserResult test();
}
