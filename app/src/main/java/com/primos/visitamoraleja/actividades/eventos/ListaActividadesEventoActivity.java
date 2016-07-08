package com.primos.visitamoraleja.actividades.eventos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.primos.visitamoraleja.ActionBarListActivity;
import com.primos.visitamoraleja.R;
import com.primos.visitamoraleja.adaptadores.eventos.ExpandableListActividadesAdapter;
import com.primos.visitamoraleja.bdsqlite.datasource.ActividadEventoDataSource;
import com.primos.visitamoraleja.contenidos.ActividadEvento;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by h on 25/06/16.
 */
public class ListaActividadesEventoActivity extends AbstractEventos {
    private final static String TAG = "ListaActividadEventActi";
    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private Map<String, List<ActividadEvento>> listDataChild;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    private long idEvento;

    class ComparadorActividadesFecha implements Comparator<ActividadEvento> {
        @Override
        public int compare(ActividadEvento lhs, ActividadEvento rhs) {
            int resul = 0;
            if(lhs != null && rhs != null) {
                Date lhsDate = lhs.getInicio();
                Date rhsDate = rhs.getInicio();
                if(lhsDate != null && rhsDate != null) {
                    resul = lhsDate.compareTo(rhsDate);
                }
            }
            return resul;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_actividades_expandible);

        idEvento = (long) getIntent().getExtras().get(EventoPrincipalActivity.ID_EVENTO);


        setTitulo(getResources().getString(R.string.actividades));

        cargar();

        initMenuLateral();

        expListView = (ExpandableListView) findViewById(R.id.expandableActividades);

        listAdapter = new ExpandableListActividadesAdapter(this, listDataHeader, listDataChild, idEvento);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        if(!listDataHeader.isEmpty()) {
            String fechaHoy = sdf.format(new Date());
            int indice = listDataHeader.indexOf(fechaHoy);
            if (indice < 0) {
                indice = 0;
            }
            expListView.expandGroup(indice);
        }
    }

    protected void cargar() {
        ActividadEventoDataSource dataSource = null;

        try {
            dataSource = new ActividadEventoDataSource(this);
            dataSource.open();

            List<ActividadEvento> lista = dataSource.getByIdEvento(idEvento);
            //setListAdapter(new ActividadEventoAdaptador(this, lista));
            prepareListData(lista);

        } finally {
            dataSource.close();
        }
    }

    public void mostrarDetalle(View view) {
        ActividadEvento actividadEvento = (ActividadEvento)view.getTag();
        Intent i = new Intent(this, ActividadEventoDetalleActivity.class);
        i.putExtra(ActividadEventoDetalleActivity.ID_ACTIVIDAD_EVENTO, actividadEvento.getId());
        startActivity(i);
    }

    private void prepareListData(List<ActividadEvento> lista) {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<ActividadEvento>>();

        Map<String, List<ActividadEvento>> actividadesAgrupadas = agruparPorFecha(lista);
        Set<String> keys = actividadesAgrupadas.keySet();
        List<String> listaKeys = new ArrayList(keys);
        Collections.sort(listaKeys, new Comparator<String>() {
            public int compare(String lhs, String rhs) {
                try {
                    Date dlhs = sdf.parse(lhs);
                    Date drhs = sdf.parse(rhs);
                    return dlhs.compareTo(drhs);
                } catch (ParseException e) {
                    Log.e(TAG, "Error al parsear la fecha");
                }
                return 0;
            }
        });
        int indice = 0;
        for(String fecha : listaKeys) {
            List<ActividadEvento> actividadesFecha = actividadesAgrupadas.get(fecha);
            // Se ordenan las actividades por inicio
            Collections.sort(actividadesFecha, new ComparadorActividadesFecha());
            if(!actividadesAgrupadas.isEmpty()) {
                listDataHeader.add(fecha);

                listDataChild.put(listDataHeader.get(indice), actividadesFecha);
                indice++;
            }
        }
    }

    private Map<String, List<ActividadEvento>> agruparPorFecha(List<ActividadEvento> lista) {
        Map<String, List<ActividadEvento>> resul = new HashMap<>();

        for(ActividadEvento actividad : lista) {
            String fecha = sdf.format(actividad.getInicio());
            List<ActividadEvento> actividadesFecha = resul.get(fecha);
            if(actividadesFecha == null) {
                actividadesFecha = new ArrayList<>();
                resul.put(fecha, actividadesFecha);
            }
            actividadesFecha.add(actividad);
        }

        return resul;
    }
}
