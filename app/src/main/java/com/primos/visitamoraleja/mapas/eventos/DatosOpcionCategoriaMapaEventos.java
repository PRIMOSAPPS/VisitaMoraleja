package com.primos.visitamoraleja.mapas.eventos;

import com.primos.visitamoraleja.contenidos.Categoria;
import com.primos.visitamoraleja.contenidos.CategoriaEvento;
import com.primos.visitamoraleja.mapas.IDatosOpcionMapa;

public class DatosOpcionCategoriaMapaEventos implements IDatosOpcionMapa {
	private String textoMenu;
	private int identificadorIcono;
	private CategoriaEvento categoria;

	public DatosOpcionCategoriaMapaEventos(CategoriaEvento categoria, String textoMenu, int identificadorIcono) {
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

	public CategoriaEvento getCategoria() {
		return categoria;
	}

}
