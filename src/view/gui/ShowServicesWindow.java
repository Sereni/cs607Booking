package view.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import controller.DatabaseHandler;
import model.BookingEventModel;
import model.ExtraService;

public class ShowServicesWindow extends JFrame {
	
	private final int windowWidth = 400;
	private final int windowHeight = 300;
	private final int margin = 25;
	
	JFrame window;
	static final long serialVersionUID = 0;

	JButton continueButton;
	ArrayList<JTextField> numOfService;
	
	public ShowServicesWindow( BookingEventModel model ) {
		
		window = this;
		setTitle("Show services");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().setBackground(Color.LIGHT_GRAY);
		
	
		ArrayList<ExtraService> allServices = new DatabaseHandler().getActiveServices();
		
		numOfService = new ArrayList<>(allServices.size());
		int yRow = margin;
		for ( ExtraService service : allServices ) {
			JLabel serviceText = new JLabel(service+"");
			serviceText.setBounds( margin, yRow, serviceText.getPreferredSize().width, serviceText.getPreferredSize().height );
			serviceText.setName("serviceText"+service.getId());
			getContentPane().add( serviceText );
			
			JTextField numServiceText = new JTextField("0");
			numServiceText.setBounds( 300, yRow, 75, serviceText.getPreferredSize().height );
			numServiceText.setName("numServiceText"+service.getId());
			numServiceText.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						Integer.parseInt(numServiceText.getText().trim());
					} catch(Exception ex) {
						JOptionPane.showMessageDialog(window, "Please enter number of services correctly.");
					}
					
				}
			});
			numOfService.add(numServiceText);
			getContentPane().add( numServiceText );
			
			yRow += numServiceText.getPreferredSize().height+margin;
		}
		
		continueButton = new JButton("Continue");
		continueButton.setName("continueButton");
		continueButton.setFocusPainted(false);
		continueButton.setBounds( (windowWidth- continueButton.getPreferredSize().width)/2, yRow,
				continueButton.getPreferredSize().width, continueButton.getPreferredSize().height);
		continueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            	for ( int i = 0; i < numOfService.size(); i++ ) {
            		int num = Integer.parseInt(numOfService.get(i).getText().trim());
            		if ( num > 0 )
            			model.services.put( allServices.get(i), num);
            	}
            
            	window.setVisible(false);
            	new UserWindow(model).setVisible(true);
            }
        }); 
		getContentPane().add( continueButton );
		
		setSize(windowWidth,windowHeight);
		setVisible(true);
	}

}