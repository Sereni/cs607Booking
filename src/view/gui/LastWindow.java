package view.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import controller.BankApi;
import controller.Calculator;
import controller.DatabaseHandler;
import model.BookingEventModel;

public class LastWindow extends JFrame {
	
	private final int windowWidth = 400;
	private final int windowHeight = 300;
	private final int margin = 25;
	
	JFrame window;
	static final long serialVersionUID = 0;
	JLabel lastInfo;
	JButton okButton;
	
	public LastWindow(BookingEventModel model) {
		window = this;
		setTitle("Thanks");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().setBackground(Color.LIGHT_GRAY);
		

		BankApi.pay(model.payment);
		new DatabaseHandler().makeBooking((BookingEventModel) model);	

		model.payment = Calculator.getInstance().getPayment(model);
		
		lastInfo = new JLabel("<html>You pay "+model.payment+"$<br><br>Thank You!</html>");
		lastInfo.setBounds( (windowWidth- lastInfo.getPreferredSize().width)/2, margin,
				lastInfo.getPreferredSize().width, lastInfo.getPreferredSize().height );
		lastInfo.setName("lastInfo");
		getContentPane().add( lastInfo );
		
		okButton = new JButton("OK!");
		okButton.setName("okButton");
		okButton.setFocusPainted(false);
		okButton.setBounds( (windowWidth- okButton.getPreferredSize().width)/2, 200,
				okButton.getPreferredSize().width, okButton.getPreferredSize().height);
		okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	window.setVisible(false);
	            new FirstWindow().setVisible(true);
            }
        }); 
		getContentPane().add( okButton );
		
		
		setSize(windowWidth,windowHeight);
		setVisible(true);
	}

}
