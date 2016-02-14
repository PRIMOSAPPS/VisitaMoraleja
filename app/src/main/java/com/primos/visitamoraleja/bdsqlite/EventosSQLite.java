package com.primos.visitamoraleja.bdsqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Crea la tabla eventos en la base de datos
 * @author h
 *
 */
public class EventosSQLite extends SQLiteOpenHelper {
	public final static String TABLE_NAME = "eventos";
	public final static String COLUMNA_ID = "id";
	public final static String COLUMNA_ID_CATEGORIA = "id_categoria";
	public final static String COLUMNA_ID_SITIO = "id_sitio";
	public final static String COLUMNA_NOMBRE = "nombre";
	public final static String COLUMNA_ES_EVENTO = "es_evento";
	public final static String COLUMNA_TEXTO_1 = "texto_1";
	public final static String COLUMNA_TEXTO_2 = "texto_2";
	public final static String COLUMNA_NOMBRE_ICONO = "nombre_icono";
	public final static String COLUMNA_LATITUD = "latitud";
	public final static String COLUMNA_LONGITUD = "longitud";
	public final static String COLUMNA_INICIO = "inicio";
	public final static String COLUMNA_FIN = "fin";
	public final static String COLUMNA_ACTIVO = "activo";
	public final static String COLUMNA_ULTIMA_ACTUALIZACION = "ultima_actualizacion";

	
	private static final int DATABASE_VERSION = 1;
	
	private final static String CREATE_TABLA = "CREATE TABLE " + TABLE_NAME + 
			" (id INTEGER PRIMARY KEY, id_categoria INTEGER, id_sitio INTEGER, nombre TEXT, es_evento INTEGER, texto_1 TEXT" +
			", texto_2 TEXT, nombre_icono TEXT, latitud NUMERIC, longitud NUMERIC, inicio NUMERIC, fin NUMERIC" +
			", activo INTEGER, ultima_actualizacion NUMERIC )";

	public EventosSQLite(Context context) {
		super(context, TABLE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		db.execSQL(CREATE_TABLA);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(EventosSQLite.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

}
