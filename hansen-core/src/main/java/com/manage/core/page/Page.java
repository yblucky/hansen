package com.manage.core.page;

import java.io.Serializable;


public class Page implements Serializable {

	private static final long serialVersionUID = 28122347460961243L;

	/** 当前页 */
	private Integer pageNo = 1;
	/** 每页大小 */
	private Integer pageSize = 10;
	/** 查询开始时间 */
	private String startTime;
	/** 查询结束时间 */
	private String endTime;
	/** 查询开始记录索引 */
	private Integer startRow;
	/** 时间排序 */
	private Integer timeSort;
	/** 价格排序 */
	private Integer priceSort;

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		if (pageNo == null || pageNo < 1) {
			pageNo = 1;
		}
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		if (pageSize == null || pageSize < 1) {
			pageNo = 10;
		}
		this.pageSize = pageSize;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getStartRow() {
		if (pageNo == null || pageNo < 1) {
			startRow = 1;
		} else {
			startRow = (pageNo - 1) * pageSize;
		}
		return startRow;
	}

	public void setStartRow(Integer startRow) {
		this.startRow = startRow;
	}

	public Integer getTimeSort() {
		return timeSort;
	}

	public void setTimeSort(Integer timeSort) {
		this.timeSort = timeSort;
	}

	public Integer getPriceSort() {
		return priceSort;
	}

	public void setPriceSort(Integer priceSort) {
		this.priceSort = priceSort;
	}

}
