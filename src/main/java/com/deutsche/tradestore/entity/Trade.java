package com.deutsche.tradestore.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.deutsche.tradestore.data.TradeStorePayload;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity class for "TRADE" table
 * 
 * @author aishwarya patra
 *
 */
@Data
@Entity
@Table(name = "trade")
@NoArgsConstructor
@AllArgsConstructor
public class Trade implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Trade(TradeStorePayload tradeDetail) {
		this.tradeStoreId = new TradeStoreId(tradeDetail.getTradeId(), tradeDetail.getVersion());
		this.bookId = tradeDetail.getBookId();
		this.counterPartyId = tradeDetail.getCounterPartyId();
		this.maturityDate = new Date(tradeDetail.getMaturityDate().getTime());
		if (null == tradeDetail.getCreatedDate())
			this.createdDate = setCreatedDate();
		else
			this.createdDate = new Date(tradeDetail.getCreatedDate().getTime());
	}

	@EmbeddedId
	private TradeStoreId tradeStoreId;

	@Column(name = "counterpartyid")
	private String counterPartyId;

	@Column(name = "bookid")
	private String bookId;

	@Column(name = "maturitydate")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date maturityDate;

	@Column(name = "createddate")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date createdDate;

	@Column(name = "expired")
	private boolean expired;

	/**
	 * Custom method to return created Date
	 * 
	 * @return current date
	 */
	private Date setCreatedDate() {
		return new Date(new java.util.Date().getTime());
	}

}
