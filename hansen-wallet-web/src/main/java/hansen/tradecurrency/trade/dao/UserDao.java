package hansen.tradecurrency.trade.dao;


import hansen.wallet.trade.model.RtbUser;

public interface UserDao {
	public Boolean insert(RtbUser user);

	public RtbUser selectByUserId(Integer userId);

}
