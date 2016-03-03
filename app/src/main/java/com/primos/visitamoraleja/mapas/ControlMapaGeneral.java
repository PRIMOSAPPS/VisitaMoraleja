package com.primos.visitamoraleja.mapas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.primos.visitamoraleja.DetalleEventoActivity;
import com.primos.visitamoraleja.R;
import com.primos.visitamoraleja.adaptadores.OpcionCategoriaMapaAdaptador;
import com.primos.visitamoraleja.almacenamiento.AlmacenamientoFactory;
import com.primos.visitamoraleja.almacenamiento.ItfAlmacenamiento;
import com.primos.visitamoraleja.bdsqlite.datasource.CategoriasDataSource;
import com.primos.visitamoraleja.bdsqlite.datasource.SitiosDataSource;
import com.primos.visitamoraleja.contenidos.Categoria;
import com.primos.visitamoraleja.contenidos.Sitio;
import com.primos.visitamoraleja.menulateral.DatosItemMenuLateral;
import com.primos.visitamoraleja.util.UtilImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by h on 21/02/16.
 */
public class ControlMapaGeneral implements ControlMapaItf {
    private Map<Long, List<Marker>> mapaMarkers = new HashMap<>();

    @Override
    public void tratarMapa(GoogleMap googleMap, final Context contexto) {
        ItfAlmacenamiento almacenamiento = AlmacenamientoFactory.getAlmacenamiento(contexto);
        SitiosDataSource sds = new SitiosDataSource(contexto);
        sds.open();
        List<Sitio> lstSitios = sds.getAll();
        // Mezclamos los sitios, para que no siempre aparezcan encima en el mapa los mismos
        Collections.shuffle(lstSitios);
        final Map<LatLng, Sitio> mapSitios = new HashMap<>();
        sds.close();
        for(Sitio sitio : lstSitios) {
            LatLng latLngSitio = new LatLng(sitio.getLatitud(), sitio.getLongitud());
            mapSitios.put(latLngSitio, sitio);
            MarkerOptions moSitio = new MarkerOptions();
            moSitio.position(latLngSitio);
            moSitio.title(sitio.getNombre());

            Bitmap bitmap = almacenamiento.getImagenSitio(sitio.getId(), sitio.getNombreLogotipo());
            bitmap = UtilImage.createScaledBitmap(bitmap, 72);
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
            moSitio.icon(bitmapDescriptor);

            Marker markerSitio = googleMap.addMarker(moSitio);

            long idCategoria = sitio.getIdCategoria();
            List<Marker> lstMarkers = mapaMarkers.get(idCategoria);
            if(lstMarkers == null) {
                lstMarkers = new ArrayList<>();
                mapaMarkers.put(idCategoria, lstMarkers);
            }
            lstMarkers.add(markerSitio);
        }

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Sitio sitio = mapSitios.get(marker.getPosition());
                Intent intent = new Intent(contexto, DetalleEventoActivity.class);
                intent.putExtra(DetalleEventoActivity.ID_SITIO, sitio.getId());
                contexto.startActivity(intent);
                return true;
            }
        });

        // Add a marker in Sydney and move the camera
        LatLng moraleja = new LatLng(40.068782, -6.6562358);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(moraleja));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(moraleja, 14.0f));

        cargarOpcionesMapa(contexto);

    }

    private void cargarOpcionesMapa(Context contexto) {
        Activity actividad = (Activity)contexto;
        List<DatosOpcionCategoriaMapa> listaItemsMenu = new ArrayList<>();
        List<Categoria> listaCategorias = getListaCategorias(contexto);
        Resources resources = contexto.getResources();
        int i=0;
        for(Categoria categoria : listaCategorias) {
            String textoMenu = categoria.getDescripcion();
            String nombreIcono = categoria.getNombreIcono();
            nombreIcono = nombreIcono.substring(0, nombreIcono.lastIndexOf("."));
            int identificadorImagen = resources.getIdentifier(nombreIcono, "mipmap", contexto.getPackageName());
            DatosOpcionCategoriaMapa datosItem = new DatosOpcionCategoriaMapa(categoria, textoMenu, identificadorImagen);
            listaItemsMenu.add(datosItem);
        }
        DatosItemMenuLateral datosItemFavoritos = new DatosItemMenuLateral((String)resources.getText(R.string.favoritos),
                R.mipmap.ic_favoritosml);

        ListView lstViewOpciones = (ListView)actividad.findViewById(R.id.listaOpcionesMapa);
        lstViewOpciones.setAdapter(new OpcionCategoriaMapaAdaptador(actividad, listaItemsMenu));

        lstViewOpciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DatosOpcionCategoriaMapa datosItem = (DatosOpcionCategoriaMapa)view.getTag();
                Categoria categoria = datosItem.getCategoria();
                CheckBox checkBox = (CheckBox)view.findViewById(R.id.checkBoxMostrarCategoria);
                checkBox.setChecked(!checkBox.isChecked());
                mostrarOcultarMarcas(categoria, checkBox.isChecked());
            }
        });
    }

    private void mostrarOcultarMarcas(Categoria categoria, boolean mostrar) {
        List<Marker> lstMarkers = mapaMarkers.get(categoria.getId());
        for(Marker markerSitio : lstMarkers) {
            markerSitio.setVisible(mostrar);
        }
    }

    private List<Categoria> getListaCategorias(Context contexto) {
        CategoriasDataSource categoriasDataSource = new CategoriasDataSource(contexto);
        categoriasDataSource.open();
        List<Categoria> listaCategorias = categoriasDataSource.getAll();
        categoriasDataSource.close();

        return listaCategorias;
    }
}
