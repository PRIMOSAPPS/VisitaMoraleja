package com.primos.visitamoraleja.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

public class UtilMapas {
	private final static String URL_MAPS_GOOGLE_APIS_JSON = "http://maps.googleapis.com/maps/api/directions/json";
	private final static String AMP_PARAM_ORIGIN = "?origin=";
	private final static String AMP_PARAM_DSETINATION = "&destination=";
	private final static String TAG = "[UtilMapas]";
	
//	private MapaLugaresActivity actividad;
//	private GoogleMap map;

	public UtilMapas() {
	}

//	public UtilMapas(GoogleMap map, MapaLugaresActivity actividad) {
//		this.map = map;
//		this.actividad = actividad;
//	}
	
//	public void pintaRuta(LatLng posActual, LatLng posDestino, String modo) {
//		String url = makeURL(posActual, posDestino, modo);
//		CalcRutaAsyncTask asyncTask  =new CalcRutaAsyncTask(url, modo);
//		asyncTask.execute((Void)null);
//	}
	
	/**
	 * Calcula la distancia entre dos coordenadas, el valor devuelto es en metros
	 * @param posicion1
	 * @param posicion2
	 * @return
	 */
	public double calculaDistancia(LatLng posicion1, LatLng posicion2) {
		float[] resultados = new float[3];
		Location.distanceBetween(posicion1.latitude, posicion1.longitude,
				posicion2.latitude, posicion2.longitude, resultados);
		BigDecimal bd = new BigDecimal(resultados[0]);// resultados en metros
		BigDecimal rounded = bd.setScale(2, RoundingMode.HALF_UP);
		double distancia = rounded.doubleValue();

		return distancia;
	}
	
	/**
	 * Convierte la distancia de metros a kilometros si esta es mayor de 1000
	 * @param distanciaMetros
	 * @return
	 */
	public double convertirMetrosKilometros(double distanciaMetros) {
		double resultado = distanciaMetros;
		if (distanciaMetros > 1000) {
			// Convertimos de metros a Kilometros
			double distancia = (Double) (distanciaMetros * 0.001f);
			BigDecimal bd = new BigDecimal(distancia);
			BigDecimal rounded = bd.setScale(2, RoundingMode.HALF_UP);
			resultado = rounded.doubleValue();
		}
		return resultado;
	}

	/**
	 * Monta la URL para pedir al API de google la ruta para llegar desde posActual hasta posDestino
	 * @param posActual
	 * @param posDestino
	 * @param modo
	 * @return
	 */
	public String montarUrlPeticionRutaJSON (LatLng posActual, LatLng posDestino, String modo) {
		String resul = null;
		if( posActual != null && posDestino != null && modo != null) {
			double latitudOrigen = posActual.latitude;
			double longitudOrigen = posActual.longitude;
			double latitudDestino = posDestino.latitude;
			double longitudDestino = posDestino.longitude;
	
	        StringBuilder urlString = new StringBuilder();
	        urlString.append(URL_MAPS_GOOGLE_APIS_JSON);
	        urlString.append(AMP_PARAM_ORIGIN);// from
	        urlString.append(Double.toString(latitudOrigen));
	        urlString.append(",");
	        urlString
	                .append(Double.toString( longitudOrigen));
	        urlString.append(AMP_PARAM_DSETINATION);// to
	        urlString
	                .append(Double.toString( latitudDestino));
	        urlString.append(",");
	        urlString.append(Double.toString( longitudDestino));
	        urlString.append("&sensor=false&mode=" + modo + "&alternatives=true");
	        Log.d(TAG, "URL: " + urlString.toString());
	        resul = urlString.toString();
		}
        return resul;
	}
	
	/**
	 * Crea el objeto ObjRuta con los que necesitamos para mostrar al usuario, en este caso: distancia, duracion,
	 * lista de puntos para pintar la ruta y la lista de indicaciones en texto.
	 * @param result
	 * @return
	 */
	public ObjRuta crearDatosRuta(String result) {
		ObjRuta resul = new ObjRuta();
	    try {
            //Tranform the string into a json object
           final JSONObject json = new JSONObject(result);
           JSONArray routeArray = json.getJSONArray("routes");
           JSONObject routes = routeArray.getJSONObject(0);
           
           // Conseguimos los pasos en texto, el tiempo y la distancia
           JSONArray pasosArray = routes.getJSONArray("legs");
           JSONObject pasosObject = pasosArray.getJSONObject(0);
           JSONObject distancia = pasosObject.getJSONObject("distance");
           JSONObject duracion = pasosObject.getJSONObject("duration");
           resul.setDistancia(distancia.getString("text"));
           resul.setTiempo(duracion.getString("text"));
           JSONArray legsArray = pasosObject.getJSONArray("steps");

           // Recogemos las instrucciones en texto
           recogerPasosTexto(resul, legsArray);
           
           // Conseguimos los puntos para pintar el trazado de la ruta
           recogerPasosLatLong(resul, routes);
	    } catch (JSONException e) {
	    	Log.e(TAG, "Error en el parseo de JSON: " + e.getMessage(), e);
	    }
		return resul;
	}
	
