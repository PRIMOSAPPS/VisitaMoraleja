package com.primos.visitamoraleja.mapas.eventos;

import android.content.Context;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.primos.visitamoraleja.bdsqlite.datasource.CategoriaEventoDataSource;
import com.primos.visitamoraleja.bdsqlite.datasource.EventosDataSource;
import com.primos.visitamoraleja.contenidos.CategoriaEvento;
import com.primos.visitamoraleja.contenidos.Evento;
import com.primos.visitamoraleja.mapas.ControlMapaItf;

import java.util.List;

/**
 * Created by h on 25/06/16.
 */
public class ControlMapaEventos implements ControlMapaItf {
    private Evento evento;
    private Context contexto;

    @Override
    public void tratarMapa(GoogleMap googleMap, Context contexto) {
        LatLng moraleja = new LatLng(evento.getLatitud(), evento.getLongitud());
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(moraleja));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(moraleja, evento.getZoomInicial()));
    }

    public void init(long idEvento, Context contexto) {
        this.contexto = contexto;
        EventosDataSource dataSource = new EventosDataSource(contexto);
        dataSource.open();
        try {
            dataSource.open();
            evento = dataSource.getById(idEvento);
        } finally {
            dataSource.close();
        }
    }

    private void cargarCategorias() {
        CategoriaEventoDataSource dataSource = new CategoriaEventoDataSource(contexto);
        try {
            dataSource.open();
            List<CategoriaEvento> categorias = dataSource.getByIdEvento(evento.getId());
        } finally {
            dataSource.close();
        }
    }
}
