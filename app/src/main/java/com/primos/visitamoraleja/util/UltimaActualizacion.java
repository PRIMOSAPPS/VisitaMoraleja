package com.primos.visitamoraleja.util;

import android.content.Context;

import com.primos.visitamoraleja.PreferenciasActivity;
import com.primos.visitamoraleja.bdsqlite.datasource.CategoriasDataSource;
import com.primos.visitamoraleja.bdsqlite.datasource.EventosDataSource;
import com.primos.visitamoraleja.bdsqlite.datasource.SitiosDataSource;

/**
 * Created by h on 18/05/16.
 */
public class UltimaActualizacion {
    public long getUltimaActualizacion(Context contexto) {
        long ultimaActualizacion = PreferenciasActivity.getFechaUltimaActualizacion(contexto);
        if(ultimaActualizacion == 0) {
            SitiosDataSource dataSource = new SitiosDataSource(contexto);
            dataSource.open();
            long ultimaActualizacionSitios = dataSource.getUltimaActualizacion();
            dataSource.close();
            CategoriasDataSource dataSourceCategoria = new CategoriasDataSource(contexto);
            dataSourceCategoria.open();
            long ultimaActualizacionCategorias = dataSourceCategoria.getUltimaActualizacion();
            dataSourceCategoria.close();
            ultimaActualizacion = (ultimaActualizacionSitios < ultimaActualizacionCategorias) ? ultimaActualizacionSitios : ultimaActualizacionCategorias;
        }
        return ultimaActualizacion;
    }

    public long getUltimaActualizacionEventos(Context contexto) {
        long ultimaActualizacion = PreferenciasActivity.getFechaUltimaActualizacionEventos(contexto);
        if(ultimaActualizacion == 0) {
            EventosDataSource dataSource = new EventosDataSource(contexto);
            dataSource.open();
            ultimaActualizacion = dataSource.getUltimaActualizacion();
            dataSource.close();
        }
        return ultimaActualizacion;
    }
}
