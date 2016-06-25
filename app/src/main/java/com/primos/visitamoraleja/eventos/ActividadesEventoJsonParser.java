package com.primos.visitamoraleja.eventos;

import com.primos.visitamoraleja.constantes.Constantes;
import com.primos.visitamoraleja.contenidos.ActividadEvento;
import com.primos.visitamoraleja.contenidos.SitioEvento;
import com.primos.visitamoraleja.util.ConversionesUtil;
import com.primos.visitamoraleja.util.UtilJson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

/**
 * Created by h on 5/06/16.
 */
public class ActividadesEventoJsonParser extends AbstractJsonParser<ActividadEvento> {
    private final static String TAG = "ActividadesEventoJsonParser";

    public ActividadEvento parse(JSONObject json) throws JSONException, ParseException, UnsupportedEncodingException {
        ActividadEvento resul = new ActividadEvento();

        UtilJson utilJson = new UtilJson();
        resul.setId(json.getLong(Constantes.Json.ID));
        resul.setIdEvento(json.getLong(Constantes.Json.ID_EVENTO));
        resul.setIdCategoriaEvento(json.getLong(Constantes.Json.ID_CATEGORIA_EVENTO));
        resul.setNombre(utilJson.getStringFromBase64(json, Constantes.Json.NOMBRE));
        resul.setTexto(utilJson.getStringFromBase64(json, Constantes.Json.TEXTO));
        resul.setDescripcion(utilJson.getStringFromBase64(json, Constantes.Json.DESCRIPCION));
        resul.setNombreIcono(utilJson.getStringFromBase64(json, Constantes.Json.NOMBRE_ICONO));
        String cadenaImagen = json.getString(Constantes.Json.ICONO);
        resul.setIcono(ConversionesUtil.getBitmap(cadenaImagen));
        resul.setInicio(utilJson.getDate(json, Constantes.Json.INICIO));
        resul.setFin(utilJson.getDate(json, Constantes.Json.FIN));
        resul.setLatitud(Double.parseDouble(json.getString(Constantes.Json.LATITUD)));
        resul.setLongitud(Double.parseDouble(json.getString(Constantes.Json.LONGITUD)));
        resul.setActivo(utilJson.getBoolean(json, Constantes.Json.ACTIVO));
        resul.setUltimaActualizacion(utilJson.getDate(json, Constantes.Json.ULTIMA_ACTUALIZACION));

        return resul;
    }


}
