package com.primos.visitamoraleja.actualizador;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.util.Log;

import com.primos.visitamoraleja.bdsqlite.datasource.CategoriasDataSource;
import com.primos.visitamoraleja.bdsqlite.datasource.EventosDataSource;
import com.primos.visitamoraleja.bdsqlite.datasource.SitiosDataSource;
import com.primos.visitamoraleja.contenidos.Categoria;
import com.primos.visitamoraleja.contenidos.Evento;
import com.primos.visitamoraleja.contenidos.Sitio;
import com.primos.visitamoraleja.excepcion.EventosException;
import com.primos.visitamoraleja.util.UtilConexion;
import com.primos.visitamoraleja.util.UtilPreferencias;

/**
 * Realiza la acualizacion de los contenidos que tienen nuevas versiones en el servidor. La comprobacion de estas
 * nuevas versiones se realiza en base a la fecha de ultima actualizacion.
 * Hereda de AsyncTask por que android no permite la ejecucion de las acciones necesarias desde el hilo principal de la aplicacion.
 * Por lo que he visto recomiendan heredar de esta clase, realiza la accion en segundo plano.
 * @author h
 *
 */
public class ThreadActualizador extends Thread {

	private Context contexto;

	/**
	 * 
	 * @param contexto
	 */
	public ThreadActualizador(Context contexto) {
		super("ThreadActualizador");
		Log.i("AsyncTaskActualizador", "INICIO");
		this.contexto = contexto;
	}

	/**
	 * Realiza la actualizacion de las categorias, comprobando primero la mayor fecha de ultima actualizacion de las
	 * categorias almacenadas en la base de datos. Usa el ConectorServidor para conseguir la lista de categorias con
	 * una fecha de ultima actualizacion mas nueva y los pasa al Actualizador para que las almacene en la base de datos
	 * y las imagenes en el almacenamiento correspondiente.
	 * @throws EventosException
	 */
	private void actualizarCategorias() throws EventosException {
		ConectorServidor cs = new ConectorServidor(contexto);
		CategoriasDataSource dataSource = new CategoriasDataSource(contexto);
		try {
			dataSource.open();
			
			long ultimaActualizacion = dataSource.getUltimaActualizacion();
	
			List<Categoria> lstCategorias = cs.getListaCategorias(ultimaActualizacion);
			Actualizador actualizador = new Actualizador(contexto);
			actualizador.actualizarCategorias(lstCategorias);
		} finally {
			dataSource.close();
		}
	}

	/**
	 * Realiza la actualizacion de los sitios, comprobando primero la mayor fecha de ultima actualizacion de los
	 * sitios almacenados en la base de datos. Usa el ConectorServidor para conseguir la lista de sitios con
	 * una fecha de ultima actualizacion mas nueva y los pasa al Actualizador para que los almacene en la base de datos
	 * y las imagenes en el almacenamiento correspondiente.
	 * 
	 * @param idsCategoriasActualizacion 
	 * @throws EventosException
	 */
	private void actualizarSitios(String idsCategoriasActualizacion) throws EventosException {
		ConectorServidor cs = new ConectorServidor(contexto);
		SitiosDataSource dataSource = new SitiosDataSource(contexto);
		try {
			dataSource.open();
			
			long ultimaActualizacion = dataSource.getUltimaActualizacion();
	
			List<Sitio> lstSitios = cs.getListaSitios(ultimaActualizacion, idsCategoriasActualizacion);
			Actualizador actualizador = new Actualizador(contexto);
			actualizador.actualizarSitios(lstSitios);

		} finally {
			dataSource.close();
		}
	}

	/**
	 * Realiza la actualizacion de los evenos, comprobando primero la mayor fecha de ultima actualizacion de los
	 * evenos almacenados en la base de datos. Usa el ConectorServidor para conseguir la lista de evenos con
	 * una fecha de ultima actualizacion mas nueva y los pasa al Actualizador para que los almacene en la base de datos
	 * y las imagenes en el almacenamiento correspondiente.
	 * 
	 * @param idsCategoriasActualizacion 
	 * @throws EventosException
	 */
	private void actualizarEventos(String idsCategoriasActualizacion) throws EventosException {
		ConectorServidor cs = new ConectorServidor(contexto);
		EventosDataSource dataSource = new EventosDataSource(contexto);
		try {
			dataSource.open();
			
			long ultimaActualizacion = dataSource.getUltimaActualizacion();
	
			List<Evento> lstEventos = cs.getListaEventos(ultimaActualizacion, idsCategoriasActualizacion);
			Actualizador actualizador = new Actualizador(contexto);
			actualizador.actualizarEventos(lstEventos);
		} finally {
			dataSource.close();
		}
	}
	
	@Override
	public void run() {
		try {
			/*
			Timer tim = new Timer("TimeActualizador");
			TimerTask task = new TimerTask() {
				
				@Override
				public void run() {
				}
			};
			long when = 0;
			long period = 6 * 60 * 60 * 1000;
			tim.scheduleAtFixedRate(task, when, period);
			*/
			
			if(UtilConexion.estaConectado(contexto)) {
				String idsCategoriasActualizacion = UtilPreferencias.getActualizacionPorCategorias(contexto);
				actualizarCategorias();
				actualizarSitios(idsCategoriasActualizacion);
				actualizarEventos(idsCategoriasActualizacion);
			}
		} catch (EventosException e) {
			Log.e("AsyncTaskActualizador", "Error leyendo la ultima actualizacion.", e);
		}
	}

}
