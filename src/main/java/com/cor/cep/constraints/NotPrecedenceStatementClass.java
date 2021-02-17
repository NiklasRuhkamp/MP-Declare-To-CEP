package com.cor.cep.constraints;

import com.cor.cep.listener.MiddleLayerActivationListener;
import com.cor.cep.listener.MiddleLayerFulfillmentListener;
import com.cor.cep.listener.MiddleLayerNotChainPrecedenceFulfillmentToViolationListener;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;

// Registration and Execution of Not Precedence Constraints

public class NotPrecedenceStatementClass{

    public void startNotPrecedenceStatement (EPServiceProvider epService, String notPrecedenceActivationStatement, String notPrecedenceFulfillmentStatement, String notPrecedenceViolationStatement, String reactToActivationStatement, String reactToFulfillmentStatement, String reactToViolationStatement){
        
        //Execute the EPL Query for Activaion
        epService.getEPAdministrator().createEPL(notPrecedenceActivationStatement);

        //Execute the EPL Query for Fulfillment
        epService.getEPAdministrator().createEPL(notPrecedenceFulfillmentStatement);

        //Execute the EPL Query for Violation
        epService.getEPAdministrator().createEPL(notPrecedenceViolationStatement);

        //Check for Activation
        EPStatement activationStatement = epService.getEPAdministrator().createEPL(reactToActivationStatement);

        //Attach a listener to activationStatement, to mark the activated Constraints
        MiddleLayerActivationListener activationListener = new MiddleLayerActivationListener();
        activationStatement.addListener(activationListener);
    
        //Ceck for Fulfillment 
        EPStatement fulfillmentStatement = epService.getEPAdministrator().createEPL(reactToFulfillmentStatement);

        //Attach a listener to violationStatement, to mark the fulfilled Constraints
        MiddleLayerFulfillmentListener fulfillmentListener = new MiddleLayerFulfillmentListener();
        fulfillmentStatement.addListener(fulfillmentListener);

        //Ceck for Violation 
        EPStatement violationStatement = epService.getEPAdministrator().createEPL(reactToViolationStatement);

        //Attach a listener to violationStatement, to mark the fulfilled Constraints
        MiddleLayerNotChainPrecedenceFulfillmentToViolationListener violationListener = new MiddleLayerNotChainPrecedenceFulfillmentToViolationListener();
        violationStatement.addListener(violationListener);

    }

}


