package ru.paradoxs.bitcoin.client;

import java.math.BigDecimal;

public class TransactionInfo {
    private String account;
    private String address;
    private String comment;
    private String blockhash;
    private Integer blockindex;
    private long time;
    private long timereceived;
    /**1491310031**/
    private Long blocktime;
    private String category;
    private BigDecimal amount;
    private BigDecimal fee;
    private long confirmations;
    private String txId;
    private String otherAccount;
    private String message;
    private String to;


    public TransactionInfo() {
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFee() {
        return this.fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public long getConfirmations() {
        return this.confirmations;
    }

    public void setConfirmations(long confirmations) {
        this.confirmations = confirmations;
    }

    public String getTxId() {
        return this.txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public String getOtherAccount() {
        return this.otherAccount;
    }

    public void setOtherAccount(String otherAccount) {
        this.otherAccount = otherAccount;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTo() {
        return this.to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getBlockhash() {
        return blockhash;
    }

    public void setBlockhash(String blockhash) {
        this.blockhash = blockhash;
    }

    public Integer getBlockindex() {
        return blockindex;
    }

    public void setBlockindex(Integer blockindex) {
        this.blockindex = blockindex;
    }

    public Long getBlocktime() {
        return blocktime;
    }

    public void setBlocktime(Long blocktime) {
        this.blocktime = blocktime;
    }

    public long getTimereceived() {
        return timereceived;
    }

    public void setTimereceived(long timereceived) {
        this.timereceived = timereceived;
    }
}
