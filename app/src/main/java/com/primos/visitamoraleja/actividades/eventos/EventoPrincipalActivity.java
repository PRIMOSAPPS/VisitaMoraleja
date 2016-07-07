package com.primos.visitamoraleja.actividades.eventos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.Toast;

import com.primos.visitamoraleja.R;
import com.primos.visitamoraleja.bdsqlite.datasource.ActividadEventoDataSource;
import com.primos.visitamoraleja.bdsqlite.datasource.EventosDataSource;
import com.primos.visitamoraleja.bdsqlite.datasource.SitioEventoDataSource;
import com.primos.visitamoraleja.constantes.Constantes;
import com.primos.visitamoraleja.contenidos.ActividadEvento;
import com.primos.visitamoraleja.contenidos.Evento;
import com.primos.visitamoraleja.contenidos.SitioEvento;
import com.primos.visitamoraleja.slider.EventoControlSlider;

import java.util.List;

public class EventoPrincipalActivity extends AbstractEventos {
	public final static String ID_EVENTO = "idEvento";
	public final static String EVENTO = "evento";
	private final static String TAG = "[" + EventoPrincipalActivity.class.getName() + "]";
	private EventoControlSlider controlSlider;
	//private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	//private EventosDataSource dataSource = null;
	//private Evento evento;
	private long idEvento;

	private DrawerLayout mDrawer;
	private ListView mDrawerOptions;

	private boolean existenSitios = false;
	private boolean existenActividades = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evento_principal);
		idEvento = (long) getIntent().getExtras().get(EventoPrincipalActivity.ID_EVENTO);

		Evento evento = null;
		EventosDataSource dataSource = null;
		try {
			controlSlider = new EventoControlSlider(this, idEvento);
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

		compruebaExistenciaDatos();

		initMenuLateral();
	}

	private void compruebaExistenciaDatos() {
		compruebaExistenciaSitios();
		compruebaExistenciaActividades();
	}

	private void compruebaExistenciaSitios() {
		SitioEventoDataSource dataSource = null;
		try {
			existenSitios = true;
			dataSource = new SitioEventoDataSource(this);
			dataSource.open();
			List<SitioEvento> sitiosEvento = dataSource.getByIdEvento(idEvento);
			if(sitiosEvento == null || sitiosEvento.isEmpty()) {
				existenSitios = false;
			}
		} finally {
			dataSource.close();
		}
	}

	private void compruebaExistenciaActividades() {
		ActividadEventoDataSource dataSource = null;
		try {
			existenActividades = true;
			dataSource = new ActividadEventoDataSource(this);
			dataSource.open();
			List<ActividadEvento> actividadesEvento = dataSource.getByIdEvento(idEvento);
			if(actividadesEvento == null || actividadesEvento.isEmpty()) {
				existenActividades = false;
			}
		} finally {
			dataSource.close();
		}
	}

	public void verSitios(View view) {
		if(existenSitios) {
			Intent i = new Intent(this, ListaSitiosEventoActivity.class);
			i.putExtra(EventoPrincipalActivity.ID_EVENTO, idEvento);
			startActivity(i);
		} else {
			Toast.makeText(this, R.string.no_hay_sitios, Toast.LENGTH_LONG).show();
		}
	}

	public void verActividades(View view) {
		if (existenSitios) {
			Intent i = new Intent(this, ListaActividadesEventoActivity.class);
			i.putExtra(EventoPrincipalActivity.ID_EVENTO, idEvento);
			startActivity(i);
		} else {
			Toast.makeText(this, R.string.no_hay_actividades, Toast.LENGTH_LONG).show();
		}
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
