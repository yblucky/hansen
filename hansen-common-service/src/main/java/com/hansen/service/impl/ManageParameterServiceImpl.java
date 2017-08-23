package com.hansen.service.impl;


import com.base.dao.CommonDao;
import com.base.page.Paging;
import com.base.service.impl.CommonServiceImpl;
import com.hansen.mappers.SysParameterMapper;
import com.hansen.service.ManageParameterService;
import com.hansen.vo.SysParameterVo;
import com.utils.classutils.MyBeanUtils;
import com.utils.toolutils.ToolUtil;
import com.model.SysParameter;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 参数设置业务层实现类
 */
@Service
public class ManageParameterServiceImpl extends CommonServiceImpl<SysParameter> implements ManageParameterService {

    @Resource
    private SysParameterMapper sysParameterMapper;

    @Override
    protected CommonDao<SysParameter> getDao() {
        return sysParameterMapper;
    }

    @Override
    protected Class<SysParameter> getModelClass() {
        return SysParameter.class;
    }

    @Override
    public List<SysParameterVo> findAll(Paging paging) throws Exception {
        RowBounds rwoBounds = new RowBounds(paging.getPageNumber(), paging.getPageSize());
        return sysParameterMapper.findAll(rwoBounds);
    }

    @Override
    public void add(SysParameterVo parameterVo) throws Exception {
        //将vo转换成Po
        SysParameter parameterPo = MyBeanUtils.copyProperties(parameterVo, SysParameter.class);
        parameterPo.setCreateTime(new Date());
        parameterPo.setId(ToolUtil.getUUID());
        parameterPo.setUpdateTime(new Date());
        //调用dao保存数据
        sysParameterMapper.create(parameterPo);
    }

    @Override
    public void update(SysParameterVo parameterVo) throws Exception {
        //将vo转换成Po
        SysParameter parameterPo = MyBeanUtils.copyProperties(parameterVo, SysParameter.class);
        parameterPo.setUpdateTime(new Date());
        //调用dao修改数据
        sysParameterMapper.updateById(parameterPo.getId(),parameterPo);
    }

    @Override
    public void delete(SysParameterVo parameterVo) {
        //调用dao删除数据
        sysParameterMapper.deleteById(parameterVo.getId());
    }

    @Override
    public long findCount() {
        return sysParameterMapper.findCount();
    }

}
