package com.cor.cep.util;


import java.util.UUID;



import com.cor.cep.event.ButtonEvent;
import com.cor.cep.event.DistanceEvent;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;

import org.eclipse.paho.client.mqttv3.*;


public class MyCallback implements MqttCallback{

    public static MqttAsyncClient myClient;

    public static void main (String [] args) throws MqttException {

        myClient = new MqttAsyncClient("tcp//:192.168.00.20", UUID.randomUUID().toString());

    }

    @Override
    public void connectionLost(Throwable cause) {
        // TODO Auto-generated method stub

    }

    @Override
    // Handle arriving messages
    public void messageArrived(String topic, MqttMessage message) throws Exception {

        final EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();

        if(topic.equals("distanceEvent_Stream")){

            // Parse Mqtt-Message into Integer
            System.out.println(message.toString());
            int distance = Integer.parseInt(message.toString());
            
            // Send DistanceEvent to the Engine
            epService.getEPRuntime().sendEvent(new DistanceEvent(distance));
        }
        else if(topic.equals("buttonEvent_Stream")){

            // Parse Mqtt-Message into Integer
            System.out.println(message.toString());
            int number = Integer.parseInt(message.toString()); 

            // Send ButtonEvent to the Engine
            epService.getEPRuntime().sendEvent(new ButtonEvent(number));
        }


    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // TODO Auto-generated method stub

    }


}
