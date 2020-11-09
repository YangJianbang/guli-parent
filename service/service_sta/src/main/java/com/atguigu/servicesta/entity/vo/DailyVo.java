package com.atguigu.servicesta.entity.vo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class DailyVo {
    private String type;
    private String begin;
    private String end;
}
