package com.primos.visitamoraleja.mapas;

import com.primos.visitamoraleja.mapas.eventos.ControlMapaEventos;

/**
 * Created by h on 21/02/16.
 */
public class ControlMapaFactory {
    public enum TIPOS_MAPAS {GENERAL, EVENTOS}

    ;

    public ControlMapaItf createControlMapa(TIPOS_MAPAS tipoMapa) {
        ControlMapaItf resul = null;
        switch (tipoMapa) {
            case GENERAL:
                resul = new ControlMapaGeneral();
                break;
            case EVENTOS:
                resul = new ControlMapaEventos();
                break;
        }

        return resul;
    }
}
