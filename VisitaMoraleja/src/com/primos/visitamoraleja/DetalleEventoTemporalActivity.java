package com.primos.visitamoraleja;

import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.primos.visitamoraleja.almacenamiento.AlmacenamientoFactory;
import com.primos.visitamoraleja.almacenamiento.ItfAlmacenamiento;
import com.primos.visitamoraleja.bdsqlite.datasource.EventosDataSource;
import com.primos.visitamoraleja.contenidos.Evento;

public class DetalleEventoTemporalActivity extends Activity {
	private final static String TAG = "[" + DetalleEventoTemporalActivity.class.getName() + "]";
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	public final static String ID_EVENTO = "idEvento";
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
		TextView tvTexto1 = (TextView) findViewById(R.id.tvTexto1);
		TextView tvTexto2 = (TextView) findViewById(R.id.tvTexto2);
		ImageView imgIcono = (ImageView) findViewById(R.id.imgIconoEvento);
		
		long idEvento = (long) getIntent().getExtras().get(DetalleEventoTemporalActivity.ID_EVENTO);
		evento = dataSource.getById(idEvento);
		tvNombreEvento.setText(evento.getNombre());
		
		String strFechaInicio = "Desde: " + dateFormat.format(evento.getInicio());
		String strFechaFin = "Hasta: " + dateFormat.format(evento.getFin());
		tvFechasEvento.setText(strFechaInicio + "\n" + strFechaFin);
		
		tvTexto1.setText(evento.getTexto1());
		tvTexto2.setText(evento.getTexto2());
		
		ItfAlmacenamiento almacenamiento = AlmacenamientoFactory.getAlmacenamiento(this);
		Bitmap bitmap = almacenamiento.getIconoEvento(evento.getId(), evento.getNombreIcono());
		imgIcono.setImageBitmap(bitmap);
	}
	
	public void mostrarEnMapa(View boton_pulsado) {
		Intent lanzarmapa = new Intent(this, MapaLugaresActivity.class);
		lanzarmapa.putExtra("nombre", evento.getNombre());
		lanzarmapa.putExtra("latitud", evento.getLatitud());
		lanzarmapa.putExtra("longitud", evento.getLongitud());
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
