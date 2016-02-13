package org.openxava.veronasuarez.actions; 

import org.openxava.actions.*; // Necesario para usar OnChangePropertyAction


/*
OpenXava permite ejecutar una acción cuando cierta propiedad sea cambiada por el usuario en la interfaz de usuario. 
Podemos mostrar el botón sólo cuando la acción esté lista para ser usada.
Una factura puede ser generada desde un pedido si el pedido ha sido entregado y no tiene factura todavía. 
Por tanto, tenemos que vigilar los cambios en la referencia factura y la propiedad entregado de la entidad Pedido.

Cuando marcas o desmarcas la casilla entregado o escoges una factura, el botón para la acción se muestra u oculta. 
También, cuando el usuario pulsa en 'Nuevo' para crear un nuevo pedido el botón para crear la factura se oculta. 
*/

public class ShowHideCreateInvoiceAction extends OnChangePropertyBaseAction // Necesario para acciones @OnChange
	implements IShowActionAction, // Para mostrar una acción
				IHideActionAction { // Para ocultar una acción
	private boolean show; // Si true la acción 'Pedido.createInvoice' se mostrará
	
	//El método execute() pone a true el campo show si la orden visualizada está grabada, entregada y no tiene factura.
	//Así, oculto o muestro la acción Pedido.createInvoice, mostrándola solo cuando proceda.
	public void execute() throws Exception {
		show = isOrderCreated() // Seteo el valor de 'show'. Este valor se usará en getActionToShow() y getActionToHide()
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
		return show?"Pedido.createInvoice":""; // La acción a mostrar 
	}
	
	public String getActionToHide() { // Obligatorio por causa de IHideActionAction
		return !show?"Pedido.createInvoice":""; // La acción a ocultar 
	}
}