package com.education.content.service;

import com.education.content.model.dto.SaveTeachplanDto;
import com.education.content.model.dto.TeachPlanDto;

import java.util.List;

/**
 * @description:
 * @author：wufengning
 * @date: 2023/8/2
 */
public interface TeachPlanService {
    List<TeachPlanDto> queryTreeNodesByCourseId(Long courseId);

    void saveOrUpdateTeachPlan(SaveTeachplanDto teachPlanDto);

    void deleteTeachPlan(Long teachPlanId);

    void orderByTeachPlan(String moveType,Long teachPlanId);
}
