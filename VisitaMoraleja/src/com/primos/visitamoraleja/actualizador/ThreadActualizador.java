package com.primos.visitamoraleja.actualizador;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
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
import com.primos.visitamoraleja.util.UtilConsultaActualizar;
import com.primos.visitamoraleja.util.UtilPreferencias;

/**
 * Realiza la acualizacion de los contenidos que tienen nuevas versiones en el
 * servidor. La comprobacion de estas nuevas versiones se realiza en base a la
 * fecha de ultima actualizacion. Hereda de AsyncTask por que android no permite
 * la ejecucion de las acciones necesarias desde el hilo principal de la
 * aplicacion. Por lo que he visto recomiendan heredar de esta clase, realiza la
 * accion en segundo plano.
 * 
 * @author h
 * 
 */
public class ThreadActualizador extends Thread {
	private final static int MOSTRAR_CONSULTA_ACTUALIZAR = 1;
	private final static int INICIO_ACTUALIZAR = 2;
	private final static int FIN_ACTUALIZAR = 3;
	private final static int SITIO_ACTUALIZANDO = 4;
	private final static int NO_DATOS_ACTUALIZAR = 5;
	private final static int EXCEPCION = 6;

	private Context contexto;

	private Handler handlerBarraProgresoActualizacion;
	private Object semaforo = new Object();
	private UtilConsultaActualizar consultaActualizar = new UtilConsultaActualizar();
	private boolean mostrarNoDatos = false;

	/**
	 * 
	 * @param contexto
	 */
	public ThreadActualizador(final Context contexto) {
		super("ThreadActualizador");
		Log.i("AsyncTaskActualizador", "INICIO");
		this.contexto = contexto;
		this.handlerBarraProgresoActualizacion = new Handler(
				new Handler.Callback() {
					private ProgressDialog dialogoProgreso;
					private int numSitiosActualizar;
					private int numSitiosActualizados = 0;

					@Override
					public boolean handleMessage(Message msg) {
						switch (msg.arg1) {
						case MOSTRAR_CONSULTA_ACTUALIZAR:
							numSitiosActualizar = msg.arg2;
							consultaActualizar.consultaActualizar(contexto,
									semaforo, numSitiosActualizar);
							break;
						case INICIO_ACTUALIZAR:
							dialogoProgreso = new ProgressDialog(contexto);
							dialogoProgreso
									.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
							dialogoProgreso.setMessage("Procesando...");
							dialogoProgreso.setCancelable(false);
							dialogoProgreso.setCanceledOnTouchOutside(false);
							dialogoProgreso.setMax(numSitiosActualizar);
							dialogoProgreso.setProgress(numSitiosActualizados);
							dialogoProgreso.show();
							break;
						case FIN_ACTUALIZAR:
							dialogoProgreso.cancel();
							break;
						case EXCEPCION:
							Toast toastError = Toast.makeText(contexto,
									R.string.error_durante_actualizacion, Toast.LENGTH_SHORT);
							toastError.show();
							dialogoProgreso.cancel();
							break;
						case SITIO_ACTUALIZANDO:
							Sitio sitio = (Sitio) msg.obj;
							dialogoProgreso.setMessage("Actualizando "
									+ sitio.getNombre());
							dialogoProgreso.setProgress(numSitiosActualizados);
							numSitiosActualizados++;
							break;
						case NO_DATOS_ACTUALIZAR:
							Toast toastNoDatos = Toast.makeText(contexto,
									R.string.no_datos_actualizar, Toast.LENGTH_SHORT);
							toastNoDatos.show();
							break;
						}

						return false;
					}
				});
	}

	/**
	 * Realiza la actualizacion de las categorias, comprobando primero la mayor
	 * fecha de ultima actualizacion de las categorias almacenadas en la base de
	 * datos. Usa el ConectorServidor para conseguir la lista de categorias con
	 * una fecha de ultima actualizacion mas nueva y los pasa al Actualizador
	 * para que las almacene en la base de datos y las imagenes en el
	 * almacenamiento correspondiente.
	 * 
	 * @throws EventosException
	 */
	private void actualizarCategorias() throws EventosException {
		ConectorServidor cs = new ConectorServidor(contexto);
		CategoriasDataSource dataSource = new CategoriasDataSource(contexto);
		try {
			dataSource.open();

			long ultimaActualizacion = dataSource.getUltimaActualizacion();

			List<Categoria> lstCategorias = cs
					.getListaCategorias(ultimaActualizacion);
			Actualizador actualizador = new Actualizador(contexto);
			actualizador.actualizarCategorias(lstCategorias);
		} finally {
			dataSource.close();
		}
	}

