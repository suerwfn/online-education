package com.education.content.api;

import com.education.base.exception.ValidationGroups;
import com.education.base.model.PageParams;
import com.education.base.model.PageResult;
import com.education.content.model.dto.AddCourseDto;
import com.education.content.model.dto.CourseBaseInfoDto;
import com.education.content.model.dto.EditCourseDto;
import com.education.content.model.dto.QueryCourseParamsDto;
import com.education.content.model.po.CourseBase;
import com.education.content.service.CourseBaseInfoService;
import com.spring4all.swagger.EnableSwagger2Doc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @description:
 * @author：wufengning
 * @date: 2023/7/31
 */
@Api(value = "课程信息管理接口",tags = "课程信息管理接口")
@RestController
public class CourseBaseInfoController {
    @Resource
    private CourseBaseInfoService courseBaseInfoService;
    @ApiOperation("课程信息查询接口")
    @PostMapping("/course/list") //这个pageParams可以直接封装get的?号后面的参数
    public PageResult<CourseBase> list( PageParams pageParams, @RequestBody(required = false) QueryCourseParamsDto queryCourseParamsDto){
        return courseBaseInfoService.queryCourseBaseList(pageParams,queryCourseParamsDto);
    }

    @ApiOperation("课程添加接口")
    @PostMapping("/course")
    public CourseBaseInfoDto createCourseBase(@RequestBody @Validated(ValidationGroups.Insert.class) AddCourseDto addCourseDto){
        Long companyId=1232141425L;
        return courseBaseInfoService.createCourseBase(companyId,addCourseDto);
    }
    @ApiOperation("根据id查询课程信息")
    @GetMapping("/course/{courseId}")
    public CourseBaseInfoDto getCourseBase(@PathVariable("courseId") Long courseId){
        return courseBaseInfoService.getCourseBaseInfo(courseId);
    }
    @ApiOperation("课程修改接口")
    @PutMapping("/course")
    public CourseBaseInfoDto updateCourseBase(@RequestBody @Validated(ValidationGroups.Update.class)EditCourseDto editCourseDto){
        Long companyId=1232141425L;
        return courseBaseInfoService.updateCourseBase(companyId,editCourseDto);
    }

    @ApiOperation("课程删除接口")
    @DeleteMapping("/course/{courseId}")
    public void deleteCourse(@PathVariable("courseId") Long courseId){
        Long companyId=1232141425L;
        courseBaseInfoService.deleteCourse(companyId,courseId);
    }
}
