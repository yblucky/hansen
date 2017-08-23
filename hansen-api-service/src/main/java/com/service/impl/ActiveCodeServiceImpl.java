package com.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.constant.CodeType;
import com.constant.Constant;
import com.mappers.ActiveCodeMapper;
import com.service.ActiveCodeService;
import com.service.TransferCodeService;
import com.service.UserService;
import com.model.ActiveCode;
import com.model.TransferCode;
import com.utils.toolutils.ToolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @date 2017年08月17日
 */
@Service
public class ActiveCodeServiceImpl extends CommonServiceImpl<ActiveCode> implements ActiveCodeService {
    @Autowired
    private ActiveCodeMapper activeCodeMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private TransferCodeService transferCodeService;

    @Override
    protected CommonDao<ActiveCode> getDao() {
        return activeCodeMapper;
    }

    @Override
    protected Class<ActiveCode> getModelClass() {
        return ActiveCode.class;
    }

    @Override
    @Transactional
    public Boolean codeTransfer(String fromUserId, String toUserId, Integer toUid, Integer transferNo) {
        userService.updateUserActiveCode(fromUserId, -transferNo);
        userService.updateUserActiveCode(toUserId, transferNo);
        TransferCode transferCode = new TransferCode();
        transferCode.setSendUserId(fromUserId);
        transferCode.setReceviceUserId(toUserId);
        transferCode.setType(CodeType.ACTIVATECODE.getCode());
        transferCode.setTransferNo(transferNo);
        transferCode.setRemark("用户转让激活码：" + fromUserId + "  to  " + toUserId);
        transferCodeService.create(transferCode);
        return true;
    }

    @Override
    @Transactional
    public Boolean useActiveCode(String userId, Integer activeNo, String remark) {
        userService.updateUserActiveCode(userId, -activeNo);
        TransferCode transferCode = new TransferCode();
        transferCode.setSendUserId(userId);
        transferCode.setReceviceUserId(Constant.SYSTEM_USER_ID);
        transferCode.setType(CodeType.ACTIVATECODE.getCode());
        transferCode.setTransferNo(-activeNo);
        if (ToolUtil.isEmpty(remark)) {
            transferCode.setRemark("用户激活账号：" + userId);
        } else {
            transferCode.setRemark(remark);
        }
        transferCodeService.create(transferCode);
        return true;
    }

    @Override
    public Boolean useRegisterCode(String userId, Integer registerCodeNo, String remark) {
        userService.updateUserActiveCode(userId, -registerCodeNo);
        TransferCode transferCode = new TransferCode();
        transferCode.setSendUserId(userId);
        transferCode.setReceviceUserId(Constant.SYSTEM_USER_ID);
        transferCode.setType(CodeType.REGISTERCODE.getCode());
        transferCode.setTransferNo(-registerCodeNo);
        if (ToolUtil.isEmpty(remark)) {
            transferCode.setRemark("用户注册账号：" + userId);
        } else {
            transferCode.setRemark(remark);
        }
        transferCodeService.create(transferCode);
        return true;
    }
}
