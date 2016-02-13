package org.openxava.veronasuarez.actions;

import java.util.*;

import javax.inject.*;

import org.openxava.actions.*;


/*
llamamos al módulo CurrentInvoiceEdition devolviendo true para hasReinitNextModule(), así CurrentInvoiceEdition se inicializa cada vez
que se llama desde el módulo Pedido, por ende la acción load se llama siempre.
Esta acción load es el lugar ideal para rellenar la vista con la factura recién creada.
*/
public class LoadCurrentInvoiceAction
extends SearchByViewKeyAction { // Para llenar la vista a partir de la clave
	@Inject
	private Map currentInvoiceKey; // Para coger el valor del objeto de sesión
	
	public void execute() throws Exception {
		getView().setValues(currentInvoiceKey); // Pone la clave en la vista
		super.execute(); // Llena toda la vista a partir de los campos clave
	}
}
