/*
*  Voy a codificar aquÃ­ los mÃ©todos que he puesto en "ResguardoGestionBancoComercial"
*   a pesar de que creo que irÃ­an en la clase bÃ¡sica "BancoComercial" pero por tenerlos separados.
* Si luego vemos que son de BancoComercial, los cambio a allÃ­.
* */
package gestion;
import java.io.*;

import java.text.SimpleDateFormat;
import java.util.*;

import clasesBasicas.ClienteImpl;
import clasesBasicas.CuentaImpl;
import clasesBasicas.TransferenciaImpl;
import utilidades.MyObjectOutputStream;

public class GestionBancoComercial {

    /*
     * INTERFAZ
     * Signatura: public boolean isCuentaBorrada(String iban)
     * Comentario: Comprueba si el iban pertenece a una cuenta del fichero CuentasBorradas
     * Precondiciones: Se pasa un iban
     * Entrada: String iban
     * Salida: boolean
     * Entrada/Salida:
     * Postcondiciones: asociado al nombre devuelve true si el iban corresponde a una cuenta del fichero CuentasBorradas y false si no
     * */
    /*
    public boolean isCuentaBorrada(String iban){
        File f_cuentasBorradas = new File("./Files/BancosComerciales/"+obtenerNombreBancoComercialPorIBAN(iban)+"/CuentasBorradas_"+obtenerNombreBancoComercialPorIBAN(iban)+".txt");
        FileReader fr = null;
        BufferedReader br = null;
        boolean isBorrada = false;

        try{
            fr = new FileReader(f_cuentasBorradas);
            br = new BufferedReader(fr);

            while (br.ready()){
                if(br.readLine().equals(iban)){
                    isBorrada = true;
                }
            }
            br.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return isBorrada;
    }
*/
    /*
     * INTERFAZ
     * Signatura: public void marcarCuentaComoBorrada(String iban_cuenta)
     * Comentario: Escribe en el fichero CuentasBorradas la cuenta indicada
     * Precondiciones: Se pasa un iban
     * Entrada: String iban
     * Salida:
     * Entrada/Salida:
     * Postcondiciones: modifica el fichero de cuentas borradas
     * */
    public boolean marcarCuentaComoBorrada(String iban_cuenta){
        File f_cuentasBorradas = new File("./Files/BancosComerciales/"+obtenerNombreBancoComercialPorIBAN(iban_cuenta)+"/CuentasBorradas_"+obtenerNombreBancoComercialPorIBAN(iban_cuenta)+".txt");
        FileWriter fw = null;
        BufferedWriter bw = null;
        boolean borrada = false;

        try{
            fw = new FileWriter(f_cuentasBorradas,true);
            bw = new BufferedWriter(fw);

            bw.write(iban_cuenta);
            bw.newLine();
            borrada = true;

            bw.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        
        return borrada;
    }

    /*
     * INTERFAZ
     * Signatura: public void eliminarCuentasBorradasDefinitivamente(String bic)
     * Comentario: Elimina definitivamente el rastro de las cuentas marcadas como borradas en el fichero CuentasBorradas.
     *              (borra la cuenta del fichero de cuentas, borra su historial de movimientos, borra al cliente ya que de momento cada cliente solo puede tener una cuenta, borra del fichero cuentas-clientes...)
     * Precondiciones: Se pasa el BIC del banco
     * Entrada: String bic
     * Salida:
     * Entrada/Salida:
     * Postcondiciones: modifica varios ficheros
     * */
    //TODO Revisar entero
    public boolean eliminarCuentasBorradasDefinitivamente(String bic) {
        File carpetaBanco = new File("./Files/BancosComerciales/" + obtenerNombrePorBIC(bic));
        File cuentas = new File(carpetaBanco, "Cuentas_" + obtenerNombrePorBIC(bic) + ".txt");
        File clientes = new File(carpetaBanco, "Clientes_" + obtenerNombrePorBIC(bic) + ".txt");
        File clientes_cuentas = new File(carpetaBanco, "Clientes_Cuentas_" + obtenerNombrePorBIC(bic) + ".txt");
        File cuentasBorradas = new File(carpetaBanco, "CuentasBorradas_" + obtenerNombrePorBIC(bic) + ".txt");
        File carpetaMovimientos = new File(carpetaBanco, "Movimientos");
        File archivoMovimientosCuenta[] = carpetaMovimientos.listFiles();
        List<String> cuentasABorrar = new ArrayList<String>();
        List<String> registrosMantenidos = new ArrayList<String>();
        FileReader fr = null;
        BufferedReader br = null;
        FileWriter fw = null;
        BufferedWriter bw = null;
        String registro = " ";
        String dniABorrar = " ";
        boolean archivoMovimientosBorrado = false;
        boolean borrada = false;
        boolean clienteBorrado = false;
        boolean cuentaBorrada = false;

        if(carpetaBanco.exists() && cuentas.exists() && clientes.exists() && clientes_cuentas.exists() && cuentasBorradas.exists() && carpetaMovimientos.exists()){
        try {
            fr = new FileReader(cuentasBorradas);
            br = new BufferedReader(fr);
            while (br.ready()) {
                registro = br.readLine();
                cuentasABorrar.add(registro);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < cuentasABorrar.size(); i++) {
            /*Comprobación y eliminación del fichero de Cuentas*/
            try {
                fr = new FileReader(cuentas);
                br = new BufferedReader(fr);
                while (br.ready()) {
                    registro = br.readLine();

                    if (!cuentasABorrar.get(i).equals(registro.split(",")[0])) {
                        registrosMantenidos.add(registro);
                    }
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fw = new FileWriter(cuentas);
                bw = new BufferedWriter(fw);
                for (String element : registrosMantenidos) {
                    bw.write(element);
                    bw.newLine();
                }
                cuentaBorrada = true;
                bw.close();
                registrosMantenidos.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }

            /*Comprobación y eliminación del fichero de Clientes_Cuentas_ */
            try {
                fr = new FileReader(clientes_cuentas);
                br = new BufferedReader(fr);
                while (br.ready()) {
                    registro = br.readLine();

                    if (!cuentasABorrar.get(i).equals(registro.split(",")[1])) {
                        registrosMantenidos.add(registro);
                    } else {
                        dniABorrar = registro.split(",")[0];
                    }
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fw = new FileWriter(clientes_cuentas);
                bw = new BufferedWriter(fw);
                for (String element : registrosMantenidos) {
                    bw.write(element);
                    bw.newLine();
                }
                bw.close();
                registrosMantenidos.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }

            /*Comprobación y eliminación del fichero de Clientes */
            try {
                fr = new FileReader(clientes);
                br = new BufferedReader(fr);
                while (br.ready()) {
                    registro = br.readLine();

                    if (!dniABorrar.equals(registro.split(",")[1])) {
                        registrosMantenidos.add(registro);
                    }
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fw = new FileWriter(clientes);
                bw = new BufferedWriter(fw);
                for (String element : registrosMantenidos) {
                    bw.write(element);
                    bw.newLine();
                }
                clienteBorrado = true;
                bw.close();
                registrosMantenidos.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }

            /*Borrar los ficheros de movimientos de las cuentas borradas*/
            for (int j = 0; j < archivoMovimientosCuenta.length; j++) {
                if (archivoMovimientosCuenta[j].getName().split("_")[2].equals(cuentasABorrar.get(i)+".txt")) {
                	archivoMovimientosBorrado = archivoMovimientosCuenta[j].delete();
                }
            }

        }

        /*Eliminar el contenido del fichero de cuentasBorradas*/
        try {
            fw = new FileWriter(cuentasBorradas);
            bw = new BufferedWriter(fw);
            bw.write("");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        }//cierra el if
        
        if(archivoMovimientosBorrado && clienteBorrado && cuentaBorrada)
        	borrada = true;
        
        return borrada;
    }



    /*
     * INTERFAZ
     * Signatura: public ArrayList<String> buscarMovimientosPorFecha(String iban_cuenta, int anyo_buscado)
     * Comentario: busca los movimientos que se hicieron en una cuenta en la fecha dada
     * Precondiciones: Se pasa un iban y un int
     * Entrada: String iban, int anyo_buscado
     * Salida: arraylist de cadenas con el / los movimientos requeridos
     * Entrada/Salida:
     * Postcondiciones: asociado al nombre devuelve un arraylist
     * */
    public List<TransferenciaImpl> buscarMovimientosPorFecha(String IBAN, int anyo){
        File file_movimientos = new File("./Files/BancosComerciales/"+obtenerNombrePorBIC(obtenerBICporIBAN(IBAN))+"/Transferencias/Transferencias_Cuenta_"+IBAN+".dat");
        ObjectInputStream leer = null;
        List<TransferenciaImpl> registros_buscados = new ArrayList<TransferenciaImpl>();
        TransferenciaImpl registro = null;
        boolean cont = true;


        try {
            leer = new ObjectInputStream(new FileInputStream(file_movimientos));
            while (cont) {
                registro = (TransferenciaImpl) leer.readObject();
                if (registro.getFecha().get(Calendar.YEAR) == anyo) {
                    registros_buscados.add(registro);
                }
            }
            leer.close();
        }catch (EOFException e){

        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        return registros_buscados;
    }

    /*
     * INTERFAZ
     * Signatura: public ArrayList<String> buscarMovimientosPorFecha(String iban_cuenta, int mes_buscado, int anyo_buscado)
     * Comentario: busca los movimientos que se hicieron en una cuenta en la fecha dada
     * Precondiciones: Se pasa un iban y dos int
     * Entrada: String iban, int mes_buscado, int anyo_buscado
     * Salida: arraylist de cadenas con el / los movimientos requeridos
     * Entrada/Salida:
     * Postcondiciones: asociado al nombre devuelve un arraylist
     * */
    public List<TransferenciaImpl> buscarMovimientosPorFecha(String IBAN, int mes, int anyo){
        File file_movimientos = new File("./Files/BancosComerciales/"+obtenerNombrePorBIC(obtenerBICporIBAN(IBAN))+"/Transferencias/Transferencias_Cuenta_"+IBAN+".dat");
        ObjectInputStream leer = null;
        List<TransferenciaImpl> registros_buscados = new ArrayList<TransferenciaImpl>();
        TransferenciaImpl registro= null;
        boolean cont = true;


        try{
            leer = new ObjectInputStream(new FileInputStream(file_movimientos));
            while (cont){
                registro = (TransferenciaImpl) leer.readObject();
                if (registro.getFecha().get(Calendar.YEAR) ==  anyo && registro.getFecha().get(Calendar.MONTH) == mes ){
                    registros_buscados.add(registro);
                }
            }
            leer.close();
        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        return registros_buscados;
    }

    /*
     * INTERFAZ
     * Signatura: public ArrayList<String> buscarMovimientosPorFecha(String iban_cuenta,int dia_buscado, int mes_buscado, int anyo_buscado)
     * Comentario: busca los movimientos que se hicieron en una cuenta en la fecha dada
     * Precondiciones: Se pasa un iban y tres int
     * Entrada: String iban,int dia_buscado, int mes_buscado, int anyo_buscado
     * Salida: arraylist de cadenas con el / los movimientos requeridos
     * Entrada/Salida:
     * Postcondiciones: asociado al nombre devuelve un arraylist
     * */
    public List<TransferenciaImpl> buscarMovimientosPorFecha(String IBAN, int dia,int mes,int anyo){
        File file_movimientos = new File("./Files/BancosComerciales/"+obtenerNombrePorBIC(obtenerBICporIBAN(IBAN))+"/Transferencias/Transferencias_Cuenta_"+IBAN+".dat");
        List<TransferenciaImpl> registros_buscados = new ArrayList<TransferenciaImpl>();
        TransferenciaImpl registro = null;
        ObjectInputStream leer = null;
        boolean cont = true;
        try{
            leer = new ObjectInputStream(new FileInputStream(file_movimientos));
            while (cont){
                registro = (TransferenciaImpl) leer.readObject();
                if (registro.getFecha().get(Calendar.YEAR) ==  anyo && registro.getFecha().get(Calendar.MONTH) == mes && registro.getFecha().get(Calendar.DAY_OF_MONTH) == dia ){
                    registros_buscados.add(registro);
                }
            }
            leer.close();
        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        return registros_buscados;
    }

    /*
     * INTERFAZ
     * Signatura: public List<String> ultimosDiezMovimientos(String iban_cuenta)
     * Comentario: devuelve los ultimos diez movimientos de la cuenta
     * Precondiciones: Se pasa un iban
     * Entrada: String iban
     * Salida: una lista de String
     * Entrada/Salida:
     * Postcondiciones: asociado al nombre devuelve una lista de String
     * */
    public List<TransferenciaImpl> ultimosDiezMovimientos(String iban_cuenta){

        File f_cuentas = new File("./Files/BancosComerciales/"+obtenerNombrePorBIC(obtenerBICporIBAN(iban_cuenta))+"/Transferencias/Transferencias_Cuenta_" + iban_cuenta + ".dat");
        ObjectInputStream leer = null;
        List<TransferenciaImpl> registros = new ArrayList<TransferenciaImpl>();
        TransferenciaImpl registro = null;
        int lineas=0;
        boolean cont = true;


        if (f_cuentas.exists()){
            try {
                leer = new ObjectInputStream(new FileInputStream(f_cuentas));
                while (((registro = (TransferenciaImpl) leer.readObject()) != null) && lineas < 10) {
                    registros.add(registro);
                    lineas++;
                }
            }catch (EOFException e){

            } catch (IOException e) {
                e.printStackTrace();
            }catch (ClassNotFoundException e){
                e.printStackTrace();
            }
        }
        return registros;
    }

    /*
     * INTERFAZ
     * Signatura: public boolean isDNIvalido(String nombre_banco,String dni_cliente)
     * Comentario: Dado un DNI, devuelve true si este pertenece a un cliente existente del banco y false si no
     * Precondiciones: Se pasa el nombre del banco y el dni del cliente
     * Entrada: String nombre_banco y String dni_cliente
     * Salida: boolean
     * Entrada/Salida:
     * Postcondiciones: asociado al nombre se devuelve un boolean que devuelve true si este pertenece a un cliente existente del banco y false si no
     * */
    @Deprecated
    public boolean isDNIvalido(String nombre_banco,String dni_cliente){
        File f_clientes = new File("./Files/BancosComerciales/"+nombre_banco+"/Clientes_"+nombre_banco+".txt");
        FileReader fr = null;
        BufferedReader br = null;
        boolean isValido = false;
        if(f_clientes.exists()) {
            try {
                fr = new FileReader(f_clientes);
                br = new BufferedReader(fr);
                while (br.ready()) {
                    if (br.readLine().split(",")[1].equals(dni_cliente)) {
                        isValido = true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return isValido;
    }

    /*
     * INTERFAZ
     * Signatura: public boolean isIBANvalido(String iban_cuenta)
     * Comentario: Dado un iban devuelve true si este existe o false si no
     * Precondiciones: Se pasa el iban de la cuenta
     * Entrada: String iban_cuenta
     * Salida: boolean
     * Entrada/Salida:
     * Postcondiciones: asociado al nombre se devuelve un boolean que devuelve true si existe y false si no
     * */
    public boolean isIBANvalido(String iban_cuenta){
        String nombre_banco = " ";
        File f_cuentas = null;
        FileReader fr = null;
        BufferedReader br = null;
        boolean isValido = false;

        if(iban_cuenta.length() >= 13) {
            nombre_banco = obtenerNombreBancoComercialPorIBAN(iban_cuenta);
            f_cuentas = new File("./Files/BancosComerciales/" + nombre_banco + "/Cuentas_" + nombre_banco + "_Maestro.txt");
            if (f_cuentas.exists()){
                try {
                    fr = new FileReader(f_cuentas);
                    br = new BufferedReader(fr);
                    while (br.ready()) {
                        if (br.readLine().split(",")[0].equals(iban_cuenta)) {
                            isValido = true;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        }
        return isValido;
    }

    /*
     * INTERFAZ
     * Signatura: public boolean isPropietario(String dni_cliente, String iban_cuenta)
     * Comentario: Dado un iban y un dni de cliente, devuelve true si este iban pertenece a este cliente o false si no
     * Precondiciones: Se pasa el iban de la cuenta
     * Entrada: String dni_cliente, String iban_cuenta
     * Salida: boolean
     * Entrada/Salida:
     * Postcondiciones: asociado al nombre se devuelve true si este iban pertenece a este cliente o false si no
     */
    public boolean isPropietario(String dni_cliente, String iban_cuenta)
    {
        String nombre_banco = obtenerNombreBancoComercialPorIBAN(iban_cuenta);
        File f_clientes_cuentas = new File("./Files/BancosComerciales/"+nombre_banco+"/Clientes_Cuentas_"+nombre_banco+"_Maestro.txt");
        FileReader fr = null;
        BufferedReader br = null;
        boolean isProp = false;
        String registro = " ";

        if(f_clientes_cuentas.exists()) {
            try {
                fr = new FileReader(f_clientes_cuentas);
                br = new BufferedReader(fr);
                while (br.ready()) {
                    registro = br.readLine();
                    if (registro.split(",")[0].equals(dni_cliente) && registro.split(",")[1].equals(iban_cuenta)) {
                        isProp = true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return isProp;
    }




    /*
     * INTERFAZ
     * Signatura: public String obtenerClientePorIBAN(String iban_cuenta)
     * Comentario: dado el iban de la cuenta, te devuelve el cliente al que pertenece la cuenta
     * Precondiciones: se pasa el iban de la cuenta
     * Entrada: String iban_cuenta
     * Salida: String que es el dni del cliente
     * Entrada/Salida:
     * Postcondiciones: Asociado al nombre se devuelve el DNI del cliente al que pertenece la cuenta
     * */
    public String obtenerClientePorIBAN(String iban_cuenta){
        String nombre_banco = obtenerNombreBancoComercialPorIBAN(iban_cuenta);
        File fichero_clientes_cuentas = new File("./Files/BancosComerciales/"+nombre_banco+"/Clientes_Cuentas_"+nombre_banco+"_Maestro.txt");
        FileReader fr = null;
        BufferedReader br = null;
        String registro = " ";
        String dni_cliente = " ";

        try{
            fr = new FileReader(fichero_clientes_cuentas);
            br = new BufferedReader(fr);
            while (br.ready()){
                registro = br.readLine();

                if (registro.split(",")[1].equals(iban_cuenta)){
                    dni_cliente = registro.split(",")[0];
                }
            }
            br.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return dni_cliente;
    }
    /*
     * INTERFAZ
     * Signatura: public String obtenerIBANPorCliente(String nombre_banco, String dni_cliente)
     * Comentario: dado el nombre del banco y el dni del propietario de la cuenta, te devuelve el IBAN de la cuenta
     * Precondiciones: se pasan el nombre del banco y el dni del propietario
     * Entrada: String nombre_banco, String dni_cliente
     * Salida: String que es el IBAN de la cuenta
     * Entrada/Salida:
     * Postcondiciones: Asociado al nombre se devuelve el IBAN de la cuenta que pertenece a dicho cliente
     * */
    public String obtenerIBANPorCliente(String nombre_banco, String dni_cliente){
        File fichero_clientes_cuentas = new File("./Files/BancosComerciales/"+nombre_banco+"/Clientes_Cuentas_"+nombre_banco+"_Maestro.txt");
        FileReader fr = null;
        BufferedReader br = null;
        String registro = " ";
        String iban_cuenta = " ";

        try{
            fr = new FileReader(fichero_clientes_cuentas);
            br = new BufferedReader(fr);
            while (br.ready()){
                registro = br.readLine();

                if (registro.split(",")[0].equals(dni_cliente)){
                    iban_cuenta = registro.split(",")[1];
                }
            }
            br.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return iban_cuenta;
    }
    
    /*
     * INTERFAZ
     * Signatura: public void ordenarMovimientosPorFecha(String iban)
     * Comentario: Ordena un fichero de movimientos de una cuenta en base a las fechas, deja primero los mÃ¡s recientes.
     * Precondiciones: Se le pasa por referencia el IBAN de la cuenta
     * Entrada: String IBAN
     * Salida:
     * Entrada/Salida:
     * Postcondiciones: Se modifica el fichero y se deja ordenado de forma descendente
     * */
    public void ordenarMovimientosPorFecha(String iban){
        String nombre_banco = obtenerNombreBancoComercialPorIBAN(iban);
        File ficheroMovimientosCuenta = new File ("./Files/BancosComerciales/"+nombre_banco+"/Transferencias/Transferencias_Cuenta_"+iban+".dat");
        //File temp = new File ("./Files/BancosComerciales/"+nombre_banco+"/Movimientos/Movimientos_Cuenta_"+iban+"_temp.txt");
        ObjectInputStream leer = null;
        ObjectOutputStream escribir = null;
        List<TransferenciaImpl> registros = new ArrayList<TransferenciaImpl>();    //arraylist - considerar cambiar a array
        TransferenciaImpl registro = null;
        TransferenciaImpl aux = null; //para el bubblesort de mÃ¡s abajo
        boolean cont = true;

        try {
            leer = new ObjectInputStream(new FileInputStream(ficheroMovimientosCuenta));
            while (cont) {
                registro = (TransferenciaImpl) leer.readObject();
                registros.add(registro);
            }
            leer.close();
        }catch (EOFException e){

        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        //ordenamiento burbuja

        for (int i = 0; i < registros.size()-1;i++){
            for (int j = registros.size()-1; j>i; j--){
                if (registros.get(j).getFecha().before(registros.get(j-1).getFecha()) || registros.get(j).getFecha().equals(registros.get(j-1).getFecha()) ) {
                    //se produce el intercambio de elementos
                    aux = registros.get(j);
                    registros.set(j,registros.get(j-1));
                    registros.set(j-1, aux);
                }
            }
        }


        try{
           escribir = new ObjectOutputStream(new FileOutputStream(ficheroMovimientosCuenta));
            for(int i = 0; i < registros.size(); i ++){
                escribir.writeObject(registros.get(i));

            }
            escribir.close();
        }catch ( IOException e ){
            e.printStackTrace();
        }

    }

    /*
     * INTERFAZ
     * Signatura: public void insertarMovimientoEnFicheroMovimientos(String ID_Cuenta,boolean isIngresoOrRetirada, String concepto, double cantidad,GregorianCalendar fecha)
     * Comentario: Este metodo se encarga de modificar en el fichero de movimientos de la cuenta, aÃ±ade un nuevo movimiento.
     * Precondiciones: Se pasa por referencia el ID de la cuenta y por valor la cantidad de dinero a mover. Se pasa
     *                  un boolean que es true si el movimiento es un ingreso o false si es una retirada de dinero. Tambien se pasa la fecha
     * Entrada: (String ID_Cuenta,boolean isIngresoOrRetirada, double cantidad,GregorianCalendar fecha)
     * Salida:
     * Entrada/Salida:
     * Postcondiciones: Se modifica el fichero de movimientos de cuentas, aÃ±adiendo un movimiento nuevo.
     * */
    public boolean insertarMovimientoEnFicheroMovimientos(String ID_Cuenta,boolean isIngresoOrRetirada, String concepto, double cantidad,GregorianCalendar fecha){
        String nombre_banco = obtenerNombreBancoComercialPorIBAN(ID_Cuenta);
        File ficheroCuentas = new File ("./Files/BancosComerciales/"+nombre_banco+"/Transferencias/Transferencias_Cuenta_"+ID_Cuenta+".txt");
        TransferenciaImpl trans = new TransferenciaImpl(ID_Cuenta,isIngresoOrRetirada,concepto,cantidad,fecha);
        boolean movimientoInsertado = false;
        MyObjectOutputStream oos = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setCalendar(fecha);
        String fechaformateada = sdf.format(fecha.getTime());
        try
        {
            oos = new MyObjectOutputStream(new FileOutputStream(ficheroCuentas,true));
            oos.writeObject(trans);
            movimientoInsertado = true;
            oos.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return movimientoInsertado;
    }


    /*
     * INTERFAZ
     * Signatura: public void modificarSaldoEnFicheroCuentas(String iban_cuenta, boolean sumaOresta,double cantidad)
     * Comentario: Este metodo se encarga de modificar en el fichero de Cuentas, el registro del saldo total (campo 2).
     * Precondiciones: Se pasa por referencia el ID de la cuenta a modificar y por valor la cantidad a aÃ±adir o substraer. Se pasa boolean que es true si aÃ±ade la cantidad o false si la resta
     * Entrada: String ID_Cuenta, boolean sumaOresta, double cantidad
     * Salida:
     * Entrada/Salida:
     * Postcondiciones: Se modifica el fichero de Cuentas y se actualiza el saldo pertinente.
     * */
    //TODO Este m�todo no est� bien: Tiene que escribir un registro nuevo en el fichero de movimientos, no modificar el maestro directamente.
    public boolean modificarSaldoEnFicheroCuentas(String iban_cuenta, boolean sumaOresta,double cantidad){
        String nombre_banco = obtenerNombreBancoComercialPorIBAN(iban_cuenta);
        File ficheroCuentas = new File ("./Files/BancosComerciales/"+nombre_banco+"/Cuentas_"+nombre_banco+"_Movimientos.txt");
        FileReader leer = null;
        BufferedReader br = null;
        FileWriter fw = null;
        BufferedWriter bw = null;
        String campos[] = null;
        List<String> registros = new ArrayList<String>();   //toma ya usando arraylist Â¯\_(ãƒ„)_/Â¯
        String registro = " ";
        boolean saldoModificado = false;

        try {
            leer = new FileReader(ficheroCuentas);
            br = new BufferedReader(leer);

            while(br.ready()){
                registro = br.readLine();
                campos = registro.split(",");

                if(campos[0].equals(iban_cuenta)){
                    if(sumaOresta){
                        registro = registro.replace(campos[1], Double.toString(cantidad+Double.parseDouble(campos[1])));
                        saldoModificado = true;
                    }else{
                        registro = registro.replace(campos[1], Double.toString(Double.parseDouble(campos[1])-cantidad));
                        saldoModificado = true;
                    }

                }

                registros.add(registro);
            }
            br.close();



            fw = new FileWriter(ficheroCuentas);
            bw = new BufferedWriter(fw);
            for(String s : registros) {
                bw.write(s);
                bw.newLine();
            }
            bw.close();


        }catch (IOException e){e.printStackTrace();}

        return saldoModificado;
    }
    
    /* INTERFAZ
     * Comentario: Comprueba si un cliente (DNI) está registrado en un banco(BIC)
     * Prototipo: public boolean DNIRegistrado(String DNI, String BIC)
     * Entrada: Un string con el DNI a comprobar, y un String con el BIC del banco donde se quiere comprobar
     * Precondiciones: No hay
     * Salida: Un boolean indicando si el DNI está registrado en el banco o no
     * Postcondiciones: Asociado al nombre deuvelve true si el DNI está registrado en el banco (BIC), o false si no lo está.
     */
    public boolean DNIRegistrado(String DNI, String BIC)
    {
    	boolean registrado = false;
    	String nombreBanco = this.obtenerNombrePorBIC(BIC);
    	File ficheroClientes = new File ("./Files/BancosComerciales/"+nombreBanco+"/Clientes_"+nombreBanco+"_Maestro.txt");
		FileReader leer = null;
	    BufferedReader br = null;
	    String campos[] = null;
	    String registro = null;
	    
	    try 
        {
            leer = new FileReader(ficheroClientes);
            br = new BufferedReader(leer);

            while(br.ready())
            {
                registro = br.readLine();
                
                if(registro != null)
                {
	                campos = registro.split(",");
	                if(campos[1].equals(DNI))
	                		registrado = true;
                }
            }
            
            br.close();
        }
        catch (IOException e)
        {
        	e.printStackTrace();
        }
    	
    	return registrado;
    }
    
    /* INTERFAZ
     * Comentario: crea un nuevo cliente y una cuenta asociada a él en un banco determinado.
     * Prototipo: public boolean insertarCliente(String BIC, String DNI, double ingresosMensuales)
     * Entrada:
     * 		-> Un string con el BIC del banco donde se insertará el nuevo cliente
     * 		-> un String con el DNI del cliente
     * 		-> un double con los ingresos mensuales del cliente
     * Precondiciones: El BIC debe ser de un banco existente.
     * Salida: Un String indicando el IBAN de la cuenta asociada al cliente nuevo creado.
     * Postcondiciones: Asociado al nombre devuelve un String, que será el IBAN de la cuenta asociada al cliente nuevo, o null
     * 					Si no se creó correctamente.
     */
    public String insertarCliente(String BIC, String DNI, double ingresosMensuales)
    {
    	String IBAN = null;
    	
    	if(this.DNIRegistrado(DNI, BIC) == false && DNI.length() == 10)
    	{
    		String nombreBanco = this.obtenerNombrePorBIC(BIC);
    		File ficheroClientes = new File("./Files/BancosComerciales/"+nombreBanco+"/Clientes_"+nombreBanco+"_Movimientos.txt");
    		File ficheroCuentas = new File("./Files/BancosComerciales/"+nombreBanco+"/Cuentas_"+nombreBanco+"_Movimientos.txt");
    		FileWriter fw = null;
    		BufferedWriter bw = null;
    		
    		FileReader fr = null;
    		BufferedReader br = null;
    		String registro = null;
    		String[] campos = null;
    		String IBANUltimaCuenta = null;
    		String numeroCuentaUltima = null;
    		
    		//Insertar registro en el fichero de clientes del banco
    		try
    		{
    			fw = new FileWriter(ficheroClientes, true);
    			bw = new BufferedWriter(fw);
    			
    			bw.write(new ClienteImpl(BIC, DNI,ingresosMensuales).toString());
    			
    			bw.close();
    		}
    		catch(IOException e)
    		{
    			e.printStackTrace();
    		}
    		
    		//Crear una cuenta
    		
    		//Leer cual es la ultima cuenta registrada
    		try
    		{
    			fr = new FileReader(ficheroCuentas);
    			br = new BufferedReader(fr);
    			
    			while(br.ready())
    			{
    				registro = br.readLine();
    				
    				if(br.ready() == false)
    				{
    					campos = registro.split(",");
    					if(campos != null)
    						IBANUltimaCuenta = campos[0];
    				}
    			}
    			
    			if(br.ready() == false)
    				IBANUltimaCuenta = "ESP" + BIC + "0000000";
    			
    			br.close();
    			fr.close();
    		}
    		catch(IOException e)
    		{
    			e.printStackTrace();
    		}
    		
    		numeroCuentaUltima = this.obtenerNumCuentaPorIBAN(IBANUltimaCuenta);

    		String numeroCuenta = String.valueOf((Integer.parseInt(numeroCuentaUltima) + 1));
    		while(numeroCuenta.length() < 7)
    		{
    			numeroCuenta = "0" + numeroCuenta;
    		}
    		
    		IBAN = "ESP"+BIC+numeroCuenta;
    		
    		CuentaImpl cuenta = new CuentaImpl(IBAN);
    		
    		//Añadir cuenta al fichero de cuentas
    		try
    		{
    			fw = new FileWriter(ficheroCuentas, true);
    			bw = new BufferedWriter(fw);
    			
    			bw.write(cuenta.toString());
    			
    			bw.close();
    		}
    		catch(IOException e)
    		{
    			e.printStackTrace();
    		}
    	}
    	
    	return IBAN;
    }
    
    /* INTERFAZ
	 * Comentario: Accede al fichero de cuentas y busca una cuenta por su IBAN para leer sus datos
	 * Prototipo: public String datosCuenta(String IBAN)
	 * Precondiciones: No hay
	 * Entrada: el IBAN de la cuenta.
	 * Salida: un String con los datos de la cuenta
	 * Postcondiciones: Asociado al nombre devuelve un String con los datos de la cuenta
	 */
	public String datosCuenta(String IBAN)
	{
		String cuenta = null;
		String nombreBanco = this.obtenerNombreBancoComercialPorIBAN(IBAN);
		File ficheroCuentas = new File("./Files/BancosComerciales/"+nombreBanco+"/Cuentas_"+nombreBanco+"_Maestro.txt");
		FileReader fr = null;
		BufferedReader br = null;
		String registro;
		String[] campos;
		
		try
		{
			fr = new FileReader(ficheroCuentas);
			br = new BufferedReader(fr);
			
			while(br.ready())
			{
				//buscar la cuenta y guardarla en el String que se devuelve
				registro = br.readLine();
				campos = registro.split(",");
				
				if(campos[0].equals(IBAN))
					cuenta = registro;
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		//System.out.println("datosCuenta en resguardo");
		
		return cuenta;
	}
	
	/* INTERFAZ
	 * Comentario: Escribe un registro nuevo en un fichero de movimientos determinado
	 * Prototipo: public boolean escribirRegistroEnMovimientos(String registro, String rutaFichero)
	 * Entrada: Un String con el registro a escribir, y otro String con la ruta del fichero donde se escribir�.
	 * Precondiciones: No hay
	 * Salida: Un boolean indicando si se ha escrito correctamente o no.
	 * Postcondiciones: Asociado al nombre devuelve:
	 * 					-> True si se ha escrito correctamente el registro en el fichero correspondiente
	 * 					-> False si no se ha escrito correctamente.
	 */
	public boolean escribirRegistroEnMovimientos(String registro, String rutaFichero)
	{
		boolean escrito = false;
		
		File fichero = new File(rutaFichero);
		FileWriter fw = null;
		BufferedWriter bw = null;
		
		try
		{
			fw = new FileWriter(fichero, true);
			bw = new BufferedWriter(fw);
			
			bw.write(registro);
			
			bw.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		return escrito;
	}


    /*
     * INTERFAZ
     * Signatura: public String obtenerBICporNombre(String nombre_banco)
     * Comentario: devuelve el BIC de un banco dando su nombre
     * Precondiciones: por referencia se pasa un string
     * Entrada: String nombre
     * Salida: String BIC
     * Entrada/Salida:
     * Postcondiciones: Asociado al nombre se devuelve un String
     * */
    public String obtenerBICporNombre(String nombre_banco){
        File clientesBancoCentral = new File("./Files/BancoCentral/Clientes_BancoCentral_Maestro.txt");
        FileReader leer = null;
        BufferedReader br = null;
        String registro = " ";
        String campos[] = null;
        String bic = " ";
        try{
            leer = new FileReader(clientesBancoCentral);
            br = new BufferedReader(leer);
            while(br.ready()){
                registro = br.readLine();
                campos = registro.split(",");

                if ( campos[1].equals(nombre_banco)){
                    bic = campos[0].substring(3,14);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }


        return bic;
    }

    /*
     * INTERFAZ
     * Signatura: public String obtenerNombreBancoComercialPorIBAN(String iban_cuenta)
     * Comentario: devuelve el nombre de un banco dado el IBAN de una cuenta
     * Precondiciones: por referencia se pasa un string
     * Entrada: String iban_cuenta
     * Salida: String nombre
     * Entrada/Salida:
     * Postcondiciones: Asociado al nombre se devuelve un String
     * */
    public String obtenerNombreBancoComercialPorIBAN(String iban_cuenta){

        return obtenerNombrePorBIC(iban_cuenta.substring(3,14));
    }

    /* INTERFAZ
     * Comentario: A partir de un IBAN, obtiene el BIC del banco central al que pertenece el banco que gestiona la cuenta
     * Prototipo: public String obtenerBICporIBAN(String IBAN)
     * Entrada: Un String con el IBAN
     * Precondiciones: No hay
     * Salida: Un string con el BIC del banco central al que pertenece el banco que gestiona la cuenta
     * Postcondiciones: Asociado al nombre devuelve un strnig con el BIC del banco central al que pertenece el banco que gestiona la cuenta
     */
    public String obtenerBICporIBAN(String IBAN)
    {
        return IBAN.substring(3, 14);
    }

    /* INTERFAZ
     * Comentario: Obtiene el numero de cuenta de un IBAN
     * Prototipo: public String obtenerNumCuentaPorIBAN(String IBAN)
     * Entrada: Un String con el IBAN del que se quiere obtener su numero de cuenta
     * Precondiciones: No hay
     * Salida: Un String con el numero de cuenta del IBAN especificado
     * Postcondiciones: Asociado al nombre devuelve un String con el numero de cuenta del IBAN especificado
     */
    public String obtenerNumCuentaPorIBAN(String IBAN)
    {
        return IBAN.substring(14, 21);
    }

    /*
     * INTERFAZ
     * Signatura: public String obtenerNombrePorBIC(String BIC)
     * Comentario: devuelve el nombre de un banco dando su BIC
     * Precondiciones: por referencia se pasa un string
     * Entrada: String BIC
     * Salida: String nombre
     * Entrada/Salida:
     * Postcondiciones: Asociado al nombre se devuelve un String
     * */
    public String obtenerNombrePorBIC(String bic){
        File clientesBancoCentral = new File("./Files/BancoCentral/Clientes_BancoCentral_Maestro.txt");
        FileReader leer = null;
        BufferedReader br = null;
        String registro = " ";
        String campos[] = null;
        String nombre = " ";
        try{
            leer = new FileReader(clientesBancoCentral);
            br = new BufferedReader(leer);
            while(br.ready()){
                registro = br.readLine();
                campos = registro.split(",");

                if ( campos[0].substring(3,14).equals(bic)){
                    nombre = campos[1];
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }


        return nombre;
    }

    /* INTERFAZ
     * Signatura: public void ingresarDinero(String nombre_banco, String ID_Cuenta,String concepto, double cantidad, int dia, int mes, int anyo)
     * Comentario: ingresa una cantidad dada de una cuenta
     * Precondiciones: Por valor se pasa una cantidad, por referencia la ID de una cuenta y el nombre del banco. Por valor se pasa dia, mes y año
     * Entrada: String nombre_banco, String ID_Cuenta, double cantidad, int dia, int mes, int anyo
     * Salida: Un boolean indiciando si se ha ingresado el dinero con exito o no
     * Entrada/Salida:
     * Postcondiciones: Asociado al nombre devuelve:
     * 				-> true si se ha ingresado el dinero con exito, insertando el movimiento en el fichero de movimientos y modificando el saldo en el fichero de cuentas
     * 				-> false si no se ha podido realizar con exito la operacion
     * */
    public boolean ingresarDinero(String IBAN,String concepto, double cantidad, GregorianCalendar fecha){

        boolean ingresado = false;

        boolean insertado = insertarMovimientoEnFicheroMovimientos(IBAN, true, concepto, cantidad, fecha);
        boolean modificado = modificarSaldoEnFicheroCuentas(IBAN, true, cantidad);

        if(insertado && modificado)
            ingresado = true;

        return ingresado;

    }

    /* INTERFAZ
     * Signatura: public void realizarMovimiento(String nombre_banco_origen,String cuenta_origen,String nombre_banco_destino, String cuenta_destino, String concepto,double cantidad, int dia, int mes, int anyo)
     * Comentario: Realiza un movimiento bancario, sacando una cantidad de la cuenta de origen e ingresándola en la cuenta destino.
     *              Llama a los métodos sacarDinero e ingresarDinero.
     * Precondiciones: Por referencia se pasan las ID de las cuentas y los nombres de los bancos de origen y destino, por valor se pasa la cantidad y dia mes y anyo. Tambien se pasa por referencia el concepto
     * Entrada: (String nombre_banco_origen,String cuenta_origen,String nombre_banco_destino, String cuenta_destino, String concepto,double cantidad, int dia, int mes, int anyo)
     * Salida: Un boolean indicando si se ha realizado bien el movimiento o no
     * Entrada/Salida:
     * Postcondiciones: Asociado al nombre devuelve:
     * 					-> true si se ha realizado bien el movimiento
     * 					-> false si no se ha realizado bien el movimiento
     * */
    public boolean realizarMovimiento(String IBANOrigen,String IBANDestino, String concepto,double cantidad, GregorianCalendar fecha){

        boolean movimientoRealizado = false;

        boolean sacado = sacarDinero(IBANOrigen, concepto, cantidad, fecha);
        boolean ingresado = ingresarDinero(IBANDestino, concepto, cantidad, fecha);


        if(sacado && ingresado)
            movimientoRealizado = true;

        return movimientoRealizado;
    }

    /*INTERFAZ
     * Signatura: public void sacarDinero(String nombre_banco, String ID_Cuenta, String concepto,double cantidad, int dia, int mes, int anyo)
     * Comentario: saca una cantidad dada de una cuenta
     * Precondiciones: Por valor se pasa una cantidad, por referencia la ID de una cuenta y el nombre del banco. Se pasa por valor dia mes y año
     * Entrada: String nombre_banco, String ID_Cuenta, double cantidad, int dia, int mes, int anyo
     * Salida: Un boolean indicando si se ha sacado bien el dinero
     * Entrada/Salida:
     * Postcondiciones: Asociado al nombre devuelve:
     * 					-> true si se ha sacado bien el dinero de la cuenta, insertando el movimiento en el fichero de movimientos y modificando el saldo en el fichero de cuentas
     * 					-> false si no se ha podido realizar bien la operacion.
     * */
    public boolean sacarDinero(String IBAN,String concepto, double cantidad, GregorianCalendar fecha){

        boolean dineroSacado = false;

        boolean movimientoInsertado = insertarMovimientoEnFicheroMovimientos(IBAN, false, concepto, cantidad, fecha);
        boolean modificado = modificarSaldoEnFicheroCuentas(IBAN, false, cantidad);

        if (movimientoInsertado && modificado)
        {
            dineroSacado = true;
        }

        return dineroSacado;
    }

    /* INTERFAZ
     * Comentario: Actualiza un fichero maestro determinado, mirando los registros de su fichero de movimiento correspondiente.
     * Prototipo: public boolean actualizarFichero(String fichero)
     * Entrada: Un string con la ruta del fichero
     * Precondiciones:
     * Salida:
     * Postcondiciones:
     */
    public boolean actualizarFichero(String fichero, int posicionCampoClave)
    {
        boolean actualizado = false;
        File ficheroMaestro = new File(fichero + "_Maestro.txt");
        File ficheroMovimientos = new File(fichero + "_Movimientos.txt");
        File ficheroMaestroAct = new File(fichero + "_Maestro_act.txt");
        FileReader frMaestro = null;
        FileReader frMovimientos = null;
        FileWriter fwMaestroAct = null;
        BufferedReader brMaestro = null;
        BufferedReader brMovimientos = null;
        BufferedWriter bwMaestroAct = null;

        try
        {
            frMaestro = new FileReader(ficheroMaestro);
            frMovimientos = new FileReader(ficheroMovimientos);
            fwMaestroAct = new FileWriter(ficheroMaestroAct);

            brMaestro = new BufferedReader(frMaestro);
            brMovimientos = new BufferedReader(frMovimientos);
            bwMaestroAct = new BufferedWriter(fwMaestroAct);

            //Leer primeros registros de ficheroMovimientos y ficheroMaestro
            String registroMovimientos = brMovimientos.readLine();
            String registroMaestro = brMaestro.readLine();

            String campoClaveMovimientos = registroMovimientos.split(",")[posicionCampoClave];
            String campoClaveMaestro = registroMaestro.split(",")[posicionCampoClave];

            //Mientras no sea fin de fichero en ninguno de los dos
            while(registroMovimientos != null && registroMaestro != null)
            {
                if(campoClaveMovimientos.compareTo(campoClaveMaestro) == 0)
                {

                    if(!registroMovimientos.contains("*"))
                    {
                        bwMaestroAct.write(registroMovimientos + "\n");
                    }

                    //Se mueven los dos punteros
                    registroMovimientos = brMovimientos.readLine();
                    if(registroMovimientos != null)
                        campoClaveMovimientos = registroMovimientos.split(",")[posicionCampoClave];

                    registroMaestro = brMaestro.readLine();
                    if(registroMaestro != null)
                        campoClaveMaestro = registroMaestro.split(",")[posicionCampoClave];
                }
                else if(campoClaveMovimientos.compareTo(campoClaveMaestro) > 0)
                {

                    while(campoClaveMovimientos.compareTo(campoClaveMaestro) > 0 && registroMaestro != null)
                    {
                        //Escribir registro de maestro en maestroAct
                        bwMaestroAct.write(registroMaestro + "\n");

                        //leer registro de maestro

                        registroMaestro = brMaestro.readLine();

                        if(registroMaestro != null)
                        {
                            campoClaveMaestro = registroMaestro.split(",")[posicionCampoClave];
                        }
                    }
                }
                else if(campoClaveMovimientos.compareTo(campoClaveMaestro) < 0)
                {
                    //Es un alta o un error
                    bwMaestroAct.write(registroMovimientos + "\n");

                    //Leer registro de movimiento
                    registroMovimientos = brMovimientos.readLine();

                    if(registroMovimientos != null)
                        campoClaveMovimientos = registroMovimientos.split(",")[posicionCampoClave];
                }
            }
            //Se ha acabado el fichero de movimientos y aun quedan registros en el maestro
            while(registroMaestro != null)
            {
                //Escribir registro de maestro en maestroAct
                bwMaestroAct.write(registroMaestro + "\n");

                //leer registro de maestro
                registroMaestro = brMaestro.readLine();

                if(registroMaestro != null)
                    campoClaveMaestro = registroMaestro.split(",")[posicionCampoClave];
            }
            //Se ha acabado el fichero maestro y aun quedan registros en el de movimientos
            while(registroMovimientos != null)
            {
                //Escribir registro de movimientos en maestroAct
                bwMaestroAct.write(registroMovimientos + "\n");

                //leer registro de movimientos
                registroMovimientos = brMovimientos.readLine();

                if(registroMovimientos != null)
                    campoClaveMovimientos = registroMovimientos.split(",")[posicionCampoClave];
            }

            //Cerrar archivos
            brMaestro.close();
            brMovimientos.close();
            bwMaestroAct.close();

            ficheroMaestro.delete();
            ficheroMaestroAct.renameTo(ficheroMaestro);

            ficheroMovimientos.delete();
            ficheroMovimientos.createNewFile();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        return actualizado;
    }

}
