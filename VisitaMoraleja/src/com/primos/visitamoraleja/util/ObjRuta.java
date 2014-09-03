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
	// De moemnto solo se usan OK y SIN_RUTA_EN_GMAP, pero dejo definidos el resto por si los usamos
	public enum ResultadosRuta {OK, SIN_POS_ACTUAL, SIN_CONEXION_GMAP, ERROR_PARSEO_JSON, SIN_RUTA_EN_GMAP};

	private ResultadosRuta resultadoRuta;
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

	public ResultadosRuta getResultadoRuta() {
		return resultadoRuta;
	}

	public void setResultadoRuta(ResultadosRuta resultadoRuta) {
		this.resultadoRuta = resultadoRuta;
	}

	public void setStatus(String status) {
		if(status == null) {
			this.resultadoRuta = ResultadosRuta.SIN_CONEXION_GMAP;
		} else if(status.equals("OK")) {
			this.resultadoRuta = ResultadosRuta.OK;
		} else if(status.equals("ZERO_RESULTS")) {
			this.resultadoRuta = ResultadosRuta.SIN_RUTA_EN_GMAP;
		}
	}

}
