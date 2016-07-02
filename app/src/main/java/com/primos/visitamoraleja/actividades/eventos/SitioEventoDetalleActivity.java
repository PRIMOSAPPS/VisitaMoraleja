package com.primos.visitamoraleja.actividades.eventos;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.primos.visitamoraleja.R;
import com.primos.visitamoraleja.almacenamiento.AlmacenamientoFactory;
import com.primos.visitamoraleja.almacenamiento.ItfAlmacenamiento;
import com.primos.visitamoraleja.bdsqlite.datasource.SitioEventoDataSource;
import com.primos.visitamoraleja.constantes.Constantes;
import com.primos.visitamoraleja.contenidos.SitioEvento;

public class SitioEventoDetalleActivity extends AbstractEventos {
    public final static String ID_SITIO_EVENTO = "ID_SITIO_EVENTO";

    private long idEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitio_evento_detalle);

        initMenuLateral();

        idEvento = (long) getIntent().getExtras().get(SitioEventoDetalleActivity.ID_SITIO_EVENTO);

        SitioEvento sitioEvento = getSitioEvento();

        ImageView imagen = (ImageView)findViewById(R.id.ivSitioEventoIcono);
        ItfAlmacenamiento almacenamiento = AlmacenamientoFactory.getAlmacenamiento(this);
        Bitmap bitmap = almacenamiento.getImagenSitioEvento(sitioEvento.getId(), sitioEvento.getNombreIcono());
        imagen.setImageBitmap(bitmap);

        TextView nombre = (TextView)findViewById(R.id.tvSitioEventoNombre);
        nombre.setText(sitioEvento.getNombre());

        WebView texto = (WebView)findViewById(R.id.wbSitioEventoTexto);
        texto.loadDataWithBaseURL(null, sitioEvento.getTexto(), Constantes.mimeType, Constantes.encoding, null);
    }

    private SitioEvento getSitioEvento() {
        SitioEventoDataSource dataSource = null;
        SitioEvento sitioEvento = null;

        try {
            dataSource = new SitioEventoDataSource(this);
            dataSource.open();
            sitioEvento = dataSource.getById(idEvento);
        } finally {
            dataSource.close();
        }

        return sitioEvento;
    }
}
