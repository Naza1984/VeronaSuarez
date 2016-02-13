package org.openxava.veronasuarez.actions; 

import org.openxava.actions.*;
import org.openxava.veronasuarez.model.*;
import org.openxava.jpa.*;
import org.openxava.model.*;

public class CreateInvoiceFromOrderAction
extends ViewBaseAction 
implements IHideActionAction // Para ocultar la acci�n
{
	private boolean hideAction = false; // Para indicar si la acci�n se ocultar�
	
	public void execute() throws Exception {
		Object oid = getView().getValue("oid");
		if (oid == null) { // Si el oid es nulo el pedido actual no se ha grabado todav�a
			addError("impossible_create_invoice_order_not_exist");
			return;
		} 
		
		MapFacade.setValues("Pedido", // Si el pedido existe lo grabo
		getView().getKeyValues(), getView().getValues());
		
		Pedido pedido = XPersistence.getManager().find( // Usamos JPA para obtener la
		Pedido.class, // entidad Pedido visualizada en la vista
		getView().getValue("oid"));
		pedido.createInvoice(); // El trabajo de verdad lo delegamos en la entidad
		getView().refresh(); // Para ver la factura creada en la pesta�a 'Invoice'
		addMessage("invoice_created_from_order", // Mensaje de confirmaci�n
		pedido.getFactura());
		hideAction = true; // Todo ha funciona a la perfecci�n, as� que ocultamos la acci�n
	}
	
	public String getActionToHide() { // La acci�n a ocultar, en este caso ella misma
		return hideAction?"Pedido.createInvoice":null;
	}

}