package com.aluracursos.javachallenge;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Conversor {
    private String uriPair = "https://v6.exchangerate-api.com/v6/#API_KEY#/pair/#ORIGEN#/#DESTINO#";
    private String API_KEY = "cc0defb29c0d85a5ee3fadce";    // api key integrada
    private String monedaOrigen;
    private String monedaDestino;
    private double montoOrigen;

    public void setMonedaOrigen(String monedaOrigen) {
        this.monedaOrigen = monedaOrigen;
    }

    public void setMonedaDestino(String monedaDestino) {
        this.monedaDestino = monedaDestino;
    }

    public void setMontoOrigen(double montoOrigen) {
        this.montoOrigen = montoOrigen;
    }

    public String getMonedaOrigen() {
        return monedaOrigen;
    }

    public String getMonedaDestino() {
        return monedaDestino;
    }

    public double getMontoOrigen() {
        return montoOrigen;
    }

    // El cliente puede proporcionar su api-key personal
    public Conversor(String API_KEY) {
        if(!API_KEY.equals("0")){
            this.API_KEY = API_KEY;
        }
    }

    // conversion rapida para verificacion de api-key valida.
    public boolean isValid(){
        this.monedaOrigen = "USD";
        this.monedaDestino = "CLP";
        ConversionExchangeRateApi misdatos = this.nuevaConversion();
        return misdatos.result().equals("success");
    }

    public ConversionExchangeRateApi nuevaConversion(){

        String fullUri = this.uriPair.replace("#API_KEY#",this.API_KEY);

        fullUri = fullUri.replace("#ORIGEN#", this.monedaOrigen);
        fullUri = fullUri.replace("#DESTINO#", this.monedaDestino);

        URI urlApi = URI.create(fullUri);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(urlApi)
                .build();

        try {
            HttpResponse<String> response = null;
            response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());

            // Convertir a JSON
            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(response.body());

            JsonObject jsonobj = root.getAsJsonObject();

            String req_result = jsonobj.get("result").getAsString();
            String respuesta = response.body();

            if(req_result.equals("error")){ // si recibo algun error desde la api, lo proceso...
                String error_msj = "";

                switch (jsonobj.get("error-type").getAsString()){
                    case "unsupported-code":
                        error_msj = "El Codigo indicado no es valido.";
                        break;
                    case "malformed-request":
                        error_msj = "Error en la estructura de los parametros";
                        break;
                    case "invalid-key":
                        error_msj = "La clave API es invalida!";
                        break;
                    case "inactive-account":
                        error_msj = "Su cuenta no ha sido verificada!";
                        break;
                    case "quota-reached":
                        error_msj = "Ha alcanzado el limite maximo de consultas para su plan";
                        break;
                    default:
                        error_msj = "Error inesperado!";
                        break;
                }
                // integro el error en el mensaje json
                respuesta = respuesta.replace("error-type","errorType");
                respuesta = respuesta.replace(jsonobj.get("error-type").getAsString(),error_msj);
            }

            return new Gson().fromJson(respuesta, ConversionExchangeRateApi.class);

        } catch (Exception e) {
            throw new RuntimeException("Los parametros ingresados son incorrectos!");
        }
    }

}
