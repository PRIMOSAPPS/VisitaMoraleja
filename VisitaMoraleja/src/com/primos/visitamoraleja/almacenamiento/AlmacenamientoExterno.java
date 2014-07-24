package com.primos.visitamoraleja.almacenamiento;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.primos.visitamoraleja.util.UtilPropiedades;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.util.Log;

/**
 * Clase que se encarga de guardar y recuperar las imagenes en almacenamiento externo.
 * Crea una estructura de directorios que empieza con un directorio cuyo nombre se corresponde con
 * el valor de la constante NOMBRE_APP. Dentro de este directorio se crean 3 subdirectorios (categorias,
 * eventos y sitios) y dentro de estos subdirectorios se crea uno por cada identificador de un contenido.
 * 
 * Por ejemplo, las imagenes de un sitio con id = 1 estarian en la ruta <b>NOMBRE_APP/sitios/1/</b> asi
 * se mantienen separadas las imagenes de los distintos contenidos.
 * Lo he hecho asi por que creo que sera mas dificil tener problemas con los nombres y tambien esta mas organizado.
 * Una vez hecha la clase solo es utilziarla para conseguir las imagenes.
 * 
 * @author h
 *
 */
public class AlmacenamientoExterno implements ItfAlmacenamiento {
	private final static String TAG = "AlmacenamientoExterno";
	
	private final static String DIR_EVENTOS = File.separator + "eventos";
	private final static String DIR_CATEGORIAS = File.separator + "categorias";
	private final static String DIR_SITIOS = File.separator + "sitios";
	/**
	 * Directorio de almacenamiento externo por defecto para la aplicacion. 
	 */
	private static String dirExterno;

	public AlmacenamientoExterno() {
		File fileDirExterno = Environment.getExternalStorageDirectory();
		dirExterno = fileDirExterno.getAbsolutePath();
	}

	/**
	 * Devuelve la ruta de almacenamiento externo para la aplicacion concatenado con el nombre de la constante NOMBRE_APP
	 * @return
	 */
	private String getDirApp() {
		String nombreAplicacion = UtilPropiedades.getInstance().getProperty(UtilPropiedades.PROP_NOMBRE_APLICACION);
		return dirExterno + File.separator + nombreAplicacion;
	}

//	private String getDirSitio(long idPoblacion) {
//		return getDirApp() + "/sitio_" + idPoblacion;
//	}
//
//	private String getDirEvento(long idPoblacion, long idEvento) {
//		return getDirSitio(idPoblacion) + "/evento_" + idEvento;
//	}
//	
//	public String getPathImagen(long idPoblacion, long idEvento, long idImagen, String nombre) {
//		return getDirEvento(idPoblacion, idEvento) + "/img_" + idImagen + "_" + nombre;
//	}
	
	/**
	 * Comprueba la existencia de un directorio, si no existe lo crea.
	 * @throws IOException 
	 */
	private void validarDirectorio(String pathDir) throws IOException {
		File fileDirApp = new File(pathDir);
		if (!fileDirApp.exists()) {
			if (!fileDirApp.mkdir()) {
				Log.e(TAG, "Error creando el directorio de la aplicacion: "
						+ pathDir);
				throw new IOException(
						"Error creando el directorio de la aplicacion: "
								+ pathDir);
			}
		}
	}
	
//	private void validaDirApp() throws IOException {
//		validarDirectorio(getDirApp());
//	}
	
	/**
	 * Valida la existencia de un directorio dentro de la ruta de subdirectorios. Si la ruta de los directorios padre
	 * no existe la crea. El directorio recibido debe existir en la ruta NOMBRE_APP/directorio/idContenido, comprueba primero
	 * la existencia de NOMBRE_APP/ despues NOMBRE_APP/directorio/ y finalmente NOMBRE_APP/directorio/idContenido y se crean los
	 * directorios si no existen.
	 * 
	 * @param directorio Nombre del primer subdirectorio dentro de la aplicacion, sera categoria, sitio o evento
	 * @param idContenido Identificador del cotenido al que pertenecen las imagenes.
	 * @throws IOException
	 */
	private void validaDir(String directorio, long idContenido) throws IOException {
		String dirApp = getDirApp();
		validarDirectorio(dirApp);
		// Se crea el fichero .nomedia para evitar que aparezcan en la agenda
		File noMedia = new File(dirApp + "/.nomedia");
		if(!noMedia.exists()) {
			noMedia.createNewFile();
		}
		String dirContenido = dirApp + directorio;
		validarDirectorio(dirContenido);
		String dirImagen = dirContenido + File.separator + idContenido;
		validarDirectorio(dirImagen);
	}
	
//	private void validaDirSitio(long idSitio) throws IOException {
//		validarDirectorio(getDirSitio(idSitio));
//	}
//	
//	private void validaDirEvento(long idPoblacion, long idEvento) throws IOException {
//		validarDirectorio(getDirEvento(idPoblacion, idEvento));
//	}
	
