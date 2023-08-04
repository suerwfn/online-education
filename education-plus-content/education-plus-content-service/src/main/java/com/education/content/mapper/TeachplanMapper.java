package com.education.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.education.content.model.dto.TeachPlanDto;
import com.education.content.model.po.Teachplan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 课程计划 Mapper 接口
 * </p>
 *
 * @author itcast
 */
@Mapper
public interface TeachplanMapper extends BaseMapper<Teachplan> {
    /**
     * 根据课程id查询目录结构
     * @param courseId
     * @return
     */
    List<TeachPlanDto> queryTreeNodes(Long courseId);

    /**
     * 查询章节最大数量
     * @param courseId
     * @param parentId
     * @return
     */
    Integer getTeachplanCount(@Param("courseId") Long courseId, @Param("parentId") Long parentId);
}
