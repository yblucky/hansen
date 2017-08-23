/* 
 * 文件名：BaseUrlRecordServiceImpl.java  
 * 版权：Copyright 2016-2016 炎宝网络科技  All Rights Reserved by
 * 修改人：邱深友  
 * 创建时间：2016年11月29日
 * 版本号：v1.0
*/
package com.hansen.service.impl;

import com.base.dao.CommonDao;
import com.base.page.Paging;
import com.base.page.RespBody;
import com.base.page.ResultCode;
import com.base.service.impl.CommonServiceImpl;
import com.hansen.mappers.SysUrlRecordMapper;
import com.hansen.service.SysUrlRecordService;
import com.hansen.vo.SysUrlRecordVo;
import com.utils.classutils.MyBeanUtils;
import com.utils.toolutils.ToolUtil;
import com.model.SysUrlRecord;
import com.model.SysUrlRecordPo;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * URL不拦截记录操作业务层实现类
 */
@Service
public class SysUrlRecordServiceImpl extends CommonServiceImpl<SysUrlRecord> implements SysUrlRecordService {
    @Resource
    private SysUrlRecordMapper sysUrlRecordMapper;

    @Override
    protected CommonDao<SysUrlRecord> getDao() {
        return sysUrlRecordMapper;
    }

    @Override
    protected Class<SysUrlRecord> getModelClass() {
        return SysUrlRecord.class;
    }

    @Override
    public List<String> findUrl() {
        return sysUrlRecordMapper.findUrl();
    }

    /**
     * 分页查询URL记录信息
     */
    @Override
    public void loadRecords(Paging page, RespBody respBody) throws Exception {
        //创建分页插件对象
        RowBounds rowBounds = new RowBounds(page.getPageNumber(), page.getPageSize());
        List<SysUrlRecord> recordPos = sysUrlRecordMapper.loadRecords(rowBounds);
        //将po持久化类转换为vo类并保存到respBody对象中
        respBody.add(ResultCode.SUCCESS.getCode().toString(), "查询URL记录信息成功", MyBeanUtils.copyList(recordPos, SysUrlRecordVo.class));
        //查总记录树
        int totalCount = sysUrlRecordMapper.findAllCount();
        page.setTotalCount(totalCount);
        respBody.setPage(page);
    }

    /**
     * 新增URL记录
     */
    @Override
    public void addRecord(SysUrlRecordVo recordVo, RespBody respBody) throws Exception {
        //将vo类转换为po持久化类
        SysUrlRecord recordPo = MyBeanUtils.copyProperties(recordVo, SysUrlRecord.class);
        //获取id
        recordPo.setId(ToolUtil.getUUID());
        //执行新增URL记录操作
        sysUrlRecordMapper.create(recordPo);
        respBody.add(ResultCode.SUCCESS.getCode().toString(), "新增URL记录成功");
    }

    /**
     * 修改URL记录
     */
    @Override
    public void updateRecord(SysUrlRecordVo recordVo, RespBody respBody) throws Exception {
        //将Vo转换成Po
        SysUrlRecordPo recordPo = MyBeanUtils.copyProperties(recordVo, SysUrlRecordPo.class);
        //执行修改URL记录操作
        sysUrlRecordMapper.updateRecord(recordPo);
        respBody.add(ResultCode.SUCCESS.getCode().toString(), "修改URL记录成功");
    }

    /**
     * 修改URL记录
     */
    @Override
    public void deleteRecord(SysUrlRecordVo recordVo, RespBody respBody) throws Exception {
        //将Vo转换成Po
        SysUrlRecordPo recordPo = MyBeanUtils.copyProperties(recordVo, SysUrlRecordPo.class);
        //执行删除URL记录操作
        sysUrlRecordMapper.deleteRecord(recordPo);
        respBody.add(ResultCode.SUCCESS.getCode().toString(), "修改URL记录成功");
    }

    /**
     * 删除多条URL记录
     */
    @Override
    public void deleteRecords(List<String> delIds, RespBody respBody) throws Exception {

        //执行删除URL记录操作
        int count = sysUrlRecordMapper.deleteRecords(delIds);
        if (count <= 0) {
            respBody.add(ResultCode.ERROR.getCode().toString(), "删除URL记录失败");
        }
        respBody.add(ResultCode.SUCCESS.getCode().toString(), "删除" + count + "条URL记录成功");
    }

}
