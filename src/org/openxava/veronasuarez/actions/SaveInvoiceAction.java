package org.openxava.veronasuarez.actions;

import org.openxava.actions.*;

public class SaveInvoiceAction
extends SaveAction // Acción estándar de OpenXava para grabar el contenido de la vista
implements IChangeModuleAction // Para navegación entre módulos
{
	
	// Vuelve al módulo que llamó, Pedido en este caso. Pero no escribo "Pedido"
	//Si usas PREVIOUS_MODULE tienes la ventaja de que puedes llamar a este módulo desde varios módulos de la aplicación, y éste sabrá a que módulo volver en cada caso. 
	//Pero más importante todavía es el hecho de que OpenXava usa una pila de llamadas a módulos para poder volver, por tanto si llamas a un módulo que te ha llamado se produce un problema de reentrada.
	public String getNextModule() {
		return PREVIOUS_MODULE;  
	}
	public boolean hasReinitNextModule() {
		return false; // No queremos inicializar el módulo Pedido
	}
}
