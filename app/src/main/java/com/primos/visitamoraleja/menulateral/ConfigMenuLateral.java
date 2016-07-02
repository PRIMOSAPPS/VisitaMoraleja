package com.primos.visitamoraleja.menulateral;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.primos.visitamoraleja.EventoListaNormalActivity;
import com.primos.visitamoraleja.MainActivity;
import com.primos.visitamoraleja.MapaLugaresActivity;
import com.primos.visitamoraleja.MapsActivity;
import com.primos.visitamoraleja.NotificacionesActivity;
import com.primos.visitamoraleja.PreferenciasActivity;
import com.primos.visitamoraleja.R;
import com.primos.visitamoraleja.adaptadores.MenuLateralAdaptador;
import com.primos.visitamoraleja.bdsqlite.datasource.CategoriasDataSource;
import com.primos.visitamoraleja.bdsqlite.datasource.SitiosDataSource;
import com.primos.visitamoraleja.contenidos.Categoria;

public class ConfigMenuLateral {
	private static int IND_EVENTOS = 5;
	private static int IND_MAPA = 6;
	private static int IND_VER_NOTIFICACIONES = -1;
	private static int IND_PREFERENCIAS = 8;
	private static int IND_CREAR_NOTIFICACION = 9;

	private EventoListaNormalActivity actividadSitios;
	private Activity actividad;
	private Categoria categoriaSeleccionada;
	private boolean mostrarFavoritos = true;
	private boolean mostrarNotificaciones = true;

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
		
		List<DatosItemMenuLateral> listaItemsMenu = new ArrayList<>();
		final List<Categoria> listaCategorias = getListaCategorias(actividadRecibida);
		
//		String[] valoresMenuLateral;

		int numOpciones = listaCategorias.size();
		numOpciones += 3;
//		valoresMenuLateral = new String[numOpciones];
		
		Resources resources = actividadRecibida.getResources();
		int i=0;
		for(Categoria categoria : listaCategorias) {
			String textoMenu = categoria.getDescripcion();
			String nombreIcono = categoria.getNombreIcono();
			nombreIcono = nombreIcono.substring(0, nombreIcono.lastIndexOf("."));
			int identificadorImagen = resources.getIdentifier(nombreIcono, "mipmap", actividadTmp.getPackageName());
			DatosItemMenuLateral datosItem = new DatosItemMenuLateral(textoMenu, identificadorImagen);
			listaItemsMenu.add(datosItem);
//			valoresMenuLateral[i++] = categoria.getDescripcion();
		}
		IND_EVENTOS=listaItemsMenu.size();
		DatosItemMenuLateral datosItemEventos = new DatosItemMenuLateral((String)resources.getText(R.string.eventos),
				R.drawable.ic_eventos);
		listaItemsMenu.add(datosItemEventos);
		IND_MAPA = listaItemsMenu.size();
		DatosItemMenuLateral datosItemPreferenciasMapa = new DatosItemMenuLateral(
				(String)resources.getText(R.string.map_mapa), R.mipmap.ic_localizar);
		listaItemsMenu.add(datosItemPreferenciasMapa);

//		valoresMenuLateral[i++] = (String)resources.getText(R.string.favoritos);
		IND_VER_NOTIFICACIONES = listaItemsMenu.size();
		DatosItemMenuLateral datosItemNotificaciones = new DatosItemMenuLateral(
				(String)resources.getText(R.string.ver_notificaciones), R.mipmap.ic_notificacionesml);
		listaItemsMenu.add(datosItemNotificaciones);
//			valoresMenuLateral[i++] = (String)resources.getText(R.string.ver_notificaciones);
		IND_PREFERENCIAS = listaItemsMenu.size();
		DatosItemMenuLateral datosItemPreferencias = new DatosItemMenuLateral(
				(String)resources.getText(R.string.actionbar_settings), R.mipmap.ic_action_action_settings);
		listaItemsMenu.add(datosItemPreferencias);
//		valoresMenuLateral[i++] = (String)resources.getText(R.string.actionbar_settings);
//		IND_CREAR_NOTIFICACION = listaItemsMenu.size();
//		DatosItemMenuLateral datosItemPreferenciasNotificacion = new DatosItemMenuLateral(
//				"Crear Notif", R.mipmap.ic_action_action_settings);
//		listaItemsMenu.add(datosItemPreferenciasNotificacion);

		ListView mDrawerOptions = (ListView) actividadRecibida.findViewById(R.id.menuLateralListaSitios);
		final DrawerLayout mDrawer = (DrawerLayout) actividadRecibida.findViewById(R.id.drawer_layout_lateral);
		
//		mDrawerOptions.setAdapter(new ArrayAdapter(actividadRecibida, android.R.layout.simple_list_item_1, android.R.id.text1,
//				valoresMenuLateral));
		mDrawerOptions.setAdapter(new MenuLateralAdaptador(actividadTmp, listaItemsMenu));
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
					/*
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
					*/
				} else if(indice == IND_PREFERENCIAS) {
					Intent intent = new Intent(actividadRecibida, PreferenciasActivity.class);
					actividadRecibida.startActivity(intent);
				} else if(indice == IND_VER_NOTIFICACIONES) {
					Intent intent = new Intent(actividadRecibida, NotificacionesActivity.class);
					if(categoriaSeleccionada != null) {
						intent.putExtra(NotificacionesActivity.CATEGORIA_NOTIFICACIONES, categoriaSeleccionada.getId());
					}
					actividadRecibida.startActivity(intent);
				} else if(indice == IND_MAPA) {
					/*
					Intent lanzarmapa = new Intent(actividadRecibida, MapaLugaresActivity.class);

					//String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%f,%f&daddr=%f,%f", sourceLatitude, sourceLongitude, destinationLatitude, destinationLongitude);
					String uri = "http://maps.google.com/maps?saddr=40.068782,-6.6562358";
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

					// Vamos a pasar los valores que necesita MapaLugarActivity
					lanzarmapa.putExtra("nombre", "Nombre del sitio");
					lanzarmapa.putExtra("latitud", 40.068782);
					lanzarmapa.putExtra("longitud", -6.6562358);
					lanzarmapa.putExtra(MapaLugaresActivity.ORIGEN, "SITIO");
					lanzarmapa.putExtra(MapaLugaresActivity.ID_RECIBIDO, 9);

					actividadRecibida.startActivity(lanzarmapa);
				} else if(indice == IND_MAPA2) {
					*/
					Intent lanzarmapa = new Intent(actividadRecibida, MapsActivity.class);

					//String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%f,%f&daddr=%f,%f", sourceLatitude, sourceLongitude, destinationLatitude, destinationLongitude);
					String uri = "http://maps.google.com/maps?saddr=40.068782,-6.6562358";
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

					actividadRecibida.startActivity(lanzarmapa);
//				} else if(indice == IND_CREAR_NOTIFICACION) {
//					MainActivity ma = (MainActivity)actividadRecibida;
//					ma.crearNotificaciones(null);
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
