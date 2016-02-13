package org.openxava.veronasuarez.actions;

import org.openxava.actions.*; // Para usar ReferenceSearchAction

public class SearchInvoiceFromOrderAction
extends ReferenceSearchAction { // L�gica est�ndar para buscar una referencia
	public void execute() throws Exception {
		super.execute(); // Ejecuta la l�gica est�ndar, la cual muestra un di�logo
		
		int customerNumber =
		getPreviousView()// getPreviousView() es la vista principal (getView() es el di�logo)
		.getValueInt("cliente.numeroCliente"); // Lee de la vista el n�mero
		// de  del pedido actual
		
		if (customerNumber > 0) { // Si hay  los usamos para filtrar
			getTab().setBaseCondition("${cliente.numeroCliente} = " + customerNumber);
		}
	}
}