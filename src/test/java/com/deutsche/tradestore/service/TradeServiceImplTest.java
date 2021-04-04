package com.deutsche.tradestore.service;

import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.deutsche.tradestore.data.TradeStorePayload;
import com.deutsche.tradestore.data.TradeStoreResponse;
import com.deutsche.tradestore.entity.Trade;
import com.deutsche.tradestore.entity.TradeStoreId;
import com.deutsche.tradestore.exception.LowerVersionReceivedException;
import com.deutsche.tradestore.exception.TradestoreException;
import com.deutsche.tradestore.repository.TradeRepository;

@RunWith(MockitoJUnitRunner.class)
public class TradeServiceImplTest {

	@InjectMocks
	private TradeServiceImpl tradeService;
	
	@Mock
	private TradeRepository tradeRepository;
	
	private Trade trade;
	private Trade trade1;
	private TradeStorePayload tradeDetail;
	private TradeStorePayload tradeDetail2;
	private TradeStorePayload tradeDetail3;
	List<Trade> tradeList = new ArrayList<>();
	@Before
	public void setUp() throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String mdString = "07/04/2021";
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
		
		
		tradeList.add(trade);
		tradeDetail = new TradeStorePayload("T1", 1, "B1", "CP1", maturityDate, createdDate);
		tradeDetail3 = new TradeStorePayload("T1", 2, "B1", "CP1", maturityDate, createdDate);
		String mdString1 = "01/04/2021";
		Date maturityDateLessThanToday = new Date(sdf.parse(mdString1).getTime());
		tradeDetail2 = new TradeStorePayload("T1", 1, "B1", "CP1", maturityDateLessThanToday, createdDate);
	}
	@Test
	public void testgetAllTradeDetails()
	{
		List<Trade> tradeList = new ArrayList<>();
		tradeList.add(trade);
		tradeList.add(trade1);
		Mockito.when(tradeRepository.findAll()).thenReturn(tradeList);
		List<Trade> actualList = tradeService.getAllTradeDetails();
		assertEquals(tradeList, actualList);
	}
	
	@Test
	public void teststoreTradeDetailsWhenTradeIsCreated() throws LowerVersionReceivedException, TradestoreException
	{
		TradeStoreResponse tradeStoreResponse = new TradeStoreResponse();
		tradeStoreResponse.setMessage("CREATED");
		tradeStoreResponse.setTradeId("T1");
		tradeStoreResponse.setVersion(1);
		Mockito.when(tradeRepository.findByTradeStoreIdTradeId("T1")).thenReturn(null);
		TradeStoreResponse actualResponse = tradeService.storeTradeDetails(tradeDetail);
		assertEquals(tradeStoreResponse, actualResponse);
	}
	
	@Test
	public void teststoreTradeDetailsWhenTradeIsUpdated() throws LowerVersionReceivedException, TradestoreException
	{
		TradeStoreResponse tradeStoreResponse = new TradeStoreResponse();
		tradeStoreResponse.setMessage("UPDATED");
		tradeStoreResponse.setTradeId("T1");
		tradeStoreResponse.setVersion(1);
		Mockito.when(tradeRepository.findByTradeStoreIdTradeId("T1")).thenReturn(tradeList);
		Mockito.when(tradeRepository.getMaxVersionForTradeId("T1")).thenReturn(1);
		TradeStoreResponse actualResponse = tradeService.storeTradeDetails(tradeDetail);
		assertEquals(tradeStoreResponse, actualResponse);
	}
	
	@Test
	public void teststoreTradeDetailsWhenAnotherVersionOfTradeIsCreated() throws LowerVersionReceivedException, TradestoreException
	{
		TradeStoreResponse tradeStoreResponse = new TradeStoreResponse();
		tradeStoreResponse.setMessage("CREATED");
		tradeStoreResponse.setTradeId("T1");
		tradeStoreResponse.setVersion(2);
		Mockito.when(tradeRepository.findByTradeStoreIdTradeId("T1")).thenReturn(tradeList);
		Mockito.when(tradeRepository.getMaxVersionForTradeId("T1")).thenReturn(1);
		TradeStoreResponse actualResponse = tradeService.storeTradeDetails(tradeDetail3);
		assertEquals(tradeStoreResponse, actualResponse);
	}
	
	@Test
	public void teststoreTradeDetailsWhenSameVersionOfTradeIsPassed() throws LowerVersionReceivedException, TradestoreException
	{
		TradeStoreResponse tradeStoreResponse = new TradeStoreResponse();
		tradeStoreResponse.setMessage("UPDATED");
		tradeStoreResponse.setTradeId("T1");
		tradeStoreResponse.setVersion(2);
		Mockito.when(tradeRepository.findByTradeStoreIdTradeId("T1")).thenReturn(tradeList);
		Mockito.when(tradeRepository.getMaxVersionForTradeId("T1")).thenReturn(2);
		TradeStoreResponse actualResponse = tradeService.storeTradeDetails(tradeDetail3);
		assertEquals(tradeStoreResponse, actualResponse);
	}
	
	@Test(expected = LowerVersionReceivedException.class)
	public void teststoreTradeDetailsWithLowerVersionReceivedException() throws LowerVersionReceivedException, TradestoreException
	{
		Mockito.when(tradeRepository.findByTradeStoreIdTradeId("T1")).thenReturn(tradeList);
		Mockito.when(tradeRepository.getMaxVersionForTradeId("T1")).thenReturn(2);
		tradeService.storeTradeDetails(tradeDetail);
	}
	
	@Test(expected = TradestoreException.class)
	public void teststoreTradeDetailsWithTradestoreExceptionForMaturityDateLessThanToday() throws LowerVersionReceivedException, TradestoreException
	{
		tradeService.storeTradeDetails(tradeDetail2);
	}
}
