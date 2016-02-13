package org.openxava.veronasuarez.model;

import java.math.*;
import javax.persistence.*;
import org.openxava.annotations.*;

@Entity
@View(name="Simple", members="numeroProducto, nombreProducto")
public class Producto {
	@Id @Column(length=9)
	private int numeroProducto;
	
	@Column(length=50) @Required
	private String nombreProducto;
	
	@Stereotype("MONEY") // La propiedad price se usa para almacenar dinero
	private BigDecimal precio; // BigDecimal se suele usar para dinero
	@Stereotype("PHOTO") // El usuario puede ver y cambiar una foto
	private byte [] foto;
	@Stereotype("IMAGES_GALLERY") // Una galería de fotos completa está disponible
	@Column(length=32) // La cadena de 32 de longitud es para almacenar la clave de la galería
	private String masFotos;
	@Stereotype("MEMO") // Esto es para un texto grande, se usará un área de texto o equivalente
	private String observaciones;
	

	@ManyToOne( // La referencia se almacena como una relación en la base de datos
			fetch=FetchType.LAZY, // La referencia se carga bajo demanda
			optional=true) // La referencia puede estar sin valor
	@DescriptionsList(descriptionProperties = "nombreMedio")
	private Medio medio; // Una referencia Java convencional


	public int getNumeroProducto() {
		return numeroProducto;
	}


	public void setNumeroProducto(int numeroProducto) {
		this.numeroProducto = numeroProducto;
	}


	public String getNombreProducto() {
		return nombreProducto;
	}


	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}


	public BigDecimal getPrecio() {
		return precio;
	}


	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}


	public byte[] getFoto() {
		return foto;
	}


	public void setFoto(byte[] foto) {
		this.foto = foto;
	}


	public String getMasFotos() {
		return masFotos;
	}


	public void setMasFotos(String masFotos) {
		this.masFotos = masFotos;
	}


	public String getObservaciones() {
		return observaciones;
	}


	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}


	public Medio getMedio() {
		return medio;
	}


	public void setMedio(Medio medio) {
		this.medio = medio;
	}
		

}