package com.primos.visitamoraleja.eventos;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.primos.visitamoraleja.contenidos.Categoria;
import com.primos.visitamoraleja.contenidos.Evento;
import com.primos.visitamoraleja.contenidos.Sitio;
import com.primos.visitamoraleja.xml.ManejadorCategoriasXML;
import com.primos.visitamoraleja.xml.ManejadorEventosXML;
import com.primos.visitamoraleja.xml.ManejadorSitiosXML;

/**
 * Lee los datos del XML recibido como un InputStream y devuelve una lista de los objetos resultantes.
 * Para ello se sirve de las clases ManejadorCategoriasXML, ManejadorSitiosXML o ManejadorEventosXML
 * segun corresponda
 * @author h
 *
 */
public class EventosXML_SAX {
	public EventosXML_SAX() {
	}
	
	public List<Categoria> leerCategoriasXML(InputStream is) throws ParserConfigurationException, SAXException, IOException {
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		XMLReader reader = parser.getXMLReader();
		
		ManejadorCategoriasXML manejador = new ManejadorCategoriasXML();
		reader.setContentHandler(manejador);
		reader.parse(new InputSource(is));
		
		return manejador.getLstElements();
	}

	public List<Sitio> leerSitiosXML(InputStream is) throws ParserConfigurationException, SAXException, IOException {
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		XMLReader reader = parser.getXMLReader();
		
		ManejadorSitiosXML manejador = new ManejadorSitiosXML();
		reader.setContentHandler(manejador);
		reader.parse(new InputSource(is));
		
		return manejador.getLstElements();
	}

	public List<Evento> leerEventosXML(InputStream is) throws ParserConfigurationException, SAXException, IOException {
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		XMLReader reader = parser.getXMLReader();
		
		ManejadorEventosXML manejador = new ManejadorEventosXML();
		reader.setContentHandler(manejador);
		reader.parse(new InputSource(is));
		
		return manejador.getLstElements();
	}

}
