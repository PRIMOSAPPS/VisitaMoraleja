package com.primos.visitamoraleja.xml;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.primos.visitamoraleja.contenidos.Categoria;
import com.primos.visitamoraleja.util.ConversionesUtil;

/**
 * Parsea un XML para convertir su contenido en una lista de categorias.
 * @author h
 *
 */
public class ManejadorCategoriasXML extends DefaultHandler {
	private StringBuilder cadena;
	private List<Categoria> lstCategorias = null;
	private Categoria categoria;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	@Override
	public void startDocument() throws SAXException {
		cadena = new StringBuilder();
		lstCategorias = new ArrayList<Categoria>();
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
			if(localName.equals("idCategoria")) {
				categoria.setId(Long.parseLong(cadena.toString()));
			} else if(localName.equals("nombre")) {
				categoria.setNombre(cadena.toString());
			} else if(localName.equals("descripcion")) {
				categoria.setDescripcion(cadena.toString());
			} else if(localName.equals("nombreIcono")) {
				categoria.setNombreIcono(cadena.toString());
			} else if(localName.equals("numeroSitios")) {
				categoria.setNumeroSitios(Integer.parseInt(cadena.toString()));
			} else if(localName.equals("icono")) {
//				byte[] strImagenBase64 = cadena.toString().trim().getBytes("UTF-16"); ;
//				byte[] base64decoded = Base64.decode(strImagenBase64, Base64.DEFAULT);
//				Bitmap icono = BitmapFactory.decodeByteArray(base64decoded, 0, base64decoded.length);
				categoria.setIcono(ConversionesUtil.getBitmap(cadena));
			} else if(localName.equals("ultimaActualizacion")) {
				categoria.setUltimaActualizacion(sdf.parse(cadena.toString().trim()));
			} else if(localName.equals("categoria")) {
				lstCategorias.add(categoria);
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
		if(localName.equals("categoria")) {
			Log.w("CATEGORIAS: ", "Una categoria nueva");
			categoria = new Categoria();
		}
	}

	public List<Categoria> getLstElements() {
		return lstCategorias;
	}


}
