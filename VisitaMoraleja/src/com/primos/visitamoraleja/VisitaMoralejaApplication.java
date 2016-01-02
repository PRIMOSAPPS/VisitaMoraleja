package com.primos.visitamoraleja;
import android.app.Application;
import android.provider.Settings.Secure;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;
import com.primos.visitamoraleja.util.UtilPropiedades;


public class VisitaMoralejaApplication extends Application {
	private final static String TAG = "[VisitaMoralejaApplication]";
	
	public VisitaMoralejaApplication() {
		super();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		UtilPropiedades.getInstance().inicializar(this);
		registraParse2();
	}

	private void registraParse2() {
		UtilPropiedades propiedades = UtilPropiedades.getInstance();
		String applicationId = propiedades.getProperty(UtilPropiedades.PROP_PARSE_APPLICATION_ID); 
        String clientKey = propiedades.getProperty(UtilPropiedades.PROP_PARSE_CLIENT_KEY);
        Log.d(TAG, "applicationId: " + applicationId);
        Log.d(TAG, "clientKey: " + clientKey);

		Parse.initialize(this, applicationId, clientKey);
//		PushService.setDefaultPushCallback(this, MainActivity.class);
		Runnable runableInicioParse = new Runnable() {
			@Override
			public void run() {
				ParseInstallation.getCurrentInstallation().saveInBackground();
			}
		};
		Thread th = new Thread(runableInicioParse, "ThreadInicioParse");
		th.start();
//		ParseInstallation.getCurrentInstallation().saveInBackground();
	}

	private void registraParse() {
		String  androidId = Secure.getString(getApplicationContext().getContentResolver(),Secure.ANDROID_ID);
		Log.d("VisitaMoralejaApplication", "ANDROID_ID El id conseguido es: " + androidId);
		UtilPropiedades propiedades = UtilPropiedades.getInstance();
		String applicationId = propiedades.getProperty(UtilPropiedades.PROP_PARSE_APPLICATION_ID); 
        String clientKey = propiedades.getProperty(UtilPropiedades.PROP_PARSE_CLIENT_KEY);
        Log.d("VisitaMoralejaApplication", "applicationId: " + applicationId);
        Log.d("VisitaMoralejaApplication", "clientKey: " + clientKey);

		Parse.initialize(this, applicationId, clientKey);
//		PushService.setDefaultPushCallback(this, MainActivity.class);
//		ParseInstallation.getCurrentInstallation().saveInBackground();
		
		ParseInstallation installation = ParseInstallation.getCurrentInstallation();
		installation.put("UniqueId", androidId);
		installation.setObjectId(null);
		installation.saveInBackground();
	}
	
	
}
