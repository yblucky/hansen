package com.mapper;

import com.base.dao.CommonDao;
import com.model.TransferCode;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferCodeMapper extends CommonDao<TransferCode> {

    Integer readCountByUserId(@Param("userId") String userId);

    List<TransferCode> readListByUserId(@Param("userId") String userId, @Param("startRow") int startRow, @Param("pageSize") int pageSize);
}
