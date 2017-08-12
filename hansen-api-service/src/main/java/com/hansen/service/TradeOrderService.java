package com.hansen.service;

import com.base.service.CommonService;
import com.model.CardGrade;
import com.model.TradeOrder;
import com.model.User;

/**
 * @date 2016年11月27日
 */
public interface TradeOrderService extends CommonService<TradeOrder> {

    TradeOrder createInsuranceTradeOrder(User activeUser, CardGrade cardGrade) throws Exception;

    Boolean handleInsuranceTradeOrder(String orderNo) throws Exception;
}
