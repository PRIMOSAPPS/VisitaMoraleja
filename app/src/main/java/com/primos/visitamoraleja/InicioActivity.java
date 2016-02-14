package com.primos.visitamoraleja;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.primos.visitamoraleja.actualizador.Actualizador;
import com.primos.visitamoraleja.contenidos.Categoria;
import com.primos.visitamoraleja.contenidos.Evento;
import com.primos.visitamoraleja.contenidos.Sitio;
import com.primos.visitamoraleja.eventos.EventosXML_SAX;
import com.primos.visitamoraleja.excepcion.EventosException;
import com.primos.visitamoraleja.util.UtilPreferencias;

public class InicioActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inicio);
		
		iniciar();
	}
	
	private void cargaInicial() {
		if(UtilPreferencias.esPrimerArranque(this)) {
			Log.d("[InicioActivity]", "Aun no se ha realizado el primer arranque");
			try {
				// Se decide el modo de almacenamiento
				UtilPreferencias.asignarModoAlmacenamiento(this);

				Actualizador actualizador = new Actualizador(this);
				EventosXML_SAX meXml = new EventosXML_SAX();
				
				Resources resources = getResources();
				
				InputStream is = this.getResources().openRawResource(R.raw.categorias_xml);
				List<Categoria> lstCategorias = meXml.leerCategoriasXML(is);
				actualizador.actualizarCategorias(lstCategorias);
				is.close();
				Log.d("[InicioActivity]", "Se ha realizado la carga desde fichero de " + lstCategorias.size() + " categorias");
				
				is = this.getResources().openRawResource(R.raw.eventos_xml);
				List<Evento> lstEventos = meXml.leerEventosXML(is);
				actualizador.actualizarEventos(lstEventos);
				is.close();
				Log.d("[InicioActivity]", "Se ha realizado la carga desde fichero de " + lstEventos.size() + " eventos");
				
				// Los sitios es probable que superen ficheros de 1MB, por eso se realiza la carga de esta manera
				int i=1;
				int idResource = resources.getIdentifier("raw/sitios_xml_" + i,
                        "raw", getPackageName());
				while(idResource != 0) {
					is = this.getResources().openRawResource(idResource);
					List<Sitio> lstSitios = meXml.leerSitiosXML(is);
					actualizador.actualizarSitios(lstSitios);
					is.close();
					Log.d("[InicioActivity]", "Se ha realizado la carga desde fichero de " + lstSitios.size() + " sitios");
					i++;
					idResource = resources.getIdentifier("raw/sitios_xml_" + i,
	                        "raw", getPackageName());
				}
				
				Log.d("[InicioActivity]", "Se se marca el primer arranque como realizado");
				UtilPreferencias.marcarPrimarArranqueRealizado(this);
			} catch (ParserConfigurationException | SAXException | IOException | EventosException e) {
				Log.e("[InicioActivity]", "Se ha producido un error durante la carga inicial de los datos.", e);
				Toast.makeText(this, "Se ha producido un error durante la carga inicial de los datos.", Toast.LENGTH_LONG).show();
			}
		}
	}

	protected void iniciar() {
		
		if(UtilPreferencias.esPrimerArranque(this)) {
			Thread thrInicio = new Thread("ThreadInicio") {
	
				@Override
				public void run() {
					Log.d("[InicioActivity]", "Se lanza la carga inicial.");
					
					cargaInicial();
					
					Log.d("[InicioActivity]", "Se lanza MainActivity.");
	
					Intent intent = new Intent(InicioActivity.this, MainActivity.class);
			    	startActivity(intent);
			    	
			    	InicioActivity.this.finish();
				}
				
			};
			// Se lanza la carga inicial de los datos en segundo plano para que se pinte la pantalla
			thrInicio.start();
		} else {
			Intent intent = new Intent(InicioActivity.this, MainActivity.class);
	    	startActivity(intent);
	    	finish();
		}
    }

}
