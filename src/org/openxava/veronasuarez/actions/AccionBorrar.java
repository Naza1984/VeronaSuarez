package org.openxava.veronasuarez.actions; 

//clase genérica para borrar cualquier entidad de la aplicación por medio de una propiedad borrada. 
//Es decir, una acción para marcar como borrada en lugar de borrarla de la base de datos, 


import java.util.*;
import java.util.Map;
import org.openxava.actions.*; // Para usar ViewBaseAction
import org.openxava.model.*;

public class AccionBorrar
extends ViewBaseAction  //tiene una propiedad view que puedes usar desde dentro de execute()Este objeto permite manejar la interfaz de usuario
implements IChainAction { // Encadena con otra acción, indicada en el método getNextAction()
	private String nextAction = null; // Para guardar la siguiente acción a ejecutar

	//método con la lógica a hacer cuando el usuario pulse en el botón o vínculo correspondiente
	public void execute() throws Exception {
		//Chequeo si el usuario pulsa en el botón de borrar cuando no hay un objeto seleccionado 
		//De lo contrario la instrucción para buscar fallará
		if (getView().getKeyValuesWithValue().isEmpty())  // En lugar de getValue(“oid”) usamos el más genérico getKeyValuesWithValue()
		{
			addError("no_delete_not_exists");//No necesito agregar esta key al archivo de mensajes porque ya existía
			return;
		} 		
		
		//Quiero entidades que puedan marcarse como borradas (Deletable) y otras que sean borradas de verdad de la bbdd.
		//La accion AccionBorrar tiene que funcionar para ambos casos.
		//Aprovecho que OpenXava almacena metadatos para todas las entidades y averiguo si la entidad tiene una propiedad deleted
		if (!getView().getMetaModel() // obtengo los metadatos de la entidad actualmente visualizada en la vista
			.containsMetaProperty("borrada")) // ¿Tiene una propiedad borrada?
		{
			//si no la tiene el proceso de borrado no se realiza
			nextAction = "CRUD.delete"; // “CRUD.delete” se ejecutará cuando esta accion finalice
			return;
		}
		
		Map values = new HashMap(); // Los valores a modificar en la entidad
		values.put("borrada", true); // Asignamos true a la propiedad borrada
		
		//MapFacade permite manejar el estado de las entidades usando mapas
		//esto es conveniente ya que View trabaja con mapas
		MapFacade.setValues( // Modifica los valores de la entidad indicada
		getModelName(), // Un método de ViewBaseAction
		getView().getKeyValues(), // La clave de la entidad a modificar
		values); // Los valores a cambiar
		resetDescriptionsCache(); // borra los caches para los combos
		addMessage("object_deleted", getModelName());
		getView().clear();
		getView().setEditable(false); // Dejamos la vista como no editable para impedir que el usuario rellene datos en la vista
	}
	
	public String getNextAction() // Obligatorio por causa de IChainAction
	throws Exception
	{
		return nextAction; // Si es nulo no se encadena con ninguna acción
	}
}
