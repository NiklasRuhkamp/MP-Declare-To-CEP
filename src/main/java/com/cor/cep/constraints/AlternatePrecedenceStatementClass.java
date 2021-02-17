package com.cor.cep.constraints;

import com.cor.cep.listener.MiddleLayerActivationListener;
import com.cor.cep.listener.MiddleLayerAlternatePrecedenceViolationToFulfillmentListener;
import com.cor.cep.listener.MiddleLayerFulfillmentToViolationListener;
import com.cor.cep.listener.MiddleLayerPrecedenceTemporaryViolationListener;
import com.cor.cep.listener.MiddleLayerPrecedenceViolationListener;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;

// Registration and Execution of Alternate Precedence Constraints


public class AlternatePrecedenceStatementClass{

    public void startAlternatePrecedenceStatement (EPServiceProvider epService, String alternatPrecedenceActivationStatement, String alternatePrecedenceTargetStatement, String alternatePrecedenceViolationStatement, String alternatePrecedenceTemporaryViolation, String reactToActivationStatement, String reactToTemporaryViolationStatement, String reactToFulfillmentStatement, String reactToViolationStatement, String reactToFinalViolationStatement){
        
        //Execute the EPL Query for Activaion
        epService.getEPAdministrator().createEPL(alternatPrecedenceActivationStatement);

        //EPL Query to set the constraint as violated, as long as no fulfillment occurs
        epService.getEPAdministrator().createEPL(alternatePrecedenceTemporaryViolation);

        //Execute the EPL Query for Target
        epService.getEPAdministrator().createEPL(alternatePrecedenceTargetStatement);

        //Execute the EPL Query for Violation
        epService.getEPAdministrator().createEPL(alternatePrecedenceViolationStatement);

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
        EPStatement finalViolationStatement = epService.getEPAdministrator().createEPL(reactToFinalViolationStatement);

        //Attach a listener to violationStatement, to mark the violated Constraints
        MiddleLayerPrecedenceViolationListener finalViolationListener = new MiddleLayerPrecedenceViolationListener();
        finalViolationStatement.addListener(finalViolationListener);

        //Ceck for Target 
        EPStatement fulfillmentStatement = epService.getEPAdministrator().createEPL(reactToFulfillmentStatement);

        //Attach a listener to fulfillmentStatement, to mark the fulfilled Constraints
        MiddleLayerAlternatePrecedenceViolationToFulfillmentListener fulfillmentListener = new MiddleLayerAlternatePrecedenceViolationToFulfillmentListener();
        fulfillmentStatement.addListener(fulfillmentListener);

        //Ceck for Violation 
        EPStatement violationStatement = epService.getEPAdministrator().createEPL(reactToViolationStatement);

        //Attach a listener to violationStatement, to mark the violated Constraints
        MiddleLayerFulfillmentToViolationListener violationListener = new MiddleLayerFulfillmentToViolationListener();
        violationStatement.addListener(violationListener);
            

    }

}


