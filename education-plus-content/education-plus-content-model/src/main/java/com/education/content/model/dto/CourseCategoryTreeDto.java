package com.education.content.model.dto;

import com.education.content.model.po.CourseCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description:
 * @author：wufengning
 * @date: 2023/8/2
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseCategoryTreeDto extends CourseCategory {
    List<CourseCategoryTreeDto> childrenTreeNodes;
}
