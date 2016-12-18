package test;

import static org.testng.Assert.assertEquals;

import java.util.Date;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.jdatepicker.impl.JDatePickerImpl;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import view.gui.FirstWindow;


public class ScenarioTest extends SimpleSwingTestFramework {

	JFrame frame;


	// Use the same instance for each test method
	@BeforeMethod
	void setupInstance() throws Exception {
		System.out.println("before");
		frame = new FirstWindow();
		
		frame.setVisible(true);
		frame.toFront();
		// wait for all GUI events to be processed before continuing
		robot.waitForIdle();
		while (!frame.isVisible()) Thread.sleep(100);
		// make sure the window is visible and focused before continuing
		frame.requestFocus();
		robot.waitForIdle();
		while (!frame.isFocusOwner()) Thread.sleep(100);      
	}

	@AfterMethod void shutdown() {
		frame.dispose();
	}
	
	//TODO: need to check if it is correctly inserted a row in db 
	@Test
	public void testBooking() throws Exception {
		System.out.println("method");
		assertEquals( getActiveTitle(), "System" );
		click( (JButton)find(frame,"bookingButton",JButton.class) );
		
		assertEquals( getActiveTitle(), "Enter details" );
		frame = getActiveFrame();
		select(((JDatePickerImpl)find(frame,"datePanelCheckIn",JDatePickerImpl.class)), new Date(117, 02, 10));
		select(((JDatePickerImpl)find(frame,"datePanelCheckOut",JDatePickerImpl.class)), new Date(117, 02, 15));
		select( (JComboBox<?>)find(frame,"roomTypeCombo",JComboBox.class), 0 );
		click( (JButton)find(frame,"continueButton",JButton.class) );
		
		assertEquals( getActiveTitle(), "Show rooms" );
		frame = getActiveFrame();
		select( (JCheckBox)find(frame,"roomCheckBox1",JCheckBox.class), true );
		click( (JButton)find(frame,"continueButton",JButton.class) );
		
		assertEquals( getActiveTitle(), "Show services" );
		frame = getActiveFrame();
		type(((JTextField)find(frame,"numServiceText1",JTextField.class)),"2");
		click( (JButton)find(frame,"continueButton",JButton.class) );
		
		assertEquals( getActiveTitle(), "User" );
		frame = getActiveFrame();
		type(((JTextField)find(frame,"emailTextField",JTextField.class)),"a");
		click( (JButton)find(frame,"continueButton",JButton.class) );
		
		assertEquals( getActiveTitle(), "Confirmation" );
		frame = getActiveFrame();
		assertEquals(((JLabel)find(frame,"bookingInfo",JLabel.class)).getText().contains("520$"),true);
		click( (JButton)find(frame,"confirmButton",JButton.class) );
		
		
//		assertEquals( getActiveTitle(), "Thanks" );
//		frame = getActiveFrame();
//		assertEquals(((JLabel)find(frame,"lastInfo",JLabel.class)).getText().contains("You pay 520$"),true);
	}

	//TODO: need to check if it correctly changes a row in db 
	@Test
	public void testCancellation() throws Exception {
		assertEquals( getActiveTitle(), "System" );
		click( (JButton)find(frame,"cancellationButton",JButton.class) );
		
		assertEquals( getActiveTitle(), "Cancellation ID" );
		frame = getActiveFrame();
		type(((JTextField)find(frame,"idTextField",JTextField.class)),"10");
		click( (JButton)find(frame,"continueButton",JButton.class) );


		frame = getActiveFrame();
		// show message box (because we canceled this booking before)
		// TODO: check msg box
		// TODO: check complete process of cancellation
	}
}
