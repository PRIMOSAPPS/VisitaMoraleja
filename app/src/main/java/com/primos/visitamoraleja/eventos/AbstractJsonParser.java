package com.primos.visitamoraleja.eventos;

import android.util.Log;

import com.primos.visitamoraleja.contenidos.ImagenEvento;
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
 * Created by h on 24/06/16.
 */
public abstract class AbstractJsonParser<T> implements IJsonParser {
    private final static String TAG = "AbstractJsonParser";

    public abstract T parse(JSONObject json) throws JSONException, ParseException, UnsupportedEncodingException;

    public List<T> parse(InputStream is) {
        List<T> resul = new ArrayList<>();

        try {
            String strJson = UtilStrings.isToString(is);
            JSONArray json = new JSONArray(strJson);
            for(int i=0; i<json.length(); i++) {
                JSONObject jsonEvento = json.getJSONObject(i);
                T obj = parse(jsonEvento);
                resul.add(obj);
            }
        } catch(Exception e) {
            Log.e(TAG, "Error al parserar la respuesta del servidor.", e);
        }

        return resul;
    }
}
