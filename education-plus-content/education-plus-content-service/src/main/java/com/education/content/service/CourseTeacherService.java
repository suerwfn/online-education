package com.education.content.service;

import com.education.base.model.PageResult;
import com.education.content.model.po.CourseTeacher;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author：wufengning
 * @date: 2023/8/3
 */

public interface CourseTeacherService {
    List<CourseTeacher> queryCourseTeachList(Long courseId);

    CourseTeacher saveOrUpdateCourseTeacher(CourseTeacher courseTeacher);

    void deleteCourseTeacher(Long courseId,Long teacherId);
}
