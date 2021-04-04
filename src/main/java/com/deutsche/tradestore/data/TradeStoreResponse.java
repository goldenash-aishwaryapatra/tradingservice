package com.deutsche.tradestore.data;

import lombok.Data;

/**
 * POJO to store the response when Trade is either created or updated or in case
 * of any exceptions
 * 
 * @author aishwarya patra
 *
 */
@Data
public class TradeStoreResponse {

	private String tradeId;
	private int version;
	private String message;
}
