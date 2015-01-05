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
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import com.primos.visitamoraleja.NotificacionesActivity;
import com.primos.visitamoraleja.R;
import com.primos.visitamoraleja.bdsqlite.datasource.NotificacionesDataSource;
import com.primos.visitamoraleja.bdsqlite.datasource.SitiosDataSource;
import com.primos.visitamoraleja.contenidos.Notificacion;
import com.primos.visitamoraleja.contenidos.Sitio;

public class PashParseReceiver extends BroadcastReceiver {
	private static final String TAG = "PushSenseiReceiver";
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

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
                            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT
                    );
			
            Log.d(TAG, "Recibida una notificacion: " + notificacion.getTexto());
            
            long[] patronVibracion = {500};
            //Esto hace posible crear la notificación
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context);
            
            Sitio sitio = getSitio(context, notificacion.getIdSitio());

            NotificationCompat.InboxStyle inboxStyle =
                    new NotificationCompat.InboxStyle();
            inboxStyle.setBigContentTitle(sitio.getNombre());
            Spanned spanTitulo = Html.fromHtml("<b>" + notificacion.getTitulo() + "</b>");
            inboxStyle.addLine(spanTitulo);
            Spanned spanTexto = Html.fromHtml("<i>" + notificacion.getTexto() + "</i>");
            inboxStyle.addLine(spanTexto);
            
//            inboxStyle.setSummaryText("setSummaryText");
            mBuilder.setStyle(inboxStyle);
            
            mBuilder.setSmallIcon(R.drawable.ic_action_notificacion)
                .setContentTitle(notificacion.getTitulo())
                .setContentText(notificacion.getTexto())
                .setContentIntent(resultPendingIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(patronVibracion)
                .setAutoCancel(true);


            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = mBuilder.build();
            mNotificationManager.notify(1, notification);
		} catch (JSONException e) {
			Log.e(TAG, "JSONException: " + e.getMessage(), e);
		} catch (ParseException e) {
			Log.e(TAG, "ParseException: " + e.getMessage(), e);
		}
	}
	
	private Sitio getSitio(Context context, long idSitio) {
		Sitio resul = null;
		SitiosDataSource sitiosDS = new SitiosDataSource(context);
		sitiosDS.open();
		resul = sitiosDS.getById(idSitio);
		sitiosDS.close();
		
		return resul;
	}
	
	private Notificacion getNotificacion(Intent intent) throws JSONException, ParseException {
		JSONObject json = new JSONObject(intent.getExtras().getString(
				"com.parse.Data"));
		Notificacion notificacion = new Notificacion();
		notificacion.setId(json.getLong("id"));
		notificacion.setIdSitio(json.getLong("idSitio"));
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
