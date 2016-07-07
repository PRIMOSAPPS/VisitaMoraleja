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
import com.primos.visitamoraleja.contenidos.ActividadEvento;
import com.primos.visitamoraleja.contenidos.SitioEvento;
import com.primos.visitamoraleja.util.UtilImage;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by h on 27/06/16.
 */
public class InfoWindowsActividadEventoAdapter implements GoogleMap.InfoWindowAdapter {
    private Activity actividad;
    private ActividadEvento actividadEvento;

    public InfoWindowsActividadEventoAdapter(Activity actividad) {
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
        text.setText(actividadEvento.getDescripcion());

        ImageView imagenView = (ImageView)v.findViewById(R.id.imagenInfoWindowEvento);
        ItfAlmacenamiento almacenamiento = AlmacenamientoFactory.getAlmacenamiento(actividad);
        Bitmap imagen = almacenamiento.getImagenActividadEvento(actividadEvento.getId(), actividadEvento.getNombreIcono());
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
        tituloInfoWindowEvento.setText(actividadEvento.getNombre());

        Date inicio = actividadEvento.getInicio();
        if(inicio != null) {
            StringBuilder sbInicioFin = new StringBuilder();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            sbInicioFin.append(sdf.format(inicio));

            Date fin = actividadEvento.getInicio();
            if(fin != null) {
                sdf = new SimpleDateFormat("HH:mm");
                sbInicioFin.append("-");
                sbInicioFin.append(sdf.format(fin));
            }

            TextView inicioFinActividad = (TextView)v.findViewById(R.id.inicioFinActividad);
        }

        return v;
    }

    public void setActividadEvento(ActividadEvento actividadEvento) {
        this.actividadEvento = actividadEvento;
    }
}
