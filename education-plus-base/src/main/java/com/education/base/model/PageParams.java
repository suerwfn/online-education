package com.education.base.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @description: 分页查询的参数封装
 * @author：wufengning
 * @date: 2023/7/31
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PageParams {
    //页码
    private Long pageNo=1L;
    //每页数量
    private Long pageSize=30L;
}
