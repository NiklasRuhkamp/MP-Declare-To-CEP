package com.cor.cep.constraints;

import com.cor.cep.listener.MiddleLayerActivationListener;
import com.cor.cep.listener.MiddleLayerPrecedenceFulfillmentListener;
import com.cor.cep.listener.MiddleLayerPrecedenceTemporaryViolationListener;
import com.cor.cep.listener.MiddleLayerPrecedenceViolationListener;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;


// Registration and Execution of Precedence Constraints

public class PrecedenceStatementClass{

    public void startPrecedenceStatement (EPServiceProvider epService, String precedenceActivationStatement, String precedenceTargetStatement, String precedenceViolationStatement, String reactToActivationStatement, String reactToFulfillmentStatement, String reactToViolationStatement, String reactToTemporaryViolationStatement){
        
        //Execute the EPL Query for Activaion
        epService.getEPAdministrator().createEPL(precedenceActivationStatement);

        //EPL Query to set the constraint as violated, as long as no fulfillment occurs
        epService.getEPAdministrator().createEPL(precedenceViolationStatement);

        //Execute the EPL Query for Target
        epService.getEPAdministrator().createEPL(precedenceTargetStatement);

        //Check for Activation
        EPStatement activationStatement = epService.getEPAdministrator().createEPL(reactToActivationStatement);

        //Attach a listener to activationStatement, to mark the activated Constraints
        MiddleLayerActivationListener activationListener = new MiddleLayerActivationListener();
        activationStatement.addListener(activationListener);
       
        //Set constraint to temporary violated, as long as fulfillment occurs 
        EPStatement temporaryViolationStatement = epService.getEPAdministrator().createEPL(reactToTemporaryViolationStatement);

        //Attach a listener to temporaryViolationStatement, to mark the violated Constraints
        MiddleLayerPrecedenceTemporaryViolationListener temporaryViolationListener = new MiddleLayerPrecedenceTemporaryViolationListener();
        temporaryViolationStatement.addListener(temporaryViolationListener);

        //react to the violation, if no fulfillment occurs next
        EPStatement violationStatement = epService.getEPAdministrator().createEPL(reactToViolationStatement);

        //Attach a listener to violationStatement, to mark the violated Constraints
        MiddleLayerPrecedenceViolationListener violationListener = new MiddleLayerPrecedenceViolationListener();
        violationStatement.addListener(violationListener);

        //Ceck for Target 
        EPStatement fulfillmentStatement = epService.getEPAdministrator().createEPL(reactToFulfillmentStatement);

        //Attach a listener to fulfillmentStatement, to mark the fulfilled Constraints
        MiddleLayerPrecedenceFulfillmentListener fulfillmentListener = new MiddleLayerPrecedenceFulfillmentListener();
        fulfillmentStatement.addListener(fulfillmentListener);

    }

}


