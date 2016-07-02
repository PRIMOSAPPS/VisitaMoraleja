package com.primos.visitamoraleja;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.primos.visitamoraleja.actividades.eventos.AbstractEventos;
import com.primos.visitamoraleja.actividades.eventos.EventoPrincipalActivity;
import com.primos.visitamoraleja.adaptadores.EventoAdaptador;
import com.primos.visitamoraleja.adaptadores.SitioAdaptador;
import com.primos.visitamoraleja.almacenamiento.ItfAlmacenamiento;
import com.primos.visitamoraleja.bdsqlite.datasource.EventosDataSource;
import com.primos.visitamoraleja.contenidos.Categoria;
import com.primos.visitamoraleja.contenidos.Evento;
import com.primos.visitamoraleja.contenidos.Sitio;
import com.primos.visitamoraleja.permisos.Permisos;

import java.util.List;

public class ListaEventosActivity extends AbstractEventos {
	public final static String ID_SITIO = "id_sitio";
	
	protected EventosDataSource dataSource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_eventos);
		dataSource = new EventosDataSource(this);
		dataSource.open();

		Permisos permisosUtil = new Permisos();
		if(!permisosUtil.preguntarPermisos(this, ItfAlmacenamiento.permisosNecesarios)) {
			cargar();
		}

		initMenuLateral();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch (requestCode) {
			case Permisos.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
//				Permisos permisos = new Permisos();
//				boolean concedidos = permisos.checkSiPermisosConcedidos(permissions, grantResults);
//				if(concedidos) {
					cargar();
//				} else {
//					Toast.makeText(this, R.string.permisos_necesarios, Toast.LENGTH_SHORT)
//							.show();
//				}
			}
			break;
			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}

	public void cargar() {
		List<Evento> lista = dataSource.getAll();
		setTitulo(getTextoTitulo());
		setListAdapter(new EventoAdaptador(this, lista));
	}

	private String getTextoTitulo() {
		return "Eventos";
	}

	public void mostrarDetalle(View view) {
		Evento evento = (Evento)view.getTag();

		Intent intent = new Intent(this, EventoPrincipalActivity.class);
		intent.putExtra(EventoPrincipalActivity.ID_EVENTO, evento.getId());
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
