package com.primos.visitamoraleja;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.ListView;
import android.widget.Toast;

import com.primos.visitamoraleja.actualizador.ThreadActualizador;
import com.primos.visitamoraleja.almacenamiento.ItfAlmacenamiento;
import com.primos.visitamoraleja.menulateral.ConfigMenuLateral;
import com.primos.visitamoraleja.permisos.Permisos;
import com.primos.visitamoraleja.util.UtilPreferencias;
import com.primos.visitamoraleja.views.DialogoAutocompletar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends ActionBarActivity implements IPrimosActividyLifeCycleEmisor {
	public final static String ACTUALIZAR = "actualizar";
	
	private DrawerLayout mDrawer;
	private ListView mDrawerOptions;
	private List<IPrimosActivityLifecycleCallbacks> lstActivityLifeCicleListener = new ArrayList<>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("MainActivity", "Inicio en onCreate");
		//setContentView(R.layout.activity_main);
		//setContentView(R.layout.fragment_main);
		setContentView(R.layout.principal);
		
		// Se comprueba si es necesario actualizar. Si venimos desde otra
		// activity, no se realiza la actualizacion.
		Bundle extras = getIntent().getExtras();
		boolean actualizar = true;
		if(extras != null && extras.containsKey(ACTUALIZAR)) {
			actualizar = (boolean) extras.get(ACTUALIZAR);
		}

		if(actualizar && UtilPreferencias.actualizacionAutomatica(this)) {
			Log.i("MainActivity", "Se realiza la actualizacion por estar activada");
			actualizar();
		}

		mDrawerOptions = (ListView) findViewById(R.id.menuLateralListaSitios);
		mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout_lateral);
		ConfigMenuLateral cml = new ConfigMenuLateral(this, null, true);
		cml.iniciarMenuLateral();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void mostrar(View view) {
    	Intent intent = new Intent(this, EventoListaNormalActivity.class);
    	intent.putExtra(EventoListaNormalActivity.CATEGORIA, (String)view.getTag());
    	startActivity(intent);
	}

	public void mostrarEventos(View view) {
    	Intent intent = new Intent(this, ListaEventosActivity.class);
    	startActivity(intent);
	}
	
	public void mostrarAcercade(View view) {
		Intent i = new Intent(this,AcercaDeActivity.class);
		startActivity(i);
	}

	

	private void buscar() {
		DialogoAutocompletar myDialog = new DialogoAutocompletar(this);
		LayoutParams wmlp = myDialog.getWindow().getAttributes();
		wmlp.gravity = Gravity.TOP;
		wmlp.y = 50;
		
        myDialog.show();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.actionbar_buscar:
	        	buscar();
	            return true;
	        case R.id.actionbar_share:
	        	Toast.makeText(this, "Se ha seleccionado COMPARTIR.", Toast.LENGTH_LONG).show();
	        case R.id.actionbar_settings:
	        	mostrarPreferencias();
	            return true;
			case R.id.actionbar_menu_lateral:
//				mostrarNotificaciones();
				if (mDrawer.isDrawerOpen(mDrawerOptions)){
					mDrawer.closeDrawers();
				}else{
					mDrawer.openDrawer(mDrawerOptions);
				}
				return true;
	    }
	    return false;
	}
	
	private void mostrarPreferencias() {
    	Intent i = new Intent(this, PreferenciasActivity.class);
        startActivity(i);
	}
	
	
	private void actualizar() {
		Permisos permisosUtil = new Permisos();
		if(!permisosUtil.preguntarPermisos(this, ItfAlmacenamiento.permisosNecesarios)) {
			lanzarActualizacion();
		}
	}

	private void lanzarActualizacion() {
		ThreadActualizador actualizador = new ThreadActualizador(this);
		actualizador.start();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch (requestCode) {
			case Permisos.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {

				Permisos permisos = new Permisos();
				boolean concedidos = permisos.checkSiPermisosConcedidos(permissions, grantResults);
				if(concedidos) {
					lanzarActualizacion();
				} else {
					Toast.makeText(this, R.string.permisos_necesarios, Toast.LENGTH_SHORT)
							.show();
				}

				/*
				Map<String, Integer> perms = new HashMap<String, Integer>();
				// Initial
				perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
				perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
				// Fill with results
				for (int i = 0; i < permissions.length; i++) {
					perms.put(permissions[i], grantResults[i]);
				}
				if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
						&& perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
					// All Permissions Granted
					lanzarActualizacion();
				} else {
					// Permission Denied
					Toast.makeText(MainActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
							.show();
				}
				*/
			}
			break;
			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}


	public void actualizar(View view) {
		actualizar();
//		AsyncTaskActualizador actualizador = new AsyncTaskActualizador(this, true);
//		actualizador.execute((Void)null);
	}
	
	public void registrar(IPrimosActivityLifecycleCallbacks activityLifeCicleListener) {
		lstActivityLifeCicleListener.add(activityLifeCicleListener);
	}
	
	public void deregistrar(IPrimosActivityLifecycleCallbacks activityLifeCicleListener) {
		lstActivityLifeCicleListener.remove(activityLifeCicleListener);
	}

	@Override
	protected void onPause() {
		super.onPause();
		for(IPrimosActivityLifecycleCallbacks listener : lstActivityLifeCicleListener) {
			listener.onActivityPaused(this);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		for(IPrimosActivityLifecycleCallbacks listener : lstActivityLifeCicleListener) {
			listener.onActivityResumed(this);
		}
	}

	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();
		for(IPrimosActivityLifecycleCallbacks listener : lstActivityLifeCicleListener) {
			listener.onActivityResumed(this);
		}
	}

}