#include <ESP8266WiFi.h>

#ifndef STASSID
#define STASSID ""
#define STAPSK  ""
#endif

const char* ssid     = STASSID;
const char* password = STAPSK;

String serial = "";
const char* host = "";
String url = "";
const uint16_t port = 80;

int sensor = A0;

void setup() {
  Serial.begin(115200);
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
  }
  pinMode(sensor, INPUT);
  pinMode(LED_BUILTIN, OUTPUT);
  digitalWrite(LED_BUILTIN, LOW);
}

void loop() {
  WiFiClient client;
  if (client.connect(host, port)) {
    int sound = analogRead(sensor);
    if (sound >= 1000) {
      digitalWrite(LED_BUILTIN, HIGH);
      client.println(url);
      delay(1000);
      digitalWrite(LED_BUILTIN, LOW);
    }
  }
  client.stop();
  delay(10);
}