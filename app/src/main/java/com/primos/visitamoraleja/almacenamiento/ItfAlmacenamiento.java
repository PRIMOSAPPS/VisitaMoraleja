package com.primos.visitamoraleja.almacenamiento;

import android.Manifest;
import android.graphics.Bitmap;

public interface ItfAlmacenamiento {

	public static String[] permisosNecesarios = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

	/**
	 * Devuelve la imagen que pertenece a una categoria con identificador idCategoria y nombre
	 * @param idCategoria
	 * @param nombre
	 * @return
	 */
	public abstract Bitmap getIconoCategoria(long idCategoria, String nombre);

	/**
	 * Devuelve la imagen que pertenece a un sitio con identificador idSitio y nombre
	 * 
	 * @param idSitio
	 * @param nombre
	 * @return
	 */
	public abstract Bitmap getImagenSitio(long idSitio, String nombre);

	/**
	 * Aniade una imagen que pertenece a una categoria al almacenamiento cuyo nombre es nombreImagen y pertenece
	 * a una categoria con identificador idCategoria
	 * @param imagen
	 * @param nombreImagen
	 * @param idCategoria
	 */
	public abstract void addIconoCategoria(Bitmap imagen, String nombreImagen,
			long idCategoria);

	/**
	 * Aniade una imagen que pertenece a un sitio al almacenamiento cuyo nombre es nombreImagen y pertenece
	 * a un sitio con identificador idSitio
	 * 
	 * @param imagen
	 * @param nombreImagen
	 * @param idSitio
	 */
	public abstract void addImagenSitio(Bitmap imagen, String nombreImagen,
			long idSitio);

	/**
	 * Borra una imagen que pertenece a un sitio al almacenamiento cuyo nombre es nombreImagen y pertenece
	 * a un sitio con identificador idSitio
	 * 
	 * @param nombreImagen
	 * @param idSitio
	 * @param idSitio
	 */
	public abstract void borrarImagenSitio(String nombreImagen, long idSitio);

	////////////////////////////////////////////////////////////////////
	// EVENTOS

	/**
	 * Devuelve el path de una imagen
	 * @param idEvento
	 * @param nombre
	 * @return
	 */
	String getDirImagenEvento(long idEvento, String nombre);

	/**
	 * Devuelve el path de una imagen
	 * @param idEvento
	 * @param nombre
	 * @return
	 */
	String getDirImagenActividadEvento(long idEvento, String nombre);

	/**
	 * Devuelve la imagen que pertenece a un evento con identificador idEvento y nombre
	 *
	 * @param idEvento
	 * @param nombre
	 * @return
	 */
	public abstract Bitmap getImagenEvento(long idEvento, String nombre);

	/**
	 * Aniade una imagen que pertenece a un evento al almacenamiento cuyo nombre es nombreImagen y pertenece
	 * a un evento con identificador idEvento
	 *
	 * @param imagen
	 * @param nombreImagen
	 * @param idEvento
	 */
	public abstract void addImagenEvento(Bitmap imagen, String nombreImagen,
										 long idEvento);

	/**
	 * Aniade una imagen que pertenece a un evento al almacenamiento cuyo nombre es nombreImagen y pertenece
	 * a un evento con identificador idEvento
	 *
	 * @param nombreImagen
	 * @param idEvento
	 */
	public abstract void borrarImagenEvento(String nombreImagen, long idEvento);


	/**
	 * Devuelve la imagen que pertenece a un sitio de un evento con identificador idEvento y nombre
	 *
	 * @param idSitioEvento
	 * @param nombre
	 * @return
	 */
	public abstract Bitmap getImagenSitioEvento(long idSitioEvento, String nombre);

	/**
	 * Aniade una imagen que pertenece a un sitio de un evento al almacenamiento cuyo nombre es nombreImagen y pertenece
	 * a un evento con identificador idEvento
	 *
	 * @param imagen
	 * @param nombreImagen
	 * @param idSitioEvento
	 */
	public abstract void addImagenSitioEvento(Bitmap imagen, String nombreImagen,
										 long idSitioEvento);

	/**
	 * Aniade una imagen que pertenece a un sitio de un evento al almacenamiento cuyo nombre es nombreImagen y pertenece
	 * a un evento con identificador idEvento
	 *
	 * @param nombreImagen
	 * @param idSitioEvento
	 */
	public abstract void borrarImagenSitioEvento(String nombreImagen, long idSitioEvento);

	/**
	 * Devuelve la imagen que pertenece a una categoria de un evento
	 *
	 * @param idCategoriaEvento
	 * @param nombre
	 * @return
	 */
	public abstract Bitmap getImagenCategoriaEvento(long idCategoriaEvento, String nombre);

	/**
	 * Aniade una imagen que pertenece a una categoria de un evento
	 *
	 * @param imagen
	 * @param nombreImagen
	 * @param idCategoriaEvento
	 */
	public abstract void addImagenCategoriaEvento(Bitmap imagen, String nombreImagen,
											  long idCategoriaEvento);

	/**
	 * Aniade una imagen que pertenece a una categoria de un evento
	 *
	 * @param nombreImagen
	 * @param idCategoriaEvento
	 */
	public abstract void borrarImagenCategoriaEvento(String nombreImagen, long idCategoriaEvento);

	/**
	 * Devuelve la imagen que pertenece a un sitio de un evento con identificador idEvento y nombre
	 *
	 * @param idActividadEvento
	 * @param nombre
	 * @return
	 */
	public abstract Bitmap getImagenActividadEvento(long idActividadEvento, String nombre);

	/**
	 * Aniade una imagen que pertenece a un sitio de un evento al almacenamiento cuyo nombre es nombreImagen y pertenece
	 * a un evento con identificador idEvento
	 *
	 * @param imagen
	 * @param nombreImagen
	 * @param idActividadEvento
	 */
	public abstract void addImagenActividadEvento(Bitmap imagen, String nombreImagen,
											  long idActividadEvento);

	/**
	 * Aniade una imagen que pertenece a un sitio de un evento al almacenamiento cuyo nombre es nombreImagen y pertenece
	 * a un evento con identificador idEvento
	 *
	 * @param nombreImagen
	 * @param idActividadEvento
	 */
	public abstract void borrarImagenActividadEvento(String nombreImagen, long idActividadEvento);
}