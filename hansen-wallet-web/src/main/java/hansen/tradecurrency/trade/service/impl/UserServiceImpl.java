package hansen.tradecurrency.trade.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hansen.wallet.trade.dao.UserDao;
import hansen.wallet.trade.model.RtbUser;
import hansen.wallet.trade.service.UserService;
@Service
public class UserServiceImpl  implements UserService {
	@Autowired
	private UserDao userDao;

	@Override
	public Boolean insert(RtbUser user) {
		return userDao.insert(user);
	}

	@Override
	public RtbUser selectByUserId(Integer userId) {
		return userDao.selectByUserId(userId);
	}

}
