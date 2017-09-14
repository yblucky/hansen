package com.service.impl;

import com.base.dao.CommonDao;
import com.base.page.Page;
import com.base.page.PageResult;
import com.base.service.impl.CommonServiceImpl;
import com.constant.Constant;
import com.constant.TransactionStatusType;
import com.constant.WalletOrderStatus;
import com.constant.WalletOrderType;
import com.mapper.WalletOrderMapper;
import com.model.*;
import com.service.*;
import com.utils.toolutils.OrderNoUtil;
import com.utils.toolutils.ToolUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @date 2016年11月27日
 */
@Service
public class WalletOrderServiceImpl extends CommonServiceImpl<WalletOrder> implements WalletOrderService {
    @Autowired
    private WalletOrderMapper walletOrderMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private WalletTransactionService transactionService;

    @Override
    protected CommonDao<WalletOrder> getDao() {
        return walletOrderMapper;
    }

    @Override
    protected Class<WalletOrder> getModelClass() {
        return WalletOrder.class;
    }

    @Override
    @Transactional
    public Boolean coinTransfer(String fromUserId, String toUserId, WalletOrderType walletOrderType, Double amt) throws Exception {
        Double poundageScale = 0d;
        Double poundage = 0d;
        Double confirmAmount = 0d;
        if (WalletOrderType.TRADE_COIN_INNER_TRANSFER.getCode() == walletOrderType.getCode()) {
            poundageScale = Double.valueOf(ParamUtil.getIstance().get(Parameter.TRADECOINTRANSFERSCALE));
            poundage = amt * poundageScale;
            confirmAmount = amt - poundage;
            userService.updateTradeAmtByUserId(fromUserId, -amt);
            userService.updateTradeAmtByUserId(toUserId, confirmAmount);
        } else if (WalletOrderType.PAY_COIN_INNER_TRANSFER.getCode() == walletOrderType.getCode()) {
            poundageScale = Double.valueOf(ParamUtil.getIstance().get(Parameter.PAYCOINTRANSFERSCALE));
            poundage = amt * poundageScale;
            confirmAmount = amt - poundage;
            userService.updatePayAmtByUserId(fromUserId, -amt);
            userService.updatePayAmtByUserId(toUserId, confirmAmount);
        } else if (WalletOrderType.EQUITY_COIN_INNER_TRANSFER.getCode() == walletOrderType.getCode()) {
            poundageScale = Double.valueOf(ParamUtil.getIstance().get(Parameter.EQUITYCOINTRANSFERSCALE));
            poundage = amt * poundageScale;
            confirmAmount = amt - poundage;
            userService.updateEquityAmtByUserId(fromUserId, -amt);
            userService.updateEquityAmtByUserId(toUserId, confirmAmount);
        }
        this.addWalletOrder(fromUserId, toUserId, walletOrderType, -amt, -confirmAmount, poundage, WalletOrderStatus.SUCCESS);
        return true;
    }

    @Override
    public WalletOrder addWalletOrder(String fromUserId, String toUserId, WalletOrderType walletOrderType, Double amt, Double confirmAmt, Double poundage, WalletOrderStatus walletOrderStatus) throws Exception {
        WalletOrder walletOrder = new WalletOrder();
        walletOrder.setOrderNo(OrderNoUtil.get());
        walletOrder.setSendUserId(fromUserId);
        walletOrder.setReceviceUserId(toUserId);
        walletOrder.setAmount(amt);
        walletOrder.setConfirmAmt(confirmAmt);
        walletOrder.setConfirmAmt(confirmAmt);
        walletOrder.setPoundage(poundage);
        walletOrder.setOrderType(walletOrderType.getCode());
        walletOrder.setRemark(walletOrderType.getMsg());
        walletOrder.setStatus(walletOrderStatus.getCode());
        this.create(walletOrder);
        return walletOrder;
    }


