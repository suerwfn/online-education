package com.education.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.education.base.exception.EducationPlusException;
import com.education.base.model.PageParams;
import com.education.base.model.PageResult;
import com.education.content.mapper.CourseBaseMapper;
import com.education.content.mapper.CourseCategoryMapper;
import com.education.content.mapper.CourseMarketMapper;
import com.education.content.mapper.CourseTeacherMapper;
import com.education.content.model.dto.AddCourseDto;
import com.education.content.model.dto.CourseBaseInfoDto;
import com.education.content.model.dto.EditCourseDto;
import com.education.content.model.dto.QueryCourseParamsDto;
import com.education.content.model.po.*;
import com.education.content.service.CourseBaseInfoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Wrapper;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @description:
 * @author：wufengning
 * @date: 2023/8/1
 */
@Service
public class CourseBaseInfoServiceImpl implements CourseBaseInfoService {
    @Resource
    private CourseBaseMapper courseBaseMapper;

    @Resource
    private CourseMarketMapper courseMarketMapper;

    @Resource
    private CourseCategoryMapper courseCategoryMapper;

    @Resource
    private CourseMarketServiceImpl courseMarketService;

    @Resource
    private CourseTeacherMapper courseTeacherMapper;
    @Override
    public PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto) {
        //封装查询条件
        LambdaQueryWrapper<CourseBase> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(queryCourseParamsDto.getCourseName()),CourseBase::getName,queryCourseParamsDto.getCourseName());
        queryWrapper.eq(StringUtils.isNotEmpty(queryCourseParamsDto.getAuditStatus()),CourseBase::getAuditStatus,queryCourseParamsDto.getAuditStatus());
        queryWrapper.eq(StringUtils.isNotEmpty(queryCourseParamsDto.getPublicStatus()),CourseBase::getStatus,queryCourseParamsDto.getPublicStatus());
        //封装分页条件
        Page<CourseBase> page=new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        //查询课程
        Page<CourseBase> courseBasePage = courseBaseMapper.selectPage(page, queryWrapper);
        List<CourseBase> item = courseBasePage.getRecords();
        long total = courseBasePage.getTotal();
        //封装结果
        return new PageResult<>(item,total, pageParams.getPageNo(), pageParams.getPageSize());
    }

    @Transactional
    @Override
    public CourseBaseInfoDto createCourseBase(Long companyId,AddCourseDto addCourseDto) {
        //通过校验后封装参数
        CourseBase courseBase=new CourseBase();
        BeanUtils.copyProperties(addCourseDto,courseBase);
        //设置默认值
        courseBase.setAuditStatus("202002");
        courseBase.setStatus("203001");
        courseBase.setCompanyId(companyId);
        courseBase.setCreateDate(LocalDateTime.now());
        //插入课程信息
        int base = courseBaseMapper.insert(courseBase);
        //封装收费信息
        CourseMarket courseMarket=new CourseMarket();
        BeanUtils.copyProperties(addCourseDto,courseMarket);
        courseMarket.setId(courseBase.getId());
        int market = saveCourseMarket(courseMarket);
        if(base<=0||market<=0){
            EducationPlusException.cast("新增课程信息失败");
        }
        return getCourseBaseInfo(courseBase.getId());
    }

    private int saveCourseMarket(CourseMarket courseMarket){
        String charge=courseMarket.getCharge();
        if (StringUtils.isBlank(charge)){
            EducationPlusException.cast("请设置收费规则");
        }
        if(charge.equals("201001")){
            Float price = courseMarket.getPrice();
            if(price==null|| price<=0){
                EducationPlusException.cast("课程设置了收费，需要设置价格，且价格要大于0");
            }
        }
        boolean insert = courseMarketService.saveOrUpdate(courseMarket);
        return insert?1:-1;
    }
    @Override
    public CourseBaseInfoDto getCourseBaseInfo(Long courseId){
        CourseBaseInfoDto courseBaseInfoDto=new CourseBaseInfoDto();
        //查询课程基本信息
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if(null==courseBase){
            return null;
        }
        BeanUtils.copyProperties(courseBase,courseBaseInfoDto);
        //查询课程价格信息
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        if(null!=courseMarket){
            BeanUtils.copyProperties(courseMarket,courseBaseInfoDto);
        }
        //查询类别名称
        //小类名称
        courseBaseInfoDto.setStName(courseCategoryMapper.selectById(courseBase.getSt()).getName());
        //大类名称
        courseBaseInfoDto.setMtName(courseCategoryMapper.selectById(courseBase.getMt()).getName());
        return courseBaseInfoDto;
    }
    @Transactional
    @Override
    public CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto editCourseDto) {
        //查询原课程信息
        CourseBase courseBaseInfo = courseBaseMapper.selectById(editCourseDto.getId());
        //判断是否属于自己的机构
        if(!courseBaseInfo.getCompanyId().equals(companyId)){
            EducationPlusException.cast("只允许修改本机构的课程");
        }
        //修改数据
        BeanUtils.copyProperties(editCourseDto,courseBaseInfo);
        //设置更新时间
        courseBaseInfo.setChangeDate(LocalDateTime.now());
        courseBaseMapper.updateById(courseBaseInfo);
        //获取营销数据
        CourseMarket courseMarket = courseMarketMapper.selectById(editCourseDto.getId());
        if(null==courseMarket){
            courseMarket=new CourseMarket();
        }
        BeanUtils.copyProperties(editCourseDto,courseMarket);
       this.saveCourseMarket(courseMarket);
        return getCourseBaseInfo(editCourseDto.getId());
    }

    @Override
    @Transactional
    public void deleteCourse(Long companyId, Long courseId) {
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if(!companyId.equals(courseBase.getCompanyId())){
            EducationPlusException.cast("只允许删除本机构的课程");
        }
        //删除教师
        LambdaQueryWrapper<CourseTeacher> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(CourseTeacher::getCourseId,courseId);
        courseTeacherMapper.delete(wrapper);
        //删除课程计划
        LambdaQueryWrapper<Teachplan> wrapperPlan=new LambdaQueryWrapper();
        wrapperPlan.eq(Teachplan::getCourseId,courseId);
        courseTeacherMapper.delete(wrapper);
        //删除媒资
        LambdaQueryWrapper<TeachplanMedia> wrapperMedia=new LambdaQueryWrapper();
        wrapperMedia.eq(TeachplanMedia::getCourseId,courseId);
        //删除营销
        LambdaQueryWrapper<CourseMarket> wrapperMarket=new LambdaQueryWrapper();
        wrapperMarket.eq(CourseMarket::getId,courseId);
        courseMarketMapper.delete(wrapperMarket);
        //删除课程
        courseBaseMapper.deleteById(courseId);
    }

}
