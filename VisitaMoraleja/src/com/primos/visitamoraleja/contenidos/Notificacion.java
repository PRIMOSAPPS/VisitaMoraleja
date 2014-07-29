package com.primos.visitamoraleja.contenidos;

import java.io.Serializable;
import java.util.Date;

public class Notificacion implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8881102854923668978L;
	
	private long id;
	private String titulo;
	private String texto;
	private Date fechaInicioValidez;
	private Date fechaFinValidez;

	public Notificacion() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Date getFechaInicioValidez() {
		return fechaInicioValidez;
	}

	public void setFechaInicioValidez(Date fechaInicioValidez) {
		this.fechaInicioValidez = fechaInicioValidez;
	}

	public Date getFechaFinValidez() {
		return fechaFinValidez;
	}

	public void setFechaFinValidez(Date fechaFinValidez) {
		this.fechaFinValidez = fechaFinValidez;
	}
	
	public boolean isActiva() {
		boolean resul = false;
		long ahoraMilis = new Date().getTime();
		Date inicio = getFechaInicioValidez();
		Date fin = getFechaFinValidez();
		if(inicio == null && fin == null) {
			resul = true;
		} else if (inicio == null) {
			long finMilis = fin.getTime();
			resul = ahoraMilis < finMilis;
		} else if (fin == null) {
			long inicioMilis = inicio.getTime();
			resul = ahoraMilis > inicioMilis;
		} else {
			long finMilis = fin.getTime();
			long inicioMilis = inicio.getTime();
			resul = ahoraMilis > inicioMilis && ahoraMilis < finMilis;
		}
		return resul;
	}

}
