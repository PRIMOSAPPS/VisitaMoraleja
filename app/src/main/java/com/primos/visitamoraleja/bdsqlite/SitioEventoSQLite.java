package com.primos.visitamoraleja.bdsqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.Date;

/**
 * Created by h on 7/06/16.
 */
public class SitioEventoSQLite extends SQLiteOpenHelper {
    public final static String TABLE_NAME = "sitiosevento";
    public final static String COLUMNA_ID = "id";
    public final static String COLUMNA_ID_EVENTO = "id_evento";
    public final static String COLUMNA_ID_CATEGORIA = "id_categoria";
    public final static String COLUMNA_ES_SITIO_REGISTRADO = "es_sitio_registrado";
    public final static String COLUMNA_ID_SITIO_REGISTRADO = "id_sitio_registrado";
    public final static String COLUMNA_NOMBRE = "nombre";
    public final static String COLUMNA_TEXTO = "texto";
    public final static String COLUMNA_DESCRIPCION = "descripcion";
    public final static String COLUMNA_NOMBRE_ICONO = "nombre_icono";
    public final static String COLUMNA_LATITUD = "latitud";
    public final static String COLUMNA_LONGITUD = "longitud";
    public final static String COLUMNA_ACTIVO = "activo";
    public final static String COLUMNA_ULTIMA_ACTUALIZACION = "ultima_actualizacion";

    private static final int DATABASE_VERSION = 1;

    private final static String CREATE_TABLA = "CREATE TABLE " + TABLE_NAME +
            " (id INTEGER PRIMARY KEY, id_evento INTEGER, id_categoria INTEGER, es_sitio_registrado INTEGER, id_sitio_registrado INTEGER, " +
            "nombre TEXT, texto TEXT, descripcion TEXT, nombre_icono TEXT, latitud NUMERIC, longitud NUMERIC, " +
            " activo INTEGER, ultima_actualizacion NUMERIC )";

    public SitioEventoSQLite(Context context) {
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
