package com.service;


import com.base.service.CommonService;
import com.model.SysLogs;
import com.vo.SysLogsVo;

/**
 * 日志信息业务层接口
 */
public interface SysLogsService extends CommonService<SysLogs> {

    /**
     * 保存数据
     *
     * @param vo 系统日志对象
     * @throws Exception
     */
    public void save(SysLogsVo vo) throws Exception;

}
