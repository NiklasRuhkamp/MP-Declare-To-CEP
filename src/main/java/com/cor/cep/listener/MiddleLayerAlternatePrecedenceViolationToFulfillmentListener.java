package com.cor.cep.listener;

import com.cor.cep.StartEngine;
import com.cor.cep.graphicalUserInterface.CreateContextScreen;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;


// Listener to set temporary violated constraints to fulfilled
public class MiddleLayerAlternatePrecedenceViolationToFulfillmentListener implements UpdateListener {
    public void update(EventBean[] newEvents, EventBean[] oldEvents) 
    {
    EventBean event = newEvents[0];
    
    System.out.println(("********************Fulfillment********************"));

    // Set the constraint from violation to fulfillment
    for(int i = StartEngine.monitoringScreen.monitorList.getRowCount()-1; i>=0; i--){
        if(CreateContextScreen.segmented == true){
            if(StartEngine.monitoringScreen.monitorList.getValueAt(i, 0).equals(event.get("constraint")) && StartEngine.monitoringScreen.monitorList.getValueAt(i, 2).equals("violation") && StartEngine.monitoringScreen.monitorList.getValueAt(i, 3).equals(event.get("id"))){
                try {
                    if(!event.get("correlationActivation").equals(null)){
                        if(StartEngine.monitoringScreen.monitorList.getValueAt(i, 1).equals(event.get("correlationActivation"))){
                            StartEngine.monitoringScreen.monitorList.setValueAt("fulfillment", i, 2);
                            i=0;
                        }
                    } 
                } catch (Exception e) {
                    StartEngine.monitoringScreen.monitorList.setValueAt("fulfillment", i, 2);
                    i=0;
                }
            }
        }
        else if(StartEngine.monitoringScreen.monitorList.getValueAt(i, 0).equals(event.get("constraint")) && StartEngine.monitoringScreen.monitorList.getValueAt(i, 2).equals("violation")){
            try{
                if(!event.get("correlationActivation").equals(null)){
                    if(StartEngine.monitoringScreen.monitorList.getValueAt(i, 1).equals(event.get("correlationActivation"))){
                        StartEngine.monitoringScreen.monitorList.setValueAt("fulfillment", i, 2);
                        i=0;
                    }
                }
            } catch (Exception e){
                StartEngine.monitoringScreen.monitorList.setValueAt("fulfillment", i, 2);
                i=0;
            }            
        }
    }
    }
}


