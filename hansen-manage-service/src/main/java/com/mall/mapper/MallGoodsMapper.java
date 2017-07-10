package com.mall.mapper;

import com.mall.core.dao.CommonDao;
import com.mall.model.MallGoods;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MallGoodsMapper extends CommonDao<MallGoods> {


    int batchDel(@Param("ids") List<String> ids);
}
