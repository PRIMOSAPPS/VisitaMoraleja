package com.primos.visitamoraleja.actividades.eventos;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.primos.visitamoraleja.R;
import com.primos.visitamoraleja.almacenamiento.AlmacenamientoFactory;
import com.primos.visitamoraleja.almacenamiento.ItfAlmacenamiento;
import com.primos.visitamoraleja.bdsqlite.datasource.ActividadEventoDataSource;
import com.primos.visitamoraleja.constantes.Constantes;
import com.primos.visitamoraleja.contenidos.ActividadEvento;

public class ActividadEventoDetalleActivity extends AbstractEventos {
    public final static String ID_ACTIVIDAD_EVENTO = "ID_ACTIVIDAD_EVENTO";

    private long idActividad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_evento_detalle);

        initMenuLateral();

        idActividad = (long) getIntent().getExtras().get(ActividadEventoDetalleActivity.ID_ACTIVIDAD_EVENTO);

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
}
