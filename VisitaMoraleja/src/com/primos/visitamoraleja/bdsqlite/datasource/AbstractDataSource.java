package com.primos.visitamoraleja.bdsqlite.datasource;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Clase padre del resto de clases XXXDataSource existentes.
 * <b>Creo que se pueden mover mas metodos a esta clase, pero todavia no los he movido</b>
 * 
 * Estas clases se usaran acceder/actualizar a los contenidos de la base de datos
 * <b>No se accede directamente a traves de las clases XXXSQLite</b>
 * @author h
 *
 */
public abstract class AbstractDataSource {
	protected SQLiteDatabase database;
	protected SQLiteOpenHelper dbHelper;
	protected Context context;

	public AbstractDataSource(Context context) {
		this.context = context;
		dbHelper = crearDbHelper(context);
	}
	
	protected abstract SQLiteOpenHelper crearDbHelper(Context context);

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

}
