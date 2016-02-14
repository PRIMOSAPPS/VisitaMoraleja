package com.primos.visitamoraleja.bdsqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Crea la tabla categorias en la base de datos
 * @author h
 *
 */
public class CategoriasSQLite extends SQLiteOpenHelper {
	public final static String TABLE_NAME = "categorias";
	public final static String COLUMNA_ID = "id";
	public final static String COLUMNA_NOMBRE = "nombre";
	public final static String COLUMNA_DESCRIPCION = "descripcion";
	public final static String COLUMNA_NOMBRE_ICONO = "nombre_icono";
	public final static String COLUMNA_NUMERO_SITIOS = "numero_sitios";
	public final static String COLUMNA_ULTIMA_ACTUALIZACION = "ultima_actualizacion";
	
	private static final int DATABASE_VERSION = 1;
	
	private final static String CREATE_TABLA = "CREATE TABLE " + TABLE_NAME + 
			" (id INTEGER PRIMARY KEY, nombre TEXT, descripcion TEXT, nombre_icono TEXT, " +
			"numero_sitios NUMERIC, ultima_actualizacion NUMERIC )";

	public CategoriasSQLite(Context context) {
		super(context, TABLE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		db.execSQL(CREATE_TABLA);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(CategoriasSQLite.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

}
