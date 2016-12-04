package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

public abstract class HotelEvent {

	protected int id;
	protected Date checkIn;
	protected Date checkOut;
	protected String userEmail;
	protected ArrayList<Room> rooms;
	protected ArrayList<HotelPricingRule> pricingRules;

	public void start() {
		doEvent();
	}

	protected String askUserEmail() {
		String email = "";

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Enter your email:");
		try {
			email = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return email;
	}

	protected abstract double calculatePayment(); 

	protected abstract void doEvent();

}
