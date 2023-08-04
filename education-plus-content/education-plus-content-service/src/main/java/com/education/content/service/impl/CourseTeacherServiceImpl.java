package com.education.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.education.base.exception.EducationPlusException;
import com.education.content.mapper.CourseTeacherMapper;
import com.education.content.model.po.CourseTeacher;
import com.education.content.service.CourseTeacherService;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @description:
 * @author：wufengning
 * @date: 2023/8/3
 */
@Service
public class CourseTeacherServiceImpl implements CourseTeacherService {
    @Resource
    private CourseTeacherMapper courseTeacherMapper;
    @Override
    public List<CourseTeacher> queryCourseTeachList(Long courseId) {
        LambdaQueryWrapper<CourseTeacher> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(CourseTeacher::getCourseId,courseId);
        List<CourseTeacher> courseTeachers = courseTeacherMapper.selectList(wrapper);
        return courseTeachers;
    }

    @Override
    @Transactional
    public CourseTeacher saveOrUpdateCourseTeacher(CourseTeacher courseTeacher) {
        Long teacherId = courseTeacher.getId();
        if(null==teacherId){
            //新增教师
            CourseTeacher teacher=new CourseTeacher();
            BeanUtils.copyProperties(courseTeacher,teacher);
            teacher.setCreateDate(LocalDateTime.now());
            int flag = courseTeacherMapper.insert(teacher);
            if(flag<=0){
                EducationPlusException.cast("新增失败");
            }
            return getTeacherById(courseTeacher);
        }
        else{
            //修改教师
            CourseTeacher teacher = courseTeacherMapper.selectById(courseTeacher.getId());
            BeanUtils.copyProperties(courseTeacher,teacher);
            int flag = courseTeacherMapper.updateById(teacher);
            if(flag<=0){
                EducationPlusException.cast("修改课程失败");
            }
            return getTeacherById(teacher);
        }
    }

    @Override
    @Transactional
    public void deleteCourseTeacher(Long courseId, Long teacherId) {
        LambdaQueryWrapper<CourseTeacher> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(CourseTeacher::getCourseId,courseId)
                .eq(CourseTeacher::getId,teacherId);
        int flag = courseTeacherMapper.delete(wrapper);
        if(flag<=0){
            EducationPlusException.cast("教师删除失败");
        }
    }

    private CourseTeacher getTeacherById(CourseTeacher courseTeacher){
        return courseTeacherMapper.selectById(courseTeacher.getId());
    }
}
