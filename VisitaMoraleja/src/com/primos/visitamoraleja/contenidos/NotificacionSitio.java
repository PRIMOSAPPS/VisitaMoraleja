package com.primos.visitamoraleja.contenidos;

/**
 * Clase que relaciona una notificacion y su sitio asociado.
 * 
 * @author h
 * 
 */
public class NotificacionSitio {
	private Notificacion notificacion;
	private Sitio sitio;

	public NotificacionSitio(Notificacion notificacion, Sitio sitio) {
		super();
		this.notificacion = notificacion;
		this.sitio = sitio;
	}

	public Notificacion getNotificacion() {
		return notificacion;
	}

	public Sitio getSitio() {
		return sitio;
	}

}
