package com.sysservice.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.mapper.SysLogsMapper;
import com.model.SysLogs;
import com.sysservice.SysLogsService;
import com.utils.classutils.MyBeanUtils;
import com.utils.toolutils.LogUtils;
import com.utils.toolutils.ToolUtil;
import com.vo.SysLogsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 日志信息业务层实现
 *
 * @author qsy
 * @version v1.0
 * @date 2016年11月26日
 */
@Service
public class SysLogsServiceImpl  extends CommonServiceImpl<SysLogs> implements SysLogsService {
    @Autowired
    private SysLogsMapper sysLogsMapper;

    @Override
    protected CommonDao<SysLogs> getDao() {
        return sysLogsMapper;
    }

    @Override
    protected Class<SysLogs> getModelClass() {
        return SysLogs.class;
    }


    /**
     * 保存数据
     *
     * @param vo
     * @throws Exception
     */
    @Transactional
    @Override
    public void save( SysLogsVo vo) throws Exception {
        try {
             SysLogs po =  MyBeanUtils.copyProperties(vo, SysLogs.class);
            po.setId(ToolUtil.getUUID());
            po.setOptDate(new Date());
            if (po.getLogDetail() != null && po.getLogDetail().length() > 1024) {
                po.setLogDetail(po.getLogDetail().substring(0, 1023));
            }
            sysLogsMapper.create(po);
        } catch (Exception ex) {
          LogUtils.error("日志信息保存失败！", ex);
            throw new Exception("日志信息保存失败！");
        }
    }

}
