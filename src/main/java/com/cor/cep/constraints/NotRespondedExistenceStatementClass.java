package com.cor.cep.constraints;

import com.cor.cep.listener.MiddleLayerActivationListener;
import com.cor.cep.listener.MiddleLayerNotResponseFulfillmentToViolationListener;
import com.cor.cep.listener.MiddleLayerNotResponseTemporaryFulfillmentListener;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;

// Registration and Execution of Not Reponded Existence Constraints


public class NotRespondedExistenceStatementClass{

    public void startNotRespondedExistenceStatement (EPServiceProvider epService, String notRespondedExistenceActivationStatement, String notResponededExistenceFutureViolationStatement, String notResponededExistencePastViolationStatement, String notResponededExistenceTemporaryFulfillment, String reactToActivationStatement, String reactToFulfillmentStatement, String reactToViolationStatement){
        
        //Execute the EPL Query for Activaion
        epService.getEPAdministrator().createEPL(notRespondedExistenceActivationStatement);

        //Execute the EPL Query for Target
        epService.getEPAdministrator().createEPL(notResponededExistenceTemporaryFulfillment);

        //Execute the EPL Query for Violation in the Future
        epService.getEPAdministrator().createEPL(notResponededExistenceFutureViolationStatement);

        //Execute the EPL Query for Violation in the Past
        epService.getEPAdministrator().createEPL(notResponededExistencePastViolationStatement);

        
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

        //Attach a listener to violationStatement, to mark the violated Constraints
        MiddleLayerNotResponseFulfillmentToViolationListener violationListener = new MiddleLayerNotResponseFulfillmentToViolationListener();
        violationStatement.addListener(violationListener);
    
    }

}


