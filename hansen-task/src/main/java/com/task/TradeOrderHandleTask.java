package com.task;

import com.base.task.BaseScheduleTask;
import com.constant.OrderStatus;
import com.constant.OrderType;
import com.constant.RedisKey;
import com.constant.UserStatusType;
import com.model.TradeOrder;
import com.model.User;
import com.model.UserDetail;
import com.redis.Strings;
import com.service.TradeOrderService;
import com.service.UserDepartmentService;
import com.service.UserDetailService;
import com.service.UserService;
import com.utils.toolutils.ToolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 保单结算定时器
 *
 * @date 2017年08月29日
 */
@Component
public class TradeOrderHandleTask extends BaseScheduleTask {
    private static final Logger logger = LoggerFactory.getLogger(TradeOrderHandleTask.class);
    @Autowired
    private TradeOrderService orderService;
    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDepartmentService userDepartmentService;


    @Override
    protected void doScheduleTask() {
        System.out.println("-----------------------------------TradeOrderHandleTask  start----------------------------------------------------------");
        try {
            String redisKey = Strings.get(RedisKey.TRADE_ORDER_IS_HANDING.getKey());
            if (ToolUtil.isNotEmpty(redisKey)) {
                return;
            }
            Integer count = null;
            count = orderService.readWaitHandleCount();
//        if (count == null || count == 0) {
//            con.setStatus(OrderStatus.PENDING.getCode());
//            con.setSource(OrderType.INSURANCE_COVER.getCode());
//            count = orderService.readCount(con);
//        }
//        if (count == null || count == 0) {
//            con.setStatus(OrderStatus.PENDING.getCode());
//            con.setSource(OrderType.INSURANCE_ORIGIN.getCode());
//            count = orderService.readCount(con);
//        }
            if (count != null && count > 0) {
                Strings.set(RedisKey.TRADE_ORDER_IS_HANDING.getKey(), RedisKey.TRADE_ORDER_IS_HANDING.getKey());
                List<TradeOrder> orders = orderService.readWaitHandleList(0, 1);
                for (TradeOrder model : orders) {
                    try {
                        logger.error("保单开始结算："+model.getOrderNo());
                        Boolean flag  =   orderService.handleInsuranceTradeOrder(model.getOrderNo());
                        //向上累加业绩
                        User refferUser = null;
                        String referId = model.getSendUserId();
                        UserDetail userDetail=userDetailService.readById(referId);
                        if (userDetail==null){
                            logger.error("保单结束结算："+model.getOrderNo()+"无法查询到下单用户");
                            return;
                        }
                        for (int i = 0; i < userDetail.getLevles(); i++) {
                            if (ToolUtil.isEmpty(referId)) {
                                break;
                            }
                            refferUser = userService.readById(referId);
                            if (refferUser == null && UserStatusType.ACTIVATESUCCESSED.getCode() != refferUser.getStatus()) {
                                continue;
                            }
                            userService.reloadUserGrade(referId);
                            referId = refferUser.getContactUserId();
                        }
                        logger.error("保单结束结算："+model.getOrderNo()+" 保单结算结果："+flag);
                    } catch (Exception e) {
                        logger.error("保单结算异常，保单号为：" + model.getOrderNo(), model.getId());
                        e.printStackTrace();
                    }
                }
            }
            Strings.del(RedisKey.TRADE_ORDER_IS_HANDING.getKey());
        } catch (Exception e) {
            logger.error("保单结束结算异常");
            e.printStackTrace();
        }
        System.out.println("-----------------------------------TradeOrderHandleTask  end----------------------------------------------------------");
    }
}
