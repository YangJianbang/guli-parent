package com.atguigu.serviceedu.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CourseQuery {

    @ApiModelProperty(value = "课程名称")
    private String title;

    private String status;
}
