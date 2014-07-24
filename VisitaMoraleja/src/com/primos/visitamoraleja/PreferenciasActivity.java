package com.primos.visitamoraleja;

import java.util.List;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;

import com.primos.visitamoraleja.R;
import com.primos.visitamoraleja.bdsqlite.datasource.CategoriasDataSource;
import com.primos.visitamoraleja.contenidos.Categoria;

public class PreferenciasActivity extends PreferenceActivity {
	public final static String PREFIJO_PREFERENCIA_CATEGORIAS = "categoria_";
	public final static String PREFERENCIA_ACTUALIZAR_POR_CATEGORIAS = "pref_actualizar_categorias";
	public final static String PREFERENCIA_ACTUALIZAR_AUTOMATICAMENTE = "pref_actualizar_automaticamente";
	
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
	        checkBoxPreference.setTitle(categoria.getNombre());
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
		targetCategory.setEnabled(checkBoxPreference.isChecked());

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
