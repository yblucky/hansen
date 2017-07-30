package hansen.tradecurrency.trade.service;


import hansen.tradecurrency.trade.model.Prepay;

import java.util.List;

public interface PrepayService {
    public Boolean insert(Prepay prepay);

    public Prepay selectById(Integer id);

    public Prepay selectPrepayByPrepayId(String prepayId);

    public Integer selectByPrepayId(String prepayId);

    public List<Prepay> listByStartToEnd(Long start, Long end);

    public Boolean updatePrepayId(String prepayId, String prepayStatus);
}
