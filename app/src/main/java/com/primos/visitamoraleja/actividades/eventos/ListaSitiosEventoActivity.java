package com.primos.visitamoraleja.actividades.eventos;

import android.os.Bundle;

import com.primos.visitamoraleja.R;
import com.primos.visitamoraleja.actividades.ListaGeneralActivity;
import com.primos.visitamoraleja.adaptadores.SitioEventoAdaptador;
import com.primos.visitamoraleja.bdsqlite.datasource.AbstractDataSource;
import com.primos.visitamoraleja.bdsqlite.datasource.SitioEventoDataSource;
import com.primos.visitamoraleja.contenidos.SitioEvento;

import java.util.List;

/**
 * Created by h on 25/06/16.
 */
public class ListaSitiosEventoActivity extends ListaGeneralActivity {
    private long idEvento;

    @Override
    protected AbstractDataSource createDataSource() {
        return new SitioEventoDataSource(this);
    }

    @Override
    protected void cargar() {
        List<SitioEvento> lista = ((SitioEventoDataSource)dataSource).getByIdEvento(idEvento);
        setListAdapter(new SitioEventoAdaptador(this, lista));
    }

    @Override
    protected String getTitulo() {
        return getResources().getString(R.string.sitios);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        idEvento = (long) getIntent().getExtras().get(EventoPrincipalActivity.ID_EVENTO);
        super.onCreate(savedInstanceState);
    }
}
