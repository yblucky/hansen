package com.hansen.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hansen.constant.DrawType;
import com.hansen.constant.RecordType;
import com.hansen.constant.RedisKey;
import com.hansen.model.GoodsIssue;
import com.hansen.model.GoodsIssueDetail;
import com.hansen.model.WalletRecord;
import com.hansen.redis.Hash;
import com.hansen.redis.Strings;
import com.hansen.service.GoodsIssueDetailService;
import com.hansen.service.GoodsIssueService;
import com.hansen.service.MallGoodsService;
import com.hansen.service.BaseUserService;
import com.hansen.service.MallRecordService;

/**
 * 竞拍回退定时器
 * 
 * @author zhuzh
 * @date 2016年12月30日
 */
@Service
public class ResetIssueTask implements InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(ResetIssueTask.class);

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
		resetTask();
	}

	public void resetTask() {
		// 获取超过90天未开奖的期数列表
		try {
			List<GoodsIssue> timeoutList = goodsIssueService.getTimeoutList();
			if (timeoutList != null && timeoutList.size() > 0) {
				for (GoodsIssue issue : timeoutList) {
					try {
						resetIssueHandler(issue);
						if (logger.isInfoEnabled()) {
							logger.info("重置期数【" + issue.getId() + "】成功！！");
						}
					} catch (Exception e) {
						logger.error("重置期数【" + issue.getId() + "】失败！！" + e);
					}
				}
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Transactional
	private void resetIssueHandler(GoodsIssue issue) throws Exception {
		// 重置期数参与人数
		issue.setCurNum(0);
		goodsIssueService.update(issue.getId(), issue);
		// 修改参与用户订单状态，退回竞拍积分
		List<GoodsIssueDetail> drawList = goodsIssueDetailService.getDrawList(issue.getId(), DrawType.PENDING.getCode());
		if (drawList != null && drawList.size() > 0) {
			for (GoodsIssueDetail detail : drawList) {
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
		// 删除redis计数
		Strings.del(RedisKey.WATCH_KEY.getKey() + issue.getId());
		Hash.hdel(RedisKey.WATCH_LIST.getKey() + issue.getId());
	}

}
