package com.primos.visitamoraleja;

import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import com.primos.visitamoraleja.almacenamiento.AlmacenamientoFactory;
import com.primos.visitamoraleja.almacenamiento.ItfAlmacenamiento;
import com.primos.visitamoraleja.bdsqlite.datasource.EventosDataSource;
import com.primos.visitamoraleja.constantes.Constantes;
import com.primos.visitamoraleja.contenidos.Evento;

public class DetalleEventoTemporalActivity extends Activity {
	public final static String ID_EVENTO = "idEvento";
	public final static String EVENTO = "evento";
	private final static String TAG = "[" + DetalleEventoTemporalActivity.class.getName() + "]";
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private EventosDataSource dataSource = null;
	private Evento evento;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalle_evento_temporal);
		dataSource = new EventosDataSource(this);
		dataSource.open();
		
		TextView tvNombreEvento = (TextView) findViewById(R.id.tvNombreEvento);
		TextView tvFechasEvento = (TextView) findViewById(R.id.tvFechasEvento);
		WebView wvEventoTextoLargo1 = (WebView) findViewById(R.id.wvEventoTextoLargo1);
		ImageView imgIcono = (ImageView) findViewById(R.id.imgIconoEvento);
		
		long idEvento = (long) getIntent().getExtras().get(DetalleEventoTemporalActivity.ID_EVENTO);
		evento = dataSource.getById(idEvento);
		tvNombreEvento.setText(evento.getNombre());
		
		String strFechaInicio = "Desde: " + dateFormat.format(evento.getInicio());
		String strFechaFin = "Hasta: " + dateFormat.format(evento.getFin());
		tvFechasEvento.setText(strFechaInicio + "\n" + strFechaFin);
		
		String texto1 = evento.getTexto1();
		String texto2 = evento.getTexto2();
		String textoWebView = texto1 + "<div style='clear:both;'></div>" + texto2;
		wvEventoTextoLargo1.loadDataWithBaseURL(null, textoWebView, Constantes.mimeType, Constantes.encoding, null);
		
		ItfAlmacenamiento almacenamiento = AlmacenamientoFactory.getAlmacenamiento(this);
		Bitmap bitmap = almacenamiento.getIconoEvento(evento.getId(), evento.getNombreIcono());
		imgIcono.setImageBitmap(bitmap);
	}
	
	public void mostrarEnMapa(View boton_pulsado) {
		Intent lanzarmapa = new Intent(this, MapaLugaresActivity.class);
		lanzarmapa.putExtra("nombre", evento.getNombre());
		lanzarmapa.putExtra("latitud", evento.getLatitud());
		lanzarmapa.putExtra("longitud", evento.getLongitud());
		lanzarmapa.putExtra(MapaLugaresActivity.ORIGEN, EVENTO);
		lanzarmapa.putExtra(MapaLugaresActivity.ID_RECIBIDO, evento.getId());

		startActivity(lanzarmapa);
		finish();
	}

	@Override
	protected void onResume() {
		dataSource.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		dataSource.close();
		super.onPause();
	}
}
