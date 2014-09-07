package com.primos.visitamoraleja;

import java.util.List;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.primos.visitamoraleja.adaptadores.ImageAdapter;
import com.primos.visitamoraleja.bdsqlite.datasource.CategoriasDataSource;
import com.primos.visitamoraleja.bdsqlite.datasource.EventosDataSource;
import com.primos.visitamoraleja.bdsqlite.datasource.SitiosDataSource;
import com.primos.visitamoraleja.contenidos.Categoria;
import com.primos.visitamoraleja.contenidos.Evento;
import com.primos.visitamoraleja.contenidos.Sitio;

public class DetalleEventoActivity extends ActionBarListActivity implements
		AdapterView.OnItemSelectedListener, ViewSwitcher.ViewFactory,
		OnClickListener {
	public final static String ID_SITIO = "idSitio";
	public final static String SITIO = "sitio";
	private final static String TAG = "[DetalleEventoActivity]";
	private SitiosDataSource dataSource = null;
	private EventosDataSource eventosDataSource = null;
	private Sitio sitio = null;
	int mFlipping = 0; // Initially flipping is off
	Button mButton; // Reference to button available in the layout to start and
					// stop the flipper
	private List<Evento> lstEventosSitio;

	private String getTxtDatosSitio(Sitio sitio) {
		StringBuilder strBuild = new StringBuilder();

		if (sitio.getDireccion() != null) {
			strBuild.append(sitio.getDireccion() + "\n");
		}
		if (sitio.getTelefonosFijos() != null) {
			strBuild.append(sitio.getTelefonosFijos() + "\n");
		}
		if (sitio.getTelefonosMoviles() != null) {
			strBuild.append(sitio.getTelefonosMoviles() + "\n");
		}
		if (sitio.getFacebook() != null) {
			strBuild.append(sitio.getFacebook() + "\n");
		}
		if (sitio.getTwitter() != null) {
			strBuild.append(sitio.getTwitter() + "\n");
		}

		return strBuild.toString();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dataSource = new SitiosDataSource(this);
		dataSource.open();
		eventosDataSource = new EventosDataSource(this);
		eventosDataSource.open();
		// setContentView(R.layout.activity_detalle_evento);
		setContentView(R.layout.fragment_detalle_evento);

		TextView textViewDireccion = (TextView) findViewById(R.id.textDireccion);
		final TextView textViewTextoLargo1 = (TextView) findViewById(R.id.tvTextoLargo1);
		TextView textNombreSitio = (TextView) findViewById(R.id.textNombreSitio);
		TextView textNombreSitio2 = (TextView) findViewById(R.id.textNombreSitio2);
		TextView textTelefono = (TextView) findViewById(R.id.textTelefono);
		TextView web = (TextView) findViewById(R.id.botonWeb);
		
		
		//TextView url = (TextView) findViewById(R.id.web);
		String url = null;
		textViewTextoLargo1.setMovementMethod(new ScrollingMovementMethod());

		Button botonTelefono = (Button) findViewById(R.id.botonTelefono);
		Button botonLocalizar = (Button) findViewById(R.id.botonLocalizar);
		Button botonCompartir = (Button) findViewById(R.id.botonCompartir);
		Button botonFacebook = (Button) findViewById(R.id.botonFacebook);
		Button botonTwiter = (Button) findViewById(R.id.botonTwiter);
		Button botonWeb = (Button) findViewById(R.id.botonWeb);

		botonTelefono.setOnClickListener(this);
		botonLocalizar.setOnClickListener(this);
		botonCompartir.setOnClickListener(this);
		botonFacebook.setOnClickListener(this);
		botonTwiter.setOnClickListener(this);
		botonWeb.setOnClickListener(this);

		long idSitio = (long) getIntent().getExtras().get(ID_SITIO);
		this.sitio = dataSource.getById(idSitio);

		Gallery myGallery = (Gallery) findViewById(R.id.gallery);
		myGallery.setAdapter(new ImageAdapter(this, this, sitio));

		//textViewDireccion.setText(getTxtDatosSitio(sitio));
		textViewDireccion.setText(sitio.getDireccion()+" - "+sitio.getPoblacion());
		textNombreSitio.setText(sitio.getNombre());
		textNombreSitio2.setText(sitio.getNombre());
		
		if (sitio.getTelefonosFijos().length()>0) {
			if (sitio.getTelefonosMoviles().length()>0) {
				textTelefono.setText(sitio.getTelefonosFijos()+" / "+sitio.getTelefonosMoviles());
			}else {
				textTelefono.setText(sitio.getTelefonosFijos());
			}
		}else {
			if (sitio.getTelefonosMoviles().length()>0) {
				textTelefono.setText(sitio.getTelefonosMoviles());
			}else {
				textTelefono.setText(sitio.getTelefonosFijos());
			}
		}	
		
		textViewTextoLargo1.setText(sitio.getTextoLargo1());
		
		myGallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
				String str = " selected option: " + position;
				for (int i = 0; i < 30; i++) {
					str += "   selected option: " + position + "-" + i;
				}
				
			}

			public void onNothingSelected(AdapterView<?> parent) {
				textViewTextoLargo1.setText("Nothing selected");
			}

		});
		dataSource.actualizar(sitio);
		
		// Se asigna el titulo del action bar
		CategoriasDataSource categoriaDataSource = new CategoriasDataSource(this);
		categoriaDataSource.open();
		Categoria categoria = categoriaDataSource.getById(sitio.getIdCategoria());
		setTitulo(categoria.getNombre());
		categoriaDataSource.close();

	}

	
	
	// Recogemos la pulsación en los 5 botones de la minificha
	public void onClick(View boton_pulsado) {
		
		String lugar = sitio.getNombre();
	
		Double latitud = sitio.getLatitud();
		Double longitud = sitio.getLongitud();

		switch (boton_pulsado.getId()) {
		case R.id.botonTelefono:
			realizarLlamada(sitio.getTelefonosFijos());
			break;
		case R.id.botonLocalizar:
			localizarSitio(lugar, latitud, longitud);
			break;
		case R.id.botonCompartir:
		
			compartirLugar(lugar, sitio.getLatitud(), sitio.getLongitud());
			break;

		case R.id.botonFacebook:
			mostrarFacebook(sitio.getFacebook());
			break;
		case R.id.botonTwiter:
			mostrarTwiter(sitio.getTwitter());
			break;
		case R.id.botonWeb:
			
			visitarWeb(sitio.getWeb());
			break;
		default:
			break;
		}

	}

	// Abre Internet para ir al sitio WEB del anunciante, si no lo tiene
		// aparece un mensajito.
		// NO SE SI SERIA MEJOR LO DEL MENSAJITO O QUE NO APARECIERA EL ICONO DE
		// TWITER, pero pienso que hay que
		// motivar a las empresas para que esten en las redes sociales.
		private void visitarWeb(String url) {
			try {
				
				if (url.length() > 0) {
					
					
					Uri irWeb = Uri.parse(url);
					Intent intent = new Intent(Intent.ACTION_VIEW, irWeb);
					startActivity(intent);
				} else {
					// si el sitio no tiene WEB
					Toast.makeText(getBaseContext(), "NO DISPONIBLE",
							Toast.LENGTH_SHORT).show();
				}

			} catch (ActivityNotFoundException activityException) {
				// si se produce un error, se muestra en el LOGCAT
				Toast.makeText(getBaseContext(), "No se pudo acceder a la WEB",
						Toast.LENGTH_SHORT).show();
				
			}
		}

	
	// Abre Internet para ir al sitio Twiter del anunciante, si no lo tiene
	// aparece un mensajito.
	// NO SE SI SERIA MEJOR LO DEL MENSAJITO O QUE NO APARECIERA EL ICONO DE
	// TWITER, pero pienso que hay que
	// motivar a las empresas para que esten en las redes sociales.
	private void mostrarTwiter(String twitter) {
		try {
			if (twitter.length() > 0) {
				Uri irTwiter = Uri.parse(twitter);
				Intent intent = new Intent(Intent.ACTION_VIEW, irTwiter);
				startActivity(intent);
			} else {
				// si el sitio no tiene Twiter
				Toast.makeText(getBaseContext(), "Sin Twiter Asociado",
						Toast.LENGTH_SHORT).show();
			}

		} catch (ActivityNotFoundException activityException) {
			// si se produce un error, se muestra en el LOGCAT
			Toast.makeText(getBaseContext(), "No se pudo acceder a Twiter",
					Toast.LENGTH_SHORT).show();
			// Log.e("ET", "No se pudo realizar la llamada.",
			// activityException);
		}
	}

	// Muy pronto estará terminado
	private void localizarSitio(String lugar, Double latitud, Double longitud) {
		Intent lanzarmapa = new Intent(this, MapaLugaresActivity.class);

		// Vamos a pasar los valores que necesita MapaLugarActivity
		lanzarmapa.putExtra("nombre", sitio.getNombre());
		lanzarmapa.putExtra("latitud", latitud);
		lanzarmapa.putExtra("longitud", longitud);
		lanzarmapa.putExtra(MapaLugaresActivity.ORIGEN, SITIO);
		lanzarmapa.putExtra(MapaLugaresActivity.ID_RECIBIDO, sitio.getId());
		
		 
		 startActivity(lanzarmapa);
		 finish();
	}

	// Abre Internet para ir al sitio Facebook del anunciante, si no lo tiene
	// aparece un mensajito.
	// NO SE SI SERIA MEJOR LO DEL MENSAJITO O QUE NO APARECIERA EL ICONO DE
	// FACEBOOK, pero pienso que hay que
	// motivar a las empresas para que esten en las redes sociales.
	private void mostrarFacebook(String facebook) {
		// TODO Auto-generated method stub

		try {
			if (facebook.length() > 0) {
				// realiza la llamada
				Uri irFacebook = Uri.parse(facebook);
				Intent intent = new Intent(Intent.ACTION_VIEW, irFacebook);
				startActivity(intent);
			} else {
				// si el sitio no tiene facebook
				Toast.makeText(getBaseContext(), "Sin Facebook Asociado",
						Toast.LENGTH_SHORT).show();
			}

		} catch (ActivityNotFoundException activityException) {
			// si se produce un error, se muestra en el LOGCAT
			Toast.makeText(getBaseContext(), "No se pudo acceder a Facebook",
					Toast.LENGTH_SHORT).show();
			// Log.e("ET", "No se pudo realizar la llamada.",
			// activityException);
		}

	}

	// Seleccionar de una lista de aplicaciones instaladas en el movil, una para
	// enviar una notificación a quien quiera
	// para indicar donde se encuentra ese lugar en concreto, sigue con el
	// problema de los decimales en lat y long.
	private void compartirLugar(String lugar, Double lat, Double lon) {
		String frase1, frase2;

		String latitud = Double.toString(lat);
		String longitud = Double.toString(lon);

		// TODO Auto-generated method stub
		frase1 = getString(R.string.compartir1);
		frase2 = getString(R.string.compartir2);
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		String titulo = frase1 + " " + lugar + frase2;
		String url = "http://maps.google.com/maps?q=" + lat + "," + lon;
		// Toast.makeText(getBaseContext(),"2-"+latitud+"/"+longitud+"--"+lat+"///"+lon,Toast.LENGTH_SHORT).show();
		intent.putExtra(Intent.EXTRA_SUBJECT, titulo);// Encabezado del mensaje
		intent.putExtra(Intent.EXTRA_TEXT, url);// Direccion web
		startActivity(Intent.createChooser(intent,
				this.getString(R.string.titulo_compartir)));

	}

	// Lanza el DIAL para que solo sonlo con pulsar un boton se realice una
	// llamada, lo hice con llamda directa, pero
	// aparte que había que modificar permisos en el manifest qu eno me gustan
	// ni a la gente tampoco, me parecía
	// demasiado atrevido.
	private void realizarLlamada(String numero) {
		// TODO Auto-generated method stub
		numero = numero.trim();
		// Toast.makeText(getBaseContext(),numero,Toast.LENGTH_SHORT).show();
		try {
			if (numero.length() > 0) {
				// realiza la llamada
				Uri marcarnumero = Uri.parse("tel:" + numero.toString());
				Intent intent = new Intent(Intent.ACTION_DIAL, marcarnumero);
				startActivity(intent);
			} else {
				// si el sitio no tiene numero
				Toast.makeText(getBaseContext(),
						"Sin Numero de Telefono Asociado", Toast.LENGTH_SHORT)
						.show();
			}

		} catch (ActivityNotFoundException activityException) {
			// si se produce un error, se muestra en el LOGCAT
			Toast.makeText(getBaseContext(), "No se pudo realizar la llamada",
					Toast.LENGTH_SHORT).show();
			// Log.e("ET", "No se pudo realizar la llamada.",
			// activityException);
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.actionbar_inicio:
			Intent i = new Intent(this, MainActivity.class);
			i.putExtra(MainActivity.ACTUALIZAR, false);
			startActivity(i);
			return true;
		case R.id.actionbar_favorito:
			cambiarEstadoFavorito(item);
			return true;
		case R.id.actionbar_eventos:
			Intent iEventos = new Intent(this, ListaEventosActivity.class);
			iEventos.putExtra(ListaEventosActivity.ID_SITIO, sitio.getId());
			startActivity(iEventos);
			return true;
		}
		return false;
	}

	/**
	 * Cambia el estado de favorito del sitio, actualiza el icono y lo guarda en
	 * la base de datos
	 * 
	 * @param item
	 */
	private void cambiarEstadoFavorito(MenuItem item) {
		sitio.setFavorito(!sitio.isFavorito());
		asignarIconoFavorito(item);
		dataSource.actualizar(sitio);
	}

	/**
	 * Asigna el icono correspondiente al estado de favorito.
	 * 
	 * @param item
	 */
	private void asignarIconoFavorito(MenuItem item) {
		int iconFavorito = R.drawable.ic_action_nofavorito;
		if (sitio.isFavorito()) {
			iconFavorito = R.drawable.ic_action_favorito;
		}
		item.setIcon(iconFavorito);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detalle_evento, menu);
		asignarIconoFavorito(menu.findItem(R.id.actionbar_favorito));
		lstEventosSitio = eventosDataSource.getBySitioId(sitio.getId());
		Log.d(TAG, "Numero de eventos para el sitio " + sitio.getNombre() + " es " + lstEventosSitio.size());
		if(lstEventosSitio.isEmpty()) {
			MenuItem item = menu.findItem(R.id.actionbar_eventos);
			item.setVisible(false);
		}
		return true;
	}

	@Override
	public View makeView() {
		ImageView i = new ImageView(this);
		i.setBackgroundColor(0xFF000000);
		i.setScaleType(ImageView.ScaleType.FIT_CENTER);
		i.setLayoutParams(new ImageSwitcher.LayoutParams(
		// LayoutParams.fill_parent, LayoutParams.fill_parent));
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		return i;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		Log.d("IMAGEN", "Mostrada imagen");
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// mySelection.setText("Nothing selected");
	}

	@Override
	protected void onResume() {
		dataSource.open();
		eventosDataSource.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		dataSource.close();
		eventosDataSource.close();
		super.onPause();
	}
}