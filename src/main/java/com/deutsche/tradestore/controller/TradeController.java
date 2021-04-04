package com.deutsche.tradestore.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.deutsche.tradestore.data.TradeStorePayload;
import com.deutsche.tradestore.data.TradeStoreResponse;
import com.deutsche.tradestore.entity.Trade;
import com.deutsche.tradestore.exception.LowerVersionReceivedException;
import com.deutsche.tradestore.exception.TradestoreException;
import com.deutsche.tradestore.service.TradeService;

/**
 * Controller class to handle trades
 * 
 * @author aishwarya patra
 *
 */
@RestController
public class TradeController {

	@Autowired
	private TradeService tradeService;

	/**
	 * This returns all the trades stored in db
	 * 
	 * @return list of trades
	 */
	@GetMapping(value = "/tradedetails")
	public List<Trade> getAllTradeStoreDetails() {
		return tradeService.getAllTradeDetails();

	}

	/**
	 * This saves or updates trade in db
	 * 
	 * @param tradeDetail
	 * @param error
	 * @return TradeStoreResponse
	 */
	@PostMapping(value = "/savetradedetails")
	public TradeStoreResponse saveTradeDetails(@Valid @RequestBody TradeStorePayload tradeDetail, Errors error) {
		TradeStoreResponse tradeStoreResponse = new TradeStoreResponse();
		if (error.hasErrors()) {
			tradeStoreResponse.setTradeId(tradeDetail.getTradeId());
			tradeStoreResponse.setVersion(tradeDetail.getVersion());
			String response = error.getAllErrors().stream().map(a -> a.getDefaultMessage()).collect(Collectors.toList())
					.toString();
			tradeStoreResponse.setMessage(response);
		} else {

			try {
				tradeStoreResponse = tradeService.storeTradeDetails(tradeDetail);
			} catch (LowerVersionReceivedException | TradestoreException e) {

				tradeStoreResponse.setTradeId(tradeDetail.getTradeId());
				tradeStoreResponse.setVersion(tradeDetail.getVersion());
				tradeStoreResponse.setMessage(e.toString());
			}
		}

		return tradeStoreResponse;
	}
}
