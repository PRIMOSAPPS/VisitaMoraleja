package com.primos.visitamoraleja.receiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.primos.visitamoraleja.NotificacionesActivity;
import com.primos.visitamoraleja.R;
import com.primos.visitamoraleja.bdsqlite.datasource.NotificacionesDataSource;
import com.primos.visitamoraleja.contenidos.Notificacion;

public class PashParseReceiver extends BroadcastReceiver {
	private static final String TAG = "PushSenseiReceiver";
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH");

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			Notificacion notificacion = getNotificacion(intent);
			
			Log.d(TAG, "Recibida una notificacion.");
			
			notificacionToBD(context, notificacion);
			
			Intent resultIntent = new Intent(context, NotificacionesActivity.class);
			resultIntent.putExtra(NotificacionesActivity.NOTIFICACION, notificacion);
            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            context,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
			
            Log.d(TAG, "Recibida una notificacion: " + notificacion.getTexto());
            
            //Esto hace posible crear la notificación
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_action_notificacion)
                            .setContentTitle(notificacion.getTitulo())
                            .setContentText(notificacion.getTexto())
                            .setContentIntent(resultPendingIntent)
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setAutoCancel(true);

            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(1, mBuilder.build());
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
		notificacion.setTitulo(json.getString("titulo"));
		notificacion.setTexto(json.getString("texto"));
		String strFechaInicio = json.getString("fiv");
		notificacion.setFechaInicioValidez(dateFormat.parse(strFechaInicio));
		String strFechaFin = json.getString("ffv");
		notificacion.setFechaFinValidez(dateFormat.parse(strFechaFin));
		
		return notificacion;
	}
	
	private void notificacionToBD(Context context, Notificacion notificacion) {
		NotificacionesDataSource dataSource = new NotificacionesDataSource(context);
		dataSource.open();
		dataSource.insertar(notificacion);
		dataSource.close();
	}
}
