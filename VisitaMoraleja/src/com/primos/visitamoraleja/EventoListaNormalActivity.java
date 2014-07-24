package com.primos.visitamoraleja;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.primos.visitamoraleja.adaptadores.SitioAdaptador;
import com.primos.visitamoraleja.bdsqlite.datasource.SitiosDataSource;
import com.primos.visitamoraleja.contenidos.Sitio;

public class EventoListaNormalActivity extends  ActionBarListActivity {
	private SitiosDataSource dataSource;
	private String categoriaSeleccionada = null;
	private boolean mostrarFavoritos = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_evento_lista_normal);
		setContentView(R.layout.fragment_evento_lista_normal);
		
		dataSource = new SitiosDataSource(this);
		dataSource.open();
		
		categoriaSeleccionada = (String) getIntent().getExtras().get("categoria");
		String favoritos = (String) getIntent().getExtras().get("favoritos");
		mostrarFavoritos = Boolean.parseBoolean(favoritos);
		
		String strParaTitulo = categoriaSeleccionada;
		if(mostrarFavoritos) {
			strParaTitulo = "FAVORITOS";
		}
		// Se asigna el titulo del action bar
		setTitulo(strParaTitulo);
		
		cargarSitios();

	}
	
	public void mostrarDetalle(View view) {
		Sitio sitio = (Sitio)view.getTag();

		Intent intent = new Intent(this, DetalleEventoActivity.class);
		intent.putExtra("idSitio", sitio.getId());
    	startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.evento_lista_normal, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.actionbar_inicio ) {
        	Intent i = new Intent(this, MainActivity.class);
        	i.putExtra(MainActivity.ACTUALIZAR, false);
            startActivity(i);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void cargarSitios() {
		List<Sitio> listaSitios = null;
		if(mostrarFavoritos) {
			listaSitios = dataSource.getByFavorito(1);
		} else if (categoriaSeleccionada != null){
			listaSitios = dataSource.getByCategoria(categoriaSeleccionada);
		}
		setListAdapter(new SitioAdaptador(this, listaSitios));
	}

	@Override
	protected void onResume() {
		dataSource.open();
		// Se recargan los sitios solamente si estamos mostrando los favoritos, que pueden haber cambiado
		if(mostrarFavoritos) {
			cargarSitios();
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		dataSource.close();
		super.onPause();
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_evento_lista_normal, container, false);
			return rootView;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(this, MainActivity.class);
    	startActivity(intent);
	}

}