package com.primos.visitamoraleja;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.primos.visitamoraleja.almacenamiento.AlmacenamientoFactory;
import com.primos.visitamoraleja.almacenamiento.ItfAlmacenamiento;
import com.primos.visitamoraleja.bdsqlite.datasource.NotificacionesDataSource;
import com.primos.visitamoraleja.bdsqlite.datasource.SitiosDataSource;
import com.primos.visitamoraleja.constantes.Constantes;
import com.primos.visitamoraleja.contenidos.Notificacion;
import com.primos.visitamoraleja.contenidos.Sitio;
import com.primos.visitamoraleja.permisos.Permisos;
import com.primos.visitamoraleja.util.UtilImage;

public class DetalleNotificacionActivity extends ActionBarListActivity {
    public final static String ID_NOTIFICACION = "ID_NOTIFICACION";

    private NotificacionesDataSource dataSource = null;
    private SitiosDataSource dataSourceSitios = null;

    private Notificacion notificacion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_notificacion);

        dataSource = new NotificacionesDataSource(this);
        dataSource.open();
        dataSourceSitios = new SitiosDataSource(this);
        dataSourceSitios.open();

        Bundle extras = getIntent().getExtras();
        long idNotificacion = (long) getIntent().getExtras().get(ID_NOTIFICACION);
        notificacion = dataSource.getById(idNotificacion);

        Sitio sitio = dataSourceSitios.getById(notificacion.getIdSitio());
        if(sitio != null) {

            Permisos permisosUtil = new Permisos();
            permisosUtil.preguntarPermisos(this, ItfAlmacenamiento.permisosNecesarios);

            ItfAlmacenamiento almacenamiento = AlmacenamientoFactory.getAlmacenamiento(this);
            Bitmap bitmap = almacenamiento.getImagenSitio(sitio.getId(), sitio.getNombreLogotipo());
            if (bitmap != null) {
                ImageView imageView = (ImageView) findViewById(R.id.imagenListaEventos);
                int width = imageView.getDrawable().getIntrinsicWidth();
                bitmap = UtilImage.createScaledBitmap(bitmap, width);
                imageView.setImageBitmap(bitmap);
            }

            TextView textNombreSitio = (TextView) findViewById(R.id.textNombreSitio);
            textNombreSitio.setText(sitio.getNombre());
        }

        TextView titulo = (TextView)findViewById(R.id.detalleNotificacionTitulo);
        WebView texto = (WebView)findViewById(R.id.detalleNotificacionTexto);

        titulo.setText(notificacion.getTitulo());
        texto.loadDataWithBaseURL(null, notificacion.getTexto(), Constantes.mimeType, Constantes.encoding, null);

    }

    public void visitarSitio(View view) {
        Intent intent = new Intent(this, DetalleEventoActivity.class);
        intent.putExtra(DetalleEventoActivity.ID_SITIO, notificacion.getIdSitio());
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        dataSource.open();
        dataSource.eliminarPasadas();
        dataSourceSitios.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        dataSource.close();
        dataSourceSitios.close();
        super.onPause();
    }
}
