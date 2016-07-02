package com.primos.visitamoraleja.bdsqlite.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.primos.visitamoraleja.bdsqlite.EventosSQLite;
import com.primos.visitamoraleja.bdsqlite.SitiosSQLite;
import com.primos.visitamoraleja.contenidos.Evento;
import com.primos.visitamoraleja.util.ConversionesUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Controla el acceso a la tabla de los eventos
 * @author h
 *
 */
public class EventosDataSource extends AbstractDataSource {
	// Database fields
	private String[] allColumns = { EventosSQLite.COLUMNA_ID,
			EventosSQLite.COLUMNA_NOMBRE,
			EventosSQLite.COLUMNA_TEXTO,
			EventosSQLite.COLUMNA_DESCRIPCION,
			EventosSQLite.COLUMNA_NOMBRE_ICONO,
			EventosSQLite.COLUMNA_LATITUD,
			EventosSQLite.COLUMNA_LONGITUD,
			EventosSQLite.COLUMNA_ZOOM_INICIAL,
			EventosSQLite.COLUMNA_INICIO,
			EventosSQLite.COLUMNA_FIN,
			EventosSQLite.COLUMNA_ACTIVO,
			EventosSQLite.COLUMNA_ULTIMA_ACTUALIZACION};
	

	public EventosDataSource(Context context) {
		super(context);
	}

	@Override
	protected SQLiteOpenHelper crearDbHelper(Context context) {
		return new EventosSQLite(context);
	}
	
	/**
	 * Convierte un objeto evento en un objeto ContentValues que podra ser usado para insercion/actualizacion
	 * de la base de datos.
	 * @param evento
	 * @return
	 */
	private ContentValues objectToContentValues(Evento evento) {
		ContentValues valores = new ContentValues();
		valores.put(EventosSQLite.COLUMNA_ID, evento.getId());
		valores.put(EventosSQLite.COLUMNA_NOMBRE, evento.getNombre());
		valores.put(EventosSQLite.COLUMNA_TEXTO, evento.getTexto());
		valores.put(EventosSQLite.COLUMNA_DESCRIPCION, evento.getDescripcion());
		valores.put(EventosSQLite.COLUMNA_NOMBRE_ICONO, evento.getNombreIcono());
		valores.put(EventosSQLite.COLUMNA_LATITUD, evento.getLatitud());
		valores.put(EventosSQLite.COLUMNA_LONGITUD, evento.getLongitud());
		valores.put(EventosSQLite.COLUMNA_ZOOM_INICIAL, evento.getZoomInicial());
		valores.put(EventosSQLite.COLUMNA_INICIO, evento.getInicio().getTime());
		valores.put(EventosSQLite.COLUMNA_FIN, evento.getFin().getTime());
		int activo = ConversionesUtil.booleanToInt(evento.isActivo());
		valores.put(EventosSQLite.COLUMNA_ACTIVO, activo);
		valores.put(EventosSQLite.COLUMNA_ULTIMA_ACTUALIZACION, evento
				.getUltimaActualizacion().getTime());
		return valores;
	}

	public long insertar(Evento evento) {
		ContentValues valores = objectToContentValues(evento);
		return database.insert(EventosSQLite.TABLE_NAME, null, valores);

	}

	public long actualizar(Evento evento) {
		ContentValues valores = objectToContentValues(evento);
		return database.update(EventosSQLite.TABLE_NAME, valores,
				EventosSQLite.COLUMNA_ID + "=" + evento.getId(), null);
	}

	public void delete(Evento evento) {
		long id = evento.getId();
		database.delete(EventosSQLite.TABLE_NAME, EventosSQLite.COLUMNA_ID
				+ " = " + id, null);
	}
	
	public Evento getById(long id) {
		Evento resul = null;
		String where = EventosSQLite.COLUMNA_ID + " = " + id;
		Cursor cursor = database.query(EventosSQLite.TABLE_NAME,
				allColumns, where, null, null, null, null);
		cursor.moveToFirst();
	    if(!cursor.isAfterLast()) {
			resul = cursorToObject(cursor);
		}
		cursor.close();
		
		return resul;
	}
	
