package hansen.tradecurrency.trade.service;

import java.util.List;

import hansen.wallet.trade.model.Prepay;

public interface PrepayService {
	public Boolean insert(Prepay prepay);

	public Prepay selectById(Integer id);
	
	public Prepay selectPrepayByPrepayId(String prepayId);

	public Integer selectByPrepayId(String prepayId);

	public List<Prepay> listByStartToEnd(Long start, Long end);

	public Boolean updatePrepayId(String prepayId, String prepayStatus);
}
