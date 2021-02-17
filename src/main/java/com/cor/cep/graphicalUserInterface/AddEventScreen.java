package com.cor.cep.graphicalUserInterface;


import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AddEventScreen extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField eventNameField;
	private JTextField payloadNameField;
	public static String [] homeHeader = {"Added Events"};
	public static String [] homeHeader2 = {"Eventname", "Datatype", "Payloadname"};

	//Table to store all Events + Events including the payload
	public static DefaultTableModel eventList = new DefaultTableModel(homeHeader,0);
	public static DefaultTableModel allListTable = new DefaultTableModel(homeHeader2,0);
	public static JTable events=new JTable(eventList);
	public static JTable allList=new JTable(allListTable);



	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddEventScreen frame = new AddEventScreen();
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
	public AddEventScreen() {
		
		//table to store the events ind display them on the homeScreen Window
		String [] tableHeader = {"Datatype", "Payloadname"};
		final DefaultTableModel tableModel = new DefaultTableModel(tableHeader,0);
		final JTable t=new JTable(tableModel);

		//set properties
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 459, 367);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//Combobox to select the datatype of the property
		String[] listOfDatatypes = {"--", "int", "String", "float", "double", "long", "boolean", "Date"};
		final JComboBox dataTypeCombo = new JComboBox(listOfDatatypes);
		dataTypeCombo.setEnabled(false);
		dataTypeCombo.setBounds(70, 73, 113, 27);
		contentPane.add(dataTypeCombo);
		
		//textField for the name of the property
		textField = new JTextField();
		textField.setBounds(511, 72, 130, 26);
		contentPane.add(textField);
		textField.setColumns(10);
		
		//user instruction
		JLabel lblConstraint = new JLabel("Eventname:");
		lblConstraint.setHorizontalAlignment(SwingConstants.RIGHT);
		lblConstraint.setFont(new Font("Helvetica", Font.PLAIN, 13));
		lblConstraint.setBounds(6, 45, 82, 16);
		contentPane.add(lblConstraint);
		
		//user instruction
		JLabel lblActivationAndActivation = new JLabel("Payload:");
		lblActivationAndActivation.setHorizontalAlignment(SwingConstants.RIGHT);
		lblActivationAndActivation.setFont(new Font("Helvetica", Font.PLAIN, 13));
		lblActivationAndActivation.setBounds(6, 78, 63, 16);
		contentPane.add(lblActivationAndActivation);
		
		//design header
		JPanel header = new JPanel();
		header.setBackground(new Color(189, 183, 107));
		header.setBounds(0, 0, 459, 27);
		contentPane.add(header);
		JLabel lblNewLabel = new JLabel("Add a new Event");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		header.add(lblNewLabel);
		
		//design footer
		JPanel footer = new JPanel();
		footer.setBackground(new Color(169, 169, 169));
		footer.setBounds(0, 329, 459, 16);
		contentPane.add(footer);
		
		//scrollPane to display the added payload
		JScrollPane scrollPane = new JScrollPane(t);
		scrollPane.setBounds(32, 123, 388, 141);
		contentPane.add(scrollPane);
		
		//textfield to specify the name of the event
		eventNameField = new JTextField();
		eventNameField.setBounds(91, 39, 269, 26);
		contentPane.add(eventNameField);
		eventNameField.setColumns(10);
		
		//textfield to specify the name of the payload
		payloadNameField = new JTextField();
		payloadNameField.setColumns(10);
		payloadNameField.setBounds(187, 72, 152, 26);
		payloadNameField.setEnabled(false);
		contentPane.add(payloadNameField);
		
		//button to add properties
		final JButton addConstraintsButton = new JButton("+");
		addConstraintsButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				//insert the values into the tableModel
				String dataType = (String) dataTypeCombo.getSelectedItem();
				String payloadName =  payloadNameField.getText();
				
				Object[] row = {dataType, payloadName};
			    tableModel.addRow(row);
			    
			    dataTypeCombo.setSelectedItem("--");
			    payloadNameField.setText("");
			}
		});
		addConstraintsButton.setBounds(387, 72, 48, 29);
		addConstraintsButton.setEnabled(false);
		contentPane.add(addConstraintsButton);
		
		//button to remove properties
		final JButton removeConstraintButton = new JButton("-");
		removeConstraintButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				//remove the values from tableModel
				if(t.getSelectedRow() != -1) {
					
					int toRemove = t.getSelectedRow();
					tableModel.removeRow(toRemove);		
					
				}
			}
		});
		removeConstraintButton.setBounds(342, 72, 48, 29);
		removeConstraintButton.setEnabled(false);
		contentPane.add(removeConstraintButton);
		
		//button to enable all textfields and comboboxes for the user input
		JButton addEventNameButton = new JButton("Add");
		addEventNameButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				addConstraintsButton.setEnabled(true);
				removeConstraintButton.setEnabled(true);
				payloadNameField.setEnabled(true);
				dataTypeCombo.setEnabled(true);
				eventNameField.setEnabled(false);

			}
		});
		addEventNameButton.setBounds(360, 40, 75, 27);
		contentPane.add(addEventNameButton);
		
		//button to clear all textfields and comboboxes and remove the inserted properties form the tableModel 
		JButton clearButton = new JButton("Clear");
		clearButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

			    for(int i=tableModel.getRowCount()-1;i>=0;i--) { 
			        	          
			        	tableModel.removeRow(i); 
			        	
			    }
				addConstraintsButton.setEnabled(false);
				removeConstraintButton.setEnabled(false);
				payloadNameField.setEnabled(false);
				dataTypeCombo.setEnabled(false);
				eventNameField.setEnabled(true);
				eventNameField.setText("");
			}
		});
		clearButton.setBounds(129, 281, 131, 36);
		contentPane.add(clearButton);
		
		//button to insert event and all properties to the allListTable and to prepare the window for the next event
		JButton submitButton = new JButton("Submit");
		submitButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String eventName =  eventNameField.getText();
				
				int columnCount = t.getColumnCount();
				int rowCount = t.getRowCount();
				
				String [][] payloadList = new String [rowCount][columnCount];
				
				for(int i=0; i<rowCount; i++) {
					for(int y=0; y<columnCount; y++) {
						payloadList[i][y] = (String) t.getValueAt(i, y);
					}
					
				Object[] pay = {eventName, t.getValueAt(i, 0),t.getValueAt(i, 1)};
				allListTable.addRow(pay);
				}
					
				//clear all comboboxes and textfields
				dataTypeCombo.setSelectedItem("--");
				payloadNameField.setText("");
				eventNameField.setText("");
				Object[] ev = {eventName};
				eventList.addRow(ev);
				
				//clear the tableModel to prepre for displaying the properties of the next event
			    for(int i=tableModel.getRowCount()-1;i>=0;i--) { 
      	          
		        	tableModel.removeRow(i); 
			    }
			    
				addConstraintsButton.setEnabled(false);
				removeConstraintButton.setEnabled(false);
				payloadNameField.setEnabled(false);
				dataTypeCombo.setEnabled(false);
				eventNameField.setEnabled(true);
		    
			}
		});
		submitButton.setBounds(272, 281, 131, 36);
		contentPane.add(submitButton);
	}
}
