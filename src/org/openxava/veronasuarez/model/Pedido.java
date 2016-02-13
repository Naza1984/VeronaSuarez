package org.openxava.veronasuarez.model;

import java.util.*;

import javax.persistence.*;

import org.apache.commons.beanutils.*;
import org.hibernate.validator.*;
import org.openxava.annotations.*;
import org.openxava.veronasuarez.actions.*;
import org.openxava.util.*;
import org.openxava.validators.*;
import org.openxava.jpa.XPersistence;

@Entity
@Views({ // Para declarar más de una vista
	@View( extendsView="super.DEFAULT",  // Extiende de la vista de DocumentoComercial
	members="entregado; factura { factura }" // y agrego las facturas dentro de una pestaña
	),
	
	@View( name="NoClienteNoFactura", // Una vista llamada NoClienteNoFactura que no incluye cliente ni factura
	members=   // Ideal para ser usada desde la interfaz de Factura cuando quiere editarse un pedido
	"anio, numero, fecha;" + 
	"detalles;" +
	"observaciones"
	)
})
@Tabs({
	@Tab(baseCondition = "borrada = false",
		properties="anio, numero, fecha, cliente.numeroCliente, cliente.nombreCliente," +
		"entregado, porcentajeIVA, montoBase, " +
		"iva, montoTotal, monto, observaciones"
	),
	
	@Tab(name="Borrada", baseCondition = "borrada = true")
})
public class Pedido extends DocumentoComercial {
	
	@ManyToOne
	@ReferenceView("NoClienteNoPedido") // referencio a la vista definida en Factura 
	@OnChange(ShowHideCreateInvoiceAction.class)
	@OnChangeSearch(OnChangeSearchInvoiceAction.class) 
	@SearchAction("Pedido.searchInvoice") // Define la acción a ejecutar cuando el usuario pulsa en el botón de la linterna para buscar una factura. Es decir la acción searchInvoice del controlador Pedido definido en el archivo controllers.xml.
	private Factura factura; // Añadida referencia a Factura
	
	//para que el usuario no pueda asignar pedidos a una factura si los pedidos no han sido servidos todavía. 
	//Es decir, solo los pedidos servidos pueden asociarse a una factura.
	@OnChange(ShowHideCreateInvoiceAction.class)
	private boolean entregado;
	
	//el usuario no puede asignar pedidos a una factura si los pedidos no han sido entregados todavía. 
	//Es decir, solo los pedidos entregados pueden asociarse a una factura.
	//Hibernate Validator es el responsable de llamar este método al grabar, y lanzar la InvalidStateException correspondiente si la validación no pasa.
	@AssertTrue  
	private boolean estaEntregadoParaEstarEnFactura() {
		return factura == null || estaEntregado(); // La lógica de validación
	}
	
	//para impedir que un usuario borre un pedido si éste tiene una factura asociada
	//si la validación falla el pedido no se borra y se lanzará una IllegalStateException
	@PreRemove // Justo antes de borrar la entidad
	private void validateOnRemove() {  //// Ahora este método no se ejecuta automáticamente ya que el borrado real no se produce
		if (factura != null) { // La lógica de validación
			throw new IllegalStateException( // Lanza una excepción runtime
			XavaResources.getString( // Para obtener un mensaje de texto
			"cannot_delete_order_with_invoice"));
		}
	}
	
	public void createInvoice() throws ValidationException // Una excepción de aplicación
	{
		if (this.factura != null) { // Si ya tiene una factura no podemos crearla
			throw new ValidationException("impossible_create_invoice_order_already_has_one");// Admite un id de 18n como argumento
		} 
		if (!estaEntregado()) {// Si el pedido no está entregado no podemos crear la factura
			throw new ValidationException("impossible_create_invoice_order_is_not_delivered");
		} 
		
		try {
			Factura factura = new Factura();
			BeanUtils.copyProperties(factura, this);
			factura.setOid(null);
			factura.setFecha(new Date());
			factura.setDetalles(new ArrayList());
			XPersistence.getManager().persist(factura);
			copyDetailsToInvoice(factura);
			this.factura = factura;
		} catch (Exception ex) {//Cualquier excepción inesperada
			throw new SystemException("impossible_create_invoice", ex);// Se lanza una excepción runtime
		}
	}
	
	public void copyDetailsToInvoice(Factura factura)  {
		try { // Envolvemos todo el código del método con un try/catch
			for (Detalle orderDetail: getDetalles()) { // Itera por los detalles del pedido actual
				Detalle invoiceDetail = (Detalle) // Clona el detalle (1)
				BeanUtils.cloneBean(orderDetail);
				invoiceDetail.setOid(null); // Para ser grabada como una nueva entidad (2)
				invoiceDetail.setPadre(factura); // El punto clave: poner un nuevo padre (3)
				XPersistence.getManager().persist(invoiceDetail); // (4)
			}
		} catch (Exception ex) {
				// Así convertimos cualquier excepción en una excepción runtime
				throw new SystemException( 
				"impossible_copy_details_to_invoice", ex);
		}	
	}
	
	public void copyDetailsToInvoice() {
		copyDetailsToInvoice(getFactura()); // Delegamos en un método ya existente
	}
	
	@AssertTrue 
	private boolean isCustomerOfInvoiceMustBeTheSame() {// Este método tiene que devolver true para que este pedido sea válido
		return factura == null || // factura es opcional
			factura.getCliente().getNumeroCliente()==getCliente().getNumeroCliente();
	}
	
	//mantengo la validación para el borrado personalizado sobrescribiendo el método setBorrada() 
	public void setBorrada(boolean borrada) {
		if (borrada) validateOnRemove(); // Llamo a la validación explícitamente
		super.setBorrada(borrada);
	}
	
	public boolean estaEntregado() {
		return entregado;
	}
	
	public boolean getEntregado() {
		return entregado;
	}
	
	public void setEntregado(boolean entregado) {
		this.entregado = entregado;
	}
	
	public Factura getFactura() {
		return factura;
	}
	
	public void setFactura(Factura factura) {
		this.factura = factura;
	}
}