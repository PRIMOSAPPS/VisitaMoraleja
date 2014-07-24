package com.primos.visitamoraleja.bdsqlite.datasource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.primos.visitamoraleja.bdsqlite.SitiosSQLite;
import com.primos.visitamoraleja.contenidos.Categoria;
import com.primos.visitamoraleja.contenidos.Sitio;
import com.primos.visitamoraleja.util.ConversionesUtil;

/**
 * Controla el acceso a la tabla de los sitios
 * @author h
 *
 */
public class SitiosDataSource extends AbstractDataSource {
	private final static String ORDER_BY_RANKING = " ranking DESC ";

	// Database fields
	private String[] allColumns = { SitiosSQLite.COLUMNA_ID,
			SitiosSQLite.COLUMNA_ID_CATEGORIA,
			SitiosSQLite.COLUMNA_NOMBRE,
			SitiosSQLite.COLUMNA_POBLACION,
			SitiosSQLite.COLUMNA_TEXTO_CORTO_1,
			SitiosSQLite.COLUMNA_TEXTO_CORTO_2,
			SitiosSQLite.COLUMNA_TEXTO_CORTO_3,
			SitiosSQLite.COLUMNA_TEXTO_LARGO_1,
			SitiosSQLite.COLUMNA_TEXTO_LARGO_2,
			SitiosSQLite.COLUMNA_NOMBRE_LOGOTIPO,
			SitiosSQLite.COLUMNA_NOMBRE_IMAGEN_1,
			SitiosSQLite.COLUMNA_NOMBRE_IMAGEN_2,
			SitiosSQLite.COLUMNA_NOMBRE_IMAGEN_3,
			SitiosSQLite.COLUMNA_NOMBRE_IMAGEN_4,
			SitiosSQLite.COLUMNA_LATITUD,
			SitiosSQLite.COLUMNA_LONGITUD,
			SitiosSQLite.COLUMNA_DIRECCION,
			SitiosSQLite.COLUMNA_TELEFONOS_FIJOS,
			SitiosSQLite.COLUMNA_TELEFONOS_MOVILES,
			SitiosSQLite.COLUMNA_WEB,
			SitiosSQLite.COLUMNA_FACEBOOK,
			SitiosSQLite.COLUMNA_TWITTER,
			SitiosSQLite.COLUMNA_RANKING,
			SitiosSQLite.COLUMNA_FAVORITO,
			SitiosSQLite.COLUMNA_ACTIVO,
			SitiosSQLite.COLUMNA_ULTIMA_ACTUALIZACION};
	

	public SitiosDataSource(Context context) {
		super(context);
	}

	@Override
	protected SQLiteOpenHelper crearDbHelper(Context context) {
		return new SitiosSQLite(context);
	}
	
	/**
	 * Convierte un objeto sitio en un objeto ContentValues que podra ser usado para insercion/actualizacion
	 * de la base de datos.
	 * @param evento
	 * @return
	 */
	private ContentValues objectToContentValues(Sitio sitio) {
		ContentValues valores = new ContentValues();
		valores.put(SitiosSQLite.COLUMNA_ID, sitio.getId());
		valores.put(SitiosSQLite.COLUMNA_ID_CATEGORIA, sitio.getIdCategoria());
		valores.put(SitiosSQLite.COLUMNA_NOMBRE, sitio.getNombre());
		valores.put(SitiosSQLite.COLUMNA_POBLACION, sitio.getPoblacion());
		valores.put(SitiosSQLite.COLUMNA_TEXTO_CORTO_1, sitio.getTextoCorto1());
		valores.put(SitiosSQLite.COLUMNA_TEXTO_CORTO_2, sitio.getTextoCorto2());
		valores.put(SitiosSQLite.COLUMNA_TEXTO_CORTO_3, sitio.getTextoCorto3());
		valores.put(SitiosSQLite.COLUMNA_TEXTO_LARGO_1, sitio.getTextoLargo1());
		valores.put(SitiosSQLite.COLUMNA_TEXTO_LARGO_2, sitio.getTextoLargo2());
		valores.put(SitiosSQLite.COLUMNA_NOMBRE_LOGOTIPO, sitio.getNombreLogotipo());
		valores.put(SitiosSQLite.COLUMNA_NOMBRE_IMAGEN_1, sitio.getNombreImagen1());
		valores.put(SitiosSQLite.COLUMNA_NOMBRE_IMAGEN_2, sitio.getNombreImagen2());
		valores.put(SitiosSQLite.COLUMNA_NOMBRE_IMAGEN_3, sitio.getNombreImagen3());
		valores.put(SitiosSQLite.COLUMNA_NOMBRE_IMAGEN_4, sitio.getNombreImagen4());
		valores.put(SitiosSQLite.COLUMNA_LATITUD, sitio.getLatitud());
		valores.put(SitiosSQLite.COLUMNA_LONGITUD, sitio.getLongitud());
		valores.put(SitiosSQLite.COLUMNA_DIRECCION, sitio.getDireccion());
		valores.put(SitiosSQLite.COLUMNA_TELEFONOS_FIJOS, sitio.getTelefonosFijos());
		valores.put(SitiosSQLite.COLUMNA_TELEFONOS_MOVILES, sitio.getTelefonosMoviles());
		valores.put(SitiosSQLite.COLUMNA_WEB, sitio.getWeb());
		valores.put(SitiosSQLite.COLUMNA_FACEBOOK, sitio.getFacebook());
		valores.put(SitiosSQLite.COLUMNA_TWITTER, sitio.getTwitter());
		valores.put(SitiosSQLite.COLUMNA_RANKING, sitio.getRanking());
		int favorito = ConversionesUtil.booleanToInt(sitio.isFavorito());
		valores.put(SitiosSQLite.COLUMNA_FAVORITO, favorito);
		int activo = ConversionesUtil.booleanToInt(sitio.isActivo());
		valores.put(SitiosSQLite.COLUMNA_ACTIVO, activo);
		valores.put(SitiosSQLite.COLUMNA_ULTIMA_ACTUALIZACION, sitio
				.getUltimaActualizacion().getTime());
		return valores;
	}

