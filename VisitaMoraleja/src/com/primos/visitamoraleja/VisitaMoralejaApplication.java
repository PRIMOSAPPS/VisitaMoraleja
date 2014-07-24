package com.primos.visitamoraleja;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;
import com.primos.visitamoraleja.util.UtilPropiedades;

import android.app.Application;
import android.provider.Settings.Secure;
import android.util.Log;


public class VisitaMoralejaApplication extends Application {

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
        Log.d("VisitaMoralejaApplication", "applicationId: " + applicationId);
        Log.d("VisitaMoralejaApplication", "clientKey: " + clientKey);

		Parse.initialize(this, applicationId, clientKey);
		PushService.setDefaultPushCallback(this, MainActivity.class);
		ParseInstallation.getCurrentInstallation().saveInBackground();
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
		PushService.setDefaultPushCallback(this, MainActivity.class);
//		ParseInstallation.getCurrentInstallation().saveInBackground();
		
		ParseInstallation installation = ParseInstallation.getCurrentInstallation();
		installation.put("UniqueId", androidId);
		installation.setObjectId(null);
		installation.saveInBackground();
	}
	
	
}
