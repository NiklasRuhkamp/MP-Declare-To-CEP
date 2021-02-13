package com.cor.cep.graphicalUserInterface;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;


public class HomeScreen extends JFrame {

	private JPanel contentPanel;
	private final JPanel header = new JPanel();
	private JLabel imageLabel;
	private JLabel footerSignature;
	private JLabel instructionLabel;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HomeScreen frame = new HomeScreen();
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
	public HomeScreen() {
		
		//set properties
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
		System.out.println(getClass().getResource("MP-D_to_CEP_icon.png"));		
		ImageIcon icon = new ImageIcon (getClass().getResource("MP-D_to_CEP_icon.png"));
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
		instructionLabel = new JLabel("Press '+' too add new events and '-' to remove");
		instructionLabel.setFont(new Font("Helvetica", Font.PLAIN, 13));
		instructionLabel.setBounds(41, 124, 281, 13);
		contentPanel.add(instructionLabel);
		
		//ContinueButton to move on to the next Window
		JButton continueButton = new JButton("Continue");
		continueButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ConstraintScreen mpCepGui = new ConstraintScreen();
				mpCepGui.setVisible(true);
				contentPanel.setVisible(false);
			}
		});
		continueButton.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		continueButton.setBounds(424, 539, 155, 44);
		contentPanel.add(continueButton);
		
		//Button to create a context for processes with several instances
		JButton btnAddContextFor = new JButton("Add Context for multiple Instances");
		btnAddContextFor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				CreateContextScreen createContextScreen = new CreateContextScreen();
				createContextScreen.setVisible(true);
			}
		});
		btnAddContextFor.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		btnAddContextFor.setBounds(22, 539, 264, 44);
		contentPanel.add(btnAddContextFor);
		
		//Button to ad Constraints
		JButton addConstraintsButton = new JButton("+");
		addConstraintsButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {	
				AddEventScreen addEventScreen = new AddEventScreen();
				addEventScreen.setVisible(true);
			}
		});
		addConstraintsButton.setBounds(364, 116, 48, 29);
		contentPanel.add(addConstraintsButton);
		
		
		//ScrollPanel to show all added Constraints
		JScrollPane eventScrollPanel = new JScrollPane(AddEventScreen.events);
		eventScrollPanel.setBounds(30, 157, 539, 370);
		contentPanel.add(eventScrollPanel);
		
		//Button to remove Constraints
		JButton removeConstraintButton = new JButton("-");
		removeConstraintButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {	
				int toRemove = AddEventScreen.events.getSelectedRow();
				AddEventScreen.eventList.removeRow(toRemove);		
				}
		});
		removeConstraintButton.setBounds(317, 116, 48, 29);
		contentPanel.add(removeConstraintButton);
		

	}
}
