package com.primos.visitamoraleja.eventos;

import com.primos.visitamoraleja.constantes.Constantes;
import com.primos.visitamoraleja.contenidos.ImagenEvento;
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
public class SitiosEventoJsonParser extends AbstractJsonParser<SitioEvento> {
    private final static String TAG = "SitiosEventoJsonParser";

    public SitioEvento parse(JSONObject json) throws JSONException, ParseException, UnsupportedEncodingException {
        SitioEvento resul = new SitioEvento();

        UtilJson utilJson = new UtilJson();
        resul.setId(json.getLong(Constantes.Json.ID));
        resul.setIdEvento(json.getLong(Constantes.Json.ID_EVENTO));
        resul.setIdCategoriaEvento(json.getLong(Constantes.Json.ID_CATEGORIA_EVENTO));
        resul.setEsSitioRegistrado(utilJson.getBoolean(json, Constantes.Json.ES_SITIO_REGISTRADO));
        resul.setIdSitioRegistrado(json.getLong(Constantes.Json.ID_SITIO_REGISTRADO));
        resul.setNombre(utilJson.getStringFromBase64(json, Constantes.Json.NOMBRE));
        resul.setTexto(utilJson.getStringFromBase64(json, Constantes.Json.TEXTO));
        resul.setDescripcion(utilJson.getStringFromBase64(json, Constantes.Json.DESCRIPCION));
        resul.setNombreIcono(utilJson.getStringFromBase64(json, Constantes.Json.NOMBRE_ICONO));
        String cadenaImagen = json.getString(Constantes.Json.ICONO);
        resul.setIcono(ConversionesUtil.getBitmap(cadenaImagen));

        resul.setLatitud(Double.parseDouble(json.getString(Constantes.Json.LATITUD)));
        resul.setLongitud(Double.parseDouble(json.getString(Constantes.Json.LONGITUD)));
        resul.setActivo(utilJson.getBoolean(json, Constantes.Json.ACTIVO));
        resul.setUltimaActualizacion(utilJson.getDate(json, Constantes.Json.ULTIMA_ACTUALIZACION));

        return resul;
    }


}
