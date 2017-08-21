package com.hansen.service;

import com.base.service.CommonService;
import com.model.ActiveCode;

/**
 * @date 2017年08月17日
 */
public interface ActiveCodeService extends CommonService<ActiveCode> {
    Boolean codeTransfer(String fromUserId, String toUserId, Integer toUid, Integer transferNo);

    Boolean useActiveCode(String userId, Integer activeNo, String remark);

    Boolean useRegisterCode(String userId, Integer registerCodeNo, String remark);
}
