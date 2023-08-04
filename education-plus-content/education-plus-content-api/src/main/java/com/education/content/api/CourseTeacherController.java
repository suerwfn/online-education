package com.education.content.api;

import com.education.content.model.po.CourseTeacher;
import com.education.content.service.CourseTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description: 教师控制类
 * @author：wufengning
 * @date: 2023/8/3
 */
@Api(value = "教师信息相关接口",description = "教师信息相关接口")
@RestController
public class CourseTeacherController {

    @Resource
    private CourseTeacherService courseTeacherService;
    @ApiOperation("根据课程id查询教师信息")
    @GetMapping("/courseTeacher/list/{courseId}")
    public List<CourseTeacher> getTeacherByCourseId(@PathVariable("courseId") Long courseId){
        return courseTeacherService.queryCourseTeachList(courseId);
    }

    @ApiOperation("添加或修改教师")
    @PostMapping("/courseTeacher")
    public CourseTeacher saveOrUpdateCourseTeacher(@RequestBody CourseTeacher courseTeacher){
        return courseTeacherService.saveOrUpdateCourseTeacher(courseTeacher);
    }

    @ApiOperation("删除教师信息")
    @DeleteMapping("/courseTeacher/course/{courseId}/{teacherId}")
    public void deleteCourseTeacher(@PathVariable("courseId") Long courseId, @PathVariable("teacherId") Long teacherId){
        courseTeacherService.deleteCourseTeacher(courseId,teacherId);
    }
}
