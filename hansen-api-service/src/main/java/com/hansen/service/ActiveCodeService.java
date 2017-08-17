package com.hansen.service;

import com.base.service.CommonService;
import com.model.ActiveCode;

/**
 * @date 2016年11月27日
 */
public interface ActiveCodeService extends CommonService<ActiveCode> {

    Boolean codeTransfer(String fromUserId,String toUserId,Integer toUid,Integer transferNo);

    Boolean useActiveCode(String userId,Integer activeNo);
}
