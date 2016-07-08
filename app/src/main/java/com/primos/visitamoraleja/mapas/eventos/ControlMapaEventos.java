package com.primos.visitamoraleja.mapas.eventos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.primos.visitamoraleja.R;
import com.primos.visitamoraleja.actividades.eventos.ActividadEventoDetalleActivity;
import com.primos.visitamoraleja.actividades.eventos.SitioEventoDetalleActivity;
import com.primos.visitamoraleja.adaptadores.ActividadEventoAdaptador;
import com.primos.visitamoraleja.adaptadores.OpcionCategoriaMapaAdaptador;
import com.primos.visitamoraleja.adaptadores.eventos.InfoWindowsActividadEventoAdapter;
import com.primos.visitamoraleja.adaptadores.eventos.InfoWindowsActividadesSeleccionAdapter;
import com.primos.visitamoraleja.adaptadores.eventos.InfoWindowsFormaEventoAdapter;
import com.primos.visitamoraleja.adaptadores.eventos.InfoWindowsSitioEventoAdapter;
import com.primos.visitamoraleja.almacenamiento.AlmacenamientoFactory;
import com.primos.visitamoraleja.almacenamiento.ItfAlmacenamiento;
import com.primos.visitamoraleja.bdsqlite.datasource.ActividadEventoDataSource;
import com.primos.visitamoraleja.bdsqlite.datasource.CategoriaEventoDataSource;
import com.primos.visitamoraleja.bdsqlite.datasource.EventosDataSource;
import com.primos.visitamoraleja.bdsqlite.datasource.FormaEventoDataSource;
import com.primos.visitamoraleja.bdsqlite.datasource.SitioEventoDataSource;
import com.primos.visitamoraleja.contenidos.ActividadEvento;
import com.primos.visitamoraleja.contenidos.CategoriaEvento;
import com.primos.visitamoraleja.contenidos.Evento;
import com.primos.visitamoraleja.contenidos.FormaEvento;
import com.primos.visitamoraleja.contenidos.SitioEvento;
import com.primos.visitamoraleja.mapas.ControlMapaItf;
import com.primos.visitamoraleja.mapas.TipoForma;
import com.primos.visitamoraleja.util.UtilCoordenadas;
import com.primos.visitamoraleja.util.UtilImage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by h on 25/06/16.
 */
public class ControlMapaEventos implements ControlMapaItf {
    private final static float ZINDEX_POLIGONO = 10;
    private final static float ZINDEX_LINEA = 20;
    private final static float ZINDEX_PUNTO = 30;

    private GoogleMap _googleMap;

    private Evento evento;
    private Activity contexto;
    private Map<LatLng, List<ContenidoMapa>> contenidosMapa = null;

    private InfoWindowsFormaEventoAdapter infoWindowsFormaEventoAdapter;
    private InfoWindowsSitioEventoAdapter infoWindowsSitioEventoAdapter;
    private InfoWindowsActividadEventoAdapter infoWindowsActividadEventoAdapter;
    private InfoWindowsActividadesSeleccionAdapter infoWindowsActividadesSeleccionAdapter;

    private Map<Long, List<Marker>> categoriasMarkers = new HashMap<>();
    private Map<Long, List<Polygon>> categoriasPolygonMapa = new HashMap<>();
    private Map<Long, List<Polyline>> categoriasPolylineMapa = new HashMap<>();
    private Map<Long, List<Circle>> categoriasCircleMapa = new HashMap<>();

    private Map<Polygon, ContenidoMapa> poligonos = null;
    private Map<Polyline, ContenidoMapa> lineas = null;
    private Map<Circle, ContenidoMapa> puntos = null;
    private Map<Marker, SitioEvento> sitios = null;
    private Map<LatLng, List<ActividadEvento>> actividades = null;

    private SitioEvento sitioEventoSeleccionado = null;
    private ActividadEvento actividadSeleccionada = null;
    private AlertDialog dialogoSeleccion = null;
    private Marker markerVisible = null;

    public class ContenidoMapa {
        /**
         * Objeto, que sera un marker, polygon o polyline
         */
        private Object objMapa;
        private LatLng coordenada;
        private FormaEvento forma;
        private TipoForma tipoForma;

