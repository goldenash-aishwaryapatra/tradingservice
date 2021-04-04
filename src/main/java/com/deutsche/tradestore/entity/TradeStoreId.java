package com.deutsche.tradestore.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * C
 * @author aishw
 *
 */
@Embeddable
@Data
@NoArgsConstructor
public class TradeStoreId implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name = "tradeid")
	private String tradeId;
	
	@Column(name = "version")
	private int version;
	
	public TradeStoreId(String tradeId, int version)
	{
		this.tradeId = tradeId;
		this.version = version;
	}

}
