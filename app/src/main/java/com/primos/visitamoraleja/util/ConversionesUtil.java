package com.primos.visitamoraleja.util;

import java.io.UnsupportedEncodingException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Funciones que contienen acciones muy simples pero que dejan el codigo mas limpio, como convertir
 * un entero en un booleano, un boolean en un entero o convertir el String recibido en un XML
 * en un objeto Bitmap.
 * Estas funciones son usadas en el tratamiento de los datos a la base de datos y XML.
 * <b>SI NO RECUERDO MAL, SQLITE NO TIENE TIPO BOOLEAN, ASI QUE LO HE REPRESENTADO POR ENTERO</b>
 * 
 * @author h
 *
 */
public class ConversionesUtil {

	/**
	 * Convierte un entero en un booleano considerando un 0 como false y cualqueir otro valor como true
	 * @param valor
	 * @return
	 */
	public static boolean intToBoolean(int valor) {
		if(valor == 0) {
			return false;
		}
		return true;
	}

	/**
	 * Copnvierte un booleano en un entero convirtiendo true en 1 y false en 0
	 * 
	 * @param valor
	 * @return
	 */
	public static int booleanToInt(boolean valor) {
		if(valor) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * Las imagenes recibidas en los XML estan codificadas en base64, esta funcion convierte el String contenido en el XML
	 * en un objeto tipo Bitmap
	 * @param cadena
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static Bitmap getBitmap(StringBuilder cadena) throws UnsupportedEncodingException {
		byte[] strImagenBase64 = cadena.toString().trim().getBytes("UTF-16"); ;
		byte[] base64decoded = Base64.decode(strImagenBase64, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(base64decoded, 0, base64decoded.length);
	}

}
