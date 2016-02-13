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
Acci�n para a�adir pedidos a la factura de la manera convencional, pero tambi�n copiar las
l�neas de estos pedidos a la factura
*/
public class AddOrdersToInvoiceAction
extends AddElementsToCollectionAction { // L�gica est�ndar para a�adir elementos a la colecci�n
	//Sobrescribimos el m�todo execute() s�lo para refrescar la vista despu�s del proceso.
	public void execute() throws Exception {
		super.execute(); // Usamos la l�gica est�ndar �tal cual�
		getView().refresh(); // Para visualizar datos frescos, incluyendo los importes
	} // recalculados, que dependen de las l�neas de detalle
	
	//Sobreescribo para refinar la l�gica de asociar un pedido a la factura. 
	// El m�todo llamado para asociar cada entidad a la principal, en este caso para asociar cada pedido a la factura
	protected void associateEntity(Map keyValues) 
		throws ValidationException, // 
		XavaException, ObjectNotFoundException, 
		FinderException, RemoteException
	{
		super.associateEntity(keyValues); // Ejecuta la l�gica est�ndar 
		Pedido order = (Pedido) MapFacade.findEntity("Pedido", keyValues); 
		order.copyDetailsToInvoice(); // Delega el trabajo principal en la entidad 
	}
}