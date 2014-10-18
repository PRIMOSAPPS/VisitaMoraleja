package com.primos.visitamoraleja.views;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.primos.visitamoraleja.DetalleEventoActivity;
import com.primos.visitamoraleja.R;
import com.primos.visitamoraleja.bdsqlite.datasource.SitiosDataSource;
import com.primos.visitamoraleja.contenidos.Sitio;

public class DialogoAutocompletar extends Dialog {
	private AutoCompleteTextView textoAutocompletar;

	/**
	 * Clase creada para no tener que cambiar el metodo toString de la clase Sitio
	 * @author h
	 *
	 */
	private class SitioAutocompletar {
		private Sitio sitio;
		
		SitioAutocompletar(Sitio sitio) {
			this.sitio = sitio;
		}

		@Override
		public String toString() {
			return sitio.getNombre();
		}

		public Sitio getSitio() {
			return sitio;
		}
		
	}

	public DialogoAutocompletar(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public DialogoAutocompletar(Context context, int theme) {
		super(context, theme);
	}

	public DialogoAutocompletar(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.title_dialog_buscador);
		setContentView(R.layout.busqueda_autocompletar);
		textoAutocompletar = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextViewBusqueda);
		
		SitiosDataSource sitiosDataSource = new SitiosDataSource(getContext());
		sitiosDataSource.open();
		List<Sitio> lstSitios = sitiosDataSource.getAll();
		sitiosDataSource.close();

		List<SitioAutocompletar> lstSitiosAutocompletar = new ArrayList<>();
		for(Sitio sitio : lstSitios) {
			lstSitiosAutocompletar.add(new SitioAutocompletar(sitio));
		}
		
		final ArrayAdapter<SitioAutocompletar> adapter = new ArrayAdapter<SitioAutocompletar>(getContext(),
                android.R.layout.simple_dropdown_item_1line, (SitioAutocompletar[])lstSitiosAutocompletar.toArray(new SitioAutocompletar[0]));
        textoAutocompletar.setAdapter(adapter);
		// Al seleccionar uno, lo abrimos.
		setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				SitioAutocompletar sitioAutoCompletar = adapter.getItem(position);
				Sitio sitio = sitioAutoCompletar.getSitio();
		    	Intent intent = new Intent(getContext(), DetalleEventoActivity.class);
		    	intent.putExtra(DetalleEventoActivity.ID_SITIO, sitio.getId());
		    	getContext().startActivity(intent);
			}
			
		});
	}
	
	public void setOnItemClickListener(OnItemClickListener itemClickListener) {
		textoAutocompletar.setOnItemClickListener(itemClickListener);
	}
	

	
}
