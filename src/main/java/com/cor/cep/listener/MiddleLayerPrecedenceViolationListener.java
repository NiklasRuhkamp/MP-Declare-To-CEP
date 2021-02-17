package com.cor.cep.listener;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;


// Listener to mark violated constraint
public class MiddleLayerPrecedenceViolationListener implements UpdateListener {
    public void update(EventBean[] newEvents, EventBean[] oldEvents) 
    {
    
    System.out.println(("{{{{{{{{{{{ALERT}}}}}}}}}}"));
    // React to violation
    // StartEngine.reactToViolation(Integer.parseInt(event.get("id").toString()), event.get("constraint").toString(), event.get("actOrTar").toString(), event.get("correlationActivation").toString());
    
    }

}


