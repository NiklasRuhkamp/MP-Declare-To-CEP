package com.cor.cep.listener;

import com.cor.cep.StartEngine;
import com.cor.cep.graphicalUserInterface.CreateContextScreen;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;


// Listener to set fulfilled constraints to violation
public class MiddleLayerNotChainPrecedenceFulfillmentToViolationListener implements UpdateListener {
    public void update(EventBean[] newEvents, EventBean[] oldEvents) 
    {
    EventBean event = newEvents[0];
    
    System.out.println(("********************Violation********************"));

    // Set the constraint from fulfillment to violation
    for(int i = StartEngine.monitoringScreen.monitorList.getRowCount()-1; i>=0; i--){
        if(CreateContextScreen.segmented == true){
            if(StartEngine.monitoringScreen.monitorList.getValueAt(i, 0).equals(event.get("constraint")) && StartEngine.monitoringScreen.monitorList.getValueAt(i, 2).equals("fulfillment") && StartEngine.monitoringScreen.monitorList.getValueAt(i, 3).equals(event.get("id"))){
                try {
                    if(!event.get("correlationActivation").equals(null)){
                        if(StartEngine.monitoringScreen.monitorList.getValueAt(i, 1).equals(event.get("correlationActivation"))){
                            StartEngine.monitoringScreen.monitorList.setValueAt("violation", i, 2);
                            // React to violation
                            // StartEngine.reactToViolation(Integer.parseInt(event.get("id").toString()), event.get("constraint").toString(), event.get("actOrTar").toString(), event.get("correlationActivation").toString());
                            i=0;
                        }
                    } 
                } catch (Exception e) {
                    StartEngine.monitoringScreen.monitorList.setValueAt("violation", i, 2);
                    // React to violation
                    // StartEngine.reactToViolation(Integer.parseInt(event.get("id").toString()), event.get("constraint").toString(), event.get("actOrTar").toString(), event.get("correlationActivation").toString());
                    i=0;
                }
            }
        }
        else if(StartEngine.monitoringScreen.monitorList.getValueAt(i, 0).equals(event.get("constraint")) && StartEngine.monitoringScreen.monitorList.getValueAt(i, 2).equals("fulfillment")){
            try{
                if(!event.get("correlationActivation").equals(null)){
                    if(StartEngine.monitoringScreen.monitorList.getValueAt(i, 1).equals(event.get("correlationActivation"))){
                        StartEngine.monitoringScreen.monitorList.setValueAt("violation", i, 2);
                        // React to violation
                        // StartEngine.reactToViolation(Integer.parseInt(event.get("id").toString()), event.get("constraint").toString(), event.get("actOrTar").toString(), event.get("correlationActivation").toString());
                        i=0;
                    }
                }
            } catch (Exception e){
                StartEngine.monitoringScreen.monitorList.setValueAt("violation", i, 2);
                // React to violation
                // StartEngine.reactToViolation(Integer.parseInt(event.get("id").toString()), event.get("constraint").toString(), event.get("actOrTar").toString(), event.get("correlationActivation").toString());
                i=0;
            }            
        } 
    }
    }
}


