package com.deutsche.tradestore.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Test;

import com.deutsche.tradestore.data.TradeStorePayload;

public class TradeTest {

	@Test
	public void testTradeWithTradeStorePayloadWhenCreatedDateIsPassed() throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String mdString = "02/03/2021";
		Date maturityDate = new Date(sdf.parse(mdString).getTime());
		String curentTime = "03/04/2021";
		Date createdDate = new Date(sdf.parse(curentTime).getTime());
		TradeStorePayload tradeDetail = new TradeStorePayload("T1", 1, "B1", "CP1", maturityDate, createdDate);
		Trade trade = new Trade(tradeDetail);
		assertEquals("T1", trade.getTradeStoreId().getTradeId());
		assertEquals(1, trade.getTradeStoreId().getVersion());
		assertEquals("B1", trade.getBookId());
		assertEquals("CP1", trade.getCounterPartyId());
		assertEquals("2021-03-02", trade.getMaturityDate().toString());
		assertEquals("2021-04-03", trade.getCreatedDate().toString());
		assertFalse(trade.isExpired());
	}
	@Test
	public void testTradeWithTradeStorePayloadWhenCreatedDateIsNull() throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String mdString = "02/03/2021";
		Date maturityDate = new Date(sdf.parse(mdString).getTime());
		TradeStorePayload tradeDetail = new TradeStorePayload("T1", 1, "B1", "CP1", maturityDate, null);
		Trade trade = new Trade(tradeDetail);
		assertEquals("T1", trade.getTradeStoreId().getTradeId());
		assertEquals(1, trade.getTradeStoreId().getVersion());
		assertEquals("B1", trade.getBookId());
		assertEquals("CP1", trade.getCounterPartyId());
		assertEquals("2021-03-02", trade.getMaturityDate().toString());
		assertEquals(new Date(new java.util.Date().getTime()).toString(), trade.getCreatedDate().toString());
		assertFalse(trade.isExpired());
	}
}
