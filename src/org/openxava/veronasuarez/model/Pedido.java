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
@Views({ // Para declarar m�s de una vista
	@View( extendsView="super.DEFAULT",  // Extiende de la vista de DocumentoComercial
	members="entregado; factura { factura }" // y agrego las facturas dentro de una pesta�a
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
	@SearchAction("Pedido.searchInvoice") // Define la acci�n a ejecutar cuando el usuario pulsa en el bot�n de la linterna para buscar una factura. Es decir la acci�n searchInvoice del controlador Pedido definido en el archivo controllers.xml.
	private Factura factura; // A�adida referencia a Factura
	
	//para que el usuario no pueda asignar pedidos a una factura si los pedidos no han sido servidos todav�a. 
	//Es decir, solo los pedidos servidos pueden asociarse a una factura.
	@OnChange(ShowHideCreateInvoiceAction.class)
	private boolean entregado;
	
	//el usuario no puede asignar pedidos a una factura si los pedidos no han sido entregados todav�a. 
	//Es decir, solo los pedidos entregados pueden asociarse a una factura.
	//Hibernate Validator es el responsable de llamar este m�todo al grabar, y lanzar la InvalidStateException correspondiente si la validaci�n no pasa.
	@AssertTrue  
	private boolean estaEntregadoParaEstarEnFactura() {
		return factura == null || estaEntregado(); // La l�gica de validaci�n
	}
	
	//para impedir que un usuario borre un pedido si �ste tiene una factura asociada
	//si la validaci�n falla el pedido no se borra y se lanzar� una IllegalStateException
	@PreRemove // Justo antes de borrar la entidad
	private void validateOnRemove() {  //// Ahora este m�todo no se ejecuta autom�ticamente ya que el borrado real no se produce
		if (factura != null) { // La l�gica de validaci�n
			throw new IllegalStateException( // Lanza una excepci�n runtime
			XavaResources.getString( // Para obtener un mensaje de texto
			"cannot_delete_order_with_invoice"));
		}
	}
	
	public void createInvoice() throws ValidationException // Una excepci�n de aplicaci�n
	{
		if (this.factura != null) { // Si ya tiene una factura no podemos crearla
			throw new ValidationException("impossible_create_invoice_order_already_has_one");// Admite un id de 18n como argumento
		} 
		if (!estaEntregado()) {// Si el pedido no est� entregado no podemos crear la factura
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
		} catch (Exception ex) {//Cualquier excepci�n inesperada
			throw new SystemException("impossible_create_invoice", ex);// Se lanza una excepci�n runtime
		}
	}
	
	public void copyDetailsToInvoice(Factura factura)  {
		try { // Envolvemos todo el c�digo del m�todo con un try/catch
			for (Detalle orderDetail: getDetalles()) { // Itera por los detalles del pedido actual
				Detalle invoiceDetail = (Detalle) // Clona el detalle (1)
				BeanUtils.cloneBean(orderDetail);
				invoiceDetail.setOid(null); // Para ser grabada como una nueva entidad (2)
				invoiceDetail.setPadre(factura); // El punto clave: poner un nuevo padre (3)
				XPersistence.getManager().persist(invoiceDetail); // (4)
			}
		} catch (Exception ex) {
				// As� convertimos cualquier excepci�n en una excepci�n runtime
				throw new SystemException( 
				"impossible_copy_details_to_invoice", ex);
		}	
	}
	
	public void copyDetailsToInvoice() {
		copyDetailsToInvoice(getFactura()); // Delegamos en un m�todo ya existente
	}
	
	@AssertTrue 
	private boolean isCustomerOfInvoiceMustBeTheSame() {// Este m�todo tiene que devolver true para que este pedido sea v�lido
		return factura == null || // factura es opcional
			factura.getCliente().getNumeroCliente()==getCliente().getNumeroCliente();
	}
	
	//mantengo la validaci�n para el borrado personalizado sobrescribiendo el m�todo setBorrada() 
	public void setBorrada(boolean borrada) {
		if (borrada) validateOnRemove(); // Llamo a la validaci�n expl�citamente
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