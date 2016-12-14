package view.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import controller.DatabaseHandler;
import model.CancellationEventModel;

public class CancelIdWindow extends JFrame {

	private final int windowWidth = 400;
	private final int windowHeight = 300;
	private final int margin = 25;

	JFrame window;
	static final long serialVersionUID = 0;
	JLabel idLabel;
	JTextField idTextField;
	JButton continueButton;

	int id;

	public CancelIdWindow() {
		window = this;
		setTitle("Cancellation ID");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().setBackground(Color.LIGHT_GRAY);

		idLabel = new JLabel("Please enter your booking id:");
		idLabel.setBounds( (windowWidth- idLabel.getPreferredSize().width)/2, margin,
				idLabel.getPreferredSize().width, idLabel.getPreferredSize().height );
		idLabel.setName("idLabel");
		getContentPane().add( idLabel );

		idTextField = new JTextField();
		idTextField.setBounds( (windowWidth-200)/2, idLabel.getY()+idLabel.getHeight()+margin,
				200, idTextField.getPreferredSize().height );
		idTextField.setName("idTextField");
		getContentPane().add( idTextField );

		continueButton = new JButton("Continue");
		continueButton.setName("continueButton");
		continueButton.setFocusPainted(false);
		continueButton.setBounds( (windowWidth- continueButton.getPreferredSize().width)/2, 150,
				continueButton.getPreferredSize().width, continueButton.getPreferredSize().height);
		continueButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					id = Integer.parseInt(idTextField.getText().trim());
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(window, "Please ID correctly!");
				}
				CancellationEventModel model = null;
				try {
					model = new DatabaseHandler().getCancelingBooking(id);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				if (model == null) 
					JOptionPane.showMessageDialog(window, "No booking with this id or it was cancelled before");
				else {
					window.setVisible(false);
					new ConfirmationWindow(model).setVisible(true);
				}
			}
		}); 
		getContentPane().add( continueButton );


		setSize(windowWidth,windowHeight);
		setVisible(true);
	}

}
