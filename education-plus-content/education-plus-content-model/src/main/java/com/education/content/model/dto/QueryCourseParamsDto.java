package com.education.content.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @description:
 * @author：wufengning
 * @date: 2023/7/31
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QueryCourseParamsDto {
    //审核状态
    private String auditStatus;
    //课程名称
    private String courseName;
    //发布状态
    private String publicStatus;
}
