package com.primos.visitamoraleja.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;


/**
 * Clase para leer las propiedades de la aplicacion. La he hecho usando el patron singleton
 * para poder unir la lectura de propiedades en una sola clase.
 * @author h
 *
 */
public class UtilPropiedades {
	/**
	 * Nombre del fichero de propiedades.
	 */
	private final static String NOMBRE_FICHERO_CONEXION = "propiedades_aplicacion.properties";
	/**
	 * Propiedad del fichero de propiedades que indica el identificador de poblacion para realizar las peticiones de actualizacion de datos.
	 */
	public final static String PROP_ID_POBLACION = "idPoblacion";
	/**
	 * Propiedad del fichero de propiedades que indica el servidor al que se conecta para realziar las peticiones de datos
	 */
	public final static String PROP_SERVIDOR = "Servidor";
	/**
	 * Ruta en el servidor para pedir los datos de actualizacion de las categorias.
	 */
	public final static String PROP_RUTA_CATEGORIAS_XML = "RutaCategoriasXML";
	/**
	 * Ruta en el servidor para pedir los datos de actualizacion de los sitios.
	 */
	public final static String PROP_RUTA_SITIOS_XML = "RutaSitiosXML";
	/**
	 * Ruta en el servidor para pedir los identificadores de los sitios actualizables.
	 */
	public final static String PROP_RUTA_SITIOS_ACTUALIZBLES_XML = "RutaSitiosActualizablesXML";
	/**
	 * Ruta en el servidor para pedir los datos de actualizacion de los eventos
	 */
	public final static String PROP_RUTA_EVENTOS_APP = "RutaEventosApp";
	/**
	 * Ruta en el servidor para pedir los datos de actualizacion de los eventos actualizables
	 */
	public final static String PROP_RUTA_EVENTOS_DESCARGABLES_APP = "RutaEventosDescargablesApp";
	/**
	 * Ruta en el servidor para pedir los datos de actualizacion de las imagenes de eventos
	 */
	public final static String PROP_RUTA_IMAGENES_EVENTO_APP = "RutaImagenesEventoApp";
	/**
	 * Ruta en el servidor para pedir los datos de actualizacion de las imagenes de las actividades de eventos
	 */
	public final static String PROP_RUTA_IMAGENES_ACTIVIDAD_EVENTO_APP = "RutaImagenesActividadEventoApp";
	/**
	 * Ruta en el servidor para pedir los datos de actualizacion de los sitios registrados en eventos
	 */
	public final static String PROP_RUTA_SITIOS_EVENTO_APP = "RutaSitiosEventoApp";
	/**
	 * Ruta en el servidor para pedir los datos de actualizacion de los sitios registrados en eventos
	 */
	public final static String PROP_RUTA_CATEGORIAS_EVENTO_APP = "RutaCategoriasEventoApp";
	/**
	 * Ruta en el servidor para pedir los datos de actualizacion de los sitios registrados en eventos
	 */
	public final static String PROP_RUTA_ACTIVIDADES_EVENTO_APP = "RutaActividadesEventoApp";
	/**
	 * ApplicationId para registrarse en Parse y recibir las notificaciones push
	 */
	public final static String PROP_PARSE_APPLICATION_ID = "parseApplicationId";
	/**
	 * ClientKey para registrarse en Parse y recibir las notificaciones push
	 */
	public final static String PROP_PARSE_CLIENT_KEY = "parseClientKey";
	/**
	 * Nombre de la apicacion para almacenamiento externo
	 */
	public static final String PROP_NOMBRE_APLICACION = "NombreAplicacion";
	/**
	 * Url de registro de la aplicación
	 */
	public static final String PROP_URL_REGISTRO = "UrlRegistro";
	/**
	 * Direccion de correo para la aplicacion.
	 */
	public static final String PROP_CORREO_APLICACION = "CorreoAplicacion";

	private static UtilPropiedades instancia = null;
	
	/**
	 * Las propiedades leidas del fichero.
	 */
	private Properties properties;

	private UtilPropiedades() {
	}
	
	public static UtilPropiedades getInstance() {
		if(instancia == null) {
			instancia = new UtilPropiedades();
		}
		return instancia;
	}
	
	/**
	 * Realiza la lectura del fichero de propiedades;
	 */
	public void inicializar(Context contexto) {
		AssetManager assetManager = contexto.getAssets();
		InputStream is = null;
		properties = new Properties();
		try {
			is = assetManager.open(NOMBRE_FICHERO_CONEXION);
			properties.load(is);
		} catch (IOException e) {
			Log.e("UtilPropiedades", "Error al leer las propiedades de la aplicacion", e);
		} finally {
			if(is != null) {
				try {
					is.close();
				} catch (IOException e) {
					Log.e("UtilPropiedades", "Error al cerrar InputStream del fichero de propiedades", e);
				}
			}
		}
		
	}
	
	public String getProperty(String property) {
		return properties.getProperty(property);
	}

}
