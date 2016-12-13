package controller;

public class BankApi {

	public static boolean pay(int payment) {
		System.out.println("You pay "+payment+"$");
		return true;
	}
	public static boolean refund(int refund) {
		System.out.println("We refund you "+refund+"$");
		return true;
	}
}