	private List<Evento> getListaEventosByCursor(Cursor cursor) {
		List<Evento> resul = new ArrayList<Evento>();
		cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
			Evento evento = cursorToObject(cursor);
			Log.d("[EventosDataSource]", "Leyendo un evento del cursor: " + evento);
			resul.add(evento);
			cursor.moveToNext();
		}
	    return resul;
	}

	/**
	 * Devuelve la fecha de la ultima actualizacion de la tabla
	 * @return
	 */
	public long getUltimaActualizacion() {
		String sql = "SELECT MAX(" + EventosSQLite.COLUMNA_ULTIMA_ACTUALIZACION + ") FROM " +
					EventosSQLite.TABLE_NAME;
		String[] bindVars = {};
		Cursor cursor = database.rawQuery(sql, bindVars);
		
		long ultimaActualizacion = 0;
		cursor.moveToFirst();
	    if(!cursor.isAfterLast()) {
			ultimaActualizacion = cursor.getLong(0);
		}
		cursor.close();
		return ultimaActualizacion;
	}

	public List<Evento> getAll() {
		List<Evento> resul = new ArrayList<Evento>();

		String seleccion = EventosSQLite.COLUMNA_ACTIVO + " = 1 OR 1=1";
		Cursor cursor = database.query(EventosSQLite.TABLE_NAME,
				allColumns, seleccion, null, null, null, null);

		resul = getListaEventosByCursor(cursor);
		cursor.close();
		// make sure to close the cursor
		cursor.close();
		return resul;
	}

	/**
	 * Convierte un cursor recibido de una consulta a la base de datos en un objeto Evento.
	 * @param cursor
	 * @return
	 */
	private Evento cursorToObject(Cursor cursor) {
		Evento resul = new Evento();
		resul.setId(cursor.getLong(cursor.getColumnIndex(EventosSQLite.COLUMNA_ID)));
		resul.setNombre(cursor.getString(cursor.getColumnIndex(EventosSQLite.COLUMNA_NOMBRE)));
		resul.setTexto(cursor.getString(cursor.getColumnIndex(EventosSQLite.COLUMNA_TEXTO)));
		resul.setDescripcion(cursor.getString(cursor.getColumnIndex(EventosSQLite.COLUMNA_DESCRIPCION)));
		resul.setNombreIcono(cursor.getString(cursor.getColumnIndex(EventosSQLite.COLUMNA_NOMBRE_ICONO)));
		double latitud = cursor.getDouble(cursor.getColumnIndex(EventosSQLite.COLUMNA_LATITUD));
		double longitud = cursor.getDouble(cursor.getColumnIndex(EventosSQLite.COLUMNA_LONGITUD));
		resul.setLatitud(latitud);
		resul.setLongitud(longitud);
		float zoomInicial = cursor.getFloat(cursor.getColumnIndex(EventosSQLite.COLUMNA_ZOOM_INICIAL));
		resul.setZoomInicial(zoomInicial);
		long inicio = cursor.getLong(cursor.getColumnIndex(EventosSQLite.COLUMNA_INICIO));
		resul.setInicio(new Date(inicio));
		long fin = cursor.getLong(cursor.getColumnIndex(EventosSQLite.COLUMNA_FIN));
		resul.setFin(new Date(fin));
		int intActivo = cursor.getInt(cursor.getColumnIndex(SitiosSQLite.COLUMNA_ACTIVO));
		resul.setActivo(ConversionesUtil.intToBoolean(intActivo));
		long ultimaActualizacion = cursor.getLong(cursor.getColumnIndex(EventosSQLite.COLUMNA_ULTIMA_ACTUALIZACION));
		resul.setUltimaActualizacion(new Date(ultimaActualizacion));
		
		return resul;
	}

}
