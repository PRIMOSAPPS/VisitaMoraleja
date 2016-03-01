package com.primos.visitamoraleja.mapas;

import com.primos.visitamoraleja.contenidos.Categoria;

public class DatosOpcionCategoriaMapa {
	private String textoMenu;
	private int identificadorIcono;
	private Categoria categoria;

	public DatosOpcionCategoriaMapa(Categoria categoria, String textoMenu, int identificadorIcono) {
		super();
		this.categoria = categoria;
		this.textoMenu = textoMenu;
		this.identificadorIcono = identificadorIcono;
	}

	public String getTextoMenu() {
		return textoMenu;
	}

	public int getIdentificadorIcono() {
		return identificadorIcono;
	}

	public Categoria getCategoria() {
		return categoria;
	}

}
