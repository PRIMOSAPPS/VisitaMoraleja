package com.primos.visitamoraleja.util;

import java.text.MessageFormat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.primos.visitamoraleja.R;

public class UtilConsultaActualizar {
	private boolean consultaActualizar;
	
	public boolean isConsultaActualizar() {
		return consultaActualizar;
	}

	public void consultaActualizar(final Context contexto, final Object semaforo, int numeroSitios) {

		final AlertDialog.Builder adb = new AlertDialog.Builder(contexto);

		float tamanio = ((float)numeroSitios / 10);
		String txtMsjActualizacion = contexto.getResources().getString(R.string.txt_texto_msj_actualizacion);
		adb.setMessage(MessageFormat.format(txtMsjActualizacion, tamanio));
		adb.setTitle(R.string.txt_titulo_actualizacion);
		adb.setIcon(android.R.drawable.ic_dialog_alert);
		adb.setPositiveButton(R.string.txt_texto__actualizar_ahora, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				synchronized(semaforo) {
					consultaActualizar = true;
					dialog.cancel();
					semaforo.notify();
				}
			}
		});

		adb.setNegativeButton(R.string.txt_texto_ahora_no, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				synchronized(semaforo) {
					consultaActualizar = false;
					dialog.cancel();
					semaforo.notify();
				}
			}
		});
		AlertDialog dialog = adb.create();
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}
}
