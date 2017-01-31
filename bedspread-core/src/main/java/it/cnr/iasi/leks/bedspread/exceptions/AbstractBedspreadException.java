package it.cnr.iasi.leks.bedspread.exceptions;

public abstract class AbstractBedspreadException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AbstractBedspreadException() {
		super();
	}

	public AbstractBedspreadException(String message) {
		super(message);
	}

	public AbstractBedspreadException(String message, Throwable cause) {
		super(message, cause);
	}

	public AbstractBedspreadException(Throwable cause) {
		super(cause);
	}
}
