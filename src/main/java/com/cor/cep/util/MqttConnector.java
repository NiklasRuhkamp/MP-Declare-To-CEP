package com.cor.cep.util;

import java.util.UUID;
import org.eclipse.paho.client.mqttv3.*;


public class MqttConnector {

    public static MqttAsyncClient myClient;



    public static void main (String [] args) throws MqttException {
    }

    // Connect to the MQTT Broker
    public void startConnection(String channel) throws MqttException {
        
        myClient = new MqttAsyncClient("tcp://192.168.0.13:1883", UUID.randomUUID().toString());
        MyCallback myCallback = new MyCallback();
        myClient.setCallback(myCallback);

        IMqttToken token = myClient.connect();
        token.waitForCompletion();

        myClient.subscribe(channel, 0);
    }



}
