package com.deutsche.tradestore.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.deutsche.tradestore.entity.Trade;
import com.deutsche.tradestore.entity.TradeStoreId;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TradeRepositoryTest {

	@Autowired
	private TradeRepository tradeRepository;
	
	private Trade trade;
	private Trade trade1;
	@Before
	public void setUp() throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String mdString = "02/03/2021";
		Date maturityDate = new Date(sdf.parse(mdString).getTime());
		String curentTime = "03/04/2021";
		Date createdDate = new Date(sdf.parse(curentTime).getTime());
		trade = new Trade();
		TradeStoreId tradeStoreId = new TradeStoreId("T1", 1);
		trade.setTradeStoreId(tradeStoreId);
		trade.setCounterPartyId("CP1");
		trade.setBookId("B1");
		trade.setMaturityDate(maturityDate);
		trade.setCreatedDate(createdDate);
		
		trade1 = new Trade();
		TradeStoreId tradeStoreId1 = new TradeStoreId("T1", 2);
		trade1.setTradeStoreId(tradeStoreId1);
		trade1.setCounterPartyId("CP1");
		trade1.setBookId("B1");
		trade1.setMaturityDate(maturityDate);
		trade1.setCreatedDate(createdDate);
		
		tradeRepository.save(trade);
		tradeRepository.save(trade1);
		
	}
	
	@After
	public void destroy()
	{
		tradeRepository.deleteAll();
	}
	@Test
	public void testfindByTradeStoreIdTradeIdAndTradeStoreIdVersion()
	{
		List<Trade> tradeList = tradeRepository.findByTradeStoreIdTradeId("T1");
		assertEquals(2, tradeList.size());
		
		tradeList = tradeRepository.findByTradeStoreIdTradeId("T2");
		assertEquals(0, tradeList.size());
	}
	
	@Test
	public void testgetMaxVersionForTradeId()
	{
		assertEquals(2, tradeRepository.getMaxVersionForTradeId("T1"));
	}
	
	@Test
	public void testgetTradesbyNotExpiredAndMaturityDateLessThanToday()
	{
		List<Trade> tradeList = new ArrayList<>();
		tradeList.add(trade);
		tradeList.add(trade1);
		java.util.Date currentDate = new java.util.Date();
		List<Trade> actualList = tradeRepository.getTradesbyNotExpiredAndMaturityDateLessThanToday(currentDate);
		assertEquals(tradeList, actualList);
	}
}
