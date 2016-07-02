package com.primos.visitamoraleja.actividades.eventos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.primos.visitamoraleja.ActionBarListActivity;
import com.primos.visitamoraleja.R;
import com.primos.visitamoraleja.adaptadores.eventos.ExpandableListActividadesAdapter;
import com.primos.visitamoraleja.bdsqlite.datasource.ActividadEventoDataSource;
import com.primos.visitamoraleja.contenidos.ActividadEvento;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by h on 25/06/16.
 */
public class ListaActividadesEventoActivity extends AbstractEventos {
    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private Map<String, List<ActividadEvento>> listDataChild;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    private long idEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_actividades_expandible);

        idEvento = (long) getIntent().getExtras().get(EventoPrincipalActivity.ID_EVENTO);


        setTitulo(getResources().getString(R.string.actividades));

        cargar();

        initMenuLateral();

        expListView = (ExpandableListView) findViewById(R.id.expandableActividades);

        listAdapter = new ExpandableListActividadesAdapter(this, listDataHeader, listDataChild);

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
        int indice = 0;
        for(String fecha : keys) {
            List<ActividadEvento> actividadesFecha = actividadesAgrupadas.get(fecha);
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
