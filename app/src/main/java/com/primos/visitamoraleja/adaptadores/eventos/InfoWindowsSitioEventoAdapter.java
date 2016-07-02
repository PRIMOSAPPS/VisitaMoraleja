package com.primos.visitamoraleja.adaptadores.eventos;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.primos.visitamoraleja.R;
import com.primos.visitamoraleja.almacenamiento.AlmacenamientoFactory;
import com.primos.visitamoraleja.almacenamiento.ItfAlmacenamiento;
import com.primos.visitamoraleja.contenidos.SitioEvento;
import com.primos.visitamoraleja.mapas.eventos.ControlMapaEventos;
import com.primos.visitamoraleja.util.UtilImage;

/**
 * Created by h on 27/06/16.
 */
public class InfoWindowsSitioEventoAdapter implements GoogleMap.InfoWindowAdapter {
    private Activity actividad;
    private SitioEvento sitioEvento;

    public InfoWindowsSitioEventoAdapter(Activity actividad) {
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
        text.setText(sitioEvento.getDescripcion());


        ImageView imagenView = (ImageView)v.findViewById(R.id.imagenInfoWindowEvento);
        ItfAlmacenamiento almacenamiento = AlmacenamientoFactory.getAlmacenamiento(actividad);
        Bitmap imagen = almacenamiento.getImagenActividadEvento(sitioEvento.getId(), sitioEvento.getNombreIcono());
        if(imagen != null) {
            if(imagen != null) {
                imagen = UtilImage.createScaledBitmap(imagen, 72);
                imagenView.setImageBitmap(imagen);
                imagenView.setTag(imagen);
                imagenView.setScaleType(ImageView.ScaleType.FIT_END);
            }
        } else {
            imagenView.setVisibility(View.GONE);
        }

        TextView tituloInfoWindowEvento = (TextView)v.findViewById(R.id.tituloInfoWindowEvento);
        tituloInfoWindowEvento.setText(sitioEvento.getNombre());

        return v;
    }

    public void setSitioEvento(SitioEvento sitioEvento) {
        this.sitioEvento = sitioEvento;
    }
}
