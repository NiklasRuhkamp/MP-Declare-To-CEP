package com.cor.cep.listener;

import com.cor.cep.StartEngine;
import com.cor.cep.graphicalUserInterface.CreateContextScreen;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;


// Listener to insert fulfilled constraint
public class MiddleLayerActivationListener implements UpdateListener {
    public void update(EventBean[] newEvents, EventBean[] oldEvents) 
    {
    EventBean event = newEvents[0];
    
    System.out.println(("********************Activation********************"));

    // Insert the activated constraint into the monitorList to display on the monitoringScreen
    Object[] activation = null;
    if(CreateContextScreen.segmented == true){
        try {
            if(!event.get("correlationActivation").equals(null)){
                activation = new Object[]{event.get("constraint"), event.get("correlationActivation"), event.get("actOrTar"), event.get("id")};
            }
        } catch (Exception e) {
            activation = new Object[]{event.get("constraint"), "", event.get("actOrTar"), event.get("id")};
        }
    }
    else{
        try {
            if(!event.get("correlationActivation").equals(null)){
                activation = new Object[]{event.get("constraint"), event.get("correlationActivation"), event.get("actOrTar")};
            }
        } catch (Exception e) {
            activation = new Object[]{event.get("constraint"), "", event.get("actOrTar")};
        }

    }
    
    StartEngine.monitoringScreen.monitorList.addRow(activation);

    }


}


