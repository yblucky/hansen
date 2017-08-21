package com.base.dao;

import com.common.BaseModel;
import com.sun.xml.internal.rngom.parse.host.Base;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 通用mapper
 */
@Repository
public interface CommonDao<D extends  BaseModel> extends BaseMapper<D>{
    // C
    void create(D model);

    // R
    D readById(@Param("id") String id);

    D readOne(@Param("model") D model);

    List<D> readList(@Param("model") D model, @Param("startRow") int startRow, @Param("pageSize") int pageSize);

    Integer readCount(@Param("model") D model);

    // U update操作的原则是先根据条件查询到记录，再根据记录的ID更新
    void updateById(@Param("id") String id, @Param("model") D model);

    // D delete操作的原则是先根据条件查询到记录，再根据记录的ID删除
    void deleteById(@Param("id")  String id);
}
