package com.aluracursos.javachallenge;
import java.util.Scanner;

public class ConversorApp {

    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);
        System.out.println("***********************************************\n");
        System.out.println("Bienvenido a Conversor de Monedas");
        System.out.print("Ingrese su Api-key de \"Exchange Rate API\" (0 = API-KEY integrada): ");
        String apiKkey = teclado.next();

        Conversor miConversor = new Conversor(apiKkey);

        if(!miConversor.isValid()){
            System.out.println("API key invalida!");
        }else {
            System.out.print("*Api-Key Valida!\n");

            while (true) {
                System.out.println("***********************************************\n");
                System.out.println("Bienvenido a Conversor de Monedas");
                System.out.println("1) Dolar =>>> Peso Chileno");
                System.out.println("2) Peso Chileno ===> Dolar");
                System.out.println("3) Dolar ===> Peso Argentino");
                System.out.println("4) Peso Argentino ===> Dolar");
                System.out.println("5) Conversion especifica [ISO 4217]");
                System.out.println("6) Salir");
                System.out.println("Elija una opcion valida:");
                System.out.println("***********************************************\n");

                try {
                    int opcion = Integer.valueOf(teclado.next().trim());
                    if (opcion > 0 && opcion < 5) {
                        switch (opcion) {
                            case 1:
                                miConversor.setMonedaOrigen("USD");
                                miConversor.setMonedaDestino("CLP");
                                break;
                            case 2:
                                miConversor.setMonedaOrigen("CLP");
                                miConversor.setMonedaDestino("USD");
                                break;
                            case 3:
                                miConversor.setMonedaOrigen("USD");
                                miConversor.setMonedaDestino("ARS");
                                break;
                            case 4:
                                miConversor.setMonedaOrigen("ARS");
                                miConversor.setMonedaDestino("USD");
                                break;
                            default:
                                System.out.println("Opcion invalida, reintente!");
                                break;
                        }

                    } else if (opcion == 5) {
                        System.out.println("***********************************************\n");
                        System.out.println("Ingrese la moneda de origen (USD, CLP, BRL, etc..): ");
                        miConversor.setMonedaOrigen(teclado.next());
                        System.out.println("Ingrese la moneda de destino (USD, CLP, BRL, etc..): ");
                        miConversor.setMonedaDestino(teclado.next());
                    } else if(opcion == 6){
                        break;
                    } else {
                        System.out.println("Opcion ingresada fuera de rango");
                        continue;
                    }

                    System.out.println("Ingrese el valor que desea convertir: ");
                    double montoOrigen = Double.valueOf(teclado.next());
                    miConversor.setMontoOrigen(montoOrigen);

                    ConversionExchangeRateApi datosExRateApi = miConversor.nuevaConversion();
                    //System.out.println(datosExRateApi);

                    if (datosExRateApi.result().equals("success")) {

                        double resultado = miConversor.getMontoOrigen() * datosExRateApi.conversion_rate();

                        String mensaje = String.format(">> %.2f %s son %.2f %s \n",
                                miConversor.getMontoOrigen(),
                                miConversor.getMonedaOrigen(),
                                resultado,
                                miConversor.getMonedaDestino()
                                );

                        System.out.println(mensaje);
                    } else {
                        System.out.println("Error en la solicitud: " + datosExRateApi.errorType());
                    }

                } catch (NumberFormatException e) {
                    System.out.println("El formato de la opcion ingresada es invalida, reintente!");
                }

            }
        }
        System.out.println("Fin del programa!");
    }



}
