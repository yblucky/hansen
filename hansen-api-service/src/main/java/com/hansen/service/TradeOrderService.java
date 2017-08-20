package com.hansen.service;

import com.base.service.CommonService;
import com.model.CardGrade;
import com.model.TradeOrder;
import com.model.User;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @date 2016年11月27日
 */
public interface TradeOrderService extends CommonService<TradeOrder> {

    TradeOrder createInsuranceTradeOrder(User activeUser, CardGrade cardGrade) throws Exception;

    Boolean handleInsuranceTradeOrder(String orderNo) throws Exception;

    List<TradeOrder> readRewardList(Date taskTime, Integer startRow, Integer pageSize) throws Exception;

    Integer batchUpdateSignCycle(List<String> idList) throws Exception;

    Integer batchUpdateTaskCycle(List<String> idList) throws Exception;

    Map<String,Double> getCoinNoFromRmb(Double rmbAmt) throws  Exception;

}
