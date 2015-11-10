package com.primos.visitamoraleja;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ListView;

import com.primos.visitamoraleja.adaptadores.SitioAdaptador;
import com.primos.visitamoraleja.bdsqlite.datasource.CategoriasDataSource;
import com.primos.visitamoraleja.bdsqlite.datasource.SitiosDataSource;
import com.primos.visitamoraleja.contenidos.Categoria;
import com.primos.visitamoraleja.contenidos.Sitio;
import com.primos.visitamoraleja.menulateral.ConfigMenuLateral;

public class EventoListaNormalActivity extends  ActionBarListActivity {
	public final static String FAVORITOS = "favoritos";
	public final static String CATEGORIA = "categoria";
	
	private SitiosDataSource dataSource;
	private Categoria categoriaSeleccionada = null;
	private boolean mostrarFavoritos = false;
	
	private DrawerLayout mDrawer;
	private ListView mDrawerOptions;
//	private static String[] valoresMenuLateral;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_evento_lista_normal);
		setContentView(R.layout.fragment_evento_lista_normal);
		
		dataSource = new SitiosDataSource(this);
		dataSource.open();
		
		String nombreCategoria = (String) getIntent().getExtras().get(CATEGORIA);
		String favoritos = (String) getIntent().getExtras().get(FAVORITOS);
		mostrarFavoritos = Boolean.parseBoolean(favoritos);

		// Cargamos la categoria seleccionada
		Button btn = (Button)findViewById(R.id.btnMostrarNotificaciones);
		if(nombreCategoria != null) {
			CategoriasDataSource categoriasDataSource = new CategoriasDataSource(this);
			categoriasDataSource.open();
			categoriaSeleccionada = categoriasDataSource.getByNombre(nombreCategoria);
			categoriasDataSource.close();
		} else {
			btn.setVisibility(View.GONE);
			ListView listView =  getListView();
			LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
					FrameLayout.LayoutParams.MATCH_PARENT);
			listView.setLayoutParams(layoutParams);
		}

		mDrawerOptions = (ListView) findViewById(R.id.menuLateralListaSitios);
		mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout_lateral);
		ConfigMenuLateral cml = new ConfigMenuLateral(this, categoriaSeleccionada, mostrarFavoritos);
		cml.iniciarMenuLateral();
		
		String strParaTitulo = getTextoTitulo();
		
		// Se asigna el titulo del action bar
		setTitulo(strParaTitulo);
		
		cargarSitios(categoriaSeleccionada, mostrarFavoritos);

	}
	
	private String getTextoTitulo() {
		String resul = null;
		if(categoriaSeleccionada != null) {
			resul = categoriaSeleccionada.getNombre();
		}
		if(mostrarFavoritos) {
			resul = "FAVORITOS";
		}
		return resul;
	}
	
	public void mostrarDetalle(View view) {
		Sitio sitio = (Sitio)view.getTag();

		Intent intent = new Intent(this, DetalleEventoActivity.class);
		intent.putExtra(DetalleEventoActivity.ID_SITIO, sitio.getId());
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
		switch (item.getItemId()) {
		case R.id.actionbar_inicio:
        	Intent i = new Intent(this, MainActivity.class);
        	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	i.putExtra(MainActivity.ACTUALIZAR, false);
            startActivity(i);
			return true;
		case R.id.actionbar_notificaciones:
//			mostrarNotificaciones();
			if (mDrawer.isDrawerOpen(mDrawerOptions)){
				mDrawer.closeDrawers();
			}else{
				mDrawer.openDrawer(mDrawerOptions);
			}
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
	
	private void mostrarNotificaciones() {
		if(categoriaSeleccionada != null) {
			Intent iNotificaciones = new Intent(this, NotificacionesActivity.class);
			iNotificaciones.putExtra(NotificacionesActivity.CATEGORIA_NOTIFICACIONES, categoriaSeleccionada.getId());
			startActivity(iNotificaciones);
		}
	}
	
	public void mostrarNotificaciones(View view) {
		mostrarNotificaciones();
	}
	
	public void cargarSitios(Categoria categoriaSeleccionada, boolean mostrarFavoritos) {
		this.categoriaSeleccionada = categoriaSeleccionada;
		this.mostrarFavoritos = mostrarFavoritos;
		List<Sitio> listaSitios = null;
		if(mostrarFavoritos) {
			listaSitios = dataSource.getByFavorito(1);
		} else if (categoriaSeleccionada != null){
			listaSitios = dataSource.getByCategoria(categoriaSeleccionada.getNombre());
		}
		setTitulo(getTextoTitulo());
		setListAdapter(new SitioAdaptador(this, listaSitios));
	}

	@Override
	protected void onResume() {
		dataSource.open();
		// Se recargan los sitios solamente si estamos mostrando los favoritos, que pueden haber cambiado
		if(mostrarFavoritos) {
			cargarSitios(categoriaSeleccionada, mostrarFavoritos);
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

}