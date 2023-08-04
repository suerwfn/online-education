package com.education.content.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author：wufengning
 * @date: 2023/8/2
 */
@Data
@ApiModel(value = "EditCourseDto",description = "修改课程信息")
public class EditCourseDto extends AddCourseDto{
    @ApiModelProperty(value = "课程编号",required = true)
    private Long id;
}
