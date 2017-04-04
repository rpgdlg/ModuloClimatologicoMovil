package com.cm.reactstudios.climatemodule;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Daniel on 14/03/2017.
 */
public class Enviar {
    private static final String IP = "mcm.sytes.net:80" ;
    private static final String TEMPERATURA = "&t=";
    private static final String HUMEDAD = "&h=";
    private static final String PRESION = "&p=";
    private static final String LUMINOSIDAD = "&l=";

    private static Weather dato = null;

    public static void setDato(Weather clima){
        dato = clima;
    }

    public static String enviarGET(){
        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resultado = null;

        try{
            url = new URL("http://" + IP + "/?action=insert" + TEMPERATURA
                    + dato.getTemperature() + HUMEDAD + dato.getHumidity()
                    + PRESION + dato.getPressure() + LUMINOSIDAD + dato.getLight());

            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            respuesta = connection.getResponseCode();

            resultado = new StringBuilder();

            if(respuesta == HttpURLConnection.HTTP_OK){
                InputStream in = new BufferedInputStream(connection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                while ((linea = reader.readLine()) != null){
                    resultado.append(linea);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return resultado.toString();
    }
}
