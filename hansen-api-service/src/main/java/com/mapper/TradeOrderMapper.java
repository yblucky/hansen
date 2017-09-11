package com.mapper;

import com.base.dao.CommonDao;
import com.model.TradeOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TradeOrderMapper extends CommonDao<TradeOrder> {
    List<TradeOrder> readWaitHandleList(@Param("startRow") Integer startRow, @Param("pageSize") Integer pageSize) throws Exception;

    Integer readWaitHandleCount() throws Exception;

    List<TradeOrder> readRewardList(@Param("userId") String userId, @Param("taskTime") Date taskTime, @Param("startRow") Integer startRow, @Param("pageSize") Integer pageSize) throws Exception;

    Integer batchUpdateSignCycle(@Param("list") List<String> idList);

    Integer batchUpdateTaskCycle(@Param("list") List<String> idList);

    Integer batchUpdateTaskCycleDefault(@Param("list") List<String> idList, @Param("taskCycle") Integer taskCycle);

    Integer batchUpdateOrderStatus(@Param("list") List<String> idList);

    List<TradeOrder> readRewardListByOrderType(@Param("userId") String userId, @Param("list") List<Integer> source, @Param("startRow") Integer startRow, @Param("pageSize") Integer pageSize) throws Exception;

    Integer readRewardCountByOrderType(@Param("userId") String userId, @Param("list") List<Integer> source);

    Double sumReadRewardByOrderType(@Param("userId") String userId, @Param("list") List<Integer> source);

    Double readSumDynamicProfitsCount(@Param("userId") String userId, @Param("list") List<Integer> source);


}
