package com.education.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.education.base.exception.EducationPlusException;
import com.education.content.mapper.TeachplanMapper;
import com.education.content.mapper.TeachplanMediaMapper;
import com.education.content.model.dto.EditCourseDto;
import com.education.content.model.dto.SaveTeachplanDto;
import com.education.content.model.dto.TeachPlanDto;
import com.education.content.model.po.CourseBase;
import com.education.content.model.po.Teachplan;
import com.education.content.model.po.TeachplanMedia;
import com.education.content.service.TeachPlanService;
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
 * @date: 2023/8/2
 */
@Service
public class TeachPlanServiceImpl implements TeachPlanService {
    @Resource
    private TeachplanMapper teachplanMapper;

    @Resource
    private TeachplanMediaMapper teachplanMediaMapper;
    @Override
    public List<TeachPlanDto> queryTreeNodesByCourseId(Long courseId) {
        return teachplanMapper.queryTreeNodes(courseId);
    }
    @Transactional
    @Override
    public void saveOrUpdateTeachPlan(SaveTeachplanDto teachPlanDto) {
        Long id = teachPlanDto.getId();
        if(null==id){
            Teachplan teachplan=new TeachPlanDto();
            BeanUtils.copyProperties(teachPlanDto,teachplan);
            teachplan.setCreateDate(LocalDateTime.now());
            Integer teachplanCount = teachplanMapper.getTeachplanCount(teachplan.getCourseId(), teachplan.getParentid());
            if(null==teachplanCount){
                teachplanCount=0;
            }
            //设置序列号
            teachplan.setOrderby(teachplanCount+1);
            int insert = teachplanMapper.insert(teachplan);
            if(insert<=0) EducationPlusException.cast("课程列表新增失败");
        }else{
            //更新课程计划
            Teachplan teachplan = teachplanMapper.selectById(teachPlanDto.getId());
            BeanUtils.copyProperties(teachPlanDto,teachplan);
            teachplan.setChangeDate(LocalDateTime.now());
            int flag = teachplanMapper.updateById(teachplan);
            if(flag<=0) EducationPlusException.cast("课程计划修改失败");
        }
    }

    @Override
    @Transactional
    public void deleteTeachPlan(Long teachPlanId) {
        if(null==teachPlanId){
            EducationPlusException.cast("课程计划为空");
        }
        //查询数据
        Teachplan teachplan = teachplanMapper.selectById(teachPlanId);
        Integer grade = teachplan.getGrade();
        if(grade==1){
            //查看该章是否有小节
            LambdaQueryWrapper<Teachplan> wrapper=new LambdaQueryWrapper<>();
            wrapper.eq(Teachplan::getParentid,teachPlanId);
            //获取小节数量
            Integer count = teachplanMapper.selectCount(wrapper);
            if(count>0){
                EducationPlusException.cast("课程还有子级信息，无法操作");
            }
            teachplanMapper.deleteById(teachPlanId);
        }
        else{
            //删除小节
            teachplanMapper.deleteById(teachPlanId);
            //删除媒资信息
            LambdaQueryWrapper<TeachplanMedia> wrapper=new LambdaQueryWrapper();
            wrapper.eq(TeachplanMedia::getTeachplanId,teachPlanId);
            teachplanMediaMapper.delete(wrapper);
        }
    }

    @Override
    @Transactional
    public void orderByTeachPlan(String moveType, Long teachPlanId) {
        //获取数据
        Teachplan teachplan = teachplanMapper.selectById(teachPlanId);
        Integer orderby = teachplan.getOrderby();
        Integer grade = teachplan.getGrade();
        Long courseId = teachplan.getCourseId();
        Long parentid = teachplan.getParentid();
        if("moveup".equals(moveType)){
           if(grade==1){
               LambdaQueryWrapper<Teachplan> wrapper=new LambdaQueryWrapper();
               wrapper.eq(Teachplan::getGrade,grade)
                       .eq(Teachplan::getCourseId,courseId)
                       .lt(Teachplan::getOrderby,orderby)
                       .orderByDesc(Teachplan::getOrderby)
                       .last("limit 1");
               Teachplan temp = teachplanMapper.selectOne(wrapper);
               exchangeOrderBy(teachplan,temp);
           }
           else if(grade==2){
               LambdaQueryWrapper<Teachplan> wrapper=new LambdaQueryWrapper();
               wrapper.eq(Teachplan::getParentid,parentid)
                       .eq(Teachplan::getCourseId,courseId)
                       .lt(Teachplan::getOrderby,orderby)
                       .orderByDesc(Teachplan::getOrderby)
                       .last("limit 1");
               Teachplan temp = teachplanMapper.selectOne(wrapper);
               exchangeOrderBy(teachplan,temp);
           }
        }
        else if("movedown".equals(moveType)){
            if(grade==1){
                LambdaQueryWrapper<Teachplan> wrapper=new LambdaQueryWrapper();
                wrapper.eq(Teachplan::getGrade,grade)
                        .eq(Teachplan::getCourseId,courseId)
                        .gt(Teachplan::getOrderby,orderby)
                        .orderByAsc(Teachplan::getOrderby)
                        .last("limit 1");
                Teachplan temp = teachplanMapper.selectOne(wrapper);
                exchangeOrderBy(teachplan,temp);
            }
            else if(grade==2){
                LambdaQueryWrapper<Teachplan> wrapper=new LambdaQueryWrapper();
                wrapper.eq(Teachplan::getParentid,parentid)
                        .eq(Teachplan::getCourseId,courseId)
                        .gt(Teachplan::getOrderby,orderby)
                        .orderByAsc(Teachplan::getOrderby)
                        .last("limit 1");
                Teachplan temp = teachplanMapper.selectOne(wrapper);
                exchangeOrderBy(teachplan,temp);
            }
        }
    }

    private void exchangeOrderBy(Teachplan teachplan,Teachplan temp){
        if(null==temp){
            EducationPlusException.cast("已经到头了，不能再移动了");
        }
        else{
            Integer orderby = teachplan.getOrderby();
            teachplan.setOrderby(temp.getOrderby());
            temp.setOrderby(orderby);
            teachplanMapper.updateById(teachplan);
            teachplanMapper.updateById(temp);
        }
    }

}