        public ContenidoMapa( LatLng coordenada, Object objMapa, FormaEvento forma, TipoForma tipoForma) {
            this.objMapa = objMapa;
            this.coordenada = coordenada;
            this.forma = forma;
            this.tipoForma = tipoForma;
        }

        public Object getObjMapa() {
            return objMapa;
        }

        public LatLng getCoordenada() {
            return coordenada;
        }

        public TipoForma getTipoForma() {
            return tipoForma;
        }

        public FormaEvento getForma() {
            return forma;
        }
    }

    public ControlMapaEventos() {
    }

    @Override
    public void tratarMapa(final GoogleMap googleMap, Context contexto) {
        _googleMap = googleMap;

        contenidosMapa = new HashMap<>();
        poligonos = new HashMap<>();
        lineas = new HashMap<>();
        puntos = new HashMap<>();
        sitios = new HashMap<>();
        actividades = new HashMap<>();

        infoWindowsFormaEventoAdapter = new InfoWindowsFormaEventoAdapter((Activity) contexto);
        infoWindowsSitioEventoAdapter = new InfoWindowsSitioEventoAdapter((Activity) contexto);
        infoWindowsActividadEventoAdapter = new InfoWindowsActividadEventoAdapter((Activity) contexto);
        infoWindowsActividadesSeleccionAdapter = new InfoWindowsActividadesSeleccionAdapter((Activity) contexto);

        //cargarCategorias();
        cargarSitios(googleMap);
        cargarFormas(googleMap);
        cargarActividades(googleMap);
        LatLng moraleja = new LatLng(evento.getLatitud(), evento.getLongitud());
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(moraleja, evento.getZoomInicial()));

