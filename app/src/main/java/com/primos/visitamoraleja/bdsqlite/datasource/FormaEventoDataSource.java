package com.primos.visitamoraleja.bdsqlite.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.primos.visitamoraleja.bdsqlite.FormaEventoSQLite;
import com.primos.visitamoraleja.bdsqlite.SitiosSQLite;
import com.primos.visitamoraleja.contenidos.FormaEvento;
import com.primos.visitamoraleja.util.ConversionesUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by h on 11/06/16.
 */
public class FormaEventoDataSource extends AbstractDataSource {
    // Database fields
    private String[] allColumns = {FormaEventoSQLite.COLUMNA_ID,
            FormaEventoSQLite.COLUMNA_ID_EVENTO,
            FormaEventoSQLite.COLUMNA_TIPO_FORMA,
            FormaEventoSQLite.COLUMNA_COLOR_RELLENO,
            FormaEventoSQLite.COLUMNA_COLOR_LINEA,
            FormaEventoSQLite.COLUMNA_GROSOR_LINEA,
            FormaEventoSQLite.COLUMNA_TEXTO,
            FormaEventoSQLite.COLUMNA_COORDENADAS,
            FormaEventoSQLite.COLUMNA_ACTIVO,
            FormaEventoSQLite.COLUMNA_ULTIMA_ACTUALIZACION};

    public FormaEventoDataSource(Context context) {
        super(context);
    }

    @Override
    protected SQLiteOpenHelper crearDbHelper(Context context) {
        return new FormaEventoSQLite(context);
    }

    /**
     * Convierte un objeto evento en un objeto ContentValues que podra ser usado para insercion/actualizacion
     * de la base de datos.
     * @param formaEvento
     * @return
     */
    private ContentValues objectToContentValues(FormaEvento formaEvento) {
        ContentValues valores = new ContentValues();
        valores.put(FormaEventoSQLite.COLUMNA_ID, formaEvento.getId());
        valores.put(FormaEventoSQLite.COLUMNA_ID_EVENTO, formaEvento.getIdEvento());
        valores.put(FormaEventoSQLite.COLUMNA_ID_CATEGORIA, formaEvento.getIdCategoriaEvento());
        valores.put(FormaEventoSQLite.COLUMNA_TIPO_FORMA, formaEvento.getTipoForma());
        valores.put(FormaEventoSQLite.COLUMNA_COLOR_RELLENO, formaEvento.getColorRelleno());
        valores.put(FormaEventoSQLite.COLUMNA_COLOR_LINEA, formaEvento.getColorLinea());
        valores.put(FormaEventoSQLite.COLUMNA_GROSOR_LINEA, formaEvento.getGrosorLinea());
        valores.put(FormaEventoSQLite.COLUMNA_TEXTO, formaEvento.getTexto());
        valores.put(FormaEventoSQLite.COLUMNA_COORDENADAS, formaEvento.getCoordenadas());
        int activo = ConversionesUtil.booleanToInt(formaEvento.isActivo());
        valores.put(FormaEventoSQLite.COLUMNA_ACTIVO, activo);
        valores.put(FormaEventoSQLite.COLUMNA_ULTIMA_ACTUALIZACION, formaEvento
                .getUltimaActualizacion().getTime());
        return valores;
    }

    public long insertar(FormaEvento formaEvento) {
        ContentValues valores = objectToContentValues(formaEvento);
        return database.insert(FormaEventoSQLite.TABLE_NAME, null, valores);

    }

    public long actualizar(FormaEvento formaEvento) {
        ContentValues valores = objectToContentValues(formaEvento);
        return database.update(FormaEventoSQLite.TABLE_NAME, valores,
                FormaEventoSQLite.COLUMNA_ID + "=" + formaEvento.getId(), null);
    }

    public void delete(FormaEvento formaEvento) {
        long id = formaEvento.getId();
        database.delete(FormaEventoSQLite.TABLE_NAME, FormaEventoSQLite.COLUMNA_ID
                + " = " + id, null);
    }

