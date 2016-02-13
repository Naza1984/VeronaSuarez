package org.openxava.veronasuarez.actions;

import java.util.*;

import javax.ejb.*;
import javax.inject.*;

import org.openxava.actions.*;
import org.openxava.veronasuarez.model.*;
import org.openxava.model.*;

/*
Despu�s de crear la factura a partir de varios pedidos, cambiamos a un nuevo m�dulo para poder editarla.
*/

public class CreateInvoiceFromSelectedOrdersAction
extends TabBaseAction // T�pico para acciones de lista. Permite usar getTab() 
implements IChangeModuleAction // Para cambiar a otro m�dulo despu�s de la ejecuci�n
{
	
	//Antes de llamar al m�todo execute() de la acci�n, el objeto veronasuarez_currentInvoiceKey definido en controllers.xml se inyecta en el campo currentInvoiceKey de la acci�n.
	//Lo interesante de @Inject es que, adem�s de inyectar el objeto en el campo antes de llamar a execute(), extrae el valor del campo y lo vuelve a poner en el contexto de la sesi�n despu�s de ejecutar el m�todo execute().
	@Inject
	private Map currentInvoiceKey; // Un campo privado sin getter ni setter
	
	public void execute() throws Exception {
		Collection<Pedido> pedidos = getSelectedOrders(); // Obtenemos la lista de los pedido marcados en la lista
		Factura factura = Factura.createFromOrders(pedidos); // llamamos al m�todo createFromOrders() de Factura
		addMessage("invoice_created_from_orders", factura, pedidos);//mostramosun mensaje
		// Pone la clave de la reci�n creada
		// factura en el campo currentInvoiceKey, por lo tanto tambi�n
		// en el objeto de sesi�n invoicing_ currentInvoiceKey
		currentInvoiceKey = toKey(factura);
	}
	
	private Map toKey(Factura factura) { // Extrae la clave de la factura en formato mapa
		Map key = new HashMap();
		key.put("oid",factura.getOid());
		return key;
	}
	
	private Collection<Pedido> getSelectedOrders() //devuelve una colecci�n con las entidades Pedido marcadas por el usuario en la lista
	throws FinderException
	{
		Collection<Pedido> result = new ArrayList<Pedido>();
		for (Map key: getTab().getSelectedKeys()) { //devuelve un objeto org.openxava.tab.Tab. El objeto Tab te permite manejar los datos tabulares de la lista.
			Pedido pedido = (Pedido)
			MapFacade.findEntity("Pedido", key); //Dado que esas claves est�n en formato Map usamos aca las convertimos en entidades Pedido.
			result.add(pedido);
		} return result;
	}
	
	public String getNextModule() {
		return "CurrentInvoiceEdition"; // Nombre de m�dulo como est� definido en application.xml
	} 
	
	public boolean hasReinitNextModule() {
		return true; // As� el m�dulo se inicializa cada vez que cambiamos a �l
	}
}
