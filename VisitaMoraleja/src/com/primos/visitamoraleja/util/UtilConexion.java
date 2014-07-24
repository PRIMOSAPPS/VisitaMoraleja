
package com.primos.visitamoraleja.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class UtilConexion {

	public static boolean estaConectado(Context contexto) {
	    boolean resul = false;
	    ConnectivityManager connec = (ConnectivityManager) contexto
	            .getSystemService(Context.CONNECTIVITY_SERVICE);
	    // No sólo wifi, también GPRS
	    NetworkInfo[] redes = connec.getAllNetworkInfo();
	    for(NetworkInfo red : redes) {
	        if (red.getState() == NetworkInfo.State.CONNECTED) {
	        	resul = true;
	        }	    	
	    }
	    return resul;
	}
}
