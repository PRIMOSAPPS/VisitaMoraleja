package com.primos.visitamoraleja;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class AcercaDeActivity extends   ActionBarListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acerca_de);
	}
	
	
//Funci�n que abre la pagina de inscripcion.
    public void inscripcion(View view){  
        
    	try {
			
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://appmoraleja.roymainformatica.com/eventos"));
			startActivity(intent);
	
		} catch (ActivityNotFoundException activityException) {
			// si se produce un error, se muestra en el LOGCAT
			Toast.makeText(getBaseContext(), "No se pudo acceder a la WEB",
					Toast.LENGTH_SHORT).show();
			
		}
    }
    
    //Funci�n que abre la pagina de royma.
    public void iraRoyma(View view){  
        
       	try {
    			
    			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.roymainformatica.com"));
    			startActivity(intent);
    			
       		} catch (ActivityNotFoundException activityException) {
    			// si se produce un error, se muestra en el LOGCAT
    			Toast.makeText(getBaseContext(), "No se pudo acceder a la WEB",
    					Toast.LENGTH_SHORT).show();
    		}
       }    
	

}
