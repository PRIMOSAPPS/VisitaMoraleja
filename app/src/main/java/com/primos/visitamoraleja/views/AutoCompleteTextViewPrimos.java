package com.primos.visitamoraleja.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;

public class AutoCompleteTextViewPrimos extends AutoCompleteTextView {
	private boolean ocultadoTeclado = false;

	public AutoCompleteTextViewPrimos(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public AutoCompleteTextViewPrimos(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AutoCompleteTextViewPrimos(Context context) {
		super(context);
	}
    
    @Override
    public void showDropDown() {
    	// Si se ha mostrado, es por que se ha pulsado una tecla
    	ocultadoTeclado = false;
    	super.showDropDown();
    }

    @Override
    public void dismissDropDown() {
		InputMethodManager imm = (InputMethodManager)getContext().getSystemService(
			      Context.INPUT_METHOD_SERVICE);
    	if(isPopupShowing() && !ocultadoTeclado) {
    		imm.hideSoftInputFromWindow(this.getWindowToken(), 0);
    		ocultadoTeclado = true;
    	} else {
    		super.dismissDropDown();
    	}
//    	showDropDown();
    }
    
    /**
     * Metodo para forzar que se muestre el teclado
     */
    public void mostrarTeclado() {
		InputMethodManager imm = (InputMethodManager)getContext().getSystemService(
			      Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
				InputMethodManager.HIDE_IMPLICIT_ONLY);
	}
    
 // Desactivamos el filtro de autocomplete
    @Override
    protected void performFiltering(final CharSequence text, final int keyCode) {
        String filterText = "";
        super.performFiltering(filterText, keyCode);
    }
    
}
