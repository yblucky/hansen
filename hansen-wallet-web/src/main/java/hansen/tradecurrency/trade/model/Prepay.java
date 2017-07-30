package hansen.tradecurrency.trade.model;

import com.hansen.common.BaseModel;

import java.math.BigDecimal;

public class Prepay extends BaseModel {
	
 public static enum PrepayStatus{
		PROCESSING,
		HANDLED,
		CANCELLED
	}
	private String userId;
	private String prepayId;
	private String address;
	private BigDecimal amount;
	private PrepayStatus prepayStatus;
	private Long transactionLongTime;
	private String message;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	 

	public PrepayStatus getPrepayStatus() {
		return prepayStatus;
	}

	public void setPrepayStatus(PrepayStatus prepayStatus) {
		this.prepayStatus = prepayStatus;
	}

	public Long getTransactionLongTime() {
		return transactionLongTime;
	}

	public void setTransactionLongTime(Long transactionLongTime) {
		this.transactionLongTime = transactionLongTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPrepayId() {
		return prepayId;
	}

	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}

}
