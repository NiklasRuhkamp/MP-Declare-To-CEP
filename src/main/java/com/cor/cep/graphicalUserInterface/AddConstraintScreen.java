package com.cor.cep.graphicalUserInterface;


import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class AddConstraintScreen extends JFrame {

	private JPanel contentPanel;
	private JTextField actCondText;
	private JTextField tarCondText;
	public static int tarPayloadCounter;
	public static int actPayloadCounter;
	public static ArrayList<String> targetCondPayload = new ArrayList<String>(); 
	public static ArrayList<String> activCondPayload = new ArrayList<String>(); 

	public JComboBox tarCondCombBox = new JComboBox();
	public JComboBox corCondActPaylCombBox = new JComboBox();

	public JTextField constraintNameTextField = new JTextField();
	private JTextField timeSpanTextfield = new JTextField();



	
	//Table to store the registered Constraints
	public static String [] constraintsHeader = {"Constraint", "Activation", "Target"};
	public static DefaultTableModel constraintList = new DefaultTableModel(constraintsHeader,0);
	public static JTable constraints=new JTable(constraintList);
	
	//Table to store the registered Constraints with all conditions
	public static String [] constraintsAndConditionsHeader = {"Constraint", "Activation", "Target", "activationConditionPayload", "activationConditionOperator", "activationConditionValue", "targetConditionPayload", "targetConditionOperator", "targetConditionValue", "corActivationPayload", "corOperator", "corTargetPayload", "constraintName", "timespanUnit", "timespan"};
	public static DefaultTableModel constraintsAndConditionsList = new DefaultTableModel(constraintsAndConditionsHeader,0);
	public static JTable constraintsAndConditions =new JTable(constraintsAndConditionsList);

	
	public static String selectedActivation;
	public static String selectedTarget;
	public static String selectedConstraint;
	public static String constraintName;
	public static String constraint;

	


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddConstraintScreen frame = new AddConstraintScreen();
					frame.setVisible(true);
					frame.setResizable(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AddConstraintScreen() {
		
		//set properties
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 659, 301);
		
		//create contentPanel
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPanel);
		contentPanel.setLayout(null);
		
		String[] listOfOperators = {"=", "<", ">", "<=", ">=", "!="};
		int eventsCounter = AddEventScreen.eventList.getRowCount();
		
		String[] activationEvents = new String[eventsCounter +1];
		
		activationEvents[0]= "--";
		
		//Add all possible Events into the activationEventsList
		for(int i=1; i <= eventsCounter; i++) {
			activationEvents[i]= (String) AddEventScreen.eventList.getValueAt(i-1, 0);
		} 
		
		//Combobox to select the TargetCondition
		tarCondCombBox.setBounds(342, 139, 104, 27);
		contentPanel.add(tarCondCombBox);
		tarCondCombBox.setEnabled(false);
		
		//Label to display the Activation Event for the Correlation Condition
		final JLabel corActLabel = new JLabel("");
		corActLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		corActLabel.setHorizontalAlignment(JTextField.RIGHT);
		corActLabel.setBounds(175, 109, 92, 16);
		contentPanel.add(corActLabel);
		
		//Label to display the Target Event for the Correlation Condition
		final JLabel corTarLabel = new JLabel("");
		corTarLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		corTarLabel.setHorizontalAlignment(JTextField.RIGHT);
		corTarLabel.setBounds(446, 109, 92, 16);
		contentPanel.add(corTarLabel);
		
		//Combobox to select the Payload of the Target for the Correlation Condition
		final JComboBox corCondTarPaylCombBox = new JComboBox();
		corCondTarPaylCombBox.setEnabled(false);
		corCondTarPaylCombBox.setBounds(538, 105, 104, 27);
		contentPanel.add(corCondTarPaylCombBox);
		
		//Combobox to select the Target
		final JComboBox targetComboBox = new JComboBox(activationEvents);
		targetComboBox.setBounds(230, 139, 113, 27);
		contentPanel.add(targetComboBox);
		targetComboBox.setEnabled(false);
		targetComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedActivation= (String)targetComboBox.getSelectedItem();
				int rowCounter = AddEventScreen.allListTable.getRowCount();
				
				tarCondText.setEnabled(true);
				tarCondCombBox.setEnabled(true);
				corCondTarPaylCombBox.setEnabled(true);
				
				//clear ComboBoxes
				tarCondCombBox.removeAllItems();
				corCondTarPaylCombBox.removeAllItems();
				targetCondPayload.clear();
				
				targetCondPayload.add("--");

				corTarLabel.setText(selectedActivation);
					
				for(int i =0; i< rowCounter; i++) {	
					
					//get the Payload of the selected Target Event
					if((String)AddEventScreen.allListTable.getValueAt(i, 0)==selectedActivation) {
						String payloadName = (String)AddEventScreen.allListTable.getValueAt(i, 2);
						targetCondPayload.add(payloadName);
						tarPayloadCounter ++;
					} 
				} 
				
				
				//add Payload to Comboboxes
				for(String element : targetCondPayload) {
					tarCondCombBox.addItem(element);
					corCondTarPaylCombBox.addItem(element);
				}
			}	
		});
		
		//Combobox to select the Payload of the Activation
		final JComboBox actCondComboBox = new JComboBox();
		actCondComboBox.setBounds(342, 73, 104, 27);
		contentPanel.add(actCondComboBox);
		actCondComboBox.setEnabled(false);
		
		//Combobox to select the Activation
		final JComboBox activationComboBox = new JComboBox(activationEvents);
		activationComboBox.setBounds(230, 73, 113, 27);
		contentPanel.add(activationComboBox);
		activationComboBox.setEnabled(false);
		activationComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent f) {
				String selectedTarget= (String)activationComboBox.getSelectedItem();
				int rowCounter = AddEventScreen.allListTable.getRowCount();
				
				actCondText.setEnabled(true);
				corCondActPaylCombBox.setEnabled(true);
				actCondComboBox.setEnabled(true);
				
				//clear ComboBoxes
				actCondComboBox.removeAllItems();
				corCondActPaylCombBox.removeAllItems();
				activCondPayload.clear();
							
				activCondPayload.add("--");

				corActLabel.setText(selectedTarget);
					
				for(int i =0; i< rowCounter; i++) {	
				
					//get the Payload of the selected Target Event
					if((String)AddEventScreen.allListTable.getValueAt(i, 0)==selectedTarget) {
						String payloadName = (String)AddEventScreen.allListTable.getValueAt(i, 2);
						activCondPayload.add(payloadName);
						actPayloadCounter ++;
					} 
				} 
				
				
				//add Payload to Comboboxes
					for(String element : activCondPayload) {
						actCondComboBox.addItem(element);
						corCondActPaylCombBox.addItem(element);
					}
			}		
		});

		//Combobox to select the Operator for the ActivationCondition
		final JComboBox actOpBox = new JComboBox(listOfOperators);
		actOpBox.setBounds(446, 73, 60, 27);
		contentPanel.add(actOpBox);
		actOpBox.setEnabled(false);
		
		//Textfield for the Value of the Activation Condition
		actCondText = new JTextField();
		actCondText.setBounds(511, 72, 130, 26);
		contentPanel.add(actCondText);
		actCondText.setColumns(10);
		actCondText.setEnabled(false);
		
		//User Instruction
		JLabel lblConstraint = new JLabel("Constraint:");
		lblConstraint.setHorizontalAlignment(SwingConstants.RIGHT);
		lblConstraint.setFont(new Font("Helvetica", Font.PLAIN, 13));
		lblConstraint.setBounds(19, 49, 210, 16);
		contentPanel.add(lblConstraint);
		
		//User Instruction
		JLabel lblActivationAndActivation = new JLabel("Activation and Activation Condition:");
		lblActivationAndActivation.setHorizontalAlignment(SwingConstants.RIGHT);
		lblActivationAndActivation.setFont(new Font("Helvetica", Font.PLAIN, 13));
		lblActivationAndActivation.setBounds(18, 78, 211, 16);
		contentPanel.add(lblActivationAndActivation);
		
		//User Instruction
		JLabel lblCorrelation = new JLabel("Correlation:");
		lblCorrelation.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCorrelation.setFont(new Font("Helvetica", Font.PLAIN, 13));
		lblCorrelation.setBounds(59, 110, 104, 16);
		contentPanel.add(lblCorrelation);

		//Combobox to select the Operator of the Correlation Condition
		final JComboBox corOptBox = new JComboBox(listOfOperators);
		corOptBox.setBounds(372, 105, 60, 27);
		contentPanel.add(corOptBox);
		corOptBox.setEnabled(false);

		//User Instruction
		JLabel lblTargetAndTarget = new JLabel("Target and Target Condition:");
		lblTargetAndTarget.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTargetAndTarget.setFont(new Font("Helvetica", Font.PLAIN, 13));
		lblTargetAndTarget.setBounds(18, 144, 211, 16);
		contentPanel.add(lblTargetAndTarget);

		//Combobox to select the Operator of the Target Condition
		final JComboBox tarOptCombBox = new JComboBox(listOfOperators);
		tarOptCombBox.setBounds(446, 139, 60, 27);
		contentPanel.add(tarOptCombBox);
		tarOptCombBox.setEnabled(false);

		//Textfield for the Value of the Target Condition
		tarCondText = new JTextField();
		tarCondText.setColumns(10);
		tarCondText.setBounds(511, 138, 130, 26);
		contentPanel.add(tarCondText);
		tarCondText.setEnabled(false);

		//design header
		JPanel header = new JPanel();
		header.setBackground(new Color(189, 183, 107));
		header.setBounds(0, 0, 659, 27);
		contentPanel.add(header);
		
		//User Instraction
		JLabel lblNewLabel = new JLabel("Add a new Constraint");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		header.add(lblNewLabel);
		
		//design footer
		JPanel footer = new JPanel();
		footer.setBackground(new Color(169, 169, 169));
		footer.setBounds(0, 257, 659, 16);
		contentPanel.add(footer);
		
		//Combobox to select the Activation Property of the Correlation Condition
		corCondActPaylCombBox.setEnabled(false);
		corCondActPaylCombBox.setBounds(268, 105, 104, 27);
		contentPanel.add(corCondActPaylCombBox);

		//textField for the timeSpan
		timeSpanTextfield = new JTextField();
		timeSpanTextfield.setEnabled(false);
		timeSpanTextfield.setColumns(10);
		timeSpanTextfield.setBounds(320, 212, 69, 26);
		contentPanel.add(timeSpanTextfield);
		
		//compobox to select the unit of the timespan
		String[] listOfUnits = {"--", "msec", "sec", "min", "hour"};
		final JComboBox timSpanCombBox = new JComboBox(listOfUnits);
		timSpanCombBox.setBounds(230, 213, 90, 27);
		contentPanel.add(timSpanCombBox);
		timSpanCombBox.setEnabled(false);
		
		//Combobox to select the MP-Declare constraint
		String[] constraintList = {"--", "Existence", "Response", "Precedence", "Responded Existence", "Alternate Response", "Alternate Precedence", "Chain Response", "Chain Precedence", "Not Response", "Not Precedence", "Not Responded Existence", "Not Chain Response", "Not Chain Precedence"};
		final JComboBox constraintBox = new JComboBox(constraintList);
		constraintBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(constraintBox.getSelectedItem()=="--") {
					
					//Set all fields and comboboxes to disabled
					activationComboBox.setEnabled(false);
					actCondComboBox.setEnabled(false);
					actOpBox.setEnabled(false);
					actCondText.setEnabled(false);
					corOptBox.setEnabled(false);
					tarCondCombBox.setEnabled(false);
					tarCondText.setEnabled(false);
					corCondActPaylCombBox.setEnabled(false);
					corCondTarPaylCombBox.setEnabled(false);
					targetComboBox.setEnabled(false);
					tarOptCombBox.setEnabled(false);
					constraintNameTextField.setEnabled(false);
					timeSpanTextfield.setEnabled(false);
					timSpanCombBox.setEnabled(false);


				}

				else if(constraintBox.getSelectedItem()=="Existence"){
					//Enable only the fields and comboboxes, required for Existence
					activationComboBox.setEnabled(true);
					actOpBox.setEnabled(true);
					actCondText.setEnabled(true);
					constraintNameTextField.setEnabled(true);
					timeSpanTextfield.setEnabled(true);
					timSpanCombBox.setEnabled(true);
				}
				
				else {
					//Set the fields and comboboxes to enabled for the next step
					activationComboBox.setEnabled(true);
					actOpBox.setEnabled(true);
					actCondText.setEnabled(true);
					corOptBox.setEnabled(true);
					targetComboBox.setEnabled(true);
					tarOptCombBox.setEnabled(true);
					constraintNameTextField.setEnabled(true);
					timeSpanTextfield.setEnabled(true);
					timSpanCombBox.setEnabled(true);

				}
			}
			
		});
		
		//Button to add the constraint
		JButton addButton = new JButton("Add");
		addButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//Insert constraint, activation and target into the constraintList to display all added constraints
				//Check, if constraintName was specified
				if(constraintNameTextField.getText() == null || constraintNameTextField.getText().equals("")){
					constraintName = (String) constraintBox.getSelectedItem();
				}
				//If no specific name is specified, the constraint is named by its constrainttype
				else{
					constraintName = constraintNameTextField.getText();
				}
				String activationName = (String) activationComboBox.getSelectedItem();
				String targetName = (String) targetComboBox.getSelectedItem();
				constraint = (String) constraintBox.getSelectedItem();
				
				Object[] constraintObject = {constraintName, activationName, targetName};
				AddConstraintScreen.constraintList.addRow(constraintObject);
				
				//insert all selected values into a List to hand it over to the ConstraintBuilder
				Object[] constraintAndConditionObject = {constraint, activationName, targetName, actCondComboBox.getSelectedItem(), actOpBox.getSelectedItem(), actCondText.getText(), tarCondCombBox.getSelectedItem(), tarOptCombBox.getSelectedItem(), tarCondText.getText(), corCondActPaylCombBox.getSelectedItem(), corOptBox.getSelectedItem(), corCondTarPaylCombBox.getSelectedItem(), constraintName, timSpanCombBox.getSelectedItem(), timeSpanTextfield.getText(), }; 
				constraintsAndConditionsList.addRow(constraintAndConditionObject);

				//Set all fields and comboboxes to disabled
				constraintBox.setSelectedItem("--");
				activationComboBox.setSelectedItem("--");
				actCondComboBox.setSelectedItem("--");
				corCondActPaylCombBox.setSelectedItem("--");
				corCondTarPaylCombBox.setSelectedItem("--");
				targetComboBox.setSelectedItem("--");
				tarCondCombBox.setSelectedItem("--");
				actCondText.setText(null);
				tarCondText.setText(null);
				constraintNameTextField.setText(null);
				timeSpanTextfield.setText(null);
				timSpanCombBox.setSelectedItem("--");

				
				activationComboBox.setEnabled(false);
				actCondComboBox.setEnabled(false);
				actOpBox.setEnabled(false);
				actCondText.setEnabled(false);
				corOptBox.setEnabled(false);
				tarCondCombBox.setEnabled(false);
				tarCondText.setEnabled(false);
				corCondActPaylCombBox.setEnabled(false);
				corCondTarPaylCombBox.setEnabled(false);
				targetComboBox.setEnabled(false);
				tarOptCombBox.setEnabled(false);
				constraintNameTextField.setEnabled(false);
				timeSpanTextfield.setEnabled(false);
				timSpanCombBox.setEnabled(false);

			}
		});
		addButton.setBounds(511, 209, 131, 36);
		contentPanel.add(addButton);

		//Add the constraintBox to the panel
		constraintBox.setBounds(230, 44, 255, 27);
		contentPanel.add(constraintBox);
		String selectedConstraint = (String) constraintBox.getSelectedItem();

		//Textfield for the Name of the Constraint
		constraintNameTextField.setEnabled(false);
		constraintNameTextField.setColumns(10);
		constraintNameTextField.setBounds(233, 175, 205, 27);
		contentPanel.add(constraintNameTextField);

		//User Instruction
		JLabel lblConstraintName = new JLabel("Constraint Name");
		lblConstraintName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblConstraintName.setFont(new Font("Helvetica", Font.PLAIN, 13));
		lblConstraintName.setBounds(18, 181, 211, 16);
		contentPanel.add(lblConstraintName);
		
		//User instruction
		JLabel lblTimespan = new JLabel("Timespan:");
		lblTimespan.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTimespan.setFont(new Font("Helvetica", Font.PLAIN, 13));
		lblTimespan.setBounds(18, 218, 211, 16);
		contentPanel.add(lblTimespan);



	}
}


