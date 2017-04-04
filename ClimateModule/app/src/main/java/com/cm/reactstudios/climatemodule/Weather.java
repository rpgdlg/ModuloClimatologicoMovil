package com.cm.reactstudios.climatemodule;
public class Weather{
    private String humidity, light, temperature, pressure;
    public Weather(String h, String l, String t, String p){
        humidity=h;
        light=l;
        temperature=t;
        pressure=p;
    }
    public String getHumidity(){return humidity;}
    public String getLight(){return light;}
    public String getTemperature(){return temperature;}
    public String getPressure(){return pressure;}
}