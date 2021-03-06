package com.primos.visitamoraleja.actividades.eventos;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.primos.visitamoraleja.R;
import com.primos.visitamoraleja.contenidos.ActividadEvento;
import com.primos.visitamoraleja.mapas.ControlMapaFactory;
import com.primos.visitamoraleja.mapas.ControlMapaItf;
import com.primos.visitamoraleja.mapas.eventos.ControlMapaEventos;
import com.primos.visitamoraleja.menulateral.ConfigMenuLateral;

public class MapsEventosActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DrawerLayout mDrawer;
    private ListView mDrawerOptions;
    private long idEvento;
    private ControlMapaEventos controlMapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_eventos);

        idEvento = (long) getIntent().getExtras().get(EventoPrincipalActivity.ID_EVENTO);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mDrawerOptions = (ListView) findViewById(R.id.menuLateralListaSitios);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout_lateral);
        ConfigMenuLateral cml = new ConfigMenuLateral(this, null, true);
        cml.iniciarMenuLateral();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        ControlMapaFactory factoria = new ControlMapaFactory();
        controlMapa = (ControlMapaEventos)factoria.createControlMapa(ControlMapaFactory.TIPOS_MAPAS.EVENTOS);
        controlMapa.init(idEvento, this);
        controlMapa.tratarMapa(mMap, this);
    }

    public void mostrarOpciones(View view) {
        ListView lstOpciones = (ListView)findViewById(R.id.listaOpcionesMapaEventos);
        int visibilidad = lstOpciones.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
        lstOpciones.setVisibility(visibilidad);

        int idResourceImgBoton = lstOpciones.getVisibility() == View.GONE ? R.mipmap.ic_launcher : R.mipmap.ic_cerrar_opciones;
        Button btnMenu = (Button)findViewById(R.id.btnMostrarOpcionesEventos);
        btnMenu.setBackgroundResource(idResourceImgBoton);
    }

    public void mostrarDetalle(View view) {
        ActividadEvento actividadEvento = (ActividadEvento)view.getTag();

        controlMapa.mostrarDetalle(actividadEvento);
    }

    public void mostrarMenuLateral(View view) {
        if (mDrawer.isDrawerOpen(mDrawerOptions)){
            mDrawer.closeDrawers();
        }else{
            mDrawer.openDrawer(mDrawerOptions);
        }
    }
}
