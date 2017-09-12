package com.task;

import com.base.task.BaseScheduleTask;
import com.service.TradeOrderService;
import com.service.UserDetailService;
import com.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 保单结算定时器
 *
 * @date 2017年08月29日
 */
@Component
public class UserDelHandleTask extends BaseScheduleTask {
    private static final Logger logger = LoggerFactory.getLogger(UserDelHandleTask.class);
    @Autowired
    private TradeOrderService orderService;
    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private UserService userService;


    @Override
    protected void doScheduleTask() {
        System.out.println("-----------------------------------UserDelHandleTask  48小时清除未激活用户  start----------------------------------------------------------");
        try {
            try {
                Boolean flag = userService.regularlyClearUnActiveUser();

                if (flag) {
                    logger.error("清除48用户信息成功");
                    return;
                }
                logger.error("清除用户信息异常");
            } catch (Exception e) {
                logger.error("清除用户信息异常，清除用户信息异常");
                e.printStackTrace();
            }
        } catch (Exception e) {
            logger.error("清除用户信息异常");
            e.printStackTrace();
        }
        System.out.println("-----------------------------------UserDelHandleTask  end----------------------------------------------------------");
    }
}
