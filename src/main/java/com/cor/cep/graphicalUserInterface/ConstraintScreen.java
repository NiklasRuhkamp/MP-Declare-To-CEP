package com.cor.cep.graphicalUserInterface;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.cor.cep.StartEngine;

public class ConstraintScreen extends JFrame {

	public static DefaultTableModel buildedConstraints;
	private JPanel contentPane;
	private final JPanel panel = new JPanel();
	private JLabel headerLogo;
	private JLabel footerSignature;
	private JLabel instructionLabel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConstraintScreen frame = new ConstraintScreen();
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

	public ConstraintScreen() {

		// set properties
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 650);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		panel.setBounds(0, 0, 600, 104);
		panel.setBackground(new Color(189, 183, 107));
		contentPane.add(panel);
		panel.setLayout(null);

		// image for the header
		ImageIcon icon = new ImageIcon (getClass().getResource("MP-D_TO_CEP_icon.png"));
		headerLogo = new JLabel(icon);
		headerLogo.setBounds(20, 6, 563, 92);
		panel.add(headerLogo);

		// signature for footer
		footerSignature = new JLabel("© Niklas Ruhkamp");
		footerSignature.setBounds(485, 603, 109, 25);
		contentPane.add(footerSignature);
		footerSignature.setFont(new Font("Helvetica", Font.PLAIN, 13));

		// design footer
		JPanel footer = new JPanel();
		footer.setBounds(0, 603, 600, 25);
		footer.setToolTipText("");
		footer.setBackground(new Color(169, 169, 169));
		contentPane.add(footer);

		// user instruction
		instructionLabel = new JLabel("Press '+' too add new constraints and '-' to remove");
		instructionLabel.setFont(new Font("Helvetica", Font.PLAIN, 13));
		instructionLabel.setBounds(20, 124, 309, 13);
		contentPane.add(instructionLabel);

		// button to start the process
		JButton StartProcessButton = new JButton("Start Process");
		StartProcessButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// call constraintBuilder to build EPL statements for each constraint
				ConstraintBuilder constraintBulder = new ConstraintBuilder();
				DefaultTableModel buildedConstraints = constraintBulder.buildConstraint(AddConstraintScreen.constraintsAndConditionsList);
				contentPane.setVisible(false);

				// start the engine
				try {
					StartEngine.main(null, buildedConstraints);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		StartProcessButton.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		StartProcessButton.setBounds(424, 539, 155, 44);
		contentPane.add(StartProcessButton);
		
		//button to add constraints
		JButton addConstraintsButton = new JButton("+");
		addConstraintsButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {	
				//open AddConstraintScreen
				AddConstraintScreen addConstraintScreen = new AddConstraintScreen();
					addConstraintScreen.setVisible(true);
			}
		});
		addConstraintsButton.setBounds(364, 116, 48, 29);
		contentPane.add(addConstraintsButton);
		
		//remove contraints
		JButton removeConstraintButton = new JButton("-");
		removeConstraintButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//remove selected constraint from constraintList
				int toRemove = AddConstraintScreen.constraints.getSelectedRow();
				AddConstraintScreen.constraintList.removeRow(toRemove);	
				AddConstraintScreen.constraintsAndConditionsList.removeRow(toRemove);
			}
		});
		removeConstraintButton.setBounds(317, 116, 48, 29);
		contentPane.add(removeConstraintButton);
		
		//scrollpane to display the added constraints
		JScrollPane scrollPane = new JScrollPane(AddConstraintScreen.constraints);
		scrollPane.setBounds(30, 157, 539, 370);
		contentPane.add(scrollPane);
		

	}
}

