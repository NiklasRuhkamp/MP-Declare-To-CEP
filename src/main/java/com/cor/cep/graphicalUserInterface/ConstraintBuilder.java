package com.cor.cep.graphicalUserInterface;


import javax.swing.table.DefaultTableModel;

import ch.qos.logback.core.recovery.ResilientSyslogOutputStream;


public class ConstraintBuilder {
	
	public static String createContextStatement;
	public static boolean SgmentedBy;
	
	// Table to store the EPL Queries
	public static String [] eplQueryHeader = {"Constraint", "ActivationQuery", "TargetQuery", "ViolationQuery", "CorrelationViolationQuery", "ReactToActivationQuery", "ReactToTargetQuery", "ReactToViolationQuery", "ReactToTimespanViolation", "Placeholder", "Placeholder2"};
	public static DefaultTableModel eplQueries = new DefaultTableModel(eplQueryHeader,0);
	
	public DefaultTableModel buildConstraint(DefaultTableModel constraintsAndConditionsList) {
		
		// Iterate over all added constraints
		for(int x =0; x<constraintsAndConditionsList.getRowCount();x++) {
			
		String activationConditionPayload;
		String targetConditionPayload;
		String corActivationPayload;
		String constraintName;
		String timeSpanUnit = null;
		String timeSpan;
		
		// Read values from constraintsAndConditionsList into variables
		String activation=(String) constraintsAndConditionsList.getValueAt(x, 1);
		String target=(String) constraintsAndConditionsList.getValueAt(x, 2);
		constraintName = (String) constraintsAndConditionsList.getValueAt(x, 12);		

		// Check, if activation condition is selected
		if(constraintsAndConditionsList.getValueAt(x, 3)=="--") {
			activationConditionPayload = null;
		}
		else {
			activationConditionPayload = (String) constraintsAndConditionsList.getValueAt(x, 3);
		}
		
		String activationConditionOperator=(String) constraintsAndConditionsList.getValueAt(x, 4);
		String activationConditionValue=(String) constraintsAndConditionsList.getValueAt(x, 5);
		
		// If the activationConditionValue is not a boolen(true/fals), but a String, it must be enclosed in quotation marks
		if(!activationConditionValue.equals(null)){
			for(int i=0; i<AddEventScreen.allListTable.getRowCount(); i++){
				if(AddEventScreen.allListTable.getValueAt(i, 2).equals(activationConditionPayload)){
					if(AddEventScreen.allListTable.getValueAt(i, 1).equals("String")){
						activationConditionValue = "'" + activationConditionValue + "'";
					}
					i=AddEventScreen.allListTable.getRowCount();
				}
			}
		}
		
		// Check, if target condition is selected
		if(constraintsAndConditionsList.getValueAt(x, 6)=="--") {
			targetConditionPayload = null;
		}
		else {
			targetConditionPayload = (String) constraintsAndConditionsList.getValueAt(x, 6);
		}
		
		String targetConditionOperator=(String) constraintsAndConditionsList.getValueAt(x, 7);
		String targetConditionValue=(String) constraintsAndConditionsList.getValueAt(x, 8);
		
		// If the targetConditionValue is not a boolen(true/fals), but a String, it must be enclosed in quotation marks

		if(!targetConditionValue.equals(null)){
			for(int i=0; i<AddEventScreen.allListTable.getRowCount(); i++){
				if(AddEventScreen.allListTable.getValueAt(i, 2).equals(targetConditionPayload)){
					if(AddEventScreen.allListTable.getValueAt(i, 1).equals("String")){
						targetConditionValue = "'" + targetConditionValue + "'";
					}
					i=AddEventScreen.allListTable.getRowCount();
				}
			}
		}
		// Check, if correlation condition is selected
		if(constraintsAndConditionsList.getValueAt(x, 9)=="--") {
			corActivationPayload = null;
		}
		else {
			corActivationPayload = (String) constraintsAndConditionsList.getValueAt(x, 9);
		}
		
		String corOperator=(String) constraintsAndConditionsList.getValueAt(x, 10);
		String corTargetPayload=(String) constraintsAndConditionsList.getValueAt(x, 11);
		String identifier=null;
		String event=null;

		// Check, if a specific timeSpan is given
		if(constraintsAndConditionsList.getValueAt(x, 13).equals("--")) {
			timeSpan = null;
		}
		else {
			timeSpanUnit = (String) constraintsAndConditionsList.getValueAt(x, 13);
			timeSpan = (String) constraintsAndConditionsList.getValueAt(x, 14);
		}

		boolean segmentedBy=CreateContextScreen.segmented;
		
		// EPL Statement to create the context SegmentedById for processes with multiple instances
		if(segmentedBy == true) {
			
			StringBuilder createContextBuilder = new StringBuilder();

			createContextBuilder.append("CREATE CONTEXT SegmentedById PARTITION BY id from middleLayer"); 
		
			for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
					identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
					event = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 0);
					createContextBuilder.append(", " + identifier + " FROM " + event);
			}
			createContextStatement =  createContextBuilder.toString();
		}
		
		// If else statement to create the suitable EPL Query for each constraint
		String constraint = (String) constraintsAndConditionsList.getValueAt(x, 0);	
		
		if(constraint.equals("Existence")){

			// Query to activated Existence
			StringBuilder existenceActivation = new StringBuilder();

			if(segmentedBy == true) {
				existenceActivation.append("CONTEXT SegmentedById ");
			}

			existenceActivation.append("INSERT INTO middleLayer SELECT ");

			if(segmentedBy == true) {
				for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
					if(CreateContextScreen.segmentedListTable.getValueAt(i, 0)==activation) {
						identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
						existenceActivation.append(identifier + ",");
					}
				}
			}

			existenceActivation.append(" '" + constraintName + "' as constraint, 'activation' as actOrTar");

			if(corActivationPayload != null) {
				existenceActivation.append(", " + corActivationPayload + " as correlationActivation");
			}

			existenceActivation.append(" FROM " + activation + ".std:firstevent()");

			if(activationConditionPayload != null) {
				existenceActivation.append(" WHERE " + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue);
			}	


			// Query to check, if Existence is fulfilled
			StringBuilder existenceTarget = new StringBuilder();

			if(segmentedBy == true) {
				existenceTarget.append("CONTEXT SegmentedById ");
			}

			existenceTarget.append("INSERT INTO middleLayer SELECT ");
				
