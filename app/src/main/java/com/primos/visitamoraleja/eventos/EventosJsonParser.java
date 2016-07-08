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
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by h on 5/06/16.
 */
public class EventosJsonParser extends AbstractJsonParser<Evento> {
    private final static String TAG = "EventosJsonParser";

    public Evento parse(JSONObject json) throws JSONException, ParseException, UnsupportedEncodingException {
        Evento resul = new Evento();
        UtilJson utilJson = new UtilJson();

        resul.setId(json.getLong(Constantes.Json.ID));
        resul.setNombre(utilJson.getStringFromBase64(json, Constantes.Json.NOMBRE));
        resul.setTexto(utilJson.getStringFromBase64(json, Constantes.Json.TEXTO));
        resul.setDescripcion(utilJson.getStringFromBase64(json, Constantes.Json.DESCRIPCION));
        resul.setNombreIcono(utilJson.getStringFromBase64(json, Constantes.Json.NOMBRE_ICONO));
        String cadenaImagen = json.getString(Constantes.Json.ICONO);
        resul.setIcono(ConversionesUtil.getBitmap(cadenaImagen));
        resul.setLatitud(Double.parseDouble(json.getString(Constantes.Json.LATITUD)));
        resul.setLongitud(Double.parseDouble(json.getString(Constantes.Json.LONGITUD)));
        resul.setZoomInicial(Integer.parseInt(json.getString(Constantes.Json.ZOOM_INICIAL)));

        TimeZone timeZone = Calendar.getInstance().getTimeZone();
        resul.setInicio(utilJson.getDate(json, Constantes.Json.INICIO, timeZone));
        resul.setFin(utilJson.getDate(json, Constantes.Json.FIN, timeZone));
        resul.setActivo(utilJson.getBoolean(json, Constantes.Json.ACTIVO));
        resul.setUltimaActualizacion(utilJson.getDate(json, Constantes.Json.ULTIMA_ACTUALIZACION));

        return resul;
    }

    /*
    private EventoActualizableDTO parseEventoActualizable(JSONObject jsonEvento) throws JSONException, ParseException {
        EventoActualizableDTO resul = new EventoActualizableDTO();
        UtilJson utilJson = new UtilJson();

        resul.setId(jsonEvento.getLong(Constantes.Json.ID));
        resul.setNombre(utilJson.getStringFromBase64(jsonEvento, Constantes.Json.NOMBRE));
        resul.setActivo(utilJson.getBoolean(jsonEvento, Constantes.Json.ACTIVO));
        resul.setUltimaActualizacion(utilJson.getDate(jsonEvento, Constantes.Json.ULTIMA_ACTUALIZACION));
        resul.setIdsSitios(utilJson.getListaLong(jsonEvento, Constantes.Json.IDS_SITIOS));
        resul.setIdsImagenes(utilJson.getListaLong(jsonEvento, Constantes.Json.IDS_IMAGENES));
        resul.setIdsCategoriasEvento(utilJson.getListaLong(jsonEvento, Constantes.Json.IDS_CATEGORIAS_EVENTO));
        resul.setIdsActividades(utilJson.getListaLong(jsonEvento, Constantes.Json.IDS_ACTIVIDADES));


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
    */

    /*
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
    */

    /*
    private SitioEvento parseSitioEvento(JSONObject jsonSitioEvento) throws JSONException, ParseException, UnsupportedEncodingException {
        SitioEvento resul = new SitioEvento();

        UtilJson utilJson = new UtilJson();
        resul.setId(jsonSitioEvento.getLong(Constantes.Json.ID));
        resul.setIdEvento(jsonSitioEvento.getLong(Constantes.Json.ID_EVENTO));
        resul.setIdCategoriaEvento(jsonSitioEvento.getLong(Constantes.Json.ID_CATEGORIA_EVENTO));
        resul.setEsSitioRegistrado(utilJson.getBoolean(jsonSitioEvento, Constantes.Json.ES_SITIO_REGISTRADO));
        resul.setIdSitioRegistrado(jsonSitioEvento.getLong(Constantes.Json.ID_SITIO_REGISTRADO));
        resul.setNombre(utilJson.getStringFromBase64(jsonSitioEvento, Constantes.Json.NOMBRE));
        resul.setTexto(utilJson.getStringFromBase64(jsonSitioEvento, Constantes.Json.TEXTO));
        resul.setDescripcion(utilJson.getStringFromBase64(jsonSitioEvento, Constantes.Json.DESCRIPCION));
        resul.setNombreIcono(utilJson.getStringFromBase64(jsonSitioEvento, Constantes.Json.NOMBRE_ICONO));
        String cadenaImagen = jsonSitioEvento.getString(Constantes.Json.ICONO);
        resul.setIcono(ConversionesUtil.getBitmap(cadenaImagen));

        resul.setLatitud(Double.parseDouble(jsonSitioEvento.getString(Constantes.Json.LATITUD)));
        resul.setLongitud(Double.parseDouble(jsonSitioEvento.getString(Constantes.Json.LONGITUD)));
        resul.setActivo(utilJson.getBoolean(jsonSitioEvento, Constantes.Json.ACTIVO));
        resul.setUltimaActualizacion(utilJson.getDate(jsonSitioEvento, Constantes.Json.ULTIMA_ACTUALIZACION));

        return resul;
    }
    */

    /*
    private ImagenEvento parseImagenEvento(JSONObject jsonImagenEvento) throws JSONException, ParseException, UnsupportedEncodingException {
        ImagenEvento resul = new ImagenEvento();

        UtilJson utilJson = new UtilJson();
        resul.setId(jsonImagenEvento.getLong(Constantes.Json.ID));
        resul.setIdEvento(jsonImagenEvento.getLong(Constantes.Json.ID_EVENTO));
        resul.setNombre(utilJson.getStringFromBase64(jsonImagenEvento, Constantes.Json.NOMBRE));
        String cadenaImagen = jsonImagenEvento.getString(Constantes.Json.IMAGEN);
        resul.setImagen(ConversionesUtil.getBitmap(cadenaImagen));
        resul.setActivo(utilJson.getBoolean(jsonImagenEvento, Constantes.Json.ACTIVO));
        resul.setUltimaActualizacion(utilJson.getDate(jsonImagenEvento, Constantes.Json.ULTIMA_ACTUALIZACION));

        return resul;
    }
    */


}
