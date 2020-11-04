package com.fridge.skl.service;

import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.ResponseObj;
import org.springframework.stereotype.Service;

//用于标注服务层，主要用来进行业务的逻辑处理
@Service
public class ErrorService extends BaseService {
    public ResponseObj onError(RequestObj request) {
        return initResporse(request);
    }
}
