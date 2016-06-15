package com.primos.visitamoraleja.adaptadores;

import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.primos.visitamoraleja.R;
import com.primos.visitamoraleja.almacenamiento.AlmacenamientoFactory;
import com.primos.visitamoraleja.almacenamiento.ItfAlmacenamiento;
import com.primos.visitamoraleja.contenidos.Evento;

/**
 * <b>ESTA CLASE SE CORRESPONDE CON PARTE DE LA VISTA DE LA APLICACION</b>
 * Adaptador del contenido de un Sitio a cada View correspondiente al ListView que muestra la lista de sitios. 
 * @author h
 *
 */
public class EventoAdaptador extends EventoSitioAdaptador<Evento> {

	public EventoAdaptador(Activity actividad, List<Evento> listaEventos) {
		super(actividad, listaEventos);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = actividad.getLayoutInflater();
		View view = inflater.inflate(R.layout.evento_lista_normal, null, true);

		List<Evento> listaEventos = (List<Evento>)listaObjetos;
		Evento evento = listaEventos.get(position);
		
		actualizaGradiente(view, evento);
		
		view.setTag(evento);
        TextView textNombreSitio = (TextView)view.findViewById(R.id.textNombreSitio);
		textNombreSitio.setText(evento.getNombre());
		
		ImageView imagen = (ImageView)view.findViewById(R.id.imagenListaEventos);
		//TextView textDescripcionCorta = (TextView)view.findViewById(R.id.textDescripcion);
		//textDescripcionCorta.setText(sitio.getTextoCorto1());
		ItfAlmacenamiento almacenamiento = AlmacenamientoFactory.getAlmacenamiento(actividad);
		Bitmap bitmap = almacenamiento.getImagenEvento(evento.getId(), evento.getNombreIcono());
		imagen.setImageBitmap(bitmap);
		
//		LayoutParams params = new LayoutParams(LayoutParams.fill_parent,
//				15 + (position * 5));
//		view.setLayoutParams(params);
		return view;
	}
	
	private void actualizaGradiente(View view, Evento evento) {
		int backgroundResource = R.drawable.gradiente_evento_inactivo;
		if(evento.isActivoPorFecha()) {
			backgroundResource = R.drawable.gradiente_evento_activo;
		}
		view.setBackgroundResource(backgroundResource);
	}

}
