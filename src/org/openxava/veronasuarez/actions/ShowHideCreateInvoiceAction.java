package org.openxava.veronasuarez.actions; 

import org.openxava.actions.*; // Necesario para usar OnChangePropertyAction


/*
OpenXava permite ejecutar una acci�n cuando cierta propiedad sea cambiada por el usuario en la interfaz de usuario. 
Podemos mostrar el bot�n s�lo cuando la acci�n est� lista para ser usada.
Una factura puede ser generada desde un pedido si el pedido ha sido entregado y no tiene factura todav�a. 
Por tanto, tenemos que vigilar los cambios en la referencia factura y la propiedad entregado de la entidad Pedido.

Cuando marcas o desmarcas la casilla entregado o escoges una factura, el bot�n para la acci�n se muestra u oculta. 
Tambi�n, cuando el usuario pulsa en 'Nuevo' para crear un nuevo pedido el bot�n para crear la factura se oculta. 
*/

public class ShowHideCreateInvoiceAction extends OnChangePropertyBaseAction // Necesario para acciones @OnChange
	implements IShowActionAction, // Para mostrar una acci�n
				IHideActionAction { // Para ocultar una acci�n
	private boolean show; // Si true la acci�n 'Pedido.createInvoice' se mostrar�
	
	//El m�todo execute() pone a true el campo show si la orden visualizada est� grabada, entregada y no tiene factura.
	//As�, oculto o muestro la acci�n Pedido.createInvoice, mostr�ndola solo cuando proceda.
	public void execute() throws Exception {
		show = isOrderCreated() // Seteo el valor de 'show'. Este valor se usar� en getActionToShow() y getActionToHide()
		&& estaEntregado() 
		&& !hasInvoice(); 
	}
	
	private boolean isOrderCreated() {
		return getView().getValue("oid") != null; // Leemos el valor desde la vista
	}
	
	private boolean estaEntregado() {
		Boolean entregado = (Boolean)
		getView().getValue("entregado"); // Leemos el valor desde la vista
		return entregado == null?false:entregado;
	}
	
	private boolean hasInvoice() {
		return getView().getValue("factura.oid") != null; // Leemos el valor
	} // desde la vista
	
	public String getActionToShow() { // Obligatorio por causa de IShowActionAction
		return show?"Pedido.createInvoice":""; // La acci�n a mostrar 
	}
	
	public String getActionToHide() { // Obligatorio por causa de IHideActionAction
		return !show?"Pedido.createInvoice":""; // La acci�n a ocultar 
	}
}