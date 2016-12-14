package view.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class FirstWindow extends JFrame {
	
	private final int windowWidth = 400;
	private final int windowHeight = 300;
	private final int margin = 25;
	
	JFrame window;
	static final long serialVersionUID = 0;
	JLabel chooseText;
	JButton cancellationButton, bookingButton, managerButton;
	
	public FirstWindow() {
		window = this;
		setTitle("System");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().setBackground(Color.LIGHT_GRAY);
		
		managerButton = new JButton("Manager Login");
		managerButton.setName("managerButton");
		managerButton.setFocusPainted(false);
		managerButton.setBounds( windowWidth - managerButton.getPreferredSize().width - margin, margin,
				managerButton.getPreferredSize().width, managerButton.getPreferredSize().height);
		managerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
         
            }
        }); 
		getContentPane().add( managerButton );
		
		chooseText = new JLabel("Choose what action do you want to do?");
		chooseText.setBounds( (windowWidth- chooseText.getPreferredSize().width)/2, managerButton.getY()+3*margin,
				chooseText.getPreferredSize().width, chooseText.getPreferredSize().height );
		chooseText.setName("chooseText");
		getContentPane().add( chooseText );
		
		bookingButton = new JButton("Booking");
		bookingButton.setName("bookingButton");
		bookingButton.setFocusPainted(false);
		bookingButton.setBounds( (windowWidth- bookingButton.getPreferredSize().width)/2, chooseText.getY()+margin,
				bookingButton.getPreferredSize().width, bookingButton.getPreferredSize().height);
		bookingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	window.setVisible(false);
                new EnterDetailsWindow().setVisible(true);
            }
        }); 
		getContentPane().add( bookingButton );
		
		
		cancellationButton = new JButton("Cancellation");
		cancellationButton.setName("cancellationButton");
		cancellationButton.setFocusPainted(false);
		cancellationButton.setBounds( (windowWidth- cancellationButton.getPreferredSize().width)/2, bookingButton.getY()+margin,
				cancellationButton.getPreferredSize().width, cancellationButton.getPreferredSize().height);
		cancellationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
         
            }
        }); 
		getContentPane().add( cancellationButton );
		
		setSize(windowWidth,windowHeight);
		setVisible(true);
	}

}
