package com.primos.visitamoraleja.bdsqlite.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.primos.visitamoraleja.bdsqlite.ImagenesEventoSQLite;
import com.primos.visitamoraleja.bdsqlite.SitiosSQLite;
import com.primos.visitamoraleja.contenidos.ImagenEvento;
import com.primos.visitamoraleja.util.ConversionesUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by h on 11/06/16.
 */
public class ImagenesEventoDatasource extends AbstractDataSource {
    // Database fields
    private String[] allColumns = {ImagenesEventoSQLite.COLUMNA_ID,
            ImagenesEventoSQLite.COLUMNA_ID_EVENTO,
            ImagenesEventoSQLite.COLUMNA_NOMBRE,
            ImagenesEventoSQLite.COLUMNA_ACTIVO,
            ImagenesEventoSQLite.COLUMNA_ULTIMA_ACTUALIZACION};

    public ImagenesEventoDatasource(Context context) {
        super(context);
    }

    @Override
    protected SQLiteOpenHelper crearDbHelper(Context context) {
        return new ImagenesEventoSQLite(context);
    }

    /**
     * Convierte un objeto evento en un objeto ContentValues que podra ser usado para insercion/actualizacion
     * de la base de datos.
     * @param objeto
     * @return
     */
    private ContentValues objectToContentValues(ImagenEvento objeto) {
        ContentValues valores = new ContentValues();
        valores.put(ImagenesEventoSQLite.COLUMNA_ID, objeto.getId());
        valores.put(ImagenesEventoSQLite.COLUMNA_ID_EVENTO, objeto.getIdEvento());
        valores.put(ImagenesEventoSQLite.COLUMNA_NOMBRE, objeto.getNombre());
        int activo = ConversionesUtil.booleanToInt(objeto.isActivo());
        valores.put(ImagenesEventoSQLite.COLUMNA_ACTIVO, activo);
        valores.put(ImagenesEventoSQLite.COLUMNA_ULTIMA_ACTUALIZACION, objeto
                .getUltimaActualizacion().getTime());
        return valores;
    }

    public long insertar(ImagenEvento objeto) {
        ContentValues valores = objectToContentValues(objeto);
        return database.insert(ImagenesEventoSQLite.TABLE_NAME, null, valores);

    }

    public long actualizar(ImagenEvento objeto) {
        ContentValues valores = objectToContentValues(objeto);
        return database.update(ImagenesEventoSQLite.TABLE_NAME, valores,
                ImagenesEventoSQLite.COLUMNA_ID + "=" + objeto.getId(), null);
    }

    public void delete(ImagenEvento objeto) {
        long id = objeto.getId();
        database.delete(ImagenesEventoSQLite.TABLE_NAME, ImagenesEventoSQLite.COLUMNA_ID
                + " = " + id, null);
    }

    public ImagenEvento getById(long id) {
        ImagenEvento resul = null;
        String where = ImagenesEventoSQLite.COLUMNA_ID + " = " + id;
        Cursor cursor = database.query(ImagenesEventoSQLite.TABLE_NAME,
                allColumns, where, null, null, null, null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast()) {
            resul = cursorToObject(cursor);
        }
        cursor.close();

        return resul;
    }

    public List<ImagenEvento> getByIdEvento(long idEvento) {
        List<ImagenEvento> resul = null;
        String where = ImagenesEventoSQLite.COLUMNA_ID_EVENTO + " = " + idEvento;
        Cursor cursor = database.query(ImagenesEventoSQLite.TABLE_NAME,
                allColumns, where, null, null, null, null);
        resul = getListaByCursor(cursor);
        cursor.close();

        return resul;
    }

    private List<ImagenEvento> getListaByCursor(Cursor cursor) {
        List<ImagenEvento> resul = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ImagenEvento objeto = cursorToObject(cursor);
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
        String sql = "SELECT MAX(" + ImagenesEventoSQLite.COLUMNA_ULTIMA_ACTUALIZACION + ") FROM " +
                ImagenesEventoSQLite.TABLE_NAME;
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

    public List<ImagenEvento> getAll() {
        List<ImagenEvento> resul = new ArrayList<ImagenEvento>();

        String seleccion = ImagenesEventoSQLite.COLUMNA_ACTIVO + " = 1";
        Cursor cursor = database.query(ImagenesEventoSQLite.TABLE_NAME,
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
    private ImagenEvento cursorToObject(Cursor cursor) {
        ImagenEvento resul = new ImagenEvento();
        resul.setId(cursor.getLong(cursor.getColumnIndex(ImagenesEventoSQLite.COLUMNA_ID)));
        resul.setIdEvento(cursor.getLong(cursor.getColumnIndex(ImagenesEventoSQLite.COLUMNA_ID_EVENTO)));
        resul.setNombre(cursor.getString(cursor.getColumnIndex(ImagenesEventoSQLite.COLUMNA_NOMBRE)));
        int intActivo = cursor.getInt(cursor.getColumnIndex(SitiosSQLite.COLUMNA_ACTIVO));
        resul.setActivo(ConversionesUtil.intToBoolean(intActivo));
        long ultimaActualizacion = cursor.getLong(cursor.getColumnIndex(ImagenesEventoSQLite.COLUMNA_ULTIMA_ACTUALIZACION));
        resul.setUltimaActualizacion(new Date(ultimaActualizacion));

        return resul;
    }

}