	/**
	 * Recoge de la respuesta en JSON las instrucciones en texto para llegar desde un punto hasta otro.
	 * @param objRuta
	 * @param arrPasos
	 * @throws JSONException
	 */
	private void recogerPasosTexto(ObjRuta objRuta, JSONArray arrPasos) throws JSONException {
		int numPasos = arrPasos.length();
		for(int i=0; i<numPasos; i++) {
			JSONObject paso = arrPasos.getJSONObject(i);
			objRuta.addPasoTexto(paso.getString("html_instructions"));
		}
	}
	
	/**
	 * Recoge de la respuesta en JSON la lista de puntos para pintar la ruta.
	 * @param objRuta
	 * @param routes
	 * @throws JSONException
	 */
	private void recogerPasosLatLong(ObjRuta objRuta, JSONObject routes) throws JSONException {
        JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
        String encodedString = overviewPolylines.getString("points");
        List<LatLng> list = decodePoly(encodedString);
        
        //map.clear();
        Log.d(TAG, "Numero de puntos: " + list.size());
        for(int z = 0; z<list.size()-1;z++){
             LatLng src= list.get(z);
             LatLng dest= list.get(z+1);
             PolylineOptions plo = new PolylineOptions();
             plo.add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude,   dest.longitude));
             objRuta.addPolyLineOptions(plo);

//             Polyline line = map.addPolyline(new PolylineOptions()
//             .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude,   dest.longitude))
//             .width(4)
//             .color(Color.BLUE).geodesic(true));
         }
	}
	
//	private ObjRuta pintarRuta(String result) {
//		ObjRuta objRuta = crearDatosRuta(result);
//		map.clear();
//		List<PolylineOptions> lstLatLong = objRuta.getLstLatLong();
//		for(PolylineOptions polyLineOption : lstLatLong) {
//			polyLineOption.width(4);
//			polyLineOption.color(Color.BLUE);
//			polyLineOption.geodesic(true);
//            Polyline line = map.addPolyline(polyLineOption);
//
//		}
//		return objRuta;
//	}

	/*
	private void pintarRuta(String result) {
//		PolylineOptions po = new PolylineOptions();
//		po.add(new LatLng(lat-1, lon));
//		po.add(new LatLng(lat, lon-1));
//		po.add(new LatLng(lat, lon));
//		map.addPolyline(po);
//		Point posActual = new Point(x, y);
//		map.getProjection().fromScreenLocation(new Point(5, 5) );
//		LatLng posFinal = new LatLng(lat, lon);
//		map.getProjection().toScreenLocation(posFinal);
		

	    try {
            //Tranform the string into a json object
           final JSONObject json = new JSONObject(result);
           JSONArray routeArray = json.getJSONArray("routes");
           JSONObject routes = routeArray.getJSONObject(0);
           JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
           String encodedString = overviewPolylines.getString("points");
           List<LatLng> list = decodePoly(encodedString);
           
           map.clear();
           Log.d(TAG, "Poliline: " + list.size());
           for(int z = 0; z<list.size()-1;z++){
                LatLng src= list.get(z);
                LatLng dest= list.get(z+1);
                Polyline line = map.addPolyline(new PolylineOptions()
                .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude,   dest.longitude))
                .width(4)
                .color(Color.BLUE).geodesic(true));
            }

	    } 
	    catch (JSONException e) {

	    }
	}
	*/
	
	private List<LatLng> decodePoly(String encoded) {

	    List<LatLng> poly = new ArrayList<LatLng>();
	    int index = 0, len = encoded.length();
	    int lat = 0, lng = 0;

	    while (index < len) {
	        int b, shift = 0, result = 0;
	        do {
	            b = encoded.charAt(index++) - 63;
	            result |= (b & 0x1f) << shift;
	            shift += 5;
	        } while (b >= 0x20);
	        int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	        lat += dlat;

	        shift = 0;
	        result = 0;
	        do {
	            b = encoded.charAt(index++) - 63;
	            result |= (b & 0x1f) << shift;
	            shift += 5;
	        } while (b >= 0x20);
	        int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	        lng += dlng;

	        LatLng p = new LatLng( (((double) lat / 1E5)),
	                 (((double) lng / 1E5) ));
	        poly.add(p);
	    }

	    return poly;
	}
	
	
//	private class CalcRutaAsyncTask extends AsyncTask<Void, Void, String>{
//	    private ProgressDialog progressDialog;
//	    private String url;
//	    private String modo;
//	    
//	    CalcRutaAsyncTask(String url, String modo){
//	        this.url = url;
//	        this.modo = modo;
//	    }
//	    @Override
//	    protected void onPreExecute() {
//	        super.onPreExecute();
//	        progressDialog = new ProgressDialog(actividad);
//	        progressDialog.setMessage("Calculando la ruta, por favor espera...");
//	        progressDialog.setIndeterminate(true);
//	        progressDialog.show();
//	    }
//	    @Override
//	    protected String doInBackground(Void... params) {
//	        JSONParser jParser = new JSONParser();
//	        return jParser.getJSONFromUrl(url);
//	    }
//
//	    @Override
//	    protected void onPostExecute(String result) {
//	        super.onPostExecute(result);   
//	        progressDialog.hide();        
//	        if(result!=null){
//	        	ObjRuta objRuta = pintarRuta(result);
//	        	//actividad.setObjRuta(objRuta);
////	        	actividad.rutaPintada(modo);
//	        }
//	    }
//	}

}
