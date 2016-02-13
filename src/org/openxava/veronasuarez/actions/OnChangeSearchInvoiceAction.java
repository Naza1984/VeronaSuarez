package org.openxava.veronasuarez.actions; // En el paquete 'actions'

import java.util.*;
import org.openxava.actions.*; // Para usar OnChangeSearchAction
import org.openxava.veronasuarez.model.*;
import org.openxava.model.*;
import org.openxava.view.*;

public class OnChangeSearchInvoiceAction
extends OnChangeSearchAction { // Lógica estándar para buscar una referencia cuando
// los valores clave cambian en la interfaz de usuario (1)
	public void execute() throws Exception {
		super.execute(); // Ejecuta la lógica estándar (2)
		Map keyValues = getView() // getView() aquí es la de la referencia, no la principal(3)
		.getKeyValuesWithValue();
		if (keyValues.isEmpty()) return; // Si la clave está vacía no se ejecuta más lógica
		Factura factura = (Factura) // Buscamos la factura usando la clave tecleada (4)
		MapFacade.findEntity(getView().getModelName(), keyValues);
		View customerView = getView().getRoot().getSubview("cliente"); // (5)
		int customerNumber = customerView.getValueInt("numeroCliente");
		if (customerNumber == 0) { // Si no hay cliente lo llenamos (6)
			customerView.setValue("numeroCliente", factura.getCliente().getNumeroCliente());
			customerView.refresh();
		} else {
		// Si ya hay un cliente verificamos que coincida con el cliente de la factura (7)
			if (customerNumber != factura.getCliente().getNumeroCliente()) {
				addError("invoice_customer_not_match",
				factura.getCliente().getNumeroCliente(), factura, customerNumber);
				getView().clear();
			}
		}
	}
}