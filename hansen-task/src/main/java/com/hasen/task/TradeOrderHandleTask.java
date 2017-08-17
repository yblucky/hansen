package com.hasen.task;

import com.common.constant.OrderStatus;
import com.common.constant.OrderType;
import com.hansen.service.TradeOrderService;
import com.hansen.service.UserService;
import com.model.TradeOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 保单结算定时器
 *
 * @date 2016年12月30日
 */
@Component
public class TradeOrderHandleTask extends BaseScheduleTask {

    private static final Logger logger = LoggerFactory.getLogger(TradeOrderHandleTask.class);
    @Autowired
    private UserService userService;
    @Autowired
    private TradeOrderService orderService;



    @Override
    protected void doScheduleTask() {
        TradeOrder con = new TradeOrder();
        con.setStatus(OrderStatus.PENDING.getCode());
        con.setSource(OrderType.INSURANCE.getCode());
        Integer count = orderService.readCount(con);
        if (count==null || count==0){
            con.setStatus(OrderStatus.PENDING.getCode());
            con.setSource(OrderType.INSURANCE_COVER.getCode());
            count = orderService.readCount(con);
        }
        if (count==null || count==0){
            con.setStatus(OrderStatus.PENDING.getCode());
            con.setSource(OrderType.INSURANCE_ORIGIN.getCode());
            count = orderService.readCount(con);
        }
        if (count != null && count > 0) {
            List<TradeOrder> orders = orderService.readList(con, 1, 10, count);
            for (TradeOrder model : orders) {
                try {
                    orderService.handleInsuranceTradeOrder(model.getOrderNo());
                } catch (Exception e) {
                    logger.error("保单结算异常，保单号为："+model.getOrderNo(),model.getId());
                    e.printStackTrace();
                }
            }
        }

    }
}
