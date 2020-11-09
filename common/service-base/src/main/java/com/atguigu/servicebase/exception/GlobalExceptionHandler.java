package com.atguigu.servicebase.exception;

import com.atguigu.commonutils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理类
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception e) {
        e.printStackTrace();
        return R.error().message("全局异常处理");
    }

    @ExceptionHandler(GuliException.class)
    @ResponseBody
    public R error(GuliException e) {
        e.printStackTrace();
        log.error(e.getMsg());
        return R.error().code(e.getCode()).message(e.getMsg());
    }
}
