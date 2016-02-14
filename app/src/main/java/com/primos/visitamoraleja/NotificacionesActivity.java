package com.primos.visitamoraleja;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.primos.visitamoraleja.adaptadores.NotificacionAdapter;
import com.primos.visitamoraleja.bdsqlite.datasource.NotificacionesDataSource;
import com.primos.visitamoraleja.bdsqlite.datasource.SitiosDataSource;
import com.primos.visitamoraleja.contenidos.Notificacion;
import com.primos.visitamoraleja.contenidos.NotificacionSitio;
import com.primos.visitamoraleja.contenidos.Sitio;
import com.primos.visitamoraleja.menulateral.ConfigMenuLateral;

public class NotificacionesActivity extends ActionBarListActivity {
	public final static String NOTIFICACION = "notificacion";
	public final static String CATEGORIA_NOTIFICACIONES = "categoriaNotificaciones";
	
	private NotificacionesDataSource dataSource = null;
	private SitiosDataSource dataSourceSitios = null;
	private ListView mListView = null;
	
	private DrawerLayout mDrawer;
	private ListView mDrawerOptions;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notificaciones);
		
		dataSource = new NotificacionesDataSource(this);
		dataSource.open();
		dataSourceSitios = new SitiosDataSource(this);
		dataSourceSitios.open();
		
		Bundle extras = getIntent().getExtras();
		// Si se recibe una notificacion solo se muestra esta
		List<NotificacionSitio> lstNotificacionesSitios = null;
		if(extras != null) {
			if(extras.containsKey(NOTIFICACION)) {
				Notificacion notificacion = (Notificacion)extras.get(NOTIFICACION);
				lstNotificacionesSitios = getlistaNotificacionesSitio(notificacion);
			} else if(extras.containsKey(CATEGORIA_NOTIFICACIONES)) {
				long idCategoria = extras.getLong(CATEGORIA_NOTIFICACIONES);
				List<Notificacion> lstNotificaciones = dataSource.getByCategoria(idCategoria);
				lstNotificacionesSitios = getlistaNotificacionesSitio(lstNotificaciones);
			}
		} else {
			dataSource.eliminarPasadas();
			List<Notificacion> lstNotificaciones = dataSource.getAll();
			lstNotificacionesSitios = getlistaNotificacionesSitio(lstNotificaciones);
		}
		
		setListAdapter(new NotificacionAdapter(this, lstNotificacionesSitios));
		
		mDrawerOptions = (ListView) findViewById(R.id.menuLateralListaSitios);
		mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout_lateral);
		ConfigMenuLateral cml = new ConfigMenuLateral(this, null, true);
		cml.iniciarMenuLateral();

	}
	
	private List<NotificacionSitio> getlistaNotificacionesSitio(Notificacion notificacion) {
		List<NotificacionSitio> lstNotificacionesSitios = new ArrayList<>();
		Sitio sitio = dataSourceSitios.getById(notificacion.getIdSitio());
		NotificacionSitio notifSitio = new NotificacionSitio(notificacion, sitio);
		lstNotificacionesSitios.add(notifSitio);
		return lstNotificacionesSitios;
	}
	
	private List<NotificacionSitio> getlistaNotificacionesSitio(List<Notificacion> lstNotificaciones) {
		List<NotificacionSitio> lstNotificacionesSitios = new ArrayList<>();
		for(Notificacion notificacion : lstNotificaciones) {
			Sitio sitio = dataSourceSitios.getById(notificacion.getIdSitio());
			NotificacionSitio notifSitio = new NotificacionSitio(notificacion, sitio);
			lstNotificacionesSitios.add(notifSitio);
		}
		return lstNotificacionesSitios;
	}
	
	public void mostrarDetalleSitio(View view) {
		Notificacion notificacion = (Notificacion)view.getTag();

		long idSitio = notificacion.getIdSitio();
		if(idSitio > -1) {
			Intent intent = new Intent(this, DetalleEventoActivity.class);
			intent.putExtra(DetalleEventoActivity.ID_SITIO, idSitio);
	    	startActivity(intent);
		}
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
		switch (item.getItemId()) {
			case R.id.actionbar_inicio:
	        	Intent i = new Intent(this, MainActivity.class);
	        	i.putExtra(MainActivity.ACTUALIZAR, false);
	            startActivity(i);
				return true;
			case R.id.actionbar_menu_lateral:
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

	public void borrarNotificacion(final View view) {
		
		AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
		dialogo1.setTitle(R.string.txt_aviso);
        dialogo1.setMessage(R.string.dlg_notif_txt_aviso);
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton(R.string.txt_aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
            	Notificacion notificacion = (Notificacion)view.getTag();
            	borrarNotificacion(notificacion);
            }
        });
        dialogo1.setNegativeButton(R.string.txt_cancelar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
            }
        });
        dialogo1.show();
	}
	
	private void borrarNotificacion(Notificacion notificacion) {
		dataSource.delete(notificacion);
		List<Notificacion> lstNotificaciones = dataSource.getAll();
		List<NotificacionSitio> lstNotifSitios = getlistaNotificacionesSitio(lstNotificaciones);
		setListAdapter(new NotificacionAdapter(this, lstNotifSitios));
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
		dataSourceSitios.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		dataSource.close();
		dataSourceSitios.close();
		super.onPause();
	}
}
