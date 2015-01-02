package com.primos.visitamoraleja;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

import com.primos.visitamoraleja.actualizador.AsyncTaskActualizador;
import com.primos.visitamoraleja.actualizador.ThreadActualizador;
import com.primos.visitamoraleja.util.UtilPreferencias;
import com.primos.visitamoraleja.views.DialogoAutocompletar;



public class MainActivity extends ActionBarActivity{
	public final static String ACTUALIZAR = "actualizar";
	
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
		Log.d("Prueba", "En main activity 2");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void mostrar(View view) {
    	Intent intent = new Intent(this, EventoListaNormalActivity.class);
    	intent.putExtra("categoria", (String)view.getTag());
    	startActivity(intent);
	}

	public void mostrarFavoritos(View view) {
    	Intent intent = new Intent(this, EventoListaNormalActivity.class);
    	intent.putExtra("favoritos", "true");
    	startActivity(intent);
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
	            return true;
	        case R.id.actionbar_notificaciones:
	        	Intent i = new Intent(this, NotificacionesActivity.class);
	            startActivity(i);
	            return true;
	        case R.id.actionbar_settings:
	        	mostrarPreferencias();
	            return true;
	    }
	    return false;
	}
	
	private void mostrarPreferencias() {
    	Intent i = new Intent(this, PreferenciasActivity.class);
        startActivity(i);
	}
	
	private void actualizar() {
//		AsyncTaskActualizador actualizador = new AsyncTaskActualizador(this, false);
//		actualizador.execute((Void)null);
		
		ThreadActualizador actualizador = new ThreadActualizador(this);
		actualizador.start();
	}
	
	public void actualizar(View view) {
		actualizar();
//		AsyncTaskActualizador actualizador = new AsyncTaskActualizador(this, true);
//		actualizador.execute((Void)null);
	}

}