package com.cor.cep.listener;

import com.cor.cep.StartEngine;
import com.cor.cep.graphicalUserInterface.CreateContextScreen;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;


// Listener to mark temporary violated constraint
public class MiddleLayerChainPrecedenceTemporaryViolatedListener implements UpdateListener {
    public void update(EventBean[] newEvents, EventBean[] oldEvents) 
    {
    EventBean event = newEvents[0];
    
    System.out.println(("********************Temporary Violation********************"));

    // Set the constraint from activation to volation
    Object[] activation = null;
    if(CreateContextScreen.segmented == true){
        try {
            if(!event.get("correlationActivation").equals(null)){
                activation = new Object[]{event.get("constraint"), event.get("correlationActivation"), "temporary violation", event.get("id")};
            }
        } catch (Exception e) {
            activation = new Object[]{event.get("constraint"), "", "temporary violation", event.get("id")};
        }
    }
    else{
        try {
            if(!event.get("correlationActivation").equals(null)){
                activation = new Object[]{event.get("constraint"), event.get("correlationActivation"), "temporary violation"};
            }
        } catch (Exception e) {
            activation = new Object[]{event.get("constraint"), "", "temporary violation"};
        }

    }
    
    StartEngine.monitoringScreen.monitorList.addRow(activation);

    }


}


