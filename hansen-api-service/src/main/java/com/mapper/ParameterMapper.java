package com.mapper;

import com.base.dao.CommonDao;
import com.model.Parameter;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商家相关接口
 * Created by Administrator on 2017/6/20.
 */
@Repository
public interface ParameterMapper extends CommonDao<Parameter> {

    /**
     * 代理区域或搜索区域商家列表
     *
     * @return
     * @throws Exception
     */
    List<Parameter> getList() throws Exception;

}
