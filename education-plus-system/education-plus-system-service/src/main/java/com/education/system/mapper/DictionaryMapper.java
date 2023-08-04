package com.education.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.education.system.model.po.Dictionary;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 数据字典 Mapper 接口
 * </p>
 *
 * @author itcast
 */
@Mapper
public interface DictionaryMapper extends BaseMapper<Dictionary> {

}