	/**
	 * Aniade una imagen al directorio NOMBRE_APP/directorio/idContenido con el nombre indicado en nombreImagen
	 * @param imagen
	 * @param directorio
	 * @param nombreImagen
	 * @param idContenido
	 * @throws IOException
	 */
	private void addImagen(Bitmap imagen, String directorio, String nombreImagen, long idContenido) throws IOException {
		if(imagen != null) {
			validaDir(directorio, idContenido);
//			validaDirEvento(idPoblacion, idEvento);

			String dirImagenes = getDirApp() + directorio;
			validarDirectorio(dirImagenes);
			dirImagenes = dirImagenes + File.separator + idContenido;
			validarDirectorio(dirImagenes);
			String pathFichero = dirImagenes + File.separator + nombreImagen;
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			FileOutputStream fos = new FileOutputStream(pathFichero);
			
			String extension = nombreImagen.replaceAll("^.*\\.([^.]+)$", "$1");
			extension = extension.toUpperCase();
			CompressFormat compressFormat = CompressFormat.PNG; 
			if(extension.equals("JPG") || extension.equals("JPEG")) {
				compressFormat = CompressFormat.JPEG;
			}
			imagen.compress(compressFormat, 100 /*ignored for PNG*/, bos);
			
			fos.write(bos.toByteArray());
			fos.close();
		}
	}

	/* (non-Javadoc)
	 * @see com.primos.visitamoraleja.almacenamiento.ItfAlmacenamiento2#getPathIconoCategoria(long, java.lang.String)
	 */
	@Override
	public Bitmap getIconoCategoria(long idCategoria, String nombre) {
		String pathImagen = getDirApp() + DIR_CATEGORIAS + File.separator + idCategoria + File.separator + nombre;
		return BitmapFactory.decodeFile(pathImagen);
	}

	/* (non-Javadoc)
	 * @see com.primos.visitamoraleja.almacenamiento.ItfAlmacenamiento2#getPathIconoEvento(long, java.lang.String)
	 */
	@Override
	public Bitmap getIconoEvento(long idEvento, String nombre) {
		String pathImagen = getDirApp() + DIR_EVENTOS + File.separator + idEvento + File.separator + nombre;
		return BitmapFactory.decodeFile(pathImagen);
	}

	/* (non-Javadoc)
	 * @see com.primos.visitamoraleja.almacenamiento.ItfAlmacenamiento2#getPathImagenSitio(long, java.lang.String)
	 */
	@Override
	public Bitmap getImagenSitio(long idSitio, String nombre) {
		String pathImagen = getDirApp() + DIR_SITIOS + File.separator + idSitio + File.separator + nombre;
		return BitmapFactory.decodeFile(pathImagen);
	}
	
	/* (non-Javadoc)
	 * @see com.primos.visitamoraleja.almacenamiento.ItfAlmacenamiento2#addIconoCategoria(android.graphics.Bitmap, java.lang.String, long)
	 */
	@Override
	public void addIconoCategoria(Bitmap imagen, String nombreImagen, long idCategoria) {
		try {
			Log.d("[AlmacenamientoExterno]", "addIconoEventos(" + nombreImagen + ", " + idCategoria + ")");
			addImagen(imagen, DIR_CATEGORIAS, nombreImagen, idCategoria);
		} catch (IOException e) {
			Log.e(TAG, "Error al trabajar con el almacenamiento externo al guardar un icono para las categorias: ", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.primos.visitamoraleja.almacenamiento.ItfAlmacenamiento2#addIconoEventos(android.graphics.Bitmap, java.lang.String, long)
	 */
	@Override
	public void addIconoEventos(Bitmap imagen, String nombreImagen, long idEvento) {
		try {
			Log.d("[AlmacenamientoExterno]", "addIconoEventos(" + nombreImagen + ", " + idEvento + ")");
			addImagen(imagen, DIR_EVENTOS, nombreImagen, idEvento);
		} catch (IOException e) {
			Log.e(TAG, "Error al trabajar con el almacenamiento externo al guardar un icono para los eventos: ", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.primos.visitamoraleja.almacenamiento.ItfAlmacenamiento2#addImagenSitio(android.graphics.Bitmap, java.lang.String, long)
	 */
	@Override
	public void addImagenSitio(Bitmap imagen, String nombreImagen, long idSitio) {
		try {
			Log.d("[AlmacenamientoExterno]", "addIconoEventos(" + nombreImagen + ", " + idSitio + ")");
			addImagen(imagen, DIR_SITIOS, nombreImagen, idSitio);
		} catch (IOException e) {
			Log.e(TAG, "Error al trabajar con el almacenamiento externo al guardar una imagen para los sitios: ", e);
		}
	}

}
