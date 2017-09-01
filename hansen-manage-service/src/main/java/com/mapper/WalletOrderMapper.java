package com.mapper;

import com.base.dao.CommonDao;
import com.base.page.Page;
import com.model.WalletOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletOrderMapper extends CommonDao<WalletOrder> {

    List<WalletOrder> readOrderList(@Param("receviceUserId") String receviceUserId, @Param("list") List<Integer> list, @Param("page") Page page);

    Integer readOrderCount(@Param("receviceUserId") String receviceUserId,@Param("list") List<Integer> list);
}
