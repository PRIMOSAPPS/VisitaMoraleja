package com.primos.visitamoraleja.mapas;

/**
 * Created by h on 21/02/16.
 */
public class ControlMapaFactory {

    public ControlMapaItf createControlMapa(int TipoMapa) {
        return new ControlMapaGeneral();
    }
}
