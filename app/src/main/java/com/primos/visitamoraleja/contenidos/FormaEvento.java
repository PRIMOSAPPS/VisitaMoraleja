package com.primos.visitamoraleja.contenidos;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by h on 6/06/16.
 */
public class FormaEvento implements Serializable, IContenidoUltimaActualizacion {
    private long id;
    private long idEvento;
    private long idCategoriaEvento;
    private String tipoForma;
    private String colorRelleno;
    private String colorLinea;
    private String grosorLinea;
    private String texto;
    private String coordenadas;
    private boolean activo;
    private Date ultimaActualizacion;

    public FormaEvento() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(long idEvento) {
        this.idEvento = idEvento;
    }

    public String getTipoForma() {
        return tipoForma;
    }

    public void setTipoForma(String tipoForma) {
        this.tipoForma = tipoForma;
    }

    public String getColorRelleno() {
        return colorRelleno;
    }

    public void setColorRelleno(String colorRelleno) {
        this.colorRelleno = colorRelleno;
    }

    public String getColorLinea() {
        return colorLinea;
    }

    public void setColorLinea(String colorLinea) {
        this.colorLinea = colorLinea;
    }

    public String getGrosorLinea() {
        return grosorLinea;
    }

    public void setGrosorLinea(String grosorLinea) {
        this.grosorLinea = grosorLinea;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Date getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public void setUltimaActualizacion(Date ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }

    public long getIdCategoriaEvento() {
        return idCategoriaEvento;
    }

    public void setIdCategoriaEvento(long idCategoriaEvento) {
        this.idCategoriaEvento = idCategoriaEvento;
    }
}
