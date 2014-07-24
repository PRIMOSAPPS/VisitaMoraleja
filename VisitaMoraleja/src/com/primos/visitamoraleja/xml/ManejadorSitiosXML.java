package com.primos.visitamoraleja.xml;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.primos.visitamoraleja.contenidos.Sitio;
import com.primos.visitamoraleja.util.ConversionesUtil;

/**
 * Parsea un XML para convertir su contenido en una lista de sitios.
 * 
 * @author h
 *
 */
public class ManejadorSitiosXML extends DefaultHandler {
	private StringBuilder cadena;
	private List<Sitio> lstSitios = null;
	private Sitio sitio;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	@Override
	public void startDocument() throws SAXException {
		cadena = new StringBuilder();
		lstSitios = new ArrayList<Sitio>();
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		cadena.append(ch, start, length);
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		try {
			if(localName.equals("idSitio")) {
				sitio.setId(Long.parseLong(cadena.toString()));
			} else if(localName.equals("idCategoria")) {
					sitio.setIdCategoria(Long.parseLong(cadena.toString()));
			} else if(localName.equals("nombre")) {
				sitio.setNombre(cadena.toString());
			} else if(localName.equals("textoCorto1")) {
				sitio.setTextoCorto1(cadena.toString());
			} else if(localName.equals("textoCorto2")) {
				sitio.setTextoCorto2(cadena.toString());
			} else if(localName.equals("textoCorto3")) {
				sitio.setTextoCorto3(cadena.toString());
			} else if(localName.equals("textoLargo1")) {
				sitio.setTextoLargo1(cadena.toString());
			} else if(localName.equals("textoLargo2")) {
				sitio.setTextoLargo2(cadena.toString());
			} else if(localName.equals("nombreLogotipo")) {
				sitio.setNombreLogotipo(cadena.toString());
			} else if(localName.equals("logotipo")) {
//				byte[] strImagenBase64 = cadena.toString().trim().getBytes("UTF-16"); ;
//				byte[] base64decoded = Base64.decode(strImagenBase64, Base64.DEFAULT);
//				Bitmap icono = BitmapFactory.decodeByteArray(base64decoded, 0, base64decoded.length);
				sitio.setLogotipo(ConversionesUtil.getBitmap(cadena));
			} else if(localName.equals("nombreImagen1")) {
				sitio.setNombreImagen1(cadena.toString());
			} else if(localName.equals("imagen1")) {
				sitio.setImagen1(ConversionesUtil.getBitmap(cadena));
			} else if(localName.equals("nombreImagen2")) {
				sitio.setNombreImagen2(cadena.toString());
			} else if(localName.equals("imagen2")) {
				sitio.setImagen2(ConversionesUtil.getBitmap(cadena));
			} else if(localName.equals("nombreImagen3")) {
				sitio.setNombreImagen3(cadena.toString());
			} else if(localName.equals("imagen3")) {
				sitio.setImagen3(ConversionesUtil.getBitmap(cadena));
			} else if(localName.equals("nombreImagen4")) {
				sitio.setNombreImagen4(cadena.toString());
			} else if(localName.equals("imagen4")) {
				sitio.setImagen4(ConversionesUtil.getBitmap(cadena));
			} else if(localName.equals("latitud")) {
				sitio.setLatitud(Double.parseDouble(cadena.toString()));
			} else if(localName.equals("longitud")) {
				sitio.setLongitud(Double.parseDouble(cadena.toString()));
			} else if(localName.equals("direccion")) {
				sitio.setDireccion(cadena.toString());
			} else if(localName.equals("poblacion")) {
				sitio.setPoblacion(cadena.toString());
			} else if(localName.equals("telefonosFijos")) {
				sitio.setTelefonosFijos(cadena.toString());
			} else if(localName.equals("telefonosMoviles")) {
				sitio.setTelefonosMoviles(cadena.toString());
			} else if(localName.equals("web")) {
				sitio.setWeb(cadena.toString());
			} else if(localName.equals("facebook")) {
				sitio.setFacebook(cadena.toString());
			} else if(localName.equals("twitter")) {
				sitio.setTwitter(cadena.toString());
			} else if(localName.equals("ranking")) {
				sitio.setRanking(Integer.parseInt(cadena.toString()));
			} else if(localName.equals("activo")) {
				int valor = Integer.parseInt(cadena.toString());
				sitio.setActivo(ConversionesUtil.intToBoolean(valor));
			} else if(localName.equals("ultimaActualizacion")) {
				sitio.setUltimaActualizacion(sdf.parse(cadena.toString().trim()));
			} else if(localName.equals("sitio")) {
				lstSitios.add(sitio);
			}
		} catch(Exception e) {
			throw new SAXException("Error al leer una cadena #" + cadena.toString().trim() + "#", e);
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		cadena.setLength(0);
		if(localName.equals("sitio")) {
			Log.w("SITIOS: ", "Un sitio nuevo");
			sitio = new Sitio();
		}
	}

	public List<Sitio> getLstElements() {
		return lstSitios;
	}


}
