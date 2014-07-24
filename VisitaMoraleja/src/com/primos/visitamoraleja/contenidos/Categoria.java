package com.primos.visitamoraleja.contenidos;

import java.io.Serializable;
import java.util.Date;

import android.graphics.Bitmap;

public class Categoria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7729847548952023660L;

	private long id;
	private String nombre;
	private String descripcion;
	private String nombreIcono;
	private Bitmap icono;
	private int numeroSitios;
	private Date ultimaActualizacion;

	public Categoria() {
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

	public int getNumeroSitios() {
		return numeroSitios;
	}

	public void setNumeroSitios(int numeroSitios) {
		this.numeroSitios = numeroSitios;
	}

	public Bitmap getIcono() {
		return icono;
	}

	public void setIcono(Bitmap icono) {
		this.icono = icono;
	}

	@Override
	public String toString() {
		return "Categoria [id=" + id + ", nombre=" + nombre + ", descripcion="
				+ descripcion + ", nombreIcono=" + nombreIcono
				+ ", numeroSitios=" + numeroSitios + "]";
	}

	public Date getUltimaActualizacion() {
		return ultimaActualizacion;
	}

	public void setUltimaActualizacion(Date ultimaActualizacion) {
		this.ultimaActualizacion = ultimaActualizacion;
	}

}
