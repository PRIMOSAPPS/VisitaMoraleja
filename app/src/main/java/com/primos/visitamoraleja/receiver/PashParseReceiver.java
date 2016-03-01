package com.primos.visitamoraleja.receiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import com.primos.visitamoraleja.DetalleNotificacionActivity;
import com.primos.visitamoraleja.NotificacionesActivity;
import com.primos.visitamoraleja.R;
import com.primos.visitamoraleja.bdsqlite.datasource.NotificacionesDataSource;
import com.primos.visitamoraleja.bdsqlite.datasource.SitiosDataSource;
import com.primos.visitamoraleja.contenidos.Notificacion;
import com.primos.visitamoraleja.contenidos.Sitio;
import com.primos.visitamoraleja.util.UtilPreferencias;

public class PashParseReceiver extends BroadcastReceiver {
	private static final String TAG = "PushSenseiReceiver";
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			Notificacion notificacion = getNotificacion(intent);
			
			Log.d(TAG, "Recibida una notificacion.");
			
			notificacionToBD(context, notificacion);

			LanzadorNotificaciones lanzador = new LanzadorNotificaciones();
			lanzador.lanzarNotificacion(context, notificacion);

		} catch (JSONException e) {
			Log.e(TAG, "JSONException: " + e.getMessage(), e);
		} catch (ParseException e) {
			Log.e(TAG, "ParseException: " + e.getMessage(), e);
		}
	}

	private Notificacion getNotificacion(Intent intent) throws JSONException, ParseException {
		JSONObject json = new JSONObject(intent.getExtras().getString(
				"com.parse.Data"));
		Notificacion notificacion = new Notificacion();
		notificacion.setId(json.getLong("id"));
		notificacion.setIdSitio(json.getLong("idSitio"));
		notificacion.setIdCategoria(json.getLong("idCategoria"));
		notificacion.setTitulo(json.getString("titulo"));
		notificacion.setTexto(json.getString("texto"));
		String strFechaInicio = json.getString("fiv");
		notificacion.setFechaInicioValidez(dateFormat.parse(strFechaInicio));
		String strFechaFin = json.getString("ffv");
		notificacion.setFechaFinValidez(dateFormat.parse(strFechaFin));
		Log.w(TAG, "Recibida notificacion con fecha inicio: " + strFechaInicio + " y fecha de fin: " + strFechaFin);
		Log.w(TAG, "FECHAS PARSEADAS fecha inicio: " + notificacion.getFechaInicioValidez() + " y fecha de fin: " + notificacion.getFechaFinValidez());
		
		return notificacion;
	}
	
	private void notificacionToBD(Context context, Notificacion notificacion) {
		NotificacionesDataSource dataSource = new NotificacionesDataSource(context);
		dataSource.open();
		dataSource.insertar(notificacion);
		dataSource.close();
	}
}
