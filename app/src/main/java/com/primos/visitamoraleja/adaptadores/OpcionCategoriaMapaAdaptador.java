package com.primos.visitamoraleja.adaptadores;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.primos.visitamoraleja.R;
import com.primos.visitamoraleja.mapas.DatosOpcionCategoriaMapa;
import com.primos.visitamoraleja.mapas.IDatosOpcionMapa;
import com.primos.visitamoraleja.menulateral.DatosItemMenuLateral;

import java.util.List;

public class OpcionCategoriaMapaAdaptador<IDatosOpcionMapaGen extends IDatosOpcionMapa> extends BaseAdapter {
	private final Activity actividad;
	private final List<IDatosOpcionMapaGen> listaItems;

	public OpcionCategoriaMapaAdaptador(Activity actividad,
										List<IDatosOpcionMapaGen> listaItems) {
		super();
		this.actividad = actividad;
		this.listaItems = listaItems;
	}

	@Override
	public int getCount() {
		return listaItems.size();
	}

	@Override
	public Object getItem(int arg0) {
		return listaItems.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = actividad.getLayoutInflater();
		View view = inflater.inflate(R.layout.opcion_categoria_mapa, null, true);
		
		ImageView imageView = (ImageView)view.findViewById(R.id.imagen_opcion_mapa);
		TextView textView = (TextView)view.findViewById(R.id.titulo_opcion_mapa);

		IDatosOpcionMapaGen datosItem = listaItems.get(position);
		textView.setText(datosItem.getTextoMenu());
		Resources resources = actividad.getResources();
//		int identificadorImagen = resources.getIdentifier(datosItem.getNombreIcono(), "drawable", actividad.getPackageName());
		int identificadorImagen = datosItem.getIdentificadorIcono();
		Drawable drawable = resources.getDrawable(identificadorImagen);
		imageView.setImageDrawable(drawable);

		view.setTag(datosItem);
		return view;
	}

}
