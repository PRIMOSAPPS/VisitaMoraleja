package com.primos.visitamoraleja.excepcion;

public class EventosException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3010903443481024846L;

	public EventosException() {
		super();
	}

	public EventosException(String detailMessage) {
		super(detailMessage);
	}

	public EventosException(Throwable throwable) {
		super(throwable);
	}

	public EventosException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

}
