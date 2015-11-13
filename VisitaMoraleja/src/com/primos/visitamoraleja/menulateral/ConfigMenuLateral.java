package com.primos.visitamoraleja.menulateral;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.primos.visitamoraleja.EventoListaNormalActivity;
import com.primos.visitamoraleja.NotificacionesActivity;
import com.primos.visitamoraleja.PreferenciasActivity;
import com.primos.visitamoraleja.R;
import com.primos.visitamoraleja.bdsqlite.datasource.CategoriasDataSource;
import com.primos.visitamoraleja.contenidos.Categoria;

public class ConfigMenuLateral {
	private static int IND_FAVORITOS = 5;
	private static int IND_VER_NOTIFICACIONES = -1;
	private static int IND_PREFERENCIAS = 7;
	
	private EventoListaNormalActivity actividadSitios;
	private Activity actividad;
	private Categoria categoriaSeleccionada;
	private boolean mostrarFavoritos = true;

	public ConfigMenuLateral(EventoListaNormalActivity actividadSitios,
			Categoria categoriaSeleccionada, boolean mostrarFavoritos) {
		super();
		this.actividadSitios = actividadSitios;
		this.categoriaSeleccionada = categoriaSeleccionada;
		this.mostrarFavoritos = mostrarFavoritos;
	}

	public ConfigMenuLateral(Activity actividad,
			Categoria categoriaSeleccionada, boolean mostrarFavoritos) {
		super();
		this.actividad = actividad;
		this.categoriaSeleccionada = categoriaSeleccionada;
		this.mostrarFavoritos = mostrarFavoritos;
	}

	public void iniciarMenuLateral() {
//		{"Conoce Moraleja", "Tomamos algo", "Comer y dormir",
//		"De compras", "Servicios y reparaciones", "Favoritos", "Notificaciones", "Inicio"};
		
		Activity actividadTmp = actividad;
		if(actividadTmp == null) {
			actividadTmp = actividadSitios;
		}
		final Activity actividadRecibida = actividadTmp;
		
		final List<Categoria> listaCategorias = getListaCategorias(actividadRecibida);
		
		String[] valoresMenuLateral;

		int numOpciones = listaCategorias.size();
		numOpciones += 2;
		if(categoriaSeleccionada != null) {
			numOpciones += 1;
		}
		valoresMenuLateral = new String[numOpciones];
		
		Resources resources = actividadRecibida.getResources();
		int i=0;
		for(Categoria categoria : listaCategorias) {
			valoresMenuLateral[i++] = categoria.getDescripcion();
		}
		IND_FAVORITOS=i;
		valoresMenuLateral[i++] = (String)resources.getText(R.string.favoritos);
		if(categoriaSeleccionada != null) {
			IND_VER_NOTIFICACIONES = i;
			valoresMenuLateral[i++] = (String)resources.getText(R.string.ver_notificaciones);
		}
		IND_PREFERENCIAS = i;
		valoresMenuLateral[i++] = (String)resources.getText(R.string.actionbar_settings);
		
		ListView mDrawerOptions = (ListView) actividadRecibida.findViewById(R.id.menuLateralListaSitios);
		final DrawerLayout mDrawer = (DrawerLayout) actividadRecibida.findViewById(R.id.drawer_layout_lateral);
		
		mDrawerOptions.setAdapter(new ArrayAdapter(actividadRecibida, android.R.layout.simple_list_item_1, android.R.id.text1,
				valoresMenuLateral));
		mDrawerOptions.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int indice,
					long arg3) {
				mostrarFavoritos = false;
				if(indice < listaCategorias.size()) {
					categoriaSeleccionada = listaCategorias.get(indice);
					if(actividadSitios == null) {
						Intent intent = new Intent(actividadRecibida, EventoListaNormalActivity.class);
						intent.putExtra(EventoListaNormalActivity.CATEGORIA, categoriaSeleccionada.getNombre());
						actividadRecibida.startActivity(intent);
					} else {
						actividadSitios.cargarSitios(categoriaSeleccionada, false);
					}
				} else if(indice == IND_FAVORITOS) {
					categoriaSeleccionada = null;
					mostrarFavoritos = true;
					if(actividadSitios == null) {
						Intent intent = new Intent(actividadRecibida, EventoListaNormalActivity.class);
						intent.putExtra(EventoListaNormalActivity.FAVORITOS, Boolean.toString(mostrarFavoritos));
						actividadRecibida.startActivity(intent);
					} else {
						actividadSitios.cargarSitios(categoriaSeleccionada, mostrarFavoritos);
					}
				} else if(indice == IND_PREFERENCIAS) {
					Intent intent = new Intent(actividadRecibida, PreferenciasActivity.class);
					actividadRecibida.startActivity(intent);
				} else if(indice == IND_VER_NOTIFICACIONES) {
					Intent intent = new Intent(actividadRecibida, NotificacionesActivity.class);
					actividadRecibida.startActivity(intent);
				}
				mDrawer.closeDrawers();
			}
		});
	}
//	
//	protected void categoriaSeleccionada() {
//		
//	}
//	
//	protected void favoritosSeleccionados() {
//		
//	}
	
	private List<Categoria> getListaCategorias(Activity actividadRecibida) {
		CategoriasDataSource categoriasDataSource = new CategoriasDataSource(actividadRecibida);
		categoriasDataSource.open();
		List<Categoria> listaCategorias = categoriasDataSource.getAll();
		categoriasDataSource.close();

		return listaCategorias;
	}

}
