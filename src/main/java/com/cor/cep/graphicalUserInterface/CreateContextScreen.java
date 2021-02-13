package com.cor.cep.graphicalUserInterface;


import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CreateContextScreen extends JFrame {

	private JPanel contentPane;
	public CreateContextScreen frame;
	public static boolean segmented = false;
	public static boolean segmentedBoolean = false;
	
	public static String [] segmentedHeader = {"EventName", "Datatype", "PropertyName"};
	public static DefaultTableModel segmentedListTable = new DefaultTableModel(segmentedHeader,0);
	public static JTable segmentedList=new JTable(segmentedListTable);



	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CreateContextScreen frame = new CreateContextScreen();
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
	public CreateContextScreen() {
		
		
		//tableModel to store the context properties
		String [] Contextheader = {"Eventname", "Datatype", "Name of Property"};
		DefaultTableModel contextTable = new DefaultTableModel(Contextheader,0);
		final JTable t=new JTable(contextTable);

		//insert all events into the first column of the contextTable
		for(int i=0; i<AddEventScreen.events.getRowCount(); i++) {
			Object[] row = {AddEventScreen.events.getValueAt(i, 0), null, null};
			contextTable.addRow(row);
		}
		
		//set properties
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 459, 290);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//design header
		JPanel header = new JPanel();
		header.setBackground(new Color(189, 183, 107));
		header.setBounds(0, 0, 459, 27);
		contentPane.add(header);
		
		//user instruaction
		JLabel lblNewLabel = new JLabel("Please add the properties, the events should be segmented by");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		header.add(lblNewLabel);
		
		//design footer
		JPanel footer = new JPanel();
		footer.setBackground(new Color(169, 169, 169));
		footer.setBounds(0, 252, 459, 16);
		contentPane.add(footer);
		
		//scollPane to display the identifier for the context of each event
		JScrollPane scrollPane = new JScrollPane(t);
		scrollPane.setBounds(33, 50, 388, 141);
		contentPane.add(scrollPane);
		
		//create a textfield for error message
		final JLabel ErrorTextfield = new JLabel("");
		ErrorTextfield.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		ErrorTextfield.setBounds(19, 203, 277, 36);
		ErrorTextfield.setForeground(Color.RED);
		contentPane.add(ErrorTextfield);
		
		//add the selected identifier to the segmentedListTable
		JButton submitButton = new JButton("Submit");
		submitButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {	
				
		    	t.getDefaultEditor(String.class).stopCellEditing();

		    	//clear segmentedListTable
				if (segmentedListTable.getRowCount() > 0) {
				       for (int i = segmentedListTable.getRowCount() - 1; i > -1; i--) {
				    	   segmentedListTable.removeRow(i);
				       }
				}
				
		    	//store the new identifier in the segmentedListTable
				for(int i=0; i<t.getRowCount(); i++) {
					String event=(String) t.getValueAt(i,0);
					String datatype=(String) t.getValueAt(i,1);
					String propertyName=(String) t.getValueAt(i,2);
					Object[] row = {event, datatype, propertyName};
					segmentedListTable.addRow(row);
				}
			
			//check if all datatypes are the same
		    String checkDataType = (String) t.getValueAt(0, 1);		    
		    
		    	for(int i=0; i<t.getRowCount(); i++) {
		    		if((t.getValueAt(i, 1).equals(checkDataType))==false) {
		    			ErrorTextfield.setText("ERROR: Data types must be identical!");
		    			ErrorTextfield.setForeground(Color.RED);
		    			return;
		    		}
	    			ErrorTextfield.setText("Everything ok, close this window");
	    			ErrorTextfield.setForeground(Color.BLACK);
	    			segmented = true;
		    	}
			}
			
		});
		submitButton.setBounds(308, 203, 131, 36);
		contentPane.add(submitButton);
		
	}

	

}
