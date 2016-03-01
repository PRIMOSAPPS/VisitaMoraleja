package com.primos.visitamoraleja.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.primos.visitamoraleja.DetalleNotificacionActivity;
import com.primos.visitamoraleja.NotificacionesActivity;
import com.primos.visitamoraleja.R;
import com.primos.visitamoraleja.bdsqlite.datasource.SitiosDataSource;
import com.primos.visitamoraleja.contenidos.Notificacion;
import com.primos.visitamoraleja.contenidos.Sitio;
import com.primos.visitamoraleja.util.UtilPreferencias;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by h on 26/02/16.
 */
public class LanzadorNotificaciones {
    private static final String TAG = "LanzadorNotificaciones";
    private final static String GRUPO_NOTIFICACIONES_DIME_MONESTERIO = "GRUPO_NOTIFICACIONES_VISITA_MORALEJA";
    private static int idNotificaciones = 0;

    public void lanzarNotificacion(Context context, Notificacion notificacion) {
            Intent resultIntent = new Intent(context, DetalleNotificacionActivity.class);
        resultIntent.putExtra(DetalleNotificacionActivity.ID_NOTIFICACION, notificacion.getId());
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        Log.d(TAG, "Recibida una notificacion: " + notificacion.getTexto());

        long[] patronVibracion = {2000, 1000};
        //Esto hace posible crear la notificación
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context);

        Sitio sitio = getSitio(context, notificacion.getIdSitio());
        mBuilder.setSmallIcon(R.drawable.ic_action_notificacion)
                .setContentTitle(sitio.getNombre())
                .setContentText(notificacion.getTitulo())
                .setContentIntent(resultPendingIntent)
                .setGroup(GRUPO_NOTIFICACIONES_DIME_MONESTERIO)
                .setGroupSummary(true)
                .setAutoCancel(true);
        if(UtilPreferencias.sonarRecibirNotificacion(context)) {
            mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        }
        if(UtilPreferencias.vibrarRecibirNotificacion(context)) {
            mBuilder.setVibrate(patronVibracion);
        }
        if(UtilPreferencias.ledRecibirNotificacion(context)) {
            mBuilder.setLights(Color.YELLOW, 2000, 1500);
        }

        if(idNotificaciones == Integer.MAX_VALUE) {
            idNotificaciones = 0;
        }
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = mBuilder.build();
        mNotificationManager.notify(idNotificaciones++, notification);
    }

    private Sitio getSitio(Context context, long idSitio) {
        Sitio resul = null;
        SitiosDataSource sitiosDS = new SitiosDataSource(context);
        sitiosDS.open();
        resul = sitiosDS.getById(idSitio);
        sitiosDS.close();

        return resul;
    }

}
