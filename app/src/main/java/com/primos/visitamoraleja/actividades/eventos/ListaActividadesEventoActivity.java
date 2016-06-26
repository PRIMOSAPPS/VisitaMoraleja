package com.primos.visitamoraleja.actividades.eventos;

import android.os.Bundle;

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
    private long idEvento;

    @Override
    protected AbstractDataSource createDataSource() {
        return new ActividadEventoDataSource(this);
    }

    @Override
    protected void cargar() {
        List<ActividadEvento> lista = ((ActividadEventoDataSource)dataSource).getByIdEvento(idEvento);
        setListAdapter(new ActividadEventoAdaptador(this, lista));
    }

    @Override
    protected String getTitulo() {
        return getResources().getString(R.string.actividades);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        idEvento = (long) getIntent().getExtras().get(EventoPrincipalActivity.ID_EVENTO);
        super.onCreate(savedInstanceState);
    }
}