	public long insertar(Sitio sitio) {
		ContentValues valores = objectToContentValues(sitio);
		return database.insert(SitiosSQLite.TABLE_NAME, null, valores);

	}

	public long actualizar(Sitio sitio) {
		ContentValues valores = objectToContentValues(sitio);
		return database.update(SitiosSQLite.TABLE_NAME, valores,
				SitiosSQLite.COLUMNA_ID + "=" + sitio.getId(), null);
	}

	public void delete(Sitio sitio) {
		long id = sitio.getId();
		database.delete(SitiosSQLite.TABLE_NAME, SitiosSQLite.COLUMNA_ID
				+ " = " + id, null);
	}

	public Sitio getById(long id) {
		Sitio resul = null;
		String where = SitiosSQLite.COLUMNA_ID + " = " + id;
		Cursor cursor = database.query(SitiosSQLite.TABLE_NAME,
				allColumns, where, null, null, null, ORDER_BY_RANKING);
		cursor.moveToFirst();
	    if(!cursor.isAfterLast()) {
			resul = cursorToObject(cursor);
		}
		cursor.close();
		
		return resul;
	}

	/**
	 * Devuelve la lista de sitios cuyo estado de favoritos coincide con el indicado.
	 * @param nombreCat
	 * @return
	 */
	public List<Sitio> getByFavorito(int favorito) {
		List<Sitio> resul = new ArrayList<Sitio>();
		String where = SitiosSQLite.COLUMNA_FAVORITO + " = " + favorito;
		Cursor cursor = database.query(SitiosSQLite.TABLE_NAME,
				allColumns, where, null, null, null, ORDER_BY_RANKING);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Sitio sitio = cursorToObject(cursor);

			resul.add(sitio);
			cursor.moveToNext();
		}
		cursor.close();
		
