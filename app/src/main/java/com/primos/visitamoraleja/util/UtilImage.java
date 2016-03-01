package com.primos.visitamoraleja.util;

import android.graphics.Bitmap;

/**
 * Created by h on 23/02/16.
 */
public class UtilImage {

    /**
     * Devuelve una imagen escalada teniendo en cuenta
     * @param bitmap
     * @param anchoFinal
     * @return
     */
    public static Bitmap createScaledBitmap(Bitmap bitmap, int anchoFinal) {
        int anchoReal = bitmap.getWidth();
        float proporcion = (float)anchoReal / (float)anchoFinal;
        int anchoProporcion = (int)(anchoReal / proporcion);
        int altoProporcion = (int)(bitmap.getHeight() / proporcion);
        return Bitmap.createScaledBitmap(bitmap, anchoProporcion, altoProporcion, false);
    }
}