    public List<FormaEvento> getByIdEvento(long idEvento) {
        List<FormaEvento> resul = new ArrayList<>();
        String where = FormaEventoSQLite.COLUMNA_ID_EVENTO + " = " + idEvento;
        Cursor cursor = database.query(FormaEventoSQLite.TABLE_NAME,
                allColumns, where, null, null, null, null);
        resul = getListaByCursor(cursor);
        cursor.close();

        return resul;
    }

    public FormaEvento getById(long id) {
        FormaEvento resul = null;
        String where = FormaEventoSQLite.COLUMNA_ID + " = " + id;
        Cursor cursor = database.query(FormaEventoSQLite.TABLE_NAME,
                allColumns, where, null, null, null, null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast()) {
            resul = cursorToObject(cursor);
        }
        cursor.close();

        return resul;
    }

    private List<FormaEvento> getListaByCursor(Cursor cursor) {
        List<FormaEvento> resul = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            FormaEvento formaEvento = cursorToObject(cursor);
            Log.d("[EventosDataSource]", "Leyendo un evento del cursor: " + formaEvento);
            resul.add(formaEvento);
            cursor.moveToNext();
        }
        return resul;
    }

    /**
     * Devuelve la fecha de la ultima actualizacion de la tabla
     * @return
     */
    public long getUltimaActualizacion() {
        String sql = "SELECT MAX(" + FormaEventoSQLite.COLUMNA_ULTIMA_ACTUALIZACION + ") FROM " +
                FormaEventoSQLite.TABLE_NAME;
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

    public List<FormaEvento> getAll() {
        List<FormaEvento> resul = new ArrayList<FormaEvento>();

        String seleccion = FormaEventoSQLite.COLUMNA_ACTIVO + " = 1";
        Cursor cursor = database.query(FormaEventoSQLite.TABLE_NAME,
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
    private FormaEvento cursorToObject(Cursor cursor) {
        FormaEvento resul = new FormaEvento();
        resul.setId(cursor.getLong(cursor.getColumnIndex(FormaEventoSQLite.COLUMNA_ID)));
        resul.setIdEvento(cursor.getLong(cursor.getColumnIndex(FormaEventoSQLite.COLUMNA_ID_EVENTO)));
        resul.setIdCategoriaEvento(cursor.getLong(cursor.getColumnIndex(FormaEventoSQLite.COLUMNA_ID_CATEGORIA)));

        resul.setTipoForma(cursor.getString(cursor.getColumnIndex(FormaEventoSQLite.COLUMNA_TIPO_FORMA)));
        resul.setColorRelleno(cursor.getString(cursor.getColumnIndex(FormaEventoSQLite.COLUMNA_COLOR_RELLENO)));
        resul.setColorLinea(cursor.getString(cursor.getColumnIndex(FormaEventoSQLite.COLUMNA_COLOR_LINEA)));
        resul.setGrosorLinea(cursor.getString(cursor.getColumnIndex(FormaEventoSQLite.COLUMNA_GROSOR_LINEA)));
        resul.setTexto(cursor.getString(cursor.getColumnIndex(FormaEventoSQLite.COLUMNA_TEXTO)));
        resul.setCoordenadas(cursor.getString(cursor.getColumnIndex(FormaEventoSQLite.COLUMNA_COORDENADAS)));
        int intActivo = cursor.getInt(cursor.getColumnIndex(SitiosSQLite.COLUMNA_ACTIVO));
        resul.setActivo(ConversionesUtil.intToBoolean(intActivo));
        long ultimaActualizacion = cursor.getLong(cursor.getColumnIndex(FormaEventoSQLite.COLUMNA_ULTIMA_ACTUALIZACION));
        resul.setUltimaActualizacion(new Date(ultimaActualizacion));

        return resul;
    }
}
