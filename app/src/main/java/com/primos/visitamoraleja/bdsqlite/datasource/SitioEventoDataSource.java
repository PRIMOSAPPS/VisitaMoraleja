package com.primos.visitamoraleja.bdsqlite.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.primos.visitamoraleja.bdsqlite.ImagenesEventoSQLite;
import com.primos.visitamoraleja.bdsqlite.SitioEventoSQLite;
import com.primos.visitamoraleja.bdsqlite.SitiosSQLite;
import com.primos.visitamoraleja.contenidos.SitioEvento;
import com.primos.visitamoraleja.util.ConversionesUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by h on 11/06/16.
 */
public class SitioEventoDataSource extends AbstractDataSource {
    // Database fields
    private String[] allColumns = {SitioEventoSQLite.COLUMNA_ID,
            SitioEventoSQLite.COLUMNA_ID_EVENTO,
            SitioEventoSQLite.COLUMNA_ID_CATEGORIA,
            SitioEventoSQLite.COLUMNA_ES_SITIO_REGISTRADO,
            SitioEventoSQLite.COLUMNA_ID_SITIO_REGISTRADO,
            SitioEventoSQLite.COLUMNA_NOMBRE,
            SitioEventoSQLite.COLUMNA_TEXTO,
            SitioEventoSQLite.COLUMNA_DESCRIPCION,
            SitioEventoSQLite.COLUMNA_NOMBRE_ICONO,
            SitioEventoSQLite.COLUMNA_LATITUD,
            SitioEventoSQLite.COLUMNA_LONGITUD,
            SitioEventoSQLite.COLUMNA_ACTIVO,
            SitioEventoSQLite.COLUMNA_ULTIMA_ACTUALIZACION};

    public SitioEventoDataSource(Context context) {
        super(context);
    }

    @Override
    protected SQLiteOpenHelper crearDbHelper(Context context) {
        return new SitioEventoSQLite(context);
    }

    /**
     * Convierte un objeto evento en un objeto ContentValues que podra ser usado para insercion/actualizacion
     * de la base de datos.
     * @param sitioEvento
     * @return
     */
    private ContentValues objectToContentValues(SitioEvento sitioEvento) {
        ContentValues valores = new ContentValues();
        valores.put(SitioEventoSQLite.COLUMNA_ID, sitioEvento.getId());
        valores.put(SitioEventoSQLite.COLUMNA_ID_EVENTO, sitioEvento.getIdEvento());
        valores.put(SitioEventoSQLite.COLUMNA_ID_CATEGORIA, sitioEvento.getIdCategoriaEvento());
        int esSitioRegistrado = ConversionesUtil.booleanToInt(sitioEvento.isEsSitioRegistrado());
        valores.put(SitioEventoSQLite.COLUMNA_ES_SITIO_REGISTRADO, esSitioRegistrado);
        valores.put(SitioEventoSQLite.COLUMNA_ID_SITIO_REGISTRADO, sitioEvento.getIdSitioRegistrado());
        valores.put(SitioEventoSQLite.COLUMNA_NOMBRE, sitioEvento.getNombre());
        valores.put(SitioEventoSQLite.COLUMNA_TEXTO, sitioEvento.getTexto());
        valores.put(SitioEventoSQLite.COLUMNA_DESCRIPCION, sitioEvento.getDescripcion());
        valores.put(SitioEventoSQLite.COLUMNA_NOMBRE_ICONO, sitioEvento.getNombreIcono());
        valores.put(SitioEventoSQLite.COLUMNA_LATITUD, sitioEvento.getLatitud());
        valores.put(SitioEventoSQLite.COLUMNA_LONGITUD, sitioEvento.getLongitud());
        int activo = ConversionesUtil.booleanToInt(sitioEvento.isActivo());
        valores.put(SitioEventoSQLite.COLUMNA_ACTIVO, activo);
        valores.put(SitioEventoSQLite.COLUMNA_ULTIMA_ACTUALIZACION, sitioEvento
                .getUltimaActualizacion().getTime());
        return valores;
    }

    public long insertar(SitioEvento sitioEvento) {
        ContentValues valores = objectToContentValues(sitioEvento);
        return database.insert(SitioEventoSQLite.TABLE_NAME, null, valores);

    }

    public long actualizar(SitioEvento sitioEvento) {
        ContentValues valores = objectToContentValues(sitioEvento);
        return database.update(SitioEventoSQLite.TABLE_NAME, valores,
                SitioEventoSQLite.COLUMNA_ID + "=" + sitioEvento.getId(), null);
    }

    public void delete(SitioEvento sitioEvento) {
        long id = sitioEvento.getId();
        database.delete(SitioEventoSQLite.TABLE_NAME, SitioEventoSQLite.COLUMNA_ID
                + " = " + id, null);
    }

