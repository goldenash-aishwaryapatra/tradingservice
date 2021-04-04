package com.deutsche.tradestore.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deutsche.tradestore.data.TradeStorePayload;
import com.deutsche.tradestore.data.TradeStoreResponse;
import com.deutsche.tradestore.entity.Trade;
import com.deutsche.tradestore.exception.LowerVersionReceivedException;
import com.deutsche.tradestore.exception.TradestoreException;
import com.deutsche.tradestore.repository.TradeRepository;

/**
 * Implementations of methods in TradeService
 * 
 * @author aishwarya patra
 *
 */
@Service
public class TradeServiceImpl implements TradeService {

	@Autowired
	private TradeRepository tradeRepository;

	@Override
	public List<Trade> getAllTradeDetails() {
		return tradeRepository.findAll();
	}

	@Override
	public TradeStoreResponse storeTradeDetails(TradeStorePayload tradeDetail)
			throws LowerVersionReceivedException, TradestoreException {
		TradeStoreResponse tradeStoreResponse = new TradeStoreResponse();
		String tradeIdInPayload = tradeDetail.getTradeId();
		int versionInPayload = tradeDetail.getVersion();
		tradeStoreResponse.setTradeId(tradeIdInPayload);
		tradeStoreResponse.setVersion(versionInPayload);
		//checks if maturity date in payload is before today's date. if yes, throw TradestoreException
		if (!tradeDetail.getMaturityDate().before(new Date())) {
			//checks if records exist for tradeId
			if (CollectionUtils.isEmpty(tradeRepository.findByTradeStoreIdTradeId(tradeIdInPayload))) 
			{
				//if no, then create trade record
				persistTrade(tradeDetail);
				tradeStoreResponse.setMessage("CREATED");
			} 
			//if record exists for tradeId, then check if version has to be created or updated or throw LowerVersionReceivedException
			else 
			{
				int highestVersionAvailableForTradeId = tradeRepository.getMaxVersionForTradeId(tradeIdInPayload);
				//if version in payload is less than the highest version of trade stored in db, throw LowerVersionReceivedException
				if (highestVersionAvailableForTradeId > versionInPayload) 
				{
					throw new LowerVersionReceivedException(
							"lower version for tradeid " + tradeIdInPayload + " received");
				} else 
				{
					//if version in payload is more than the highest version of trade, create a trade and persist it
					if (highestVersionAvailableForTradeId < versionInPayload) 
					{
						persistTrade(tradeDetail);
						tradeStoreResponse.setMessage("CREATED");
					}
					//else update the same record
					else 
					{
						persistTrade(tradeDetail);
						tradeStoreResponse.setMessage("UPDATED");
					}
				}
			}
		} else
			throw new TradestoreException(
					"maturity date " + tradeDetail.getMaturityDate() + " is less than today's date");
		return tradeStoreResponse;
	}

	private void persistTrade(TradeStorePayload tradeDetail) {
		Trade newTrade = new Trade(tradeDetail);
		tradeRepository.save(newTrade);
	}

}
