package org.openxava.veronasuarez.model;
import java.math.*;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.veronasuarez.calculators.*;

@Entity
@View(members="producto; cantidad, precioPorUnidad, monto")
public class Detalle extends Identificable{
	@ManyToOne // Sin lazy fetching, si no falla al quitar un detalle desde el padre
	private DocumentoComercial padre; // As� la relaci�n entre Detalle e Factura es bidireccional
	
	private int cantidad;
	
	@ManyToOne(fetch=FetchType.LAZY, optional=true)
	@ReferenceView("Simple") // Producto se visualiza usando su vista Simple
	@NoFrame // No se usa un marco alrededor de los datos de producto
	private Producto producto;
	
	/* 
	El usuario puede querer cambiar el precio unitario. Ademas, si el precio de un producto cambia los importes de todas 
	las facturas cambian tambi�n, y esto no es bueno.
	Entonces lo mejor es almacenar el precio de cada producto en cada l�nea de detalle. 
	Para esto agrego una propiedad persistente y calculos su valor desde el precio de Producto
	 */	
	@DefaultValueCalculator(
		value=CalculadorPrecioUnitario.class, //clase que contiene la l�gica para calcular el valor inicial leyendo el precio del producto
		properties=@PropertyValue(
		name="numeroProducto", // La propiedad numeroProducto del calculador:
		from="producto.numeroProducto") // se llena con el valor de producto.numeroProducto de la entidad
	) 	
	@Stereotype("MONEY")
	private BigDecimal precioPorUnidad; 
	
	public BigDecimal getPrecioPorUnidad() { 
		return precioPorUnidad==null?
		BigDecimal.ZERO:precioPorUnidad; // As� nunca devuelve nulo
	}
	
	
	//Llamo al m�todo recalculateAmount() del CommercialDocument padre cada vez que se a�ade, quita o modifica un detalle:
	@PrePersist // Al grabar el detalle por primera vez
	private void onPersist() {
		getPadre().getDetalles().add(this); // Para tener la colecci�n sincronizada
		getPadre().recalcularMonto();
	}
	
	@PreUpdate // Cada vez que el detalle se modifica
	private void onUpdate() {
		getPadre().recalcularMonto();
	}
	
	@PreRemove // Al borrar el detalle
	private void onRemove() {
		if (getPadre().estaBorrando()) return; // para evitar excepciones: si el DocumentoComercial est� siendo borrado, no ejecuto esta l�gica
		getPadre().getDetalles().remove(this); // Para tener la colecci�n sincronizada
		getPadre().recalcularMonto();
	}

	
	//defino una propiedad calculada monto: no almacena su valor en la bbdd sino que se calcula cada vez que se accede
	@Stereotype("MONEY")
	// Cuando el usuario cambie precio o cantidad esta propiedad se recalcular� y se redibujar�
	// uso la propiedad persistente calculada precioPorUnidad que defin�
	@Depends("precioPorUnidad, cantidad") 
	public BigDecimal getMonto() {
		return new BigDecimal(cantidad)
		.multiply(getPrecioPorUnidad()); // getPrecioPorUnidad() en vez de producto.getPrecio()
	}
	
	public void setPrecioPorUnidad(BigDecimal precioPorUnidad) {
		this.precioPorUnidad = precioPorUnidad;
	}

	public DocumentoComercial getPadre() {
		return padre;
	}

	public void setPadre(DocumentoComercial padre) {
		this.padre = padre;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	
	
}