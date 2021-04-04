package com.deutsche.tradestore.scheduler;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.deutsche.tradestore.entity.Trade;
import com.deutsche.tradestore.entity.TradeStoreId;
import com.deutsche.tradestore.repository.TradeRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = "trade.expiry.scheduler.rate=1000")
public class TradeExpirySchedulerTest {

	@SpyBean
	private TradeExpiryScheduler tradeExpiryScheduler;
	
	@MockBean
	private TradeRepository tradeRepository;
	

	    @Test
	    public void cronSchedulerGenerator_1() {
	    	CronSequenceGenerator cron1 = new CronSequenceGenerator("0 0 0 1/1 * ?");
	    	Calendar cal = Calendar.getInstance();
	    	System.out.println("Next cron trigger date cron1 "+cron1.next(cal.getTime()));
	    }
	    
	@Test
    public void updateExpiredFlagForTrade() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String mdString = "02/03/2021";
		Date maturityDate = new Date(sdf.parse(mdString).getTime());
		String curentTime = "03/04/2021";
		Date createdDate = new Date(sdf.parse(curentTime).getTime());
		Trade trade = new Trade();
		TradeStoreId tradeStoreId = new TradeStoreId("T1", 1);
		trade.setTradeStoreId(tradeStoreId);
		trade.setCounterPartyId("CP1");
		trade.setBookId("B1");
		trade.setMaturityDate(maturityDate);
		trade.setCreatedDate(createdDate);

		Trade trade1 = new Trade();
		trade.setTradeStoreId(tradeStoreId);
		trade1.setCounterPartyId("CP1");
		trade1.setBookId("B1");
		trade1.setMaturityDate(maturityDate);
		trade1.setCreatedDate(createdDate);

		List<Trade> tradeList = new ArrayList<>();
		tradeList.add(trade);
		tradeList.add(trade1);
		
		Mockito.when(tradeRepository.getTradesbyNotExpiredAndMaturityDateLessThanToday(Mockito.any(java.util.Date.class))).thenReturn(tradeList);
		tradeExpiryScheduler.updateExpiredFlagForTrade();
		Mockito.verify(tradeRepository).save(trade);
		Mockito.verify(tradeRepository).save(trade1);
    }
}
