package com.primos.visitamoraleja.util;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by h on 26/06/16.
 */
public class UtilCoordenadas {
    public class CoordenadasForma {
        private LatLng punto;
        private List<LatLng> coordenadas;

        public CoordenadasForma(LatLng punto, List<LatLng> coordenadas) {
            this.punto = punto;
            this.coordenadas = coordenadas;
        }

        public LatLng getPunto() {
            return punto;
        }

        public List<LatLng> getCoordenadas() {
            return coordenadas;
        }
    }

    public final static String SEPARADOR_PUNTO_COORDENADAS=";";
    public final static String SEPARADOR_COORDENADAS=" ";
    public final static String SEPARADOR_LATITUD_LONGITUD=",";

    /**
     * Devuelve una lista de coordenadas que tiene el siguiente format xxxx,yyyy;xxxx,yyyy xxxx,yyyy xxxx,yyyy
     * Donde xxxx indica una latitud e yyyy indica una longitud. La primera coordenada indica un punto especial
     * @param coordenadas
     * @return
     */
    public CoordenadasForma parse(String coordenadas) {
        List<LatLng> coordenadasForma = new ArrayList<>();

        String[] puntoCoordenadas = coordenadas.split(SEPARADOR_PUNTO_COORDENADAS);
        LatLng punto = parseCoordenadaLngLat(puntoCoordenadas[0]);

        CoordenadasForma resul = new CoordenadasForma(punto, coordenadasForma);
        return resul;
    }

    private List<LatLng> parseSerieCoordenadasLngLat(String coordenadas) {
        List<LatLng> resul = new ArrayList<>();
        String[] puntosCoordenadas = coordenadas.split(SEPARADOR_COORDENADAS);
        for(String puntoCoordenada : puntosCoordenadas) {
            LatLng coordenada = parseCoordenadaLngLat(puntoCoordenada);
            resul.add(coordenada);
        }

        return resul;
    }

    /**
     * Parsea un String con el formato yyyy,xxxx donde yyyy es la longitud y xxxx es la latitud a un
     * objeto LatLng
     * @param coordenada
     * @return
     */
    private LatLng parseCoordenadaLngLat(String coordenada) {
        LatLng resul = null;
        String[] longLat = coordenada.split(SEPARADOR_LATITUD_LONGITUD);

        double longitud = Double.parseDouble(longLat[0]);
        double latitud = Double.parseDouble(longLat[1]);
        resul = new LatLng(latitud, longitud);
        return resul;
    }
}