    @Override
    public Boolean coinOut(String fromUserId, String toUserId, String address, WalletOrderType walletOrderType, Double amt) throws Exception {
        List<Integer> outType = new ArrayList<>();
        outType.add(WalletOrderType.TRADE_COIN_DRWA.getCode());
        outType.add(WalletOrderType.PAY_COIN_DRWA.getCode());
        outType.add(WalletOrderType.EQUITY_COIN_DRWA.getCode());
        Double poundageScale = 0d;
        Double poundage = 0d;
        Double confirmAmount = 0d;
        if (!outType.contains(walletOrderType.getCode())) {
            return false;
        }
        UserDetail userDetail = userDetailService.readById(fromUserId);
        User user = userService.readById(fromUserId);
        WalletTransaction model = new WalletTransaction();

        if (WalletOrderType.TRADE_COIN_DRWA.getCode() == walletOrderType.getCode()) {
            poundageScale = Double.valueOf(ParamUtil.getIstance().get(Parameter.TRADECOINOUTSCALE));
            poundage = amt * poundageScale;
            confirmAmount = amt - poundage;
            userService.updateTradeAmtByUserId(fromUserId, -amt);
            model.setAddress(userDetail.getOutTradeAddress());
            model.setOrderType(WalletOrderType.TRADE_COIN_DRWA.getCode());
            model.setRemark(WalletOrderType.TRADE_COIN_DRWA.getMsg());
            model.setFee(poundage);
            userDetailService.updateForzenTradeAmtByUserId(fromUserId, amt);
//            bitcoinClient = WalletUtil.getBitCoinClient(CurrencyType.TRADE);
//            txtId= WalletUtil.sendToAddress(bitcoinClient, address, new BigDecimal(amt + ""), "提币", "");
        } else if (WalletOrderType.PAY_COIN_DRWA.getCode() == walletOrderType.getCode()) {
            poundageScale = Double.valueOf(ParamUtil.getIstance().get(Parameter.PAYCOINOUTSCALE));
            poundage = amt * poundageScale;
            userService.updatePayAmtByUserId(fromUserId, -amt);
            userDetailService.updateForzenPayAmtByUserId(fromUserId, amt);
            model.setAddress(userDetail.getOutPayAddress());
            model.setRemark(WalletOrderType.TRADE_COIN_DRWA.getMsg());
            model.setFee(poundage);
            model.setOrderType(WalletOrderType.PAY_COIN_DRWA.getCode());
//            bitcoinClient = WalletUtil.getBitCoinClient(CurrencyType.PAY);
//            txtId=WalletUtil.sendToAddress(bitcoinClient, address, new BigDecimal(amt + ""), "提币", "");
        } else if (WalletOrderType.EQUITY_COIN_DRWA.getCode() == walletOrderType.getCode()) {
            poundageScale = Double.valueOf(ParamUtil.getIstance().get(Parameter.EQUITYCOINOUTSCALE));
            poundage = amt * poundageScale;
            model.setAddress(userDetail.getOutEquityAddress());
            userService.updateEquityAmtByUserId(fromUserId, -amt);
            userDetailService.updateForzenEquityAmtByUserId(fromUserId, amt);
            model.setRemark(WalletOrderType.TRADE_COIN_DRWA.getMsg());
            model.setOrderType(WalletOrderType.EQUITY_COIN_DRWA.getCode());
            model.setFee(poundage);
//            bitcoinClient = WalletUtil.getBitCoinClient(CurrencyType.EQUITY);
//            txtId=  WalletUtil.sendToAddress(bitcoinClient, address, new BigDecimal(amt + ""), "提币", "");
        }
        //创建提币订单
//        WalletOrder order = this.addWalletOrder(fromUserId, "", walletOrderType, -amt, -confirmAmount, poundage, WalletOrderStatus.PENDING);
        model.setAmount(amt);
        model.setStatus(WalletOrderStatus.DENIED.getCode());
        model.setTransactionStatus(TransactionStatusType.UNCHECKED.getCode());
        model.setMessage(WalletOrderStatus.PENDING.getMsg());
        model.setUserId(user.getUid().toString());
        model.setPrepayId(OrderNoUtil.get());
        model.setCategory(Constant.COIN_OUT);
        model.setConfirmations(0l);
        transactionService.create(model);
        //管理后台审核通过的审核，生成此记录
//        transactionService.addWalletOrderTransaction(Constant.SYSTEM_USER_ID,address,walletOrderType,txtId,order.getOrderNo(),amt);
        return true;
    }

    @Override
    public PageResult<WalletOrder> readTransferList(String userId, List<Integer> orderType, Page page) throws Exception {
        if (StringUtils.isBlank(userId)) {
            return null;
        }
        PageResult<WalletOrder> pageResult = new PageResult<WalletOrder>();
        BeanUtils.copyProperties(pageResult, page);
        List<WalletOrder> list = walletOrderMapper.readTransferList(userId, orderType, page);
        if (ToolUtil.isEmpty(list)) {
            list = Collections.emptyList();
            pageResult.setTotalSize(0);
        }
        pageResult.setTotalSize(list.size());
        pageResult.setRows(list);
        return pageResult;
    }
}
