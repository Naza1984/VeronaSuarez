package org.openxava.veronasuarez.model;
import javax.persistence.*;

import org.openxava.annotations.*;


@Entity
public class Medio extends Identificable { // Extiende de Identifiable por tanto no necesita tener una propiedad id
	@Column(length=50)
	private String nombreMedio;
	
	@ManyToOne( // La referencia se almacena como una relaci�n en la base de datos
			fetch=FetchType.LAZY, // La referencia se carga bajo demanda
			optional=true) // La referencia puede estar sin valor
	@DescriptionsList(descriptionProperties = "tipoMedio")// As� la referencia se visualiza con un combo
	private TipoMedio tipoMedio; // Una referencia Java convencional

	public String getNombreMedio() {
		return nombreMedio;
	}

	public void setNombreMedio(String nombreMedio) {
		this.nombreMedio = nombreMedio;
	}

	public TipoMedio getTipoMedio() {
		return tipoMedio;
	}

	public void setTipoMedio(TipoMedio tipoMedio) {
		this.tipoMedio = tipoMedio;
	}
}
