package com.primos.visitamoraleja.actividades.eventos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ListView;

import com.primos.visitamoraleja.ActionBarListActivity;
import com.primos.visitamoraleja.MainActivity;
import com.primos.visitamoraleja.R;
import com.primos.visitamoraleja.bdsqlite.datasource.EventosDataSource;
import com.primos.visitamoraleja.constantes.Constantes;
import com.primos.visitamoraleja.contenidos.Evento;
import com.primos.visitamoraleja.menulateral.ConfigMenuLateral;
import com.primos.visitamoraleja.slider.ControlSlider;

public class EventoPrincipalActivity extends AbstractEventos {
	public final static String ID_EVENTO = "idEvento";
	public final static String EVENTO = "evento";
	private final static String TAG = "[" + EventoPrincipalActivity.class.getName() + "]";
	private ControlSlider controlSlider;
	//private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	//private EventosDataSource dataSource = null;
	//private Evento evento;
	private long idEvento;

	private DrawerLayout mDrawer;
	private ListView mDrawerOptions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evento_principal);
		idEvento = (long) getIntent().getExtras().get(EventoPrincipalActivity.ID_EVENTO);

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

		initMenuLateral();
	}

	public void verSitios(View view) {
		Intent i = new Intent(this, ListaSitiosEventoActivity.class);
		i.putExtra(EventoPrincipalActivity.ID_EVENTO, idEvento);
		startActivity(i);
	}

	public void verActividades(View view) {
		Intent i = new Intent(this, ListaActividadesEventoActivity.class);
		i.putExtra(EventoPrincipalActivity.ID_EVENTO, idEvento);
		startActivity(i);
	}

	public void verMapa(View view) {
		Intent i = new Intent(this, MapsEventosActivity.class);
		i.putExtra(EventoPrincipalActivity.ID_EVENTO, idEvento);
		startActivity(i);
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
