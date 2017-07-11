package com.api.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.constant.DrawType;
import com.api.constant.IssueType;
import com.api.constant.RecordType;
import com.api.model.GoodsIssue;
import com.api.model.GoodsIssueDetail;
import com.api.model.WalletRecord;
import com.api.service.GoodsIssueDetailService;
import com.api.service.GoodsIssueService;
import com.api.service.MallGoodsService;
import com.api.service.BaseUserService;
import com.api.service.MallRecordService;

/**
 * 竞拍回退定时器
 * 
 * @author zhuzh
 * @date 2016年12月30日
 */
@Service
public class RollbackTask implements InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(RollbackTask.class);

	@Autowired
	private BaseUserService userService;
	@Autowired
	private MallGoodsService goodsService;
	@Autowired
	private GoodsIssueService goodsIssueService;
	@Autowired
	private GoodsIssueDetailService goodsIssueDetailService;
	@Autowired
	private MallRecordService walletRecordService;

	@Override
	public void afterPropertiesSet() throws Exception {
		rollbackTask();
	}

	public void rollbackTask() {
		try {
			List<GoodsIssueDetail> rollbackDrawList = goodsIssueDetailService.getRollbackDrawList();
			if (rollbackDrawList != null && rollbackDrawList.size() > 0) {
				for (GoodsIssueDetail detail : rollbackDrawList) {
					try {
						rollbackIssueHandler(detail);
						if (logger.isInfoEnabled()) {
							logger.info("退款【" + detail.getId() + "】成功！！");
						}
					} catch (Exception e) {
						logger.error("退款【" + detail.getId() + "】失败！！" + e);
					}
				}
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Transactional
	private void rollbackIssueHandler(GoodsIssueDetail detail) throws Exception {
		GoodsIssue issue = goodsIssueService.getById(detail.getIssueId());
		if (issue == null || issue.getStatus() != IssueType.FINISH.getCode()) {
			return;
		}
		// 修改参与用户订单状态，退回竞拍积分
		detail.setStatus(DrawType.CANCEL.getCode());
		goodsIssueDetailService.update(detail.getId(), detail);
		// 退回积分
		userService.updateScore(detail.getUserId(), detail.getDrawPrice());
		// 增加积分流水
		WalletRecord record = new WalletRecord();
		record.setUserId(detail.getUserId());
		record.setOrderNo(detail.getOrderNo());
		record.setScore(detail.getDrawPrice());
		record.setRecordType(RecordType.ROLLBACK.getCode());
		record.setRemark(RecordType.ROLLBACK.getMsg());
		walletRecordService.add(record);
	}

}
