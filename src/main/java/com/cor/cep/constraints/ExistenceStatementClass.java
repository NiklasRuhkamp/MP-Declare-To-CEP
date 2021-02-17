package com.cor.cep.constraints;

import com.cor.cep.listener.MiddleLayerActivationListener;
import com.cor.cep.listener.MiddleLayerResponseFulfillmentListener;
import com.cor.cep.listener.MiddleLayerViolationListener;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;

// Registration and Execution of Existence

public class ExistenceStatementClass{

    public void startExistenceStatement (EPServiceProvider epService, String existenceActivation, String existenceTargetStatement, String existenceTargetPastReactionQuery, String reactToActivationStatement, String reactToFulfillmentStatement, String reactToTimeSpanViolationStatement){

        //Execute the EPL Query for Activation
        epService.getEPAdministrator().createEPL(existenceActivation);

        //Execute the EPL Query for Target
        epService.getEPAdministrator().createEPL(existenceTargetStatement);
        epService.getEPAdministrator().createEPL(existenceTargetPastReactionQuery);


        //Check for Activation
        EPStatement activationStatement = epService.getEPAdministrator().createEPL(reactToActivationStatement);

        //Attach a listener to activationStatement, to mark the activated Constraints
        MiddleLayerActivationListener activationListener = new MiddleLayerActivationListener();
        activationStatement.addListener(activationListener);

        //Ceck for Target 
        EPStatement fulfillmentStatement = epService.getEPAdministrator().createEPL(reactToFulfillmentStatement);

        //Attach a listener to fulfillmentStatement, to mark the fulfilled Constraints
        MiddleLayerResponseFulfillmentListener fulfillmentListener = new MiddleLayerResponseFulfillmentListener();
        fulfillmentStatement.addListener(fulfillmentListener);

        if(!reactToTimeSpanViolationStatement.isEmpty()){
            
            //Ceck for TimeSpan Violation 
            EPStatement timeSpanViolationStatmenet = epService.getEPAdministrator().createEPL(reactToTimeSpanViolationStatement);
            
            //Attach a listener to timeSpanViolationStatmenet, to mark the violated Constraints
            MiddleLayerViolationListener timeSpanViolationListener = new MiddleLayerViolationListener();
            timeSpanViolationStatmenet.addListener(timeSpanViolationListener);
            
        }
    }

}


