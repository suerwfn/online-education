package com.education.content.api;

import com.education.content.model.dto.CourseCategoryTreeDto;
import com.education.content.service.CourseCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description:
 * @author：wufengning
 * @date: 2023/8/2
 */
@RestController
@Api(value = "课程分类相关接口",description = "课程分类相关接口")
public class CourseCategoryController {
    @Resource
    private CourseCategoryService courseCategoryService;

    @ApiOperation("课程分类相关接口")
    @GetMapping("/course-category/tree-nodes")
    public List<CourseCategoryTreeDto> queryTreeNodes(){
        return courseCategoryService.selectTreeNodes("1");
    }
}
