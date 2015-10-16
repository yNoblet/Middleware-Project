package core;

import java.rmi.registry.Registry;
import java.util.Scanner;

public class StartServer {
	public static void main(String[] args) {
		try {
			Scanner sc = new Scanner(System.in);
			System.out.println("Adresse du serveur principal ? ");
			String adr = sc.nextLine();
			System.out.println("Port du serveur principal ? ");
			String port = sc.nextLine();
			sc.close();

			IServer obj = new Server(adr, port);

			/*
			 * IAccount a = obj.getAccount("author1"); IAccount b =
			 * obj.getAccount("A");
			 *
			 * obj.newTopic("a", "author1"); obj.newTopic("b", "author1");
			 * obj.newTopic("c", "author1"); obj.newTopic("d", "author1");
			 *
			 * b.addSubscription("b"); b.addSubscription("c");
			 */

			Registry reg = java.rmi.registry.LocateRegistry.createRegistry(1097);

			reg.bind("Server", obj);

			System.out.println("Server launched");

		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
		}
	}
}
