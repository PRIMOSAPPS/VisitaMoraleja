package com.primos.visitamoraleja;
import java.util.List;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.primos.visitamoraleja.prueba.JSONParser;
import com.primos.visitamoraleja.util.ObjRuta;
import com.primos.visitamoraleja.util.UtilMapas;
import com.primos.visitamoraleja.util.UtilPreferencias;

/* Clase que se encarga de mostrar los datos (Foto, nombre, descripcion y coordenadas) de un lugar
 * almacenado. Esta clase pude ser llamada desde ListaLugaresActivity o al pulsar sobre
 * un marcador en MapaLugaresActivity
 */
public class MapaLugaresActivity extends FragmentActivity {
	public final static String COCHE = "driving";
	private final static String BICI = "bicycling";
	private final static String ANDANDO = "walking";
	private final static String TAG = "[MapaLugaresActivity]";
	private GoogleMap map;
	// Cursor cursor;
	private String id;
	
	private double latitudDestino;
	private double longitudDestino;
	private ObjRuta objRuta;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa);
		
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
		 
        // Showing status
        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
        } else {
        	map = ((SupportMapFragment) getSupportFragmentManager()
        			.findFragmentById(R.id.map)).getMap();

        	map.setMyLocationEnabled(true);
        	Projection p = map.getProjection();

        	// Recogemos el intent por si fue llamado desde otra activity
        	Intent llamadas = getIntent();

        	// Si el mapa ha sido llamado desde MostrarLugarActivity
        	// vamos directamente al punto en el mapa.

        	latitudDestino = llamadas.getDoubleExtra("latitud", 0);
        	longitudDestino = llamadas.getDoubleExtra("longitud", 0);

        	String nombreSitio = llamadas.getStringExtra("nombre");
        	insertaMarcador(map, nombreSitio, latitudDestino, longitudDestino);
        	setTitle(nombreSitio);

        	LatLng punto = getLatLngDestino();

        	// Centramos mapa en el punto elegido
        	CameraPosition camPos = new CameraPosition.Builder().target(punto)
        			.zoom(15) // Establecemos el zoom en 19
        			// .bearing(45) // Establecemos la orientacion con el noreste
        			// arriba
        			.tilt(30) // Bajamos el punto de vista de la camara 70 grados
        			.build();
        	CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
        	map.animateCamera(camUpd3);

        	// Muestra las coordenadas de un punto cuando hacemos una pulsacion
        	// larga sobre el mapa

        	map.setOnMapLongClickListener(new OnMapLongClickListener() {
        		public void onMapLongClick(LatLng point) {
        			Toast.makeText(
        					MapaLugaresActivity.this,
        					getString(R.string.coordenadas) + " \n " + "Lat: "
        							+ point.latitude + "\n" + "Lng: "
        							+ point.longitude, Toast.LENGTH_SHORT).show();
        		} // onMapLongClick
        	});
        	
        	boolean calcularAutomaticamente = UtilPreferencias.isCalcularRutaAutomaticamente(this);
        	if(calcularAutomaticamente) {
        		String medioTransporte = UtilPreferencias.getMedioTransporteDefectoRuta(this);
        		mostrarRuta(medioTransporte);
        	}
        }
	} // Oncreate
	
	private LatLng getLatLngDestino () {
		return new LatLng(latitudDestino, longitudDestino);
	}

	// Metodo que inserta un Marker en el mapa pasandole el mapa, el id para
	// localizarlo
	// despues en la Base de Datos, el titulo, y las coordenadas
	private void insertaMarcador(GoogleMap mapa, String titulo, double lat,
			double lon) {
		mapa.addMarker(new MarkerOptions().position(new LatLng(lat, lon))
				.title(titulo));
	}
	
	private LatLng getPosActual() {
		LatLng resul = null;
		// Obtenemos una referencia al LocationManager y creamos el objeto
		// para gestionar las localizaciones
		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		// Definimos un objeto de la clase Criteria para decidir que
		// caracteristicas tiene que tener
		// nuestro proveedor de localizacion en este caso queremos obtener
		// uno con bastante precision
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);

		// Cargamos el nombre del proveedor obtenido en un proveedor
		String proveedor = locationManager.getBestProvider(criteria, true);
		if(proveedor != null) {
			// Almacenamos la ultima posicion uqe se obtuvo a traves del
			// proveedor de busquedas asignado
			Location posicionActual = locationManager
					.getLastKnownLocation(proveedor);

			if(posicionActual != null) {
				resul = new LatLng(posicionActual.getLatitude(), posicionActual.getLongitude());
			}
		}
		return resul;
	}
	
	///////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////
	public void mostrarOpciones(View view) {
		View btnMostrarOpciones = findViewById(R.id.btnMostrarOpciones);
		View opciones = findViewById(R.id.lyOpcionesMapa);
		int visible = View.VISIBLE;
		int backGroundBtnOpciones = R.drawable.ic_actionbar_ocultar_opciones;
		if(opciones.getVisibility() == View.VISIBLE) {
			visible = View.GONE;
			backGroundBtnOpciones = R.drawable.ic_actionbar_mostrar_opciones;
		}
		opciones.setVisibility(visible);
		btnMostrarOpciones.setBackgroundResource(backGroundBtnOpciones);
	}
	
	private void mostrarRuta(String modo) {
		CalcRutaAsyncTask calcRutaAyncTask = new CalcRutaAsyncTask(modo);
		calcRutaAyncTask.execute((Void)null);
	}
	
	public void mostrarIndicacionesRuta(View view) {
		Dialog myDialog = new Dialog(this);
	    myDialog.setContentView(R.layout.lista_instrucciones_ruta);
	    ListView lv = (ListView)myDialog.findViewById(R.id.lvListaInstrucciones);
	    final List<String> listaPasos = objRuta.getLstPasosTexto();
	    ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
	    		listaPasos) {
	    	public String getItem(int position) {
	             return Html.fromHtml(listaPasos.get(position)).toString();
	        }
	    };
	    lv.setAdapter(adapter);
	    myDialog.show();
	}
	
	public void mostrarRuta(View view) {
		String modo = (String)view.getTag();
		//calculaDistancia(latitudDestino, longitudDestino, modo);
		mostrarRuta(modo);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detalle_evento, menu);
		return true;
	}
	
	private ObjRuta pintarRuta(ObjRuta objRuta) {
		map.clear();
		List<PolylineOptions> lstLatLong = objRuta.getLstLatLong();
		for(PolylineOptions polyLineOption : lstLatLong) {
			polyLineOption.width(4);
			polyLineOption.color(Color.BLUE);
			polyLineOption.geodesic(true);
            Polyline line = map.addPolyline(polyLineOption);

		}
		return objRuta;
	}
	
	private void pintarRuta(String result, String modo) {
    	UtilMapas utilMapas = new UtilMapas();
        if(result == null){
        	// Si no tenemos resultado de la consulta a google, al menos calculamos la distancia si tenemos la posicion actual.
        	LatLng posActual = getPosActual();
        	if(posActual != null) {
        		LatLng posDestino = getLatLngDestino();
        		// Distancia en metros
        		double distancia = utilMapas.calculaDistancia(posActual, posDestino);
        		String unidades = " m";
        		if (distancia > 1000) {
        			distancia = utilMapas.convertirMetrosKilometros(distancia);
        			unidades = " km";
        		}
            	String mensaje = " No ha sido posible calcula la ruta. La distancia aproximada es " + distancia + unidades;
    			Toast.makeText(
    					MapaLugaresActivity.this,
    					mensaje,
    					Toast.LENGTH_SHORT).show();
        	}
        } else {
        	objRuta = utilMapas.crearDatosRuta(result);
        	pintarRuta(objRuta);
        	
        	// Se muestra el mensaje con información
        	String mensaje = " La distancia es: " + objRuta.getDistancia() + "\n"
        			+ " El tiempo estimado es: " + objRuta.getTiempo();
			Toast.makeText(
					MapaLugaresActivity.this,
					mensaje,
					Toast.LENGTH_SHORT).show();

        }
	}
	
	/**
	 * Clase qe realiza la llamada al API de google para conseguir losdatos de la ruta en formato JSON.
	 * @author h
	 *
	 */
	private class CalcRutaAsyncTask extends AsyncTask<Void, Void, String>{
	    private ProgressDialog progressDialog;
	    private String url;
	    private String modo;

	    CalcRutaAsyncTask(String modo){
	        this.modo = modo;
	    }

	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        progressDialog = new ProgressDialog(MapaLugaresActivity.this);
	        progressDialog.setMessage("Calculando la ruta, por favor espera...");
	        progressDialog.setIndeterminate(true);
	        progressDialog.show();
	        
	        UtilMapas utilMapas = new UtilMapas();
        	LatLng posDestino = new LatLng(latitudDestino, longitudDestino);
        	LatLng posActual = getPosActual();
        	if(posActual == null) {
        		mostrarMensajeToast("No ha sido posible calcular la posición actual. Lo sentimos.");
        	} else {
        		url = utilMapas.montarUrlPeticionRutaJSON(posActual, posDestino, modo);
        	}
	    }
	    
	    private void mostrarMensajeToast(String mensaje) {
			Toast.makeText(
					MapaLugaresActivity.this,
					mensaje,
					Toast.LENGTH_SHORT).show();
		}

	    @Override
	    protected String doInBackground(Void... params) {
	    	String resul = null;
	    	if(url != null) {
		        JSONParser jParser = new JSONParser();
		        resul = jParser.getJSONFromUrl(url);
	    	}
	        return resul;
	    }

	    @Override
	    protected void onPostExecute(String result) {
	        super.onPostExecute(result);   
	        progressDialog.hide();
	        if(result == null) {
	        	mostrarMensajeToast("No ha sido posible calcular la ruta. Lo sentimos.");
	        } else {
	        	pintarRuta(result, modo);
	        }
	    }
	}
}// MapaLugaresActivity