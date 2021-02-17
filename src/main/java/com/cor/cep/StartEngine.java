package com.cor.cep;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.cor.cep.constraints.AlternatePrecedenceStatementClass;
import com.cor.cep.constraints.AlternateResponseStatementClass;
import com.cor.cep.constraints.ChainPrecedenceStatementClass;
import com.cor.cep.constraints.ChainResponseStatementClass;
import com.cor.cep.constraints.ExistenceStatementClass;
import com.cor.cep.constraints.NotChainPrecedenceStatementClass;
import com.cor.cep.constraints.NotChainResponseStatementClass;
import com.cor.cep.constraints.NotPrecedenceStatementClass;
import com.cor.cep.constraints.NotRespondedExistenceStatementClass;
import com.cor.cep.constraints.NotResponseStatementClass;
import com.cor.cep.constraints.PrecedenceStatementClass;
import com.cor.cep.constraints.RespondedExistenceStatementClass;
import com.cor.cep.constraints.ResponseStatementClass;
import com.cor.cep.event.MachineFailureEvent;
import com.cor.cep.event.OrderMaintenanceEvent;
import com.cor.cep.graphicalUserInterface.AddConstraintScreen;
import com.cor.cep.graphicalUserInterface.ConstraintBuilder;
import com.cor.cep.graphicalUserInterface.MonitoringScreen;
import com.cor.cep.util.MqttConnector;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;

public class StartEngine {

    private static Logger LOG = LoggerFactory.getLogger(StartEngine.class);
    public static EPServiceProvider epService;
    public static MonitoringScreen monitoringScreen;

