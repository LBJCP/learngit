package com.fridge.skl.factory;

import com.fridge.skl.handler.CallBackHandler;
import com.fridge.skl.handler.ResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangn
 * @Date 2019-12-12
 */
@Component
public class FactoryForhandler {

    @Autowired
    public Map<String, ResultHandler> handlerMaps = new ConcurrentHashMap<>(1);
    @Autowired
    public Map<String, CallBackHandler> callBackHandlerMaps = new ConcurrentHashMap<>(1);

    public ResultHandler getHandler(String component) {
        //主操作通过注入
        //特殊taskid在这里处理
//        handlerMaps.put(QUERY_HEATH_DOMAIN_TASK_ID, new DomainService());
//        handlerMaps.put(FOOD_EXPIRE_TASK_ID, new FoodService());

        ResultHandler resultHandler = handlerMaps.get(component);


        if (resultHandler == null) {
            // throw new RuntimeException("no strategy defined");
            System.out.println("no strategy defined");
        }
        return resultHandler;
    }

    public CallBackHandler getCallBackHandler(String component) {

        CallBackHandler resultHandler = callBackHandlerMaps.get(component);

        if (resultHandler == null) {
            System.out.println("no strategy defined");
        }
        return resultHandler;
    }
}
