package com.primos.visitamoraleja.actividades.eventos;

import com.primos.visitamoraleja.R;
import com.primos.visitamoraleja.actividades.ListaGeneralActivity;
import com.primos.visitamoraleja.adaptadores.ActividadEventoAdaptador;
import com.primos.visitamoraleja.adaptadores.SitioEventoAdaptador;
import com.primos.visitamoraleja.bdsqlite.datasource.AbstractDataSource;
import com.primos.visitamoraleja.bdsqlite.datasource.ActividadEventoDataSource;
import com.primos.visitamoraleja.bdsqlite.datasource.SitioEventoDataSource;
import com.primos.visitamoraleja.contenidos.ActividadEvento;
import com.primos.visitamoraleja.contenidos.SitioEvento;

import java.util.List;

/**
 * Created by h on 25/06/16.
 */
public class ListaActividadesEventoActivity extends ListaGeneralActivity {
    @Override
    protected AbstractDataSource createDataSource() {
        return new ActividadEventoDataSource(this);
    }

    @Override
    protected void cargar() {
        List<ActividadEvento> lista = ((ActividadEventoDataSource)dataSource).getAll();
        setListAdapter(new ActividadEventoAdaptador(this, lista));
    }

    @Override
    protected String getTitulo() {
        return getResources().getString(R.string.actividades);
    }
}
