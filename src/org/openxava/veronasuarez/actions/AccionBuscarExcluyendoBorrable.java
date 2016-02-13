package org.openxava.veronasuarez.actions;

/* refino la l�gica de b�squeda: la b�squeda se ha de hacer de la forma convencional, 
 * con la excepci�n de las entidades con una propiedad borrada cuyo valor sea true.
 * */

import java.util.*;

import org.openxava.actions.*;

public class AccionBuscarExcluyendoBorrable
extends SearchExecutingOnChangeAction { // La acci�n est�ndar de OpenXava para buscar
	private boolean esBorrable() { // Pregunta si la entidad tiene una propiedad 'borrada'
		return getView().getMetaModel()
			.containsMetaProperty("borrada");
	}
	
	protected Map getValuesFromView() // Toma los valores visualizados en la vista y se usan como clave al buscar
	throws Exception
	{
		if (!esBorrable()) { // Si no es 'borrable' usamos la l�gica est�ndar
			return super.getValuesFromView();
		} 
		
		Map values = super.getValuesFromView();
		values.put("borrada", false); // Llenamos la propiedad borrada con false
		return values;
	}
	
	protected Map getMemberNames() // Los miembros a leer de la entidad
	throws Exception
	{
		if (!esBorrable()) { // Si no es 'borrable' ejecutamos la l�gica est�ndar
			return super.getMemberNames();
		} 
		
		Map members = super.getMemberNames();
		members.put("borrada", null); // Queremos obtener la propiedad 'borrada', aunque no est� en la vista
		return members; 
	}
	
	protected void setValuesToView(Map values) // Asigna los valores desde la entidad a la vista
	throws Exception 
	{
		if (esBorrable() && (Boolean) values.get("borrada")) { // vale true
			throw new Exception(); // lanzamos la misma excepci�n que OpenXava lanza cuando el objeto no se encuentra
		} 
		else {
			super.setValuesToView(values); // En caso contrario usamos la l�gica est�ndar
		}
	}
}
