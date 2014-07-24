package com.primos.visitamoraleja.actualizador;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
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
import com.primos.visitamoraleja.eventos.EventosXML_SAX;
import com.primos.visitamoraleja.excepcion.EventosException;
import com.primos.visitamoraleja.util.UtilPropiedades;

public class ConectorServidor {
	private static String URL_GET_LISTA_CATEGORIAS = null;//"http://10.0.2.2/eventos/categorias/CategoriasToXML.php";
	private static String URL_GET_LISTA_SITIOS = null;//"http://10.0.2.2/eventos/sitios/SitiosToXML.php";
	private static String URL_GET_LISTA_EVENTOS = null;//"http://10.0.2.2/eventos/eventos/EventosToXML.php";

	public ConectorServidor(Context contexto) {
		cargarPropiedades();
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
			String rutaEventos = propiedades.getProperty(UtilPropiedades.PROP_RUTA_EVENTOS_XML);

			URL_GET_LISTA_CATEGORIAS = servidor + rutaCategorias;
			URL_GET_LISTA_EVENTOS = servidor + rutaEventos;
			URL_GET_LISTA_SITIOS = servidor + rutaSitios;
		}
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
			Log.w("[ConectorServidor]", "Pidiendo la actualizacion de sitios para la ultimaActualizacion: " + ultimaActualizacion);

			HttpClient httpclient = new DefaultHttpClient();
			/*Creamos el objeto de HttpClient que nos permitira conectarnos mediante peticiones http*/
			HttpPost httppost = new HttpPost(URL_GET_LISTA_CATEGORIAS);

			/*El objeto HttpPost permite que enviemos una peticion de tipo POST a una URL especificada*/
			//ANADIR PARAMETROS
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("ultima_actualizacion", Long.toString(ultimaActualizacion) ) );
			/* Una vez anadidos los parametros actualizamos la entidad de httppost, esto quiere decir
			 * en pocas palabras anexamos los parametros al objeto para que al enviarse al servidor
			 * envien los datos que hemos añadido
			 */
			httppost.setEntity(new UrlEncodedFormEntity(params));
			
			/*Finalmente ejecutamos enviando la info al server*/
			HttpResponse resp = httpclient.execute(httppost);
			HttpEntity ent = resp.getEntity();/*y obtenemos una respuesta*/
			
			String text = EntityUtils.toString(ent);
			Log.w("CATEGORIAS: ", text);
			
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
	 * @param ultimaActualizacion
	 * @param idsCategoriasActualizacion 
	 * @return
	 * @throws EventosException
	 */
	public List<Sitio> getListaSitios(long ultimaActualizacion, String idsCategoriasActualizacion) throws EventosException {
		try {
			Log.w("[ConectorServidor]", "Pidiendo la actualizacion de sitios para la ultimaActualizacion: " + ultimaActualizacion +
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
			Log.w("SITIOS: ", text);
			
			InputStream is = new ByteArrayInputStream(text.getBytes());
			
			EventosXML_SAX meXml = new EventosXML_SAX();
			return meXml.leerSitiosXML(is);
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
	 * @param idsCategoriasActualizacion 
	 * @return
	 * @throws EventosException
	 */
	public List<Evento> getListaEventos(long ultimaActualizacion, String idsCategoriasActualizacion) throws EventosException {
		try {
			Log.w("[ConectorServidor]", "Pidiendo la actualizacion de eventos para la ultimaActualizacion: " + ultimaActualizacion +
					" y categorias: " + idsCategoriasActualizacion);
			HttpClient httpclient = new DefaultHttpClient();
			/*Creamos el objeto de HttpClient que nos permitira conectarnos mediante peticiones http*/
			HttpPost httppost = new HttpPost(URL_GET_LISTA_EVENTOS);

			/*El objeto HttpPost permite que enviemos una peticion de tipo POST a una URL especificada*/
			//ANADIR PARAMETROS
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("ultima_actualizacion", Long.toString(ultimaActualizacion) ) );
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
			Log.w("EVENTOS: ", text);
			
			InputStream is = new ByteArrayInputStream(text.getBytes());
			
			EventosXML_SAX meXml = new EventosXML_SAX();
			return meXml.leerEventosXML(is);
		} catch (Exception e) {
			throw new EventosException("Error al realizar la peticion al servidor: " + e.getMessage(), e);
		}
	}

}
