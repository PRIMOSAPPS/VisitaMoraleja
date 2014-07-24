package com.primos.visitamoraleja.almacenamiento;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;

public class AlmacenamientoInterno implements ItfAlmacenamiento {
	private final static String TAG = "AlmacenamientoInterno";
	private final static String PREFIJO_EVENTOS = "eventos_";
	private final static String PREFIJO_CATEGORIAS = "categorias_";
	private final static String PREFIJO_SITIOS = "sitios_";
	
	private Context contexto;

	public AlmacenamientoInterno(Context contexto) {
		this.contexto = contexto;
	}
	
	private Bitmap getBitmap(String nombreFicheroImagen) throws FileNotFoundException {
		Bitmap resul = null;
		FileInputStream fis;
		fis = contexto.openFileInput(nombreFicheroImagen);
		resul = BitmapFactory.decodeStream(fis);
		return resul;
	}

	@Override
	public Bitmap getIconoCategoria(long idCategoria, String nombre) {
		String nombreImagen = PREFIJO_CATEGORIAS + nombre;
		Bitmap resul = null;
		try {
			resul = getBitmap(nombreImagen);
		} catch (FileNotFoundException e) {
			Log.e(TAG, "Error leyendo el icono de categoria: " + idCategoria + " nombre " + nombre, e);
		}
		return resul;
	}

	@Override
	public Bitmap getIconoEvento(long idEvento, String nombre) {
		String nombreImagen = PREFIJO_EVENTOS + nombre;
		Bitmap resul = null;
		try {
			resul = getBitmap(nombreImagen);
		} catch (FileNotFoundException e) {
			Log.e(TAG, "Error leyendo el icono de evento: " + idEvento + " nombre " + nombre, e);
		}
		return resul;
	}

	@Override
	public Bitmap getImagenSitio(long idSitio, String nombre) {
		String nombreImagen = PREFIJO_SITIOS + nombre;
		Bitmap resul = null;
		try {
			resul = getBitmap(nombreImagen);
		} catch (FileNotFoundException e) {
			Log.e(TAG, "Error leyendo la imagen de sitio: " + idSitio + " nombre " + nombre, e);
		}
		return resul;
	}
	
	private CompressFormat getCompress(String nombreFichero) {
		String extension = nombreFichero.replaceAll("^.*\\.([^.]+)$", "$1");
		extension = extension.toUpperCase();
		CompressFormat compressFormat = CompressFormat.PNG; 
		if(extension.equals("JPG") || extension.equals("JPEG")) {
			compressFormat = CompressFormat.JPEG;
		}
		return compressFormat;
	}
	
	private void guardarImagen(Bitmap imagen, String nombreImg) throws FileNotFoundException {
		if(imagen != null) {
			FileOutputStream fos = contexto.openFileOutput(nombreImg, Context.MODE_APPEND);
			CompressFormat compressFormat = getCompress(nombreImg);
			imagen.compress(compressFormat, 100 /*ignored for PNG*/, fos);
//			fos.close();
		}
	}

	@Override
	public void addIconoCategoria(Bitmap imagen, String nombreImagen,
			long idCategoria) {
		String nombreImg = PREFIJO_CATEGORIAS + nombreImagen;
		try {
			Log.d("[AlmacenamientoInterno]", "addIconoEventos(" + nombreImagen + ", " + idCategoria + ")");
			guardarImagen(imagen, nombreImg);
		} catch (FileNotFoundException e) {
			Log.e(TAG, "Error guardando el icono de categoria: " + idCategoria + " nombre " + nombreImagen, e);
		}
	}

	@Override
	public void addIconoEventos(Bitmap imagen, String nombreImagen,
			long idEvento) {
		String nombreImg = PREFIJO_EVENTOS + nombreImagen;
		try {
			Log.d("[AlmacenamientoInterno]", "addIconoEventos(" + nombreImagen + ", " + idEvento + ")");
			guardarImagen(imagen, nombreImg);
		} catch (FileNotFoundException e) {
			Log.e(TAG, "Error guardando el icono de evento: " + idEvento + " nombre " + nombreImagen, e);
		}
	}

	@Override
	public void addImagenSitio(Bitmap imagen, String nombreImagen, long idSitio) {
		String nombreImg = PREFIJO_SITIOS + nombreImagen;
		try {
			Log.d("[AlmacenamientoInterno]", "addImagenSitio(" + nombreImagen + ", " + idSitio + ")");
			guardarImagen(imagen, nombreImg);
		} catch (FileNotFoundException e) {
			Log.e(TAG, "Error guardando la imagen del sitio: " + idSitio + " nombre " + nombreImagen, e);
		}
	}

}
