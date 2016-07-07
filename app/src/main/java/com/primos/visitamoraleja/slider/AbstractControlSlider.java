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
 * Created by h on 5/07/16.
 */
public abstract class AbstractControlSlider {

    protected Activity actividad;

    protected long id;

    protected List<Uri> imagenesGaleria = null;//{R.drawable.slide001, R.drawable.slide002, R.drawable.slide003, R.drawable.slide004, R.drawable.slide005, R.drawable.slide006, R.drawable.slide007};

    private int idImageSwitcher;
    private ImageSwitcher imageSwitcher;

    private int position;

    private static final Integer DURATION = 5000;

    private Timer timer = null;

    protected abstract void cargarGallery();

    public AbstractControlSlider(Activity actividad, long id, int idImageSwitcher) {
        this.actividad = actividad;
        this.id = id;
        this.idImageSwitcher = idImageSwitcher;
        cargarGallery();
    }

    public void initSlider() {
        imageSwitcher = (ImageSwitcher) actividad.findViewById(idImageSwitcher);
        if(conImagenes()) {
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
    }

    private boolean conImagenes() {
        return (imagenesGaleria != null && !imagenesGaleria.isEmpty());
    }

    private void startSlider() {
        if(conImagenes()) {
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
    }

    public void ocultar() {
        imageSwitcher.setVisibility(View.GONE);
    }

    public void mostrar() {
        imageSwitcher.setVisibility(View.VISIBLE);
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