        googleMap.setInfoWindowAdapter(infoWindowsFormaEventoAdapter);

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if(sitioEventoSeleccionado != null) {
                    detalleSitio();
                } else if(actividadSeleccionada != null) {
                    detalleActividad();
                }
            }
        });

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                hideCurrentMarker();
                SitioEvento sitioEvento = sitios.get(marker);
                if (sitioEvento != null) {
                    showInfoMarker(googleMap, sitioEvento);
                } else {
                    LatLng punto = marker.getPosition();
                    List<ActividadEvento> actividadesPosition = actividades.get(punto);
                    if (actividadesPosition.size() > 1) {
                            showSeleccionActividad(googleMap, actividadesPosition, punto);
                    } else {
                        ActividadEvento actividadEvento = actividadesPosition.get(0);
                        if (actividadEvento != null) {
                            showInfoMarker(googleMap, actividadEvento);
                        }
                    }
                }
                return true;
            }
        });

        googleMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
            @Override
            public void onPolygonClick(Polygon polygon) {
                hideCurrentMarker();
                showInfoNewMarker(googleMap, poligonos.get(polygon));
            }
        });

        googleMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
            @Override
            public void onPolylineClick(Polyline polyline) {
                hideCurrentMarker();
                showInfoNewMarker(googleMap, lineas.get(polyline));
            }
        });

        // Adding and showing marker while touching the GoogleMap
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng arg0) {
                hideCurrentMarker();
                Collection<ContenidoMapa> contenidosPunto = puntos.values();
                for (ContenidoMapa contenidoPunto : contenidosPunto) {
                    if (clickInCircle(arg0, (Circle) contenidoPunto.getObjMapa())) {
                        showInfoNewMarker(googleMap, contenidoPunto);
                        break;
                    }
                }
            }

            private boolean clickInCircle(LatLng position, Circle circle) {
                LatLng center = circle.getCenter();
                double radius = circle.getRadius();
                float[] distance = new float[1];
                Location.distanceBetween(position.latitude, position.longitude, center.latitude, center.longitude, distance);
                boolean clicked = distance[0] < radius;
                return clicked;
            }
        });

        cargarOpcionesMapa(contexto);
    }

    private void detalleSitio() {
        Intent i = new Intent(contexto, SitioEventoDetalleActivity.class);
        i.putExtra(SitioEventoDetalleActivity.ID_SITIO_EVENTO, sitioEventoSeleccionado.getId());
        contexto.startActivity(i);
    }

    private void detalleActividad() {
        Intent i = new Intent(contexto, ActividadEventoDetalleActivity.class);
        i.putExtra(ActividadEventoDetalleActivity.ID_ACTIVIDAD_EVENTO, actividadSeleccionada.getId());
        contexto.startActivity(i);
    }

    private void hideCurrentMarker() {
        if(markerVisible != null) {
            markerVisible.hideInfoWindow();
            markerVisible.remove();
        }
    }

    private void showInfoNewMarker(GoogleMap googleMap, ContenidoMapa cm) {
        MarkerOptions mo = new MarkerOptions();

        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marker_transparente);
        mo.icon(icon);
        mo.position(cm.getCoordenada());
        mo.title("Esto es una linea");

        Marker marker = googleMap.addMarker(mo);
        showInfoMarker(googleMap, marker, cm);
    }

    private void showSeleccionActividad(GoogleMap googleMap, List<ActividadEvento> actividadesPosition, LatLng punto) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(contexto);
        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle(R.string.seleccione_elemento_infowindows);

        final ActividadEventoAdaptador adapter = new ActividadEventoAdaptador(contexto, actividadesPosition);
        builderSingle.
                setCancelable(true).
                setAdapter(
                        adapter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
        dialogoSeleccion = builderSingle.create();
        dialogoSeleccion.show();

        /*
        infoWindowsActividadesSeleccionAdapter.setElementos(actividadesPosition);

        showInfoMarker(googleMap, punto, infoWindowsActividadesSeleccionAdapter);
        */
    }

    private void showInfoMarker(GoogleMap googleMap, SitioEvento sitioEvento) {
        actividadSeleccionada = null;
        sitioEventoSeleccionado = sitioEvento;

        LatLng punto = new LatLng(sitioEvento.getLatitud(), sitioEvento.getLongitud());
        infoWindowsSitioEventoAdapter.setSitioEvento(sitioEvento);

        showInfoMarker(googleMap, punto, infoWindowsSitioEventoAdapter);
    }

    private void showInfoMarker(GoogleMap googleMap, ActividadEvento actividadEvento) {
        actividadSeleccionada = actividadEvento;
        sitioEventoSeleccionado = null;
        LatLng punto = new LatLng(actividadEvento.getLatitud(), actividadEvento.getLongitud());
        infoWindowsActividadEventoAdapter.setActividadEvento(actividadEvento);

        showInfoMarker(googleMap, punto, infoWindowsActividadEventoAdapter);
    }

    private void showInfoMarker(GoogleMap googleMap, LatLng punto, GoogleMap.InfoWindowAdapter adapter) {
        // Creating an instance of MarkerOptions to set position
        MarkerOptions markerOptions = new MarkerOptions();
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marker_transparente);
        markerOptions.icon(icon);
        markerOptions.position(punto);
        Marker marker = googleMap.addMarker(markerOptions);

        googleMap.setInfoWindowAdapter(adapter);
        marker.showInfoWindow();
        markerVisible = marker;
    }

    private void showInfoMarker(GoogleMap googleMap, Marker marker, ContenidoMapa cm) {
        actividadSeleccionada = null;
        sitioEventoSeleccionado = null;
        googleMap.setInfoWindowAdapter(infoWindowsFormaEventoAdapter);
        infoWindowsFormaEventoAdapter.setContenidoMapa(cm);
        marker.showInfoWindow();
        markerVisible = marker;
    }

    public void init(long idEvento, Activity contexto) {
        this.contexto = contexto;
        EventosDataSource dataSource = new EventosDataSource(contexto);
        dataSource.open();
        try {
            dataSource.open();
            evento = dataSource.getById(idEvento);
        } finally {
            dataSource.close();
        }
    }

    /*
    private void cargarCategorias() {
        CategoriaEventoDataSource dataSource = new CategoriaEventoDataSource(contexto);
        try {
            dataSource.open();
            List<CategoriaEvento> categorias = dataSource.getByIdEvento(evento.getId());
        } finally {
            dataSource.close();
        }
    }
    */

    private void cargarFormas(GoogleMap googleMap) {
        UtilCoordenadas utilCoordenadas = new UtilCoordenadas();
        FormaEventoDataSource dataSource = null;
        try {
            dataSource = new FormaEventoDataSource(contexto);
            dataSource.open();
            List<FormaEvento> formas = dataSource.getByIdEvento(evento.getId());
            ordenar(formas);
            for(FormaEvento forma : formas) {
                addFormaMapa(googleMap, forma, utilCoordenadas);
            }
            dataSource.open();
        } finally {
            dataSource.close();
        }
    }

    private void cargarSitios(GoogleMap googleMap) {
        UtilCoordenadas utilCoordenadas = new UtilCoordenadas();
        SitioEventoDataSource dataSource = null;
        try {
            dataSource = new SitioEventoDataSource(contexto);
            dataSource.open();
            List<SitioEvento> sitios = dataSource.getByIdEvento(evento.getId());
            for(SitioEvento sitio : sitios) {
                addSitioMapa(googleMap, sitio, utilCoordenadas);
            }
            dataSource.open();
        } finally {
            dataSource.close();
        }
    }

    private void cargarActividades(GoogleMap googleMap) {
        UtilCoordenadas utilCoordenadas = new UtilCoordenadas();
        ActividadEventoDataSource dataSource = null;
        try {
            dataSource = new ActividadEventoDataSource(contexto);
            dataSource.open();
            List<ActividadEvento> actividades = dataSource.getByIdEvento(evento.getId());
            for(ActividadEvento actividad : actividades) {
                addActividadMapa(googleMap, actividad, utilCoordenadas);
            }
            dataSource.open();
        } finally {
            dataSource.close();
        }
    }

    /**
     * Ordena la lista para pintar primero los poligonos. Quizas sea mejor asignar un z-index
     * @param formas
     */
    private void ordenar(List<FormaEvento> formas) {
        Collections.sort(formas, new Comparator<FormaEvento>() {
            @Override
            public int compare(FormaEvento lhs, FormaEvento rhs) {
                int resul = 0;
                TipoForma tipoFormaL = TipoForma.get(lhs.getTipoForma());
                TipoForma tipoFormaR = TipoForma.get(rhs.getTipoForma());
                if (tipoFormaL == TipoForma.POLIGONO) {
                    if (tipoFormaR != TipoForma.POLIGONO) {
                        resul = -1;
                    }
                } else if (tipoFormaL == TipoForma.LINEA) {
                    if (tipoFormaR == TipoForma.POLIGONO) {
                        resul = 1;
                    } else if (tipoFormaR == TipoForma.PUNTO) {
                        resul = -1;
                    }
                } else if (tipoFormaL == TipoForma.PUNTO) {
                    if(tipoFormaR != TipoForma.PUNTO) {
                        resul = 1;
                    }
                }
                return resul;
            }
        });
    }

    private void add(ContenidoMapa cm) {
        LatLng punto = cm.getCoordenada();
        List<ContenidoMapa> contenidos = contenidosMapa.get(punto);
        if(contenidos == null) {
            contenidos = new ArrayList<>();
            contenidosMapa.put(punto, contenidos);
        }
        contenidos.add(cm);
    }

    private void addFormaMapa(GoogleMap googleMap, FormaEvento forma, UtilCoordenadas utilCoordenadas) {
        TipoForma tipoForma = TipoForma.get(forma.getTipoForma());

        switch (tipoForma) {
            case PUNTO:
                LatLng punto = utilCoordenadas.parseCoordenadaLngLat(forma.getCoordenadas());
                addPunto(googleMap, forma, punto);
                break;
            case LINEA:
                UtilCoordenadas.CoordenadasForma coordenadasLinea = utilCoordenadas.parse(forma.getCoordenadas());
                addLinea(googleMap, forma, coordenadasLinea);
                break;
            case POLIGONO:
                UtilCoordenadas.CoordenadasForma coordenadasPoligono = utilCoordenadas.parse(forma.getCoordenadas());
                addPoligono(googleMap, forma, coordenadasPoligono);
                break;
        }

    }

    private void addPunto(GoogleMap googleMap, FormaEvento forma, LatLng punto) {
        //ItfAlmacenamiento almacenamiento = AlmacenamientoFactory.getAlmacenamiento(contexto);

        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(punto);
        circleOptions.zIndex(ZINDEX_PUNTO);
        circleOptions.radius(5);
        circleOptions.strokeColor(getColor(forma.getColorLinea()));
        circleOptions.strokeWidth(Float.parseFloat(forma.getGrosorLinea()));
        circleOptions.fillColor(getColor(forma.getColorRelleno()));
        Circle circle = googleMap.addCircle(circleOptions);

        ContenidoMapa cm = new ContenidoMapa(punto, circle, forma, TipoForma.PUNTO);
        add(cm);
        puntos.put(circle, cm);

        addToCategoria(forma.getIdCategoriaEvento(), circle);

    }

    private void addToCategoria(long idCategoria, Marker marker) {
        List<Marker> lista = categoriasMarkers.get(idCategoria);
        if(lista == null) {
            lista = new ArrayList<>();
            categoriasMarkers.put(idCategoria, lista);
        }
        lista.add(marker);
    }

    private void addToCategoria(long idCategoria, Polygon polygon) {
        List<Polygon> lista = categoriasPolygonMapa.get(idCategoria);
        if(lista == null) {
            lista = new ArrayList<>();
            categoriasPolygonMapa.put(idCategoria, lista);
        }
        lista.add(polygon);
    }

    private void addToCategoria(long idCategoria, Polyline polyline) {
        List<Polyline> lista = categoriasPolylineMapa.get(idCategoria);
        if(lista == null) {
            lista = new ArrayList<>();
            categoriasPolylineMapa.put(idCategoria, lista);
        }
        lista.add(polyline);
    }

    private void addToCategoria(long idCategoria, Circle circle) {
        List<Circle> lista = categoriasCircleMapa.get(idCategoria);
        if(lista == null) {
            lista = new ArrayList<>();
            categoriasCircleMapa.put(idCategoria, lista);
        }
        lista.add(circle);
    }

    private void addSitioMapa(GoogleMap googleMap, SitioEvento sitio, UtilCoordenadas coordenadasForma) {
        ItfAlmacenamiento almacenamiento = AlmacenamientoFactory.getAlmacenamiento(contexto);

        LatLng punto = new LatLng(sitio.getLatitud(), sitio.getLongitud());
        MarkerOptions mo = new MarkerOptions();
        mo.position(punto);
        //mo.title(forma.get);
        Bitmap bitmap = almacenamiento.getImagenSitioEvento(sitio.getId(), sitio.getNombreIcono());
        bitmap = UtilImage.createScaledBitmap(bitmap, 72);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
        mo.icon(bitmapDescriptor);

        Marker marker = googleMap.addMarker(mo);

        sitios.put(marker, sitio);

        addToCategoria(sitio.getIdCategoriaEvento(), marker);
    }

    private void addActividadMapa(GoogleMap googleMap, ActividadEvento actividad, UtilCoordenadas coordenadasForma) {
        ItfAlmacenamiento almacenamiento = AlmacenamientoFactory.getAlmacenamiento(contexto);

        LatLng punto = new LatLng(actividad.getLatitud(), actividad.getLongitud());
        MarkerOptions mo = new MarkerOptions();
        mo.position(punto);
        //mo.title(forma.get);
        Bitmap bitmap = almacenamiento.getImagenActividadEvento(actividad.getId(), actividad.getNombreIcono());
        if(bitmap != null) {
            bitmap = UtilImage.createScaledBitmap(bitmap, 72);
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
            mo.icon(bitmapDescriptor);
        }

        Marker marker = googleMap.addMarker(mo);
        LatLng position = marker.getPosition();

        List<ActividadEvento> actividadesPosition = actividades.get(position);
        if(actividadesPosition == null) {
            actividadesPosition = new ArrayList<>();
            actividades.put(position, actividadesPosition);
        }
        actividadesPosition.add(actividad);

        addToCategoria(actividad.getIdCategoriaEvento(), marker);
    }
    
    private void addLinea(GoogleMap googleMap, FormaEvento forma, UtilCoordenadas.CoordenadasForma coordenadasForma) {
        MarkerOptions mo = new MarkerOptions();
        mo.position(coordenadasForma.getPunto());

        List<LatLng> coordenadas = coordenadasForma.getCoordenadas();
        PolylineOptions polyLineOpt = new PolylineOptions();
        polyLineOpt.addAll(coordenadas);
        polyLineOpt.zIndex(ZINDEX_LINEA);
        polyLineOpt.color(getColor(forma.getColorLinea()));
        polyLineOpt.clickable(true);

        Polyline polyLine = googleMap.addPolyline(polyLineOpt);

        ContenidoMapa cm = new ContenidoMapa(coordenadasForma.getPunto(), polyLine, forma, TipoForma.LINEA);
        add(cm);
        lineas.put(polyLine, cm);

        addToCategoria(forma.getIdCategoriaEvento(), polyLine);
    }

    private int getColor(String color) {
        if(!color.startsWith("#")) {
            color = "#" + color;
        }
        return Color.parseColor(color);
    }

    private void addPoligono(GoogleMap googleMap, FormaEvento forma, UtilCoordenadas.CoordenadasForma coordenadasForma) {
        MarkerOptions mo = new MarkerOptions();
        mo.position(coordenadasForma.getPunto());

        PolygonOptions polygonOpt = new PolygonOptions();
        polygonOpt.zIndex(ZINDEX_POLIGONO);
        polygonOpt.addAll(coordenadasForma.getCoordenadas());
        polygonOpt.strokeColor(getColor(forma.getColorLinea()));
        polygonOpt.strokeWidth(Float.parseFloat(forma.getGrosorLinea()));
        polygonOpt.fillColor(getColor(forma.getColorRelleno()));
        polygonOpt.clickable(true);

        Polygon polygon = googleMap.addPolygon(polygonOpt);

        ContenidoMapa cm = new ContenidoMapa(coordenadasForma.getPunto(), polygon, forma, TipoForma.LINEA);
        add(cm);
        poligonos.put(polygon, cm);

        addToCategoria(forma.getIdCategoriaEvento(), polygon);
    }


    // Carga las opciones del mapa
    private void cargarOpcionesMapa(Context contexto) {
        Activity actividad = (Activity)contexto;
        List<DatosOpcionCategoriaMapaEventos> listaItemsMenu = new ArrayList<>();
        List<CategoriaEvento> listaCategorias = getListaCategorias(contexto);
        Resources resources = contexto.getResources();
        int i=0;
        for(CategoriaEvento categoria : listaCategorias) {
            String textoMenu = categoria.getNombre();
            String nombreIcono = categoria.getNombreIcono();
            nombreIcono = nombreIcono.substring(0, nombreIcono.lastIndexOf("."));
            int identificadorImagen = resources.getIdentifier(nombreIcono, "mipmap", contexto.getPackageName());
            DatosOpcionCategoriaMapaEventos datosItem = new DatosOpcionCategoriaMapaEventos(categoria, textoMenu, identificadorImagen);
            listaItemsMenu.add(datosItem);
        }

        ListView lstViewOpciones = (ListView)actividad.findViewById(R.id.listaOpcionesMapaEventos);
        lstViewOpciones.setAdapter(new OpcionCategoriaMapaAdaptador(actividad, listaItemsMenu));

        lstViewOpciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DatosOpcionCategoriaMapaEventos datosItem = (DatosOpcionCategoriaMapaEventos) view.getTag();
                CategoriaEvento categoria = datosItem.getCategoria();
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBoxMostrarCategoria);
                checkBox.setChecked(!checkBox.isChecked());
                mostrarOcultarMarcas(categoria, checkBox.isChecked());
            }
        });
    }

    private void mostrarOcultarMarcas(CategoriaEvento categoria, boolean mostrar) {
        List<Marker> lstMarkers = categoriasMarkers.get(categoria.getId());
        if(lstMarkers != null) {
            for (Marker markerSitio : lstMarkers) {
                markerSitio.setVisible(mostrar);
            }
        }

        List<Polygon> lstPolygon = categoriasPolygonMapa.get(categoria.getId());
        if(lstPolygon != null) {
            for (Polygon poligono : lstPolygon) {
                poligono.setVisible(mostrar);
            }
        }


        List<Polyline> lstPolyline = categoriasPolylineMapa.get(categoria.getId());
        if(lstPolyline != null) {
            for (Polyline polyline : lstPolyline) {
                polyline.setVisible(mostrar);
            }
        }


        List<Circle> lstCircle = categoriasCircleMapa.get(categoria.getId());
        if(lstCircle != null) {
            for (Circle circle : lstCircle) {
                circle.setVisible(mostrar);
            }
        }
    }

    private List<CategoriaEvento> getListaCategorias(Context contexto) {
        CategoriaEventoDataSource dataSource = new CategoriaEventoDataSource(contexto);
        dataSource.open();
        List<CategoriaEvento> categorias = dataSource.getByIdEvento(evento.getId());
        dataSource.close();

        return categorias;
    }

    public void mostrarDetalle(ActividadEvento actividadEvento) {
        if(dialogoSeleccion != null) {
            dialogoSeleccion.cancel();
        }
        showInfoMarker(_googleMap, actividadEvento);
    }

}
