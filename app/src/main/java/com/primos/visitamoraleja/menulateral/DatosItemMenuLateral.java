package com.primos.visitamoraleja.menulateral;

public class DatosItemMenuLateral {
	private String textoMenu;
	private int identificadorIcono;
	
	public DatosItemMenuLateral(String textoMenu, int identificadorIcono) {
		super();
		this.textoMenu = textoMenu;
		this.identificadorIcono = identificadorIcono;
	}

	public String getTextoMenu() {
		return textoMenu;
	}

	public int getIdentificadorIcono() {
		return identificadorIcono;
	}

}
