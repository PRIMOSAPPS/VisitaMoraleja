package com.primos.visitamoraleja.actividades.eventos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.primos.visitamoraleja.ActionBarListActivity;
import com.primos.visitamoraleja.MainActivity;
import com.primos.visitamoraleja.R;
import com.primos.visitamoraleja.menulateral.ConfigMenuLateral;

/**
 * Created by h on 2/07/16.
 */
public abstract class AbstractEventos extends ActionBarListActivity {

    private DrawerLayout mDrawer;
    private ListView mDrawerOptions;

    protected void initMenuLateral() {
        mDrawerOptions = (ListView) findViewById(R.id.menuLateralListaSitios);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout_lateral);
        ConfigMenuLateral cml = new ConfigMenuLateral(this, null, false);
        cml.iniciarMenuLateral();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bar_eventos_normal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.actionbar_inicio:
                Intent i = new Intent(this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra(MainActivity.ACTUALIZAR, false);
                startActivity(i);
                return true;
            case R.id.actionbar_menu_lateral:
                if (mDrawer.isDrawerOpen(mDrawerOptions)){
                    mDrawer.closeDrawers();
                }else{
                    mDrawer.openDrawer(mDrawerOptions);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
