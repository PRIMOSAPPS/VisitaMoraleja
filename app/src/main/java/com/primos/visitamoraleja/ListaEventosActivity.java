package com.primos.visitamoraleja;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.primos.visitamoraleja.bdsqlite.datasource.EventosDataSource;
import com.primos.visitamoraleja.contenidos.Evento;

public class ListaEventosActivity extends ActionBarListActivity {
	public final static String ID_SITIO = "id_sitio";
	
	private EventosDataSource dataSource;
	private long idSitio;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_eventos);
		dataSource = new EventosDataSource(this);
		dataSource.open();
		
		idSitio = getIntent().getExtras().getLong(ID_SITIO);

	}

	public void mostrarDetalle(View view) {
		Evento evento = (Evento)view.getTag();

		Intent intent = new Intent(this, DetalleEventoTemporalActivity.class);
		intent.putExtra(DetalleEventoTemporalActivity.ID_EVENTO, evento.getId());
    	startActivity(intent);
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
