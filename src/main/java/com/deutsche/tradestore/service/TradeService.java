package com.deutsche.tradestore.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.deutsche.tradestore.data.TradeStorePayload;
import com.deutsche.tradestore.data.TradeStoreResponse;
import com.deutsche.tradestore.entity.Trade;
import com.deutsche.tradestore.exception.LowerVersionReceivedException;
import com.deutsche.tradestore.exception.TradestoreException;

/**
 * Service layer
 * 
 * @author aishwarya patra
 *
 */
@Component
public interface TradeService {

	/**
	 * Method to get all trades persisted in db
	 * 
	 * @return
	 */
	public List<Trade> getAllTradeDetails();

	/**
	 * Method to create/update trade
	 * 
	 * @param tradeDetail
	 * @return
	 * @throws LowerVersionReceivedException
	 * @throws TradestoreException
	 */
	public TradeStoreResponse storeTradeDetails(TradeStorePayload tradeDetail)
			throws LowerVersionReceivedException, TradestoreException;
}
