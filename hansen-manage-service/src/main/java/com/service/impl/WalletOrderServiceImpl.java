package com.service.impl;

import com.base.dao.CommonDao;
import com.base.page.Page;
import com.base.service.impl.CommonServiceImpl;
import com.constant.CodeType;
import com.constant.Constant;
import com.constant.WalletOrderStatus;
import com.constant.WalletOrderType;
import com.mapper.WalletOrderMapper;
import com.model.Parameter;
import com.model.TransferCode;
import com.model.User;
import com.model.WalletOrder;
import com.service.*;
import com.utils.toolutils.OrderNoUtil;
import com.utils.toolutils.ToolUtil;
import com.vo.BackReChargeVo;
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
    @Autowired
    private TransferCodeService transferCodeService;

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
            poundageScale = amt * poundageScale;
            confirmAmount = amt - poundage;
            userService.updateTradeAmtByUserId(fromUserId, -confirmAmount);
            userService.updateTradeAmtByUserId(toUserId, confirmAmount);
        } else if (WalletOrderType.PAY_COIN_INNER_TRANSFER.getCode() == walletOrderType.getCode()) {
            poundageScale = amt * poundageScale;
            poundageScale = Double.valueOf(ParamUtil.getIstance().get(Parameter.PAYCOINTRANSFERSCALE));
            userService.updatePayAmtByUserId(fromUserId, -confirmAmount);
            userService.updatePayAmtByUserId(toUserId, confirmAmount);
        } else if (WalletOrderType.EQUITY_COIN_INNER_TRANSFER.getCode() == walletOrderType.getCode()) {
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
        String txtId = "";
        if (!outType.contains(walletOrderType.getCode())) {
            return false;
        }
        BitcoinClient bitcoinClient = null;
        if (WalletOrderType.TRADE_COIN_DRWA.getCode() == walletOrderType.getCode()) {
            poundageScale = Double.valueOf(ParamUtil.getIstance().get(Parameter.TRADECOINOUTSCALE));
            poundageScale = amt * poundageScale;
            confirmAmount = amt - poundage;
            userService.updateTradeAmtByUserId(fromUserId, -amt);
//            bitcoinClient = WalletUtil.getBitCoinClient(CurrencyType.TRADE);
//            txtId= WalletUtil.sendToAddress(bitcoinClient, address, new BigDecimal(amt + ""), "提币", "");
        } else if (WalletOrderType.PAY_COIN_DRWA.getCode() == walletOrderType.getCode()) {
            poundageScale = amt * poundageScale;
            poundageScale = Double.valueOf(ParamUtil.getIstance().get(Parameter.PAYCOINOUTSCALE));
            userService.updatePayAmtByUserId(fromUserId, -amt);
//            bitcoinClient = WalletUtil.getBitCoinClient(CurrencyType.PAY);
//            txtId=WalletUtil.sendToAddress(bitcoinClient, address, new BigDecimal(amt + ""), "提币", "");
        } else if (WalletOrderType.EQUITY_COIN_DRWA.getCode() == walletOrderType.getCode()) {
            poundageScale = amt * poundageScale;
            poundageScale = Double.valueOf(ParamUtil.getIstance().get(Parameter.EQUITYCOINOUTSCALE));
            userService.updateEquityAmtByUserId(fromUserId, -amt);
//            bitcoinClient = WalletUtil.getBitCoinClient(CurrencyType.EQUITY);
//            txtId=  WalletUtil.sendToAddress(bitcoinClient, address, new BigDecimal(amt + ""), "提币", "");
        }
        //创建提币订单
        WalletOrder order = this.addWalletOrder(fromUserId, "", walletOrderType, amt, confirmAmount, poundage, WalletOrderStatus.PENDING);
        //管理后台审核通过的审核，生成此记录
//        transactionService.addWalletOrderTransaction(Constant.SYSTEM_USER_ID,address,walletOrderType,txtId,order.getOrderNo(),amt);
        return true;
    }

    @Override
    public List<WalletOrder> readOrderList(String receviceUserId, List<Integer> list, Page page) {
        return walletOrderMapper.readOrderList(receviceUserId, list, page);
    }

    @Override
    public Integer readOrderCount(String receviceUserId, List<Integer> list) {
        return walletOrderMapper.readOrderCount(receviceUserId, list);
    }

    @Override
    @Transactional
    public void chargeService(BackReChargeVo vo, User chargeTargerUser) {
        if (vo.getActiveCodeNo() != null && vo.getActiveCodeNo() > 0) {
            TransferCode transferCode = new TransferCode();
            transferCode.setSendUserId(Constant.SYSTEM_USER_ID);
            transferCode.setReceviceUserId(chargeTargerUser.getId());
            transferCode.setTransferNo(vo.getActiveCodeNo().intValue());
            transferCode.setType(CodeType.ACTIVATECODE.getCode());
            transferCode.setReceviceUserNick(chargeTargerUser.getNickName());
            transferCode.setSendUserNick(Constant.SYSTEM_USER_NICKNAME);
            transferCode.setRemark("系统赠送激活码");
            transferCodeService.create(transferCode);
            userService.updateUserActiveCode(chargeTargerUser.getId(), vo.getActiveCodeNo().intValue());
        }
        if (vo.getRegisterCodeNo() != null && vo.getRegisterCodeNo() > 0) {
            TransferCode transferCode = new TransferCode();
            transferCode.setSendUserId(Constant.SYSTEM_USER_ID);
            transferCode.setReceviceUserId(chargeTargerUser.getId());
            transferCode.setTransferNo(vo.getRegisterCodeNo().intValue());
            transferCode.setType(CodeType.REGISTERCODE.getCode());
            transferCode.setReceviceUserNick(chargeTargerUser.getNickName());
            transferCode.setSendUserNick(Constant.SYSTEM_USER_NICKNAME);
            transferCode.setRemark("系统赠送注册码");
            transferCodeService.create(transferCode);
            userService.updateUserRegisterCode(chargeTargerUser.getId(), vo.getRegisterCodeNo().intValue());
        }

        WalletOrder addModel = new WalletOrder();
        addModel.setReceviceUserId(chargeTargerUser.getId());
        addModel.setSendUserId(Constant.SYSTEM_USER_ID);
        addModel.setStatus(WalletOrderStatus.SUCCESS.getCode());
        if (vo.getPayAmt() != null && vo.getPayAmt() > 0) {
            addModel.setId(ToolUtil.getUUID());
            addModel.setOrderNo(OrderNoUtil.get());
            addModel.setAmount(-vo.getPayAmt());
            addModel.setConfirmAmt(-vo.getPayAmt());
            addModel.setOrderType(WalletOrderType.PAY_COIN_BACK_CHARGE.getCode());
            addModel.setRemark("管理员后台充值购物币");
            this.create(addModel);
            userService.updatePayAmtByUserId(chargeTargerUser.getId(), vo.getPayAmt());
        }
        if (vo.getTradeAmt() != null && vo.getTradeAmt() > 0) {
            addModel.setId(ToolUtil.getUUID());
            addModel.setOrderNo(OrderNoUtil.get());
            addModel.setAmount(-vo.getTradeAmt());
            addModel.setConfirmAmt(-vo.getTradeAmt());
            addModel.setOrderType(WalletOrderType.TRADE_COIN_BACK_CHARGE.getCode());
            addModel.setRemark("管理员后台充值交易币");
            this.create(addModel);
            userService.updateTradeAmtByUserId(chargeTargerUser.getId(), vo.getTradeAmt());
        }
        if (vo.getEquityAmt() != null && vo.getEquityAmt() > 0) {
            addModel.setId(ToolUtil.getUUID());
            addModel.setOrderNo(OrderNoUtil.get());
            addModel.setAmount(-vo.getEquityAmt());
            addModel.setConfirmAmt(-vo.getEquityAmt());
            addModel.setOrderType(WalletOrderType.EQUITY_COIN_BACK_CHARGE.getCode());
            addModel.setRemark("管理员后台充值股权币");
            this.create(addModel);
            userService.updateEquityAmtByUserId(chargeTargerUser.getId(), vo.getEquityAmt());
        }
    }
}
