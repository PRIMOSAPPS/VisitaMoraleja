package com.primos.visitamoraleja.contenidos;

import android.graphics.Bitmap;

import com.primos.visitamoraleja.util.UtilFechas;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Evento implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3847802472159042786L;
	
	private long id;
	private String nombre;
	private String texto;
	private String descripcion;
	private String nombreIcono;
	private Bitmap icono;
	private double longitud;
	private double latitud;
	private float zoomInicial;
	private Date inicio;
	private Date fin;
	private boolean activo;
	private Date ultimaActualizacion;
	private List<ImagenEvento> imagenes;
	


	public Evento() {
	}


	public float getZoomInicial() {
		return zoomInicial;
	}

	public void setZoomInicial(float zoomInicial) {
		this.zoomInicial = zoomInicial;
	}

	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public String getNombre() {
		return nombre;
	}



	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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

	/**
	 * Devuelve true si la fecha actual esta entre el inicio y el fin del evento
	 * @return
	 */
	public boolean isActivoPorFecha() {
		Date inicio = getInicio();
		Date fin = getFin();
		return UtilFechas.isActivaFechaActual(inicio, fin);
	}

	@Override
	public String toString() {
		return "Evento [id=" + id + ", nombre=" + nombre
				+ ", nombreIcono=" + nombreIcono + "]";
	}

}
