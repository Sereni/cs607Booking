package view.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import controller.Calculator;
import model.BookingEventModel;

public class ConfirmationWindow extends JFrame {
	
	private final int windowWidth = 400;
	private final int windowHeight = 300;
	private final int margin = 25;
	
	JFrame window;
	static final long serialVersionUID = 0;
	JLabel bookingInfo;
	JButton confirmButton;
	
	public ConfirmationWindow(BookingEventModel model) {
		window = this;
		setTitle("Confirmation");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().setBackground(Color.LIGHT_GRAY);

		model.payment = Calculator.getInstance().getPayment(model);
		
		bookingInfo = new JLabel("<html>You want to Book:<br>"+model.toString().replaceAll("\n", "<br>")+"</html>");
		bookingInfo.setBounds( margin, margin,
							windowWidth-2*margin, bookingInfo.getPreferredSize().height );
		bookingInfo.setName("bookingInfo");
		getContentPane().add( bookingInfo );
		
		confirmButton = new JButton("Confirm");
		confirmButton.setName("confirmButton");
		confirmButton.setFocusPainted(false);
		confirmButton.setBounds( (windowWidth- confirmButton.getPreferredSize().width)/2, 150,
				confirmButton.getPreferredSize().width, confirmButton.getPreferredSize().height);
		confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	window.setVisible(false);
	            new LastWindow(model).setVisible(true);
            }
        }); 
		getContentPane().add( confirmButton );
		
		
		setSize(windowWidth,windowHeight);
		setVisible(true);
	}

}
