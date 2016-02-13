package org.openxava.veronasuarez.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.validators.*;


@Entity
@Views({
	@View( extendsView="super.DEFAULT", // Extiende de la vista de DocumentoComercial
			members="pedidos { pedidos } " // y agrego los pedidos dentro de una pesta�a
	),
	@View( name="NoClienteNoPedido", // desde la interfaz de usuario de Pedido referencio a esta vista que no muestra cliente ni pedidos 
		members= 
			"anio, numero, fecha;" + 
			"detalles;" +
			"observaciones"
	)
})
//@Tab permite definir la forma en que los datos tabulares (los datos mostrados en modo lista) 
//son visualizados, y permite adem�s definir una condici�n
@Tabs({ 
	@Tab(
		baseCondition = "borrada = false",//el modo lista no mostrar� las entidades �marcadas como borradas�.
		properties="anio, numero, fecha, cliente.numeroCliente, cliente.nombreCliente," +
				"porcentajeIVA, montoBase, " +
				"iva, montoTotal, monto, observaciones"
	),
	@Tab(name="Borrada", baseCondition = "borrada = true") // Tab con nombre
})
public class Factura extends DocumentoComercial {
	@OneToMany(mappedBy="factura")
	@CollectionView("NoClienteNoFactura") //vista de Pedido referenciada para visualizar o editar elementos individuales de la colecci�n
	@NewAction("Factura.addOrders") // Define nuestra propia acci�n para a�adir pedidos
	private Collection<Pedido> pedidos; // A�adimos colecci�n de entidades Order
	
	/*
	La l�gica de negocio para crear una nueva Factura a partir de varias entidades Pedido est� en la capa del modelo. 
	No podemos poner el m�todo en la clase Pedido porque el proceso se hace a partir de varios pedidos, no de uno. 
	No podemos usar un m�todo de instancia en Factura porque todav�a no existe el objeto Factura, de hecho lo que queremos es crearlo.
	Por lo tanto, vamos a crear un m�todo de factor�a est�tico en la clase Factura para crear una nueva factura a partir de varios pedidos. 
	*/
	public static Factura createFromOrders(Collection<Pedido> pedidos) throws ValidationException
	{
		Factura factura = null;
		for (Pedido pedido: pedidos) {
			if (factura == null) { // La primera vez, el primer pedido
				pedido.createInvoice(); // Reutilizamos la l�gica para crear una factura a partir de un pedido
				factura = pedido.getFactura(); // y cogemos la factura reci�n creada
			} 
			else {
				// Para el resto de los pedido la factura ya est� creada
				pedido.setFactura(factura); // Asigna la factura
				pedido.copyDetailsToInvoice(factura); // Copia la l�nea. Por eso este m�todo es publico en Pedido
			}  
		} 
		
		/*
		Si factura es nulo al final del proceso, es porque la colecci�n pedidos est� vac�a. 
		En este caso lanzamos una ValidationException, ya que la acci�n no atrapa las excepciones, 
		OpenXava muestra el mensaje de la ValidationException al usuario. 
		Si el usuario no marca los pedido y pulsa en el bot�n para crear la factura, le aparecer� este mensaje de error.
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
