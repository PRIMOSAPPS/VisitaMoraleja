package com.primos.visitamoraleja.contenidos;

import java.io.Serializable;
import java.util.Date;

import android.graphics.Bitmap;

public class Sitio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1572400769597339508L;
	
	private long id;
	private long idCategoria;
	private String nombre;
	private String poblacion;
	private String direccion;
	private String textoCorto1;
	private String textoCorto2;
	private String textoCorto3;
	private String textoLargo1;
	private String textoLargo2;
	private String nombreLogotipo;
	private Bitmap logotipo;
	private String nombreImagen1;
	private Bitmap imagen1;
	private String nombreImagen2;
	private Bitmap imagen2;
	private String nombreImagen3;
	private Bitmap imagen3;
	private String nombreImagen4;
	private Bitmap imagen4;
	private double longitud;
	private double latitud;
	private String telefonosFijos;
	private String telefonosMoviles;
	private String web;
	private String facebook;
	private String twitter;
	private int ranking;
	private boolean favorito;
	private boolean activo;
	private Date ultimaActualizacion;


	public Sitio() {
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


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getPoblacion() {
		return poblacion;
	}


	public void setPoblacion(String poblacion) {
		this.poblacion = poblacion;
	}


	public String getTextoCorto1() {
		return textoCorto1;
	}


	public void setTextoCorto1(String textoCorto1) {
		this.textoCorto1 = textoCorto1;
	}


	public String getTextoCorto2() {
		return textoCorto2;
	}


	public void setTextoCorto2(String textoCorto2) {
		this.textoCorto2 = textoCorto2;
	}


	public String getTextoCorto3() {
		return textoCorto3;
	}


	public void setTextoCorto3(String textoCorto3) {
		this.textoCorto3 = textoCorto3;
	}


	public String getTextoLargo1() {
		return textoLargo1;
	}


	public void setTextoLargo1(String textoLargo1) {
		this.textoLargo1 = textoLargo1;
	}


	public String getTextoLargo2() {
		return textoLargo2;
	}


	public void setTextoLargo2(String textoLargo2) {
		this.textoLargo2 = textoLargo2;
	}


	public String getNombreLogotipo() {
		return nombreLogotipo;
	}


	public void setNombreLogotipo(String nombreLogotipo) {
		this.nombreLogotipo = nombreLogotipo;
	}


	public Bitmap getLogotipo() {
		return logotipo;
	}


	public void setLogotipo(Bitmap logotipo) {
		this.logotipo = logotipo;
	}


	public String getNombreImagen1() {
		return nombreImagen1;
	}


	public void setNombreImagen1(String nombreImagen1) {
		this.nombreImagen1 = nombreImagen1;
	}


	public Bitmap getImagen1() {
		return imagen1;
	}


	public void setImagen1(Bitmap imagen1) {
		this.imagen1 = imagen1;
	}


	public String getNombreImagen2() {
		return nombreImagen2;
	}


	public void setNombreImagen2(String nombreImagen2) {
		this.nombreImagen2 = nombreImagen2;
	}


	public Bitmap getImagen2() {
		return imagen2;
	}


	public void setImagen2(Bitmap imagen2) {
		this.imagen2 = imagen2;
	}


	public String getNombreImagen3() {
		return nombreImagen3;
	}


	public void setNombreImagen3(String nombreImagen3) {
		this.nombreImagen3 = nombreImagen3;
	}


	public Bitmap getImagen3() {
		return imagen3;
	}


	public void setImagen3(Bitmap imagen3) {
		this.imagen3 = imagen3;
	}


	public String getNombreImagen4() {
		return nombreImagen4;
	}


	public void setNombreImagen4(String nombreImagen4) {
		this.nombreImagen4 = nombreImagen4;
	}


	public Bitmap getImagen4() {
		return imagen4;
	}


	public void setImagen4(Bitmap imagen4) {
		this.imagen4 = imagen4;
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


	public String getTelefonosFijos() {
		return telefonosFijos;
	}


	public void setTelefonosFijos(String telefonosFijos) {
		this.telefonosFijos = telefonosFijos;
	}


	public String getTelefonosMoviles() {
		return telefonosMoviles;
	}


	public void setTelefonosMoviles(String telefonosMoviles) {
		this.telefonosMoviles = telefonosMoviles;
	}


	public String getWeb() {
		return web;
	}


	public void setWeb(String web) {
		this.web = web;
	}


	public String getFacebook() {
		return facebook;
	}


	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}


	public String getTwitter() {
		return twitter;
	}


	public void setTwitter(String twitter) {
		this.twitter = twitter;
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


	public String getDireccion() {
		return direccion;
	}


	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}


	@Override
	public String toString() {
		return "Sitio [id=" + id + ", idCategoria=" + idCategoria + ", nombre="
				+ nombre + ", poblacion=" + poblacion + ", activo=" + activo
				+ "]";
	}


	public boolean isFavorito() {
		return favorito;
	}


	public void setFavorito(boolean favorito) {
		this.favorito = favorito;
	}


	public int getRanking() {
		return ranking;
	}


	public void setRanking(int ranking) {
		this.ranking = ranking;
	}

}
