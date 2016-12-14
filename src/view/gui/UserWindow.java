package view.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import model.BookingEventModel;
import model.HotelEventModel;

public class UserWindow extends JFrame {

	private final int windowWidth = 400;
	private final int windowHeight = 300;
	private final int margin = 25;

	JFrame window;
	static final long serialVersionUID = 0;
	JLabel emailLabel;
	JTextField emailTextField;
	JButton continueButton;

	public UserWindow(HotelEventModel model) {
		window = this;
		setTitle("User");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().setBackground(Color.LIGHT_GRAY);

		emailLabel = new JLabel("Please enter your Email:");
		emailLabel.setBounds( (windowWidth- emailLabel.getPreferredSize().width)/2, margin,
				emailLabel.getPreferredSize().width, emailLabel.getPreferredSize().height );
		emailLabel.setName("emailLabel");
		getContentPane().add( emailLabel );

		emailTextField = new JTextField();
		emailTextField.setBounds( (windowWidth-200)/2, emailLabel.getY()+emailLabel.getHeight()+margin,
				200, emailTextField.getPreferredSize().height );
		emailTextField.setName("emailTextField");
		getContentPane().add( emailTextField );

		continueButton = new JButton("Continue");
		continueButton.setName("continueButton");
		continueButton.setFocusPainted(false);
		continueButton.setBounds( (windowWidth- continueButton.getPreferredSize().width)/2, 150,
				continueButton.getPreferredSize().width, continueButton.getPreferredSize().height);
		continueButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String email = emailTextField.getText();

				if ( model instanceof BookingEventModel ) {
					if ( email != null && email.length() > 0 ) {
						model.userEmail = email;
						window.setVisible(false);
						new ConfirmationWindow(model).setVisible(true);
					}
					else {
						JOptionPane.showMessageDialog(window, "Please enter your Email correctly!");
					}
				}
				else {
					if ( email.equals(model.userEmail) ) {
						window.setVisible(false);
						new ConfirmationWindow(model).setVisible(true);
					}
					else {
						JOptionPane.showMessageDialog(window, "Authorisation failed!");
					}
				}
			}
		}); 
		getContentPane().add( continueButton );


		setSize(windowWidth,windowHeight);
		setVisible(true);
	}

}
