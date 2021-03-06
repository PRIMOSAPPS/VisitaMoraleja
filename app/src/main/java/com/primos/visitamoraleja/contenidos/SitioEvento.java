package com.primos.visitamoraleja.contenidos;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by h on 7/06/16.
 */
public class SitioEvento implements Serializable, IContenidoUltimaActualizacion, IContenidoGeneral {
    private long id;
    private long idEvento;
    private long idCategoriaEvento;
    private boolean esSitioRegistrado;
    private long idSitioRegistrado;
    private String nombre;
    private String texto;
    private String descripcion;
    private String nombreIcono;
    private Bitmap icono;
    private double longitud;
    private double latitud;
    private boolean activo;
    private Date ultimaActualizacion;

    public SitioEvento() {
        idSitioRegistrado = -1;
    }

    public long getId() {
        return id;
    }

    @Override
    public String getNombreImagen() {
        return getNombreIcono();
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

    public boolean isEsSitioRegistrado() {
        return esSitioRegistrado;
    }

    public void setEsSitioRegistrado(boolean esSitioRegistrado) {
        this.esSitioRegistrado = esSitioRegistrado;
    }

    public long getIdSitioRegistrado() {
        return idSitioRegistrado;
    }

    public void setIdSitioRegistrado(long idSitioRegistrado) {
        this.idSitioRegistrado = idSitioRegistrado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombreIcono() {
        return nombreIcono;
    }

    public void setNombreIcono(String nombreIcono) {
        this.nombreIcono = nombreIcono;
    }

    public Bitmap getIcono() {
        return icono;
    }

    public void setIcono(Bitmap icono) {
        this.icono = icono;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
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
