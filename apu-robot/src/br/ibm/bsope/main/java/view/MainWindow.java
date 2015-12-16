package br.ibm.bsope.main.java.view;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import br.ibm.bsope.main.java.controller.APUFeePayment;
import br.ibm.bsope.main.java.model.exceptions.EmptyFileException;
import br.ibm.bsope.main.java.model.exceptions.InvalidSpreadsheetFormatException;
import br.ibm.bsope.main.java.model.services.application.Utils;

public class MainWindow {

	private JFrame mainFrame;
	private JTextField txtFirstNameFirstApprover;
	private JTextField txtLastNameFirstApprover;
	private JTextField txtFirstNameSecondApprover;
	private JTextField txtLastNameSecondApprover;
	private JTextField txtId;
	private JPasswordField passwordField;
	private final ButtonGroup btgrpPayType = new ButtonGroup();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.mainFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		mainFrame = new JFrame();
		mainFrame.setTitle("APU Robot");
		mainFrame.setResizable(false);
		mainFrame.setBounds(100, 100, 500, 380);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setLayout(null);
		
		JPanel pnlApprovers = new JPanel();
		pnlApprovers.setBounds(10, 115, 474, 119);
		mainFrame.getContentPane().add(pnlApprovers);
		
		JLabel lblApprovers = new JLabel("Approvers");
		lblApprovers.setBounds(185, 11, 90, 24);
		lblApprovers.setHorizontalAlignment(SwingConstants.CENTER);
		lblApprovers.setFont(new Font("Cambria", Font.PLAIN, 20));
		
		JLabel lblFirstApprover = new JLabel("First Approver");
		lblFirstApprover.setBounds(29, 30, 103, 20);
		lblFirstApprover.setFont(new Font("Cambria", Font.PLAIN, 16));
		
		JLabel lblSecondApprover = new JLabel("Second Approver");
		lblSecondApprover.setBounds(301, 30, 121, 20);
		lblSecondApprover.setFont(new Font("Cambria", Font.PLAIN, 16));
		
		JLabel lblFirstNameFirstApprover = new JLabel("First Name:");
		lblFirstNameFirstApprover.setBounds(10, 64, 61, 15);
		lblFirstNameFirstApprover.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		JLabel lblLastNameFirstApprover = new JLabel("Last Name:");
		lblLastNameFirstApprover.setBounds(10, 91, 61, 15);
		lblLastNameFirstApprover.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		JLabel lblFirstNameSecondApprover = new JLabel("First Name:");
		lblFirstNameSecondApprover.setBounds(283, 64, 61, 15);
		lblFirstNameSecondApprover.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		JLabel lblLastNameSecondApprover = new JLabel("Last Name:");
		lblLastNameSecondApprover.setBounds(283, 91, 61, 15);
		lblLastNameSecondApprover.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		txtFirstNameFirstApprover = new JTextField();
		txtFirstNameFirstApprover.setBounds(75, 61, 112, 21);
		txtFirstNameFirstApprover.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtFirstNameFirstApprover.setColumns(10);
		
		txtLastNameFirstApprover = new JTextField();
		txtLastNameFirstApprover.setBounds(75, 88, 112, 21);
		txtLastNameFirstApprover.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtLastNameFirstApprover.setColumns(10);
		
		txtFirstNameSecondApprover = new JTextField();
		txtFirstNameSecondApprover.setBounds(352, 61, 112, 21);
		txtFirstNameSecondApprover.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtFirstNameSecondApprover.setColumns(10);
		pnlApprovers.setLayout(null);
		
		txtLastNameSecondApprover = new JTextField();
		txtLastNameSecondApprover.setBounds(352, 88, 112, 21);
		txtLastNameSecondApprover.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtLastNameSecondApprover.setColumns(10);
		pnlApprovers.add(txtLastNameSecondApprover);
		pnlApprovers.add(lblLastNameFirstApprover);
		pnlApprovers.add(txtLastNameFirstApprover);
		pnlApprovers.add(lblLastNameSecondApprover);
		pnlApprovers.add(lblFirstNameFirstApprover);
		pnlApprovers.add(txtFirstNameFirstApprover);
		pnlApprovers.add(lblFirstNameSecondApprover);
		pnlApprovers.add(lblFirstApprover);
		pnlApprovers.add(lblSecondApprover);
		pnlApprovers.add(lblApprovers);
		pnlApprovers.add(txtFirstNameSecondApprover);
		
		JPanel pnlLogin = new JPanel();
		pnlLogin.setBounds(10, 11, 474, 99);
		mainFrame.getContentPane().add(pnlLogin);
		pnlLogin.setLayout(null);
		
		JLabel lblLogin = new JLabel("Login");
		lblLogin.setBounds(184, 11, 90, 24);
		lblLogin.setHorizontalAlignment(SwingConstants.CENTER);
		lblLogin.setFont(new Font("Cambria", Font.PLAIN, 20));
		pnlLogin.add(lblLogin);
		
		JLabel lblId = new JLabel("Enter your Intranet Login:");
		lblId.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblId.setBounds(37, 41, 150, 14);
		pnlLogin.add(lblId);
		
