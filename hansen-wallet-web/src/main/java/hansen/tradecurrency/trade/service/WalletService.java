package hansen.tradecurrency.trade.service;

import java.math.BigDecimal;

import hansen.wallet.trade.model.Transaction;

public interface WalletService {
	public String getAccountAddress(String account);
	
	public void sendToAddress(Transaction transaction);
	
	public BigDecimal getBalance();
}
