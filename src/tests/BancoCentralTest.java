package tests;

import clasesBasicas.BancoCentral;
import clasesBasicas.CuentaImpl;

public class BancoCentralTest {

	public static void main(String[] args) 
	{
		BancoCentral porDefecto = new BancoCentral();
		BancoCentral conParametros = new BancoCentral("BANCOCENTRAL",1.5, 10);
		BancoCentral deCopia = new BancoCentral(conParametros);
		
		System.out.println("porDefecto.getTasaInteres(): " + porDefecto.getTasaInteres());
		System.out.println("conParametros.getCoeficienteCaja(): " + conParametros.getCoeficienteCaja());
		
		System.out.println("deCopia.toString(): " + deCopia.toString());
		
		System.out.println("-----------------------------------------------------");
		
		System.out.println("porDefecto.setTasaInteres(2)");
		System.out.println("ANTES -> " + porDefecto.getTasaInteres());
		porDefecto.setTasaInteres(2);
		System.out.println("DESPUES -> " + porDefecto.getTasaInteres());
		
		System.out.println("-----------------------------------------------------");
		
		System.out.println("porDefecto.setCoeficienteCaja(8)");
		System.out.println("ANTES -> " + porDefecto.getCoeficienteCaja());
		porDefecto.setCoeficienteCaja(8);
		System.out.println("DESPUES -> " + porDefecto.getCoeficienteCaja());
		
		System.out.println("-----------------------------------------------------");
		
		CuentaImpl cu_origen = new CuentaImpl(conParametros.getID(), "12XXXXXXX", 15000000);
		CuentaImpl cu_destino = new CuentaImpl(conParametros.getID(), "10XXXXXXX", 15000000);
		
		conParametros.realizarIngreso(cu_origen, cu_destino, 15000000.50);
		
		System.out.println("-----------------------------------------------------");
		
		System.out.println("copia = porDefecto.clone()");
		
		BancoCentral copia = porDefecto.clone();
		
		System.out.println("copia.toString(): " + copia.toString());
	}

}