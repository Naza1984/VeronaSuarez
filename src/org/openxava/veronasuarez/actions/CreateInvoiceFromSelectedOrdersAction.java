package org.openxava.veronasuarez.actions;

import java.util.*;

import javax.ejb.*;
import javax.inject.*;

import org.openxava.actions.*;
import org.openxava.veronasuarez.model.*;
import org.openxava.model.*;

/*
Después de crear la factura a partir de varios pedidos, cambiamos a un nuevo módulo para poder editarla.
*/

public class CreateInvoiceFromSelectedOrdersAction
extends TabBaseAction // Típico para acciones de lista. Permite usar getTab() 
implements IChangeModuleAction // Para cambiar a otro módulo después de la ejecución
{
	
	//Antes de llamar al método execute() de la acción, el objeto veronasuarez_currentInvoiceKey definido en controllers.xml se inyecta en el campo currentInvoiceKey de la acción.
	//Lo interesante de @Inject es que, además de inyectar el objeto en el campo antes de llamar a execute(), extrae el valor del campo y lo vuelve a poner en el contexto de la sesión después de ejecutar el método execute().
	@Inject
	private Map currentInvoiceKey; // Un campo privado sin getter ni setter
	
	public void execute() throws Exception {
		Collection<Pedido> pedidos = getSelectedOrders(); // Obtenemos la lista de los pedido marcados en la lista
		Factura factura = Factura.createFromOrders(pedidos); // llamamos al método createFromOrders() de Factura
		addMessage("invoice_created_from_orders", factura, pedidos);//mostramosun mensaje
		// Pone la clave de la recién creada
		// factura en el campo currentInvoiceKey, por lo tanto también
		// en el objeto de sesión invoicing_ currentInvoiceKey
		currentInvoiceKey = toKey(factura);
	}
	
	private Map toKey(Factura factura) { // Extrae la clave de la factura en formato mapa
		Map key = new HashMap();
		key.put("oid",factura.getOid());
		return key;
	}
	
	private Collection<Pedido> getSelectedOrders() //devuelve una colección con las entidades Pedido marcadas por el usuario en la lista
	throws FinderException
	{
		Collection<Pedido> result = new ArrayList<Pedido>();
		for (Map key: getTab().getSelectedKeys()) { //devuelve un objeto org.openxava.tab.Tab. El objeto Tab te permite manejar los datos tabulares de la lista.
			Pedido pedido = (Pedido)
			MapFacade.findEntity("Pedido", key); //Dado que esas claves están en formato Map usamos aca las convertimos en entidades Pedido.
			result.add(pedido);
		} return result;
	}
	
	public String getNextModule() {
		return "CurrentInvoiceEdition"; // Nombre de módulo como está definido en application.xml
	} 
	
	public boolean hasReinitNextModule() {
		return true; // Así el módulo se inicializa cada vez que cambiamos a él
	}
}
