
package hansen.bitcoin.client;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Holds transaction information, returned from both #getTransaction(String)
 * and listTransactions(String, int). In the first case, the category may be
 * null.
 *
 * @author mats@henricson.se
 * @since 0.3.18
 */
public class TransactionInfo {
    private String category="";     // Can be null, "generate", "send", "receive", or "move"
    private BigDecimal amount=new BigDecimal("0.0");   // Can be positive or negative
    private BigDecimal fee=new BigDecimal("0.0");      // Only for send, can be 0.0
    private long confirmations;  // only for generate/send/receive
    private String txId="";         // only for generate/send/receive
    private long time;
    private long blockTime;
    private long receviceTime;
    private long sendTime;
    private String message="";
    private Map<String, Object> details = new HashMap<String, Object>();

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public long getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(long confirmations) {
        this.confirmations = confirmations;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    /**
     * @deprecated No longer sent from bitcoind, function will always return null
     */
    @Deprecated
    public String getOtherAccount() {
        return "";
    }

    /**
     * @deprecated No longer sent from bitcoind, function will always return null
     */
    @Deprecated
    public String getMessage() {
        return "";
    }

    /**
     * @deprecated No longer sent from bitcoind, function will always return null
     */
    @Deprecated
    public String getTo() {
        return "";
    }

    
    public long getBlockTime() {
		return blockTime;
	}

	public void setBlockTime(long blockTime) {
		this.blockTime = blockTime;
	}

	public long getReceviceTime() {
		return receviceTime;
	}

	public void setReceviceTime(long receviceTime) {
		this.receviceTime = receviceTime;
	}

	public long getSendTime() {
		return sendTime;
	}

	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
	
	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Object> getDetails() {
		return details;
	}
	
	public void setDetails(Map<String, Object> details) {
		this.details = details;
	}

	
	
	@Override
    public String toString() {
        return "TransactionInfo{" +
                "category='" + category + '\'' +
                ", amount=" + amount +
                ", time=" + time +
                ", fee=" + fee +
                ", confirmations=" + confirmations +
                ", txId='" + txId + '\'' +
                '}';
    }

	
}
