package com.primos.visitamoraleja.actividades;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.primos.visitamoraleja.ActionBarListActivity;
import com.primos.visitamoraleja.R;
import com.primos.visitamoraleja.actividades.eventos.AbstractEventos;
import com.primos.visitamoraleja.bdsqlite.datasource.AbstractDataSource;

public abstract class ListaGeneralActivity extends AbstractEventos {

    protected AbstractDataSource dataSource;

    protected abstract AbstractDataSource createDataSource();

    protected abstract void cargar();

    protected abstract String getTitulo();

    protected abstract void mostrarDetalle(View view);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_general);

        dataSource = createDataSource();
        openDataSource();

        setTitulo(getTitulo());

        getTitulo();
        cargar();

        initMenuLateral();
    }

    protected void openDataSource() {
        dataSource.open();
    }

    protected void closeDataSource() {
        dataSource.close();
    }

    @Override
    protected void onResume() {
        openDataSource();
        super.onResume();
    }

    @Override
    protected void onPause() {
        closeDataSource();
        super.onPause();
    }
}
