package view.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import controller.BankApi;
import controller.DatabaseHandler;
import model.BookingEventModel;
import model.CancellationEventModel;
import model.HotelEventModel;

public class LastWindow extends JFrame {
	
	private final int windowWidth = 400;
	private final int windowHeight = 300;
	private final int margin = 25;
	
	JFrame window;
	static final long serialVersionUID = 0;
	JLabel lastInfo;
	JButton okButton;
	
	public LastWindow(HotelEventModel model) {
		window = this;
		setTitle("Thanks");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().setBackground(Color.LIGHT_GRAY);
		

		if ( model instanceof BookingEventModel ) {
			BankApi.pay(model.payment);
			new DatabaseHandler().makeBooking((BookingEventModel) model);	
		
			lastInfo = new JLabel("<html>You pay "+model.payment+"$<br><br>Thank You!</html>");
		}
		else {
			BankApi.refund(((CancellationEventModel)model).refund);
			try {
				new DatabaseHandler().cancelBooking((CancellationEventModel) model);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}	
		
			lastInfo = new JLabel("<html>We refund "+((CancellationEventModel)model).refund+"$<br><br>Thank You!</html>");
		}
		
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
