package com.primos.visitamoraleja.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.primos.visitamoraleja.contenidos.Sitio;

/**
 * Parsea un XML para convertir su contenido en una lista de sitios.
 * 
 * @author h
 *
 */
public class ManejadorSitiosActualizablesXML extends DefaultHandler {
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
			} else if(localName.equals("nombre")) {
				sitio.setNombre(cadena.toString());
			} else if(localName.equals("sitio_actualizable")) {
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
		if(localName.equals("sitio_actualizable")) {
			Log.w("SITIOS: ", "Un sitio nuevo");
			sitio = new Sitio();
		}
	}

	public List<Sitio> getLstElements() {
		return lstSitios;
	}


}