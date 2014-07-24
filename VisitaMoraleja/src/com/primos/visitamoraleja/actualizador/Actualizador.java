package com.primos.visitamoraleja.actualizador;

import java.util.List;

import android.content.Context;
import android.util.Log;

import com.primos.visitamoraleja.almacenamiento.AlmacenamientoExterno;
import com.primos.visitamoraleja.almacenamiento.AlmacenamientoFactory;
import com.primos.visitamoraleja.almacenamiento.ItfAlmacenamiento;
import com.primos.visitamoraleja.bdsqlite.datasource.CategoriasDataSource;
import com.primos.visitamoraleja.bdsqlite.datasource.EventosDataSource;
import com.primos.visitamoraleja.bdsqlite.datasource.SitiosDataSource;
import com.primos.visitamoraleja.contenidos.Categoria;
import com.primos.visitamoraleja.contenidos.Evento;
import com.primos.visitamoraleja.contenidos.Sitio;
import com.primos.visitamoraleja.excepcion.EventosException;

/**
 * Clase que realiza la actualizacion de los datos en la base de datos y almacenamiento externo.
 * Para los nuevos contenidos inserta o actualiza el contenido en la base de datos y para aquellos
 * contenido que tengan imagenes guarda las imagenes en almacenamiento externo.
 * @author h
 *
 */
public class Actualizador {
	private Context contexto;

	public Actualizador(Context contexto) {
		this.contexto = contexto;
	}

	/**
	 * Realiza la insercion/actualizacion de las categorias segun la lista de categorias recibidas.
	 * Ademas almacena la imagen del icono de la categoria.
	 * @param lstCategorias
	 * @throws EventosException
	 */
	public void actualizarCategorias(List<Categoria> lstCategorias) throws EventosException {
		ItfAlmacenamiento almacenamiento = AlmacenamientoFactory.getAlmacenamiento(contexto);
		CategoriasDataSource dataSource = new CategoriasDataSource(contexto);
		try {
			dataSource.open();
			
			Log.d("CATEGORIAS", lstCategorias.toString());
			for(Categoria categ : lstCategorias) {
				long id = categ.getId();
				Categoria existente = dataSource.getById(id);
				if(existente == null) {
					dataSource.insertar(categ);
				} else {
					dataSource.actualizar(categ);
				}
				almacenamiento.addIconoCategoria(categ.getIcono(), categ.getNombreIcono(), id);
			}
		} finally {
			dataSource.close();
		}
	}


	/**
	 * Realiza la insercion/actualizacion de los sitios segun la lista de sitios recibidos.
	 * Ademas almacena la imagen del logotipo y las imagenes asociadas a este sitio.
	 * 
	 * @param lstSitios
	 * @throws EventosException
	 */
	public void actualizarSitios(List<Sitio> lstSitios) throws EventosException {
		ItfAlmacenamiento almacenamiento = AlmacenamientoFactory.getAlmacenamiento(contexto);

		SitiosDataSource dataSource = new SitiosDataSource(contexto);
		try {
			dataSource.open();
			
			Log.d("Sitios", lstSitios.toString());
			for(Sitio sitio : lstSitios) {
				long id = sitio.getId();
				Sitio existente = dataSource.getById(id);
				if(existente == null) {
					dataSource.insertar(sitio);
				} else {
					dataSource.actualizar(sitio);
				}
				long idSitio = sitio.getId();
				almacenamiento.addImagenSitio(sitio.getLogotipo(), sitio.getNombreLogotipo(), idSitio);
				almacenamiento.addImagenSitio(sitio.getImagen1(), sitio.getNombreImagen1(), idSitio);
				almacenamiento.addImagenSitio(sitio.getImagen2(), sitio.getNombreImagen2(), idSitio);
				almacenamiento.addImagenSitio(sitio.getImagen3(), sitio.getNombreImagen3(), idSitio);
				almacenamiento.addImagenSitio(sitio.getImagen4(), sitio.getNombreImagen4(), idSitio);
			}
		} finally {
			dataSource.close();
		}
	}

	/**
	 * Realiza la insercion/actualizacion de los eventos segun la lista de eventos recibidos.
	 * Ademas almacena la imagen del icono asociado a este evento.
	 * 
	 * @param lstEventos
	 * @throws EventosException
	 */
	public void actualizarEventos(List<Evento> lstEventos) throws EventosException {
		ItfAlmacenamiento almacenamiento = AlmacenamientoFactory.getAlmacenamiento(contexto);

		EventosDataSource dataSource = new EventosDataSource(contexto);
		try {
			dataSource.open();
			
			Log.d("Eventos", lstEventos.toString());
			for(Evento evento : lstEventos) {
				long id = evento.getId();
				Evento existente = dataSource.getById(id);
				if(existente == null) {
					dataSource.insertar(evento);
				} else {
					dataSource.actualizar(evento);
				}
				almacenamiento.addIconoEventos(evento.getIcono(), evento.getNombreIcono(), id);
			}
		} finally {
			dataSource.close();
		}
	}

}
