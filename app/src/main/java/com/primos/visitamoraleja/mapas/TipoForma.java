package com.primos.visitamoraleja.mapas;

/**
 * Created by h on 26/06/16.
 */
public enum TipoForma {
    PUNTO, LINEA, POLIGONO;

    public static TipoForma get(String forma) {
        TipoForma resul = null;
        if("poligono".equals(forma)) {
            resul = POLIGONO;
        } else if("linea".equals(forma)) {
            resul = LINEA;
        } else if("punto".equals(forma)) {
            resul = PUNTO;
        }
        return resul;
    }
}
