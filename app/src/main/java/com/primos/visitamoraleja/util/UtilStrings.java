package com.primos.visitamoraleja.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by h on 12/06/16.
 */
public class UtilStrings {
    public static String isToString(InputStream is) throws IOException {
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuilder responseStrBuilder = new StringBuilder();

        String inputStr;
        while ((inputStr = streamReader.readLine()) != null) {
            responseStrBuilder.append(inputStr);
        }

        return responseStrBuilder.toString();
    }
}
