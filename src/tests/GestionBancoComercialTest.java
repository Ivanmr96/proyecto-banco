package tests;

import gestion.GestionBancoComercial;
import utilidades.Utilidades;

import java.util.GregorianCalendar;

public class GestionBancoComercialTest {
    public static void main(String[] args) {
        GestionBancoComercial gbc = new GestionBancoComercial();
        Utilidades u = new Utilidades();
        System.out.println("isIBANValido ANTES-> ESPCAIXESBBXXX0000001 : " + gbc.isIBANvalido("ESPCAIXESBBXXX0000001"));
        System.out.println("isDNIValido ANTES -> 28835488-c : " + gbc.DNIRegistrado("28835488-c","CAIXESBBXXX" ));
        System.out.println("eliminarCuenta -> ESPCAIXESBBXXX0000001 : " + gbc.eliminarCuenta("ESPCAIXESBBXXX0000001"));
        System.out.println("isIBANValido DESPUES-> ESPCAIXESBBXXX0000001: " + gbc.isIBANvalido("ESPCAIXESBBXXX0000001"));
        System.out.println("isDNIValido DESPUES -> 28835488-c : " + gbc.DNIRegistrado("28835488-c","CAIXESBBXXX" ));

        System.out.println("Busco movimientos por fecha");
        System.out.println("buscarMovimientoPorFecha año 2019 de ESPCAIXESBBXXX0000003: ");
        u.imprimirMovimientos(gbc.buscarMovimientosPorFecha("ESPCAIXESBBXXX0000003", 2019));

        System.out.println("buscarMovimientoPorFecha año 2019 mes 04 de ESPCAIXESBBXXX0000003: ");
        u.imprimirMovimientos(gbc.buscarMovimientosPorFecha("ESPCAIXESBBXXX0000003", 04, 2019));

        System.out.println("buscarMovimientoPorFecha año 2019 mes 04 dia 25 de ESPCAIXESBBXXX0000003: ");
        u.imprimirMovimientos(gbc.buscarMovimientosPorFecha("ESPCAIXESBBXXX0000003", 25, 04, 2019));

        System.out.println("ultimosDiezMovimientos -> ESPCAIXESBBXXX0000003");
        u.imprimirMovimientos(gbc.ultimosDiezMovimientos("ESPCAIXESBBXXX0000003"));

        System.out.println("isPropietario -> 28835488-C , ESPCAIXESBBXXX0000003 (debe dar false) : " + gbc.isPropietario("28835488-C", "ESPCAIXESBBXXX0000003"));

        System.out.println("obtenerClientePorIBAN  -> ESPCAIXESBBXXX0000003 : " + gbc.obtenerClientePorIBAN("ESPCAIXESBBXXX0000003"));

        System.out.println("obtenerIBANporCliente  -> Santander, 28835488-c : " + gbc.obtenerIBANPorCliente("Santander", "28835488-c"));

        System.out.println("SALDO ANTES : " + gbc.obtenerSaldoPorIBAN("ESPCAIXESBBXXX0000003"));
        System.out.println("insertarMovimientoEnFicheroMovimientos ->ESPCAIXESBBXXX0000003 , true, Concepto1, 150.0, new GregorianCalendar() : " + gbc.insertarMovimientoEnFicheroMovimientos("ESPCAIXESBBXXX0000003",true, "Concepto1", 150.0, new GregorianCalendar()));
        System.out.println("SALDO DESPUES : " + gbc.obtenerSaldoPorIBAN("ESPCAIXESBBXXX0000003"));


        System.out.println("insertarCliente -> CAIXESBBXXX, 6546464464-L, 560 : "+ gbc.insertarCliente("CAIXESBBXXX","6546464464-L", 500 ) );

        System.out.println("datosCuenta ESPCAIXESBBXXX0000003 : " + gbc.datosCuenta("ESPCAIXESBBXXX0000003"));

        System.out.println("escribirRegistroEnMovimientos : ");

        System.out.println("obtener BIC por nombre -> LaCaixa: " + gbc.obtenerBICporNombre("LaCaixa"));
        System.out.println("obtener nombre banco comercial por iban -> ESPCAIXESBBXXX0000003 : "+ gbc.obtenerNombreBancoComercialPorIBAN("ESPCAIXESBBXXX0000003"));
        System.out.println("obtenerBICPorIBAN -> ESPCAIXESBBXXX0000003 : " + gbc.obtenerBICporIBAN("ESPCAIXESBBXXX0000003"));
        System.out.println("obtenerNumCuentaPorIBAN ESPCAIXESBBXXX0000003 : "+ gbc.obtenerNumCuentaPorIBAN("ESPCAIXESBBXXX0000003"));
        System.out.println("obtenerNombrePorBIC -> CAIXESBBXXX : " + gbc.obtenerNombrePorBIC("CAIXESBBXXX"));

        System.out.println("datos cuenta ANTES: " + gbc.datosCuenta("ESPCAIXESBBXXX0000003"));
        System.out.println("ingresarDinero -> ESPCAIXESBBXXX0000003, conceptoingreso, 15, new GregorianCalendar() :"+ gbc.ingresarDinero("ESPCAIXESBBXXX0000003", "conceptoingreso", 15, new GregorianCalendar()) );
        System.out.println("datos cuenta DESPUES: " + gbc.datosCuenta("ESPCAIXESBBXXX0000003"));

        System.out.println("datos cuenta origen ANTES: " + gbc.datosCuenta("ESPCAIXESBBXXX0000003"));
        System.out.println("datos cuenta dest ANTES: " + gbc.datosCuenta("ESPCAIXESBBXXX0000002"));
        System.out.println("realizarMovimiento -> ESPCAIXESBBXXX0000003, ESPCAIXESBBXXX0000002, conceptoMov, 1500, new GregorianCalendar() :"+ gbc.realizarMovimiento("ESPCAIXESBBXXX0000003","ESPCAIXESBBXXX0000002", "conceptoMov", 1500, new GregorianCalendar()) );
        System.out.println("datos cuenta origen DESPUES: " + gbc.datosCuenta("ESPCAIXESBBXXX0000003"));
        System.out.println("datos cuenta dest DESPUES: " + gbc.datosCuenta("ESPCAIXESBBXXX0000002"));

        System.out.println("datos cuenta ANTES: " + gbc.datosCuenta("ESPCAIXESBBXXX0000003"));
        System.out.println("sacarDinero -> ESPCAIXESBBXXX0000003, conceptoretirada, 50, new GregorianCalendar() :"+ gbc.sacarDinero("ESPCAIXESBBXXX0000003", "conceptoretirada", 50, new GregorianCalendar()) );
        System.out.println("datos cuenta DESPUES: " + gbc.datosCuenta("ESPCAIXESBBXXX0000003"));

        System.out.println("actualizarFichero Clientes_Cuentas La Caixa :" + gbc.actualizarFichero("./Files/BancosComerciales/LaCaixa/Clientes_Cuentas_LaCaixa",0 ));


    }
}
