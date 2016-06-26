package com.primos.visitamoraleja.bdsqlite.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.primos.visitamoraleja.bdsqlite.ActividadEventoSQLite;
import com.primos.visitamoraleja.bdsqlite.SitiosSQLite;
import com.primos.visitamoraleja.contenidos.ActividadEvento;
import com.primos.visitamoraleja.util.ConversionesUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by h on 11/06/16.
 */
public class ActividadEventoDataSource extends AbstractDataSource {
    // Database fields
    private String[] allColumns = {ActividadEventoSQLite.COLUMNA_ID,
            ActividadEventoSQLite.COLUMNA_ID_EVENTO,
            ActividadEventoSQLite.COLUMNA_ID_CATEGORIA,
            ActividadEventoSQLite.COLUMNA_NOMBRE,
            ActividadEventoSQLite.COLUMNA_TEXTO,
            ActividadEventoSQLite.COLUMNA_DESCRIPCION,
            ActividadEventoSQLite.COLUMNA_NOMBRE_ICONO,
            ActividadEventoSQLite.COLUMNA_INICIO,
            ActividadEventoSQLite.COLUMNA_FIN,
            ActividadEventoSQLite.COLUMNA_LATITUD,
            ActividadEventoSQLite.COLUMNA_LONGITUD,
            ActividadEventoSQLite.COLUMNA_ACTIVO,
            ActividadEventoSQLite.COLUMNA_ULTIMA_ACTUALIZACION};

    public ActividadEventoDataSource(Context context) {
        super(context);
    }

    @Override
    protected SQLiteOpenHelper crearDbHelper(Context context) {
        return new ActividadEventoSQLite(context);
    }

    /**
     * Convierte un objeto evento en un objeto ContentValues que podra ser usado para insercion/actualizacion
     * de la base de datos.
     * @param actividadEvento
     * @return
     */
    private ContentValues objectToContentValues(ActividadEvento actividadEvento) {
        ContentValues valores = new ContentValues();
        valores.put(ActividadEventoSQLite.COLUMNA_ID, actividadEvento.getId());
        valores.put(ActividadEventoSQLite.COLUMNA_ID_EVENTO, actividadEvento.getIdEvento());
        valores.put(ActividadEventoSQLite.COLUMNA_ID_CATEGORIA, actividadEvento.getIdCategoriaEvento());
        valores.put(ActividadEventoSQLite.COLUMNA_NOMBRE, actividadEvento.getNombre());
        valores.put(ActividadEventoSQLite.COLUMNA_TEXTO, actividadEvento.getTexto());
        valores.put(ActividadEventoSQLite.COLUMNA_DESCRIPCION, actividadEvento.getDescripcion());
        valores.put(ActividadEventoSQLite.COLUMNA_NOMBRE_ICONO, actividadEvento.getNombreIcono());
        valores.put(ActividadEventoSQLite.COLUMNA_INICIO, actividadEvento
                .getInicio().getTime());
        valores.put(ActividadEventoSQLite.COLUMNA_FIN, actividadEvento
                .getFin().getTime());
        valores.put(ActividadEventoSQLite.COLUMNA_LATITUD, actividadEvento.getLatitud());
        valores.put(ActividadEventoSQLite.COLUMNA_LONGITUD, actividadEvento.getLongitud());
        int activo = ConversionesUtil.booleanToInt(actividadEvento.isActivo());
        valores.put(ActividadEventoSQLite.COLUMNA_ACTIVO, activo);
        valores.put(ActividadEventoSQLite.COLUMNA_ULTIMA_ACTUALIZACION, actividadEvento
                .getUltimaActualizacion().getTime());
        return valores;
    }

    public long insertar(ActividadEvento ActividadEvento) {
        ContentValues valores = objectToContentValues(ActividadEvento);
        return database.insert(ActividadEventoSQLite.TABLE_NAME, null, valores);

    }

    public long actualizar(ActividadEvento ActividadEvento) {
        ContentValues valores = objectToContentValues(ActividadEvento);
        return database.update(ActividadEventoSQLite.TABLE_NAME, valores,
                ActividadEventoSQLite.COLUMNA_ID + "=" + ActividadEvento.getId(), null);
    }

    public void delete(ActividadEvento ActividadEvento) {
        long id = ActividadEvento.getId();
        database.delete(ActividadEventoSQLite.TABLE_NAME, ActividadEventoSQLite.COLUMNA_ID
                + " = " + id, null);
    }

