package com.deutsche.tradestore.exception;

import org.springframework.stereotype.Component;

@Component
public class LowerVersionReceivedException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public LowerVersionReceivedException()
	{
		
	}
	public LowerVersionReceivedException(String message)
	{
		super(message);
	}

}
