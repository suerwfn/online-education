package com.education.content.model.dto;

import com.education.content.model.po.Teachplan;
import com.education.content.model.po.TeachplanMedia;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author：wufengning
 * @date: 2023/8/2
 */
@Data
@ApiModel("课程节点信息")
public class TeachPlanDto extends Teachplan {
    @ApiModelProperty(value = "课程计划子目录",required = true)
    private List<TeachPlanDto> teachPlanTreeNodes;

    @ApiModelProperty(value = "课程媒体资源",required = true)
    private TeachplanMedia teachplanMedia;
}
