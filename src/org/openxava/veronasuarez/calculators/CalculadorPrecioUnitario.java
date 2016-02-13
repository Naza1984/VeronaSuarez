package org.openxava.veronasuarez.calculators; 

import org.openxava.calculators.*;
import org.openxava.veronasuarez.model.*;
import static org.openxava.jpa.XPersistence.*; // Para usar getManager()

public class CalculadorPrecioUnitario implements ICalculator {
	private int numeroProducto; // En calculate() contendrá el número de producto
	
	public Object calculate() throws Exception {
		Producto product = getManager() // getManager() de XPersistence
				.find(Producto.class, numeroProducto); // Busca el producto
		return product.getPrecio(); // devuelve el precio
	}

	public int getNumeroProducto() {
		return numeroProducto;
	}

	public void setNumeroProducto(int numeroProducto) {
		this.numeroProducto = numeroProducto;
	}
	

}