package com.primos.visitamoraleja.adaptadores;

import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.primos.visitamoraleja.R;
import com.primos.visitamoraleja.almacenamiento.AlmacenamientoFactory;
import com.primos.visitamoraleja.almacenamiento.ItfAlmacenamiento;
import com.primos.visitamoraleja.contenidos.Sitio;

/**
 * <b>ESTA CLASE SE CORRESPONDE CON PARTE DE LA VISTA DE LA APLICACION</b>
 * Adaptador del contenido de un Sitio a cada View correspondiente al ListView que muestra la lista de sitios. 
 * @author h
 *
 */
public class SitioAdaptador extends EventoSitioAdaptador<Sitio> {

	public SitioAdaptador(Activity actividad, List<Sitio> listaSitios) {
		super(actividad, listaSitios);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = actividad.getLayoutInflater();
		View view = inflater.inflate(R.layout.evento_lista_normal, null, true);

		List<Sitio> listaSitios = (List<Sitio>)listaObjetos;
		Sitio sitio = listaSitios.get(position);
		
		view.setTag(sitio);
        TextView textNombreSitio = (TextView)view.findViewById(R.id.textNombreSitio);
		textNombreSitio.setText(sitio.getNombre());
		
		ImageView imagen = (ImageView)view.findViewById(R.id.imagenListaEventos);
		//TextView textDescripcionCorta = (TextView)view.findViewById(R.id.textDescripcion);
		//textDescripcionCorta.setText(sitio.getTextoCorto1());
		ItfAlmacenamiento almacenamiento = AlmacenamientoFactory.getAlmacenamiento(actividad);
		Bitmap bitmap = almacenamiento.getImagenSitio(sitio.getId(), sitio.getNombreLogotipo());
		imagen.setImageBitmap(bitmap);
		
//		LayoutParams params = new LayoutParams(LayoutParams.fill_parent,
//				15 + (position * 5));
//		view.setLayoutParams(params);
		return view;
	}

}
