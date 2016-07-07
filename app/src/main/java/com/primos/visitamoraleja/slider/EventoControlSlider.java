package com.primos.visitamoraleja.slider;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ViewSwitcher;

import com.primos.visitamoraleja.R;
import com.primos.visitamoraleja.almacenamiento.AlmacenamientoFactory;
import com.primos.visitamoraleja.almacenamiento.ItfAlmacenamiento;
import com.primos.visitamoraleja.bdsqlite.datasource.ImagenesEventoDatasource;
import com.primos.visitamoraleja.contenidos.ImagenEvento;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by h on 3/04/16.
 */
public class EventoControlSlider extends AbstractControlSlider {

    public EventoControlSlider(Activity actividad, long id) {
        super(actividad, id, R.id.imageSlider);
    }

    protected void cargarGallery() {
        ImagenesEventoDatasource dataSource = null;
        try {
            imagenesGaleria = new ArrayList<>();
            ItfAlmacenamiento almacenamiento = AlmacenamientoFactory.getAlmacenamiento(actividad);
            dataSource = new ImagenesEventoDatasource(actividad);
            dataSource.open();
            List<ImagenEvento> imagenes = dataSource.getByIdEvento(id);
            for(ImagenEvento imagen : imagenes) {
                File fich = new File(almacenamiento.getDirImagenEvento(id, imagen.getNombre()));
                Uri uri = Uri.fromFile(fich);
                imagenesGaleria.add(uri);
            }
        } finally {
            if(dataSource != null) {
                dataSource.close();
            }
        }
    }

}
