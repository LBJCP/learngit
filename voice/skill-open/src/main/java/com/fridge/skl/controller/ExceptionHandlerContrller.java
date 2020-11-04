package com.fridge.skl.controller;

import com.fridge.skl.util.Result;
import com.fridge.skl.util.ResultUtil;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author zhangn
 * @Date:
 */
@ControllerAdvice
public class ExceptionHandlerContrller {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Result exception(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<ObjectError> allErrors = bindingResult.getAllErrors();

        for (ObjectError error : allErrors) {
            return ResultUtil.fail(error.getDefaultMessage());
        }

        return ResultUtil.fail("服务器异常");
    }
}