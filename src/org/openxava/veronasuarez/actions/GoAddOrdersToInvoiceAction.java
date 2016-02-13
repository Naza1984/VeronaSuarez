package org.openxava.veronasuarez.actions; 

import org.openxava.actions.*; // Para usar GoAddElementsToCollectionAction

public class GoAddOrdersToInvoiceAction
extends GoAddElementsToCollectionAction { // L�gica est�ndar para ir a la lista que permite a�adir elementos a la colecci�n
	public void execute() throws Exception {
		super.execute(); // Ejecuta la l�gica est�ndar, la cual muestra un di�logo
		int customerNumber =
		getPreviousView()// getPreviousView() es la vista principal (estamos en un di�logo)
		.getValueInt("cliente.numeroCliente"); // Lee el n�mero de cliente de la factura actual de la vista
		getTab().setBaseCondition( // La condici�n de la lista de pedidos a a�adir
		"${cliente.numeroCliente} = " + customerNumber +
		" and ${entregado} = true and ${factura.oid} is null"
		);
	}
	
	//Por defecto las acciones en la lista de entidades a a�adir (los botones 'Add' y 'Cancel') son del controlador est�ndar de OpenXava AddToCollection.
	//Sobrescribir getNextController() en nuestra acci�n nos permite definir nuestro propio controlador en su lugar
	public String getNextController() { 
		return "AddOrdersToInvoice"; // El controlador con las acciones disponibles en la lista de pedidos a a�adir
	} 
}