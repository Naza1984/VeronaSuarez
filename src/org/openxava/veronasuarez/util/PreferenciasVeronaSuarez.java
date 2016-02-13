package org.openxava.veronasuarez.util; 

/*Clase que leerá valores de configuración en un archivo de propiedades (un archivo plano con pares clave=valor). 
En este caso se leerá el valor por defecto de porcentaje de IVA del arhivo veronasuarez.properties de la carpeta VeronaSuarez/properties

La ventaja de usar esta clase en lugar de leer directamente del archivo de propiedades es que si cambio la forma 
en que se obtienen las preferencias, por ejemplo leyendo de una bbdd, solo tengo que cambiar esta clase en toda la app
*/


import java.io.*;
import java.math.*;
import java.util.*;
import org.apache.commons.logging.*;
import org.openxava.util.*;

public class PreferenciasVeronaSuarez {
	private final static String FILE_PROPERTIES="veronasuarez.properties";
	private static Log log = LogFactory.getLog(PreferenciasVeronaSuarez.class);
	private static Properties properties; // Almacenamos las propiedades aquí
	
	private static Properties getProperties() {
		if (properties == null) { 
			PropertiesReader reader = // PropertiesReader es una clase de OpenXava
			new PropertiesReader(
			PreferenciasVeronaSuarez.class,
			FILE_PROPERTIES);
			try {
				properties = reader.get();
			}
			catch (IOException ex) {
				log.error(
				XavaResources.getString( // Para leer un mensaje i18n
				"properties_file_error",
				FILE_PROPERTIES),
				ex);
				properties = new Properties();
			}
		} 
		
		return properties;
	}
	
	public static BigDecimal getPorcentajeIVADefault() { // El único método público
		return new BigDecimal(
				getProperties().getProperty("porcentajeIVAdefault"));
	}
}