package hansen.tradecurrency.trade.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import hansen.wallet.trade.model.Prepay;

public interface PrepayDao {
	public Boolean insert(Prepay prepay);

	public Prepay selectById(Integer id);
	
	public Prepay selectPrepayByPrepayId(@Param("prepayId") String pyPrepayId);
	
	public Integer selectByPrepayId(@Param("prepayId") String prepayId);

	public List<Prepay> listByStartToEnd(@Param("start") Long start, @Param("end") Long end);

	public Boolean updatePrepayId(@Param("prepayId") String prepayId, @Param("prepayStatus") String prepayStatus);

}
