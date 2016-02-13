package org.openxava.veronasuarez.actions; 
import java.rmi.*;
import java.util.*;
import javax.ejb.*;
import org.openxava.actions.*; // Para usar AddElementsToCollectionAction
import org.openxava.veronasuarez.model.*;
import org.openxava.model.*;
import org.openxava.util.*;
import org.openxava.validators.*;

/*
Acción para añadir pedidos a la factura de la manera convencional, pero también copiar las
líneas de estos pedidos a la factura
*/
public class AddOrdersToInvoiceAction
extends AddElementsToCollectionAction { // Lógica estándar para añadir elementos a la colección
	//Sobrescribimos el método execute() sólo para refrescar la vista después del proceso.
	public void execute() throws Exception {
		super.execute(); // Usamos la lógica estándar “tal cual”
		getView().refresh(); // Para visualizar datos frescos, incluyendo los importes
	} // recalculados, que dependen de las líneas de detalle
	
	//Sobreescribo para refinar la lógica de asociar un pedido a la factura. 
	// El método llamado para asociar cada entidad a la principal, en este caso para asociar cada pedido a la factura
	protected void associateEntity(Map keyValues) 
		throws ValidationException, // 
		XavaException, ObjectNotFoundException, 
		FinderException, RemoteException
	{
		super.associateEntity(keyValues); // Ejecuta la lógica estándar 
		Pedido order = (Pedido) MapFacade.findEntity("Pedido", keyValues); 
		order.copyDetailsToInvoice(); // Delega el trabajo principal en la entidad 
	}
}