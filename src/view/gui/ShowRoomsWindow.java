package view.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import controller.Availability;
import model.BookingEventModel;
import model.Room;
import model.RoomType;

public class ShowRoomsWindow extends JFrame {
	
	private final int windowWidth = 400;
	private final int windowHeight = 300;
	private final int margin = 25;
	
	JFrame window;
	static final long serialVersionUID = 0;

	JButton continueButton;
	ArrayList<JCheckBox> roomSelected;
	
	public ShowRoomsWindow( BookingEventModel model, RoomType roomType ) {
		
		window = this;
		setTitle("Show rooms");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().setBackground(Color.LIGHT_GRAY);
		
	
		ArrayList<Room> allRooms = Availability.findAvailableRooms(model.checkIn, model.checkOut, roomType);
		roomSelected = new ArrayList<>(allRooms.size());
		int yRow = margin;
		for ( Room room : allRooms ) {
		    JCheckBox roomCheckBox = new JCheckBox(room+"");
		    roomCheckBox.setMnemonic(KeyEvent.VK_C); 
		    roomCheckBox.setName("roomCheckBox"+room.getId());
		    roomCheckBox.setBounds(margin, yRow,
		    		roomCheckBox.getPreferredSize().width, roomCheckBox.getPreferredSize().height);
		    
		    roomSelected.add(roomCheckBox);
		    getContentPane().add( roomCheckBox );
			
			yRow += roomCheckBox.getPreferredSize().height+margin;
		}
		
		continueButton = new JButton("Continue");
		continueButton.setName("continueButton");
		continueButton.setFocusPainted(false);
		continueButton.setBounds( (windowWidth- continueButton.getPreferredSize().width)/2, yRow,
				continueButton.getPreferredSize().width, continueButton.getPreferredSize().height);
		continueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            	for ( int i = 0; i < roomSelected.size(); i++ )
            		if ( roomSelected.get(i).isSelected() ) {
            			model.rooms.add(allRooms.get(i));
            		}
            	if ( model.rooms.size() < 1 ) {
					JOptionPane.showMessageDialog(window, "Please select at least one room.");
				}
            	else {
	            	window.setVisible(false);
	                new ShowServicesWindow(model).setVisible(true);
            	}
            }
        }); 
		getContentPane().add( continueButton );
		
		setSize(windowWidth,windowHeight);
		setVisible(true);
	}

}