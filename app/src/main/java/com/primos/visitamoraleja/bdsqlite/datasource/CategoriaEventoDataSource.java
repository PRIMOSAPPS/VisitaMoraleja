package com.primos.visitamoraleja.bdsqlite.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.primos.visitamoraleja.bdsqlite.CategoriaEventoSQLite;
import com.primos.visitamoraleja.bdsqlite.FormaEventoSQLite;
import com.primos.visitamoraleja.bdsqlite.SitiosSQLite;
import com.primos.visitamoraleja.contenidos.CategoriaEvento;
import com.primos.visitamoraleja.util.ConversionesUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by h on 11/06/16.
 */
public class CategoriaEventoDataSource extends AbstractDataSource {
    // Database fields
    private String[] allColumns = {CategoriaEventoSQLite.COLUMNA_ID,
            CategoriaEventoSQLite.COLUMNA_ID_EVENTO,
            CategoriaEventoSQLite.COLUMNA_NOMBRE,
            CategoriaEventoSQLite.COLUMNA_TEXTO,
            CategoriaEventoSQLite.COLUMNA_NOMBRE_ICONO,
            CategoriaEventoSQLite.COLUMNA_ACTIVO,
            CategoriaEventoSQLite.COLUMNA_ULTIMA_ACTUALIZACION};

    public CategoriaEventoDataSource(Context context) {
        super(context);
    }

    @Override
    protected SQLiteOpenHelper crearDbHelper(Context context) {
        return new CategoriaEventoSQLite(context);
    }

    /**
     * Convierte un objeto evento en un objeto ContentValues que podra ser usado para insercion/actualizacion
     * de la base de datos.
     * @param categoriaEvento
     * @return
     */
    private ContentValues objectToContentValues(CategoriaEvento categoriaEvento) {
        ContentValues valores = new ContentValues();
        valores.put(CategoriaEventoSQLite.COLUMNA_ID, categoriaEvento.getId());
        valores.put(CategoriaEventoSQLite.COLUMNA_ID_EVENTO, categoriaEvento.getIdEvento());
        valores.put(CategoriaEventoSQLite.COLUMNA_NOMBRE, categoriaEvento.getNombre());
        valores.put(CategoriaEventoSQLite.COLUMNA_TEXTO, categoriaEvento.getTexto());
        valores.put(CategoriaEventoSQLite.COLUMNA_NOMBRE_ICONO, categoriaEvento.getNombreIcono());
        int activo = ConversionesUtil.booleanToInt(categoriaEvento.isActivo());
        valores.put(CategoriaEventoSQLite.COLUMNA_ACTIVO, activo);
        valores.put(CategoriaEventoSQLite.COLUMNA_ULTIMA_ACTUALIZACION, categoriaEvento
                .getUltimaActualizacion().getTime());
        return valores;
    }

    public long insertar(CategoriaEvento CategoriaEvento) {
        ContentValues valores = objectToContentValues(CategoriaEvento);
        return database.insert(CategoriaEventoSQLite.TABLE_NAME, null, valores);

    }

    public long actualizar(CategoriaEvento CategoriaEvento) {
        ContentValues valores = objectToContentValues(CategoriaEvento);
        return database.update(CategoriaEventoSQLite.TABLE_NAME, valores,
                CategoriaEventoSQLite.COLUMNA_ID + "=" + CategoriaEvento.getId(), null);
    }

    public void delete(CategoriaEvento CategoriaEvento) {
        long id = CategoriaEvento.getId();
        database.delete(CategoriaEventoSQLite.TABLE_NAME, CategoriaEventoSQLite.COLUMNA_ID
                + " = " + id, null);
    }

    public void deleteByIdEvento(long idEvento) {
        database.delete(CategoriaEventoSQLite.TABLE_NAME, CategoriaEventoSQLite.COLUMNA_ID_EVENTO
                + " = " + idEvento, null);
    }

    public CategoriaEvento getById(long id) {
        CategoriaEvento resul = null;
        String where = CategoriaEventoSQLite.COLUMNA_ID + " = " + id;
        Cursor cursor = database.query(CategoriaEventoSQLite.TABLE_NAME,
                allColumns, where, null, null, null, null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast()) {
            resul = cursorToObject(cursor);
        }
        cursor.close();

        return resul;
    }

    public List<CategoriaEvento> getByIdEvento(long idEvento) {
        List<CategoriaEvento> resul = new ArrayList<>();
        String where = CategoriaEventoSQLite.COLUMNA_ID_EVENTO + " = " + idEvento;
        Cursor cursor = database.query(CategoriaEventoSQLite.TABLE_NAME,
                allColumns, where, null, null, null, null);
        resul = getListaByCursor(cursor);
        cursor.close();

        return resul;
    }

    private List<CategoriaEvento> getListaByCursor(Cursor cursor) {
        List<CategoriaEvento> resul = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            CategoriaEvento CategoriaEvento = cursorToObject(cursor);
            Log.d("[EventosDataSource]", "Leyendo un evento del cursor: " + CategoriaEvento);
            resul.add(CategoriaEvento);
            cursor.moveToNext();
        }
        return resul;
    }

    /**
     * Devuelve la fecha de la ultima actualizacion de la tabla
     * @return
     */
    public long getUltimaActualizacion() {
        String sql = "SELECT MAX(" + CategoriaEventoSQLite.COLUMNA_ULTIMA_ACTUALIZACION + ") FROM " +
                CategoriaEventoSQLite.TABLE_NAME;
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

    public List<CategoriaEvento> getAll() {
        List<CategoriaEvento> resul = new ArrayList<CategoriaEvento>();

        String seleccion = CategoriaEventoSQLite.COLUMNA_ACTIVO + " = 1";
        Cursor cursor = database.query(CategoriaEventoSQLite.TABLE_NAME,
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
    private CategoriaEvento cursorToObject(Cursor cursor) {
        CategoriaEvento resul = new CategoriaEvento();
        resul.setId(cursor.getLong(cursor.getColumnIndex(CategoriaEventoSQLite.COLUMNA_ID)));
        resul.setIdEvento(cursor.getLong(cursor.getColumnIndex(CategoriaEventoSQLite.COLUMNA_ID_EVENTO)));
        resul.setNombre(cursor.getString(cursor.getColumnIndex(CategoriaEventoSQLite.COLUMNA_NOMBRE)));
        resul.setTexto(cursor.getString(cursor.getColumnIndex(CategoriaEventoSQLite.COLUMNA_TEXTO)));
        resul.setNombreIcono(cursor.getString(cursor.getColumnIndex(CategoriaEventoSQLite.COLUMNA_NOMBRE_ICONO)));
        int intActivo = cursor.getInt(cursor.getColumnIndex(SitiosSQLite.COLUMNA_ACTIVO));
        resul.setActivo(ConversionesUtil.intToBoolean(intActivo));
        long ultimaActualizacion = cursor.getLong(cursor.getColumnIndex(CategoriaEventoSQLite.COLUMNA_ULTIMA_ACTUALIZACION));
        resul.setUltimaActualizacion(new Date(ultimaActualizacion));

        return resul;
    }
}
