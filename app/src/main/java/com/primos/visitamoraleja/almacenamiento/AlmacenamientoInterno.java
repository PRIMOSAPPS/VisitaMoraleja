package com.primos.visitamoraleja.almacenamiento;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;

public class AlmacenamientoInterno implements ItfAlmacenamiento {
	private final static String TAG = "AlmacenamientoInterno";
	private final static String PREFIJO_EVENTOS = "eventos_";
	private final static String PREFIJO_CATEGORIAS_EVENTOS = "categorias_eventos_";
	private final static String PREFIJO_ACTIVIDADES_EVENTOS = "actividades_eventos_";
	private final static String PREFIJO_CATEGORIAS = "categorias_";
	private final static String PREFIJO_SITIOS = "sitios_";
	private final static String PREFIJO_SITIOS_EVENTOSs = "sitios_eventos_";
	
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
	
	private void borrarImagen(String nombreImg) throws FileNotFoundException {
		contexto.deleteFile(nombreImg);
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
	public void addImagenSitio(Bitmap imagen, String nombreImagen, long idSitio) {
		String nombreImg = PREFIJO_SITIOS + nombreImagen;
		try {
			Log.d("[AlmacenamientoInterno]", "addImagenSitio(" + nombreImagen + ", " + idSitio + ")");
			guardarImagen(imagen, nombreImg);
		} catch (FileNotFoundException e) {
			Log.e(TAG, "Error guardando la imagen del sitio: " + idSitio + " nombre " + nombreImagen, e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.primos.visitamoraleja.almacenamiento.ItfAlmacenamiento2#addImagenSitio(android.graphics.Bitmap, java.lang.String, long)
	 */
	@Override
	public void borrarImagenSitio(String nombreImagen, long idSitio) {
		try {
			String nombreImg = PREFIJO_SITIOS + nombreImagen;
			Log.d("[AlmacenamientoInterno]", "borrarImagenSitio(" + nombreImg + ", " + idSitio + ")");
			borrarImagen(nombreImg);
		} catch (IOException e) {
			Log.e(TAG, "Error borrando la imagen del sitio: " + idSitio + " nombre " + nombreImagen, e);
		}
	}

	private String getNombreImagen(String prefijo, long identificador, String nombreImagen) {
		return prefijo + identificador + nombreImagen;
	}

	@Override
	public String getDirImagenEvento(long idEvento, String nombre) {
		return getNombreImagen(PREFIJO_EVENTOS, idEvento, nombre);
	}

	@Override
	public String getDirImagenActividadEvento(long id, String nombre) {
		return getNombreImagen(PREFIJO_ACTIVIDADES_EVENTOS, id, nombre);
	}

	@Override
	public Bitmap getImagenEvento(long idEvento, String nombre) {
		String nombreImagen = getDirImagenEvento(idEvento, nombre);
		Bitmap resul = null;
		try {
			resul = getBitmap(nombreImagen);
		} catch (FileNotFoundException e) {
			Log.e(TAG, "Error leyendo el icono de evento: " + idEvento + " nombre " + nombre, e);
		}
		return resul;
	}

	@Override
	public void addImagenEvento(Bitmap imagen, String nombreImagen,
								long idEvento) {
		String nombreImg = getNombreImagen(PREFIJO_EVENTOS, idEvento, nombreImagen);
		try {
			Log.d("[AlmacenamientoInterno]", "addIconoEventos(" + nombreImagen + ", " + idEvento + ")");
			guardarImagen(imagen, nombreImg);
		} catch (FileNotFoundException e) {
			Log.e(TAG, "Error guardando el icono de evento: " + idEvento + " nombre " + nombreImagen, e);
		}
	}

	/* (non-Javadoc)
	 * @see com.primos.visitamoraleja.almacenamiento.ItfAlmacenamiento2#addImagenSitio(android.graphics.Bitmap, java.lang.String, long)
	 */
	@Override
	public void borrarImagenEvento(String nombreImagen, long idEvento) {
		try {
			String nombreImg = getNombreImagen(PREFIJO_EVENTOS, idEvento, nombreImagen);
			Log.d("[AlmacenamientoInterno]", "borrarImagenSitio(" + nombreImg + ", " + idEvento + ")");
			borrarImagen(nombreImg);
		} catch (IOException e) {
			Log.e(TAG, "Error borrando la imagen del sitio: " + idEvento + " nombre " + nombreImagen, e);
		}
	}

	@Override
	public Bitmap getImagenSitioEvento(long idSitioEvento, String nombre) {
		String nombreImagen = getNombreImagen(PREFIJO_SITIOS_EVENTOSs, idSitioEvento, nombre);
		Bitmap resul = null;
		try {
			resul = getBitmap(nombreImagen);
		} catch (FileNotFoundException e) {
			Log.e(TAG, "Error leyendo el icono de evento de evento: " + idSitioEvento + " nombre " + nombre, e);
		}
		return resul;
	}

	@Override
	public void addImagenSitioEvento(Bitmap imagen, String nombreImagen,
								long idSitioEvento) {
		String nombreImg = getNombreImagen(PREFIJO_SITIOS_EVENTOSs, idSitioEvento, nombreImagen);
		try {
			Log.d("[AlmacenamientoInterno]", "addIconoEventos(" + nombreImagen + ", " + idSitioEvento + ")");
			guardarImagen(imagen, nombreImg);
		} catch (FileNotFoundException e) {
			Log.e(TAG, "Error guardando el icono de evento de evento: " + idSitioEvento + " nombre " + nombreImagen, e);
		}
	}

	/* (non-Javadoc)
	 * @see com.primos.visitamoraleja.almacenamiento.ItfAlmacenamiento2#addImagenSitio(android.graphics.Bitmap, java.lang.String, long)
	 */
	@Override
	public void borrarImagenSitioEvento(String nombreImagen, long idSitioEvento) {
		try {
			String nombreImg = getNombreImagen(PREFIJO_SITIOS_EVENTOSs, idSitioEvento, nombreImagen);
			Log.d("[AlmacenamientoInterno]", "borrarImagenSitio(" + nombreImg + ", " + idSitioEvento + ")");
			borrarImagen(nombreImg);
		} catch (IOException e) {
			Log.e(TAG, "Error borrando la imagen del sitio de evento: " + idSitioEvento + " nombre " + nombreImagen, e);
		}
	}

	@Override
	public Bitmap getImagenCategoriaEvento(long idCategoriaEvento, String nombre) {
		String nombreImagen = getNombreImagen(PREFIJO_CATEGORIAS_EVENTOS, idCategoriaEvento, nombre);
		Bitmap resul = null;
		try {
			resul = getBitmap(nombreImagen);
		} catch (FileNotFoundException e) {
			Log.e(TAG, "Error leyendo el icono de la categoria de evento: " + idCategoriaEvento + " nombre " + nombre, e);
		}
		return resul;
	}

	@Override
	public void addImagenCategoriaEvento(Bitmap imagen, String nombreImagen,
									 long idCategoriaEvento) {
		String nombreImg = getNombreImagen(PREFIJO_CATEGORIAS_EVENTOS, idCategoriaEvento, nombreImagen);
		try {
			Log.d("[AlmacenamientoInterno]", "addImagenCategoriaEvento(" + nombreImagen + ", " + idCategoriaEvento + ")");
			guardarImagen(imagen, nombreImg);
		} catch (FileNotFoundException e) {
			Log.e(TAG, "Error guardando el icono de la categoria de evento: " + idCategoriaEvento + " nombre " + nombreImagen, e);
		}
	}

	@Override
	public void borrarImagenCategoriaEvento(String nombreImagen, long idCategoriaEvento) {
		try {
			String nombreImg = getNombreImagen(PREFIJO_CATEGORIAS_EVENTOS, idCategoriaEvento, nombreImagen);
			Log.d("[AlmacenamientoInterno]", "borrarImagenCategoriaEvento(" + nombreImg + ", " + idCategoriaEvento + ")");
			borrarImagen(nombreImg);
		} catch (IOException e) {
			Log.e(TAG, "Error borrando la imagen de la categoria de evento: " + idCategoriaEvento + " nombre " + nombreImagen, e);
		}
	}

	@Override
	public Bitmap getImagenActividadEvento(long idActividadEvento, String nombre) {
		String nombreImagen = getNombreImagen(PREFIJO_ACTIVIDADES_EVENTOS, idActividadEvento, nombre);
		Bitmap resul = null;
		try {
			resul = getBitmap(nombreImagen);
		} catch (FileNotFoundException e) {
			Log.e(TAG, "Error leyendo el icono de la actividad de evento: " + idActividadEvento + " nombre " + nombre, e);
		}
		return resul;
	}

	@Override
	public void addImagenActividadEvento(Bitmap imagen, String nombreImagen,
										 long idActividadEvento) {
		String nombreImg = getNombreImagen(PREFIJO_ACTIVIDADES_EVENTOS, idActividadEvento, nombreImagen);
		try {
			Log.d("[AlmacenamientoInterno]", "addImagenActividadEvento(" + nombreImagen + ", " + idActividadEvento + ")");
			guardarImagen(imagen, nombreImg);
		} catch (FileNotFoundException e) {
			Log.e(TAG, "Error guardando el icono de la actividad de evento: " + idActividadEvento + " nombre " + nombreImagen, e);
		}
	}

	@Override
	public void borrarImagenActividadEvento(String nombreImagen, long idActividadEvento) {
		try {
			String nombreImg = getNombreImagen(PREFIJO_ACTIVIDADES_EVENTOS, idActividadEvento, nombreImagen);
			Log.d("[AlmacenamientoInterno]", "borrarImagenActividadEvento(" + nombreImg + ", " + idActividadEvento + ")");
			borrarImagen(nombreImg);
		} catch (IOException e) {
			Log.e(TAG, "Error borrando la imagen de la actividad de evento: " + idActividadEvento + " nombre " + nombreImagen, e);
		}
	}
}
