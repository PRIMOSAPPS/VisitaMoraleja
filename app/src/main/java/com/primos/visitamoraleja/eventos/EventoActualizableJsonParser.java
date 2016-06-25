package com.primos.visitamoraleja.eventos;

import com.primos.visitamoraleja.constantes.Constantes;
import com.primos.visitamoraleja.contenidos.FormaEvento;
import com.primos.visitamoraleja.contenidos.SitioEvento;
import com.primos.visitamoraleja.dto.EventoActualizableDTO;
import com.primos.visitamoraleja.util.ConversionesUtil;
import com.primos.visitamoraleja.util.UtilJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by h on 5/06/16.
 */
public class EventoActualizableJsonParser extends AbstractJsonParser<EventoActualizableDTO> {
    private final static String TAG = "EventoActualizableJsonParser";

    public EventoActualizableDTO parse(JSONObject json) throws JSONException, ParseException {
        EventoActualizableDTO resul = new EventoActualizableDTO();
        UtilJson utilJson = new UtilJson();

        resul.setId(json.getLong(Constantes.Json.ID));
        resul.setNombre(utilJson.getStringFromBase64(json, Constantes.Json.NOMBRE));
        resul.setActivo(utilJson.getBoolean(json, Constantes.Json.ACTIVO));
        resul.setUltimaActualizacion(utilJson.getDate(json, Constantes.Json.ULTIMA_ACTUALIZACION));
        resul.setIdsSitios(utilJson.getListaLong(json, Constantes.Json.IDS_SITIOS));
        resul.setIdsImagenes(utilJson.getListaLong(json, Constantes.Json.IDS_IMAGENES));
        resul.setIdsCategoriasEvento(utilJson.getListaLong(json, Constantes.Json.IDS_CATEGORIAS_EVENTO));
        resul.setIdsActividades(utilJson.getListaLong(json, Constantes.Json.IDS_ACTIVIDADES));


        List<FormaEvento> formas = new ArrayList<>();
        JSONArray jsonFormas = json.getJSONArray(Constantes.Json.FORMAS_EVENTOS);
        for (int i = 0; i < jsonFormas.length(); i++) {
            JSONObject jsonForma = jsonFormas.getJSONObject(i);
            FormaEvento forma = parseFormaEvento(jsonForma);
            if(forma != null) {
                formas.add(forma);
            }
        }
        resul.setFormas(formas);

        return resul;
    }

    private FormaEvento parseFormaEvento(JSONObject jsonFormaEvento) throws JSONException, ParseException {
        FormaEvento resul = new FormaEvento();

        UtilJson utilJson = new UtilJson();
        resul.setId(jsonFormaEvento.getLong(Constantes.Json.ID));
        resul.setIdEvento(jsonFormaEvento.getLong(Constantes.Json.ID_EVENTO));
        resul.setIdCategoriaEvento(jsonFormaEvento.getLong(Constantes.Json.ID_CATEGORIA_EVENTO));
        resul.setTipoForma(jsonFormaEvento.getString(Constantes.Json.TIPO_FORMA));
        resul.setColorRelleno(jsonFormaEvento.getString(Constantes.Json.COLOR_RELLENO));
        resul.setColorLinea(jsonFormaEvento.getString(Constantes.Json.COLOR_LINEA));
        resul.setGrosorLinea(jsonFormaEvento.getString(Constantes.Json.GROSOR_LINEA));
        resul.setTexto(utilJson.getStringFromBase64(jsonFormaEvento, Constantes.Json.TEXTO));
        resul.setCoordenadas(jsonFormaEvento.getString(Constantes.Json.COORDENADAS));
        resul.setActivo(utilJson.getBoolean(jsonFormaEvento, Constantes.Json.ACTIVO));
        resul.setUltimaActualizacion(utilJson.getDate(jsonFormaEvento, Constantes.Json.ULTIMA_ACTUALIZACION));

        return resul;
    }

}
