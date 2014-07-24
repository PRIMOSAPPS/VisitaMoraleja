package com.primos.visitamoraleja.util;

import java.util.List;

import com.primos.visitamoraleja.PreferenciasActivity;
import com.primos.visitamoraleja.bdsqlite.datasource.CategoriasDataSource;
import com.primos.visitamoraleja.contenidos.Categoria;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

public class UtilPreferencias {
	private final static String ALMACENAMIENTO = "tipoAlmacenamiento";
	private final static String PRIMER_ARRANQUE = "primerArranque";
	public final static String ALMACENAMIENTO_INTERNO = "almacenamientoInterno";
	public final static String ALMACENAMIENTO_EXTERNO = "almacenamientoExterno";

	public static boolean esPrimerArranque(Context contexto) {
		Log.d("[UtilPreferencias]", "Se comprueba el primer arranque.");
		
		SharedPreferences ratePrefs = PreferenceManager
                .getDefaultSharedPreferences(contexto);
		return ratePrefs.getBoolean(PRIMER_ARRANQUE, true);
	}
	
	public static void marcarPrimarArranqueRealizado(Context contexto) {
		SharedPreferences ratePrefs = PreferenceManager
                .getDefaultSharedPreferences(contexto);
        Editor edit = ratePrefs.edit();
        edit.putBoolean(PRIMER_ARRANQUE, false);
        edit.commit();
	}
	
	public static void asignarModoAlmacenamiento(Context contexto) {
		SharedPreferences ratePrefs = PreferenceManager
                .getDefaultSharedPreferences(contexto);
		Editor edit = ratePrefs.edit();
		
		Log.d("[UtilPreferencias]", "Comprobando el tipo de almacenamiento");
        String estadoSD = Environment.getExternalStorageState();
        
        Log.d("[UtilPreferencias]", "Estado del almacenamiento externo: " + estadoSD);
        String almacenamiento = ALMACENAMIENTO_INTERNO;
        if(estadoSD.equals(Environment.MEDIA_MOUNTED)) {
        	almacenamiento = ALMACENAMIENTO_EXTERNO;
        }
        
        Log.d("[UtilPreferencias]", "Almacenamiento asignado: " + almacenamiento);
        edit.putString(ALMACENAMIENTO, almacenamiento);

        edit.commit();
	}
	
	public static String getTipoAlmacenamiento(Context contexto) {
		SharedPreferences ratePrefs = PreferenceManager
                .getDefaultSharedPreferences(contexto);
		return ratePrefs.getString(ALMACENAMIENTO, ALMACENAMIENTO_INTERNO);
	}
	
	
	public static boolean actualizacionAutomatica(Context contexto) {
		Log.w("[UtilPreferencias]", "Se comprueba si estan activadas las actualizaciones automaticas.");
		
		SharedPreferences ratePrefs = PreferenceManager
                .getDefaultSharedPreferences(contexto);
		return ratePrefs.getBoolean(PreferenciasActivity.PREFERENCIA_ACTUALIZAR_AUTOMATICAMENTE, true);
	}
	
	/**
	 * Devuelve el String que contiene los identificadores de las categorias para actualizar solo por estas categorias.
	 * Los identificadores estan separados por comas
	 * Si no hay ninguna categoria seleccionada, se devuelve null
	 * @return
	 */
	public static String getActualizacionPorCategorias(Context contexto) {
		String resul = "";
		CategoriasDataSource dataSource = new CategoriasDataSource(contexto);
		try {
			dataSource.open();
			List<Categoria> lstCategorias = dataSource.getAll();
			
			SharedPreferences ratePrefs = PreferenceManager
	                .getDefaultSharedPreferences(contexto);
			boolean actualizarPorCategorias = ratePrefs.getBoolean(PreferenciasActivity.PREFERENCIA_ACTUALIZAR_POR_CATEGORIAS, false);
			if(actualizarPorCategorias) {
				for(Categoria categoria : lstCategorias) {
					String key = PreferenciasActivity.PREFIJO_PREFERENCIA_CATEGORIAS + categoria.getId();
					boolean activada = ratePrefs.getBoolean(key, false);
					if(activada) {
						resul += ","+categoria.getId();
					}
				}
			}
			if(!resul.equals("")) {
				resul = resul.substring(1);
			} else {
				resul = null;
			}
		} finally {
			dataSource.close();
		}
		return resul;
	}

}
