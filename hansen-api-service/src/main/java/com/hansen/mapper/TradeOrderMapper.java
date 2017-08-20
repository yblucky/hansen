package com.hansen.mapper;

import com.base.dao.CommonDao;
import com.model.TradeOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TradeOrderMapper extends CommonDao<TradeOrder> {
    List<TradeOrder> readRewardList(@Param("taskTime") Date taskTime, @Param("startRow") Integer startRow, @Param("pageSize") Integer pageSize) throws Exception;

    Integer batchUpdateSignCycle(@Param("list") List<String> idList);

    Integer batchUpdateTaskCycle(@Param("list") List<String> idList);

    Integer batchUpdateTaskCycleDefault(@Param("list") List<String> idList,@Param("taskCycle") Integer taskCycle);

    Integer batchUpdateOrderStatus(@Param("list") List<String> idList);


}
