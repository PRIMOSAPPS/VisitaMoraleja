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
import com.primos.visitamoraleja.bdsqlite.datasource.EventosDataSource;
import com.primos.visitamoraleja.bdsqlite.datasource.ImagenesEventoDatasource;
import com.primos.visitamoraleja.contenidos.Evento;
import com.primos.visitamoraleja.contenidos.ImagenEvento;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by h on 3/04/16.
 */
public class ControlSlider {

    private Activity actividad;

    private long idEvento;

    private List<Uri> imagenesGaleria = null;//{R.drawable.slide001, R.drawable.slide002, R.drawable.slide003, R.drawable.slide004, R.drawable.slide005, R.drawable.slide006, R.drawable.slide007};

    private ImageSwitcher imageSwitcher;

    private int position;

    private static final Integer DURATION = 5000;

    private Timer timer = null;

    public ControlSlider(Activity actividad, long idEvento) {
        this.actividad = actividad;
        this.idEvento = idEvento;
        cargarGallery();
    }

    public void initSlider() {
        imageSwitcher = (ImageSwitcher) actividad.findViewById(R.id.imageSlider);
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView myView = new ImageView(actividad);
                myView.setScaleType(ImageView.ScaleType.FIT_XY);
                myView.setLayoutParams(new ImageSwitcher.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT));
                return myView;
            }
        });
        /*
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            public View makeView() {
                return new ImageView(actividad);
            }
        });
        */

        // Set animations
        // https://danielme.com/2013/08/18/diseno-android-transiciones-entre-activities/
        Animation fadeIn = AnimationUtils.loadAnimation(actividad, R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(actividad, R.anim.fade_out);
        imageSwitcher.setInAnimation(fadeIn);
        imageSwitcher.setOutAnimation(fadeOut);

        startSlider();
    }

    private void cargarGallery() {
        ImagenesEventoDatasource dataSource = null;
        try {
            imagenesGaleria = new ArrayList<>();
            ItfAlmacenamiento almacenamiento = AlmacenamientoFactory.getAlmacenamiento(actividad);
            dataSource = new ImagenesEventoDatasource(actividad);
            dataSource.open();
            List<ImagenEvento> imagenes = dataSource.getByIdEvento(idEvento);
            for(ImagenEvento imagen : imagenes) {
                File fich = new File(almacenamiento.getDirImagenEvento(idEvento, imagen.getNombre()));
                Uri uri = Uri.fromFile(fich);
                imagenesGaleria.add(uri);
            }
        } finally {
            if(dataSource != null) {
                dataSource.close();
            }
        }
    }


    private void startSlider() {
        final ItfAlmacenamiento almacenamiento = AlmacenamientoFactory.getAlmacenamiento(actividad);
        timer = new Timer("ControlSlider_" + actividad.getClass().getName());
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                // avoid exception:
                // "Only the original thread that created a view hierarchy can touch its views"
                actividad.runOnUiThread(new Runnable() {
                    public void run() {
                        // El null se pasa por esto
                        // http://stackoverflow.com/questions/2313148/imageview-setimageuri-does-not-work-when-trying-to-assign-a-r-drawable-x-uri
                        // Pero parece que no funciona
                        //imageSwitcher.setImageURI(null);

                        Drawable imagen = new BitmapDrawable(actividad.getResources(), imagenesGaleria.get(position).toString());
                        imageSwitcher.setImageURI(imagenesGaleria.get(position));
                        position++;
                        if (position == imagenesGaleria.size()) {
                            position = 0;
                        }
                    }
                });
            }

        }, 0, DURATION);
    }

    // Stops the slider when the Activity is going into the background
    public void pause() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void resume() {
        if (timer == null) {
            startSlider();
        }

    }

}
