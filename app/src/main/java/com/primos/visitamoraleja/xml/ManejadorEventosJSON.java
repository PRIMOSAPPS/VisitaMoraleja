package com.primos.visitamoraleja.xml;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Base64;
import android.util.Log;

import com.primos.visitamoraleja.contenidos.Evento;
import com.primos.visitamoraleja.util.ConversionesUtil;
import com.primos.visitamoraleja.util.UtilFechas;

/**
 * Parsea un XML para convertir su contenido en una lista de eventos.
 * 
 * @author h
 *
 */
public class ManejadorEventosJSON extends DefaultHandler {
	private StringBuilder cadena;
	private List<Evento> lstEventos = null;
	private Evento evento;
	
	@Override
	public void startDocument() throws SAXException {
		cadena = new StringBuilder();
		lstEventos = new ArrayList<Evento>();
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
			if(localName.equals("idEvento")) {
				evento.setId(Long.parseLong(cadena.toString()));
				/*
			} else if(localName.equals("idSitio")) {
				evento.setIdSitio(Long.parseLong(cadena.toString()));
			} else if(localName.equals("idCategoria")) {
				evento.setIdCategoriaEvento(Long.parseLong(cadena.toString()));
			} else if(localName.equals("nombre")) {
				evento.setNombre(cadena.toString());
			} else if(localName.equals("esEvento")) {
				int valor = Integer.parseInt(cadena.toString());
				evento.setEsEvento(ConversionesUtil.intToBoolean(valor));
			} else if(localName.equals("texto1")) {
				String textoLargoBase64 = cadena.toString();
				String datosTextoLargo = new String(Base64.decode(textoLargoBase64, Base64.DEFAULT));
				evento.setTexto1(datosTextoLargo);
			} else if(localName.equals("texto2")) {
				String textoLargoBase64 = cadena.toString();
				String datosTextoLargo = new String(Base64.decode(textoLargoBase64, Base64.DEFAULT));
				evento.setTexto2(datosTextoLargo);
				*/
			} else if(localName.equals("nombreIcono")) {
				evento.setNombreIcono(cadena.toString());
			} else if(localName.equals("icono")) {
				evento.setIcono(ConversionesUtil.getBitmap(cadena));
			} else if(localName.equals("latitud")) {
				evento.setLatitud(Double.parseDouble(cadena.toString()));
			} else if(localName.equals("longitud")) {
				evento.setLongitud(Double.parseDouble(cadena.toString()));
			} else if(localName.equals("inicio")) {
				Date fechaInicio = UtilFechas.parseFromStrDefault(cadena.toString().trim());
				evento.setInicio(fechaInicio);
			} else if(localName.equals("fin")) {
				Date fechaFin = UtilFechas.parseFromStrDefault(cadena.toString().trim());
				evento.setFin(fechaFin);
			} else if(localName.equals("activo")) {
				int valor = Integer.parseInt(cadena.toString());
				evento.setActivo(ConversionesUtil.intToBoolean(valor));
			} else if(localName.equals("ultimaActualizacion")) {
				Date ultimaActualizacionDefault = UtilFechas.fechaFromUTC(cadena.toString().trim());
				evento.setUltimaActualizacion(ultimaActualizacionDefault);
			} else if(localName.equals("evento")) {
				lstEventos.add(evento);
			}
		} catch(Exception e) {
			throw new SAXException("Error al leer una fecha #" + cadena.toString().trim() + "#", e);
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		cadena.setLength(0);
		if(localName.equals("evento")) {
			Log.w("Eventos: ", "Un evento nuevo");
			evento = new Evento();
		}
	}

	public List<Evento> getLstElements() {
		return lstEventos;
	}


}
