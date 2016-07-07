package com.primos.visitamoraleja.adaptadores;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.primos.visitamoraleja.R;
import com.primos.visitamoraleja.contenidos.IContenidoGeneral;

import java.util.List;

/**
 * <b>ESTA CLASE SE CORRESPONDE CON PARTE DE LA VISTA DE LA APLICACION</b>
 * Adaptador del contenido de un Sitio a cada View correspondiente al ListView que muestra la lista de sitios. 
 * @author h
 *
 */
public abstract class ListaGeneralAdaptador<IGeneralDTO extends IContenidoGeneral> extends AbstractAdaptador  {

	protected abstract Bitmap getBitmap(IContenidoGeneral obj);

	public ListaGeneralAdaptador(Activity actividad, List<IGeneralDTO> listaGeneral) {
		super(actividad, listaGeneral);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = actividad.getLayoutInflater();
		View view = inflater.inflate(R.layout.item_lista_general, null, true);

		List<IGeneralDTO> objetos = (List<IGeneralDTO>)listaObjetos;
		IContenidoGeneral objeto = objetos.get(position);
		
		view.setTag(objeto);
        TextView textItem = (TextView)view.findViewById(R.id.textItem);
		textItem.setText(objeto.getNombre());
		
		ImageView imagen = (ImageView)view.findViewById(R.id.imagenItem);

		//ItfAlmacenamiento almacenamiento = AlmacenamientoFactory.getAlmacenamiento(actividad);
		//Bitmap bitmap = almacenamiento.getImagenSitio(objeto.getId(), objeto.getNombreImagen());
		imagen.setImageBitmap(getBitmap(objeto));
		
		return view;
	}

}
