package com.primos.visitamoraleja.util;

import android.util.Base64;

import com.primos.visitamoraleja.constantes.Constantes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by h on 6/06/16.
 */
public class UtilJson {

    public String getStringFromBase64(JSONObject json, String campoJson) throws JSONException {
        String nombre = json.getString(campoJson);
        return new String(Base64.decode(nombre, Base64.DEFAULT));
    }

    public boolean getBoolean(JSONObject json, String campoJson) throws JSONException {
        int valor = Integer.parseInt(json.getString(campoJson));
        return ConversionesUtil.intToBoolean(valor);
    }

    public Date getDate(JSONObject json, String campoJson) throws JSONException, ParseException {
        return UtilFechas.fechaFromUTC(json.getString(campoJson));
    }


    public Date getDate(JSONObject json, String campoJson, TimeZone timeZone) throws JSONException, ParseException {
        return UtilFechas.fechaFromString(json.getString(campoJson), timeZone);
    }

    public List<Integer> getListaInteger(JSONObject json, String campoJson) throws JSONException {
        List<Integer> resul = new ArrayList<>();

        JSONArray enteros = json.getJSONArray(campoJson);
        for(int i=0; i<enteros.length(); i++) {
            resul.add(enteros.getInt(i));
        }

        return resul;
    }


    public List<Long> getListaLong(JSONObject json, String campoJson) throws JSONException {
        List<Long> resul = new ArrayList<>();

        JSONArray enteros = json.getJSONArray(campoJson);
        for(int i=0; i<enteros.length(); i++) {
            resul.add(enteros.getLong(i));
        }

        return resul;
    }
}
