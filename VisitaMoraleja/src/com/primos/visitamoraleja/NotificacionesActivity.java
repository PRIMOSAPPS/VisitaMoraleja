package com.primos.visitamoraleja;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.primos.visitamoraleja.adaptadores.NotificacionAdapter;
import com.primos.visitamoraleja.bdsqlite.datasource.NotificacionesDataSource;
import com.primos.visitamoraleja.contenidos.Notificacion;

public class NotificacionesActivity extends Activity {
	public final static String NOTIFICACION = "notificacion";
	
	private NotificacionesDataSource dataSource = null;
	private ListView mListView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notificaciones);
		
		dataSource = new NotificacionesDataSource(this);
		dataSource.open();
		
		Bundle extras = getIntent().getExtras();
		// Si se recibe una notificacion solo se muestra esta
		List<Notificacion> lstNotificaciones = null;
		if(extras != null && extras.containsKey(NOTIFICACION)) {
			Notificacion notificacion = (Notificacion)extras.get(NOTIFICACION);
			lstNotificaciones = new ArrayList<>();
			lstNotificaciones.add(notificacion);
		} else {
			dataSource.eliminarPasadas();
			lstNotificaciones = dataSource.getAll();
		}
		
		setListAdapter(new NotificacionAdapter(this, lstNotificaciones));
	}
	
	public void borrarNotificacion(View view) {
		Notificacion notificacion = (Notificacion)view.getTag();
		
		dataSource.delete(notificacion);
		setListAdapter(new NotificacionAdapter(this, dataSource.getAll()));
		findViewById(R.id.lvNotificaciones).invalidate();
	}
	
	protected ListView getListView() {
		if (mListView == null) {
			mListView = (ListView) findViewById(R.id.lvNotificaciones);
		}
		return mListView;
	}

	protected void setListAdapter(ListAdapter adapter) {
		getListView().setAdapter(adapter);
	}
	
	@Override
	protected void onResume() {
		dataSource.open();
		dataSource.eliminarPasadas();
		super.onResume();
	}

	@Override
	protected void onPause() {
		dataSource.close();
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(this, MainActivity.class);
    	startActivity(intent);
	}
}
