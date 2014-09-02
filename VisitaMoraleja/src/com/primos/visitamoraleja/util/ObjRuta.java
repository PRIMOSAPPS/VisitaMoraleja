package com.primos.visitamoraleja.util;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Clase que guardara los datos que nos interesan de la respuesta en JSON.
 * De momento, los datos que nos interesan son la distancia y el tiempo estimado según la respuesta de google.
 * Tambien guardamos la lista de puntos que forman la ruta y la lista de indicaciones en texto claro.
 * @author h
 *
 */
public class ObjRuta {
	private String distancia;
	private String tiempo;
	private List<String> lstPasosTexto = new ArrayList<>();
	private List<PolylineOptions> lstPolyLineOptions = new ArrayList<>();

	public String getDistancia() {
		return distancia;
	}

	public List<String> getLstPasosTexto() {
		return lstPasosTexto;
	}

	public List<PolylineOptions> getLstLatLong() {
		return lstPolyLineOptions;
	}

	public void setDistancia(String distancia) {
		this.distancia = distancia;
	}

	public String getTiempo() {
		return tiempo;
	}

	public void setTiempo(String tiempo) {
		this.tiempo = tiempo;
	}
	
	public void addPasoTexto(String pasoTxt) {
		lstPasosTexto.add(pasoTxt);
	}
	
	public void addPolyLineOptions(PolylineOptions polyLineOption) {
		lstPolyLineOptions.add(polyLineOption);
	}

}
