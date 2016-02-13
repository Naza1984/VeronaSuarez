package org.openxava.veronasuarez.model;
import javax.persistence.*;

@Entity
public class TipoMedio extends Identificable { // Extiende de Identifiable, por tanto no necesita tener una propiedad id
	@Column(length=50)
	private String tipoMedio;

	public String getTipoMedio() {
		return tipoMedio;
	}

	public void setTipoMedio(String tipoMedio) {
		this.tipoMedio = tipoMedio;
	}
	

}