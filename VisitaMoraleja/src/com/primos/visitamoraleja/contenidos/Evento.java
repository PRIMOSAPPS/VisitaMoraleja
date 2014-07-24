package com.primos.visitamoraleja.contenidos;

import java.io.Serializable;
import java.util.Date;

import android.graphics.Bitmap;

public class Evento implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3847802472159042786L;
	
	private long id;
	private long idCategoria;
	private long idSitio;
	private boolean esEvento;
	private String nombre;
	private String texto1;
	private String texto2;
	private String nombreIcono;
	private Bitmap icono;
	private double longitud;
	private double latitud;
	private Date inicio;
	private Date fin;
	private boolean activo;
	private Date ultimaActualizacion;
	


	public Evento() {
	}



	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public long getIdCategoria() {
		return idCategoria;
	}



	public void setIdCategoria(long idCategoria) {
		this.idCategoria = idCategoria;
	}



	public long getIdSitio() {
		return idSitio;
	}



	public void setIdSitio(long idSitio) {
		this.idSitio = idSitio;
	}



	public boolean isEsEvento() {
		return esEvento;
	}



	public void setEsEvento(boolean esEvento) {
		this.esEvento = esEvento;
	}



	public String getNombre() {
		return nombre;
	}



	public void setNombre(String nombre) {
		this.nombre = nombre;
	}



	public String getTexto1() {
		return texto1;
	}



	public void setTexto1(String texto1) {
		this.texto1 = texto1;
	}



	public String getTexto2() {
		return texto2;
	}



	public void setTexto2(String texto2) {
		this.texto2 = texto2;
	}



	public String getNombreIcono() {
		return nombreIcono;
	}



	public void setNombreIcono(String nombreIcono) {
		this.nombreIcono = nombreIcono;
	}



	public double getLongitud() {
		return longitud;
	}



	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}



	public double getLatitud() {
		return latitud;
	}



	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}



	public Date getInicio() {
		return inicio;
	}



	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}



	public Date getFin() {
		return fin;
	}



	public void setFin(Date fin) {
		this.fin = fin;
	}



	public boolean isActivo() {
		return activo;
	}



	public void setActivo(boolean activo) {
		this.activo = activo;
	}



	public Date getUltimaActualizacion() {
		return ultimaActualizacion;
	}



	public void setUltimaActualizacion(Date ultimaActualizacion) {
		this.ultimaActualizacion = ultimaActualizacion;
	}



	public Bitmap getIcono() {
		return icono;
	}



	public void setIcono(Bitmap icono) {
		this.icono = icono;
	}



	@Override
	public String toString() {
		return "Evento [id=" + id + ", idCategoria=" + idCategoria
				+ ", idSitio=" + idSitio + ", nombre=" + nombre
				+ ", nombreIcono=" + nombreIcono + "]";
	}

}
