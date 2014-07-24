package com.primos.visitamoraleja.actualizador;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.primos.visitamoraleja.R;
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
public class AsyncTaskActualizador extends AsyncTask<Void, Void, AsyncTaskActualizador.ResultadoActualizacion> {
	/**
	 * Resultado de la accion en segundo plano, que indica si las actualizaciones se realizan correctamente
	 * o se produce un error durante la actualizacion.
	 * @author h
	 *
	 */
	enum ResultadoActualizacion {OK, KO};

	private Context contexto;
	private boolean mostrarToast;

	/**
	 * 
	 * @param contexto
	 * @param mostrarToast Indica si se debe mostrar un mensaje mediante la clase Toast con el resultado de la actualizacion.
	 */
	public AsyncTaskActualizador(Context contexto, boolean mostrarToast) {
		Log.i("AsyncTaskActualizador", "INICIO");
		this.contexto = contexto;
		this.mostrarToast = mostrarToast;
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
	
//	/**
//	 * Devuelve el String que contiene los identificadores de las categorias para actualizar solo por estas categorias.
//	 * Los identificadores estan separados por comas
//	 * Si no hay ninguna categoria seleccionada, se devuelve null
//	 * @return
//	 */
//	private String getActualizacionPorCategorias() {
//		String resul = "";
//		CategoriasDataSource dataSource = new CategoriasDataSource(contexto);
//		try {
//			dataSource.open();
//			List<Categoria> lstCategorias = dataSource.getAll();
//			
//			SharedPreferences ratePrefs = PreferenceManager
//	                .getDefaultSharedPreferences(contexto);
//			boolean actualizarPorCategorias = ratePrefs.getBoolean(PreferenciasActivity.PREFERENCIA_ACTUALIZAR_POR_CATEGORIAS, false);
//			if(actualizarPorCategorias) {
//				for(Categoria categoria : lstCategorias) {
//					String key = PreferenciasActivity.PREFIJO_PREFERENCIA_CATEGORIAS + categoria.getId();
//					boolean activada = ratePrefs.getBoolean(key, false);
//					if(activada) {
//						resul += ","+categoria.getId();
//					}
//				}
//			}
//			if(!resul.equals("")) {
//				resul = resul.substring(1);
//			} else {
//				resul = null;
//			}
//		} finally {
//			dataSource.close();
//		}
//		return resul;
//	}

	/**
	 * Estas son las acciones que por heredar de la clase AsyncTask se ejecutara en segundo plano.
	 */
	@Override
	protected ResultadoActualizacion doInBackground(Void... arg0) {
		try {
			if(UtilConexion.estaConectado(contexto)) {
				String idsCategoriasActualizacion = UtilPreferencias.getActualizacionPorCategorias(contexto);
				actualizarCategorias();
				actualizarSitios(idsCategoriasActualizacion);
				actualizarEventos(idsCategoriasActualizacion);
			}
		} catch (EventosException e) {
			Log.e("AsyncTaskActualizador", "Error leyendo la ultima actualizacion.", e);
			return ResultadoActualizacion.KO;
		}
		return ResultadoActualizacion.OK;
	}

	/**
	 * Accion que se realiza al finalizar la accion en segundo plano. En este caso mostrara un mensaje con la clase
	 * Toast si el objeto ha sido configurado para ello mediante el atributo mostrarToast
	 */
	@Override
	protected void onPostExecute(ResultadoActualizacion result) {
		super.onPostExecute(result);
		if(this.mostrarToast) {
			int resulActualizacion = R.string.actualizacion_ko;
			if(result == ResultadoActualizacion.OK) {
				resulActualizacion = R.string.actualizacion_ok;
			}
			Toast.makeText(contexto, resulActualizacion, Toast.LENGTH_SHORT).show();
		}

	}

}
