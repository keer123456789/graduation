package com.keer.graduation.Service;

import com.keer.graduation.Domain.ParserResult;

public interface IService {
    ParserResult getKey();

    ParserResult startConn(String url);
}
