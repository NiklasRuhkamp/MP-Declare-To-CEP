package com.cor.cep.constraints;

import com.cor.cep.listener.MiddleLayerActivationListener;
import com.cor.cep.listener.MiddleLayerFulfillmentListener;
import com.cor.cep.listener.MiddleLayerViolationListener;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;

// Registration and Execution of Chain Response Constraints


public class ChainResponseStatementClass {

    public void startChainResponseStatement (EPServiceProvider epService, String chainResponseActivationStatement, String chainResponseTargetStatement, String chainResponseViolationStatement, String reactToActivationStatement, String reactToFulfillmentStatement, String reactToViolationStatement, String reactToTimeSpanViolationStatement){
      
        //Execute the EPL Query for Activaion
        epService.getEPAdministrator().createEPL(chainResponseActivationStatement);

        //Execute the EPL Query for Target
        epService.getEPAdministrator().createEPL(chainResponseTargetStatement);

        //Execute the EPL Query for Violation
        epService.getEPAdministrator().createEPL(chainResponseViolationStatement);    

        //Check for Activation
        EPStatement activationStatement = epService.getEPAdministrator().createEPL(reactToActivationStatement);

        //Attach a listener to activationStatement, to mark the activated Constraints
        MiddleLayerActivationListener activationListener = new MiddleLayerActivationListener();
        activationStatement.addListener(activationListener);

        //Ceck for Target 
        EPStatement fulfillmentStatement = epService.getEPAdministrator().createEPL(reactToFulfillmentStatement);

        //Attach a listener to fulfillmentStatement, to mark the fulfilled Constraints
        MiddleLayerFulfillmentListener fulfillmentListener = new MiddleLayerFulfillmentListener();
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


