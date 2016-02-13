package org.openxava.veronasuarez.model;

import javax.persistence.*;

import org.openxava.annotations.*;

//Clase usada para marcar como borrada una entidad sin borrarla realmente.

//una superclase mapeada no es una entidad, es una clase con propiedades, métodos y anotaciones de mapeo 
//para ser usada como superclase para entidades

@MappedSuperclass
public class Borrable extends Identificable {
	@Hidden
	private boolean borrada;
	
	public boolean estaBorrada() {
		return borrada;
	}
	
	public void setBorrada(boolean borrada) {
		this.borrada = borrada;
	}
}
