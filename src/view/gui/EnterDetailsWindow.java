package view.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import model.BookingEventModel;
import model.RoomType;


public class EnterDetailsWindow extends JFrame {
	
	private final int windowWidth = 400;
	private final int windowHeight = 300;
	private final int margin = 25;
	
	JFrame window;
	static final long serialVersionUID = 0;
	
	JButton continueButton;

	JLabel checkInLabel, checkOutLabel, roomTypeLabel;
	
	UtilDateModel modelCheckIn;
	JDatePanelImpl datePanelCheckIn;
	JDatePickerImpl datePickerCheckIn;
	
	UtilDateModel modelCheckOut;
	JDatePanelImpl datePanelCheckOut;
	JDatePickerImpl datePickerCheckOut;

	
	JComboBox<String> roomTypeCombo = new JComboBox<String>();
	

	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	public EnterDetailsWindow() {
		BookingEventModel model = new BookingEventModel();
		
		window = this;
		setTitle("Enter details");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().setBackground(Color.LIGHT_GRAY);

		checkInLabel = new JLabel("Check-in date:   ");
		checkInLabel.setBounds(margin, margin+4,
				checkInLabel.getPreferredSize().width, checkInLabel.getPreferredSize().height);
		checkInLabel.setName("checkInLabel");
		getContentPane().add( checkInLabel );

		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		modelCheckIn = new UtilDateModel();
		datePanelCheckIn = new JDatePanelImpl(modelCheckIn, p);
		datePanelCheckIn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					model.checkIn = formatter.parse(datePickerCheckIn.getJFormattedTextField().getText());
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if ( model.checkIn.before(new Date()) ) {
					modelCheckIn.setValue(null);
					JOptionPane.showMessageDialog(window, "Check-in should be after today.");
				}
			}
		});
		datePickerCheckIn = new JDatePickerImpl(datePanelCheckIn, new DateLabelFormatter());
		datePickerCheckIn.setName("datePanelCheckIn");
		datePickerCheckIn.setBounds(checkInLabel.getX()+checkInLabel.getWidth(), margin,
				datePickerCheckIn.getPreferredSize().width, datePickerCheckIn.getPreferredSize().height);
		datePickerCheckIn.setBackground(Color.LIGHT_GRAY);
		getContentPane().add( datePickerCheckIn );
		

		checkOutLabel = new JLabel("Check-out date: ");
		checkOutLabel.setBounds(margin, margin+checkInLabel.getY()+checkInLabel.getHeight()+4,
				checkOutLabel.getPreferredSize().width, checkOutLabel.getPreferredSize().height);
		checkOutLabel.setName("checkOutLabel");
		getContentPane().add( checkOutLabel );
		
		modelCheckOut = new UtilDateModel();
		datePanelCheckOut = new JDatePanelImpl(modelCheckOut, p);
		datePanelCheckOut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					model.checkOut = formatter.parse(datePickerCheckOut.getJFormattedTextField().getText());
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				if ( model.checkOut.before(model.checkIn) ) {
					modelCheckOut.setValue(null);
					JOptionPane.showMessageDialog(window, "Check-out should be after check-in.");
				}
			}
		});
		datePickerCheckOut = new JDatePickerImpl(datePanelCheckOut, new DateLabelFormatter());
		datePickerCheckOut.setName("datePanelCheckOut");
		datePickerCheckOut.setBounds(checkOutLabel.getX()+checkOutLabel.getWidth(), checkInLabel.getY()+checkInLabel.getHeight()+margin,
				datePickerCheckOut.getPreferredSize().width, datePickerCheckOut.getPreferredSize().height);
		datePickerCheckOut.setBackground(Color.LIGHT_GRAY);
		getContentPane().add( datePickerCheckOut );
		
		roomTypeLabel = new JLabel("Room type: ");
		roomTypeLabel.setBounds(margin, margin+checkOutLabel.getY()+checkOutLabel.getHeight()+4,
				roomTypeLabel.getPreferredSize().width, roomTypeLabel.getPreferredSize().height);
		roomTypeLabel.setName("roomTypeLabel");
		getContentPane().add( roomTypeLabel );
		
		for (RoomType type : RoomType.values()) {
			roomTypeCombo.addItem(type.name());
		}
		roomTypeCombo.setBounds(datePickerCheckOut.getX(), margin+checkOutLabel.getY()+checkOutLabel.getHeight(),
				roomTypeCombo.getPreferredSize().width, roomTypeCombo.getPreferredSize().height);
		roomTypeCombo.setName("roomTypeCombo");
		getContentPane().add( roomTypeCombo );
		
		continueButton = new JButton("Continue");
		continueButton.setName("continueButton");
		continueButton.setFocusPainted(false);
		continueButton.setBounds( (windowWidth- continueButton.getPreferredSize().width)/2, roomTypeCombo.getY()+roomTypeCombo.getHeight()+margin,
				continueButton.getPreferredSize().width, continueButton.getPreferredSize().height);
		continueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	try {
					model.checkIn = formatter.parse(datePickerCheckIn.getJFormattedTextField().getText());
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}try {
					model.checkOut = formatter.parse(datePickerCheckOut.getJFormattedTextField().getText());
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            	if ( model.checkOut == null ||
            			model.checkIn == null ) {
					JOptionPane.showMessageDialog(window, "Please enter dates.");
				}
            	else {
	            	window.setVisible(false);
	                new ShowRoomsWindow(model, RoomType.valueOf((String) roomTypeCombo.getSelectedItem())).setVisible(true);
            	}
            }
        }); 
		getContentPane().add( continueButton );
		
		setSize(windowWidth,windowHeight);
		setVisible(true);
	}

}