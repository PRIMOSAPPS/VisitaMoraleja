package com.primos.visitamoraleja.views;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
	 * Indica el numero de sitios encontrados a partir de los que se oculta el teclado automaticamente. Si
	 * el numero de sitios encontrados es menor o igual este valor se oculta.
	 */
	private final static int NUM_RESULTADOS_OCULTAR_TECLADO = 4;

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

		List<SitioAutocompletar> lstSitiosAutocompletar = toSitiosAutoCompletar(lstSitios);
		
		final ArrayAdapter<SitioAutocompletar> adapter = new ArrayAdapter<SitioAutocompletar>(getContext(),
                android.R.layout.simple_dropdown_item_1line, lstSitiosAutocompletar);
        textoAutocompletar.setAdapter(adapter);
        
        textoAutocompletar.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				adapter.clear();
				if(s.length() > 1) {
					SitiosDataSource sitiosDataSource = new SitiosDataSource(getContext());
					sitiosDataSource.open();
					List<Sitio> lstResultados = sitiosDataSource.getBusqueda(s.toString());
					List<SitioAutocompletar> lstAutocompletar = toSitiosAutoCompletar(lstResultados);
					for (SitioAutocompletar sitioAutoCompletar : lstAutocompletar) {
						adapter.add(sitioAutoCompletar);
					}
					if (lstAutocompletar.size() <= NUM_RESULTADOS_OCULTAR_TECLADO) {
						textoAutocompletar.dismissDropDown();
					}
					sitiosDataSource.close();
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		// Al seleccionar uno, lo abrimos.
		setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// Ocultamos el dialogo.
				hide();
				SitioAutocompletar sitioAutoCompletar = adapter.getItem(position);
				Sitio sitio = sitioAutoCompletar.getSitio();
		    	Intent intent = new Intent(getContext(), DetalleEventoActivity.class);
		    	intent.putExtra(DetalleEventoActivity.ID_SITIO, sitio.getId());
		    	getContext().startActivity(intent);
			}
			
		});
	}
	
	private List<SitioAutocompletar> toSitiosAutoCompletar(List<Sitio> lstSitios) {
		List<SitioAutocompletar> resul = new ArrayList<>();
		for(Sitio sitio : lstSitios) {
			resul.add(new SitioAutocompletar(sitio));
		}
		return resul;
	}
	
	public void setOnItemClickListener(OnItemClickListener itemClickListener) {
		textoAutocompletar.setOnItemClickListener(itemClickListener);
	}

	@Override
	public void show() {
		super.show();
		// Cuando se muestra, el teclado esta oculto (creo que por ser ArrayAdapter)
		// Forzamos que se muestre
		((AutoCompleteTextViewPrimos)textoAutocompletar).mostrarTeclado();
	}
	

	
}
