package com.primos.visitamoraleja.almacenamiento;

import com.primos.visitamoraleja.permisos.Permisos;
import com.primos.visitamoraleja.util.UtilPreferencias;

import android.Manifest;
import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class AlmacenamientoFactory {

	/**
	 * Devuelve el tipo de almacenamiento segun la inicializacion realizada en el primer arranque, en el que se decide
	 * si el almacenamiento sera interno o externo.
	 * @param contexto
	 * @return
	 */
	public static ItfAlmacenamiento getAlmacenamiento(Context contexto) {
		ItfAlmacenamiento resul = new AlmacenamientoNulo(contexto);

		if(conPermisos((Activity)contexto)) {
			String tipoAlmacenamiento = UtilPreferencias.getTipoAlmacenamiento(contexto);

			if (UtilPreferencias.ALMACENAMIENTO_INTERNO.equals(tipoAlmacenamiento)) {
				resul = new AlmacenamientoInterno(contexto);
			} else {
				resul = new AlmacenamientoExterno();
			}
		}
//		resul = new AlmacenamientoInterno(contexto);
		
		return resul;
	}

	private static boolean conPermisos(Activity actividad) {
		Permisos permisosUtil = new Permisos();
		return permisosUtil.permisosConcedido(actividad, ItfAlmacenamiento.permisosNecesarios);
	}

}
