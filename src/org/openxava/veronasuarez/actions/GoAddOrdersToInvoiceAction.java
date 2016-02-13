package org.openxava.veronasuarez.actions; 

import org.openxava.actions.*; // Para usar GoAddElementsToCollectionAction

public class GoAddOrdersToInvoiceAction
extends GoAddElementsToCollectionAction { // Lógica estándar para ir a la lista que permite añadir elementos a la colección
	public void execute() throws Exception {
		super.execute(); // Ejecuta la lógica estándar, la cual muestra un diálogo
		int customerNumber =
		getPreviousView()// getPreviousView() es la vista principal (estamos en un diálogo)
		.getValueInt("cliente.numeroCliente"); // Lee el número de cliente de la factura actual de la vista
		getTab().setBaseCondition( // La condición de la lista de pedidos a añadir
		"${cliente.numeroCliente} = " + customerNumber +
		" and ${entregado} = true and ${factura.oid} is null"
		);
	}
	
	//Por defecto las acciones en la lista de entidades a añadir (los botones 'Add' y 'Cancel') son del controlador estándar de OpenXava AddToCollection.
	//Sobrescribir getNextController() en nuestra acción nos permite definir nuestro propio controlador en su lugar
	public String getNextController() { 
		return "AddOrdersToInvoice"; // El controlador con las acciones disponibles en la lista de pedidos a añadir
	} 
}