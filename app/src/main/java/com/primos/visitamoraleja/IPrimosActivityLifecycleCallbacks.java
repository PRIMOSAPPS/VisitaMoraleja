package com.primos.visitamoraleja;

import android.app.Activity;
import android.os.Bundle;

public interface IPrimosActivityLifecycleCallbacks {

	void onActivityCreated(Activity activity, Bundle savedInstanceState);

	void onActivityDestroyed(Activity activity);

	void onActivityPaused(Activity activity);

	void onActivityResumed(Activity activity);

	void onActivitySaveInstanceState(Activity activity, Bundle outState);

	void onActivityStarted(Activity activity);

	void onActivityStopped(Activity activity);

}
