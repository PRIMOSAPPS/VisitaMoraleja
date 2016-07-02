package com.primos.visitamoraleja.util;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;

import java.util.List;

/**
 * Created by h on 27/06/16.
 */
public class UtilMapas2 {
    public boolean containsInPolygon(LatLng latLng, Polygon polygon) {

        boolean oddTransitions = false;
        List<LatLng> coordenadas = polygon.getPoints();
        float[] polyY, polyX;
        float x = (float) (latLng.latitude);
        float y = (float) (latLng.longitude);

        // Create arrays for vertices coordinates
        polyY = new float[coordenadas.size()];
        polyX = new float[coordenadas.size()];
        for (int i=0; i<coordenadas.size() ; i++) {
            LatLng coordenada = coordenadas.get(i);
            polyY[i] = (float) (coordenada.longitude);
            polyX[i] = (float) (coordenada.latitude);
        }
        // Check if a virtual infinite line cross each arc of the polygon
        for (int i = 0, j = coordenadas.size() - 1; i < coordenadas.size(); j = i++) {
            if ((polyY[i] < y && polyY[j] >= y)
                    || (polyY[j] < y && polyY[i] >= y)
                    && (polyX[i] <= x || polyX[j] <= x)) {
                if (polyX[i] + (y - polyY[i]) / (polyY[j] - polyY[i])
                        * (polyX[j] - polyX[i]) < x) {
                    // The line cross this arc
                    oddTransitions = !oddTransitions;
                }
            }
        }
        // Return odd-even number of intersecs
        return oddTransitions;
    }
    /*
    private boolean containsInPolygon(LatLng latLng, Polygon polygon) {

    boolean oddTransitions = false;
    List<VerticesPolygon> verticesPolygon = polygon.getVertices();
    float[] polyY, polyX;
    float x = (float) (latLng.latitude);
    float y = (float) (latLng.longitude);

    // Create arrays for vertices coordinates
    polyY = new float[verticesPolygon.size()];
    polyX = new float[verticesPolygon.size()];
    for (int i=0; i<verticesPolygon.size() ; i++) {
        VerticesPolygon verticePolygon = verticesPolygon.get(i);
        polyY[i] = (float) (verticePolygon.getVertice().getLongitude());
        polyX[i] = (float) (verticePolygon.getVertice().getLatitude());
    }
    // Check if a virtual infinite line cross each arc of the polygon
    for (int i = 0, j = verticesPolygon.size() - 1; i < verticesPolygon.size(); j = i++) {
        if ((polyY[i] < y && polyY[j] >= y)
                || (polyY[j] < y && polyY[i] >= y)
                && (polyX[i] <= x || polyX[j] <= x)) {
            if (polyX[i] + (y - polyY[i]) / (polyY[j] - polyY[i])
                    * (polyX[j] - polyX[i]) < x) {
                // The line cross this arc
                oddTransitions = !oddTransitions;
            }
        }
    }
    // Return odd-even number of intersecs
    return oddTransitions;
}
     */
}
