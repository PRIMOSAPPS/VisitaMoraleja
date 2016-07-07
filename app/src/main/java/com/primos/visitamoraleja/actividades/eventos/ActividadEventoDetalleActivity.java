package com.primos.visitamoraleja.actividades.eventos;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.primos.visitamoraleja.R;
import com.primos.visitamoraleja.almacenamiento.AlmacenamientoFactory;
import com.primos.visitamoraleja.almacenamiento.ItfAlmacenamiento;
import com.primos.visitamoraleja.bdsqlite.datasource.ActividadEventoDataSource;
import com.primos.visitamoraleja.bdsqlite.datasource.ImagenesActividadEventoDatasource;
import com.primos.visitamoraleja.constantes.Constantes;
import com.primos.visitamoraleja.contenidos.ActividadEvento;
import com.primos.visitamoraleja.contenidos.ImagenActividadEvento;
import com.primos.visitamoraleja.slider.AbstractControlSlider;
import com.primos.visitamoraleja.slider.ActividadControlSlider;
import com.primos.visitamoraleja.slider.EventoControlSlider;

import java.util.List;

public class ActividadEventoDetalleActivity extends AbstractEventos {
    public final static String ID_ACTIVIDAD_EVENTO = "ID_ACTIVIDAD_EVENTO";

    private AbstractControlSlider controlSlider;

    private long idActividad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_evento_detalle);

        initMenuLateral();

        idActividad = (long) getIntent().getExtras().get(ActividadEventoDetalleActivity.ID_ACTIVIDAD_EVENTO);

        controlSlider = new ActividadControlSlider(this, idActividad);
        controlSlider.initSlider();

        visibilidadImagenes();

        ActividadEvento actividadEvento = getSitioEvento();

        ImageView imagen = (ImageView)findViewById(R.id.ivActividadEventoIcono);
        ItfAlmacenamiento almacenamiento = AlmacenamientoFactory.getAlmacenamiento(this);
        Bitmap bitmap = almacenamiento.getImagenActividadEvento(actividadEvento.getId(), actividadEvento.getNombreIcono());
        imagen.setImageBitmap(bitmap);

        TextView nombre = (TextView)findViewById(R.id.tvActividadEventoNombre);
        nombre.setText(actividadEvento.getNombre());

        WebView texto = (WebView)findViewById(R.id.wbActividadEventoTexto);
        texto.loadDataWithBaseURL(null, actividadEvento.getTexto(), Constantes.mimeType, Constantes.encoding, null);
    }

    private void visibilidadImagenes() {
        ImagenesActividadEventoDatasource dataSource = null;

        try {
            dataSource = new ImagenesActividadEventoDatasource(this);
            dataSource.open();
            List<ImagenActividadEvento> resul = dataSource.getByIdActividad(idActividad);
            if(resul.isEmpty()) {
                View v = findViewById(R.id.layoutImageActividadSlider);
                v.setVisibility(View.GONE);
            }
        } finally {
            dataSource.close();
        }
    }

    private ActividadEvento getSitioEvento() {
        ActividadEventoDataSource dataSource = null;
        ActividadEvento resul = null;

        try {
            dataSource = new ActividadEventoDataSource(this);
            dataSource.open();
            resul = dataSource.getById(idActividad);
        } finally {
            dataSource.close();
        }

        return resul;
    }

    @Override
    protected void onPause() {
        super.onPause();
        controlSlider.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        controlSlider.resume();
    }
}
