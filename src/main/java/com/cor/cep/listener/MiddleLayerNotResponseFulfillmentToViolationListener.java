package com.cor.cep.listener;

import com.cor.cep.StartEngine;
import com.cor.cep.graphicalUserInterface.CreateContextScreen;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;


// Listener to set fulfilled constraints to violation
public class MiddleLayerNotResponseFulfillmentToViolationListener implements UpdateListener {
    public void update(EventBean[] newEvents, EventBean[] oldEvents) 
    {
    EventBean event = newEvents[0];
    
    System.out.println(("********************Violation********************"));

    // Set the constraint from violation to fulfillment
    int x=0;
    for(int i = StartEngine.monitoringScreen.monitorList.getRowCount(); i>0; i--){
        if(CreateContextScreen.segmented == true){
            if(StartEngine.monitoringScreen.monitorList.getValueAt(x, 0).equals(event.get("constraint")) && StartEngine.monitoringScreen.monitorList.getValueAt(x, 2).equals("fulfillment") && StartEngine.monitoringScreen.monitorList.getValueAt(x, 3).equals(event.get("id"))){
                try {
                    if(!event.get("correlationActivation").equals(null)){
                        if(StartEngine.monitoringScreen.monitorList.getValueAt(x, 1).equals(event.get("correlationActivation"))){
                            StartEngine.monitoringScreen.monitorList.setValueAt("violation", x, 2);
                            // React to violation
                            // StartEngine.reactToViolation(Integer.parseInt(event.get("id").toString()), event.get("constraint").toString(), event.get("actOrTar").toString(), event.get("correlationActivation").toString());
                          
                        }
                    } 
                } catch (Exception e) {
                    StartEngine.monitoringScreen.monitorList.setValueAt("violation", x, 2);
                    // React to violation
                    // StartEngine.reactToViolation(Integer.parseInt(event.get("id").toString()), event.get("constraint").toString(), event.get("actOrTar").toString(), event.get("correlationActivation").toString());
                }
            }
        }
        else if(StartEngine.monitoringScreen.monitorList.getValueAt(x, 0).equals(event.get("constraint")) && StartEngine.monitoringScreen.monitorList.getValueAt(x, 2).equals("fulfillment")){
            try{
                if(!event.get("correlationActivation").equals(null)){
                    if(StartEngine.monitoringScreen.monitorList.getValueAt(x, 1).equals(event.get("correlationActivation"))){
                        StartEngine.monitoringScreen.monitorList.setValueAt("violation", x, 2);
                        // React to violation
                        // StartEngine.reactToViolation(Integer.parseInt(event.get("id").toString()), event.get("constraint").toString(), event.get("actOrTar").toString(), event.get("correlationActivation").toString());
                    }
                }
            } catch (Exception e){
                StartEngine.monitoringScreen.monitorList.setValueAt("violation", x, 2);
                // React to violation
                // StartEngine.reactToViolation(Integer.parseInt(event.get("id").toString()), event.get("constraint").toString(), event.get("actOrTar").toString(), event.get("correlationActivation").toString());
            }            
        } 
        x++;
    }
    }
}


