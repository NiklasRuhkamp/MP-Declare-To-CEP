package com.cor.cep.graphicalUserInterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.cor.cep.StartEngine;



import java.awt.Component;
import javax.swing.SwingConstants;

public class MonitoringScreen extends JFrame {

	private JPanel contentPanel;
	private final JPanel header = new JPanel();
	private JLabel imageLabel;
	private JLabel footerSignature;
	public static String [] monitorHeader = {"Constraint", "Correlation Activation", "Status"};
	//Table to store all Events + Events including the payload
	public static DefaultTableModel monitorList = new DefaultTableModel(monitorHeader, 0); 
	JTable constraintMonitoring=new JTable(monitorList);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MonitoringScreen frame = new MonitoringScreen();
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
	public MonitoringScreen() {
		
		//add another column ID for processes with multiple instances
		if(CreateContextScreen.segmented==true) {
			monitorList.addColumn("ID");
			constraintMonitoring.moveColumn(monitorList.getColumnCount()-1, 1);	
		}


		//set properties
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 650);
		
		//create contentPanel
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPanel);
		contentPanel.setLayout(null);
		
		//design header
		header.setBounds(0, 0, 600, 104);
		header.setBackground(new Color(189, 183, 107));
		contentPanel.add(header);
		header.setLayout(null);
		
		//image for the header
		ImageIcon icon = new ImageIcon (getClass().getResource("MP-D_TO_CEP_icon.png"));
		imageLabel = new JLabel(icon);
		imageLabel.setBounds(20, 6, 563, 92);
		header.add(imageLabel);
		
		//signature for footer
		footerSignature = new JLabel("© Niklas Ruhkamp");
		footerSignature.setBounds(485, 603, 109, 25);
		contentPanel.add(footerSignature);
		footerSignature.setFont(new Font("Helvetica", Font.PLAIN, 13));
		
		//design footer
		JPanel footer = new JPanel();
		footer.setBounds(0, 603, 600, 25);
		footer.setToolTipText("");
		footer.setBackground(new Color(169, 169, 169));
		contentPanel.add(footer);
		
		//Label for User Instruction
		JLabel instructionLabel = new JLabel("Monitoring of all activated, fulfilled and violated Constraints");
		instructionLabel.setFont(new Font("Helvetica", Font.PLAIN, 13));
		instructionLabel.setBounds(41, 124, 469, 13);
		contentPanel.add(instructionLabel);
		
		//Button to finish the process and check all activated constraints for violation/fulfillment
		JButton finishButton = new JButton("Finish");
		finishButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				StartEngine.finishProcess();
				StartEngine.stopProcess();
			}
		});
		finishButton.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		finishButton.setBounds(424, 539, 155, 44);
		contentPanel.add(finishButton);
		
		//Button to cancel the process during runtime
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				StartEngine.stopProcess();
			}
		});
		cancelButton.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		cancelButton.setBounds(257, 539, 155, 44);
		contentPanel.add(cancelButton);
		
		
		//ScrollPanel to show status of all Constraints
		JScrollPane constraintScrollPanel = new JScrollPane(constraintMonitoring);
		constraintScrollPanel.setBounds(30, 157, 539, 370);
		contentPanel.add(constraintScrollPanel);
		

		TableColorCellRenderer renderer = new TableColorCellRenderer();
		constraintMonitoring.setDefaultRenderer(Object.class, renderer);



		//Labels for the legend 
		JLabel constraintActivatedLabel = new JLabel("Activation");
		constraintActivatedLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		constraintActivatedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		constraintActivatedLabel.setFont(new Font("Helvetica", Font.PLAIN, 13));
		constraintActivatedLabel.setBounds(16, 539, 80, 13);
		contentPanel.add(constraintActivatedLabel);
		
		JLabel constraintFulfilledLabel = new JLabel("Fulfillment");
		constraintFulfilledLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		constraintFulfilledLabel.setFont(new Font("Helvetica", Font.PLAIN, 13));
		constraintFulfilledLabel.setBounds(16, 557, 80, 13);
		contentPanel.add(constraintFulfilledLabel);
		
		JLabel constraintViolatedLabel = new JLabel("Violaton");
		constraintViolatedLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		constraintViolatedLabel.setFont(new Font("Helvetica", Font.PLAIN, 13));
		constraintViolatedLabel.setBounds(16, 575, 80, 13);
		contentPanel.add(constraintViolatedLabel);
		
		JPanel activatedColorBox = new JPanel();
		activatedColorBox.setToolTipText("");
		activatedColorBox.setBackground(Color.YELLOW);
		activatedColorBox.setBounds(108, 539, 20, 13);
		contentPanel.add(activatedColorBox);
		
		JPanel fulfilledColorBox = new JPanel();
		fulfilledColorBox.setToolTipText("");
		fulfilledColorBox.setBackground(Color.GREEN);
		fulfilledColorBox.setBounds(108, 557, 20, 13);
		contentPanel.add(fulfilledColorBox);
		
		JPanel violatedColorBox = new JPanel();
		violatedColorBox.setToolTipText("");
		violatedColorBox.setBackground(Color.RED);
		violatedColorBox.setBounds(108, 575, 20, 13);
		contentPanel.add(violatedColorBox);
	}
	
}


