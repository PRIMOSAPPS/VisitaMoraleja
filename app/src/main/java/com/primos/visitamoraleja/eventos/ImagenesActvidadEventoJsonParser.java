package com.primos.visitamoraleja.eventos;

import com.primos.visitamoraleja.constantes.Constantes;
import com.primos.visitamoraleja.contenidos.ImagenActividadEvento;
import com.primos.visitamoraleja.contenidos.ImagenEvento;
import com.primos.visitamoraleja.util.ConversionesUtil;
import com.primos.visitamoraleja.util.UtilJson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

/**
 * Created by h on 5/06/16.
 */
public class ImagenesActvidadEventoJsonParser extends AbstractJsonParser<ImagenActividadEvento> {
    private final static String TAG = "ImagenesEventoJsonParse";

    public ImagenActividadEvento parse(JSONObject json) throws JSONException, ParseException, UnsupportedEncodingException {
        ImagenActividadEvento resul = new ImagenActividadEvento();

        UtilJson utilJson = new UtilJson();
        resul.setId(json.getLong(Constantes.Json.ID));
        resul.setIdActividad(json.getLong(Constantes.Json.ID_ACTIVIDAD));
        resul.setNombre(utilJson.getStringFromBase64(json, Constantes.Json.NOMBRE));
        String cadenaImagen = json.getString(Constantes.Json.IMAGEN);
        resul.setImagen(ConversionesUtil.getBitmap(cadenaImagen));
        resul.setActivo(utilJson.getBoolean(json, Constantes.Json.ACTIVO));
        resul.setUltimaActualizacion(utilJson.getDate(json, Constantes.Json.ULTIMA_ACTUALIZACION));

        return resul;
    }


}
