package com.deutsche.tradestore.scheduler;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.deutsche.tradestore.entity.Trade;
import com.deutsche.tradestore.repository.TradeRepository;

/**
 * Scheduler to run the job every midnight to fetch trades whose maturity date
 * is before today's date
 * 
 * @author aishw
 *
 */
@Component
public class TradeExpiryScheduler {

	@Autowired
	private TradeRepository tradeRepository;

	@Scheduled(cron = "0 0 0 1/1 * ?")
	public void updateExpiredFlagForTrade() {
		Date currentDate = new Date();
		List<Trade> tradeList = tradeRepository.getTradesbyNotExpiredAndMaturityDateLessThanToday(currentDate);
		if (!CollectionUtils.isEmpty(tradeList)) {
			tradeList.forEach(trade -> {
				trade.setExpired(true);
				tradeRepository.save(trade);
			});
		}
	}
}
