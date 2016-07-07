package com.primos.visitamoraleja.adaptadores;

import android.app.Activity;
import android.graphics.Bitmap;

import com.primos.visitamoraleja.almacenamiento.AlmacenamientoFactory;
import com.primos.visitamoraleja.almacenamiento.ItfAlmacenamiento;
import com.primos.visitamoraleja.contenidos.IContenidoGeneral;

import java.util.List;

/**
 * Created by h on 25/06/16.
 */
public class SitioEventoAdaptador extends ListaGeneralAdaptador {
    public SitioEventoAdaptador(Activity actividad, List<? extends IContenidoGeneral> listaGeneral) {
        super(actividad, listaGeneral);
    }

    @Override
    protected Bitmap getBitmap(IContenidoGeneral obj) {
        ItfAlmacenamiento almacenamiento = AlmacenamientoFactory.getAlmacenamiento(actividad);
        return almacenamiento.getImagenSitioEvento(obj.getId(), obj.getNombreImagen());
    }
}
