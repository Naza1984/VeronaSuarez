package org.openxava.veronasuarez.model;

import javax.persistence.*;
import org.hibernate.annotations.*;
import org.openxava.annotations.*;

@MappedSuperclass // Marcada como una superclase mapeada 
//Es una clase Java con anotaciones de mapeo JPA, pero no es una entidad en sí. 
//Su único objetivo es ser usada como clase base para definir entidades.
public class Identificable {
	@Id 
	@GeneratedValue(generator="system-uuid")  //uso el algoritmo Identificador Universal Unico para generar el identificador
	//La ventaja es que puedo migrar la aplicación a otras bases de datos (MySQL, Oracle, etc) sin tocar el código
	@Hidden    //esta propiedad no se va a mostrar al usuario
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	@Column(length=32)
	private String oid; 

	public String getOid() {
		return oid;
	}
	
	public void setOid(String oid) {
		this.oid = oid;
	}
}