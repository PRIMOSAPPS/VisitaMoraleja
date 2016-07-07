package com.primos.visitamoraleja.bdsqlite.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.primos.visitamoraleja.bdsqlite.ImagenesActividadEventoSQLite;
import com.primos.visitamoraleja.bdsqlite.SitioEventoSQLite;
import com.primos.visitamoraleja.bdsqlite.SitiosSQLite;
import com.primos.visitamoraleja.contenidos.ImagenActividadEvento;
import com.primos.visitamoraleja.util.ConversionesUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by h on 11/06/16.
 */
public class ImagenesActividadEventoDatasource extends AbstractDataSource {
    // Database fields
    private String[] allColumns = {ImagenesActividadEventoSQLite.COLUMNA_ID,
            ImagenesActividadEventoSQLite.COLUMNA_ID_ACTIVIDADO,
            ImagenesActividadEventoSQLite.COLUMNA_NOMBRE,
            ImagenesActividadEventoSQLite.COLUMNA_ACTIVO,
            ImagenesActividadEventoSQLite.COLUMNA_ULTIMA_ACTUALIZACION};

    public ImagenesActividadEventoDatasource(Context context) {
        super(context);
    }

    @Override
    protected SQLiteOpenHelper crearDbHelper(Context context) {
        return new ImagenesActividadEventoSQLite(context);
    }

    /**
     * Convierte un objeto evento en un objeto ContentValues que podra ser usado para insercion/actualizacion
     * de la base de datos.
     * @param objeto
     * @return
     */
    private ContentValues objectToContentValues(ImagenActividadEvento objeto) {
        ContentValues valores = new ContentValues();
        valores.put(ImagenesActividadEventoSQLite.COLUMNA_ID, objeto.getId());
        valores.put(ImagenesActividadEventoSQLite.COLUMNA_ID_ACTIVIDADO, objeto.getIdActividad());
        valores.put(ImagenesActividadEventoSQLite.COLUMNA_NOMBRE, objeto.getNombre());
        int activo = ConversionesUtil.booleanToInt(objeto.isActivo());
        valores.put(ImagenesActividadEventoSQLite.COLUMNA_ACTIVO, activo);
        valores.put(ImagenesActividadEventoSQLite.COLUMNA_ULTIMA_ACTUALIZACION, objeto
                .getUltimaActualizacion().getTime());
        return valores;
    }

    public long insertar(ImagenActividadEvento objeto) {
        ContentValues valores = objectToContentValues(objeto);
        return database.insert(ImagenesActividadEventoSQLite.TABLE_NAME, null, valores);

    }

    public long actualizar(ImagenActividadEvento objeto) {
        ContentValues valores = objectToContentValues(objeto);
        return database.update(ImagenesActividadEventoSQLite.TABLE_NAME, valores,
                ImagenesActividadEventoSQLite.COLUMNA_ID + "=" + objeto.getId(), null);
    }

    public void delete(ImagenActividadEvento objeto) {
        long id = objeto.getId();
        database.delete(ImagenesActividadEventoSQLite.TABLE_NAME, ImagenesActividadEventoSQLite.COLUMNA_ID
                + " = " + id, null);
    }

    public void deleteByIdActividad(long idActividad) {
        database.delete(ImagenesActividadEventoSQLite.TABLE_NAME, ImagenesActividadEventoSQLite.COLUMNA_ID_ACTIVIDADO
                + " = " + idActividad, null);
    }

    public ImagenActividadEvento getById(long id) {
        ImagenActividadEvento resul = null;
        String where = ImagenesActividadEventoSQLite.COLUMNA_ID + " = " + id;
        Cursor cursor = database.query(ImagenesActividadEventoSQLite.TABLE_NAME,
                allColumns, where, null, null, null, null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast()) {
            resul = cursorToObject(cursor);
        }
        cursor.close();

        return resul;
    }

    public List<ImagenActividadEvento> getByIdActividad(long idActividad) {
        List<ImagenActividadEvento> resul = null;
        String where = ImagenesActividadEventoSQLite.COLUMNA_ID_ACTIVIDADO + " = " + idActividad;
        Cursor cursor = database.query(ImagenesActividadEventoSQLite.TABLE_NAME,
                allColumns, where, null, null, null, null);
        resul = getListaByCursor(cursor);
        cursor.close();

        return resul;
    }

    private List<ImagenActividadEvento> getListaByCursor(Cursor cursor) {
        List<ImagenActividadEvento> resul = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ImagenActividadEvento objeto = cursorToObject(cursor);
            Log.d("[EventosDataSource]", "Leyendo un evento del cursor: " + objeto);
            resul.add(objeto);
            cursor.moveToNext();
        }
        return resul;
    }

    /**
     * Devuelve la fecha de la ultima actualizacion de la tabla
     * @return
     */
    public long getUltimaActualizacion() {
        String sql = "SELECT MAX(" + ImagenesActividadEventoSQLite.COLUMNA_ULTIMA_ACTUALIZACION + ") FROM " +
                ImagenesActividadEventoSQLite.TABLE_NAME;
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

    public List<ImagenActividadEvento> getAll() {
        List<ImagenActividadEvento> resul = new ArrayList<ImagenActividadEvento>();

        String seleccion = ImagenesActividadEventoSQLite.COLUMNA_ACTIVO + " = 1";
        Cursor cursor = database.query(ImagenesActividadEventoSQLite.TABLE_NAME,
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
    private ImagenActividadEvento cursorToObject(Cursor cursor) {
        ImagenActividadEvento resul = new ImagenActividadEvento();
        resul.setId(cursor.getLong(cursor.getColumnIndex(ImagenesActividadEventoSQLite.COLUMNA_ID)));
        resul.setIdActividad(cursor.getLong(cursor.getColumnIndex(ImagenesActividadEventoSQLite.COLUMNA_ID_ACTIVIDADO)));
        resul.setNombre(cursor.getString(cursor.getColumnIndex(ImagenesActividadEventoSQLite.COLUMNA_NOMBRE)));
        int intActivo = cursor.getInt(cursor.getColumnIndex(SitiosSQLite.COLUMNA_ACTIVO));
        resul.setActivo(ConversionesUtil.intToBoolean(intActivo));
        long ultimaActualizacion = cursor.getLong(cursor.getColumnIndex(ImagenesActividadEventoSQLite.COLUMNA_ULTIMA_ACTUALIZACION));
        resul.setUltimaActualizacion(new Date(ultimaActualizacion));

        return resul;
    }

}