    public ActividadEvento getById(long id) {
        ActividadEvento resul = null;
        String where = ActividadEventoSQLite.COLUMNA_ID + " = " + id;
        Cursor cursor = database.query(ActividadEventoSQLite.TABLE_NAME,
                allColumns, where, null, null, null, null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast()) {
            resul = cursorToObject(cursor);
        }
        cursor.close();

        return resul;
    }

    public List<ActividadEvento> getByIdEvento(long idEvento) {
        List<ActividadEvento> resul = new ArrayList<>();
        String where = ActividadEventoSQLite.COLUMNA_ID_EVENTO + " = " + idEvento;
        Cursor cursor = database.query(ActividadEventoSQLite.TABLE_NAME,
                allColumns, where, null, null, null, null);
        resul = getListaByCursor(cursor);
        cursor.close();

        return resul;
    }

    private List<ActividadEvento> getListaByCursor(Cursor cursor) {
        List<ActividadEvento> resul = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ActividadEvento ActividadEvento = cursorToObject(cursor);
            Log.d("[EventosDataSource]", "Leyendo un evento del cursor: " + ActividadEvento);
            resul.add(ActividadEvento);
            cursor.moveToNext();
        }
        return resul;
    }

    /**
     * Devuelve la fecha de la ultima actualizacion de la tabla
     * @return
     */
    public long getUltimaActualizacion() {
        String sql = "SELECT MAX(" + ActividadEventoSQLite.COLUMNA_ULTIMA_ACTUALIZACION + ") FROM " +
                ActividadEventoSQLite.TABLE_NAME;
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

    public List<ActividadEvento> getAll() {
        List<ActividadEvento> resul = new ArrayList<ActividadEvento>();

        String seleccion = ActividadEventoSQLite.COLUMNA_ACTIVO + " = 1";
        Cursor cursor = database.query(ActividadEventoSQLite.TABLE_NAME,
                allColumns, seleccion, null, null, null, null);

        resul = getListaByCursor(cursor);
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
    private ActividadEvento cursorToObject(Cursor cursor) {
        ActividadEvento resul = new ActividadEvento();
        resul.setId(cursor.getLong(cursor.getColumnIndex(ActividadEventoSQLite.COLUMNA_ID)));
        resul.setIdEvento(cursor.getLong(cursor.getColumnIndex(ActividadEventoSQLite.COLUMNA_ID_EVENTO)));
        resul.setIdCategoriaEvento(cursor.getLong(cursor.getColumnIndex(ActividadEventoSQLite.COLUMNA_ID_CATEGORIA)));
        resul.setNombre(cursor.getString(cursor.getColumnIndex(ActividadEventoSQLite.COLUMNA_NOMBRE)));
        resul.setTexto(cursor.getString(cursor.getColumnIndex(ActividadEventoSQLite.COLUMNA_TEXTO)));
        resul.setDescripcion(cursor.getString(cursor.getColumnIndex(ActividadEventoSQLite.COLUMNA_DESCRIPCION)));
        resul.setNombreIcono(cursor.getString(cursor.getColumnIndex(ActividadEventoSQLite.COLUMNA_NOMBRE_ICONO)));
        long inicio = cursor.getLong(cursor.getColumnIndex(ActividadEventoSQLite.COLUMNA_INICIO));
        resul.setInicio(new Date(inicio));
        long fin = cursor.getLong(cursor.getColumnIndex(ActividadEventoSQLite.COLUMNA_FIN));
        resul.setFin(new Date(fin));
        long latitud = cursor.getLong(cursor.getColumnIndex(ActividadEventoSQLite.COLUMNA_LATITUD));
        long longitud = cursor.getLong(cursor.getColumnIndex(ActividadEventoSQLite.COLUMNA_LONGITUD));
        resul.setLatitud(latitud);
        resul.setLongitud(longitud);
        int intActivo = cursor.getInt(cursor.getColumnIndex(SitiosSQLite.COLUMNA_ACTIVO));
        resul.setActivo(ConversionesUtil.intToBoolean(intActivo));
        long ultimaActualizacion = cursor.getLong(cursor.getColumnIndex(ActividadEventoSQLite.COLUMNA_ULTIMA_ACTUALIZACION));
        resul.setUltimaActualizacion(new Date(ultimaActualizacion));

        return resul;
    }
}
