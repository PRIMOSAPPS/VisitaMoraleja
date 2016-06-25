package com.primos.visitamoraleja.eventos;

import android.util.Log;

import com.primos.visitamoraleja.constantes.Constantes;
import com.primos.visitamoraleja.contenidos.Evento;
import com.primos.visitamoraleja.contenidos.FormaEvento;
import com.primos.visitamoraleja.contenidos.ImagenEvento;
import com.primos.visitamoraleja.contenidos.SitioEvento;
import com.primos.visitamoraleja.dto.EventoActualizableDTO;
import com.primos.visitamoraleja.util.ConversionesUtil;
import com.primos.visitamoraleja.util.UtilJson;
import com.primos.visitamoraleja.util.UtilStrings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by h on 5/06/16.
 */
public class ImagenesEventoJsonParser extends AbstractJsonParser<ImagenEvento> {
    private final static String TAG = "ImagenesEventoJsonParse";

    public ImagenEvento parse(JSONObject json) throws JSONException, ParseException, UnsupportedEncodingException {
        ImagenEvento resul = new ImagenEvento();

        UtilJson utilJson = new UtilJson();
        resul.setId(json.getLong(Constantes.Json.ID));
        resul.setIdEvento(json.getLong(Constantes.Json.ID_EVENTO));
        resul.setNombre(utilJson.getStringFromBase64(json, Constantes.Json.NOMBRE));
        String cadenaImagen = json.getString(Constantes.Json.IMAGEN);
        resul.setImagen(ConversionesUtil.getBitmap(cadenaImagen));
        resul.setActivo(utilJson.getBoolean(json, Constantes.Json.ACTIVO));
        resul.setUltimaActualizacion(utilJson.getDate(json, Constantes.Json.ULTIMA_ACTUALIZACION));

        return resul;
    }


}
