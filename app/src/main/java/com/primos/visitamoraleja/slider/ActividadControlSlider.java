package com.primos.visitamoraleja.slider;

import android.app.Activity;
import android.net.Uri;

import com.primos.visitamoraleja.R;
import com.primos.visitamoraleja.almacenamiento.AlmacenamientoFactory;
import com.primos.visitamoraleja.almacenamiento.ItfAlmacenamiento;
import com.primos.visitamoraleja.bdsqlite.datasource.ImagenesActividadEventoDatasource;
import com.primos.visitamoraleja.bdsqlite.datasource.ImagenesEventoDatasource;
import com.primos.visitamoraleja.contenidos.ImagenActividadEvento;
import com.primos.visitamoraleja.contenidos.ImagenEvento;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by h on 3/04/16.
 */
public class ActividadControlSlider extends AbstractControlSlider {

    public ActividadControlSlider(Activity actividad, long id) {
        super(actividad, id, R.id.imageActividadSlider);
    }

    protected void cargarGallery() {
        ImagenesActividadEventoDatasource dataSource = null;
        try {
            imagenesGaleria = new ArrayList<>();
            ItfAlmacenamiento almacenamiento = AlmacenamientoFactory.getAlmacenamiento(actividad);
            dataSource = new ImagenesActividadEventoDatasource(actividad);
            dataSource.open();
            List<ImagenActividadEvento> imagenes = dataSource.getByIdActividad(id);
            for(ImagenActividadEvento imagen : imagenes) {
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
