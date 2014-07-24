package com.primos.visitamoraleja.adaptadores;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.primos.visitamoraleja.DetalleEventoActivity;
import com.primos.visitamoraleja.almacenamiento.AlmacenamientoExterno;
import com.primos.visitamoraleja.almacenamiento.AlmacenamientoFactory;
import com.primos.visitamoraleja.almacenamiento.ItfAlmacenamiento;
import com.primos.visitamoraleja.contenidos.Sitio;

/**
 * <b>ESTA CLASE SE CORRESPONDE CON PARTE DE LA VISTA DE LA APLICACION</b>
 * Adaptador para las imagenes de los sitios que son mostradas en la galeria en la actividad <b>DetalleEventoActivity</b>
 * Con este adaptador se muestran las imagenes en la galeria.
 * 
 * @author h
 *
 */
public class ImageAdapter extends BaseAdapter {
	/**
	 * 
	 */
	private final DetalleEventoActivity ImageAdapter;
	/** The parent context */
	private Context myContext;
	// Put some images to project-folder: /res/drawable/
	// format: jpg, gif, png, bmp, ...
//	private int[] myImageIds = { R.drawable.imagen1, R.drawable.imagen2,
//			R.drawable.imagen3, R.drawable.imagen4, R.drawable.imagen5 };
	private Sitio sitio;

	/** Simple Constructor saving the 'parent' context. */
	public ImageAdapter(DetalleEventoActivity detalleEventoActivity, Context c, Sitio sitio) {
		ImageAdapter = detalleEventoActivity;
		this.myContext = c;
		this.sitio = sitio;
	}
	
	private boolean noVacio(String str) {
		return str != null && !str.equals("");
	}

	/**
	 * Devuelve el numero de imagenes que existen en el objeto Sitio
	 */
	public int getCount() {
		int resul = 0;
		if(noVacio(sitio.getNombreImagen1())) {
			resul++;
		}
		if(noVacio(sitio.getNombreImagen2())) {
			resul++;
		}
		if(noVacio(sitio.getNombreImagen3())) {
			resul++;
		}
		if(noVacio(sitio.getNombreImagen4())) {
			resul++;
		}
		return resul;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}
	
	/**
	 * Devuelve el nombre de la imagen segun la posicion pedida. Necesario para poder leer el fichero que contiene la imagen
	 * en el almacenamiento.
	 * @param position
	 * @return
	 */
	private String getNombreImagen(int position) {
		switch (position) {
		case 0:
			return sitio.getNombreImagen1();
		case 1:
			return sitio.getNombreImagen2();
		case 2:
			return sitio.getNombreImagen3();
		case 3:
			return sitio.getNombreImagen4();
		default:
			return null;
		}
		
	}

	/**
	 * Devueleve un ImageView para ser mostrado en la galeria.
	 */
	public View getView(int position, View convertView, ViewGroup parent) {

		// Get a View to display image data
		ImageView iv = new ImageView(this.myContext);
		ItfAlmacenamiento almacenamiento = AlmacenamientoFactory.getAlmacenamiento(myContext);
		Bitmap bmp = almacenamiento.getImagenSitio(sitio.getId(), getNombreImagen(position));
		iv.setImageBitmap(bmp);

		// Image should be scaled somehow
		// iv.setScaleType(ImageView.ScaleType.CENTER);
		// iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
		// iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		// iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
		// iv.setScaleType(ImageView.ScaleType.FIT_XY);
		iv.setScaleType(ImageView.ScaleType.FIT_END);

		// Set the Width & Height of the individual images
		// iv.setLayoutParams(new Gallery.LayoutParams(95, 70));

		return iv;
	}
}