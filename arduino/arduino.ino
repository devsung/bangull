#include <SoftwareSerial.h>

#define BT_RXD 2
#define BT_TXD 3
#define LED 13

String serial = "";
String ssid = "";
String password = "";
int sensor = A0;

String server = "";
String url = "";

SoftwareSerial ESP_wifi(BT_RXD, BT_TXD);

void setup() {
  Serial.begin(9600);
  ESP_wifi.begin(9600);
  pinMode(sensor, INPUT);
  pinMode(LED, OUTPUT);
  delay(1000);
  ESP_wifi.println("AT+CWJAP=\"" + ssid + "\",\"" + password + "\"");
  while (!ESP_wifi.find("OK")) { }
  delay(1000);
  digitalWrite(13, HIGH);
}

void loop() {
  int Sound = analogRead(sensor);
  if (Sound >= 1000) {
    digitalWrite(13, LOW);
    ESP_wifi.println(server);
    delay(1000);
    ESP_wifi.println("AT+CIPSEND=39");
    ESP_wifi.println(url);
    delay(1000);
    digitalWrite(13, HIGH);
  }
  delay(10);
}
