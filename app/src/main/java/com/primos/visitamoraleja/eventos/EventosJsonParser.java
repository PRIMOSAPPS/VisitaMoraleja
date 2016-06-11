package com.primos.visitamoraleja.eventos;

import android.util.Base64;
import android.util.Log;

import com.primos.visitamoraleja.constantes.Constantes;
import com.primos.visitamoraleja.contenidos.Evento;
import com.primos.visitamoraleja.contenidos.FormaEvento;
import com.primos.visitamoraleja.dto.EventoActualizableDTO;
import com.primos.visitamoraleja.util.ConversionesUtil;
import com.primos.visitamoraleja.util.UtilFechas;
import com.primos.visitamoraleja.util.UtilJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by h on 5/06/16.
 */
public class EventosJsonParser {
    private final static String TAG = "EventosJsonParser";

    public List<Evento> parseEventos(InputStream is) {
        List<Evento> resul = new ArrayList<>();

        try {
            String strJson = isToString(is);
            JSONArray json = new JSONArray(strJson);
            for(int i=0; i<json.length(); i++) {
                JSONObject jsonEvento = json.getJSONObject(i);
            }
        } catch(Exception e) {
            Log.e(TAG, "Error al parserar la respuesta del servidor.", e);
        }

        return resul;
    }


    public List<EventoActualizableDTO> parseEventosActualizables(InputStream is) {
        List<EventoActualizableDTO> resul = new ArrayList<>();

        try {
            String strJson = isToString(is);
            JSONArray json = new JSONArray(strJson);
            for(int i=0; i<json.length(); i++) {
                JSONObject jsonEvento = json.getJSONObject(i);
                EventoActualizableDTO eventoActualizable = parseEventoActualizable(jsonEvento);
                resul.add(eventoActualizable);
            }
        } catch(Exception e) {
            Log.e(TAG, "Error al parserar la respuesta del servidor.", e);
        }

        return resul;
    }

    private EventoActualizableDTO parseEventoActualizable(JSONObject jsonEvento) throws JSONException, ParseException {
        EventoActualizableDTO resul = new EventoActualizableDTO();
        UtilJson utilJson = new UtilJson();

        resul.setId(jsonEvento.getLong(Constantes.Json.ID));
        resul.setNombre(utilJson.getStringFromBase64(jsonEvento, Constantes.Json.NOMBRE));
        resul.setActivo(utilJson.getBoolean(jsonEvento, Constantes.Json.ACTIVO));
        resul.setUltimaActualizacion(utilJson.getDate(jsonEvento, Constantes.Json.ULTIMA_ACTUALIZACION));
        resul.setIdsSitios(utilJson.getListaLong(jsonEvento, Constantes.Json.IDS_SITIOS));
        resul.setIdsImagenes(utilJson.getListaLong(jsonEvento, Constantes.Json.IDS_IMAGENES));

        List<FormaEvento> formas = new ArrayList<>();
        JSONArray jsonFormas = jsonEvento.getJSONArray(Constantes.Json.FORMAS_EVENTOS);
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

    private String isToString(InputStream is) throws IOException{
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuilder responseStrBuilder = new StringBuilder();

        String inputStr;
        while ((inputStr = streamReader.readLine()) != null) {
            responseStrBuilder.append(inputStr);
        }

        return responseStrBuilder.toString();
    }
}
