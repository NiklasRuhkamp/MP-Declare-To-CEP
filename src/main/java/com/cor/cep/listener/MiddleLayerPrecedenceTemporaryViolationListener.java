package com.cor.cep.listener;

import com.cor.cep.StartEngine;
import com.cor.cep.graphicalUserInterface.CreateContextScreen;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;


// Listener to mark temporary violated constraint
public class MiddleLayerPrecedenceTemporaryViolationListener implements UpdateListener {
    public void update(EventBean[] newEvents, EventBean[] oldEvents) 
    {
    EventBean event = newEvents[0];
    
    System.out.println(("********************Temporary Violation********************"));

    // Set the constraint from violation to fulfillment
    int x=0;
    int i = StartEngine.monitoringScreen.monitorList.getRowCount()-1;
        if(CreateContextScreen.segmented == true){
            if(StartEngine.monitoringScreen.monitorList.getValueAt(i, 0).equals(event.get("constraint")) && StartEngine.monitoringScreen.monitorList.getValueAt(i, 2).equals("activation") && StartEngine.monitoringScreen.monitorList.getValueAt(i, 3).equals(event.get("id"))){
                try {
                    if(!event.get("correlationActivation").equals(null)){
                        if(StartEngine.monitoringScreen.monitorList.getValueAt(i, 1).equals(event.get("correlationActivation"))){
                            StartEngine.monitoringScreen.monitorList.setValueAt("violation", i, 2);
                        }
                    } 
                } catch (Exception e) {
                    StartEngine.monitoringScreen.monitorList.setValueAt("violation", i, 2);
                }
            }
        }
        else if(StartEngine.monitoringScreen.monitorList.getValueAt(i, 0).equals(event.get("constraint")) && StartEngine.monitoringScreen.monitorList.getValueAt(i, 2).equals("activation")){
            try{
                if(!event.get("correlationActivation").equals(null)){
                    if(StartEngine.monitoringScreen.monitorList.getValueAt(i, 1).equals(event.get("correlationActivation"))){
                        StartEngine.monitoringScreen.monitorList.setValueAt("violation", i, 2);
                    }
                }
            } catch (Exception e){
                StartEngine.monitoringScreen.monitorList.setValueAt("violation", i, 2);

            }            
        }    
    }
}