    public void deleteByIdEvento(long idEvento) {
        database.delete(SitioEventoSQLite.TABLE_NAME, SitioEventoSQLite.COLUMNA_ID_EVENTO
                + " = " + idEvento, null);
    }

    public SitioEvento getById(long id) {
        SitioEvento resul = null;
        String where = SitioEventoSQLite.COLUMNA_ID + " = " + id;
        Cursor cursor = database.query(SitioEventoSQLite.TABLE_NAME,
                allColumns, where, null, null, null, null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast()) {
            resul = cursorToObject(cursor);
        }
        cursor.close();

        return resul;
    }

    public List<SitioEvento> getByIdEvento(long idEvento) {
        String where = SitioEventoSQLite.COLUMNA_ID_EVENTO + " = " + idEvento;
        Cursor cursor = database.query(SitioEventoSQLite.TABLE_NAME,
                allColumns, where, null, null, null, null);
        List<SitioEvento> resul = getListaByCursor(cursor);
        cursor.close();

        return resul;
    }

    private List<SitioEvento> getListaByCursor(Cursor cursor) {
        List<SitioEvento> resul = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            SitioEvento sitioEvento = cursorToObject(cursor);
            Log.d("[EventosDataSource]", "Leyendo un evento del cursor: " + sitioEvento);
            resul.add(sitioEvento);
            cursor.moveToNext();
        }
        return resul;
    }

    /**
     * Devuelve la fecha de la ultima actualizacion de la tabla
     * @return
     */
    public long getUltimaActualizacion() {
        String sql = "SELECT MAX(" + SitioEventoSQLite.COLUMNA_ULTIMA_ACTUALIZACION + ") FROM " +
                SitioEventoSQLite.TABLE_NAME;
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

    public List<SitioEvento> getAll() {
        List<SitioEvento> resul = new ArrayList<SitioEvento>();

        String seleccion = SitioEventoSQLite.COLUMNA_ACTIVO + " = 1";
        Cursor cursor = database.query(SitioEventoSQLite.TABLE_NAME,
                allColumns, seleccion, null, null, null, null);

        resul = getListaByCursor(cursor);
        // make sure to close the cursor
        cursor.close();
        return resul;
    }

    /**
     * Convierte un cursor recibido de una consulta a la base de datos en un objeto Evento.
     * @param cursor
     * @return
     */
    private SitioEvento cursorToObject(Cursor cursor) {
        SitioEvento resul = new SitioEvento();
        resul.setId(cursor.getLong(cursor.getColumnIndex(SitioEventoSQLite.COLUMNA_ID)));
        resul.setIdEvento(cursor.getLong(cursor.getColumnIndex(SitioEventoSQLite.COLUMNA_ID_EVENTO)));
        resul.setIdCategoriaEvento(cursor.getLong(cursor.getColumnIndex(SitioEventoSQLite.COLUMNA_ID_CATEGORIA)));
        int esSitioRegistrado = cursor.getInt(cursor.getColumnIndex(SitioEventoSQLite.COLUMNA_ES_SITIO_REGISTRADO));
        resul.setEsSitioRegistrado(ConversionesUtil.intToBoolean(esSitioRegistrado));
        resul.setIdSitioRegistrado(cursor.getLong(cursor.getColumnIndex(SitioEventoSQLite.COLUMNA_ID_SITIO_REGISTRADO)));
        resul.setNombre(cursor.getString(cursor.getColumnIndex(SitioEventoSQLite.COLUMNA_NOMBRE)));
        resul.setTexto(cursor.getString(cursor.getColumnIndex(SitioEventoSQLite.COLUMNA_TEXTO)));
        resul.setDescripcion(cursor.getString(cursor.getColumnIndex(SitioEventoSQLite.COLUMNA_DESCRIPCION)));
        resul.setNombreIcono(cursor.getString(cursor.getColumnIndex(SitioEventoSQLite.COLUMNA_NOMBRE_ICONO)));
        double latitud = cursor.getDouble(cursor.getColumnIndex(SitioEventoSQLite.COLUMNA_LATITUD));
        double longitud = cursor.getDouble(cursor.getColumnIndex(SitioEventoSQLite.COLUMNA_LONGITUD));
        resul.setLatitud(latitud);
        resul.setLongitud(longitud);
        int intActivo = cursor.getInt(cursor.getColumnIndex(SitiosSQLite.COLUMNA_ACTIVO));
        resul.setActivo(ConversionesUtil.intToBoolean(intActivo));
        long ultimaActualizacion = cursor.getLong(cursor.getColumnIndex(SitioEventoSQLite.COLUMNA_ULTIMA_ACTUALIZACION));
        resul.setUltimaActualizacion(new Date(ultimaActualizacion));

        return resul;
    }
}