	/**
	 * Realiza la actualizacion de los sitios, comprobando primero la mayor
	 * fecha de ultima actualizacion de los sitios almacenados en la base de
	 * datos. Usa el ConectorServidor para conseguir la lista de sitios con una
	 * fecha de ultima actualizacion mas nueva y los pasa al Actualizador para
	 * que los almacene en la base de datos y las imagenes en el almacenamiento
	 * correspondiente.
	 * 
	 * @param idsCategoriasActualizacion
	 * @throws EventosException
	 */
	private void actualizarSitios(String idsCategoriasActualizacion)
			throws EventosException {
		ConectorServidor cs = new ConectorServidor(contexto);
		SitiosDataSource dataSource = new SitiosDataSource(contexto);
		try {
			dataSource.open();

			long ultimaActualizacion = dataSource.getUltimaActualizacion();

			List<Sitio> lstSitiosActualizables = cs
					.getListaSitiosActualizables(ultimaActualizacion,
							idsCategoriasActualizacion);

			if (!lstSitiosActualizables.isEmpty()) {
				synchronized (semaforo) {
					try {
						Message msjConsulta = new Message();
						msjConsulta.arg1 = MOSTRAR_CONSULTA_ACTUALIZAR;
						msjConsulta.arg2 = lstSitiosActualizables.size();
						handlerBarraProgresoActualizacion
								.sendMessage(msjConsulta);

						semaforo.wait();

						if (consultaActualizar.isConsultaActualizar()) {
							Message msjInicio = new Message();
							msjInicio.arg1 = INICIO_ACTUALIZAR;
							handlerBarraProgresoActualizacion
									.sendMessage(msjInicio);

							for (Sitio sitio : lstSitiosActualizables) {
								Message msj = new Message();
								msj.arg1 = SITIO_ACTUALIZANDO;
								msj.obj = sitio;
								handlerBarraProgresoActualizacion
										.sendMessage(msj);
								List<Sitio> lstSitios = cs.getSitio(sitio);
								Actualizador actualizador = new Actualizador(
										contexto);
								actualizador.actualizarSitios(lstSitios);
							}

							Message msjFin = new Message();
							msjFin.arg1 = FIN_ACTUALIZAR;
							handlerBarraProgresoActualizacion
									.sendMessage(msjFin);
						}
					} catch (Exception e) {
						Message msjExcepcion = new Message();
						msjExcepcion.arg1 = EXCEPCION;
						handlerBarraProgresoActualizacion
								.sendMessage(msjExcepcion);
						throw new EventosException(
								"Error en la sincronizacion al consultar si actualizar.",
								e);
					}
				}
			} else {
				if(mostrarNoDatos) {
					Message msjNoDatosActualizar = new Message();
					msjNoDatosActualizar.arg1 = NO_DATOS_ACTUALIZAR;
					handlerBarraProgresoActualizacion
							.sendMessage(msjNoDatosActualizar);
					mostrarNoDatos = false;
				}
			}
		} finally {
			dataSource.close();
		}
	}

	/**
	 * Realiza la actualizacion de los evenos, comprobando primero la mayor
	 * fecha de ultima actualizacion de los evenos almacenados en la base de
	 * datos. Usa el ConectorServidor para conseguir la lista de evenos con una
	 * fecha de ultima actualizacion mas nueva y los pasa al Actualizador para
	 * que los almacene en la base de datos y las imagenes en el almacenamiento
	 * correspondiente.
	 * 
	 * @param idsCategoriasActualizacion
	 * @throws EventosException
	 */
	private void actualizarEventos(String idsCategoriasActualizacion)
			throws EventosException {
		ConectorServidor cs = new ConectorServidor(contexto);
		EventosDataSource dataSource = new EventosDataSource(contexto);
		try {
			dataSource.open();

			long ultimaActualizacion = dataSource.getUltimaActualizacion();

			List<Evento> lstEventos = cs.getListaEventos(ultimaActualizacion,
					idsCategoriasActualizacion);
			Actualizador actualizador = new Actualizador(contexto);
			actualizador.actualizarEventos(lstEventos);
		} finally {
			dataSource.close();
		}
	}

	@Override
	public void run() {
		try {

			if (UtilConexion.estaConectado(contexto)) {
				String idsCategoriasActualizacion = UtilPreferencias
						.getActualizacionPorCategorias(contexto);
				actualizarCategorias();
				actualizarSitios(idsCategoriasActualizacion);
				actualizarEventos(idsCategoriasActualizacion);
			}
		} catch (EventosException e) {
			Log.e("AsyncTaskActualizador",
					"Error leyendo la ultima actualizacion.", e);
		}
	}

	public void setMostrarNoDatos(boolean mostrarNoDatos) {
		this.mostrarNoDatos = mostrarNoDatos;
	}

}
