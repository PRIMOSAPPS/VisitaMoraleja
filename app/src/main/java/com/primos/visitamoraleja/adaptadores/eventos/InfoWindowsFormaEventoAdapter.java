package com.primos.visitamoraleja.adaptadores.eventos;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.primos.visitamoraleja.R;
import com.primos.visitamoraleja.almacenamiento.AlmacenamientoFactory;
import com.primos.visitamoraleja.almacenamiento.ItfAlmacenamiento;
import com.primos.visitamoraleja.constantes.Constantes;
import com.primos.visitamoraleja.mapas.eventos.ControlMapaEventos;

/**
 * Created by h on 27/06/16.
 */
public class InfoWindowsFormaEventoAdapter implements GoogleMap.InfoWindowAdapter {
    private Activity actividad;
    private ControlMapaEventos.ContenidoMapa cm;

    public InfoWindowsFormaEventoAdapter(Activity actividad) {
        this.actividad = actividad;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        // Getting view from the layout file info_window_layout
        View v = actividad.getLayoutInflater().inflate(R.layout.infowindows_formaevento, null);
        TextView text = (TextView)v.findViewById(R.id.txtTextoFormaEvento);
        text.setText(cm.getForma().getTexto());


        ImageView imagenView = (ImageView)v.findViewById(R.id.imagenInfoWindowEvento);
        imagenView.setVisibility(View.GONE);

        TextView tituloInfoWindowEvento = (TextView)v.findViewById(R.id.tituloInfoWindowEvento);
        tituloInfoWindowEvento.setVisibility(View.GONE);

        return v;
    }

    public void setContenidoMapa(ControlMapaEventos.ContenidoMapa cm) {
        this.cm = cm;
    }
}