		return resul;
	}

	/**
	 * Devuelve la lista de sitios cuya categoria se corresponde con la categoria indicada.
	 * @param nombreCat
	 * @return
	 */
	public List<Sitio> getByCategoria(String nombreCat) {
		List<Sitio> resul = new ArrayList<Sitio>();
		CategoriasDataSource catDataSource = new CategoriasDataSource(context);
		try {
			catDataSource.open();
			Categoria categoria = catDataSource.getByNombre(nombreCat);
	
			String where = SitiosSQLite.COLUMNA_ID_CATEGORIA + " = " + categoria.getId();
			Cursor cursor = database.query(SitiosSQLite.TABLE_NAME,
					allColumns, where, null, null, null, ORDER_BY_RANKING);
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Sitio sitio = cursorToObject(cursor);
	
				resul.add(sitio);
				cursor.moveToNext();
			}
			cursor.close();
			
			return resul;
		} finally {
			catDataSource.close();
		}

	}
	
	/**
	 * Devuelve la fecha de la ultima actualizacion de la tabla
	 * @param idPoblacion
	 * @return 
	 */
	public long getUltimaActualizacion() {
		String sql = "SELECT MAX(" + SitiosSQLite.COLUMNA_ULTIMA_ACTUALIZACION + ") FROM " +
					SitiosSQLite.TABLE_NAME;
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

	public List<Sitio> getAll() {
		List<Sitio> resul = new ArrayList<Sitio>();

		Cursor cursor = database.query(SitiosSQLite.TABLE_NAME,
				allColumns, null, null, null, null, ORDER_BY_RANKING);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Sitio sitio = cursorToObject(cursor);
			Log.i("BORRAR", "EL SITIO: " + sitio);

			resul.add(sitio);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return resul;
	}

	/**
	 * Convierte un cursor recibido de una consulta a la base de datos en un objeto Evento.
	 * @param cursor
	 * @return
	 */
	private Sitio cursorToObject(Cursor cursor) {
		Sitio resul = new Sitio();
		resul.setId(cursor.getLong(cursor.getColumnIndex(SitiosSQLite.COLUMNA_ID)));
		resul.setIdCategoria(cursor.getLong(cursor.getColumnIndex(SitiosSQLite.COLUMNA_ID_CATEGORIA)));
		resul.setNombre(cursor.getString(cursor.getColumnIndex(SitiosSQLite.COLUMNA_NOMBRE)));
		resul.setPoblacion(cursor.getString(cursor.getColumnIndex(SitiosSQLite.COLUMNA_POBLACION)));
		resul.setTextoCorto1(cursor.getString(cursor.getColumnIndex(SitiosSQLite.COLUMNA_TEXTO_CORTO_1)));
		resul.setTextoCorto2(cursor.getString(cursor.getColumnIndex(SitiosSQLite.COLUMNA_TEXTO_CORTO_2)));
		resul.setTextoCorto3(cursor.getString(cursor.getColumnIndex(SitiosSQLite.COLUMNA_TEXTO_CORTO_3)));
		resul.setTextoLargo1(cursor.getString(cursor.getColumnIndex(SitiosSQLite.COLUMNA_TEXTO_LARGO_1)));
		resul.setTextoLargo2(cursor.getString(cursor.getColumnIndex(SitiosSQLite.COLUMNA_TEXTO_LARGO_2)));
		resul.setNombreLogotipo(cursor.getString(cursor.getColumnIndex(SitiosSQLite.COLUMNA_NOMBRE_LOGOTIPO)));
		resul.setNombreImagen1(cursor.getString(cursor.getColumnIndex(SitiosSQLite.COLUMNA_NOMBRE_IMAGEN_1)));
		resul.setNombreImagen2(cursor.getString(cursor.getColumnIndex(SitiosSQLite.COLUMNA_NOMBRE_IMAGEN_2)));
		resul.setNombreImagen3(cursor.getString(cursor.getColumnIndex(SitiosSQLite.COLUMNA_NOMBRE_IMAGEN_3)));
		resul.setNombreImagen4(cursor.getString(cursor.getColumnIndex(SitiosSQLite.COLUMNA_NOMBRE_IMAGEN_4)));
		double latitud = cursor.getDouble(cursor.getColumnIndex(SitiosSQLite.COLUMNA_LATITUD));
		double longitud = cursor.getDouble(cursor.getColumnIndex(SitiosSQLite.COLUMNA_LONGITUD));
		resul.setLatitud(latitud);
		resul.setLongitud(longitud);
		resul.setDireccion(cursor.getString(cursor.getColumnIndex(SitiosSQLite.COLUMNA_DIRECCION)));
		resul.setTelefonosFijos(cursor.getString(cursor.getColumnIndex(SitiosSQLite.COLUMNA_TELEFONOS_FIJOS)));
		resul.setTelefonosMoviles(cursor.getString(cursor.getColumnIndex(SitiosSQLite.COLUMNA_TELEFONOS_MOVILES)));
		resul.setWeb(cursor.getString(cursor.getColumnIndex(SitiosSQLite.COLUMNA_WEB)));
		resul.setFacebook(cursor.getString(cursor.getColumnIndex(SitiosSQLite.COLUMNA_FACEBOOK)));
		resul.setTwitter(cursor.getString(cursor.getColumnIndex(SitiosSQLite.COLUMNA_TWITTER)));
		resul.setRanking(cursor.getInt(cursor.getColumnIndex(SitiosSQLite.COLUMNA_RANKING)));
		int intFavorito = cursor.getInt(cursor.getColumnIndex(SitiosSQLite.COLUMNA_FAVORITO));
		resul.setFavorito(ConversionesUtil.intToBoolean(intFavorito));
		int intActivo = cursor.getInt(cursor.getColumnIndex(SitiosSQLite.COLUMNA_ACTIVO));
		resul.setActivo(ConversionesUtil.intToBoolean(intActivo));
		long ultimaActualizacion = cursor.getColumnIndex(SitiosSQLite.COLUMNA_ULTIMA_ACTUALIZACION);
		resul.setUltimaActualizacion(new Date(ultimaActualizacion));
		
		return resul;
	}

}
