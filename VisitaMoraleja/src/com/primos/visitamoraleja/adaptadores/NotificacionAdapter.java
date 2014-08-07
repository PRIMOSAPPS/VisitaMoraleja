package com.primos.visitamoraleja.adaptadores;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.primos.visitamoraleja.R;
import com.primos.visitamoraleja.contenidos.Notificacion;

public class NotificacionAdapter extends BaseAdapter {
	private final Activity actividad;
	private final List<Notificacion> listaNotificaciones;

	public NotificacionAdapter(Activity actividad, List<Notificacion> listaNotificaciones) {
		this.actividad = actividad;
		this.listaNotificaciones = listaNotificaciones;
	}

	@Override
	public int getCount() {
		return listaNotificaciones.size();
	}

	@Override
	public Object getItem(int position) {
		return listaNotificaciones.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = actividad.getLayoutInflater();
		View view = inflater.inflate(R.layout.notificacion_lista, null, true);
		
		Notificacion notificacion = listaNotificaciones.get(position);
		view.setTag(notificacion);
		
		if(notificacion.isActiva()) {
			view.setBackgroundResource(R.color.notificacion_activa);
		} else {
			view.setBackgroundResource(R.color.notificacion_inactiva);
		}
		
		TextView textTitulo = (TextView)view.findViewById(R.id.notificacionTitulo);
		textTitulo.setText(notificacion.getTitulo());
		TextView textTexto = (TextView)view.findViewById(R.id.notificacionTexto);
		String textoTmp = notificacion.getTexto();
		textoTmp += "\n" + notificacion.getFechaInicioValidez();
		textoTmp += "\n" + notificacion.getFechaFinValidez();
		textTexto.setText(textoTmp);
		ImageButton imageButtonDelete = (ImageButton)view.findViewById(R.id.notificacionDelete);
		imageButtonDelete.setTag(notificacion);
		
		return view;
	}

}
