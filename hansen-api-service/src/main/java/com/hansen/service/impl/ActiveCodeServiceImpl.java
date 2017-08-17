package com.hansen.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.common.constant.CodeType;
import com.common.constant.Constant;
import com.hansen.mapper.ActiveCodeMapper;
import com.hansen.service.ActiveCodeService;
import com.hansen.service.TransferCodeService;
import com.hansen.service.UserService;
import com.hansen.service.UserSignService;
import com.model.ActiveCode;
import com.model.Task;
import com.model.TransferCode;
import com.model.User;
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
    public Boolean codeTransfer(String fromUserId,String toUserId, Integer toUid, Integer transferNo) {
        userService.updateUserActiveCode(fromUserId,-transferNo);
        userService.updateUserActiveCode(toUserId,transferNo);
        TransferCode transferCode = new TransferCode();
        transferCode.setSendUserId(fromUserId);
        transferCode.setReceviceUserId(toUserId);
        transferCode.setType(CodeType.ACTIVATECODE.getCode());
        transferCode.setTransferNo(transferNo);
        transferCode.setRemark("用户转让激活码："+fromUserId+"  to  "+toUserId);
        transferCodeService.create(transferCode);
        return true;
    }

    @Override
    @Transactional
    public Boolean useActiveCode(String userId, Integer activeNo) {
        userService.updateUserActiveCode(userId,-activeNo);
        TransferCode transferCode = new TransferCode();
        transferCode.setSendUserId(userId);
        transferCode.setReceviceUserId(Constant.SYSTEM_USER_ID);
        transferCode.setType(CodeType.ACTIVATECODE.getCode());
        transferCode.setTransferNo(-activeNo);
        transferCode.setRemark("用户激活账号："+userId);
        transferCodeService.create(transferCode);
        return null;
    }
}
