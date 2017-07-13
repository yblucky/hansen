package hansen.tradecurrency.trade.service;

import hansen.wallet.trade.model.RtbUser;

public interface UserService {
	public Boolean insert(RtbUser user);

	public RtbUser selectByUserId(Integer userId);
}
