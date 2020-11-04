package com.fridge.skl.controller;

import com.fridge.skl.service.AsynfoodService;
import com.fridge.skl.util.Result;
import com.fridge.skl.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 同步优越食材列表
 */
@RequestMapping("/syncfoodlist")
@RestController
public class SyncFoodJsonController {
    @Autowired
    AsynfoodService asynfoodService;

    @PostMapping("/sync")
    public Result process(@RequestBody() String requestjson) {
        int count = asynfoodService.getFoodInfolist();
        return ResultUtil.success("更新了" + count + "条数据");
    }

}
