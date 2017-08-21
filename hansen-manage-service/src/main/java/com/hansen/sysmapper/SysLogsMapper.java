package com.hansen.sysmapper;

import com.base.dao.BaseMapper;
import com.base.dao.CommonDao;
import com.common.BaseModel;
import com.model.SysLogsPo;
import org.springframework.stereotype.Repository;

/**
 * 日志信息表操作类
 */
@Repository

public interface SysLogsMapper extends  BaseMapper<SysLogsPo>  {

}
