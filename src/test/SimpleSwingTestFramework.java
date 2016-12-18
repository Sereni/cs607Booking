package test;

import java.awt.Component;
import java.awt.Container;
import java.awt.KeyboardFocusManager;
import java.awt.Robot;
import java.awt.Window;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

/**
 * Simple Swing test framework.<br>
 * Note you can safely read directly from Swing components, but any write operations must take
 * place on the Swing event queue (SwingUtilities.invokeLater or SwingUtilities.InvokeAndWait)
 * @author sbrown
 *
 */
public class SimpleSwingTestFramework {


	/**
	 * Create Swing robot at object creation
	 */
	Robot robot=null;
	{
		try {robot = new Robot();} catch (Exception e) {};
		robot.setAutoWaitForIdle( true );
	}

	// Parameters for the Swing invoke queue
	Component laterComponent;
	String laterString;
	boolean laterBoolean;
	int laterInteger;

	/**
	 * Find a Swing component by its name & class
	 * @param c - the Swing Container to search (note: full hierarchical search searches sub-containers until component found) 
	 * @param cName - the name of the component (set by setName)
	 * @param requiredClass - the required class of the component
	 * @return - a reference to the component, or null if not found
	 */
	Component find(Container c, String cName, Class<?> requiredClass) {
		Component[] l=c.getComponents();
		for (Component d:l) {
			if (d!=null) {
				if ( (d.getName()!=null) && (d.getName().equals(cName)) &&
						(d.getClass()==requiredClass) ) {
					return d;
				}
				if (d instanceof Container) {
					Component dd = find( (Container)d, cName, requiredClass);
					if (dd!=null)
						return dd;
				}
			}
		}
		return null;
	}

	/**
	 * Find a JButton by the text displayed on the button
	 * @param c - the Swing Container to search (note: full hierarchical search searches sub-containers until component found)
	 * @param bText - the text displayed on the button
	 * @return - a reference to the button, or null if not found
	 */
	JButton findButton(Container c, String bText) {
		Component[] l=c.getComponents();
		for (Component d:l) {
			if (d!=null) {
				if (d.getClass()==JButton.class)
					if ( (((JButton)d).getText()!=null) &&
							(((JButton)d).getText().equals(bText)))
						return (JButton)d;
				if (d instanceof Container) {
					JButton dd = findButton( (Container)d, bText);
					if (dd!=null)
						return dd;
				}
			}
		}
		return null;
	}

	/**
	 * Click on a button. Note this hangs if the click brings up a modal dialog, so use clickLater in this case.
	 * @param c - the JButton to click on
	 * @throws Exception - if anything goes wrong
	 */
	void click(JButton c) throws Exception {
		laterComponent = c;
		SwingUtilities.invokeAndWait(
				new Runnable() {
					public void run() {
						((JButton)laterComponent).doClick();
					}
				}
				);
		robot.waitForIdle();
	}   

	/**
	 * Click on a button. Note only use this is the click brings up a modal dialog
	 * @param c - the JButton to click on
	 * @throws Exception - if anything goes wrong
	 */
	// Use if the button click brings up a modal dialog
	void clickLater(JButton c) throws Exception {
		laterComponent = c;
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						((JButton)laterComponent).doClick();
					}
				}
				);
	}   

	/**
	 * Set a checkbox to be selected or unselected
	 * @param c - the checkbox
	 * @param selected - if true, the checkbox is selected. Otherwise, it is deselected.
	 * @throws Exception - if anything goes wrrong.
	 */
	void select(JCheckBox c, boolean selected) throws Exception {
		laterBoolean = selected;
		laterComponent = c;
		SwingUtilities.invokeAndWait(
				new Runnable() {
					public void run() {
						((JCheckBox)laterComponent).setSelected(laterBoolean);
					}
				}
				);
		robot.waitForIdle();
	}

	/**
	 * Select an item (by index) from a pull-down list (JComboBox)
	 * @param c - the pull-down list
	 * @param index - the item index (starts at 0)
	 * @throws Exception - if anything goes wrong
	 */
	void select(JComboBox<?> c, int index) throws Exception {
		laterInteger = index;
		laterComponent = c;
		SwingUtilities.invokeAndWait(
				new Runnable() {
					public void run() {
						((JComboBox<?>)laterComponent).setSelectedIndex(laterInteger);
					}
				}
				);
		robot.waitForIdle();
	}

	/**
	 * Select a date in JDatePicker
	 * @param datePicker - date picker
	 * @param date - selected date dd/MM/yyyy
	 * @throws Exception - if anything goes wrong
	 */
	void select(JDatePickerImpl datePicker, Date date) throws Exception {
		laterComponent = datePicker;
		SwingUtilities.invokeAndWait(
				new Runnable() {
					public void run() {
						UtilDateModel model = (UtilDateModel) ((JDatePickerImpl)laterComponent).getModel();
						model.setValue(date);
						model.setSelected(true);
					}
				}
				);
		robot.waitForIdle();
	}
	
	/**
	 * Enter some text into a textbox. Note this does not simulate "typing" - it just sets the text value directly. Use a Swing Robot, and keyPress/keyRelease to simulate typing specific keys.
	 * @param c - the textbox
	 * @param s - the text string to enter
	 * @throws Exception - if anything goes wrong
	 */
	void type(JTextField c, String s) throws Exception {
		laterComponent = c;
		laterString = new String(s);
		SwingUtilities.invokeAndWait(
				new Runnable() {
					public void run() {
						((JTextField)laterComponent).setText(laterString);
					}
				}
				);
		robot.waitForIdle();
	}

	/**
	 * Get the title of the window with focus
	 * @return - the title
	 */
	String getActiveTitle() {
		String title=null;
		robot.waitForIdle();
		Window w = 
				KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusedWindow();
		if (w instanceof JFrame)
			title = ((JFrame)w).getTitle();
		else if (w instanceof JDialog)
			title = ((JDialog)w).getTitle();
		return title; 
	}
	
	/**
	 * active frame
	 * @return active frame
	 */
	JFrame getActiveFrame() {
		robot.waitForIdle();
		Window w = 
				KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusedWindow();
		if (w instanceof JFrame)
			return ((JFrame)w);
		return null;
	}

	/**
	 * Wait for a particular window to become active (i.e. have focus). Note this waits forever-it would be an improvement to add a timeout!
	 * @param name - the name (title) of the window to wait for
	 * @throws Exception - if anything goes wrong
	 */
	void waitForActiveTitle(String name) throws Exception {
		String title=null;
		while (true) {
			title = getActiveTitle();
			if (title==name)
				return; 
			else
				Thread.sleep(100);
		}
	}

}

