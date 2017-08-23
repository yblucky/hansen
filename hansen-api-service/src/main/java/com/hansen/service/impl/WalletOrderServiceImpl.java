package com.hansen.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.constant.CurrencyType;
import com.constant.WalletOrderStatus;
import com.constant.WalletOrderType;
import com.utils.ParamUtil;
import com.utils.toolutils.OrderNoUtil;
import com.hansen.mappers.WalletOrderMapper;
import com.hansen.service.UserService;
import com.hansen.service.WalletOrderService;
import com.hansen.service.WalletTransactionService;
import com.model.Parameter;
import com.model.WalletOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.paradoxs.bitcoin.client.BitcoinClient;

import java.util.ArrayList;
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
        if (CurrencyType.TRADE.getCode() == walletOrderType.getCode()) {
            poundageScale = Double.valueOf(ParamUtil.getIstance().get(Parameter.TRADECOINTRANSFERSCALE));
            poundageScale = amt * poundageScale;
            confirmAmount = amt - poundage;
            userService.updateTradeAmtByUserId(fromUserId, -confirmAmount);
            userService.updateTradeAmtByUserId(toUserId, confirmAmount);
        } else if (CurrencyType.PAY.getCode() == walletOrderType.getCode()) {
            poundageScale = amt * poundageScale;
            poundageScale = Double.valueOf(ParamUtil.getIstance().get(Parameter.PAYCOINTRANSFERSCALE));
            userService.updatePayAmtByUserId(fromUserId, -confirmAmount);
            userService.updatePayAmtByUserId(toUserId, confirmAmount);
        } else if (CurrencyType.EQUITY.getCode() == walletOrderType.getCode()) {
            poundageScale = amt * poundageScale;
            poundageScale = Double.valueOf(ParamUtil.getIstance().get(Parameter.EQUITYCOINTRANSFERSCALE));
            userService.updateEquityAmtByUserId(fromUserId, -confirmAmount);
            userService.updateEquityAmtByUserId(toUserId, confirmAmount);
        }
        this.addWalletOrder(fromUserId, toUserId, walletOrderType, amt, confirmAmount, poundage, WalletOrderStatus.SUCCESS);
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
        String txtId="";
        if (!outType.contains(walletOrderType.getCode())) {
            return false;
        }
        BitcoinClient bitcoinClient = null;
        if (WalletOrderType.TRADE_COIN_DRWA.getCode() == walletOrderType.getCode()) {
            poundageScale = Double.valueOf(ParamUtil.getIstance().get(Parameter.TRADECOINOUTSCALE));
            poundageScale = amt * poundageScale;
            confirmAmount = amt - poundage;
            userService.updateTradeAmtByUserId(fromUserId, -confirmAmount);
//            bitcoinClient = WalletUtil.getBitCoinClient(CurrencyType.TRADE);
//            txtId= WalletUtil.sendToAddress(bitcoinClient, address, new BigDecimal(amt + ""), "提币", "");
        } else if (WalletOrderType.PAY_COIN_DRWA.getCode() == walletOrderType.getCode()) {
            poundageScale = amt * poundageScale;
            poundageScale = Double.valueOf(ParamUtil.getIstance().get(Parameter.PAYCOINOUTSCALE));
            userService.updatePayAmtByUserId(fromUserId, -confirmAmount);
//            bitcoinClient = WalletUtil.getBitCoinClient(CurrencyType.PAY);
//            txtId=WalletUtil.sendToAddress(bitcoinClient, address, new BigDecimal(amt + ""), "提币", "");
        } else if (WalletOrderType.EQUITY_COIN_DRWA.getCode() == walletOrderType.getCode()) {
            poundageScale = amt * poundageScale;
            poundageScale = Double.valueOf(ParamUtil.getIstance().get(Parameter.EQUITYCOINOUTSCALE));
            userService.updateEquityAmtByUserId(fromUserId, -confirmAmount);
//            bitcoinClient = WalletUtil.getBitCoinClient(CurrencyType.EQUITY);
//            txtId=  WalletUtil.sendToAddress(bitcoinClient, address, new BigDecimal(amt + ""), "提币", "");
        }
        //创建提币订单
        WalletOrder order=this.addWalletOrder(fromUserId, "", walletOrderType, amt, confirmAmount, poundage, WalletOrderStatus.PENDING);
       //管理后台审核通过的审核，生成此记录
//        transactionService.addWalletOrderTransaction(Constant.SYSTEM_USER_ID,address,walletOrderType,txtId,order.getOrderNo(),amt);
        return true;
    }
}
