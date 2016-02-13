package org.openxava.veronasuarez.model;


import java.math.*;
import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.*;
//import org.hibernate.validator.*;

import org.openxava.annotations.*;
import org.openxava.calculators.*;
import org.openxava.veronasuarez.calculators.*;
import org.openxava.jpa.*;
import org.openxava.validators.*;

@Entity
@View(members=
	"anio, numero, fecha;" +  //en una misma linea
	"datos {" +  // Una pestaña 'datos' para los datos principales del documento
	"cliente;" +
	"detalles;" +
	"montos [ " + // Los corchetes indican un grupo, visualizado dentro de un marco
	" porcentajeIVA, montoBase, iva, montoTotal" +
	"];" +
	"observaciones" +
	"}"
)
abstract public class DocumentoComercial extends Borrable{	//extiende de Borrable para heredar las propiedades
	@SearchKey 
	@Column(length=4) 
	@DefaultValueCalculator(CurrentYearCalculator.class)
	private int anio;
	
	@SearchKey 
	@Column(length=6) 
	@ReadOnly(forViews="DEFAULT") // El usuario no puede modificar el valor
	private int numero;
	
	@Required
	@DefaultValueCalculator(CurrentDateCalculator.class) // Fecha actual
	private Date fecha;
	
	@Stereotype("MEMO")
	private String observaciones;
	
	@ManyToOne(fetch=FetchType.LAZY, optional=false) // cliente es obligatorio
	@ReferenceView("Simple") // La vista llamada 'Simple' se usará para visualizar esta referencia
	private Cliente cliente;
	
	@OneToMany( // Para declararla como una colección persistente
			mappedBy="padre", // El miembro de Detalle que almacena la relación
			cascade=CascadeType.ALL) // Indica que es una colección de entidades dependientes
	@ListProperties(
			"producto.numeroProducto, producto.nombreProducto, " +
			"cantidad, precioPorUnidad, monto")
	private Collection<Detalle> detalles = new ArrayList<Detalle>();
	
	//propiedad calculada que suma los importes de las líneas. Recalculado cuando las líneas cambian
	@Stereotype("MONEY")
	public BigDecimal getMontoBase() {
		BigDecimal result = new BigDecimal("0.00");
		for (Detalle detalle: getDetalles()) { // Iteramos por todas la líneas de detalle
			result = result.add(detalle.getMonto()); // Acumulamos el importe
		} 
		
		return result;
	}
	
	
	//propiedad con un valor por defecto a cambiar por el usuario que se usará para calcular el IVA
	@Digits(integer=2, fraction=0)  // Para indicar su tamaño. La anotación es una alternativa a @Column para especificar el tamaño.
	@Required  
	@DefaultValueCalculator(CalculadorPorcentajeIVA.class)//cuando el usuario crea una factura, el campo con el valor que haya puesto en el arvhico de propiedades
	private BigDecimal porcentajeIVA;
	
	//propiedad calculada
	@Stereotype("MONEY")
	@Depends("porcentajeIVA") // Cuando porcentajeIVA cambia entonces IVA se recalcula y revisualiza
	public BigDecimal getIva() {
		return getMontoBase() // montoBase * porcentajeIVA / 100
		.multiply(getPorcentajeIVA())
		.divide(new BigDecimal("100"));
	}
	
	
	//propiedad calculada
	@Stereotype("MONEY")
	@Depends("montoBase, iva") // Cuando montoBase o IVA cambian montoTotal se recalcula y revisualiza
	public BigDecimal getMontoTotal() { // 
		return getMontoBase().add(getIva()); // montoBase + IVA
	}
	
	//Agrego esta propiedad persistente que contendrá el mismo valor que la calculada montoTotal
	//Esto es porque si quiero informe de todas las facturas, si la bbdd es grande el proceso puede ser muy lento porque se instancian todas las facturas para calcular su importe total. 
	//Para esto tengo que sincronizar los valores usando métodos de retrollamada JPA en Detalle
	@Stereotype("MONEY")
	private BigDecimal monto;
	
	@Transient // No se almacena en la tabla de la base de datos
	private boolean borrando = false; // Indica si JPA está borrando el documento ahora

	boolean estaBorrando() { // Acceso paquete, no es accesible desde fuera
		return borrando;
	}
	
	@PreRemove // Cuando el documento va a ser borrado marcamos borrando como true
	private void marcarBorrando() {
		this.borrando = true;
	}
	
	@PostRemove // Cuando el documento ha sido borrado marcamos borrando como false
	private void desmarcarBorrando() {
		this.borrando = false;
	}
	
	public BigDecimal getMonto() {
		return monto;
	}
	
	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
	
	
	
	@PrePersist // Ejecutado justo antes de grabar el objeto por primera vez
	public void calcularNumero() throws Exception {
		Query query = XPersistence.getManager()
		.createQuery("select max(i.numero) from " +
		getClass().getSimpleName() + //devuelve el nombre de la clase (sin paquete):Pedido o Factura. Así podemos obtener una numeración diferente
		" i where i.anio = :anio");
		query.setParameter("anio", anio);
		Integer lastNumber = (Integer) query.getSingleResult();
		this.numero = lastNumber == null?1:lastNumber + 1;
	}
	
	
	
	public String toString() {
		return anio + "/" + numero;
	}
	
	//sincroniza la propiedad persistente monto con el valor de la calculada montoTotal
	//La llamo desde Detalle cada vez que se añade, quita o modifica un detalle
	public void recalcularMonto() {
		setMonto(getMontoTotal());
	}
	
	public BigDecimal getPorcentajeIVA() {
		return porcentajeIVA==null?
				BigDecimal.ZERO:porcentajeIVA; // Asi nunca devuelve nulo
	}
	
	public void setPorcentajeIVA(BigDecimal porcentajeIVA) {
		this.porcentajeIVA = porcentajeIVA;
	}


	public int getAnio() {
		return anio;
	}

	public void setAnio(int anio) {
		this.anio = anio;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Collection<Detalle> getDetalles() {
		return detalles;
	}

	public void setDetalles(Collection<Detalle> detalles) {
		this.detalles = detalles;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	
}