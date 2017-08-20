package com.hansen.sysservice;


import com.hansen.vo.SysLogsVo;

/**
 * 日志信息业务层接口
 */
public interface SysLogsService {

    /**
     * 保存数据
     *
     * @param vo 系统日志对象
     * @throws Exception
     */
    public void save(SysLogsVo vo) throws Exception;

}
