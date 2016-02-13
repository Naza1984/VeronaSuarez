package org.openxava.veronasuarez.actions;

import java.util.*;

import javax.inject.*;

import org.openxava.actions.*;


/*
llamamos al m�dulo CurrentInvoiceEdition devolviendo true para hasReinitNextModule(), as� CurrentInvoiceEdition se inicializa cada vez
que se llama desde el m�dulo Pedido, por ende la acci�n load se llama siempre.
Esta acci�n load es el lugar ideal para rellenar la vista con la factura reci�n creada.
*/
public class LoadCurrentInvoiceAction
extends SearchByViewKeyAction { // Para llenar la vista a partir de la clave
	@Inject
	private Map currentInvoiceKey; // Para coger el valor del objeto de sesi�n
	
	public void execute() throws Exception {
		getView().setValues(currentInvoiceKey); // Pone la clave en la vista
		super.execute(); // Llena toda la vista a partir de los campos clave
	}
}
