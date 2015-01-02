package com.primos.visitamoraleja;

import java.util.List;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.util.Log;

import com.primos.visitamoraleja.actualizador.ThreadActualizador;
import com.primos.visitamoraleja.bdsqlite.datasource.CategoriasDataSource;
import com.primos.visitamoraleja.contenidos.Categoria;

public class PreferenciasActivity extends PreferenceActivity {
	public final static String PREFIJO_PREFERENCIA_CATEGORIAS = "categoria_";
	public final static String PREFERENCIA_ACTUALIZAR_POR_CATEGORIAS = "pref_actualizar_categorias";
	public final static String PREFERENCIA_ACTUALIZAR_AUTOMATICAMENTE = "pref_actualizar_automaticamente";
	public final static String PREFERENCIA_CALCULAR_RUTAS_DEFECTO = "pref_calcular_rutas_defecto";
	public final static String PREFERENCIA_CATE_OPCIONES_RUTA = "pref_cate_opciones_ruta";
	public final static String PREFERENCIA_TRANSPORTE_RUTA_DEFECTO = "pref_transporte_ruta_defecto";
	public final static String PREFERENCIA_ACTUALIZAR_AHORA = "actualizar_ahora";
	
	private CategoriasDataSource dataSource;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);

		dataSource = new CategoriasDataSource(this);
		dataSource.open();

		crearOpcionesCategorias();
		
    }
	
	private void crearOpcionesCategorias() {
		SharedPreferences ratePrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
		
		List<Categoria> lstCategorias = dataSource.getAll();
		
		final PreferenceCategory targetCategory = (PreferenceCategory)findPreference("pref_cate_actualizar_categorias");
		for(Categoria categoria : lstCategorias) {

	        CheckBoxPreference checkBoxPreference = new CheckBoxPreference(this);
	        //make sure each key is unique  
	        checkBoxPreference.setTitle(categoria.getDescripcion());
	        String key = PREFIJO_PREFERENCIA_CATEGORIAS + categoria.getId();
	        checkBoxPreference.setKey(key);
	        checkBoxPreference.setChecked(ratePrefs.getBoolean(key, false));

	        targetCategory.addPreference(checkBoxPreference);
		}
		
		final CheckBoxPreference checkBoxPreference = (CheckBoxPreference)findPreference(PREFERENCIA_ACTUALIZAR_POR_CATEGORIAS);
		checkBoxPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				targetCategory.setEnabled(!checkBoxPreference.isChecked());
				return true;
			}
		});
		
		final CheckBoxPreference checkBoxCacularRutas = (CheckBoxPreference)findPreference(PREFERENCIA_CALCULAR_RUTAS_DEFECTO);
		final PreferenceCategory categoriaOpcionesRuta = (PreferenceCategory)findPreference(PREFERENCIA_CATE_OPCIONES_RUTA);
		checkBoxCacularRutas.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				categoriaOpcionesRuta.setEnabled(!checkBoxCacularRutas.isChecked());
				return true;
			}
		});
		
		targetCategory.setEnabled(checkBoxPreference.isChecked());
		Preference actualizarAhora = (Preference)findPreference(PREFERENCIA_ACTUALIZAR_AHORA);
		actualizarAhora.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Log.w("[Preferencias]", "Se actualizan los datos.");
				actualizar();
				return false;
			}
		});

	}
	
	private void actualizar() {
//		AsyncTaskActualizador actualizador = new AsyncTaskActualizador(this, false);
//		actualizador.execute((Void)null);
		
		ThreadActualizador actualizador = new ThreadActualizador(this);
		actualizador.start();
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
