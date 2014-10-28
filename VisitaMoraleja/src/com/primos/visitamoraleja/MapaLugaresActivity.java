package com.primos.visitamoraleja;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
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
import com.primos.visitamoraleja.util.ObjRuta.ResultadosRuta;
import com.primos.visitamoraleja.util.UtilMapas;
import com.primos.visitamoraleja.util.UtilPreferencias;

/**
 * Clase que se encarga de mostrar los datos (Foto, nombre, descripcion y coordenadas) de un lugar
 * almacenado. Esta clase pude ser llamada desde ListaLugaresActivity o al pulsar sobre
 * un marcador en MapaLugaresActivity
 */
public class MapaLugaresActivity extends FragmentActivity implements LocationListener {
	public final static String ID_RECIBIDO = "idRecibido";
	public final static String ORIGEN = "origen";
	
	public final static String COCHE = "driving";
	private final static String BICI = "bicycling";
	private final static String ANDANDO = "walking";
	private final static String TAG = "[MapaLugaresActivity]";
	private GoogleMap map;
	// Cursor cursor;
	private long idRecibido;
	private String origen;
	
	private double latitudDestino;
	private double longitudDestino;
	private ObjRuta objRuta;
	private String nombreSitio;
	private LatLng posActual;
	private LocationManager locationManager;
	private String proveedor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa);
		
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
	    Criteria criteria = new Criteria();
	    proveedor = locationManager.getBestProvider(criteria, false);
		
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
        	idRecibido = llamadas.getLongExtra(ID_RECIBIDO, -1);
        	origen = llamadas.getStringExtra(ORIGEN);

        	nombreSitio = llamadas.getStringExtra("nombre");
        	insertaMarcador(map, nombreSitio, latitudDestino, longitudDestino);
        	setTitle(nombreSitio);

        	LatLng punto = getLatLngDestino();

        	// Centramos mapa en el punto elegido
        	CameraPosition camPos = new CameraPosition.Builder().target(punto)
        			.zoom(15) // Establecemos el zoom en 19
        			// .bearing(45) // Establecemos la orientacion con el noreste arriba
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
        		// Marcamos la opcion de transporte, que puede no ser la configurada
        		// si ha cambiado la configuracion
        		marcaCheck(medioTransporte);
        		mostrarRuta(medioTransporte);
        	}
        }
	} // Oncreate
	
	/**
	 * Devuelve las coordenadas de destino
	 * @return
	 */
	private LatLng getLatLngDestino () {
		return new LatLng(latitudDestino, longitudDestino);
	}

	/**
	 * Metodo que inserta un Marker en el mapa pasandole el mapa, el id para localizarlo
	 * despues, el titulo, y las coordenadas
	 * @param mapa
	 * @param titulo
	 * @param lat
	 * @param lon
	 */
	private void insertaMarcador(GoogleMap mapa, String titulo, double lat,
			double lon) {
		mapa.addMarker(new MarkerOptions().position(new LatLng(lat, lon))
				.title(titulo));
	}
	
	private boolean isLocationActivated() {
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
				locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}
	
	private void mostrarAvisoLocalizacionInhabilitada() {
		Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("GPS inhabilitado");
		dialog.setMessage("Para poder calcular la ruta es necesario activar la localizacion.");
	    dialog.show();
	}
	
	/**
	 * Devuelve la posicion actual si es posible, null si no lo es
	 * @return
	 */
	private LatLng getPosActual() {
		posActual = null;
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
		proveedor = locationManager.getBestProvider(criteria, true);
		if(proveedor != null) {
			// Almacenamos la ultima posicion uqe se obtuvo a traves del
			// proveedor de busquedas asignado
			Location posicionActual = locationManager
					.getLastKnownLocation(proveedor);

			if(posicionActual != null) {
				posActual = new LatLng(posicionActual.getLatitude(), posicionActual.getLongitude());
			}
		}
		return posActual;
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
	
	/**
	 * Desmarca todas las opciones de transporte
	 */
	private void uncheckOpciones() {
		RadioGroup radioGroup = (RadioGroup)findViewById(R.id.rgnMostrarOpciones);
		radioGroup.clearCheck();
	}
	
	/**
	 * Marca como checkeada la opcion correspondiente segun el Stringrecibido
	 * @param view
	 */
	private void marcaCheck(String opcion) {
		RadioGroup radioGroup = (RadioGroup)findViewById(R.id.rgnMostrarOpciones);
		int idopcion = -1;
		if(COCHE.equals(opcion)) {
			idopcion = R.id.rbRutaCoche;
		} else if(BICI.equals(opcion)) {
			idopcion = R.id.rbRutaBici;
		} else if(ANDANDO.equals(opcion)) {
			idopcion = R.id.rbRutaAndando;
		}
		radioGroup.check(idopcion);
	}
	
	public void mostrarIndicacionesRuta(View view) {
		Dialog myDialog = new Dialog(this);
		myDialog.setTitle(R.string.title_dialog_indicaciones_ruta);
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
		insertaMarcador(map, nombreSitio, latitudDestino, longitudDestino);
		return objRuta;
	}
	
	/**
	 * Si no ha sido posible calcular la ruta en googlemaps, intentamos conseguir la posicion actual. Si la conseguimos
	 * calculamos la distancia.
	 */
	private void mostrarDistancia(String mensaje) {
    	UtilMapas utilMapas = new UtilMapas();
    	// Si no tenemos resultado de la consulta a google, al menos calculamos la distancia si tenemos la posicion actual.
    	LatLng posActual = getPosActual();
    	String mensajeCompleto = mensaje;
    	if(posActual != null) {
    		LatLng posDestino = getLatLngDestino();
    		// Distancia en metros
    		double distancia = utilMapas.calculaDistancia(posActual, posDestino);
    		String unidades = " m";
    		if (distancia > 1000) {
    			distancia = utilMapas.convertirMetrosKilometros(distancia);
    			unidades = " km";
    		}
        	mensajeCompleto += " La distancia aproximada es " + distancia + unidades;
    	}
		Toast.makeText(
				MapaLugaresActivity.this, mensajeCompleto,
				Toast.LENGTH_LONG).show();
	}
	
	private void cambiarVisibilidadBotonIndicaciones(int visibilidad) {
		Button boton = (Button)findViewById(R.id.btnIndicacionesRuta);
		boton.setVisibility(visibilidad);
	}
	
	private void pintarRuta(String result, String modo) {
    	UtilMapas utilMapas = new UtilMapas();
        if(result == null){
        	mostrarDistancia(" No ha sido posible calcular la ruta. Lo sentimos.");
	        // Desmarcamos todas las opcioens de la ruta, por si no es posible calcularla
	        uncheckOpciones();
        } else {
        	objRuta = utilMapas.crearDatosRuta(result);
        	// Si la ruta es en bici y no se ha encontrado una ruta se busca la ruta andando
        	if(BICI.equals(modo) && objRuta.getResultadoRuta() == ResultadosRuta.SIN_RUTA_EN_GMAP) {
        		CalcRutaAsyncTask calcRutaAyncTask = new CalcRutaAsyncTask(ANDANDO);
        		calcRutaAyncTask.execute((Void)null);
        		String mensaje = "No se ha podido calcular la ruta en bici. Se cambia a una ruta andando.";
				Toast.makeText(
						MapaLugaresActivity.this, mensaje,
						Toast.LENGTH_LONG).show();
				marcaCheck(ANDANDO);
        	} else if(objRuta.getResultadoRuta() == ResultadosRuta.OK) {
        		pintarRuta(objRuta);
        		cambiarVisibilidadBotonIndicaciones(View.VISIBLE);
	        	// Se muestra el mensaje con información
	        	String mensaje = " La distancia es: " + objRuta.getDistancia() + "\n"
	        			+ " El tiempo estimado es: " + objRuta.getTiempo();
				Toast.makeText(
						MapaLugaresActivity.this, mensaje,
						Toast.LENGTH_SHORT).show();
        	} else {
        		mostrarDistancia(" No se ha podido calcular la ruta para el medio de transporte indicado.");
    	        // Desmarcamos todas las opcioens de la ruta, por si no es posible calcularla
    	        uncheckOpciones();
        	}
        }
//        return resul;
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
			if(!isLocationActivated()) {
				mostrarAvisoLocalizacionInhabilitada();
			} else {
		        progressDialog = new ProgressDialog(MapaLugaresActivity.this);
		        progressDialog.setMessage("Calculando la ruta, por favor espera...");
		        progressDialog.setIndeterminate(true);
		        progressDialog.show();
		        
		        UtilMapas utilMapas = new UtilMapas();
	        	LatLng posDestino = new LatLng(latitudDestino, longitudDestino);
	        	LatLng posActual = getPosActual();
	        	if(posActual != null) {
	        		url = utilMapas.montarUrlPeticionRutaJSON(posActual, posDestino, modo);
	        	}
			}
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
			if(progressDialog != null) {
				progressDialog.hide();
			}
			if(isLocationActivated()) {
				pintarRuta(result, modo);
			}
	    }
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Class<?> clase = null;
		String nombreParametro = null;
		Intent volver = null;
		if(DetalleEventoActivity.SITIO.equals(origen)) {
			clase = DetalleEventoActivity.class;
			nombreParametro = DetalleEventoActivity.ID_SITIO;
		} else if(DetalleEventoTemporalActivity.EVENTO.equals(origen)) {
			clase = DetalleEventoTemporalActivity.class;
			nombreParametro = DetalleEventoTemporalActivity.ID_EVENTO;
		}
		volver = new Intent(this, clase);
		volver.putExtra(nombreParametro, idRecibido);
		 
		startActivity(volver);
	}

	@Override
	public void onLocationChanged(Location location) {
		posActual = new LatLng(location.getLatitude(), location.getLongitude());
        Log.d(TAG, "Localizacion cambiada: Latitud: " + posActual.latitude + " : Longitud" + posActual.longitude);
	}

	@Override
	public void onProviderDisabled(String provider) {
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		locationManager.requestLocationUpdates(proveedor, 400, 1, this);
	}
}// MapaLugaresActivity