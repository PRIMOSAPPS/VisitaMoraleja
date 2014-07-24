package com.primos.visitamoraleja;
import java.math.BigDecimal;
import java.math.RoundingMode;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/* Clase que se encarga de mostrar los datos (Foto, nombre, descripcion y coordenadas) de un lugar
 * almacenado. Esta clase pude ser llamada desde ListaLugaresActivity o al pulsar sobre
 * un marcador en MapaLugaresActivity
 */
public class MapaLugaresActivity extends FragmentActivity {
	GoogleMap map;
	// Cursor cursor;
	String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa);
		map = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();

		map.setMyLocationEnabled(true);

		// Recogemos el intent por si fue llamado desde otra activity
		Intent llamadas = getIntent();

		// Si el mapa ha sido llamado desde MostrarLugarActivity
		// vamos directamente al punto en el mapa.

		double lt = llamadas.getDoubleExtra("latitud", 0);
		double ln = llamadas.getDoubleExtra("longitud", 0);

		insertaMarcador(map, llamadas.getStringExtra("nombre"), lt, ln);

		LatLng punto = new LatLng(lt, ln);

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
	} // Oncreate

	// Metodo que inserta un Marker en el mapa pasandole el mapa, el id para
	// localizarlo
	// despues en la Base de Datos, el titulo, y las coordenadas
	private void insertaMarcador(GoogleMap mapa, String titulo, double lat,
			double lon) {
		mapa.addMarker(new MarkerOptions().position(new LatLng(lat, lon))
				.title(titulo));

		Toast.makeText(MapaLugaresActivity.this,
				titulo + ": estas a " + calculaDistancia(lat, lon),
				Toast.LENGTH_LONG).show();
	}

	private String calculaDistancia(Double lat, Double lon) {

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

		String distanciaTexto = "No ha sido posible calcular la posicion actual.";
		if(proveedor != null) {
			// Almacenamos la ultima posicion uqe se obtuvo a traves del
			// proveedor de busquedas asignado
			Location posicionActual = locationManager
					.getLastKnownLocation(proveedor);

			if(posicionActual != null) {
				Double latitudActual = posicionActual.getLatitude();
				Double longitudActual = posicionActual.getLongitude();
	
				double endLatitude = lat;
				double endLongitude = lon;
	
				float[] results = new float[3];
				Location.distanceBetween(latitudActual, longitudActual, endLatitude,
						endLongitude, results);
	
				BigDecimal bd = new BigDecimal(results[0]);// resultados en metros
				BigDecimal rounded = bd.setScale(2, RoundingMode.HALF_UP);
				double distancia = rounded.doubleValue();
				distanciaTexto = String.valueOf(distancia)
						+ " metros de distancia.";
				if (distancia > 1000) {
					distancia = (Double) (distancia * 0.001f);// convierte de metros a
					// Kilometros
					bd = new BigDecimal(distancia);
					rounded = bd.setScale(2, RoundingMode.HALF_UP);
					distancia = rounded.doubleValue();
					distanciaTexto = String.valueOf(distancia) + " Kms. de distancia.";
				}
			}
		}

		return distanciaTexto;
	}

}// MapaLugaresActivity