package it.cnr.iasi.leks.bedspread.exceptions.impl;

import it.cnr.iasi.leks.bedspread.exceptions.AbstractBedspreadException;

public class UnexpectedValueException extends AbstractBedspreadException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 314140743094275218L;

	public UnexpectedValueException() {
		super();
	}

	public UnexpectedValueException(String message) {
		super(message);
	}

	public UnexpectedValueException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnexpectedValueException(Throwable cause) {
		super(cause);
	}

}
