package com.primos.visitamoraleja.eventos;

import com.primos.visitamoraleja.constantes.Constantes;
import com.primos.visitamoraleja.contenidos.ActividadEvento;
import com.primos.visitamoraleja.contenidos.CategoriaEvento;
import com.primos.visitamoraleja.util.ConversionesUtil;
import com.primos.visitamoraleja.util.UtilJson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

/**
 * Created by h on 5/06/16.
 */
public class CategoriasEventoJsonParser extends AbstractJsonParser<CategoriaEvento> {
    private final static String TAG = "CategoriasEventoJsonParser";

    public CategoriaEvento parse(JSONObject json) throws JSONException, ParseException, UnsupportedEncodingException {
        CategoriaEvento resul = new CategoriaEvento();

        UtilJson utilJson = new UtilJson();
        resul.setId(json.getLong(Constantes.Json.ID));
        resul.setIdEvento(json.getLong(Constantes.Json.ID_EVENTO));
        resul.setNombre(utilJson.getStringFromBase64(json, Constantes.Json.NOMBRE));
        resul.setTexto(utilJson.getStringFromBase64(json, Constantes.Json.TEXTO));
        resul.setNombreIcono(utilJson.getStringFromBase64(json, Constantes.Json.NOMBRE_ICONO));
        String cadenaImagen = json.getString(Constantes.Json.ICONO);
        resul.setIcono(ConversionesUtil.getBitmap(cadenaImagen));
        resul.setActivo(utilJson.getBoolean(json, Constantes.Json.ACTIVO));
        resul.setUltimaActualizacion(utilJson.getDate(json, Constantes.Json.ULTIMA_ACTUALIZACION));

        return resul;
    }


}
