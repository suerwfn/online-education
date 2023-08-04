package com.education.base.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 分页查询结果
 * @author：wufengning
 * @date: 2023/7/31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> implements Serializable {
    //数据列表
    private List<T> items;
    private Long counts;
    private Long page;
    private Long pageSize;
}
