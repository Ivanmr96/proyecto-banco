package utilidades;

import clasesBasicas.TransferenciaImpl;

import java.io.*;
import java.util.*;

public class Utilidades {

    /*
     *Signatura: public void imprimirMovimientos(TransferenciaImpl movimientos)
     * Comentario: imprime un movimiento. M�todo sobrecargado
     * */
    public void imprimirMovimientos(TransferenciaImpl element){

        String tipo = " ";
            System.out.println("-------------------------------");
            System.out.println("Fecha: " + element.toStringFecha());
            System.out.println("Tipo: " + ((element.isIngresoOrRetirada())?"INGRESO":"RETIRADA"));
            System.out.println("Cantidad: " + element.getCantidad());
            System.out.println("Concepto: " + element.getConcepto());

    }

    /*
    *Signatura: public void imprimirMovimientos(List<String> movimientos)
    * Comentario: imprime una lista de Strings en pantalla
    * */
    public void imprimirMovimientos(List<TransferenciaImpl> movimientos){

        String tipo = " ";
        for(TransferenciaImpl element:movimientos)
        {
            System.out.println("-------------------------------");
            System.out.println("Fecha: " + element.toStringFecha());
            System.out.println("Tipo: " + ((element.isIngresoOrRetirada())?"INGRESO":"RETIRADA"));
            System.out.println("Cantidad: " + element.getCantidad());
            System.out.println("Concepto: " + element.getConcepto());
        }
    }
    
    public void imprimirDatosCuenta(String datosCuenta)
    {
    	String[] campos = datosCuenta.split(",");
    	
    	System.out.println("Numero de cuenta: " + campos[0]);
    	System.out.println("Saldo: " + campos[1]);
    }

    /*
    * INTERFAZ
    * Signatura: public void ordenarFicheroPorClave(String ruta, int campoClave)
    * Comentario: ordena el fichero de una ruta en base a la lexicograf�a de un campo de un fichero
    * Precondiciones:
    * Entradas: Se pasar� como String la ruta del fichero a ordenar y como entero el campo clave que se evaluar� para ordenar
    * Salidas:
    * Postcondiciones: El fichero quedar� ordenado en base a ese campo lexicogr�ficamente.
    * */
    public void ordenarFicheroPorClave(String ruta, int campoClave){
        File ficheroAOrdenar = new File(ruta);
        FileReader fr = null;
        FileWriter fw = null;
        BufferedReader br = null;
        BufferedWriter bw = null;
        List<String> registros = new ArrayList<String>();    //arraylist - considerar cambiar a array
        String registro = " ";
        String aux=" "; //para el bubblesort de más abajo

        try{
            fr = new FileReader(ficheroAOrdenar);
            br = new BufferedReader(fr);
            while(br.ready()){
                registro = br.readLine();
                registros.add(registro);
            }
            br.close();
            fr.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        //ordenamiento burbuja

        for (int i = 0; i < registros.size()-1;i++){

            for (int j = registros.size()-1; j>i; j--){
                if (registros.get(j).compareTo(registros.get(j-1)) < 0) {
                    //se produce el intercambio de elementos
                    aux = registros.get(j);
                    registros.set(j,registros.get(j-1));
                    registros.set(j-1, aux);
                }
            }
        }
        try{
            fw = new FileWriter(ficheroAOrdenar);
            bw = new BufferedWriter(fw);

            for(int i = 0; i < registros.size(); i ++){
                bw.write(registros.get(i));
                bw.newLine();
            }
            bw.close();
            fw.close();
        }catch ( IOException e ){
            e.printStackTrace();
        }



    }

}
