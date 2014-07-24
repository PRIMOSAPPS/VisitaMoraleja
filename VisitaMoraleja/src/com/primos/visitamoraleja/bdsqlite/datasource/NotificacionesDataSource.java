package com.primos.visitamoraleja.bdsqlite.datasource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.primos.visitamoraleja.bdsqlite.NotificacionesSQLite;
import com.primos.visitamoraleja.contenidos.Notificacion;

/**
 * Controla el acceso a la tabla de las categorias
 * @author h
 *
 */
public class NotificacionesDataSource extends AbstractDataSource {
	private static String[] allColumns = { NotificacionesSQLite.COLUMNA_ID,
			NotificacionesSQLite.COLUMNA_TITULO,
			NotificacionesSQLite.COLUMNA_TEXTO,
			NotificacionesSQLite.COLUMNA_FECHA_INICIO_VALIDEZ,
			NotificacionesSQLite.COLUMNA_FECHA_FIN_VALIDEZ};

	public NotificacionesDataSource(Context context) {
		super(context);
	}

	@Override
	protected SQLiteOpenHelper crearDbHelper(Context context) {
		return new NotificacionesSQLite(context);
	}

	public long insertar(Notificacion notificacion) {
		ContentValues valores = eventoToObject(notificacion);
		return database.insert(NotificacionesSQLite.TABLE_NAME, null, valores);
	}
	
	/**
	 * Devuelve la fecha de la ultima actualizacion de la tabla
	 * @return 
	 */
	public void eliminarPasadas() {
		long fechaHoy = new Date().getTime();
		String sql = "DELETE FROM " + NotificacionesSQLite.TABLE_NAME + " WHERE " +
					NotificacionesSQLite.COLUMNA_FECHA_FIN_VALIDEZ + " < " + fechaHoy;
		database.rawQuery(sql, null);
	}

	/**
	 * Devuelve una notificacion por su identificador
	 * @param id
	 * @return
	 */
	public Notificacion getById(long id) {
		Notificacion resul = null;
		String where = NotificacionesSQLite.COLUMNA_ID + " = " + id;
		Cursor cursor = database.query(NotificacionesSQLite.TABLE_NAME,
				allColumns, where, null, null, null, null);
		cursor.moveToFirst();
	    if(!cursor.isAfterLast()) {
			resul = cursorToObject(cursor);
		}
		cursor.close();
		
		return resul;
	}

	/**
	 * Devuelve todas las categorias existentes en la base de datos.
	 * @return
	 */
	public List<Notificacion> getAll() {
		List<Notificacion> resul = new ArrayList<Notificacion>();

		Cursor cursor = database.query(NotificacionesSQLite.TABLE_NAME,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Notificacion notificacion = cursorToObject(cursor);

			resul.add(notificacion);
			cursor.moveToNext();
		}
		cursor.close();
		return resul;
	}
	
	/**
	 * Actualiza la notificacion recibida en la base de datos.
	 * @param notificacion
	 * @return
	 */
	public long actualizar(Notificacion notificacion) {
		ContentValues valores = eventoToObject(notificacion);
		return database.update(NotificacionesSQLite.TABLE_NAME, valores,
				NotificacionesSQLite.COLUMNA_ID + "=" + notificacion.getId(), null);
	}

	/**
	 * Borra la notificacion recibida de la base de datos.
	 * @param notificacion
	 */
	public void delete(Notificacion notificacion) {
		long id = notificacion.getId();
		database.delete(NotificacionesSQLite.TABLE_NAME, NotificacionesSQLite.COLUMNA_ID
				+ " = " + id, null);
	}

	/**
	 * Convierte un cursor recibido de una consulta a la base de datos en un objeto Notificacion.
	 * @param cursor
	 * @return
	 */
	private Notificacion cursorToObject(Cursor cursor) {
		Notificacion resul = new Notificacion();
		resul.setId(cursor.getLong(cursor.getColumnIndex(NotificacionesSQLite.COLUMNA_ID)));
		resul.setTitulo(cursor.getString(cursor.getColumnIndex(NotificacionesSQLite.COLUMNA_TITULO)));
		resul.setTexto(cursor.getString(cursor.getColumnIndex(NotificacionesSQLite.COLUMNA_TEXTO)));
		long fechaInicioValidez = cursor.getColumnIndex(NotificacionesSQLite.COLUMNA_FECHA_INICIO_VALIDEZ);
		resul.setFechaInicioValidez(new Date(fechaInicioValidez));
		long fechaFinValidez = cursor.getColumnIndex(NotificacionesSQLite.COLUMNA_FECHA_FIN_VALIDEZ);
		resul.setFechaFinValidez(new Date(fechaFinValidez));

		return resul;
	}

	/**
	 * Convierte un objeto notificacion en un objeto ContentValues que podra ser usado para insercion/actualizacion
	 * de la base de datos.
	 * @param notificacion
	 * @return
	 */
	private ContentValues eventoToObject(Notificacion notificacion) {
		ContentValues valores = new ContentValues();
		valores.put(NotificacionesSQLite.COLUMNA_ID, notificacion.getId());
		valores.put(NotificacionesSQLite.COLUMNA_TITULO, notificacion.getTitulo());
		valores.put(NotificacionesSQLite.COLUMNA_TEXTO,
				notificacion.getTexto());
		valores.put(NotificacionesSQLite.COLUMNA_FECHA_INICIO_VALIDEZ,
				notificacion.getFechaInicioValidez().getTime());
		valores.put(NotificacionesSQLite.COLUMNA_FECHA_FIN_VALIDEZ,
				notificacion.getFechaFinValidez().getTime());

		return valores;
	}

}