package com.hansen.sysservice.impl;


import com.common.utils.classutils.MyBeanUtils;
import com.common.utils.toolutils.ToolUtil;
import com.hansen.resp.Paging;
import com.hansen.sysmapper.SysParameterMapper;
import com.hansen.sysservice.ManageParameterService;
import com.hansen.vo.SysParameterVo;
import com.model.SysParameterPo;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 参数设置业务层实现类
 */
@Service
public class ManageParameterServiceImpl implements ManageParameterService {
	@Resource
	private SysParameterMapper sysParameterMapper;

	@Override
	public List<SysParameterVo> findAll(Paging paging) throws Exception {
		RowBounds rwoBounds = new RowBounds(paging.getPageNumber(),paging.getPageSize());
		return sysParameterMapper.findAll(rwoBounds);
	}

	@Override
	public void add(SysParameterVo parameterVo) throws Exception {
		//将vo转换成Po
		SysParameterPo parameterPo = MyBeanUtils.copyProperties(parameterVo, SysParameterPo.class);
		parameterPo.setCreateTime(new Date());
		parameterPo.setId(ToolUtil.getUUID());
		parameterPo.setUpdateTime(new Date());
		//调用dao保存数据
		sysParameterMapper.insert(parameterPo);
	}

	@Override
	public void update(SysParameterVo parameterVo) throws Exception {
		//将vo转换成Po
		SysParameterPo parameterPo = MyBeanUtils.copyProperties(parameterVo, SysParameterPo.class);
		parameterPo.setUpdateTime(new Date());
		//调用dao修改数据
		sysParameterMapper.updateByPrimaryKey(parameterPo);
	}

	@Override
	public void delete(SysParameterVo parameterVo) {
		//调用dao删除数据
		sysParameterMapper.deleteByPrimaryKey(parameterVo.getId());
	}

	@Override
	public long findCount() {
		return sysParameterMapper.findCount();
	}

}