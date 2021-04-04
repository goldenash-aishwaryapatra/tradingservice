package com.deutsche.tradestore.data;

import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * POJO class to store the input data for Trade
 * 
 * @author aishwarya patra
 *
 */
@Data
public class TradeStorePayload {

	@NotNull(message = "tradeId cannot be null")
	private String tradeId;

	@NotNull(message = "version cannot be null")
	@Min(value = 1, message = "version must be equal to or greater than 1")
	private Integer version;

	private String bookId;

	private String counterPartyId;

	@NotNull(message = "maturitydate cannot be null")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date maturityDate;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date createdDate;

	/**
	 * Contructor to create TradeStorePayload
	 * 
	 * @param tradeId
	 * @param version
	 * @param bookId
	 * @param counterPartyId
	 * @param maturityDate
	 * @param createdDate
	 */
	public TradeStorePayload(String tradeId, Integer version, String bookId, String counterPartyId, Date maturityDate,
			Date createdDate) {
		this.tradeId = tradeId;
		this.version = version;
		this.bookId = bookId;
		this.counterPartyId = counterPartyId;
		this.maturityDate = maturityDate;
		this.createdDate = createdDate;
	}
}
