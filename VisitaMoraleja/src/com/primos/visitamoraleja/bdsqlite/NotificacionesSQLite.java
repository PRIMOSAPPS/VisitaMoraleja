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
public class NotificacionesSQLite extends SQLiteOpenHelper {
	public final static String TABLE_NAME = "notificaciones";
	public final static String COLUMNA_ID = "id";
	public final static String COLUMNA_TITULO = "titulo";
	public final static String COLUMNA_TEXTO = "texto";
	public final static String COLUMNA_FECHA_INICIO_VALIDEZ = "fecha_inicio_validez";
	public final static String COLUMNA_FECHA_FIN_VALIDEZ = "fecha_fin_validez";
	
	private static final int DATABASE_VERSION = 1;
	
	private final static String CREATE_TABLA = "CREATE TABLE " + TABLE_NAME + 
			" (id INTEGER PRIMARY KEY, titulo TEXT, texto TEXT, " +
			"fecha_inicio_validez NUMERIC, fecha_fin_validez NUMERIC )";

	public NotificacionesSQLite(Context context) {
		super(context, TABLE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		db.execSQL(CREATE_TABLA);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(NotificacionesSQLite.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

}
