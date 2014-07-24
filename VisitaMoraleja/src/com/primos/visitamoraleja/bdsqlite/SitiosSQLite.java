package com.primos.visitamoraleja.bdsqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Crea la tabla sitios en la base de datos
 * @author h
 *
 */
public class SitiosSQLite extends SQLiteOpenHelper {
	public final static String TABLE_NAME = "sitios";
	public final static String COLUMNA_ID = "id";
	public final static String COLUMNA_ID_CATEGORIA = "id_categoria";
	public final static String COLUMNA_NOMBRE = "nombre";
	public final static String COLUMNA_POBLACION = "poblacion";
	public final static String COLUMNA_TEXTO_CORTO_1 = "texto_corto1";
	public final static String COLUMNA_TEXTO_CORTO_2 = "texto_corto2";
	public final static String COLUMNA_TEXTO_CORTO_3 = "texto_corto3";
	public final static String COLUMNA_TEXTO_LARGO_1 = "texto_largo1";
	public final static String COLUMNA_TEXTO_LARGO_2 = "texto_largo2";
	public final static String COLUMNA_NOMBRE_LOGOTIPO = "nombre_logotipo";
	public final static String COLUMNA_NOMBRE_IMAGEN_1 = "nombre_imagen1";
	public final static String COLUMNA_NOMBRE_IMAGEN_2 = "nombre_imagen2";
	public final static String COLUMNA_NOMBRE_IMAGEN_3 = "nombre_imagen3";
	public final static String COLUMNA_NOMBRE_IMAGEN_4 = "nombre_imagen4";
	public final static String COLUMNA_LATITUD = "latitud";
	public final static String COLUMNA_LONGITUD = "longitud";
	public final static String COLUMNA_DIRECCION = "direccion";
	public final static String COLUMNA_TELEFONOS_FIJOS = "telefonos_fijos";
	public final static String COLUMNA_TELEFONOS_MOVILES = "telefonos_moviles";
	public final static String COLUMNA_WEB = "web";
	public final static String COLUMNA_FACEBOOK = "facebook";
	public final static String COLUMNA_TWITTER = "twitter";
	public final static String COLUMNA_RANKING = "ranking";
	public final static String COLUMNA_FAVORITO = "favorito";
	public final static String COLUMNA_ACTIVO = "activo";
	public final static String COLUMNA_ULTIMA_ACTUALIZACION = "ultima_actualizacion";

	
	private static final int DATABASE_VERSION = 1;
	
	private final static String CREATE_TABLA = "CREATE TABLE " + TABLE_NAME + 
			" (id INTEGER PRIMARY KEY, id_categoria INTEGER, nombre TEXT, poblacion TEXT, texto_corto1 TEXT" +
			", texto_corto2 TEXT, texto_corto3 TEXT, texto_largo1 TEXT, texto_largo2 TEXT, nombre_logotipo TEXT" +
			", nombre_imagen1 TEXT, nombre_imagen2 TEXT, nombre_imagen3 TEXT, nombre_imagen4 TEXT, latitud NUMERIC" +
			", longitud NUMERIC, direccion TEXT, telefonos_fijos TEXT, telefonos_moviles TEXT, web TEXT, facebook TEXT" +
			", twitter TEXT, ranking INTEGER, favorito INTEGER, activo INTEGER, ultima_actualizacion NUMERIC )";

	public SitiosSQLite(Context context) {
		super(context, TABLE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		db.execSQL(CREATE_TABLA);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(SitiosSQLite.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

}