		JLabel lblEnterYourIbm = new JLabel("Enter your Intranet Password:");
		lblEnterYourIbm.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEnterYourIbm.setBounds(21, 70, 166, 14);
		pnlLogin.add(lblEnterYourIbm);
		
		txtId = new JTextField();
		txtId.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtId.setBounds(197, 38, 239, 20);
		pnlLogin.add(txtId);
		txtId.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 12));
		passwordField.setBounds(197, 67, 239, 20);
		pnlLogin.add(passwordField);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 240, 474, 109);
		mainFrame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblPaymentType = new JLabel("Payment Type");
		lblPaymentType.setHorizontalAlignment(SwingConstants.CENTER);
		lblPaymentType.setFont(new Font("Cambria", Font.PLAIN, 20));
		lblPaymentType.setBounds(158, 11, 149, 24);
		panel.add(lblPaymentType);
		
		JRadioButton rdbtnUS = new JRadioButton("US");
		rdbtnUS.setActionCommand("US");
		btgrpPayType.add(rdbtnUS);
		rdbtnUS.setFont(new Font("Tahoma", Font.PLAIN, 12));
		rdbtnUS.setBounds(103, 42, 83, 23);
		panel.add(rdbtnUS);
		
		JRadioButton rdbtnCanada = new JRadioButton("Canada");
		rdbtnCanada.setActionCommand("CA");
		btgrpPayType.add(rdbtnCanada);
		rdbtnCanada.setFont(new Font("Tahoma", Font.PLAIN, 12));
		rdbtnCanada.setBounds(103, 68, 83, 23);
		panel.add(rdbtnCanada);
		
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File excelFile = getChoosenExcelFile();
				if(excelFile == null)
					return;
				
				String login = txtId.getText();
				String password = new String(passwordField.getPassword());
				String[] approversNames = {txtFirstNameFirstApprover.getText(),
						                   txtLastNameFirstApprover.getText(),
						                   txtFirstNameSecondApprover.getText(),
						                   txtLastNameSecondApprover.getText()};
				try {
					if(btgrpPayType.getSelection().getActionCommand().equals("US"))
						APUFeePayment.paymentUS(excelFile, login, password, approversNames);
					else
						APUFeePayment.paymentCA(excelFile, login, password, approversNames);
					
					String message = "Process completed. Check your spreadsheet to see the request ids.";
					String title = "Success!";
					int messageType = JOptionPane.INFORMATION_MESSAGE;
					JOptionPane.showMessageDialog(null, message, title, messageType);
					
				} catch (InvalidFormatException e) {
					e.printStackTrace();
					System.exit(0);
				} catch (IOException e) {
					String message = "There was an error trying to load your spreadsheet. Please try again.";
					String title = "Error!";
					int messageType = JOptionPane.ERROR_MESSAGE;
					JOptionPane.showMessageDialog(null, message, title, messageType);
					e.printStackTrace();
				} catch (EmptyFileException e) {
					String message = "Your spreadsheet is empty. Please choose a valid fulfilled spreadsheet.";
					String title = "Error!";
					int messageType = JOptionPane.ERROR_MESSAGE;
					JOptionPane.showMessageDialog(null, message, title, messageType);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					System.exit(0);
				} catch (InstantiationException e) {
					e.printStackTrace();
					System.exit(0);
				} catch (ClassNotFoundException e) {
					String message = "One province in your spreadsheet is not valid. Please review your spreadsheet.";
					String title = "Error!";
					int messageType = JOptionPane.ERROR_MESSAGE;
					JOptionPane.showMessageDialog(null, message, title, messageType);
					e.printStackTrace();
				} catch (InvalidSpreadsheetFormatException e) {
					String message = "The spreadsheet header does not follow the business guidelines. Please review your spreadsheet.";
					String title = "Error!";
					int messageType = JOptionPane.ERROR_MESSAGE;
					JOptionPane.showMessageDialog(null, message, title, messageType);
				}
			}
		});
		btnStart.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnStart.setBounds(259, 68, 89, 23);
		panel.add(btnStart);
		
		Vector<Component> order = new Vector<Component>(9);
		order.add(txtId);
		order.add(passwordField);
		order.add(txtFirstNameFirstApprover);
		order.add(txtLastNameFirstApprover);
		order.add(txtFirstNameSecondApprover);
		order.add(txtLastNameSecondApprover);
		order.add(rdbtnUS);
		order.add(rdbtnCanada);
		order.add(btnStart);
		FocusTraversalPolicy policy = new CustomFocusTraversalPolicy(order);
		mainFrame.setFocusTraversalPolicy(policy);
	}
	
	
	private File getChoosenExcelFile(){
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new FileFilter() {
			
			public String getDescription() {
				return "Excel Files (*.xls, *.xlsx)";
			}
			
			public boolean accept(File f) {
				boolean result = false;
				if(f.isDirectory())
					return true;
				String extension = Utils.getExtension(f);
				if (extension != null) {
			    	return (extension.equals(Utils.xls) || extension.equals(Utils.xlsx) );
			    }
				return result;
			}
		});
		
		int returnVal = fc.showOpenDialog(mainFrame);
		if(returnVal == JFileChooser.APPROVE_OPTION)
			return fc.getSelectedFile();
		else
			return null;
	}
}
