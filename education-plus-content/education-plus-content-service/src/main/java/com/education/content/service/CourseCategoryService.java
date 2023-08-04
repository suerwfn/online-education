package com.education.content.service;

import com.education.content.model.dto.CourseCategoryTreeDto;

import java.util.List;

/**
 * @description:
 * @author：wufengning
 * @date: 2023/8/2
 */
public interface CourseCategoryService {

    public List<CourseCategoryTreeDto> selectTreeNodes(String id);
}