				if(segmentedBy == true) {
					for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
						if(CreateContextScreen.segmentedListTable.getValueAt(i, 0)==activation) {
							identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
							existenceTarget.append(identifier + ",");
						}
					}
				}
				
				existenceTarget.append(" '" + constraintName + "' as constraint, 'target' as actOrTar");
				
				if(corActivationPayload != null) {
					existenceTarget.append(", " + corActivationPayload + " as correlationActivation");
				}
	
				existenceTarget.append(" FROM " + activation);
				
				if(activationConditionPayload != null) {
					existenceTarget.append(" WHERE " + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue);
				}	

			// Query to react to the Activation of Existence
			String existenceActivationReactionQuery;

				if(corActivationPayload != null) {
					existenceActivationReactionQuery = "SELECT id, constraint, actOrTar, correlationActivation FROM middleLayer WHERE actOrTar = 'activation'  AND constraint = '" + constraintName + "'";
				}
				else {
					existenceActivationReactionQuery = "SELECT id, constraint, actOrTar FROM middleLayer WHERE actOrTar = 'activation' AND constraint = '" + constraintName + "'";
				}

			
			// Query to react to the Fulfillment of Existence
			StringBuilder existenceTargetReactionQuery = new StringBuilder();
				
				if(segmentedBy == true) {
	
					existenceTargetReactionQuery.append("CONTEXT SegmentedById ");
				
				}	
					
				if(corActivationPayload != null) {
					existenceTargetReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar, b.correlationActivation as correlationActivation FROM PATTERN [a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') ->  b=middleLayer(actOrTar='target', a.constraint=constraint)]");
				}
				else {
					existenceTargetReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar FROM PATTERN [a=middleLayer(constraint='" + constraintName + "', actOrTar='target') ->  b=middleLayer(actOrTar='activation', a.constraint=constraint)]");
				}

			// Query to react to the Fulfillment of Existence
			StringBuilder existenceTargetPastReactionQuery = new StringBuilder();
				
				if(segmentedBy == true) {
	
					existenceTargetPastReactionQuery.append("CONTEXT SegmentedById ");
				
				}	
					
				if(corActivationPayload != null) {
					existenceTargetPastReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar, b.correlationActivation as correlationActivation FROM PATTERN [a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') ->  b=middleLayer(actOrTar='target', a.constraint=constraint)]");
				}
				else {
					existenceTargetPastReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar FROM PATTERN [a=middleLayer(constraint='" + constraintName + "', actOrTar='target') ->  b=middleLayer(actOrTar='activation', a.constraint=constraint)]");
				}


			// Query to react to the TimeSpan Violation of Existence
			StringBuilder existenceTimeSpanViolationReactionQuery = new StringBuilder();
							
			if(timeSpan != null){
				if(segmentedBy == true) {
		
					existenceTimeSpanViolationReactionQuery.append("CONTEXT SegmentedById ");
				
				}	
					
				if(corActivationPayload != null) {
					existenceTimeSpanViolationReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar, a.correlationActivation as correlationActivation FROM PATTERN [ a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') ->  ");
					
					existenceTimeSpanViolationReactionQuery.append("(timer:interval(" + timeSpan + " " + timeSpanUnit + ") AND NOT ");

					
					existenceTimeSpanViolationReactionQuery.append("b=middleLayer(actOrTar='target', a.constraint=constraint))]");
				}
				else {
					existenceTimeSpanViolationReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar FROM PATTERN [a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') ->  ");
					
					existenceTimeSpanViolationReactionQuery.append("(timer:interval(" + timeSpan + " " + timeSpanUnit + ") AND NOT ");

					existenceTimeSpanViolationReactionQuery.append("b=middleLayer(actOrTar='target', a.constraint=constraint))]");
				}
			}
				
			// Insert the builded EPL Queries into the eplQueries Table	
			Object [] existenceQueries = {(String) constraintsAndConditionsList.getValueAt(x, 0), existenceActivation, existenceTarget, existenceTargetPastReactionQuery, existenceActivationReactionQuery, existenceTargetReactionQuery, null, existenceTimeSpanViolationReactionQuery};
			eplQueries.addRow(existenceQueries);
		
	
		}
		
		else if(constraint.equals("Response")){
			
			// Query to check, if Response is activated
			StringBuilder responseActivation = new StringBuilder();

				responseActivation.append("INSERT INTO middleLayer SELECT ");
				
				if(segmentedBy == true) {
					for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
						if(CreateContextScreen.segmentedListTable.getValueAt(i, 0)==activation) {
							identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
							responseActivation.append(identifier + ",");
						}
					}
				}
				
				responseActivation.append(" '" + constraintName + "' as constraint, 'activation' as actOrTar");
				
				if(corActivationPayload != null) {
					responseActivation.append(", " + corActivationPayload + " as correlationActivation");
				}
	
				responseActivation.append(" FROM " + activation);
				
				if(activationConditionPayload != null) {
					responseActivation.append(" WHERE " + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue);
				}

			// Query to check, if Response is fulfilled
			StringBuilder responseTarget = new StringBuilder();
				
				if(segmentedBy == true) {
					responseTarget.append("CONTEXT SegmentedById ");
				}
									
				
				responseTarget.append("INSERT INTO middleLayer SELECT ");
				
				if(segmentedBy==true) {
					
					for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
						if(CreateContextScreen.segmentedListTable.getValueAt(i, 0)==target) {
							identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
							responseTarget.append("b." + identifier + " as id, ");
						}
					}
				}
				
				responseTarget.append("'" + constraintName + "' as constraint, 'target' as actOrTar");

				if(corActivationPayload != null) {
					responseTarget.append(", b." + corTargetPayload + " as correlationActivation");
				}
				
				responseTarget.append(" FROM PATTERN [every a=" + activation);
			
				if(activationConditionPayload != null) {
					responseTarget.append(" (" + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue +")");
				}
				
				responseTarget.append(" ->  b= " + target);
				
				if(targetConditionPayload != null) {
					responseTarget.append("(" + targetConditionPayload + " " + targetConditionOperator + " " + targetConditionValue + ")");
				}
				
				responseTarget.append("]");
				
				if(corActivationPayload != null) {
					responseTarget.append(" WHERE a." + corActivationPayload + " " +  corOperator + " b." + corTargetPayload);
				}
				

			// Query to react to the Activation of Response
			String responseActivationReactionQuery;

			if(corActivationPayload != null) {
				responseActivationReactionQuery = "SELECT id, constraint, actOrTar, correlationActivation FROM middleLayer WHERE actOrTar = 'activation'  AND constraint = '" + constraintName + "'";
			}
			else {
				responseActivationReactionQuery = "SELECT id, constraint, actOrTar FROM middleLayer WHERE actOrTar = 'activation' AND constraint = '" + constraintName + "'";
			}
			
			// Query to react to the Fulfillment of Response
			StringBuilder responseTargetReactionQuery = new StringBuilder();
				
			if(segmentedBy == true) {
	
				responseTargetReactionQuery.append("CONTEXT SegmentedById ");
			
			}	
				
			if(corActivationPayload != null) {
				responseTargetReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar, b.correlationActivation as correlationActivation FROM PATTERN [every (a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') -> every b=middleLayer(actOrTar='target', a.constraint=constraint))]");
			}
			else {
				responseTargetReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar FROM PATTERN [every (a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') -> every b=middleLayer(actOrTar='target', a.constraint=constraint))]");
			}

			// Query to react to the TimeSpan Violation of Response
			StringBuilder responseTimeSpanViolationReactionQuery = new StringBuilder();
							
			if(timeSpan != null){
				if(segmentedBy == true) {
		
					responseTimeSpanViolationReactionQuery.append("CONTEXT SegmentedById ");
				
				}	
					
				if(corActivationPayload != null) {
					responseTimeSpanViolationReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar, a.correlationActivation as correlationActivation FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') ->  ");
					
					responseTimeSpanViolationReactionQuery.append("(timer:interval(" + timeSpan + " " + timeSpanUnit + ") AND NOT ");

					
					responseTimeSpanViolationReactionQuery.append("b=middleLayer(actOrTar='target', a.constraint=constraint))]");
				}
				else {
					responseTimeSpanViolationReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') ->  ");
					
					responseTimeSpanViolationReactionQuery.append("(timer:interval(" + timeSpan + " " + timeSpanUnit + ") AND NOT ");

					responseTimeSpanViolationReactionQuery.append("b=middleLayer(actOrTar='target', a.constraint=constraint))]");
				}
			}
			
			// Insert the builded EPL Queries into the eplQueries Table	
			Object [] responseQueries = {(String) constraintsAndConditionsList.getValueAt(x, 0), responseActivation, responseTarget, null, null, responseActivationReactionQuery, responseTargetReactionQuery, responseTimeSpanViolationReactionQuery};
			eplQueries.addRow(responseQueries);
								
		}
			
		else if(constraint.equals("Precedence")){
			
			// Query to check, if Precedence is activated
			StringBuilder precedenceActivation = new StringBuilder();

				precedenceActivation.append("INSERT INTO middleLayer SELECT ");
					
				if(segmentedBy == true) {
						for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
							if(CreateContextScreen.segmentedListTable.getValueAt(i, 0)==activation) {
								identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
								precedenceActivation.append(identifier + ",");
							}
						}
					}
				
				precedenceActivation.append(" '" + constraintName + "' as constraint, 'activation' as actOrTar");
			
				if(corActivationPayload != null) {
					precedenceActivation.append(", " + corActivationPayload + " as correlationActivation");
				}

				precedenceActivation.append(" FROM " + activation);	
				
				if(activationConditionPayload != null) {
					precedenceActivation.append(" WHERE " + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue);
				}

			// Query to set Precedence as temporary violated
			StringBuilder precedenceViolatioin = new StringBuilder();


				precedenceViolatioin.append("INSERT INTO middleLayer SELECT ");
						
				if(segmentedBy == true) {
						for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
							if(CreateContextScreen.segmentedListTable.getValueAt(i, 0)==activation) {
								identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
								precedenceViolatioin.append(identifier + ",");
							}
						}
					}
				
					precedenceViolatioin.append(" '" + constraintName + "' as constraint, 'violation' as actOrTar");
			
				if(corActivationPayload != null) {
					precedenceViolatioin.append(", " + corActivationPayload + " as correlationActivation");
				}

				precedenceViolatioin.append(" FROM " + activation);	
				
				if(activationConditionPayload != null) {
					precedenceViolatioin.append(" WHERE " + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue);
				}

			// Query to check, if Precedence is fulfilled
			StringBuilder precedenceTarget = new StringBuilder();
				
				if(segmentedBy == true) {
					precedenceTarget.append("CONTEXT SegmentedById ");
				}
								
				precedenceTarget.append("INSERT INTO middleLayer SELECT ");
				
				if(segmentedBy==true) {
					
					for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
						if(CreateContextScreen.segmentedListTable.getValueAt(i, 0)==target) {
							identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
							precedenceTarget.append("a." + identifier + " as id, ");
						}
					}
				}
				
				precedenceTarget.append("'" + constraintName + "' as constraint, 'target' as actOrTar");
				
				if(corActivationPayload != null) {
					precedenceTarget.append(", a." + corTargetPayload + " as correlationActivation");
				}

				precedenceTarget.append(" FROM PATTERN [every  a= " + target);
				
				if(targetConditionPayload != null) {
					precedenceTarget.append(" (" + targetConditionPayload + " " + targetConditionOperator + " " + targetConditionValue + ")");
				}
				
				
				precedenceTarget.append(" -> every b= " + activation);
					
				if(activationConditionPayload != null) {
					precedenceTarget.append(" (" + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue +")");
				}

				if(timeSpan != null){
					precedenceTarget.append(" AND NOT timer:interval(" + timeSpan + " " + timeSpanUnit + ")");
				}
				
				precedenceTarget.append("]");
				
				if(corActivationPayload != null) {
					precedenceTarget.append(" WHERE a." + corTargetPayload + " " +  corOperator + " b." + corActivationPayload);
				}
				
			// Query to react to the Activation of Precedence
			String precedenceActivationReactionQuery;
				
				if(corActivationPayload != null) {
					precedenceActivationReactionQuery = "SELECT id, constraint, actOrTar, correlationActivation FROM middleLayer WHERE actOrTar = 'activation' AND constraint = '" + constraintName + "'";
				}
				else {
					precedenceActivationReactionQuery = "SELECT id, constraint, actOrTar FROM middleLayer WHERE actOrTar = 'activation' AND constraint = '" + constraintName + "'";
				}

			// Query to react to the Temporary Violation of Precedence
			StringBuilder precedenceTemporaryViolationReactionQuery = new StringBuilder();

				if(segmentedBy == true) {
		
					precedenceTemporaryViolationReactionQuery.append("CONTEXT SegmentedById ");
				
				}	
					
				if(corActivationPayload != null) {
					precedenceTemporaryViolationReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar, b.correlationActivation as correlationActivation FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') -> b=middleLayer(actOrTar='violation', a.constraint=constraint) and not middleLayer(actOrTar='activation', a.constraint=constraint) and not middleLayer(actOrTar='target', a.constraint=constraint)]");
				}
				else {
					precedenceTemporaryViolationReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') -> b=middleLayer(actOrTar='violation', a.constraint=constraint) and not middleLayer(actOrTar='activation', a.constraint=constraint) and not middleLayer(actOrTar='target', a.constraint=constraint)]");
				}
			
			// Query to react to the Fulfillment of Precedence
			StringBuilder precedenceTargetReactionQuery = new StringBuilder();
				
				if(segmentedBy == true) {
	
					precedenceTargetReactionQuery.append("CONTEXT SegmentedById ");
				
				}	
					
				if(corActivationPayload != null) {
					precedenceTargetReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar, b.correlationActivation as correlationActivation FROM PATTERN [every (a=middleLayer(constraint='" + constraintName + "', actOrTar='violation') -> every b=middleLayer(actOrTar='target', a.constraint=constraint))]");
				}
				else {
					precedenceTargetReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar FROM PATTERN [every (a=middleLayer(constraint='" + constraintName + "', actOrTar='violation') -> every b=middleLayer(actOrTar='target', a.constraint=constraint))]");
				}

			// Query to react to a Temporary Violation of Precedence, that is not followed by a Fulfillment
			StringBuilder precedenceViolationReactionQuery = new StringBuilder();

				if(segmentedBy == true) {
		
					precedenceViolationReactionQuery.append("CONTEXT SegmentedById ");
				
				}	
					
				if(corActivationPayload != null) {
					precedenceViolationReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar, a.correlationActivation as correlationActivation FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='violation') ->  ");
					
					precedenceViolationReactionQuery.append("(timer:interval(1 sec) AND NOT ");

					
					precedenceViolationReactionQuery.append("b=middleLayer(actOrTar='target', a.constraint=constraint, correlationActivation = a.correlationActivation))]");
				}
				else {
					precedenceViolationReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='violation') ->  ");
					
					precedenceViolationReactionQuery.append("(timer:interval(1 sec) AND NOT ");

					precedenceViolationReactionQuery.append("b=middleLayer(actOrTar='target', a.constraint=constraint))]");
				}
			

			// Insert the builded EPL Queries into the eplQueries Table						
			Object [] precedenceQueries = {(String) constraintsAndConditionsList.getValueAt(x, 0), precedenceActivation, precedenceTarget, precedenceViolatioin, precedenceActivationReactionQuery, precedenceTargetReactionQuery, precedenceTemporaryViolationReactionQuery, precedenceViolationReactionQuery, null};
			eplQueries.addRow(precedenceQueries);
			
		}

		else if(constraint.equals("Alternate Response")){
			
			// Query to check, if Alternate Response is activated
			StringBuilder alternateResponseActivation = new StringBuilder();
			
			alternateResponseActivation.append("INSERT INTO middleLayer SELECT ");
			
				if(segmentedBy == true) {
					for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
						if(CreateContextScreen.segmentedListTable.getValueAt(i, 0)==activation) {
							identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
							alternateResponseActivation.append(identifier + ", ");
						}
					}
				}
			
			alternateResponseActivation.append("'" + constraintName + "' as constraint, 'activation' as actOrTar");
			
			if(corActivationPayload != null) {
				alternateResponseActivation.append(", " + corActivationPayload + " as correlationActivation");
			}

			alternateResponseActivation.append(" FROM " + activation);
							
			if(activationConditionPayload != null) {
				alternateResponseActivation.append(" WHERE " + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue);
			}
			
			// Query to check, if Alternate Response is fulfilled
			StringBuilder alternateResponseFulfillment = new StringBuilder();
		
			if(segmentedBy == true) {
				alternateResponseFulfillment.append("CONTEXT SegmentedById ");
			}
			
			alternateResponseFulfillment.append("INSERT INTO middleLayer SELECT ");
				
			if(segmentedBy==true) {
				
				for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
					if(CreateContextScreen.segmentedListTable.getValueAt(i, 0)==activation) {
						identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
						alternateResponseFulfillment.append("b." + identifier + " as id, ");
					}
				}
			}		
			
			alternateResponseFulfillment.append("'" + constraintName + "' as constraint, 'target' as actOrTar");
			
			if(corActivationPayload != null) {
				alternateResponseFulfillment.append(", b." + corTargetPayload + " as correlationActivation");
			}

			alternateResponseFulfillment.append(" FROM PATTERN [every a= " + activation);
			
			if(activationConditionPayload != null) {
				alternateResponseFulfillment.append("(" + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue +")");
			}
			
			alternateResponseFulfillment.append(" ->  b=" + target);
			
			if(targetConditionPayload !=null && corActivationPayload !=null) {
				alternateResponseFulfillment.append("(" + targetConditionPayload + " " + targetConditionOperator + " " + targetConditionValue);
				alternateResponseFulfillment.append(", " + corTargetPayload + " " +  corOperator + " a." + corActivationPayload + ") ");
			}
			
			if(targetConditionPayload != null && corActivationPayload == null) {
				alternateResponseFulfillment.append("(" + targetConditionPayload + " " + targetConditionOperator + " " + targetConditionValue + ") ");
				
			}
			
			if(corActivationPayload != null && targetConditionPayload == null) {
				alternateResponseFulfillment.append("(" + corTargetPayload + " " +  corOperator + " a." + corActivationPayload + ") ");
			}
			
			alternateResponseFulfillment.append(" AND NOT " + activation);

			
			if(activationConditionPayload != null) {
				alternateResponseFulfillment.append("(" + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue + ") ");
				
			}
			

			if(timeSpan != null){
				alternateResponseFulfillment.append(" AND NOT timer:interval(" + timeSpan + " " + timeSpanUnit + ")");
			}
		
			alternateResponseFulfillment.append("]");
			

			// Query to check, if Alternate Response is violated 
			StringBuilder alternateResponseViolation = new StringBuilder();
			
			if(segmentedBy == true) {
				alternateResponseViolation.append("CONTEXT SegmentedById ");
			}
			
			alternateResponseViolation.append("INSERT INTO middleLayer SELECT ");
				
			if(segmentedBy==true) {
				
				for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
					if(CreateContextScreen.segmentedListTable.getValueAt(i, 0)==activation) {
						identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
						alternateResponseViolation.append("b." + identifier + " as id, ");
					}
				}
			}

			
			alternateResponseViolation.append("'" + constraintName + "' as constraint, 'violation' as actOrTar");

			if(corActivationPayload != null) {
				alternateResponseViolation.append(", a." + corActivationPayload + " as correlationActivation");
			}

			alternateResponseViolation.append(" FROM PATTERN [every a= " + activation);
			
			if(activationConditionPayload != null) {
				alternateResponseViolation.append("(" + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue +")");
			}
			
			alternateResponseViolation.append(" ->  b=" + activation);
			
			
			if(activationConditionPayload != null) {
				alternateResponseViolation.append("(" + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue + ") ");
				
			}
			
			
			alternateResponseViolation.append(" AND NOT " + target);
	
			if(targetConditionPayload != null && corActivationPayload != null) {
				alternateResponseViolation.append("(" + targetConditionPayload + " " + targetConditionOperator + " " + targetConditionValue);
				alternateResponseViolation.append(", a." + corActivationPayload + " " +  corOperator + " " + corTargetPayload + ") ");
			}
			
			if(corActivationPayload != null && targetConditionPayload == null) {
				alternateResponseViolation.append("( a." + corActivationPayload + " " +  corOperator + " " + corTargetPayload + ") ");
			}
			
			if(targetConditionPayload != null && corActivationPayload == null) {
				alternateResponseViolation.append("(" + targetConditionPayload + " " + targetConditionOperator + " " + targetConditionValue + ") ");
			}
		
			if(timeSpan != null){
				alternateResponseViolation.append(" AND NOT timer:interval(" + timeSpan + " " + timeSpanUnit + ")");
			}

			alternateResponseViolation.append("]");
			
			// Query to react to the Activation of Alternate Response
			String alternateResponseActivationReactionQuery;
			
			if(corActivationPayload != null) {
				alternateResponseActivationReactionQuery = "SELECT id, constraint, actOrTar, correlationActivation FROM middleLayer WHERE actOrTar = 'activation' AND constraint = '" + constraintName + "'";
			}
			else {
				alternateResponseActivationReactionQuery = "SELECT id, constraint, actOrTar FROM middleLayer WHERE actOrTar = 'activation' AND constraint = '" + constraintName + "'";
			}
			
			// Query to react to the Fulfillment of Alternate Response
			StringBuilder alternateResponseTargetReactionQuery = new StringBuilder();
							
			if(segmentedBy == true) {
	
				alternateResponseTargetReactionQuery.append("CONTEXT SegmentedById ");
			
			}	
				
			if(corActivationPayload != null) {
				alternateResponseTargetReactionQuery.append("SELECT b.id as id, b.constraint as constraint, b.actOrTar as actOrTar, b.correlationActivation as correlationActivation FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') ->  b=middleLayer(actOrTar='target', constraint=a.constraint)]");
			}
			else {
				alternateResponseTargetReactionQuery.append("SELECT b.id as id, b.constraint as constraint, b.actOrTar as actOrTar FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') ->  b=middleLayer(actOrTar='target', constraint=a.constraint)]");
			}			
			
			// Query to react to the Violation of Alternate Response
			StringBuilder alternateResponseViolationReactionQuery = new StringBuilder();
			
			if(segmentedBy == true) {
	
				alternateResponseViolationReactionQuery.append("CONTEXT SegmentedById ");
			
			}	
				
			if(corActivationPayload != null) {
				alternateResponseViolationReactionQuery.append("SELECT b.id as id, a.constraint as constraint, b.actOrTar as actOrTar, a.correlationActivation as correlationActivation FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') -> b=middleLayer(actOrTar='violation', constraint=a.constraint)]");
			}
			else {
				alternateResponseViolationReactionQuery.append("SELECT b.id as id, a.constraint as constraint, b.actOrTar as actOrTar FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') -> b=middleLayer(actOrTar='violation', constraint=a.constraint)]");
			}

			// Query to react to the TimeSpan Violation of Alternate Response
			StringBuilder alternateResponseTimeSpanViolationReactionQuery = new StringBuilder();
	
			if(timeSpan != null){
				if(segmentedBy == true) {
		
					alternateResponseTimeSpanViolationReactionQuery.append("CONTEXT SegmentedById ");
				
				}	
					
				if(corActivationPayload != null) {
					alternateResponseTimeSpanViolationReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar, a.correlationActivation as correlationActivation FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') ->  ");
					
					alternateResponseTimeSpanViolationReactionQuery.append("(timer:interval(" + timeSpan + " " + timeSpanUnit + ") AND NOT ");

					
					alternateResponseTimeSpanViolationReactionQuery.append("b=middleLayer(actOrTar='target', a.constraint=constraint))]");
				}
				else {
					alternateResponseTimeSpanViolationReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') ->  ");
					
					alternateResponseTimeSpanViolationReactionQuery.append("(timer:interval(" + timeSpan + " " + timeSpanUnit + ") AND NOT ");

					alternateResponseTimeSpanViolationReactionQuery.append("b=middleLayer(actOrTar='target', a.constraint=constraint))]");
				}
			}

			// Insert the builded EPL Queries into the eplQueries Table				
			Object [] AlternateResponseQueries = {(String) constraintsAndConditionsList.getValueAt(x, 0), alternateResponseActivation, alternateResponseFulfillment, alternateResponseViolation, null, alternateResponseActivationReactionQuery, alternateResponseTargetReactionQuery, alternateResponseViolationReactionQuery, alternateResponseTimeSpanViolationReactionQuery};
			eplQueries.addRow(AlternateResponseQueries);
			
		}
		
		else if (constraint.equals("Alternate Precedence")){
			
			// Query to check, if Alternate Precedence is activated
			StringBuilder alternatePrecedenceActivation = new StringBuilder();
			
			alternatePrecedenceActivation.append("INSERT INTO middleLayer SELECT ");
			
			if(segmentedBy == true) {
				for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
					if(CreateContextScreen.segmentedListTable.getValueAt(i, 0)==activation) {
						identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
						alternatePrecedenceActivation.append(identifier + ", ");
					}
				}
		
			}
				
			alternatePrecedenceActivation.append("'" + constraintName + "' as constraint, 'activation' as actOrTar");

			if(corActivationPayload != null) {
				alternatePrecedenceActivation.append(", " + corActivationPayload + " as correlationActivation");
			}
			
			alternatePrecedenceActivation.append(" FROM " + activation);
											
			if(activationConditionPayload != null) {
				alternatePrecedenceActivation.append(" WHERE " + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue);
			}

			// Query to set Alternate Precedence temporary as violated
			StringBuilder alternatePrecedenceTemporaryViolation = new StringBuilder();

			alternatePrecedenceTemporaryViolation.append("INSERT INTO middleLayer SELECT ");
			
			if(segmentedBy == true) {
				for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
					if(CreateContextScreen.segmentedListTable.getValueAt(i, 0)==activation) {
						identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
						alternatePrecedenceTemporaryViolation.append(identifier + ", ");
					}
				}
		
			}
				
			alternatePrecedenceTemporaryViolation.append("'" + constraintName + "' as constraint, 'violation' as actOrTar");

			if(corActivationPayload != null) {
				alternatePrecedenceTemporaryViolation.append(", " + corActivationPayload + " as correlationActivation");
			}
			
			alternatePrecedenceTemporaryViolation.append(" FROM " + activation);
											
			if(activationConditionPayload != null) {
				alternatePrecedenceTemporaryViolation.append(" WHERE " + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue);
			}

			
			
			// Query to check, if Alternate Precedence is fulfilled
			StringBuilder alternatePrecedenceFulfillment = new StringBuilder();
			
			if(segmentedBy == true) {
				alternatePrecedenceFulfillment.append("CONTEXT SegmentedById ");
			}
			
			alternatePrecedenceFulfillment.append("INSERT INTO middleLayer SELECT ");
			
			if(segmentedBy == true) {
				
				for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
					if(CreateContextScreen.segmentedListTable.getValueAt(i, 0)==activation) {
						identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
						alternatePrecedenceFulfillment.append("a." + identifier + " as id, ");
					}
				}
			}
			
			
			alternatePrecedenceFulfillment.append("'" + constraintName + "' as constraint, 'target' as actOrTar");
					
			if(corActivationPayload != null) {
				alternatePrecedenceFulfillment.append(", a." + corTargetPayload + " as correlationActivation");
			}
			
			alternatePrecedenceFulfillment.append(" FROM PATTERN [every a= " + target);
			
			if(targetConditionPayload != null) {
				alternatePrecedenceFulfillment.append("(" + targetConditionPayload + " " + targetConditionOperator + " " + targetConditionValue +")");
			}
			
			alternatePrecedenceFulfillment.append(" -> b=" + activation);
			
			if(activationConditionPayload != null && corActivationPayload != null) {
				alternatePrecedenceFulfillment.append("(" + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue);
				alternatePrecedenceFulfillment.append(", a." + corTargetPayload + " " +  corOperator + " " + corActivationPayload + ") ");
			}
			
			if(activationConditionPayload != null && corActivationPayload == null) {
				alternatePrecedenceFulfillment.append("(" + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue + ") ");
			}

			if(activationConditionPayload == null && corActivationPayload != null) {
				alternatePrecedenceFulfillment.append("( a." + corTargetPayload + " " +  corOperator + " " + corActivationPayload + ") ");
			}

			if(timeSpan != null){
				alternatePrecedenceFulfillment.append(" AND NOT timer:interval(" + timeSpan + " " + timeSpanUnit + ")");
			}				

			alternatePrecedenceFulfillment.append("]");
			
			
			// Query to check, if Alternate Precedence is violated
			StringBuilder alternatePrecedenceViolation = new StringBuilder();
			
			if(segmentedBy == true) {
				alternatePrecedenceViolation.append("CONTEXT SegmentedById ");
			}
			
			alternatePrecedenceViolation.append("INSERT INTO middleLayer SELECT ");
			
			if(segmentedBy == true) {
				
				for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
					if(CreateContextScreen.segmentedListTable.getValueAt(i, 0)==activation) {
						identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
						alternatePrecedenceViolation.append("a." + identifier + " as id, ");
					}
				}
			}
			
			alternatePrecedenceViolation.append("'" + constraintName + "' as constraint, 'violation' as actOrTar");

			if(corActivationPayload != null) {
				alternatePrecedenceViolation.append(", a." + corActivationPayload + " as correlationActivation");
			}

			alternatePrecedenceViolation.append(" FROM PATTERN [every a= " + activation);
			
			if(activationConditionPayload != null) {
				alternatePrecedenceViolation.append("(" + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue +")");
			}
			
			alternatePrecedenceViolation.append(" -> b=" + activation);
			
			if(activationConditionPayload != null && corActivationPayload != null) {
				alternatePrecedenceViolation.append("(" + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue);
				alternatePrecedenceViolation.append(", " + corActivationPayload + " " +  corOperator + " a." + corActivationPayload + ") ");
			}
			
			if(corActivationPayload != null && activationConditionPayload == null) {
				alternatePrecedenceViolation.append("( " + corActivationPayload + " " +  corOperator + " a." + corActivationPayload + ") ");
			}
			
			if(activationConditionPayload != null && corActivationPayload == null) {
				alternatePrecedenceViolation.append("(" + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue + ") ");
			}
				
			alternatePrecedenceViolation.append(" AND NOT " + target);

			if(targetConditionPayload != null && corActivationPayload != null) {
				alternatePrecedenceViolation.append("(" + targetConditionPayload + " " + targetConditionOperator + " " + targetConditionValue);
				alternatePrecedenceViolation.append(", " + corTargetPayload + " " +  corOperator + " a." + corActivationPayload + ") ");
			}
			
			if(corActivationPayload != null && targetConditionPayload == null) {
				alternatePrecedenceViolation.append("( " + corTargetPayload + " " +  corOperator + " a." + corActivationPayload + ") ");
			}
			
			if(targetConditionPayload != null && corActivationPayload == null) {
				alternatePrecedenceViolation.append("(" + targetConditionPayload + " " + targetConditionOperator + " " + targetConditionValue + ") ");
			}

			if(timeSpan != null){
				alternatePrecedenceFulfillment.append(" AND NOT timer:interval(" + timeSpan + " " + timeSpanUnit + ")");
			}	
		
			alternatePrecedenceViolation.append("]");
			
			
			// Query to react to the Activation of Alternate Precedence
			String alternatePrecedenceActivationReactionQuery;
			
			if(corActivationPayload != null) {
				alternatePrecedenceActivationReactionQuery = "SELECT id, constraint, actOrTar, correlationActivation FROM middleLayer WHERE actOrTar = 'activation' AND constraint = '" + constraintName + "'";
			}
			else {
				alternatePrecedenceActivationReactionQuery = "SELECT id, constraint, actOrTar FROM middleLayer WHERE actOrTar = 'activation' AND constraint = '" + constraintName + "'";
			}

			// Query to react to the Temporary Violation of Alternate Precedence
			StringBuilder alternatePrecedenceTemporaryViolationReactionQuery = new StringBuilder();

			if(segmentedBy == true) {
	
				alternatePrecedenceTemporaryViolationReactionQuery.append("CONTEXT SegmentedById ");
			
			}	
				
			if(corActivationPayload != null) {
				alternatePrecedenceTemporaryViolationReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar, b.correlationActivation as correlationActivation FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') -> b=middleLayer(actOrTar='violation', a.constraint=constraint) and not middleLayer(actOrTar='activation', a.constraint=constraint) and not middleLayer(actOrTar='target', a.constraint=constraint)]");
			}
			else {
				alternatePrecedenceTemporaryViolationReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') -> b=middleLayer(actOrTar='violation', a.constraint=constraint) and not middleLayer(actOrTar='activation', a.constraint=constraint) and not middleLayer(actOrTar='target', a.constraint=constraint)]");
			}
			
			// Query to react to the Fulfillment of Alternate Precedence
			StringBuilder alternatePrecedenceTargetReactionQuery = new StringBuilder();
			
			if(segmentedBy == true) {
	
				alternatePrecedenceTargetReactionQuery.append("CONTEXT SegmentedById ");
			
			}	
				
			if(corActivationPayload != null) {
				alternatePrecedenceTargetReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar, b.correlationActivation as correlationActivation FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='violation') ->  b=middleLayer(actOrTar='target', a.constraint=constraint)]");
			}
			else {
				alternatePrecedenceTargetReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='violation') ->  b=middleLayer(actOrTar='target', a.constraint=constraint)]");
			}
			
			// Query to react to the Violation of Alternate Precedence
			StringBuilder alternatePrecedenceViolationReactionQuery = new StringBuilder();
			
			if(segmentedBy == true) {
	
				alternatePrecedenceViolationReactionQuery.append("CONTEXT SegmentedById ");
			
			}	
				
			if(corActivationPayload != null) {
				alternatePrecedenceViolationReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar, b.correlationActivation as correlationActivation FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='target') ->  b=middleLayer(actOrTar='violation', a.constraint=constraint) AND NOT middleLayer(actOrTar='activation', a.constraint=constraint)]");
			}
			else {
				alternatePrecedenceViolationReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='target') ->  b=middleLayer(actOrTar='violation', a.constraint=constraint) AND NOT middleLayer(actOrTar='activation', a.constraint=constraint)]");
			}


			// Query to react to a Temporary Violation of Alternate Precedence, that is not followed by a Fulfillment and not violated directly
			StringBuilder alternatePrecedenceFinalViolationReactionQuery = new StringBuilder();

				if(segmentedBy == true) {
		
					alternatePrecedenceFinalViolationReactionQuery.append("CONTEXT SegmentedById ");
				
				}	
					
				if(corActivationPayload != null) {
					alternatePrecedenceFinalViolationReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar, a.correlationActivation as correlationActivation FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='violation') ->  ");
					
					alternatePrecedenceFinalViolationReactionQuery.append("(timer:interval(1 sec) AND NOT ");

					
					alternatePrecedenceFinalViolationReactionQuery.append("b=middleLayer(actOrTar='target', a.constraint=constraint, correlationActivation = a.correlationActivation))]");
				}
				else {
					alternatePrecedenceFinalViolationReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='violation') ->  ");
					
					alternatePrecedenceFinalViolationReactionQuery.append("(timer:interval(1 sec) AND NOT ");

					alternatePrecedenceFinalViolationReactionQuery.append("b=middleLayer(actOrTar='target', a.constraint=constraint))]");
				}
			
		
			// Insert the builded EPL Queries into the eplQueries Table	
			Object [] AlternatePrecedenceQueries = {(String) constraintsAndConditionsList.getValueAt(x, 0), alternatePrecedenceActivation, alternatePrecedenceFulfillment, alternatePrecedenceViolation, alternatePrecedenceTemporaryViolation, alternatePrecedenceActivationReactionQuery, alternatePrecedenceTargetReactionQuery, alternatePrecedenceViolationReactionQuery, null, alternatePrecedenceTemporaryViolationReactionQuery, alternatePrecedenceFinalViolationReactionQuery};
			eplQueries.addRow(AlternatePrecedenceQueries);
				
		}
			
		else if (constraint.equals("Responded Existence")){
			
			// Query to check, if Responded Existence is activated
			StringBuilder respondedExistenceActivation = new StringBuilder();

			respondedExistenceActivation.append("INSERT INTO middleLayer SELECT ");
				
			if(segmentedBy == true) {
					for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
						if(CreateContextScreen.segmentedListTable.getValueAt(i, 0)==activation) {
							identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
							respondedExistenceActivation.append(identifier + ",");
						}
					}
				}
			
			
			respondedExistenceActivation.append("'" + constraintName + "' as constraint, 'activation' as actOrTar");

			if(corActivationPayload != null) {
				respondedExistenceActivation.append(", " + corActivationPayload + " as correlationActivation");
			}

			respondedExistenceActivation.append(" FROM " + activation);
													
			
			if(activationConditionPayload != null) {
				respondedExistenceActivation.append(" WHERE " + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue);
			}

			
			// Query to check, if Responded Existence is fulfilled in the future
			StringBuilder respondedExistenceFutureTarget = new StringBuilder();
				
			if(segmentedBy == true) {
					respondedExistenceFutureTarget.append("CONTEXT SegmentedById ");
				}
									
				
			respondedExistenceFutureTarget.append("INSERT INTO middleLayer SELECT ");
				
			if(segmentedBy==true) {
				
				for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
					if(CreateContextScreen.segmentedListTable.getValueAt(i, 0)==target) {
						identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
						respondedExistenceFutureTarget.append("b." + identifier + " as id, ");
					}
				}
			}
				
			
			respondedExistenceFutureTarget.append("'" + constraintName + "' as constraint, 'target' as actOrTar");
						
			if(corActivationPayload != null) {
				respondedExistenceFutureTarget.append(", a." + corActivationPayload + " as correlationActivation");
			}

			respondedExistenceFutureTarget.append(" FROM PATTERN [every a= " + activation);
					
			if(activationConditionPayload != null) {
				respondedExistenceFutureTarget.append(" (" + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue +")");
			}
			
			respondedExistenceFutureTarget.append(" -> every b= " + target);
			
			if(targetConditionPayload != null) {
				respondedExistenceFutureTarget.append(" (" + targetConditionPayload + " " + targetConditionOperator + " " + targetConditionValue + ")");
			}
			
			respondedExistenceFutureTarget.append("]");
			
			if(corActivationPayload != null) {
				respondedExistenceFutureTarget.append(" WHERE a." + corActivationPayload + " " +  corOperator + " b." + corTargetPayload);
			}
			
			// Query to check, if Responded Existence was fulfilled in the past
			StringBuilder respondedExistencePastTarget = new StringBuilder();
			
			if(segmentedBy == true) {
				respondedExistencePastTarget.append("CONTEXT SegmentedById ");
			}
								
			
			respondedExistencePastTarget.append("INSERT INTO middleLayer SELECT ");
			
			if(segmentedBy==true) {
				
				for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
					if(CreateContextScreen.segmentedListTable.getValueAt(i, 0)==target) {
						identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
						respondedExistencePastTarget.append("a." + identifier + " as id, ");
					}
				}
			}
			
			
			respondedExistencePastTarget.append("'" + constraintName + "' as constraint, 'target' as actOrTar");

			if(corActivationPayload != null) {
				respondedExistencePastTarget.append(", a." + corTargetPayload + " as correlationActivation");
			}
			
			respondedExistencePastTarget.append(" FROM PATTERN [every a= " + target);
		
			if(targetConditionPayload != null) {
				respondedExistencePastTarget.append(" (" + targetConditionPayload + " " + targetConditionOperator + " " + targetConditionValue + ")");
			}
			
			
			respondedExistencePastTarget.append(" -> every b= " + activation);
			
			if(activationConditionPayload != null) {
				respondedExistencePastTarget.append(" (" + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue +")");
			}

			if(timeSpan != null){
				respondedExistencePastTarget.append(" AND NOT timer:interval(" + timeSpan + " " + timeSpanUnit + ")");
			}
			
			respondedExistencePastTarget.append("]");
			
			if(corActivationPayload != null) {
				respondedExistencePastTarget.append(" WHERE a." + corTargetPayload + " " +  corOperator + " b." + corActivationPayload);
			}
	
			// Query to react to the Activation of Responded Existence
			String respondedExistenceActivationReactionQuery;
			
			if(corActivationPayload != null) {
				respondedExistenceActivationReactionQuery = "SELECT id, constraint, actOrTar, correlationActivation FROM middleLayer WHERE actOrTar = 'activation' AND constraint = '" + constraintName + "'";
			}
			else {
				respondedExistenceActivationReactionQuery = "SELECT id, constraint, actOrTar FROM middleLayer WHERE actOrTar = 'activation' AND constraint = '" + constraintName + "'";
			}
			
			// Query to react to the Fulfillment of Responded Existence
			StringBuilder respondedExistenceTargetReactionQuery = new StringBuilder();
					
			if(segmentedBy == true) {
	
				respondedExistenceTargetReactionQuery.append("CONTEXT SegmentedById ");
			
			}	
				
			if(corActivationPayload != null) {
				respondedExistenceTargetReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar, b.correlationActivation as correlationActivation FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') -> every b=middleLayer(actOrTar='target', a.constraint=constraint)]");
			}
			else {
				respondedExistenceTargetReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') -> every b=middleLayer(actOrTar='target', a.constraint=constraint)]");
			}

			// Query to react to the Violation of Responded Existence
			StringBuilder respondedExistenceViolationReactionQuery = new StringBuilder();
							
			if(segmentedBy == true) {
	
				respondedExistenceViolationReactionQuery.append("CONTEXT SegmentedById ");
			
			}	
				
			if(corActivationPayload != null) {
				respondedExistenceViolationReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar, b.correlationActivation as correlationActivation FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') -> every b=middleLayer(actOrTar='violation', a.constraint=constraint)]");
			}
			else {
				respondedExistenceViolationReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') -> every b=middleLayer(actOrTar='violation', a.constraint=constraint)]");
			}

			// Query to react to the TimeSpan Violation of Responded Existence
			StringBuilder respondedExistenceTimeSpanViolationReactionQuery = new StringBuilder();
	
			if(timeSpan != null){
				if(segmentedBy == true) {
		
					respondedExistenceTimeSpanViolationReactionQuery.append("CONTEXT SegmentedById ");
				
				}	
					
				if(corActivationPayload != null) {
					respondedExistenceTimeSpanViolationReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar, a.correlationActivation as correlationActivation FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') ->  ");
					
					respondedExistenceTimeSpanViolationReactionQuery.append("(timer:interval(" + timeSpan + " " + timeSpanUnit + ") AND NOT ");

					
					respondedExistenceTimeSpanViolationReactionQuery.append("b=middleLayer(actOrTar='target', a.constraint=constraint))]");
				}
				else {
					respondedExistenceTimeSpanViolationReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') ->  ");
					
					respondedExistenceTimeSpanViolationReactionQuery.append("(timer:interval(" + timeSpan + " " + timeSpanUnit + ") AND NOT ");

					respondedExistenceTimeSpanViolationReactionQuery.append("b=middleLayer(actOrTar='target', a.constraint=constraint))]");
				}
			}
			
			
			// Insert the builded EPL Queries into the eplQueries Table					
			Object [] RespondedExistenceQueries = {(String) constraintsAndConditionsList.getValueAt(x, 0), respondedExistenceActivation, respondedExistenceFutureTarget, respondedExistencePastTarget, null, respondedExistenceActivationReactionQuery, respondedExistenceTargetReactionQuery, respondedExistenceViolationReactionQuery, respondedExistenceTimeSpanViolationReactionQuery};
			eplQueries.addRow(RespondedExistenceQueries);
				
		}
			
		else if (constraint.equals("Chain Response")){

			// Query to check, if Chain Response is activated
			StringBuilder chainResponseActivation = new StringBuilder();
	
			chainResponseActivation.append("INSERT INTO middleLayer SELECT ");
			
			if(segmentedBy == true) {
				for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
					if(CreateContextScreen.segmentedListTable.getValueAt(i, 0)==activation) {
						identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
						chainResponseActivation.append(identifier + ",");
					}
				}
			}		
			
			chainResponseActivation.append("'" + constraintName + "' as constraint, 'activation' as actOrTar");

			if(corActivationPayload != null) {
				chainResponseActivation.append(", " + corActivationPayload + " as correlationActivation");
			}

			chainResponseActivation.append(" FROM " + activation);
																
			if(activationConditionPayload != null) {
				chainResponseActivation.append(" WHERE " + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue);
			}
				
			// Query to check, if Chain Response is fulfilled
			StringBuilder chainResponseTarget = new StringBuilder();
				
			if(segmentedBy == true) {
					chainResponseTarget.append("CONTEXT SegmentedById ");
				}
				
			chainResponseTarget.append("INSERT INTO middleLayer SELECT ");
				
			if(segmentedBy==true) {
				
				for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
					if(CreateContextScreen.segmentedListTable.getValueAt(i, 0)==target) {
						identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
						chainResponseTarget.append("b." + identifier + " as id, ");
					}
				}
			}
			
			
			chainResponseTarget.append("'" + constraintName + "' as constraint, 'target' as actOrTar");
			
			if(corActivationPayload != null) {
				chainResponseTarget.append(", b." + corTargetPayload + " as correlationActivation");
			}
			
			chainResponseTarget.append(" FROM PATTERN [every a= " + activation);
					
			if(activationConditionPayload != null) {
				chainResponseTarget.append(" (" + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue +")");
			}
			
			chainResponseTarget.append("-> b=" + target);
			
			if(targetConditionPayload != null && corActivationPayload != null) {
				chainResponseTarget.append("(" + targetConditionPayload + " " + targetConditionOperator + " " + targetConditionValue);
				chainResponseTarget.append(", a." + corActivationPayload + " " +  corOperator + " " + corTargetPayload + ") ");
				chainResponseTarget.append(" AND NOT " + target);
			}
			
			if(targetConditionPayload != null && corActivationPayload == null) {
				chainResponseTarget.append("(" + targetConditionPayload + " " + targetConditionOperator + " " + targetConditionValue + ") ");
				chainResponseTarget.append(" AND NOT " + target);
			}

			if(targetConditionPayload == null && corActivationPayload != null) {
				chainResponseTarget.append("( a." + corActivationPayload + " " +  corOperator + " " + corTargetPayload + ") ");
				chainResponseTarget.append(" AND NOT " + target);
			}
			
			chainResponseTarget.append(" AND NOT " + activation);
			
			for(int i=0; i<AddEventScreen.eventList.getRowCount(); i++) {
				if(AddEventScreen.eventList.getValueAt(i, 0) != activation && AddEventScreen.eventList.getValueAt(i, 0) != target) {
					
					chainResponseTarget.append(" AND NOT " + AddEventScreen.eventList.getValueAt(i, 0));
				}
			}	
				
			chainResponseTarget.append("]");
				
			// Query to check, if Chain Response is violated
			StringBuilder chainResponseViolation = new StringBuilder();
			
			if(segmentedBy == true) {
				chainResponseViolation.append("CONTEXT SegmentedById ");
			}
			chainResponseViolation.append("INSERT INTO middleLayer SELECT ");
			
			if(segmentedBy==true) {
				
				for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
					if(CreateContextScreen.segmentedListTable.getValueAt(i, 0)==target) {
						identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
						chainResponseViolation.append("a." + identifier + " as id, ");
					}
				}
			}
			
			chainResponseViolation.append("'" + constraintName + "' as constraint, 'violation' as actOrTar");

			if(corActivationPayload != null) {
				chainResponseViolation.append(", a." + corTargetPayload + " as correlationActivation");
			}
			
			chainResponseViolation.append(" FROM PATTERN [every a= " + activation);
							
			if(activationConditionPayload != null) {
				chainResponseViolation.append(" (" + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue +")");
			}
			
			chainResponseViolation.append(" -> (" + activation);
			
			for(int i=0; i<AddEventScreen.eventList.getRowCount(); i++) {
				if(AddEventScreen.eventList.getValueAt(i, 0) != activation) {
					if(AddEventScreen.eventList.getValueAt(i, 0).equals(target)) {
						if(targetConditionPayload != null || corActivationPayload != null){
							chainResponseViolation.append(" OR " + AddEventScreen.eventList.getValueAt(i, 0));
						}
					}	
					else{
					chainResponseViolation.append(" OR " + AddEventScreen.eventList.getValueAt(i, 0));
					}
				}
			}

			chainResponseViolation.append(") AND NOT b=" + target);
			
			if(targetConditionPayload != null && corActivationPayload != null) {
				chainResponseViolation.append(" (" + targetConditionPayload + " " + targetConditionOperator + " " + targetConditionValue);
				chainResponseViolation.append(", " + corTargetPayload + " " +  corOperator + " a." + corActivationPayload + ")");
			}
			else if(targetConditionPayload != null && corActivationPayload == null) {
				chainResponseViolation.append(" (" + targetConditionPayload + " " + targetConditionOperator + " " + targetConditionValue + ")");
			}
			else if(targetConditionPayload == null && corActivationPayload != null) {
				chainResponseViolation.append("(a." + corActivationPayload + " " +  corOperator + " " + corTargetPayload + ")");
			}
			chainResponseViolation.append("]");
			
			
			// Query to react to the Activation of Chain Response
			String chainReponseActivationReactionQuery;
			
			if(corActivationPayload != null) {
				chainReponseActivationReactionQuery = "SELECT id, constraint, actOrTar, correlationActivation FROM middleLayer WHERE actOrTar = 'activation' AND constraint = '" + constraintName + "'";
			}
			else {
				chainReponseActivationReactionQuery = "SELECT id, constraint, actOrTar FROM middleLayer WHERE actOrTar = 'activation' AND constraint = '" + constraintName + "'";
			}
			
			// Query to react to the Fulfillment of Chain Response
			StringBuilder chainReponseTargetReactionQuery = new StringBuilder();
			
			if(segmentedBy == true) {
	
				chainReponseTargetReactionQuery.append("CONTEXT SegmentedById ");
			
			}	
				
			if(corActivationPayload != null) {
				chainReponseTargetReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar, b.correlationActivation as correlationActivation FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') -> every b=middleLayer(actOrTar='target', a.constraint=constraint)]");
			}
			else {
				chainReponseTargetReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') -> every b=middleLayer(actOrTar='target', a.constraint=constraint)]");
			}
			
			// Query to react to the Violation of Chain Response
			StringBuilder chainResponseViolationReactionQuery = new StringBuilder();			
			
			if(segmentedBy == true) {
	
				chainResponseViolationReactionQuery.append("CONTEXT SegmentedById ");
			
			}	
				
			if(corActivationPayload != null) {
				chainResponseViolationReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar, b.correlationActivation as correlationActivation FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') -> every b=middleLayer(actOrTar='violation', a.constraint=constraint)]");
			}
			else {
				chainResponseViolationReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') -> every b=middleLayer(actOrTar='violation', a.constraint=constraint)]");
			}

			// Query to react to the TimeSpan Violation of Chain Response
			StringBuilder chainResponseTimeSpanViolationReactionQuery = new StringBuilder();
				
			if(timeSpan != null){
				if(segmentedBy == true) {
		
					chainResponseTimeSpanViolationReactionQuery.append("CONTEXT SegmentedById ");
				
				}	
					
				if(corActivationPayload != null) {
					chainResponseTimeSpanViolationReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar, a.correlationActivation as correlationActivation FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') ->  ");
					
					chainResponseTimeSpanViolationReactionQuery.append("(timer:interval(" + timeSpan + " " + timeSpanUnit + ") AND NOT ");

					
					chainResponseTimeSpanViolationReactionQuery.append("b=middleLayer(actOrTar='target', a.constraint=constraint))]");
				}
				else {
					chainResponseTimeSpanViolationReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') ->  ");
					
					chainResponseTimeSpanViolationReactionQuery.append("(timer:interval(" + timeSpan + " " + timeSpanUnit + ") AND NOT ");

					chainResponseTimeSpanViolationReactionQuery.append("b=middleLayer(actOrTar='target', a.constraint=constraint))]");
				}
			}

			// Insert the builded EPL Queries into the eplQueries Table	
			Object [] ChainResponseQueries = {(String) constraintsAndConditionsList.getValueAt(x, 0), chainResponseActivation, chainResponseTarget, chainResponseViolation, null, chainReponseActivationReactionQuery, chainReponseTargetReactionQuery, chainResponseViolationReactionQuery, chainResponseTimeSpanViolationReactionQuery};
			eplQueries.addRow(ChainResponseQueries);
			
		}
		
		else if (constraint.equals("Chain Precedence")){
			
			// Query to check, if Chain Precedence is activated
			StringBuilder chainPrecedenceActivation = new StringBuilder();
	
			chainPrecedenceActivation.append("INSERT INTO middleLayer SELECT ");
			
			if(segmentedBy == true) {
				for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
					if(CreateContextScreen.segmentedListTable.getValueAt(i, 0)==activation) {
						identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
						chainPrecedenceActivation.append(identifier + ",");
					}
				}
			}
			
			
			chainPrecedenceActivation.append("'" + constraintName + "' as constraint, 'activation' as actOrTar");

			if(corActivationPayload != null) {
				chainPrecedenceActivation.append(", " + corActivationPayload + " as correlationActivation");
			}

			chainPrecedenceActivation.append(" FROM " + activation);
						
			if(activationConditionPayload != null) {
				chainPrecedenceActivation.append(" WHERE " + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue);
			}
			
			// Query to check, if Chain Precedence is fulfilled
			StringBuilder chainPrecedenceTarget = new StringBuilder();
			
			if(segmentedBy == true) {
				chainPrecedenceTarget.append("CONTEXT SegmentedById ");
			}
			chainPrecedenceTarget.append("INSERT INTO middleLayer SELECT ");
			
			if(segmentedBy==true) {
				
				for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
					if(CreateContextScreen.segmentedListTable.getValueAt(i, 0)==target) {
						identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
						chainPrecedenceTarget.append("a." + identifier + " as id, ");
					}
				}
			}
			
			chainPrecedenceTarget.append("'" + constraintName + "' as constraint, 'target' as actOrTar");
	
			if(corActivationPayload != null) {
				chainPrecedenceTarget.append(", a." + corTargetPayload + " as correlationActivation");
			}

			chainPrecedenceTarget.append(" FROM PATTERN [every a= " + target);
							
			if(targetConditionPayload != null) {
				chainPrecedenceTarget.append(" (" + targetConditionPayload + " " + targetConditionOperator + " " + targetConditionValue +")");
			}
			
			chainPrecedenceTarget.append("-> b=" + activation);
			
			if(activationConditionPayload != null) {
				chainPrecedenceTarget.append(" (" + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue + ")");
				chainPrecedenceTarget.append(" AND NOT " + target);
			}
			
			
			chainPrecedenceTarget.append(" AND NOT " + target);
			
			
			
			for(int i=0; i<AddEventScreen.eventList.getRowCount(); i++) {
				if(AddEventScreen.eventList.getValueAt(i, 0) != target && AddEventScreen.eventList.getValueAt(i, 0) != activation) {
					
					chainPrecedenceTarget.append(" AND NOT " + AddEventScreen.eventList.getValueAt(i, 0));
				}
			}
			
			if(timeSpan != null){
				chainPrecedenceTarget.append(" AND NOT timer:interval(" + timeSpan + " " + timeSpanUnit + ")");
			}
				
			chainPrecedenceTarget.append("]");
				
			if(corActivationPayload != null) {
				chainPrecedenceTarget.append(" WHERE a." + corTargetPayload + " " +  corOperator + " b." + corActivationPayload);
			}
			
			// Query to set Chain Precedence as temporary violated
			StringBuilder chainPrecedenceViolation = new StringBuilder();
			
			chainPrecedenceViolation.append("INSERT INTO middleLayer SELECT ");
			
			if(segmentedBy == true) {
				for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
					if(CreateContextScreen.segmentedListTable.getValueAt(i, 0)==activation) {
						identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
						chainPrecedenceViolation.append(identifier + ",");
					}
				}
			}
			
			
			chainPrecedenceViolation.append("'" + constraintName + "' as constraint, 'violation' as actOrTar");

			if(corActivationPayload != null) {
				chainPrecedenceViolation.append(", " + corActivationPayload + " as correlationActivation");
			}

			chainPrecedenceViolation.append(" FROM " + activation);
						
			if(activationConditionPayload != null) {
				chainPrecedenceViolation.append(" WHERE " + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue);
			}		
			
			// Query to react to the Activation of Chain Precedence
			String chainPrecedenceActivationReactionQuery;
			
			if(corActivationPayload != null) {
				chainPrecedenceActivationReactionQuery = "SELECT id, constraint, actOrTar, correlationActivation FROM middleLayer WHERE actOrTar = 'activation' AND constraint = '" + constraintName + "'";
			}
			else {
				chainPrecedenceActivationReactionQuery = "SELECT id, constraint, actOrTar FROM middleLayer WHERE actOrTar = 'activation' AND constraint = '" + constraintName + "'";
			}
			
			// Query to react to the Fulfillment of Chain Precedence
			StringBuilder chainPrecedenceTargetReactionQuery = new StringBuilder();
			
			if(segmentedBy == true) {
	
				chainPrecedenceTargetReactionQuery.append("CONTEXT SegmentedById ");
			
			}	
				
			if(corActivationPayload != null) {
				chainPrecedenceTargetReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar, b.correlationActivation as correlationActivation FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='violation') -> b=middleLayer(actOrTar='target', a.constraint=constraint) and not middleLayer(actOrTar='activation', a.constraint=constraint) and not middleLayer(actOrTar='violation', a.constraint=constraint)]");
			}
			else {
				chainPrecedenceTargetReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='violation') -> b=middleLayer(actOrTar='target', a.constraint=constraint) and not middleLayer(actOrTar='activation', a.constraint=constraint) and not middleLayer(actOrTar='violation', a.constraint=constraint)]");
			}
			
			// Query to react to the Temporary Violation of Chain Precedence
			StringBuilder chainPrecedenceTemporaryViolationReactionQuery = new StringBuilder();
			
			if(segmentedBy == true) {
	
				chainPrecedenceTemporaryViolationReactionQuery.append("CONTEXT SegmentedById ");
			
			}	
				
			if(corActivationPayload != null) {
				chainPrecedenceTemporaryViolationReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar, b.correlationActivation as correlationActivation FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') -> b=middleLayer(actOrTar='violation', a.constraint=constraint) and not middleLayer(actOrTar='activation', a.constraint=constraint) and not middleLayer(actOrTar='violation', a.constraint=constraint)]");
			}
			else {
				chainPrecedenceTemporaryViolationReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') -> b=middleLayer(actOrTar='violation', a.constraint=constraint) and not middleLayer(actOrTar='activation', a.constraint=constraint) and not middleLayer(actOrTar='violation', a.constraint=constraint)]");
			}

			// Query to react to a Temporary Violation of Chain Precedence, that is not followed by a Fulfillment
			StringBuilder chainPrecedenceViolationReactionQuery = new StringBuilder();

			if(segmentedBy == true) {
		
				chainPrecedenceViolationReactionQuery.append("CONTEXT SegmentedById ");
			
			}	
				
			if(corActivationPayload != null) {
				chainPrecedenceViolationReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar, a.correlationActivation as correlationActivation FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='violation') ->  ");
				
				chainPrecedenceViolationReactionQuery.append("(timer:interval(1 sec) AND NOT ");

				
				chainPrecedenceViolationReactionQuery.append("b=middleLayer(actOrTar='target', a.constraint=constraint))]");
			}
			else {
				chainPrecedenceViolationReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='violation') ->  ");
				
				chainPrecedenceViolationReactionQuery.append("(timer:interval(1 sec) AND NOT ");

				chainPrecedenceViolationReactionQuery.append("b=middleLayer(actOrTar='target', a.constraint=constraint))]");
			}
			
			// Insert the builded EPL Queries into the eplQueries Table	
			Object [] ChainPrecedenceQueries = {(String) constraintsAndConditionsList.getValueAt(x, 0), chainPrecedenceActivation, chainPrecedenceTarget, chainPrecedenceViolation, chainPrecedenceActivationReactionQuery, chainPrecedenceTargetReactionQuery, chainPrecedenceTemporaryViolationReactionQuery, chainPrecedenceViolationReactionQuery};
			eplQueries.addRow(ChainPrecedenceQueries);
			
		}
			
		else if (constraint.equals("Not Response")){
			
			// Query to check, if NotResponse is activated
			StringBuilder notResponseActivation = new StringBuilder();

			notResponseActivation.append("INSERT INTO middleLayer SELECT ");
				
				if(segmentedBy == true) {
					for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
						if(CreateContextScreen.segmentedListTable.getValueAt(i, 0)==activation) {
							identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
							notResponseActivation.append(identifier + ",");
						}
					}
				}
				
				notResponseActivation.append(" '" + constraintName + "' as constraint, 'activation' as actOrTar");
				
				if(corActivationPayload != null) {
					notResponseActivation.append(", " + corActivationPayload + " as correlationActivation");
				}
	
				notResponseActivation.append(" FROM " + activation);
				
				if(activationConditionPayload != null) {
					notResponseActivation.append(" WHERE " + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue);
				}

			// Query to set Not Response as temporary fulfilled
			StringBuilder notResponseFulfillment = new StringBuilder();

			notResponseFulfillment.append("INSERT INTO middleLayer SELECT ");
				
				if(segmentedBy == true) {
					for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
						if(CreateContextScreen.segmentedListTable.getValueAt(i, 0)==activation) {
							identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
							notResponseFulfillment.append(identifier + ",");
						}
					}
				}
				
				notResponseFulfillment.append(" '" + constraintName + "' as constraint, 'target' as actOrTar");
				
				if(corActivationPayload != null) {
					notResponseFulfillment.append(", " + corActivationPayload + " as correlationActivation");
				}
	
				notResponseFulfillment.append(" FROM " + activation);
				
				if(activationConditionPayload != null) {
					notResponseFulfillment.append(" WHERE " + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue);
				}

			// Query to check, if Not Response is violated
			StringBuilder notResponseViolation = new StringBuilder();
				
				if(segmentedBy == true) {
					notResponseViolation.append("CONTEXT SegmentedById ");
				}
									
				
				notResponseViolation.append("INSERT INTO middleLayer SELECT ");
				
				if(segmentedBy==true) {
					
					for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
						if(CreateContextScreen.segmentedListTable.getValueAt(i, 0)==target) {
							identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
							notResponseViolation.append("b." + identifier + " as id, ");
						}
					}
				}
				
				notResponseViolation.append("'" + constraintName + "' as constraint, 'violation' as actOrTar");

				if(corActivationPayload != null) {
					notResponseViolation.append(", a." + corActivationPayload + " as correlationActivation");
				}
				
				notResponseViolation.append(" FROM PATTERN [every a= " + activation);
			
				if(activationConditionPayload != null) {
					notResponseViolation.append(" (" + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue +")");
				}
				
				notResponseViolation.append(" -> b= " + target);
				
				if(targetConditionPayload != null) {
					notResponseViolation.append(" (" + targetConditionPayload + " " + targetConditionOperator + " " + targetConditionValue + ")");
				}

				if(timeSpan != null){
					notResponseViolation.append(" AND NOT timer:interval(" + timeSpan + " " + timeSpanUnit + ")");
				}
				
				notResponseViolation.append("]");
				
				if(corActivationPayload != null) {
					notResponseViolation.append(" WHERE a." + corActivationPayload + " " +  corOperator + " b." + corTargetPayload);
				}
				

			// Query to react to the Activation of Not Response
			String notResponseActivationReactionQuery;

				if(corActivationPayload != null) {
					notResponseActivationReactionQuery = "SELECT id, constraint, actOrTar, correlationActivation FROM middleLayer WHERE actOrTar = 'activation'  AND constraint = '" + constraintName + "'";
				}
				else {
					notResponseActivationReactionQuery = "SELECT id, constraint, actOrTar FROM middleLayer WHERE actOrTar = 'activation' AND constraint = '" + constraintName + "'";
				}

			// Query to react to the Fulfillment of Response
			StringBuilder notResponseTargetReactionQuery = new StringBuilder();
				
			if(segmentedBy == true) {
	
				notResponseTargetReactionQuery.append("CONTEXT SegmentedById ");
			
			}	
				
			if(corActivationPayload != null) {
				notResponseTargetReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar, b.correlationActivation as correlationActivation FROM PATTERN [every (a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') -> every b=middleLayer(actOrTar='target', a.constraint=constraint))]");
			}
			else {
				notResponseTargetReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar FROM PATTERN [every (a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') -> every b=middleLayer(actOrTar='target', a.constraint=constraint))]");
			}

			
			// Query to react to the Violation of Not Response
			StringBuilder notResponseViolationReactionQuery = new StringBuilder();
				
				if(segmentedBy == true) {
		
					notResponseViolationReactionQuery.append("CONTEXT SegmentedById ");
				
				}	
					
				if(corActivationPayload != null) {
					notResponseViolationReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar, b.correlationActivation as correlationActivation FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='target') -> every b=middleLayer(actOrTar='violation', a.constraint=constraint)]");
				}
				else {
					notResponseViolationReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='target') -> every b=middleLayer(actOrTar='violation', a.constraint=constraint)]");
				}

		
			// Insert the builded EPL Queries into the eplQueries Table	
			Object [] responseQueries = {(String) constraintsAndConditionsList.getValueAt(x, 0), notResponseActivation, notResponseViolation, notResponseFulfillment, null, notResponseActivationReactionQuery, notResponseViolationReactionQuery, notResponseTargetReactionQuery, null};
			eplQueries.addRow(responseQueries);
						
								
		}
			
		else if (constraint.equals("Not Precedence")){
			
			// Query to check, if Not Precedence is activated
			StringBuilder notPrecedenceActivation = new StringBuilder();

				notPrecedenceActivation.append("INSERT INTO middleLayer SELECT ");
					
				if(segmentedBy == true) {
						for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
							if(CreateContextScreen.segmentedListTable.getValueAt(i, 0)==activation) {
								identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
								notPrecedenceActivation.append(identifier + ",");
							}
						}
					}
				
					notPrecedenceActivation.append(" '" + constraintName + "' as constraint, 'activation' as actOrTar");
			
				if(corActivationPayload != null) {
					notPrecedenceActivation.append(", " + corActivationPayload + " as correlationActivation");
				}

				notPrecedenceActivation.append(" FROM " + activation);	
				
				if(activationConditionPayload != null) {
					notPrecedenceActivation.append(" WHERE " + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue);
				}

			// Query to set Not Precedence as temporary fulfilled
			StringBuilder notPrecedenceFulfillment = new StringBuilder();


			notPrecedenceFulfillment.append("INSERT INTO middleLayer SELECT ");
						
				if(segmentedBy == true) {
						for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
							if(CreateContextScreen.segmentedListTable.getValueAt(i, 0)==activation) {
								identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
								notPrecedenceFulfillment.append(identifier + ",");
							}
						}
					}
				
					notPrecedenceFulfillment.append(" '" + constraintName + "' as constraint, 'target' as actOrTar");
			
				if(corActivationPayload != null) {
					notPrecedenceFulfillment.append(", " + corActivationPayload + " as correlationActivation");
				}

				notPrecedenceFulfillment.append(" FROM " + activation);	
				
				if(activationConditionPayload != null) {
					notPrecedenceFulfillment.append(" WHERE " + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue);
				}

			// Query to check, if Not Precedence is violated
			StringBuilder notPrecedenceViolation = new StringBuilder();
				
				if(segmentedBy == true) {
					notPrecedenceViolation.append("CONTEXT SegmentedById ");
				}
								
				notPrecedenceViolation.append("INSERT INTO middleLayer SELECT ");
				
				if(segmentedBy==true) {
					
					for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
						if(CreateContextScreen.segmentedListTable.getValueAt(i, 0)==target) {
							identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
							notPrecedenceViolation.append("b." + identifier + " as id, ");
						}
					}
				}
				
				notPrecedenceViolation.append("'" + constraintName + "' as constraint, 'violation' as actOrTar");
				
				if(corActivationPayload != null) {
					notPrecedenceViolation.append(", b." + corActivationPayload + " as correlationActivation");
				}

				notPrecedenceViolation.append(" FROM PATTERN [a= " + target);
				
				if(targetConditionPayload != null) {
					notPrecedenceViolation.append(" (" + targetConditionPayload + " " + targetConditionOperator + " " + targetConditionValue + ")");
				}
				
				
				notPrecedenceViolation.append(" -> every b= " + activation);
					
				if(activationConditionPayload != null) {
					notPrecedenceViolation.append(" (" + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue +")");
				}

				if(timeSpan != null){
					notPrecedenceViolation.append(" AND NOT timer:interval(" + timeSpan + " " + timeSpanUnit + ")");
				}
				
				notPrecedenceViolation.append("]");
				
				if(corActivationPayload != null) {
					notPrecedenceViolation.append(" WHERE a." + corTargetPayload + " " +  corOperator + " b." + corActivationPayload);
				}
				
			// Query to react to the Activation of Not Precedence
			String notPrecedenceActivationReactionQuery;
				
				if(corActivationPayload != null) {
					notPrecedenceActivationReactionQuery = "SELECT id, constraint, actOrTar, correlationActivation FROM middleLayer WHERE actOrTar = 'activation' AND constraint = '" + constraintName + "'";
				}
				else {
					notPrecedenceActivationReactionQuery = "SELECT id, constraint, actOrTar FROM middleLayer WHERE actOrTar = 'activation' AND constraint = '" + constraintName + "'";
				}

			// Query to react to the Temporary Fulfillment of Not Precedence
			StringBuilder notPrecedenceTemporaryFulfillmentReactionQuery = new StringBuilder();

				if(segmentedBy == true) {
		
					notPrecedenceTemporaryFulfillmentReactionQuery.append("CONTEXT SegmentedById ");
				
				}	
					
				if(corActivationPayload != null) {
					notPrecedenceTemporaryFulfillmentReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar, b.correlationActivation as correlationActivation FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') -> b=middleLayer(actOrTar='target', a.constraint=constraint) and not middleLayer(actOrTar='activation', a.constraint=constraint) and not middleLayer(actOrTar='violation', a.constraint=constraint)]");
				}
				else {
					notPrecedenceTemporaryFulfillmentReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') -> b=middleLayer(actOrTar='target', a.constraint=constraint) and not middleLayer(actOrTar='activation', a.constraint=constraint) and not middleLayer(actOrTar='violation', a.constraint=constraint)]");
				}
			
			// Query to react to the Violation of Not Precedence
			StringBuilder notPrecedenceViolationReactionQuery = new StringBuilder();
				
				if(segmentedBy == true) {
	
					notPrecedenceViolationReactionQuery.append("CONTEXT SegmentedById ");
				
				}	
					
				if(corActivationPayload != null) {
					notPrecedenceViolationReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar, b.correlationActivation as correlationActivation FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='target') -> b=middleLayer(actOrTar='violation', a.constraint=constraint) and not middleLayer(actOrTar='activation', a.constraint=constraint) and not middleLayer(actOrTar='target', a.constraint=constraint)]");
				}
				else {
					notPrecedenceViolationReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='target') -> b=middleLayer(actOrTar='violation', a.constraint=constraint) and not middleLayer(actOrTar='activation', a.constraint=constraint) and not middleLayer(actOrTar='target', a.constraint=constraint)]");
				}

			// Insert the builded EPL Queries into the eplQueries Table					
			Object [] notPrecedenceQueries = {(String) constraintsAndConditionsList.getValueAt(x, 0), notPrecedenceActivation, notPrecedenceFulfillment, notPrecedenceViolation, null, notPrecedenceActivationReactionQuery, notPrecedenceTemporaryFulfillmentReactionQuery, notPrecedenceViolationReactionQuery};
			eplQueries.addRow(notPrecedenceQueries);
			
		}
			
			
		else if (constraint.equals("Not Responded Existence")){
			
			// Query to check, if Not Responded Existence is activated
			StringBuilder notRespondedExistenceActivation = new StringBuilder();

			notRespondedExistenceActivation.append("INSERT INTO middleLayer SELECT ");
				
			if(segmentedBy == true) {
					for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
						if(CreateContextScreen.segmentedListTable.getValueAt(i, 0)==activation) {
							identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
							notRespondedExistenceActivation.append(identifier + ",");
						}
					}
				}
			
			
				notRespondedExistenceActivation.append("'" + constraintName + "' as constraint, 'activation' as actOrTar");

			if(corActivationPayload != null) {
				notRespondedExistenceActivation.append(", " + corActivationPayload + " as correlationActivation");
			}

			notRespondedExistenceActivation.append(" FROM " + activation);
			
			
			if(activationConditionPayload != null) {
				notRespondedExistenceActivation.append(" WHERE " + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue);
			}

			// Query to set Not Responded Existence as temporary fulfilled
			StringBuilder notRespondedExistenceTemporaryFulfillment = new StringBuilder();

			notRespondedExistenceTemporaryFulfillment.append("INSERT INTO middleLayer SELECT ");
				
			if(segmentedBy == true) {
					for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
						if(CreateContextScreen.segmentedListTable.getValueAt(i, 0)==activation) {
							identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
							notRespondedExistenceTemporaryFulfillment.append(identifier + ",");
						}
					}
				}
			
			
				notRespondedExistenceTemporaryFulfillment.append("'" + constraintName + "' as constraint, 'target' as actOrTar");

			if(corActivationPayload != null) {
				notRespondedExistenceTemporaryFulfillment.append(", " + corActivationPayload + " as correlationActivation");
			}

			notRespondedExistenceTemporaryFulfillment.append(" FROM " + activation);
			
			
			if(activationConditionPayload != null) {
				notRespondedExistenceTemporaryFulfillment.append(" WHERE " + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue);
			}


			// Query to check, if Not Responded Existence is violated in the future
			StringBuilder notRespondedExistenceFutureViolation = new StringBuilder();
				
			if(segmentedBy == true) {
				notRespondedExistenceFutureViolation.append("CONTEXT SegmentedById ");
				}
									
				
				notRespondedExistenceFutureViolation.append("INSERT INTO middleLayer SELECT ");
				
			if(segmentedBy==true) {
				
				for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
					if(CreateContextScreen.segmentedListTable.getValueAt(i, 0)==target) {
						identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
						notRespondedExistenceFutureViolation.append("b." + identifier + " as id, ");
					}
				}
			}
				
			
			notRespondedExistenceFutureViolation.append("'" + constraintName + "' as constraint, 'violation' as actOrTar");
						
			if(corActivationPayload != null) {
				notRespondedExistenceFutureViolation.append(", a." + corActivationPayload + " as correlationActivation");
			}

			notRespondedExistenceFutureViolation.append(" FROM PATTERN [every a= " + activation);
					
			if(activationConditionPayload != null) {
				notRespondedExistenceFutureViolation.append(" (" + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue +")");
			}
			
			notRespondedExistenceFutureViolation.append(" -> every b= " + target);
			
			if(targetConditionPayload != null) {
				notRespondedExistenceFutureViolation.append(" (" + targetConditionPayload + " " + targetConditionOperator + " " + targetConditionValue + ")");
			}

			if(timeSpan != null){
				notRespondedExistenceFutureViolation.append(" AND NOT timer:interval(" + timeSpan + " " + timeSpanUnit + ")");
			}
			
			notRespondedExistenceFutureViolation.append("]");
			
			if(corActivationPayload != null) {
				notRespondedExistenceFutureViolation.append(" WHERE a." + corActivationPayload + " " +  corOperator + " b." + corTargetPayload);
			}
			
			// Query to check, if Not Responded Existence was violated in the past
			StringBuilder notRespondedExistencePastViolation = new StringBuilder();
			
			if(segmentedBy == true) {
				notRespondedExistencePastViolation.append("CONTEXT SegmentedById ");
			}
								
			
			notRespondedExistencePastViolation.append("INSERT INTO middleLayer SELECT ");
			
			if(segmentedBy==true) {
				
				for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
					if(CreateContextScreen.segmentedListTable.getValueAt(i, 0)==target) {
						identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
						notRespondedExistencePastViolation.append("b." + identifier + " as id, ");
					}
				}
			}
			
			
			notRespondedExistencePastViolation.append("'" + constraintName + "' as constraint, 'violation' as actOrTar");

			if(corActivationPayload != null) {
				notRespondedExistencePastViolation.append(", b." + corActivationPayload + " as correlationActivation");
			}
			
			notRespondedExistencePastViolation.append(" FROM PATTERN [every a= " + target);
		
			if(targetConditionPayload != null) {
				notRespondedExistencePastViolation.append(" (" + targetConditionPayload + " " + targetConditionOperator + " " + targetConditionValue + ")");
			}
			
			
			notRespondedExistencePastViolation.append(" -> every b= " + activation);
			
			if(activationConditionPayload != null) {
				notRespondedExistencePastViolation.append(" (" + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue +")");
			}

			if(timeSpan != null){
				notRespondedExistencePastViolation.append(" AND NOT timer:interval(" + timeSpan + " " + timeSpanUnit + ")");
			}
			
			notRespondedExistencePastViolation.append("]");
			
			if(corActivationPayload != null) {
				notRespondedExistencePastViolation.append(" WHERE a." + corTargetPayload + " " +  corOperator + " b." + corActivationPayload);
			}
	
			// Query to react to the Activation of Not Responded Existence
			String notRespondedExistenceActivationReactionQuery;
			
			if(corActivationPayload != null) {
				notRespondedExistenceActivationReactionQuery = "SELECT id, constraint, actOrTar, correlationActivation FROM middleLayer WHERE actOrTar = 'activation' AND constraint = '" + constraintName + "'";
			}
			else {
				notRespondedExistenceActivationReactionQuery = "SELECT id, constraint, actOrTar FROM middleLayer WHERE actOrTar = 'activation' AND constraint = '" + constraintName + "'";
			}

			// Query to react to the Fulfillment of Not Responded Existence
			StringBuilder notRespondedExistenceTargetReactionQuery = new StringBuilder();
				
			if(segmentedBy == true) {
	
				notRespondedExistenceTargetReactionQuery.append("CONTEXT SegmentedById ");
			
			}	
				
			if(corActivationPayload != null) {
				notRespondedExistenceTargetReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar, b.correlationActivation as correlationActivation FROM PATTERN [every (a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') -> every b=middleLayer(actOrTar='target', a.constraint=constraint))]");
			}
			else {
				notRespondedExistenceTargetReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar FROM PATTERN [every (a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') -> every b=middleLayer(actOrTar='target', a.constraint=constraint))]");
			}
			
			// Query to react to the Violation of Not Responded Existence
			StringBuilder notRespondedExistenceViolationReactionQuery = new StringBuilder();
					
			if(segmentedBy == true) {
	
				notRespondedExistenceViolationReactionQuery.append("CONTEXT SegmentedById ");
			
			}	
				
			if(corActivationPayload != null) {
				notRespondedExistenceViolationReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar, b.correlationActivation as correlationActivation FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='target') -> every b=middleLayer(actOrTar='violation', a.constraint=constraint)]");
			}
			else {
				notRespondedExistenceViolationReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='target') -> every b=middleLayer(actOrTar='violation', a.constraint=constraint)]");
			}


			// Insert the builded EPL Queries into the eplQueries Table		
			Object [] notRespondedExistenceQueries = {(String) constraintsAndConditionsList.getValueAt(x, 0), notRespondedExistenceActivation, notRespondedExistenceFutureViolation, notRespondedExistencePastViolation, notRespondedExistenceTemporaryFulfillment, notRespondedExistenceActivationReactionQuery, notRespondedExistenceTargetReactionQuery, notRespondedExistenceViolationReactionQuery, null};
			eplQueries.addRow(notRespondedExistenceQueries);
			
		}		
			
		else if (constraint.equals("Not Chain Response")){


			// Query to check, if Not Chain Response is activated
			StringBuilder notChainResponseActivation = new StringBuilder();
	
			notChainResponseActivation.append("INSERT INTO middleLayer SELECT ");
			
			if(segmentedBy == true) {
				for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
					if(CreateContextScreen.segmentedListTable.getValueAt(i, 0)==activation) {
						identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
						notChainResponseActivation.append(identifier + ",");
					}
				}
			}		
			
			notChainResponseActivation.append("'" + constraintName + "' as constraint, 'activation' as actOrTar");

			if(corActivationPayload != null) {
				notChainResponseActivation.append(", " + corActivationPayload + " as correlationActivation");
			}

			notChainResponseActivation.append(" FROM " + activation);
																
			if(activationConditionPayload != null) {
				notChainResponseActivation.append(" WHERE " + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue);
			}

			// Query to set Not Chain Response temporary fulfilled
			StringBuilder notChainResponseTemporaryFulfillment = new StringBuilder();
	
			notChainResponseTemporaryFulfillment.append("INSERT INTO middleLayer SELECT ");
			
			if(segmentedBy == true) {
				for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
					if(CreateContextScreen.segmentedListTable.getValueAt(i, 0)==activation) {
						identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
						notChainResponseTemporaryFulfillment.append(identifier + ",");
					}
				}
			}		
			
			notChainResponseTemporaryFulfillment.append("'" + constraintName + "' as constraint, 'target' as actOrTar");

			if(corActivationPayload != null) {
				notChainResponseTemporaryFulfillment.append(", " + corActivationPayload + " as correlationActivation");
			}

			notChainResponseTemporaryFulfillment.append(" FROM " + activation);
																
			if(activationConditionPayload != null) {
				notChainResponseTemporaryFulfillment.append(" WHERE " + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue);
			}
				
			// Query to check, if Not Chain Response is violated
			StringBuilder notChainResponseViolation = new StringBuilder();
				
			if(segmentedBy == true) {
				notChainResponseViolation.append("CONTEXT SegmentedById ");
				}
				
				notChainResponseViolation.append("INSERT INTO middleLayer SELECT ");
				
			if(segmentedBy==true) {
				
				for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
					if(CreateContextScreen.segmentedListTable.getValueAt(i, 0)==target) {
						identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
						notChainResponseViolation.append("b." + identifier + " as id, ");
					}
				}
			}
			
			
			notChainResponseViolation.append("'" + constraintName + "' as constraint, 'violation' as actOrTar");
			
			if(corActivationPayload != null) {
				notChainResponseViolation.append(", a." + corActivationPayload + " as correlationActivation");
			}
			
			notChainResponseViolation.append(" FROM PATTERN [every a= " + activation);
					
			if(activationConditionPayload != null) {
				notChainResponseViolation.append(" (" + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue +")");
			}
			
			notChainResponseViolation.append("-> b=" + target);
			
			if(targetConditionPayload != null && corActivationPayload != null) {
				notChainResponseViolation.append("(" + targetConditionPayload + " " + targetConditionOperator + " " + targetConditionValue);
				notChainResponseViolation.append(", a." + corActivationPayload + " " +  corOperator + " " + corTargetPayload + ") ");
				notChainResponseViolation.append(" AND NOT " + target);
			}
			
			if(targetConditionPayload != null && corActivationPayload == null) {
				notChainResponseViolation.append("(" + targetConditionPayload + " " + targetConditionOperator + " " + targetConditionValue + ") ");
				notChainResponseViolation.append(" AND NOT " + target);
			}

			if(targetConditionPayload == null && corActivationPayload != null) {
				notChainResponseViolation.append("( a." + corActivationPayload + " " +  corOperator + " " + corTargetPayload + ") ");
				notChainResponseViolation.append(" AND NOT " + target);
			}
			
			notChainResponseViolation.append(" AND NOT " + activation);
			
			for(int i=0; i<AddEventScreen.eventList.getRowCount(); i++) {
				if(AddEventScreen.eventList.getValueAt(i, 0) != activation && AddEventScreen.eventList.getValueAt(i, 0) == target) {
					
					notChainResponseViolation.append(" AND NOT " + AddEventScreen.eventList.getValueAt(i, 0));
				}
			}	

			if(timeSpan != null){
				notChainResponseViolation.append(" AND NOT timer:interval(" + timeSpan + " " + timeSpanUnit + ")");
			}
				
			notChainResponseViolation.append("]");
			
			
			// Query to react to the Activation of Not Chain Response
			String notChainReponseActivationReactionQuery;
			
			if(corActivationPayload != null) {
				notChainReponseActivationReactionQuery = "SELECT id, constraint, actOrTar, correlationActivation FROM middleLayer WHERE actOrTar = 'activation' AND constraint = '" + constraintName + "'";
			}
			else {
				notChainReponseActivationReactionQuery = "SELECT id, constraint, actOrTar FROM middleLayer WHERE actOrTar = 'activation' AND constraint = '" + constraintName + "'";
			}
			
			// Query to react to the Fulfillment of Not Chain Response
			StringBuilder notChainReponseTemporaryFulfillmentReactionQuery = new StringBuilder();
			
			if(segmentedBy == true) {
	
				notChainReponseTemporaryFulfillmentReactionQuery.append("CONTEXT SegmentedById ");
			
			}	
				
			if(corActivationPayload != null) {
				notChainReponseTemporaryFulfillmentReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar, b.correlationActivation as correlationActivation FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') -> every b=middleLayer(actOrTar='target', a.constraint=constraint)]");
			}
			else {
				notChainReponseTemporaryFulfillmentReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') -> every b=middleLayer(actOrTar='target', a.constraint=constraint)]");
			}
			
			// Query to react to the Violation of Not Chain Response
			StringBuilder notChainResponseViolationReactionQuery = new StringBuilder();			
				
			if(segmentedBy == true) {
	
				notChainResponseViolationReactionQuery.append("CONTEXT SegmentedById ");
			
			}	
				
			if(corActivationPayload != null) {
				notChainResponseViolationReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar, b.correlationActivation as correlationActivation FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='target') -> every b=middleLayer(actOrTar='violation', a.constraint=constraint)]");
			}
			else {
				notChainResponseViolationReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='target') -> every b=middleLayer(actOrTar='violation', a.constraint=constraint)]");
			}			
			

			// Insert the builded EPL Queries into the eplQueries Table	
			Object [] NotChainResponseQueries = {(String) constraintsAndConditionsList.getValueAt(x, 0), notChainResponseActivation, notChainResponseTemporaryFulfillment, notChainResponseViolation, null, notChainReponseActivationReactionQuery, notChainReponseTemporaryFulfillmentReactionQuery, notChainResponseViolationReactionQuery, null};
			eplQueries.addRow(NotChainResponseQueries);
						
		}
			
			
		else if (constraint.equals("Not Chain Precedence")){
			
			// Query to check, if Not Chain Precedence is activated
			StringBuilder notChainPrecedenceActivation = new StringBuilder();
	
			notChainPrecedenceActivation.append("INSERT INTO middleLayer SELECT ");
			
			if(segmentedBy == true) {
				for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
					if(CreateContextScreen.segmentedListTable.getValueAt(i, 0)==activation) {
						identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
						notChainPrecedenceActivation.append(identifier + ",");
					}
				}
			}
			
			
			notChainPrecedenceActivation.append("'" + constraintName + "' as constraint, 'activation' as actOrTar");

			if(corActivationPayload != null) {
				notChainPrecedenceActivation.append(", " + corActivationPayload + " as correlationActivation");
			}

			notChainPrecedenceActivation.append(" FROM " + activation);
						
			if(activationConditionPayload != null) {
				notChainPrecedenceActivation.append(" WHERE " + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue);
			}
			
			// Query to check, if Not Chain Precedence is violated
			StringBuilder notChainPrecedenceViolation = new StringBuilder();
			
			if(segmentedBy == true) {
				notChainPrecedenceViolation.append("CONTEXT SegmentedById ");
			}
			notChainPrecedenceViolation.append("INSERT INTO middleLayer SELECT ");
			
			if(segmentedBy==true) {
				
				for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
					if(CreateContextScreen.segmentedListTable.getValueAt(i, 0)==target) {
						identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
						notChainPrecedenceViolation.append("b." + identifier + " as id, ");
					}
				}
			}
			
			notChainPrecedenceViolation.append("'" + constraintName + "' as constraint, 'violation' as actOrTar");
	
			if(corActivationPayload != null) {
				notChainPrecedenceViolation.append(", a." + corTargetPayload + " as correlationActivation");
			}

			notChainPrecedenceViolation.append(" FROM PATTERN [every a= " + target);
							
			if(targetConditionPayload != null) {
				notChainPrecedenceViolation.append(" (" + targetConditionPayload + " " + targetConditionOperator + " " + targetConditionValue +")");
			}
			
			notChainPrecedenceViolation.append("-> b=" + activation);
			
			if(activationConditionPayload != null) {
				notChainPrecedenceViolation.append(" (" + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue + ")");
				notChainPrecedenceViolation.append(" AND NOT " + target);
			}
			
			
			notChainPrecedenceViolation.append(" AND NOT " + target);
			
			
			
			for(int i=0; i<AddEventScreen.eventList.getRowCount(); i++) {
				if(AddEventScreen.eventList.getValueAt(i, 0) != target && AddEventScreen.eventList.getValueAt(i, 0) == activation) {
					
					notChainPrecedenceViolation.append(" AND NOT " + AddEventScreen.eventList.getValueAt(i, 0));
				}
			}
			
			if(timeSpan != null){
				notChainPrecedenceViolation.append(" AND NOT timer:interval(" + timeSpan + " " + timeSpanUnit + ")");
			}
				
			notChainPrecedenceViolation.append("]");
				
			if(corActivationPayload != null) {
				notChainPrecedenceViolation.append(" WHERE a." + corTargetPayload + " " +  corOperator + " b." + corActivationPayload);
			}
			
			// Query to set Not Chain Precedence as temporary fulfilled
			StringBuilder notChainPrecedenceFulfillment = new StringBuilder();
			
			notChainPrecedenceFulfillment.append("INSERT INTO middleLayer SELECT ");
			
			if(segmentedBy == true) {
				for(int i=0; i<CreateContextScreen.segmentedListTable.getRowCount(); i++) {
					if(CreateContextScreen.segmentedListTable.getValueAt(i, 0)==activation) {
						identifier = (String) CreateContextScreen.segmentedListTable.getValueAt(i, 2);
						notChainPrecedenceFulfillment.append(identifier + ",");
					}
				}
			}
			
			
			notChainPrecedenceFulfillment.append("'" + constraintName + "' as constraint, 'target' as actOrTar");

			if(corActivationPayload != null) {
				notChainPrecedenceFulfillment.append(", " + corActivationPayload + " as correlationActivation");
			}

			notChainPrecedenceFulfillment.append(" FROM " + activation);
						
			if(activationConditionPayload != null) {
				notChainPrecedenceFulfillment.append(" WHERE " + activationConditionPayload + " " + activationConditionOperator + " " + activationConditionValue);
			}		
			
			// Query to react to the Activation of Not Chain Precedence
			String notChainPrecedenceActivationReactionQuery;
			
			if(corActivationPayload != null) {
				notChainPrecedenceActivationReactionQuery = "SELECT id, constraint, actOrTar, correlationActivation FROM middleLayer WHERE actOrTar = 'activation' AND constraint = '" + constraintName + "'";
			}
			else {
				notChainPrecedenceActivationReactionQuery = "SELECT id, constraint, actOrTar FROM middleLayer WHERE actOrTar = 'activation' AND constraint = '" + constraintName + "'";
			}
			
			// Query to react to the Violation of Not Chain Precedence
			StringBuilder notChainPrecedenceViolationReactionQuery = new StringBuilder();
			
			if(segmentedBy == true) {
	
				notChainPrecedenceViolationReactionQuery.append("CONTEXT SegmentedById ");
			
			}	
				
			if(corActivationPayload != null) {
				notChainPrecedenceViolationReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar, b.correlationActivation as correlationActivation FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='target') -> b=middleLayer(actOrTar='violation', a.constraint=constraint) and not middleLayer(actOrTar='activation', a.constraint=constraint) and not middleLayer(actOrTar='target', a.constraint=constraint)]");
			}
			else {
				notChainPrecedenceViolationReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='target') -> b=middleLayer(actOrTar='violation', a.constraint=constraint) and not middleLayer(actOrTar='activation', a.constraint=constraint) and not middleLayer(actOrTar='target', a.constraint=constraint)]");
			}
			
			// Query to react to the Temporary Fulfillment of Not Chain Precedence
			StringBuilder notChainPrecedenceTemporaryFulfillmentReactionQuery = new StringBuilder();
			
			if(segmentedBy == true) {
	
				notChainPrecedenceTemporaryFulfillmentReactionQuery.append("CONTEXT SegmentedById ");
			
			}	
				
			if(corActivationPayload != null) {
				notChainPrecedenceTemporaryFulfillmentReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar, b.correlationActivation as correlationActivation FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') -> b=middleLayer(actOrTar='target', a.constraint=constraint) and not middleLayer(actOrTar='activation', a.constraint=constraint) and not middleLayer(actOrTar='violation', a.constraint=constraint)]");
			}
			else {
				notChainPrecedenceTemporaryFulfillmentReactionQuery.append("SELECT a.id as id, a.constraint as constraint, a.actOrTar as actOrTar FROM PATTERN [every a=middleLayer(constraint='" + constraintName + "', actOrTar='activation') -> b=middleLayer(actOrTar='target', a.constraint=constraint) and not middleLayer(actOrTar='activation', a.constraint=constraint) and not middleLayer(actOrTar='violation', a.constraint=constraint)]");
			}

			// Insert the builded EPL Queries into the eplQueries Table	
			Object [] NotChainPrecedenceQueries = {(String) constraintsAndConditionsList.getValueAt(x, 0), notChainPrecedenceActivation, notChainPrecedenceFulfillment, notChainPrecedenceViolation, null, notChainPrecedenceActivationReactionQuery, notChainPrecedenceTemporaryFulfillmentReactionQuery, notChainPrecedenceViolationReactionQuery};
			eplQueries.addRow(NotChainPrecedenceQueries);
	
		}

				

	}
	for (int i = 0; i < eplQueries.getRowCount(); i++) { 
		for (int j = 0; j < eplQueries.getColumnCount(); j++) {
			System.out.println(eplQueries.getValueAt(i,j));
		}
	}
	return eplQueries;
	}	
}

