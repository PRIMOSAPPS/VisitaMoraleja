package com.primos.visitamoraleja;

public interface IPrimosActividyLifeCycleEmisor {
	void registrar(IPrimosActivityLifecycleCallbacks activityLifeCicleListener);
	
	void deregistrar(IPrimosActivityLifecycleCallbacks activityLifeCicleListener);
}
