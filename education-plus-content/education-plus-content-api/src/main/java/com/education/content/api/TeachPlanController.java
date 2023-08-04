package com.education.content.api;

import com.education.content.model.dto.SaveTeachplanDto;
import com.education.content.model.dto.TeachPlanDto;
import com.education.content.service.TeachPlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description:
 * @author：wufengning
 * @date: 2023/8/2
 */
@Slf4j
@Api(value = "课程计划编辑接口",description = "课程计划编辑接口")
@RestController
public class TeachPlanController {
    @Resource
    private TeachPlanService teachPlanService;
    @ApiOperation("查询课程计划编辑接口")
    @GetMapping("/teachplan/{courseId}/tree-nodes")
    public List<TeachPlanDto> queryTreeNodes(@PathVariable("courseId") Long courseId){
        return teachPlanService.queryTreeNodesByCourseId(courseId);
    }

    @ApiOperation("课程计划创建或修改")
    @PostMapping("/teachplan")
    public void saveOrUpdateTeachPlan(@RequestBody SaveTeachplanDto saveTeachplanDto){
        teachPlanService.saveOrUpdateTeachPlan(saveTeachplanDto);
    }

    @ApiOperation("删除课程计划")
    @DeleteMapping("/teachplan/{teachPlanId}")
    public void deleteTeachPlan(@PathVariable("teachPlanId") Long teachPlanId){
        teachPlanService.deleteTeachPlan(teachPlanId);
    }

    @ApiOperation("课程计划移动排序")
    @PostMapping("/teachplan/{moveType}/{teachplanId}")
    public void orderByTeachPlan(@PathVariable("moveType") String moveType, @PathVariable("teachplanId") Long teachpalnId){
        teachPlanService.orderByTeachPlan(moveType,teachpalnId);
    }
}
