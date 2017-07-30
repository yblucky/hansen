package hansen.tradecurrency.trade.vo;

import com.hansen.common.utils.toolutils.ToolUtil;

public class TransactionInfoVo {
	
	private String address;
	private String amount;
	private String category;
	private String comment;
	private String commentTo;
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getCommentTo() {
		return commentTo;
	}
	public void setCommentTo(String commentTo) {
		this.commentTo = "";
	}
	public String getCategory() {
		if (ToolUtil.isEmpty(category)) {
			category="SEND";
		}
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
}
