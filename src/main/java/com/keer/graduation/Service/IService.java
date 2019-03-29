package com.keer.graduation.Service;

import com.keer.graduation.Domain.ParserResult;

import java.io.IOException;

public interface IService {
    ParserResult getKey();

    ParserResult startConn(String url);

    ParserResult getCloumnsName(String key);

    ParserResult getTableData(String key,String operation);

    ParserResult runBDQL(String BDQL);

    void Experiment(int i,int j) throws IOException, InterruptedException;
}
