package org.openxava.veronasuarez.calculators;

/* Uso la clase de lectura del archivo de propiedades para calcular el porcentaje de IVA por default */

import org.openxava.calculators.*; // Para usar ICalculator
import org.openxava.veronasuarez.util.*; // Para usar PreferenciasVeronaSuarez

public class CalculadorPorcentajeIVA implements ICalculator {

	public Object calculate() throws Exception {
		return PreferenciasVeronaSuarez.getPorcentajeIVADefault();
	}
}