package com.primos.visitamoraleja.actualizador;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.util.Log;

import com.primos.visitamoraleja.contenidos.Categoria;
import com.primos.visitamoraleja.contenidos.Evento;
import com.primos.visitamoraleja.contenidos.Sitio;
import com.primos.visitamoraleja.dto.EventoActualizableDTO;
import com.primos.visitamoraleja.eventos.EventosJsonParser;
import com.primos.visitamoraleja.eventos.EventosXML_SAX;
import com.primos.visitamoraleja.excepcion.EventosException;
import com.primos.visitamoraleja.util.UtilPropiedades;
import com.primos.visitamoraleja.util.VersionApp;

public class ConectorServidor {
	private final static String TAG = "[ConectorServidor]";
	private static String URL_GET_LISTA_CATEGORIAS = null;//"http://10.0.2.2/eventos/categorias/CategoriasToXML.php";
	private static String URL_GET_LISTA_SITIOS = null;//"http://10.0.2.2/eventos/sitios/SitiosToXML.php";
	private static String URL_GET_LISTA_SITIOS_ACTUALIZABLES = null;
	private static String URL_GET_LISTA_EVENTOS = null;//"http://10.0.2.2/eventos/eventos/EventosToXML.php";
	private static String URL_GET_LISTA_EVENTOS_ACTUALIZABLES = null;//"http://10.0.2.2/eventos/eventos/EventosToXML.php";
	private static String URL_GET_LISTA_IMAGENES_EVENTOS = null;//"http://10.0.2.2/eventos/eventos/EventosToXML.php";
	private static String URL_GET_LISTA_SITIOS_EVENTOS = null;//"http://10.0.2.2/eventos/eventos/EventosToXML.php";
	private Context contexto;

	public ConectorServidor(Context contexto) {
		cargarPropiedades();
		this.contexto = contexto;
	}
	
	/**
	 * Realiza la carga de las propiedades si no ha sido realizada anteriormente.
	 * Forma la URL a la que selocitar los datos de categorias, sitios y eventos.
	 */
	private void cargarPropiedades() {
		if(URL_GET_LISTA_CATEGORIAS == null) {
			UtilPropiedades propiedades = UtilPropiedades.getInstance();

			String servidor = propiedades.getProperty(UtilPropiedades.PROP_SERVIDOR);
			String rutaCategorias = propiedades.getProperty(UtilPropiedades.PROP_RUTA_CATEGORIAS_XML);
			String rutaSitios = propiedades.getProperty(UtilPropiedades.PROP_RUTA_SITIOS_XML);
			String rutaSitiosActualizables = propiedades.getProperty(UtilPropiedades.PROP_RUTA_SITIOS_ACTUALIZBLES_XML);
			String rutaEventos = propiedades.getProperty(UtilPropiedades.PROP_RUTA_EVENTOS_APP);
			String rutaEventosActualizables = propiedades.getProperty(UtilPropiedades.PROP_RUTA_EVENTOS_DESCARGABLES_APP);
			String rutaImagenesEvento = propiedades.getProperty(UtilPropiedades.PROP_RUTA_IMAGENES_EVENTO_APP);
			String rutaSitiosEvento = propiedades.getProperty(UtilPropiedades.PROP_RUTA_SITIOS_EVENTO_APP);

			URL_GET_LISTA_CATEGORIAS = servidor + rutaCategorias;
			URL_GET_LISTA_SITIOS = servidor + rutaSitios;
			URL_GET_LISTA_SITIOS_ACTUALIZABLES = servidor + rutaSitiosActualizables;
			URL_GET_LISTA_EVENTOS = servidor + rutaEventos;
			URL_GET_LISTA_EVENTOS_ACTUALIZABLES = servidor + rutaEventosActualizables;
			URL_GET_LISTA_IMAGENES_EVENTOS = servidor + rutaImagenesEvento;
			URL_GET_LISTA_SITIOS_EVENTOS = servidor + rutaSitiosEvento;
		}
		Log.d(TAG, "URL de las categorias para la actualizacion: " + URL_GET_LISTA_CATEGORIAS);
		Log.d(TAG, "URL de los sitios para conocer los identificadores de sitios actualizables: " + URL_GET_LISTA_SITIOS_ACTUALIZABLES);
		Log.d(TAG, "URL de los sitios para la actualizacion: " + URL_GET_LISTA_SITIOS);
		Log.d(TAG, "URL de los eventos para la actualizacion: " + URL_GET_LISTA_EVENTOS);
		Log.d(TAG, "URL de los eventos para conocer los identificadores de los eventos actualizables: " + URL_GET_LISTA_EVENTOS_ACTUALIZABLES);
		Log.d(TAG, "URL de las imagenes pertenecientes a eventos: " + URL_GET_LISTA_IMAGENES_EVENTOS);
		Log.d(TAG, "URL de los sitios pertenecientes a eventos: " + URL_GET_LISTA_SITIOS_EVENTOS);
	}

