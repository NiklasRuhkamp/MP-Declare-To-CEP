package com.cor.cep.constraints;

import com.cor.cep.listener.MiddleLayerActivationListener;
import com.cor.cep.listener.MiddleLayerNotResponseFulfillmentToViolationListener;
import com.cor.cep.listener.MiddleLayerNotResponseTemporaryFulfillmentListener;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;

// Registration and Execution of Not Response Constraints

public class NotResponseStatementClass{

    public void startNotResponseStatement (EPServiceProvider epService, String notResponseActivationStatement, String notResponseViolationStatement, String notResponseTemporaryVulfillmentStatement, String reactToActivationStatement, String reactToViolationStatement, String reactToFulfillmentStatement){

        //Execute the EPL Query for Activaion
        epService.getEPAdministrator().createEPL(notResponseActivationStatement);

        //Execute the EPL Query for Target
        epService.getEPAdministrator().createEPL(notResponseTemporaryVulfillmentStatement);

        //Execute the EPL Query for Violation
        epService.getEPAdministrator().createEPL(notResponseViolationStatement);

        //Check for Activation
        EPStatement activationStatement = epService.getEPAdministrator().createEPL(reactToActivationStatement);

        //Attach a listener to activationStatement, to mark the activated Constraints
        MiddleLayerActivationListener activationListener = new MiddleLayerActivationListener();
        activationStatement.addListener(activationListener);
      
        //Set constraint to temporary fulfilled, as long as not violation occurs 
        EPStatement temporaryFulfillmentStatement = epService.getEPAdministrator().createEPL(reactToFulfillmentStatement);

        //Attach a listener to temporaryViolationStatement, to mark the fulfilled Constraints
        MiddleLayerNotResponseTemporaryFulfillmentListener temporaryFulfillmentListener = new MiddleLayerNotResponseTemporaryFulfillmentListener();
        temporaryFulfillmentStatement.addListener(temporaryFulfillmentListener);

        //Ceck for Violation 
        EPStatement violationStatement = epService.getEPAdministrator().createEPL(reactToViolationStatement);

        //Attach a listener to fulfillmentStatement, to mark the fulfilled Constraints
        MiddleLayerNotResponseFulfillmentToViolationListener violationListener = new MiddleLayerNotResponseFulfillmentToViolationListener();
        violationStatement.addListener(violationListener);


    }

}


