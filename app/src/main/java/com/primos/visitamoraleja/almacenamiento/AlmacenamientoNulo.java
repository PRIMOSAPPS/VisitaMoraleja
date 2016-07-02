package com.primos.visitamoraleja.almacenamiento;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.primos.visitamoraleja.R;

/**
 * Created by h on 2/07/16.
 */
public class AlmacenamientoNulo implements ItfAlmacenamiento {

    private Context contexto;
    private boolean mostrado = false;

    public AlmacenamientoNulo(Context contexto) {
        this.contexto = contexto;
    }

    private void mostrarMensajePermisos() {
        if(!mostrado) {
            Toast.makeText(contexto, R.string.permisos_necesarios, Toast.LENGTH_SHORT)
                    .show();
            mostrado = true;
        }
    }

    @Override
    public Bitmap getIconoCategoria(long idCategoria, String nombre) {
        mostrarMensajePermisos();
        return null;
    }

    @Override
    public Bitmap getImagenSitio(long idSitio, String nombre) {
        mostrarMensajePermisos();return null;
    }

    @Override
    public void addIconoCategoria(Bitmap imagen, String nombreImagen, long idCategoria) {
    }

    @Override
    public void addImagenSitio(Bitmap imagen, String nombreImagen, long idSitio) {
    }

    @Override
    public void borrarImagenSitio(String nombreImagen, long idSitio) {
    }

    @Override
    public String getDirImagenEvento(long idEvento, String nombre) {
        return null;
    }

    @Override
    public Bitmap getImagenEvento(long idEvento, String nombre) {
        mostrarMensajePermisos();return null;
    }

    @Override
    public void addImagenEvento(Bitmap imagen, String nombreImagen, long idEvento) {

    }

    @Override
    public void borrarImagenEvento(String nombreImagen, long idEvento) {

    }

    @Override
    public Bitmap getImagenSitioEvento(long idSitioEvento, String nombre) {
        mostrarMensajePermisos();return null;
    }

    @Override
    public void addImagenSitioEvento(Bitmap imagen, String nombreImagen, long idSitioEvento) {

    }

    @Override
    public void borrarImagenSitioEvento(String nombreImagen, long idSitioEvento) {

    }

    @Override
    public Bitmap getImagenCategoriaEvento(long idCategoriaEvento, String nombre) {
        mostrarMensajePermisos();return null;
    }

    @Override
    public void addImagenCategoriaEvento(Bitmap imagen, String nombreImagen, long idCategoriaEvento) {

    }

    @Override
    public void borrarImagenCategoriaEvento(String nombreImagen, long idCategoriaEvento) {

    }

    @Override
    public Bitmap getImagenActividadEvento(long idActividadEvento, String nombre) {
        mostrarMensajePermisos();return null;
    }

    @Override
    public void addImagenActividadEvento(Bitmap imagen, String nombreImagen, long idActividadEvento) {

    }

    @Override
    public void borrarImagenActividadEvento(String nombreImagen, long idActividadEvento) {

    }
}
