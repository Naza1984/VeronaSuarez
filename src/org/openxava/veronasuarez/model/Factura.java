package org.openxava.veronasuarez.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.validators.*;


@Entity
@Views({
	@View( extendsView="super.DEFAULT", // Extiende de la vista de DocumentoComercial
			members="pedidos { pedidos } " // y agrego los pedidos dentro de una pestaña
	),
	@View( name="NoClienteNoPedido", // desde la interfaz de usuario de Pedido referencio a esta vista que no muestra cliente ni pedidos 
		members= 
			"anio, numero, fecha;" + 
			"detalles;" +
			"observaciones"
	)
})
//@Tab permite definir la forma en que los datos tabulares (los datos mostrados en modo lista) 
//son visualizados, y permite además definir una condición
@Tabs({ 
	@Tab(
		baseCondition = "borrada = false",//el modo lista no mostrará las entidades “marcadas como borradas”.
		properties="anio, numero, fecha, cliente.numeroCliente, cliente.nombreCliente," +
				"porcentajeIVA, montoBase, " +
				"iva, montoTotal, monto, observaciones"
	),
	@Tab(name="Borrada", baseCondition = "borrada = true") // Tab con nombre
})
public class Factura extends DocumentoComercial {
	@OneToMany(mappedBy="factura")
	@CollectionView("NoClienteNoFactura") //vista de Pedido referenciada para visualizar o editar elementos individuales de la colección
	@NewAction("Factura.addOrders") // Define nuestra propia acción para añadir pedidos
	private Collection<Pedido> pedidos; // Añadimos colección de entidades Order
	
	/*
	La lógica de negocio para crear una nueva Factura a partir de varias entidades Pedido está en la capa del modelo. 
	No podemos poner el método en la clase Pedido porque el proceso se hace a partir de varios pedidos, no de uno. 
	No podemos usar un método de instancia en Factura porque todavía no existe el objeto Factura, de hecho lo que queremos es crearlo.
	Por lo tanto, vamos a crear un método de factoría estático en la clase Factura para crear una nueva factura a partir de varios pedidos. 
	*/
	public static Factura createFromOrders(Collection<Pedido> pedidos) throws ValidationException
	{
		Factura factura = null;
		for (Pedido pedido: pedidos) {
			if (factura == null) { // La primera vez, el primer pedido
				pedido.createInvoice(); // Reutilizamos la lógica para crear una factura a partir de un pedido
				factura = pedido.getFactura(); // y cogemos la factura recién creada
			} 
			else {
				// Para el resto de los pedido la factura ya está creada
				pedido.setFactura(factura); // Asigna la factura
				pedido.copyDetailsToInvoice(factura); // Copia la línea. Por eso este método es publico en Pedido
			}  
		} 
		
		/*
		Si factura es nulo al final del proceso, es porque la colección pedidos está vacía. 
		En este caso lanzamos una ValidationException, ya que la acción no atrapa las excepciones, 
		OpenXava muestra el mensaje de la ValidationException al usuario. 
		Si el usuario no marca los pedido y pulsa en el botón para crear la factura, le aparecerá este mensaje de error.
		*/
		if (factura == null) { 
			throw new ValidationException("impossible_create_invoice_orders_not_specified");
		} 
		
		return factura;
	}
	
	public Collection<Pedido> getPedidos() {
		return pedidos;
	}
	
	public void setPedidos(Collection<Pedido> pedidos) {
		this.pedidos = pedidos;
	}
}
