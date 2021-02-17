package com.cor.cep.constraints;

import com.cor.cep.listener.MiddleLayerActivationListener;
import com.cor.cep.listener.MiddleLayerAlternateResponseFulfillmentListener;
import com.cor.cep.listener.MiddleLayerViolationListener;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;

// Registration and Execution of Alternate Reponse Constraints


public class AlternateResponseStatementClass{

    public void startAlternateResponseStatement (EPServiceProvider epService, String alternatResponseActivationStatement, String alternateResponseTargetStatement, String alternateResponseViolationStatement, String reactToActivationStatement, String reactToFulfillmentStatement, String reactToViolationStatement, String reactToTimeSpanViolationStatement){
        
        //Execute the EPL Query for Activaion
        epService.getEPAdministrator().createEPL(alternatResponseActivationStatement);

        //Execute the EPL Query for Target
        epService.getEPAdministrator().createEPL(alternateResponseTargetStatement);

        //Execute the EPL Query for Violation
        epService.getEPAdministrator().createEPL(alternateResponseViolationStatement);

        //Check for Activation
        EPStatement activationStatement = epService.getEPAdministrator().createEPL(reactToActivationStatement);

        //Attach a listener to activationStatement, to mark the activated Constraints
        MiddleLayerActivationListener activationListener = new MiddleLayerActivationListener();
        activationStatement.addListener(activationListener);

        //Ceck for Target 
        EPStatement fulfillmentStatement = epService.getEPAdministrator().createEPL(reactToFulfillmentStatement);

        //Attach a listener to fulfillmentStatement, to mark the fulfilled Constraints
        MiddleLayerAlternateResponseFulfillmentListener fulfillmentListener = new MiddleLayerAlternateResponseFulfillmentListener();
        fulfillmentStatement.addListener(fulfillmentListener);

        //Ceck for Violation 
        EPStatement violationStatement = epService.getEPAdministrator().createEPL(reactToViolationStatement);

        //Attach a listener to violationStatement, to mark the violated Constraints
        MiddleLayerViolationListener violationListener = new MiddleLayerViolationListener();
        violationStatement.addListener(violationListener);

        if(!reactToTimeSpanViolationStatement.isEmpty()){
            
            //Ceck for TimeSpan Violation 
            EPStatement timeSpanViolationStatmenet = epService.getEPAdministrator().createEPL(reactToTimeSpanViolationStatement);
            
            //Attach a listener to timeSpanViolationStatmenet, to mark the violated Constraints
            MiddleLayerViolationListener timeSpanViolationListener = new MiddleLayerViolationListener();
            timeSpanViolationStatmenet.addListener(timeSpanViolationListener);
            
        }
    }

}


