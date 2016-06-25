package com.primos.visitamoraleja.dto;

import com.primos.visitamoraleja.contenidos.FormaEvento;

import java.util.Date;
import java.util.List;

/**
 * Created by h on 6/06/16.
 */
public class EventoActualizableDTO {
    private long id;
    private String nombre;
    private boolean activo;
    private Date ultimaActualizacion;
    private List<FormaEvento> formas;
    private List<Long> idsImagenes;
    private List<Long> idsSitios;
    private List<Long> idsCategoriasEvento;
    private List<Long> idsActividades;

    public List<Long> getIdsCategoriasEvento() {
        return idsCategoriasEvento;
    }

    public void setIdsCategoriasEvento(List<Long> idsCategoriasEvento) {
        this.idsCategoriasEvento = idsCategoriasEvento;
    }

    public List<Long> getIdsActividades() {
        return idsActividades;
    }

    public void setIdsActividades(List<Long> idsActividades) {
        this.idsActividades = idsActividades;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public List<FormaEvento> getFormas() {
        return formas;
    }

    public void setFormas(List<FormaEvento> formas) {
        this.formas = formas;
    }

    public List<Long> getIdsImagenes() {
        return idsImagenes;
    }

    public void setIdsImagenes(List<Long> idsImagenes) {
        this.idsImagenes = idsImagenes;
    }

    public List<Long> getIdsSitios() {
        return idsSitios;
    }

    public void setIdsSitios(List<Long> idsSitios) {
        this.idsSitios = idsSitios;
    }
}
