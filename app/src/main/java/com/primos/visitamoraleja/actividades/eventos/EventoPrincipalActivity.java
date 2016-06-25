package com.primos.visitamoraleja.actividades.eventos;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.primos.visitamoraleja.R;
import com.primos.visitamoraleja.bdsqlite.datasource.EventosDataSource;
import com.primos.visitamoraleja.constantes.Constantes;
import com.primos.visitamoraleja.contenidos.Evento;
import com.primos.visitamoraleja.slider.ControlSlider;

public class EventoPrincipalActivity extends Activity {
	public final static String ID_EVENTO = "idEvento";
	public final static String EVENTO = "evento";
	private final static String TAG = "[" + EventoPrincipalActivity.class.getName() + "]";
	private ControlSlider controlSlider;
	//private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	//private EventosDataSource dataSource = null;
	//private Evento evento;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evento_principal);
		long idEvento = (long) getIntent().getExtras().get(EventoPrincipalActivity.ID_EVENTO);

		Evento evento = null;
		EventosDataSource dataSource = null;
		try {
			controlSlider = new ControlSlider(this, idEvento);
			controlSlider.initSlider();

			dataSource = new EventosDataSource(this);
			dataSource.open();
			evento = dataSource.getById(idEvento);

			WebView textoEvento = (WebView) findViewById(R.id.textoEvento);
			textoEvento.loadDataWithBaseURL(null, evento.getTexto(), Constantes.mimeType, Constantes.encoding, null);
		} catch(Exception e) {
			Log.e(TAG, "Error al cargar un evento.", e);
		} finally {
			if(dataSource != null) {
				dataSource.close();
			}
		}

		/*
		dataSource = new EventosDataSource(this);
		dataSource.open();
		
		TextView tvNombreEvento = (TextView) findViewById(R.id.tvNombreEvento);
		TextView tvFechasEvento = (TextView) findViewById(R.id.tvFechasEvento);
		WebView wvEventoTextoLargo1 = (WebView) findViewById(R.id.wvEventoTextoLargo1);
		ImageView imgIcono = (ImageView) findViewById(R.id.imgIconoEvento);
		

		evento = dataSource.getById(idEvento);
		tvNombreEvento.setText(evento.getNombre());
		
		String strFechaInicio = "Desde: " + dateFormat.format(evento.getInicio());
		String strFechaFin = "Hasta: " + dateFormat.format(evento.getFin());
		tvFechasEvento.setText(strFechaInicio + "\n" + strFechaFin);
		
		String textoWebView = evento.getTexto();
		wvEventoTextoLargo1.loadDataWithBaseURL(null, textoWebView, Constantes.mimeType, Constantes.encoding, null);
		
		ItfAlmacenamiento almacenamiento = AlmacenamientoFactory.getAlmacenamiento(this);
		Bitmap bitmap = almacenamiento.getImagenEvento(evento.getId(), evento.getNombreIcono());
		imgIcono.setImageBitmap(bitmap);
		*/
	}

	/*
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
	*/

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
