package it.cnr.iasi.leks.bedspread.exceptions.impl;

import it.cnr.iasi.leks.bedspread.exceptions.AbstractBedspreadException;

public class InteractionProtocolViolationException extends AbstractBedspreadException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8993234614736446559L;
	
	public InteractionProtocolViolationException() {
		super();
	}

	public InteractionProtocolViolationException(String message) {
		super(message);
	}

	public InteractionProtocolViolationException(String message, Throwable cause) {
		super(message, cause);
	}

	public InteractionProtocolViolationException(Throwable cause) {
		super(cause);
	}
}
