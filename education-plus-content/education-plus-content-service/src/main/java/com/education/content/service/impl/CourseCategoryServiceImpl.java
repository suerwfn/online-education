package com.education.content.service.impl;

import com.education.content.mapper.CourseCategoryMapper;
import com.education.content.model.dto.CourseCategoryTreeDto;
import com.education.content.model.po.CourseCategory;
import com.education.content.service.CourseCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description:
 * @author：wufengning
 * @date: 2023/8/2
 */
@Service
public class CourseCategoryServiceImpl implements CourseCategoryService {
    @Resource
    private CourseCategoryMapper courseCategoryMapper;
    @Override
    public List<CourseCategoryTreeDto> selectTreeNodes(String id) {
        //获取所有的树节点
        List<CourseCategoryTreeDto> courseCategoryList = courseCategoryMapper.selectTreeNodes(id);
        //用Map存储节点，方便查找父节点
        Map<String, CourseCategoryTreeDto> categoryMap = courseCategoryList.stream()
                .filter(item -> !id.equals(item.getId()))
                .collect(Collectors.toMap(key -> key.getId(), value -> value));
        //使用List保存最后树结果,结果集
        List<CourseCategoryTreeDto> courseCategoryTreeDtos=new ArrayList<>();
        //处理map中的数据
        courseCategoryList.stream().filter(item->!id.equals(item.getId())).forEach(item->{
            //如果是被查询节点的子节点
            if(item.getParentid().equals(id)){
                courseCategoryTreeDtos.add(item);
            }
            //寻找该节点的父节点，创建容器保存自己
            CourseCategoryTreeDto courseCategory = categoryMap.get(item.getParentid());
            if(courseCategory!=null){
                if(courseCategory.getChildrenTreeNodes()==null){
                    courseCategory.setChildrenTreeNodes(new ArrayList<CourseCategoryTreeDto>());
                }
                //将自己作为子节点添加进去
                courseCategory.getChildrenTreeNodes().add(item);
            }
        });
        return courseCategoryTreeDtos;
    }
}
