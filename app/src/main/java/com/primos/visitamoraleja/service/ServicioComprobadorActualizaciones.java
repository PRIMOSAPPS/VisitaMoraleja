package com.primos.visitamoraleja.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.primos.visitamoraleja.DetalleNotificacionActivity;
import com.primos.visitamoraleja.MainActivity;
import com.primos.visitamoraleja.R;
import com.primos.visitamoraleja.actualizador.ConectorServidor;
import com.primos.visitamoraleja.bdsqlite.datasource.SitiosDataSource;
import com.primos.visitamoraleja.contenidos.Sitio;
import com.primos.visitamoraleja.excepcion.EventosException;
import com.primos.visitamoraleja.util.UtilPreferencias;

import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by h on 6/03/16.
 */
public class ServicioComprobadorActualizaciones extends Service {
    private final static String GRUPO_NOTIFICACIONES_ACTUALIZACIONES_VISITA_MORALEJA = "GRUPO_NOTIFICACIONES_ACTUALIZACIONES_VISITA_MORALEJA";
    // constant
    public static final long INTERVALO_EJECUCION = 24 * 60 * 60 * 1000;
    private final  static String TAG = "[ServicioComprobadorAc]";

    private SitiosDataSource dataSource;

    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;

    public ServicioComprobadorActualizaciones() {
        dataSource = new SitiosDataSource(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // cancel if already existed
        if(mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }
        Calendar calFecha = Calendar.getInstance();
        // schedule task
        mTimer.schedule(new ComprobadorActualizacionesTask(), calFecha.getTime(), INTERVALO_EJECUCION);
    }

    class ComprobadorActualizacionesTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {

                    dataSource.open();
                    long ultimaActualizacion = dataSource.getUltimaActualizacion();
                    dataSource.close();

                    String idsCategoriasActualizacion = UtilPreferencias
                            .getActualizacionPorCategorias(ServicioComprobadorActualizaciones.this);

                    ConectorServidor cs = new ConectorServidor(ServicioComprobadorActualizaciones.this);
                    try {
                        List<Sitio> lstSitiosActualizables = cs.getListaSitiosActualizables(ultimaActualizacion, idsCategoriasActualizacion);
                        if(!lstSitiosActualizables.isEmpty()) {
                            long[] patronVibracion = {2000, 1000};
                            //Esto hace posible crear la notificación
                            NotificationCompat.Builder mBuilder =
                                    new NotificationCompat.Builder(ServicioComprobadorActualizaciones.this);

                            Intent resultIntent = new Intent(ServicioComprobadorActualizaciones.this, MainActivity.class);
                            PendingIntent resultPendingIntent =
                                    PendingIntent.getActivity(
                                            ServicioComprobadorActualizaciones.this,
                                            0,
                                            resultIntent,
                                            PendingIntent.FLAG_UPDATE_CURRENT
                                    );

                            mBuilder.setSmallIcon(R.drawable.ic_action_notificacion)
                                    .setContentTitle(getString(R.string.title_notificacion_nuevas_actualizaciones))
                                    .setContentIntent(resultPendingIntent)
                                    .setGroup(GRUPO_NOTIFICACIONES_ACTUALIZACIONES_VISITA_MORALEJA)
                                    .setGroupSummary(true)
                                    .setAutoCancel(true);
                            mBuilder.setVibrate(patronVibracion);
                            mBuilder.setLights(Color.YELLOW, 2000, 1500);

                            NotificationManager mNotificationManager =
                                    (NotificationManager) ServicioComprobadorActualizaciones.this.getSystemService(Context.NOTIFICATION_SERVICE);
                            Notification notification = mBuilder.build();
                            mNotificationManager.notify(1, notification);
                        }
                    } catch (EventosException e) {
                        Log.e(TAG, "Error al consultar la lista de sitios actualizables para comprobar actualizaciones.");
                    }
                }

            });
        }

    }
}

