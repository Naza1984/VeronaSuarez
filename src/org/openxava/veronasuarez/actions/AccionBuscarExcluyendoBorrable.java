package org.openxava.veronasuarez.actions;

/* refino la lógica de búsqueda: la búsqueda se ha de hacer de la forma convencional, 
 * con la excepción de las entidades con una propiedad borrada cuyo valor sea true.
 * */

import java.util.*;

import org.openxava.actions.*;

public class AccionBuscarExcluyendoBorrable
extends SearchExecutingOnChangeAction { // La acción estándar de OpenXava para buscar
	private boolean esBorrable() { // Pregunta si la entidad tiene una propiedad 'borrada'
		return getView().getMetaModel()
			.containsMetaProperty("borrada");
	}
	
	protected Map getValuesFromView() // Toma los valores visualizados en la vista y se usan como clave al buscar
	throws Exception
	{
		if (!esBorrable()) { // Si no es 'borrable' usamos la lógica estándar
			return super.getValuesFromView();
		} 
		
		Map values = super.getValuesFromView();
		values.put("borrada", false); // Llenamos la propiedad borrada con false
		return values;
	}
	
	protected Map getMemberNames() // Los miembros a leer de la entidad
	throws Exception
	{
		if (!esBorrable()) { // Si no es 'borrable' ejecutamos la lógica estándar
			return super.getMemberNames();
		} 
		
		Map members = super.getMemberNames();
		members.put("borrada", null); // Queremos obtener la propiedad 'borrada', aunque no esté en la vista
		return members; 
	}
	
	protected void setValuesToView(Map values) // Asigna los valores desde la entidad a la vista
	throws Exception 
	{
		if (esBorrable() && (Boolean) values.get("borrada")) { // vale true
			throw new Exception(); // lanzamos la misma excepción que OpenXava lanza cuando el objeto no se encuentra
		} 
		else {
			super.setValuesToView(values); // En caso contrario usamos la lógica estándar
		}
	}
}
