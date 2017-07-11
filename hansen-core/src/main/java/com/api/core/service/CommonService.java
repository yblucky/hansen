package com.api.core.service;

import com.api.common.BaseModel;

import java.util.List;

public interface CommonService<M extends BaseModel> {

    // C
    void create(M model);

    void createWithUUID(M model);

    // R
    M readById(String id);


    M readOne(M model);

    // pageNo 业务上的起始页，一般从1开始
    List<M> readList(M model, int pageNo, int pageSize, int totalRow);

    List<M> readAll(M model);

    int readCount(M model);

    // U
    void updateById(String id, M model);

    // D
    void deleteById(String id);


}
