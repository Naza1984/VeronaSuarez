package org.openxava.veronasuarez.model;

import javax.persistence.*;
import org.openxava.annotations.*;

@Entity // Esto marca la clase Cliente como una entidad
@View(name="Simple", // Esta vista solo se usar� cuando se especifique �Simple�
members="numeroCliente, nombreCliente" // en la misma l�nea
)
public class Cliente {
	@Id // La propiedad numeroCliente es la clave. Las claves son obligatorias (required) por defecto
	@Column(length=6) // La longitud de columna se usa a nivel UI y a nivel DB
	private int numeroCliente;
	
	@Column(length=50) 
	@Required // Se mostrar� un error de validaci�n si la propiedad name se deja en blanco
	private String nombreCliente;
	
	@Embedded // para referenciar a una clase incrustable
	private Direccion direccion; // Una referencia Java convencional
	
	public Direccion getDireccion() {
		if (direccion == null) direccion = new Direccion(); // As� nunca es nulo
		return direccion;
	}
	
	public void setDireccion(Direccion direccion) {
		this.direccion = direccion;
	}

	public int getNumeroCliente() {
		return numeroCliente;
	}

	public void setNumeroCliente(int numeroCliente) {
		this.numeroCliente = numeroCliente;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
	
	
}