package org.openxava.veronasuarez.actions;

import org.openxava.actions.*; // Para usar ReferenceSearchAction

public class SearchInvoiceFromOrderAction
extends ReferenceSearchAction { // Lógica estándar para buscar una referencia
	public void execute() throws Exception {
		super.execute(); // Ejecuta la lógica estándar, la cual muestra un diálogo
		
		int customerNumber =
		getPreviousView()// getPreviousView() es la vista principal (getView() es el diálogo)
		.getValueInt("cliente.numeroCliente"); // Lee de la vista el número
		// de  del pedido actual
		
		if (customerNumber > 0) { // Si hay  los usamos para filtrar
			getTab().setBaseCondition("${cliente.numeroCliente} = " + customerNumber);
		}
	}
}