    public static void main(String[] args, DefaultTableModel buildedConstraints) {

        LOG.debug("Starting...");

        /*
        // Start connection to MQTT broker
        MqttConnector mqttConnectorBtnStream = new MqttConnector();
        try {
            mqttConnectorBtnStream.startConnection("buttonEvent_Stream");
        } catch (MqttException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
       
        MqttConnector mqttConnectorDstnStream = new MqttConnector();
        try {
            mqttConnectorDstnStream.startConnection("distanceEvent_Stream");
        } catch (MqttException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        */
    
        // Open MonitoringScreen
        monitoringScreen = new MonitoringScreen();
        monitoringScreen.setVisible(true);


        // Get an engine instance
        final EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();

        // Register the event type
        epService.getEPAdministrator().getConfiguration().addEventType(com.cor.cep.event.MachineFailureEvent.class);
        epService.getEPAdministrator().getConfiguration().addEventType(com.cor.cep.event.OrderMaintenanceEvent.class);
        epService.getEPAdministrator().getConfiguration().addEventType(com.cor.cep.event.ButtonEvent.class);
        epService.getEPAdministrator().getConfiguration().addEventType(com.cor.cep.event.DistanceEvent.class);

    

        // Create middle level stream
        epService.getEPAdministrator().createEPL(
                "create schema middleLayer (id integer, constraint String, actOrTar String, correlationActivation String)");

        
        // Create context SegmentedById, if the process has multiple instances
        if (ConstraintBuilder.createContextStatement != null) {

            epService.getEPAdministrator().createEPL(ConstraintBuilder.createContextStatement);

        }

        try {
            // For-loop to register every constraint
            for (int i = 0; i < buildedConstraints.getRowCount(); i++) {

                // If-else statement to check, which constraint class has to be used
                // Existence
                if (buildedConstraints.getValueAt(i, 0).equals("Existence")) {

                    String existenceActivationStatement = buildedConstraints.getValueAt(i, 1).toString();
                    String existenceTargetStatement = buildedConstraints.getValueAt(i, 2).toString();
                    String existenceTargetPastReactionQuery = buildedConstraints.getValueAt(i, 3).toString();
                    String reactToActivationStatement = buildedConstraints.getValueAt(i,4).toString();
                    String reactToTargetStatement = buildedConstraints.getValueAt(i,5).toString();
                    String reactToTimeSpanViolationStatement = buildedConstraints.getValueAt(i,7).toString();

                    ExistenceStatementClass existenceStatement = new ExistenceStatementClass();
                    existenceStatement.startExistenceStatement(epService, existenceActivationStatement,
                    existenceTargetStatement, existenceTargetPastReactionQuery, reactToActivationStatement, reactToTargetStatement, reactToTimeSpanViolationStatement);

                }

                // Response
                else if (buildedConstraints.getValueAt(i, 0).equals("Response")) {

                    String responseActivationStatement = buildedConstraints.getValueAt(i, 1).toString();
                    String responseTargetStatement = buildedConstraints.getValueAt(i, 2).toString();
                    String reactToActivationStatement = buildedConstraints.getValueAt(i, 5).toString();
                    String reactToFulfillmentStatement = buildedConstraints.getValueAt(i, 6).toString();
                    String reactToTimeSpanViolationStatement = buildedConstraints.getValueAt(i, 7).toString();


                    ResponseStatementClass responseStatement = new ResponseStatementClass();
                    responseStatement.startResponseStatement(epService, responseActivationStatement,
                            responseTargetStatement, reactToActivationStatement, reactToFulfillmentStatement, reactToTimeSpanViolationStatement);

                }

                // Precedence
                else if (buildedConstraints.getValueAt(i, 0).equals("Precedence")) {

                    String precedenceActivationStatement = buildedConstraints.getValueAt(i, 1).toString();
                    String precedenceTargetStatement = buildedConstraints.getValueAt(i, 2).toString();
                    String precedenceViolationStatement = buildedConstraints.getValueAt(i, 3).toString();
                    String reactToActivationStatement = buildedConstraints.getValueAt(i, 4).toString();
                    String reactToFulfillmentStatement = buildedConstraints.getValueAt(i, 5).toString();
                    String reactToTemporaryViolationStatement = buildedConstraints.getValueAt(i, 6).toString();
                    String reactToViolationStatement = buildedConstraints.getValueAt(i, 7).toString();


                    PrecedenceStatementClass precedenceStatement = new PrecedenceStatementClass();
                    precedenceStatement.startPrecedenceStatement(epService, precedenceActivationStatement,
                            precedenceTargetStatement, precedenceViolationStatement,
                            reactToActivationStatement,
                            reactToFulfillmentStatement, reactToViolationStatement, reactToTemporaryViolationStatement);

                }

                // Alternate Response
                else if (buildedConstraints.getValueAt(i, 0).equals("Alternate Response")) {

                    String alternatResponseActivationStatement = buildedConstraints.getValueAt(i, 1).toString();
                    String alternateResponseTargetStatement = buildedConstraints.getValueAt(i, 2).toString();
                    String alternateResponseViolationStatement = buildedConstraints.getValueAt(i, 3).toString();
                    String reactToActivationStatement = buildedConstraints.getValueAt(i, 5).toString();
                    String reactToFulfillmentStatement = buildedConstraints.getValueAt(i, 6).toString();
                    String reactToViolationStatement = buildedConstraints.getValueAt(i, 7).toString();
                    String reactToTimeSpanViolationStatement = buildedConstraints.getValueAt(i, 8).toString();


                    AlternateResponseStatementClass alternateResponseStatement = new AlternateResponseStatementClass();
                    alternateResponseStatement.startAlternateResponseStatement(epService,
                            alternatResponseActivationStatement, alternateResponseTargetStatement,
                            alternateResponseViolationStatement, reactToActivationStatement, reactToFulfillmentStatement,
                            reactToViolationStatement, reactToTimeSpanViolationStatement);

                }

                // Alternate Precedence
                else if (buildedConstraints.getValueAt(i, 0).equals("Alternate Precedence")) {

                    String alternatPrecedenceActivationStatement = buildedConstraints.getValueAt(i, 1).toString();
                    String alternatePrecedenceTargetStatement = buildedConstraints.getValueAt(i, 2).toString();
                    String alternatePrecedenceViolationStatement = buildedConstraints.getValueAt(i, 3).toString();
                    String alternatePrecedenceTemporaryViolation = buildedConstraints.getValueAt(i, 4).toString();
                    String reactToActivationStatement = buildedConstraints.getValueAt(i, 5).toString();
                    String reactToFulfillmentStatement = buildedConstraints.getValueAt(i, 6).toString();
                    String reactToTemporaryViolationStatement = buildedConstraints.getValueAt(i, 9).toString();
                    String reactToViolationStatement = buildedConstraints.getValueAt(i, 7).toString();
                    String reactToFinalViolationStatement = buildedConstraints.getValueAt(i, 10).toString();


                    AlternatePrecedenceStatementClass alternatePrecedenceStatement = new AlternatePrecedenceStatementClass();
                    alternatePrecedenceStatement.startAlternatePrecedenceStatement(epService,
                            alternatPrecedenceActivationStatement, alternatePrecedenceTargetStatement,
                            alternatePrecedenceViolationStatement, alternatePrecedenceTemporaryViolation, reactToActivationStatement, reactToTemporaryViolationStatement, reactToFulfillmentStatement,
                            reactToViolationStatement, reactToFinalViolationStatement);

                }

                // Responded Existence
                else if (buildedConstraints.getValueAt(i, 0).equals("Responded Existence")) {

                    String respondedExistenceActivationStatement = buildedConstraints.getValueAt(i, 1).toString();
                    String respondedExistenceFutureTargetStatement = buildedConstraints.getValueAt(i, 2).toString();
                    String respondedExistencePastTargetStatement = buildedConstraints.getValueAt(i, 3).toString();
                    String reactToActivationStatement = buildedConstraints.getValueAt(i, 5).toString();
                    String reactToFulfillmentStatement = buildedConstraints.getValueAt(i, 6).toString();
                    String reactToViolationStatement = buildedConstraints.getValueAt(i, 7).toString();
                    String reactToTimeSpanViolationStatement = buildedConstraints.getValueAt(i, 8).toString();


                    RespondedExistenceStatementClass respondedExistenceStatement = new RespondedExistenceStatementClass();
                    respondedExistenceStatement.startRespondedExistenceStatement(epService,
                            respondedExistenceActivationStatement, respondedExistenceFutureTargetStatement,
                            respondedExistencePastTargetStatement, reactToActivationStatement, reactToFulfillmentStatement,
                            reactToViolationStatement, reactToTimeSpanViolationStatement);

                }

                // Chain Response
                else if (buildedConstraints.getValueAt(i, 0).equals("Chain Response")) {

                    String chainResponseActivationStatement = buildedConstraints.getValueAt(i, 1).toString();
                    String chainResponseTargetStatement = buildedConstraints.getValueAt(i, 2).toString();
                    String chainResponseViolationStatement = buildedConstraints.getValueAt(i, 3).toString();
                    String reactToActivationStatement = buildedConstraints.getValueAt(i, 5).toString();
                    String reactToFulfillmentStatement = buildedConstraints.getValueAt(i, 6).toString();
                    String reactToViolationStatement = buildedConstraints.getValueAt(i, 7).toString();
                    String reactToTimeSpanViolationStatement = buildedConstraints.getValueAt(i, 8).toString();


                    ChainResponseStatementClass chainResponseStatement = new ChainResponseStatementClass();
                    chainResponseStatement.startChainResponseStatement(epService, chainResponseActivationStatement,
                            chainResponseTargetStatement, chainResponseViolationStatement, reactToActivationStatement,
                            reactToFulfillmentStatement, reactToViolationStatement, reactToTimeSpanViolationStatement);

                }

                // Chain Precedence
                else if (buildedConstraints.getValueAt(i, 0).equals("Chain Precedence")) {

                    String chainPrecedenceActivationStatement = buildedConstraints.getValueAt(i, 1).toString();
                    String chainPrecedenceTargetStatement = buildedConstraints.getValueAt(i, 2).toString();
                    String chainPrecedenceViolationStatement = buildedConstraints.getValueAt(i, 3).toString();
                    String reactToActivationStatement = buildedConstraints.getValueAt(i, 4).toString();
                    String reactToFulfillmentStatement = buildedConstraints.getValueAt(i, 5).toString();
                    String reactToTemporaryViolationStatement = buildedConstraints.getValueAt(i, 6).toString();
                    String reactToViolationStatement = buildedConstraints.getValueAt(i, 7).toString();


                    ChainPrecedenceStatementClass chainPrecedenceStatement = new ChainPrecedenceStatementClass();
                    chainPrecedenceStatement.startChainPrecedenceStatement(epService, chainPrecedenceActivationStatement,
                            chainPrecedenceViolationStatement, chainPrecedenceTargetStatement,
                            reactToActivationStatement,
                            reactToFulfillmentStatement, reactToTemporaryViolationStatement, reactToViolationStatement);

                }

                // Not Response
                else if (buildedConstraints.getValueAt(i, 0).equals("Not Response")) {

                    String notResponseActivationStatement = buildedConstraints.getValueAt(i, 1).toString();
                    String notResponseViolationStatement = buildedConstraints.getValueAt(i, 2).toString();
                    String notResponseTemporaryVulfillmentStatement = buildedConstraints.getValueAt(i, 3).toString();
                    String reactToActivationStatement = buildedConstraints.getValueAt(i, 5).toString();
                    String reactToViolationStatement = buildedConstraints.getValueAt(i, 6).toString();
                    String reactToFulfillmentStatement = buildedConstraints.getValueAt(i, 7).toString();



                    NotResponseStatementClass notResponseStatement = new NotResponseStatementClass();
                    notResponseStatement.startNotResponseStatement(epService, notResponseActivationStatement,
                            notResponseViolationStatement, notResponseTemporaryVulfillmentStatement, reactToActivationStatement, reactToViolationStatement, reactToFulfillmentStatement);

                }

                // Not Precedence
                else if (buildedConstraints.getValueAt(i, 0).equals("Not Precedence")) {

                    String notPrecedenceActivationStatement = buildedConstraints.getValueAt(i, 1).toString();
                    String notPrecedenceFulfillmentStatement = buildedConstraints.getValueAt(i, 2).toString();
                    String notPrecedenceViolationStatement = buildedConstraints.getValueAt(i, 3).toString();
                    String reactToActivationStatement = buildedConstraints.getValueAt(i, 5).toString();
                    String reactToFulfillmentStatement = buildedConstraints.getValueAt(i, 6).toString();
                    String reactToViolationStatement = buildedConstraints.getValueAt(i, 7).toString();

                    NotPrecedenceStatementClass notPrecedenceStatement = new NotPrecedenceStatementClass();
                    notPrecedenceStatement.startNotPrecedenceStatement(epService, notPrecedenceActivationStatement,
                    notPrecedenceFulfillmentStatement, notPrecedenceViolationStatement, reactToActivationStatement, reactToFulfillmentStatement, reactToViolationStatement);

                }

                // Not Responded Existence
                else if (buildedConstraints.getValueAt(i, 0).equals("Not Responded Existence")) {

                    String notRespondedExistenceActivationStatement = buildedConstraints.getValueAt(i, 1).toString();
                    String notResponededExistencePastFutureStatement = buildedConstraints.getValueAt(i, 2).toString();
                    String notResponededExistencePastTargetStatement = buildedConstraints.getValueAt(i, 3).toString();
                    String notResponededExistenceTemporaryFulfillment = buildedConstraints.getValueAt(i, 4).toString();
                    String reactToActivationStatement = buildedConstraints.getValueAt(i, 5).toString();
                    String reactToFulfillmentStatement = buildedConstraints.getValueAt(i, 6).toString();
                    String reactToViolationStatement = buildedConstraints.getValueAt(i, 7).toString();


                    NotRespondedExistenceStatementClass notRespondedExistenceStatement = new NotRespondedExistenceStatementClass();
                    notRespondedExistenceStatement.startNotRespondedExistenceStatement(epService,
                            notRespondedExistenceActivationStatement, notResponededExistencePastFutureStatement,
                            notResponededExistencePastTargetStatement, notResponededExistenceTemporaryFulfillment, reactToActivationStatement,
                            reactToFulfillmentStatement, reactToViolationStatement);

                }

                // Not Chain Response
                else if (buildedConstraints.getValueAt(i, 0).equals("Not Chain Response")) {

                    String notChainResponseActivationStatement = buildedConstraints.getValueAt(i, 1).toString();
                    String notChainResponseTemporaryFulfillmentStatement = buildedConstraints.getValueAt(i, 2).toString();
                    String notChainResponseViolationStatement = buildedConstraints.getValueAt(i, 3).toString();
                    String reactToActivationStatement = buildedConstraints.getValueAt(i, 5).toString();
                    String reactToFulfillmentStatement = buildedConstraints.getValueAt(i, 6).toString();
                    String reactToViolationStatement = buildedConstraints.getValueAt(i, 7).toString();


                    NotChainResponseStatementClass notChainReponseStatement = new NotChainResponseStatementClass();
                    notChainReponseStatement.startNotChainResponseStatement(epService, notChainResponseActivationStatement,
                    notChainResponseTemporaryFulfillmentStatement, notChainResponseViolationStatement, reactToActivationStatement,
                            reactToFulfillmentStatement, reactToViolationStatement);

                }

                // Not Chain Precedence
                else if (buildedConstraints.getValueAt(i, 0).equals("Not Chain Precedence")) {

                    String notChainPrecedenceActivationStatement = buildedConstraints.getValueAt(i, 1).toString();
                    String notChainPrecedenceTargetStatement = buildedConstraints.getValueAt(i, 2).toString();
                    String notChainPrecedenceViolationStatement = buildedConstraints.getValueAt(i, 3).toString();
                    String reactToActivationStatement = buildedConstraints.getValueAt(i, 5).toString();
                    String reactToFulfillmentStatement = buildedConstraints.getValueAt(i, 6).toString();
                    String reactToViolationStatement = buildedConstraints.getValueAt(i, 7).toString();

                    NotChainPrecedenceStatementClass notChainPrecedenceStatement = new NotChainPrecedenceStatementClass();
                    notChainPrecedenceStatement.startNotChainPrecedenceStatement(epService,
                            notChainPrecedenceActivationStatement, notChainPrecedenceTargetStatement,
                            notChainPrecedenceViolationStatement,
                            reactToActivationStatement, reactToFulfillmentStatement, reactToViolationStatement);

                }

            }
        } catch (Exception e) {
            LOG.debug("Failed to start statements...");
            JOptionPane.showMessageDialog(null, "Failed to start the EQL-Queries. Please try again and check for misspellings!", "Something wrent wrong..", JOptionPane.ERROR_MESSAGE);
            epService.destroy();
        }


        new Thread(new Runnable() {

            @Override
            public void run() { 
                System.out.println("EVENT: " + System.currentTimeMillis());
                epService.getEPRuntime().sendEvent(new MachineFailureEvent(1, "ABB", false));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("EVENT: " + System.currentTimeMillis());
                epService.getEPRuntime().sendEvent(new OrderMaintenanceEvent(1, "ABB", true));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("EVENT: " + System.currentTimeMillis());
                epService.getEPRuntime().sendEvent(new MachineFailureEvent(1, "ABB", true));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("EVENT: " + System.currentTimeMillis());
                epService.getEPRuntime().sendEvent(new MachineFailureEvent(1, "Siemens", true));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("EVENT: " + System.currentTimeMillis());
                epService.getEPRuntime().sendEvent(new MachineFailureEvent(1, "ABB", true));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("EVENT: " + System.currentTimeMillis());
                epService.getEPRuntime().sendEvent(new OrderMaintenanceEvent(1, "Emerson", true));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("EVENT: " + System.currentTimeMillis());
                epService.getEPRuntime().sendEvent(new OrderMaintenanceEvent(1, "ABB", true));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("EVENT: " + System.currentTimeMillis());
                epService.getEPRuntime().sendEvent(new OrderMaintenanceEvent(1, "ABB", false));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("EVENT: " + System.currentTimeMillis());
                epService.getEPRuntime().sendEvent(new MachineFailureEvent(2, "ABB", true));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("EVENT: " + System.currentTimeMillis());
                epService.getEPRuntime().sendEvent(new OrderMaintenanceEvent(1, "ABB", false));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("EVENT: " + System.currentTimeMillis());
                epService.getEPRuntime().sendEvent(new OrderMaintenanceEvent(2, "ABB", true));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("EVENT: " + System.currentTimeMillis());
                epService.getEPRuntime().sendEvent(new OrderMaintenanceEvent(1, "ABB", true));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("EVENT: " + System.currentTimeMillis());
                epService.getEPRuntime().sendEvent(new OrderMaintenanceEvent(2, "KUKA", true));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("EVENT: " + System.currentTimeMillis());
                epService.getEPRuntime().sendEvent(new MachineFailureEvent(1, "ABB", false));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("EVENT: " + System.currentTimeMillis());
                epService.getEPRuntime().sendEvent(new MachineFailureEvent(1, "ABB", true));
            }
            
        }).start();


    }

