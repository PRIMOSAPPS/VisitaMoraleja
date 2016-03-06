package com.primos.visitamoraleja.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by h on 3/03/16.
 */
public class VersionApp {
    public static String getVersionApp(Context context) throws PackageManager.NameNotFoundException {
        PackageManager manager = context.getPackageManager();
        PackageInfo info = manager.getPackageInfo(
                context.getPackageName(), 0);
        return info.versionName;
    }
}
