package com.atguigu.serviceedu.entity.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class SubjectReadVo {
    @ExcelProperty(index = 0)//第一列
    private String oneLevelSubject;
    @ExcelProperty(index = 1)//第二列
    private String twoLevelSubject;
}
