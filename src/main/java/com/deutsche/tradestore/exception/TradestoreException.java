package com.deutsche.tradestore.exception;

import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class TradestoreException extends Throwable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public TradestoreException(String message)
	{
		super(message);
	}

}
