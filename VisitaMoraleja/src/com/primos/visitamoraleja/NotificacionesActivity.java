package com.primos.visitamoraleja;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.primos.visitamoraleja.adaptadores.NotificacionAdapter;
import com.primos.visitamoraleja.bdsqlite.datasource.NotificacionesDataSource;
import com.primos.visitamoraleja.contenidos.Notificacion;

public class NotificacionesActivity extends ActionBarListActivity {
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notificaciones, menu);
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
}
