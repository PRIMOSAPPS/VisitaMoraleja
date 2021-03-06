package com.primos.visitamoraleja.xml;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Base64;
import android.util.Log;

import com.primos.visitamoraleja.contenidos.Sitio;
import com.primos.visitamoraleja.util.ConversionesUtil;
import com.primos.visitamoraleja.util.UtilFechas;

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
				sitio.setNombre(stringFromBase64(cadena.toString()));
			} else if(localName.equals("textoCorto1")) {
				sitio.setTextoCorto1(stringFromBase64(cadena.toString()));
			} else if(localName.equals("textoCorto2")) {
				sitio.setTextoCorto2(stringFromBase64(cadena.toString()));
			} else if(localName.equals("textoCorto3")) {
				sitio.setTextoCorto3(stringFromBase64(cadena.toString()));
			} else if(localName.equals("textoLargo1")) {
				String textoLargoBase64 = cadena.toString();
				String datosTextoLargo = new String(Base64.decode(textoLargoBase64, Base64.DEFAULT));
				sitio.setTextoLargo1(datosTextoLargo);
			} else if(localName.equals("textoLargo2")) {
				String textoLargoBase64 = cadena.toString();
				String datosTextoLargo = new String(Base64.decode(textoLargoBase64, Base64.DEFAULT));
				sitio.setTextoLargo2(datosTextoLargo);
			} else if(localName.equals("nombreLogotipo")) {
				sitio.setNombreLogotipo(stringFromBase64(cadena.toString()));
			} else if(localName.equals("logotipo")) {
//				byte[] strImagenBase64 = cadena.toString().trim().getBytes("UTF-16"); ;
//				byte[] base64decoded = Base64.decode(strImagenBase64, Base64.DEFAULT);
//				Bitmap icono = BitmapFactory.decodeByteArray(base64decoded, 0, base64decoded.length);
				sitio.setLogotipo(ConversionesUtil.getBitmap(cadena));
			} else if(localName.equals("nombreImagen1")) {
				sitio.setNombreImagen1(stringFromBase64(cadena.toString()));
			} else if(localName.equals("imagen1")) {
				sitio.setImagen1(ConversionesUtil.getBitmap(cadena));
			} else if(localName.equals("nombreImagen2")) {
				sitio.setNombreImagen2(stringFromBase64(cadena.toString()));
			} else if(localName.equals("imagen2")) {
				sitio.setImagen2(ConversionesUtil.getBitmap(cadena));
			} else if(localName.equals("nombreImagen3")) {
				sitio.setNombreImagen3(stringFromBase64(cadena.toString()));
			} else if(localName.equals("imagen3")) {
				sitio.setImagen3(ConversionesUtil.getBitmap(cadena));
			} else if(localName.equals("nombreImagen4")) {
				sitio.setNombreImagen4(stringFromBase64(cadena.toString()));
			} else if(localName.equals("imagen4")) {
				sitio.setImagen4(ConversionesUtil.getBitmap(cadena));
			} else if(localName.equals("latitud")) {
				sitio.setLatitud(Double.parseDouble(cadena.toString()));
			} else if(localName.equals("longitud")) {
				sitio.setLongitud(Double.parseDouble(cadena.toString()));
			} else if(localName.equals("direccion")) {
				sitio.setDireccion(stringFromBase64(cadena.toString()));
			} else if(localName.equals("poblacion")) {
				sitio.setPoblacion(stringFromBase64(cadena.toString()));
			} else if(localName.equals("telefonosFijos")) {
				sitio.setTelefonosFijos(stringFromBase64(cadena.toString()));
			} else if(localName.equals("telefonosMoviles")) {
				sitio.setTelefonosMoviles(stringFromBase64(cadena.toString()));
			} else if(localName.equals("web")) {
				sitio.setWeb(stringFromBase64(cadena.toString()));
			} else if(localName.equals("email")) {
				sitio.setEmail(stringFromBase64(cadena.toString()));
			} else if(localName.equals("facebook")) {
				sitio.setFacebook(stringFromBase64(cadena.toString()));
			} else if(localName.equals("twitter")) {
				sitio.setTwitter(stringFromBase64(cadena.toString()));
			} else if(localName.equals("ranking")) {
				sitio.setRanking(Integer.parseInt(cadena.toString()));
			} else if(localName.equals("activo")) {
				int valor = Integer.parseInt(cadena.toString());
				sitio.setActivo(ConversionesUtil.intToBoolean(valor));
			} else if(localName.equals("ultimaActualizacion")) {
				Date ultimaActualizacionDefault = UtilFechas.fechaFromUTC(cadena.toString().trim());
				sitio.setUltimaActualizacion(ultimaActualizacionDefault);
			} else if(localName.equals("sitio")) {
				lstSitios.add(sitio);
			}
		} catch(Exception e) {
			throw new SAXException("Error al leer una cadena #" + cadena.toString().trim() + "#", e);
		}
	}
	
	private String stringFromBase64(String txtBase64) {
		String textoBase64 = txtBase64.toString();
		return new String(Base64.decode(textoBase64, Base64.DEFAULT));
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
