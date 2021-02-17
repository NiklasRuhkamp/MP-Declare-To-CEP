package com.cor.cep.constraints;

import com.cor.cep.listener.MiddleLayerActivationListener;
import com.cor.cep.listener.MiddleLayerFulfillmentListener;
import com.cor.cep.listener.MiddleLayerNotChainPrecedenceFulfillmentToViolationListener;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;

// Registration and Execution of Not Chain Precedence Constraints


public class NotChainPrecedenceStatementClass {

    public void startNotChainPrecedenceStatement (EPServiceProvider epService, String notChainPrecedenceActivationStatement, String notChainPrecedenceTargetStatement, String notChainPrecedenceViolationStatement, String reactToActivationStatement, String reactToFulfillmentStatement, String reactToViolationStatement){
        
        //Execute the EPL Query for Activaion
        epService.getEPAdministrator().createEPL(notChainPrecedenceActivationStatement);

        //Execute the EPL Query for Target
        epService.getEPAdministrator().createEPL(notChainPrecedenceTargetStatement);
        
        //Execute the EPL Query for Violation
        epService.getEPAdministrator().createEPL(notChainPrecedenceViolationStatement);

        
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
        MiddleLayerNotChainPrecedenceFulfillmentToViolationListener violationListener = new MiddleLayerNotChainPrecedenceFulfillmentToViolationListener();
        violationStatement.addListener(violationListener);
    }

}


