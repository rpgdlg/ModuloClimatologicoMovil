#include <Wire.h>
#include "MPL3115A2.h"
#include "HTU21D.h"
#include <SoftwareSerial.h>   // Incluimos la librer�a  SoftwareSerial  
#include <stdlib.h>

MPL3115A2 sensorPresion;  
HTU21D sensorHumedad;

SoftwareSerial BT(10,11);    // Definimos los pines RX y TX del Arduino conectados al Bluetooth

const byte LED1 = 7;
const byte LED2 = 8;
const byte SENSORLUZ = A1;
const byte REFERENCE_3V3 = A3;

float altitud;
float temperatura;
float humedad;
float luminosidad;

int tiempo;

void setup() {
  Serial.begin(9600);
  BT.begin(9600);       // Inicializamos el puerto serie BT (Para Modo AT 2)
  Serial.println("Inicializando Sensores...");
  
  pinMode(LED1, OUTPUT); //Status LED Blue
  pinMode(LED2, OUTPUT); //Status LED Green
  
  pinMode(SENSORLUZ, INPUT);

  sensorPresion.begin();
  sensorPresion.setModeAltimeter();
  sensorPresion.setOversampleRate(7);
  sensorPresion.enableEventFlags();

  sensorHumedad.begin();

  tiempo = 0;

  Serial.println("Sensores en linea.");
}

void loop() {
  digitalWrite(LED2, HIGH);
  if (tiempo > 59) digitalWrite(LED1, HIGH);
  altitud = sensorPresion.readAltitude();
  temperatura = sensorPresion.readTemp();
  humedad = sensorHumedad.readHumidity();
  luminosidad = getLightLevel();
  digitalWrite(LED2, LOW);
  if (tiempo > 59){
    imprime();
    digitalWrite(LED1, LOW);
    tiempo = -1;
  }
  tiempo++;
  delay(1000);
}

float getLightLevel(){
  float voltaje = analogRead(REFERENCE_3V3);
  float luzSensor = analogRead(SENSORLUZ);
  voltaje = 3.3 / voltaje; //The reference voltage is 3.3V or 4.5V to 5.2V
  luzSensor = voltaje * luzSensor;
  return(luzSensor);
}
void imprime(){
  String datos_clima="#";
  datos_clima+=(String)humedad;
  datos_clima+=(String)luminosidad;
  datos_clima+=(String)temperatura;
  datos_clima+=(String)altitud;
  datos_clima+="~";
  BT.print(datos_clima);
}
