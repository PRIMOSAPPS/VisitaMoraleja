package com.primos.visitamoraleja.adaptadores.eventos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.primos.visitamoraleja.R;
import com.primos.visitamoraleja.almacenamiento.AlmacenamientoFactory;
import com.primos.visitamoraleja.almacenamiento.ItfAlmacenamiento;
import com.primos.visitamoraleja.bdsqlite.datasource.EventosDataSource;
import com.primos.visitamoraleja.contenidos.ActividadEvento;
import com.primos.visitamoraleja.contenidos.Evento;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by h on 30/06/16.
 */
public class ExpandableListActividadesAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private Map<String, List<ActividadEvento>> _listDataChild;
    private SimpleDateFormat sdf;
    private Evento evento;

    public ExpandableListActividadesAdapter(Context context, List<String> listDataHeader,
                                 Map<String, List<ActividadEvento>> listChildData, long idEvento) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.sdf = new SimpleDateFormat("HH:mm");

        cargaEvento(idEvento);
    }

    private void cargaEvento(long idEvento) {
        EventosDataSource dataSource = null;
        try {
            dataSource = new EventosDataSource(_context);
            dataSource.open();
            evento = dataSource.getById(idEvento);
        } finally {
            dataSource.close();
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final ActividadEvento actividadEvento = (ActividadEvento) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.actividad_item, null);
        }
        convertView.setTag(actividadEvento);

        ImageView imagen = (ImageView)convertView.findViewById(R.id.imagenItemActiv);
        ItfAlmacenamiento almacenamiento = AlmacenamientoFactory.getAlmacenamiento(_context);
        Bitmap bitmap = almacenamiento.getImagenActividadEvento(actividadEvento.getId(), actividadEvento.getNombreIcono());
        imagen.setImageBitmap(bitmap);

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        StringBuilder sbTexto = new StringBuilder(actividadEvento.getNombre());
        Date inicio = actividadEvento.getInicio();
        if(inicio != null) {
            sbTexto.append(" ");
            sbTexto.append(sdf.format(inicio));
            Date fin = actividadEvento.getFin();
            if(fin != null) {
                sbTexto.append("-");
                sbTexto.append(sdf.format(fin));

            }
        }

        txtListChild.setText(sbTexto.toString());
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.actividades_group, null);
        }

        ImageView imagen = (ImageView)convertView.findViewById(R.id.imagenGroupActiv);
        ItfAlmacenamiento almacenamiento = AlmacenamientoFactory.getAlmacenamiento(_context);
        Bitmap bitmap = almacenamiento.getImagenEvento(evento.getId(), evento.getNombreIcono());
        imagen.setImageBitmap(bitmap);

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
