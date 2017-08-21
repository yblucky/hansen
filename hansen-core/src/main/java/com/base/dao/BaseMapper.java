package com.base.dao;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 通用dao基础接口，其他dao继承该接口即可
 */
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T> {

}
