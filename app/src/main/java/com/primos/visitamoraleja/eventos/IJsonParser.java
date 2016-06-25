package com.primos.visitamoraleja.eventos;

import java.io.InputStream;
import java.util.List;

/**
 * Created by h on 24/06/16.
 */
public interface IJsonParser<T> {
    List<T> parse(InputStream is);
}
