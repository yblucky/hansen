package com.service;

import com.base.service.CommonService;
import com.model.Parameter;

import java.util.List;
import java.util.Map;

/**
 * 获取系统参数公用service
 */
public interface ParameterService extends CommonService<Parameter> {

    /**
     * 获取系统参数列表
     *
     * @return
     * @throws Exception
     */
    List<Parameter> getList() throws Exception;

    Double getRmbConvertCoinRate(String id, String name);

    Map<String, Object> getScale();

    Double getScale(String key);
}
