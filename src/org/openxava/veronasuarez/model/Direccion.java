package org.openxava.veronasuarez.model;

import javax.persistence.*;

@Embeddable // Uso @Embeddable en vez de @Entity para modelar una clase incrustable 
//Luego, creo una referencia anotada como @Embedded a esta clase desde una entidad
public class Direccion {
	@Column(length=30) // Los miembros se anotan igual que en las entidades
	private String calle;
	
	@Column(length=5)
	private int codigoPostal;
	
	@Column(length=20)
	private String ciudad;
	
	@Column(length=30)
	private String provincia;
	
	public String getCalle() {
		return calle;
	}
	
	public void setCalle(String calle) {
		this.calle = calle;
	}
	
	public int getCodigoPostal() {
		return codigoPostal;
	}
	
	public void setCodigoPostal(int codigoPostal) {
		this.codigoPostal = codigoPostal;
	}
	
	public String getCiudad() {
		return ciudad;
	}
	
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	
	public String getProvincia() {
		return provincia;
	}
	
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}


}