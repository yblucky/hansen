package com.mall.model;

import com.mall.common.BaseModel;

/**
 * 用户部门表
 *
 * @date 2016年12月7日
 */
public class BaseUserDepartment extends BaseModel {

	private static final long serialVersionUID = 241493373209830052L;
	/** 用户ID */
	private String userId;
	/** uid */
	private Integer uid;
	/** 业绩 */
	private Double performance;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Double getPerformance() {
		return performance;
	}

	public void setPerformance(Double performance) {
		this.performance = performance;
	}
}
