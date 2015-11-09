package core;

import java.util.Scanner;

/**
 * The Class StartServer.
 */
public class StartServer {
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		try {
			Scanner sc = new Scanner(System.in);
			System.out.println("Adresse du serveur principal ? (laissez vide si premier) ");
			String adrP = sc.nextLine();
			System.out.println("Port du serveur principal ? (laissez vide si premier) ");
			String portP = sc.nextLine();
			System.out.println("Port de ce serveur ?");
			String portS = sc.nextLine();
			sc.close();

			new Server(adrP, portP, portS);

			System.out.println("Server launched");

		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
		}
	}
}
