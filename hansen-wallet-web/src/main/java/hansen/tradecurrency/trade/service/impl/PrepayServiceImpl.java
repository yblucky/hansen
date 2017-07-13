package hansen.tradecurrency.trade.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hansen.wallet.trade.dao.PrepayDao;
import hansen.wallet.trade.model.Prepay;
import hansen.wallet.trade.service.PrepayService;

@Service
public class PrepayServiceImpl implements PrepayService {
	@Autowired
	private PrepayDao prepayDao;

	@Override
	public Boolean insert(Prepay prepay) {
		return prepayDao.insert(prepay);
	}

	@Override
	public Prepay selectById(Integer id) {
		return prepayDao.selectById(id);
	}

	@Override
	public Prepay selectPrepayByPrepayId(String prepayId) {
		return prepayDao.selectPrepayByPrepayId(prepayId);
	}
	
	@Override
	public Integer selectByPrepayId(String prepayId) {
		return prepayDao.selectByPrepayId(prepayId);
	}

	@Override
	public List<Prepay> listByStartToEnd(Long start, Long end) {
		return prepayDao.listByStartToEnd(start, end);
	}

	@Override
	public Boolean updatePrepayId(String prepayId,String prepayStatus) {
		return prepayDao.updatePrepayId(prepayId,prepayStatus);
	}
	
}
