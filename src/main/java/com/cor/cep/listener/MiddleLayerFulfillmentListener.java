package com.cor.cep.listener;

import com.cor.cep.StartEngine;
import com.cor.cep.graphicalUserInterface.CreateContextScreen;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;


// Listener to mark fulfilled constraint
public class MiddleLayerFulfillmentListener implements UpdateListener {
    public void update(EventBean[] newEvents, EventBean[] oldEvents) 
    {
    EventBean event = newEvents[0];
    
    System.out.println(("********************Fulfillment********************"));

    // Set the constraint from activation to fulfillment
    int x=0;
    for(int i = StartEngine.monitoringScreen.monitorList.getRowCount(); i>0; i--){
        if(CreateContextScreen.segmented == true){
            if(StartEngine.monitoringScreen.monitorList.getValueAt(x, 0).equals(event.get("constraint")) && StartEngine.monitoringScreen.monitorList.getValueAt(x, 2).equals("activation") && StartEngine.monitoringScreen.monitorList.getValueAt(x, 3).equals(event.get("id"))){
                try {
                    if(!event.get("correlationActivation").equals(null)){
                        if(StartEngine.monitoringScreen.monitorList.getValueAt(x, 1).equals(event.get("correlationActivation"))){
                            StartEngine.monitoringScreen.monitorList.setValueAt("fulfillment", x, 2);
                            i=0;
                        }
                    } 
                } catch (Exception e) {
                    StartEngine.monitoringScreen.monitorList.setValueAt("fulfillment", x, 2);
                    i=0;
                }
            }
        }
        else if(StartEngine.monitoringScreen.monitorList.getValueAt(x, 0).equals(event.get("constraint")) && StartEngine.monitoringScreen.monitorList.getValueAt(x, 2).equals("activation")){
            try{
                if(!event.get("correlationActivation").equals(null)){
                    if(StartEngine.monitoringScreen.monitorList.getValueAt(x, 1).equals(event.get("correlationActivation"))){
                        StartEngine.monitoringScreen.monitorList.setValueAt("fulfillment", x, 2);
                        i=0;
                    }
                }
            } catch (Exception e){
                StartEngine.monitoringScreen.monitorList.setValueAt("fulfillment", x, 2);
                i=0;
            }            
        } 
        x++;
    }
    }
}


