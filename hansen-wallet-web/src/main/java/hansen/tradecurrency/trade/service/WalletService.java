package hansen.tradecurrency.trade.service;

import hansen.tradecurrency.trade.model.Transaction;

import java.math.BigDecimal;

public interface WalletService {
    public String getAccountAddress(String account);

    public void sendToAddress(Transaction transaction);

    public BigDecimal getBalance();
}
