package org.openxava.veronasuarez.actions;

import java.util.*;

import org.openxava.actions.*;
import org.openxava.model.*;
import org.openxava.model.meta.*;
import org.openxava.validators.*;

public class AccionBorrarSeleccionado extends TabBaseAction // Para trabajar con datos tabulares (lista) por medio de getTab()
implements IChainAction { // Encadena con otra acción, indicada con getNextAction()
	private String nextAction = null; // Para almacenar la siguiente acción a ejecutar
	
	//lógica a ejecutar cuando el usuario pulse en el botón o vínculo correspondiente
	public void execute() throws Exception {
		if (!getMetaModel().containsMetaProperty("borrada")) {
			nextAction="CRUD.deleteSelected"; // “CRUD.deleteSelected” se ejecutará cuando esta acción finalice
			return;
		} 
		
		markSelectedEntitiesAsDeleted(); // La lógica para marcar las filas seleccionadas como objetos borrados
	} 
	
	private boolean restaurar; // Una nueva propiedad restaurar…
	
	public boolean estaRestaurada() { // ...con su getter
		return restaurar;
	}
	
	public void setRestaurar(boolean restaurar) { // ...y su setter
		this.restaurar = restaurar;
	}
	
	private MetaModel getMetaModel() {
		return MetaModel.get(getTab().getModelName());
	}
	
	public String getNextAction() // Obligatorio por causa de IChainAction
	throws Exception
	{
		return nextAction; // Si es nulo no se encadena con ninguna acción
	}
	
	//Si la entidad tiene un propiedad deleted entonces se ejecuta nuestra propia lógica de borrado
	private void markSelectedEntitiesAsDeleted() throws Exception {
		Map values = new HashMap(); // Valores a asignar a cada entidad para marcarla
		values.put("borrada", !estaRestaurada()); // el valor de la propiedad restaurar
		
		for (int row: getSelected()) { // Itera sobre todas las filas seleccionadas
			Map key = (Map) getTab().getTableModel().getObjectAt(row);
			try { // seleccionadas. Obtenemos la clave de cada entidad
				MapFacade.setValues( // Modificamos cada entidad
				getTab().getModelName(),
				key,
				values);
			} catch (
			ValidationException ex) {
				// Si se produce una ValidationException..
				addError("no_delete_row", row + 1, key);
				addErrors(ex.getErrors()); // ...mostramos los mensajes
			} catch (
				Exception ex) {
				// Si se lanza cualquier otra excepción, se añade
				addError("no_delete_row", row + 1, key); // un mensaje genérico
			}
		} 
		
		getTab().deselectAll(); // Después de borrar deseleccionamos la filas
		resetDescriptionsCache(); // Y reiniciamos el caché de los combos para este usuario
	}
}