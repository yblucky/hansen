package com.mall.service;

import java.util.List;

import com.mall.core.service.CommonService;
import com.mall.model.BaseUserAddress;

/**
 * @date 2016年11月27日
 */
public interface BaseUserAddressService extends CommonService<BaseUserAddress> {
	
	public List<BaseUserAddress> getAddressListByUserId(String userId); 
	
}
