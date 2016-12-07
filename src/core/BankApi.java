package core;

public class BankApi {

	public static boolean pay(int payment) {
		System.out.println("you pay "+payment+"$");
		return true;
	}
	public static boolean refund(int refund) {
		System.out.println("we refund you "+refund+"$");
		return true;
	}
}
