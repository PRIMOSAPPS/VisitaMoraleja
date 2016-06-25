package com.primos.visitamoraleja.adaptadores;

import java.util.List;

import android.app.Activity;
import android.widget.BaseAdapter;

public abstract class AbstractAdaptador<T> extends BaseAdapter {
	protected final Activity actividad;
	protected final List<T> listaObjetos;
	
	public AbstractAdaptador(Activity actividad, List<T> listaObjetos) {
		this.actividad = actividad;
		this.listaObjetos = listaObjetos;
	}

	@Override
	public int getCount() {
		return listaObjetos.size();
	}

	@Override
	public Object getItem(int position) {
		return listaObjetos.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}
}
