package com.primos.visitamoraleja.almacenamiento;

import com.primos.visitamoraleja.util.UtilPreferencias;

import android.content.Context;

public class AlmacenamientoFactory {
	
	/**
	 * Devuelve el tipo de almacenamiento segun la inicializacion realizada en el primer arranque, en el que se decide
	 * si el almacenamiento sera interno o externo.
	 * @param contexto
	 * @return
	 */
	public static ItfAlmacenamiento getAlmacenamiento(Context contexto) {
		ItfAlmacenamiento resul = null;
		
		String tipoAlmacenamiento = UtilPreferencias.getTipoAlmacenamiento(contexto);
		
		if(UtilPreferencias.ALMACENAMIENTO_INTERNO.equals(tipoAlmacenamiento)) {
			resul = new AlmacenamientoInterno(contexto);
		} else {
			resul = new AlmacenamientoExterno();
		}
//		resul = new AlmacenamientoInterno(contexto);
		
		return resul;
	}

}
