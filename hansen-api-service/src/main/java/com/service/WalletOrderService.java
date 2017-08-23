package com.service;

import com.base.service.CommonService;
import com.constant.WalletOrderStatus;
import com.constant.WalletOrderType;
import com.model.WalletOrder;

/**
 * @date 2016年11月27日
 */
public interface WalletOrderService extends CommonService<WalletOrder> {
    Boolean coinTransfer(String fromUserId, String toUserId, WalletOrderType walletOrderType, Double amt) throws Exception;

    WalletOrder addWalletOrder(String fromUserId, String toUserId, WalletOrderType walletOrderType, Double amt, Double confirmAmt, Double poundage, WalletOrderStatus walletOrderStatus) throws Exception;

    Boolean coinOut(String fromUserId, String toUserId, String address, WalletOrderType walletOrderType, Double amt) throws Exception;
}
