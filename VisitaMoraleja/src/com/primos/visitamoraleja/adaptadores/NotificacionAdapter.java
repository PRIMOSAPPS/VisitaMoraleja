package com.primos.visitamoraleja.adaptadores;

import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.primos.visitamoraleja.R;
import com.primos.visitamoraleja.almacenamiento.AlmacenamientoFactory;
import com.primos.visitamoraleja.almacenamiento.ItfAlmacenamiento;
import com.primos.visitamoraleja.constantes.Constantes;
import com.primos.visitamoraleja.contenidos.Notificacion;
import com.primos.visitamoraleja.contenidos.NotificacionSitio;
import com.primos.visitamoraleja.contenidos.Sitio;

public class NotificacionAdapter extends BaseAdapter {
	private final Activity actividad;
	private final List<NotificacionSitio> listaNotifSitios;
	private ItfAlmacenamiento almacenamiento;

	public NotificacionAdapter(Activity actividad, List<NotificacionSitio> listaNotifSitios) {
		this.actividad = actividad;
		this.listaNotifSitios = listaNotifSitios;
		almacenamiento = AlmacenamientoFactory.getAlmacenamiento(actividad);
	}

	@Override
	public int getCount() {
		return listaNotifSitios.size();
	}

	@Override
	public Object getItem(int position) {
		return listaNotifSitios.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = actividad.getLayoutInflater();
		View view = inflater.inflate(R.layout.notificacion_lista, null, true);
		
		NotificacionSitio notificacionSitio = listaNotifSitios.get(position);
		Notificacion notificacion = notificacionSitio.getNotificacion();
		Sitio sitio = notificacionSitio.getSitio();
		view.setTag(notificacion);
		GradientDrawable background = (GradientDrawable)view.getBackground();
		
		ImageButton imageButtonDelete = (ImageButton)view.findViewById(R.id.notificacionDelete);
		imageButtonDelete.setTag(notificacion);
		
		int resColorBorde;
		int resColorFondo;
		if(notificacion.isActiva()) {
			resColorFondo = R.color.notificacion_activa;
			resColorBorde = R.color.verde;
//			view.setBackgroundResource(R.color.notificacion_activa);
		} else {
			resColorFondo = R.color.notificacion_inactiva;
			resColorBorde = R.color.rojo;
//			view.setBackgroundResource(R.color.notificacion_inactiva);
		}
		
		int colorBorde = actividad.getResources().getColor(resColorBorde);
		int colorFondo = actividad.getResources().getColor(resColorFondo);
		background.setStroke(2, colorBorde);
		background.setColor(colorFondo);
		
		TextView textNombreSitio = (TextView)view.findViewById(R.id.notificacionNombreSitio);
		textNombreSitio.setText(sitio.getNombre());
		
		Bitmap bitmap = almacenamiento.getImagenSitio(sitio.getId(), sitio.getNombreLogotipo());
		if(bitmap != null) {
			ImageButton imageButton = (ImageButton)view.findViewById(R.id.notificacionIconoSitio);
			int width = imageButtonDelete.getDrawable().getIntrinsicWidth();
			int height = imageButtonDelete.getDrawable().getIntrinsicHeight();
			bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
			imageButton.setImageBitmap(bitmap);
		}

		
		TextView textTitulo = (TextView)view.findViewById(R.id.notificacionTitulo);
		textTitulo.setText(notificacion.getTitulo());
		final WebView webViewTexto = (WebView)view.findViewById(R.id.notificacionTexto);
		webViewTexto.setVerticalScrollBarEnabled(true);
		webViewTexto.setHorizontalScrollBarEnabled(true);
		webViewTexto.requestFocusFromTouch();

		webViewTexto.getSettings().setJavaScriptEnabled(false);
		webViewTexto.getSettings().setUseWideViewPort(true);                                                         
        webViewTexto.getSettings().setLoadWithOverviewMode(true);
//        webViewTexto.getSettings().setBuiltInZoomControls(true);
        StringBuilder sBuilder = new StringBuilder("<div stile='height: 100px;'>");
        sBuilder.append(notificacion.getTexto());
        sBuilder.append("</div>");
//		textoTmp += "\n" + notificacion.getFechaInicioValidez();
//		textoTmp += "\n" + notificacion.getFechaFinValidez();
		webViewTexto.loadDataWithBaseURL(null, sBuilder.toString(), Constantes.mimeType, Constantes.encoding, null);
		
		webViewTexto.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
            	webViewTexto.scrollTo(0,0);
            	webViewTexto.setInitialScale(100);
            }
        });
		
		return view;
	}

}