    public static void finishProcess(){

        String constraintType = null;

        // This method is called at the end of the process execution
        // Every activated constraint that has not been violated or fulfilled during the process, is set to violation/fulfillment (depending on its constraint-type)
        for(int i=0; i< monitoringScreen.monitorList.getRowCount(); i++){
            if(monitoringScreen.monitorList.getValueAt(i,2).equals("activation")){
                String constraint = monitoringScreen.monitorList.getValueAt(i,0).toString();
                for(int x=0; x<AddConstraintScreen.constraintsAndConditionsList.getRowCount(); x++){
                    if(constraint.equals(AddConstraintScreen.constraintsAndConditionsList.getValueAt(x,12).toString())){
                        constraintType = AddConstraintScreen.constraintsAndConditionsList.getValueAt(x,0).toString();
                        x=AddConstraintScreen.constraintsAndConditionsList.getRowCount();
                    }
                }
                if(constraintType.equals("Existence")){
                    monitoringScreen.monitorList.setValueAt("violation",i,2);

                    /* Only used if reactToViolation() is to be used
                    int id =  Integer.parseInt(monitoringScreen.monitorList.getValueAt(i,3).toString());
                    String constraintName = monitoringScreen.monitorList.getValueAt(i,0).toString();
                    String actOrTar = monitoringScreen.monitorList.getValueAt(i,2).toString();
                    String correlationActivation = monitoringScreen.monitorList.getValueAt(i,1).toString();
                    reactToViolation(id, constraintName, actOrTar, correlationActivation);
                    */
                }
                else if(constraintType.equals("Response")){
                    monitoringScreen.monitorList.setValueAt("violation",i,2);

                    /* Only used if reactToViolation() is to be used
                    int id =  Integer.parseInt(monitoringScreen.monitorList.getValueAt(i,3).toString());
                    String constraintName = monitoringScreen.monitorList.getValueAt(i,0).toString();
                    String actOrTar = monitoringScreen.monitorList.getValueAt(i,2).toString();
                    String correlationActivation = monitoringScreen.monitorList.getValueAt(i,1).toString();
                    reactToViolation(id, constraintName, actOrTar, correlationActivation);
                    */
                }
                else if(constraintType.equals("Responded Existence")){
                    monitoringScreen.monitorList.setValueAt("violation",i,2);
                    
                    /* Only used if reactToViolation() is to be used
                    int id =  Integer.parseInt(monitoringScreen.monitorList.getValueAt(i,3).toString());
                    String constraintName = monitoringScreen.monitorList.getValueAt(i,0).toString();
                    String actOrTar = monitoringScreen.monitorList.getValueAt(i,2).toString();
                    String correlationActivation = monitoringScreen.monitorList.getValueAt(i,1).toString();
                    reactToViolation(id, constraintName, actOrTar, correlationActivation);
                    */
                }
                else if(constraintType.equals("Alternate Response")){
                    monitoringScreen.monitorList.setValueAt("violation",i,2);

                    /* Only used if reactToViolation() is to be used
                    int id =  Integer.parseInt(monitoringScreen.monitorList.getValueAt(i,3).toString());
                    String constraintName = monitoringScreen.monitorList.getValueAt(i,0).toString();
                    String actOrTar = monitoringScreen.monitorList.getValueAt(i,2).toString();
                    String correlationActivation = monitoringScreen.monitorList.getValueAt(i,1).toString();
                    reactToViolation(id, constraintName, actOrTar, correlationActivation);
                    */
                }
                else if(constraintType.equals("Chain Response")){
                    monitoringScreen.monitorList.setValueAt("violation",i,2);

                    /* Only used if reactToViolation() is to be used
                    int id =  Integer.parseInt(monitoringScreen.monitorList.getValueAt(i,3).toString());
                    String constraintName = monitoringScreen.monitorList.getValueAt(i,0).toString();
                    String actOrTar = monitoringScreen.monitorList.getValueAt(i,2).toString();
                    String correlationActivation = monitoringScreen.monitorList.getValueAt(i,1).toString();
                    reactToViolation(id, constraintName, actOrTar, correlationActivation);
                    */
                }
                else if(constraintType.equals("Not Responded Exsitence")){
                    monitoringScreen.monitorList.setValueAt("fulfillment",i,2);

                }
                else if(constraintType.equals("Not Response")){
                    monitoringScreen.monitorList.setValueAt("fulfillment",i,2);

                }
                else if(constraintType.equals("Not Chain Response")){
                    monitoringScreen.monitorList.setValueAt("fulfillment",i,2);

                }
                
            }
        }
    }

    public static void stopProcess(){
        final EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();
        epService.destroy();
    }

    public static void reactToViolation(int id, String constraintName, String actOrTar, String correlationActivation){
        // TODO to define the reaction to be done
    }

    

}