	/**
	 * Realiza la peticion al servidor de las categorias con una fecha de ultima actualizacion posterior a la
	 * recibida como parametro. Recibe las categorias en XML y usa la clase EventosXML_SAX para convertir el
	 * XML en una lista de objetos Categoria.
	 * 
	 * @param ultimaActualizacion
	 * @return
	 * @throws EventosException
	 */
	public List<Categoria> getListaCategorias(long ultimaActualizacion) throws EventosException {
		try {
			Log.w(TAG, "Pidiendo la actualizacion de categorias para la ultimaActualizacion: " + ultimaActualizacion + " -- " + new Date(ultimaActualizacion));

			HttpClient httpclient = new DefaultHttpClient();
			/*Creamos el objeto de HttpClient que nos permitira conectarnos mediante peticiones http*/
			HttpPost httppost = new HttpPost(URL_GET_LISTA_CATEGORIAS);

			/*El objeto HttpPost permite que enviemos una peticion de tipo POST a una URL especificada*/
			//ANADIR PARAMETROS
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("ultima_actualizacion", Long.toString(ultimaActualizacion) ) );
			params.add(new BasicNameValuePair("version_app", VersionApp.getVersionApp(contexto)) );

			/* Una vez anadidos los parametros actualizamos la entidad de httppost, esto quiere decir
			 * en pocas palabras anexamos los parametros al objeto para que al enviarse al servidor
			 * envien los datos que hemos añadido
			 */
			httppost.setEntity(new UrlEncodedFormEntity(params));
			
			/*Finalmente ejecutamos enviando la info al server*/
			HttpResponse resp = httpclient.execute(httppost);
			HttpEntity ent = resp.getEntity();/*y obtenemos una respuesta*/
			
			String text = EntityUtils.toString(ent);

			Log.w(TAG, text);
			
			InputStream is = new ByteArrayInputStream(text.getBytes());
			
			EventosXML_SAX meXml = new EventosXML_SAX();
			return meXml.leerCategoriasXML(is);
		} catch (Exception e) {
			throw new EventosException("Error al realizar la peticion al servidor: " + e.getMessage(), e);
		}
	}

	/**
	 * Realiza la peticion al servidor de los sitios con una fecha de ultima actualizacion posterior a la
	 * recibida como parametro. Recibe los sitios en XML y usa la clase EventosXML_SAX para convertir el
	 * XML en una lista de objetos Sitio.
	 * 
	 * @param sitio Identificador del sitio a recuperar
	 * @return Devuelve una lista, aunque solo tendra un sitio, por necesidades de tiempo
	 * @throws EventosException
	 */
	public List<Sitio> getSitio(Sitio sitio) throws EventosException {
		try {
			long idSitio = sitio.getId();
			Log.w(TAG, "Pidiendo la actualizacion de sitio para id: " + idSitio +
					" y nombre: " + sitio.getNombre());
			HttpClient httpclient = new DefaultHttpClient();
			/*Creamos el objeto de HttpClient que nos permitira conectarnos mediante peticiones http*/
			HttpPost httppost = new HttpPost(URL_GET_LISTA_SITIOS);

			/*El objeto HttpPost permite que enviemos una peticion de tipo POST a una URL especificada*/
			//ANADIR PARAMETROS
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id_sitio", Long.toString(idSitio) ) );
			String idPoblacion = UtilPropiedades.getInstance().getProperty(UtilPropiedades.PROP_ID_POBLACION);
			params.add(new BasicNameValuePair("id_poblacion", idPoblacion ) );
			params.add(new BasicNameValuePair("version_app", VersionApp.getVersionApp(contexto)) );


			/* Una vez anadidos los parametros actualizamos la entidad de httppost, esto quiere decir
			 * en pocas palabras anexamos los parametros al objeto para que al enviarse al servidor
			 * envien los datos que hemos añadido
			 */
			httppost.setEntity(new UrlEncodedFormEntity(params));
			
			/*Finalmente ejecutamos enviando la info al server*/
			HttpResponse resp = httpclient.execute(httppost);
			HttpEntity ent = resp.getEntity();/*y obtenemos una respuesta*/
			
			String text = EntityUtils.toString(ent);
			Log.w(TAG, text);
			
			InputStream is = new ByteArrayInputStream(text.getBytes());
			
			EventosXML_SAX meXml = new EventosXML_SAX();
			return meXml.leerSitiosXML(is);
		} catch (Exception e) {
			throw new EventosException("Error al realizar la peticion al servidor: " + e.getMessage(), e);
		}
	}
	
	/**
	 * Realiza la peticion al servidor de los sitios con una fecha de ultima actualizacion posterior a la
	 * recibida como parametro. Recibe los sitios en XML y usa la clase EventosXML_SAX para convertir el
	 * XML en una lista de objetos Sitio.
	 * 
	 * @param ultimaActualizacion
	 * @param idsCategoriasActualizacion 
	 * @return
	 * @throws EventosException
	 */
	public List<Sitio> getListaSitios(long ultimaActualizacion, String idsCategoriasActualizacion) throws EventosException {
		try {
			Log.w(TAG, "Pidiendo la actualizacion de sitios para la ultimaActualizacion: " + ultimaActualizacion +
					" y categorias: " + idsCategoriasActualizacion);
			HttpClient httpclient = new DefaultHttpClient();
			/*Creamos el objeto de HttpClient que nos permitira conectarnos mediante peticiones http*/
			HttpPost httppost = new HttpPost(URL_GET_LISTA_SITIOS);

			/*El objeto HttpPost permite que enviemos una peticion de tipo POST a una URL especificada*/
			//ANADIR PARAMETROS
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("ultima_actualizacion", Long.toString(ultimaActualizacion) ) );
			String idPoblacion = UtilPropiedades.getInstance().getProperty(UtilPropiedades.PROP_ID_POBLACION);
			params.add(new BasicNameValuePair("id_poblacion", idPoblacion ) );
			params.add(new BasicNameValuePair("version_app", VersionApp.getVersionApp(contexto)) );
			if(idsCategoriasActualizacion != null) {
				params.add(new BasicNameValuePair("ids_categorias", idsCategoriasActualizacion ) );
			}
			/* Una vez anadidos los parametros actualizamos la entidad de httppost, esto quiere decir
			 * en pocas palabras anexamos los parametros al objeto para que al enviarse al servidor
			 * envien los datos que hemos añadido
			 */
			httppost.setEntity(new UrlEncodedFormEntity(params));
			
			/*Finalmente ejecutamos enviando la info al server*/
			HttpResponse resp = httpclient.execute(httppost);
			HttpEntity ent = resp.getEntity();/*y obtenemos una respuesta*/
			
			String text = EntityUtils.toString(ent);
			Log.w(TAG, text);
			
			InputStream is = new ByteArrayInputStream(text.getBytes());
			
			EventosXML_SAX meXml = new EventosXML_SAX();
			return meXml.leerSitiosXML(is);
		} catch (Exception e) {
			throw new EventosException("Error al realizar la peticion al servidor: " + e.getMessage(), e);
		}
	}

	/**
	 * Realiza la peticion al servidor los identificadores de los sitios con una fecha de ultima actualizacion posterior a la
	 * recibida como parametro. Recibe los sitios en XML y usa la clase EventosXML_SAX para convertir el
	 * XML en una lista de objetos Sitio.
	 * 
	 * @param ultimaActualizacion
	 * @param idsCategoriasActualizacion 
	 * @return
	 * @throws EventosException
	 */
	public List<Sitio> getListaSitiosActualizables(long ultimaActualizacion, String idsCategoriasActualizacion) throws EventosException {
		try {
			Log.w(TAG, "Pidiendo la actualizacion de sitios para la ultimaActualizacion: " + ultimaActualizacion +
					" y categorias: " + idsCategoriasActualizacion);
			HttpClient httpclient = new DefaultHttpClient();
			/*Creamos el objeto de HttpClient que nos permitira conectarnos mediante peticiones http*/
			HttpPost httppost = new HttpPost(URL_GET_LISTA_SITIOS_ACTUALIZABLES);

			/*El objeto HttpPost permite que enviemos una peticion de tipo POST a una URL especificada*/
			//ANADIR PARAMETROS
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("ultima_actualizacion", Long.toString(ultimaActualizacion) ) );
			String idPoblacion = UtilPropiedades.getInstance().getProperty(UtilPropiedades.PROP_ID_POBLACION);
			params.add(new BasicNameValuePair("id_poblacion", idPoblacion ) );
			params.add(new BasicNameValuePair("version_app", VersionApp.getVersionApp(contexto)) );

			if(idsCategoriasActualizacion != null) {
				params.add(new BasicNameValuePair("ids_categorias", idsCategoriasActualizacion ) );
			}
			/* Una vez anadidos los parametros actualizamos la entidad de httppost, esto quiere decir
			 * en pocas palabras anexamos los parametros al objeto para que al enviarse al servidor
			 * envien los datos que hemos añadido
			 */
			httppost.setEntity(new UrlEncodedFormEntity(params));
			
			/*Finalmente ejecutamos enviando la info al server*/
			HttpResponse resp = httpclient.execute(httppost);
			HttpEntity ent = resp.getEntity();/*y obtenemos una respuesta*/
			
			String text = EntityUtils.toString(ent);
			Log.w(TAG, text);
			
			InputStream is = new ByteArrayInputStream(text.getBytes());
			
			EventosXML_SAX meXml = new EventosXML_SAX();
			return meXml.leerSitiosActualizablesXML(is);
		} catch (Exception e) {
			throw new EventosException("Error al realizar la peticion al servidor: " + e.getMessage(), e);
		}
	}

	private abstract class ICustomConector {
		abstract String getUrl();
		abstract List<NameValuePair> getParams() throws EventosException;
	}

	/**
	 * Realiza la peticion al servidor de los eventos con una fecha de ultima actualizacion posterior a la
	 * recibida como parametro. Recibe los eventos en XML y usa la clase EventosXML_SAX para convertir el
	 * XML en una lista de objetos Evento.
	 *
	 * @oaram customConector
	 * @return
	 * @throws EventosException
	 */
	public InputStream getRespuestaServidor(ICustomConector customConector) throws EventosException {
		try {
			Log.w(TAG, "Pidiendo la actualizacion de eventos para la ultimaActualizacion: " + customConector);
			HttpClient httpclient = new DefaultHttpClient();
			/*Creamos el objeto de HttpClient que nos permitira conectarnos mediante peticiones http*/
			HttpPost httppost = new HttpPost(customConector.getUrl());

			/*El objeto HttpPost permite que enviemos una peticion de tipo POST a una URL especificada*/
			//ANADIR PARAMETROS
			List<NameValuePair> params = customConector.getParams();

			/* Una vez anadidos los parametros actualizamos la entidad de httppost, esto quiere decir
			 * en pocas palabras anexamos los parametros al objeto para que al enviarse al servidor
			 * envien los datos que hemos añadido
			 */
			httppost.setEntity(new UrlEncodedFormEntity(params));

			/*Finalmente ejecutamos enviando la info al server*/
			HttpResponse resp = httpclient.execute(httppost);
			HttpEntity ent = resp.getEntity();/*y obtenemos una respuesta*/

			String text = EntityUtils.toString(ent);
			Log.w(TAG, text);

			return new ByteArrayInputStream(text.getBytes());
		} catch (Exception e) {
			throw new EventosException("Error al realizar la peticion al servidor: " + e.getMessage(), e);
		}
	}

	/**
	 * Realiza la peticion al servidor de los eventos con una fecha de ultima actualizacion posterior a la
	 * recibida como parametro. Recibe los eventos en XML y usa la clase EventosXML_SAX para convertir el
	 * XML en una lista de objetos Evento.
	 *
	 * @param ultimaActualizacion
	 * @return
	 * @throws EventosException
	 */
	public List<EventoActualizableDTO> getListaEventosActualizables(final long ultimaActualizacion) throws EventosException {
		try {

			class CustomConectorEventosActualizables extends ICustomConector {
				String getUrl() {
					return URL_GET_LISTA_EVENTOS_ACTUALIZABLES;
				}
				List<NameValuePair> getParams() throws EventosException {
					try {
						Log.w(TAG, "Pidiendo la actualizacion de eventos para la ultimaActualizacion: " + ultimaActualizacion);
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("ultima_actualizacion", Long.toString(ultimaActualizacion)));
						params.add(new BasicNameValuePair("version_app", VersionApp.getVersionApp(contexto)));

						return params;
					} catch(Exception e) {
						throw new EventosException("Error al realizar la peticion al servidor: " + e.getMessage(), e);
					}
				}

			}

			InputStream is = getRespuestaServidor(new CustomConectorEventosActualizables());

			EventosJsonParser parser = new EventosJsonParser();
			return parser.parseEventosActualizables(is);
		} catch (Exception e) {
			throw new EventosException("Error al realizar la peticion al servidor: " + e.getMessage(), e);
		}
	}

	/**
	 * Realiza la peticion al servidor de los eventos con una fecha de ultima actualizacion posterior a la
	 * recibida como parametro. Recibe los eventos en XML y usa la clase EventosXML_SAX para convertir el
	 * XML en una lista de objetos Evento.
	 *
	 * @return
	 * @throws EventosException
	 */
	public List<Evento> getEvento(final Evento evento) throws EventosException {
		try {

			class CustomConectorEventos extends ICustomConector {
				String getUrl() {
					return URL_GET_LISTA_EVENTOS;
				}
				List<NameValuePair> getParams() throws EventosException {
					try {
						Log.w(TAG, "Pidiendo la actualizacion de eventos para el evento: " + evento.getId());
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("id_evento", Long.toString(evento.getId())));
						params.add(new BasicNameValuePair("version_app", VersionApp.getVersionApp(contexto)));

						return params;
					} catch(Exception e) {
						throw new EventosException("Error al realizar la peticion al servidor: " + e.getMessage(), e);
					}
				}
			}

			InputStream is = getRespuestaServidor(new CustomConectorEventos());

			EventosXML_SAX meXml = new EventosXML_SAX();
			return meXml.leerEventosXML(is);
		} catch (Exception e) {
			throw new EventosException("Error al realizar la peticion al servidor: " + e.getMessage(), e);
		}
	}

	public List<Evento> getEvento(final EventoActualizableDTO eventoActualizable) throws EventosException {
		Evento evento = new Evento();
		evento.setId(eventoActualizable.getId());
		return getEvento(evento);
	}
}
