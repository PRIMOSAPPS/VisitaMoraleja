package com.primos.visitamoraleja.contenidos;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by h on 7/06/16.
 */
public class ImagenEvento implements Serializable {
    private long id;
    private long idEvento;
    private String nombre;
    private Bitmap imagen;
    private boolean activo;
    private Date ultimaActualizacion;

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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
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
}
