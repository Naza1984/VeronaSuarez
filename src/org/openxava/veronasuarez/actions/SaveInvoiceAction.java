package org.openxava.veronasuarez.actions;

import org.openxava.actions.*;

public class SaveInvoiceAction
extends SaveAction // Acci�n est�ndar de OpenXava para grabar el contenido de la vista
implements IChangeModuleAction // Para navegaci�n entre m�dulos
{
	
	// Vuelve al m�dulo que llam�, Pedido en este caso. Pero no escribo "Pedido"
	//Si usas PREVIOUS_MODULE tienes la ventaja de que puedes llamar a este m�dulo desde varios m�dulos de la aplicaci�n, y �ste sabr� a que m�dulo volver en cada caso. 
	//Pero m�s importante todav�a es el hecho de que OpenXava usa una pila de llamadas a m�dulos para poder volver, por tanto si llamas a un m�dulo que te ha llamado se produce un problema de reentrada.
	public String getNextModule() {
		return PREVIOUS_MODULE;  
	}
	public boolean hasReinitNextModule() {
		return false; // No queremos inicializar el m�dulo Pedido
	}
}
