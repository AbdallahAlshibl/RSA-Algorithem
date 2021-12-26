import java.io.*;
import java.util.*;

public class RSAMain {

	public static void main(String[] args) throws IOException {

		Scanner sc = new Scanner(System.in);
		int choice = 0;
		while (choice != 1 && choice != 2) {
			System.out.println(
					"Please enter you choise :\n  1. for the first part (n is known).\n  2. for the second part (where digits number of n is know).");
			choice = sc.nextInt();
		}

		// For the first part where n is known ...
		if (choice == 1) {

			System.out.print("plz enter n value: ");
			int n01 = sc.nextInt();
			File file = new File("p1.rsa");
			sc.close();

			String fileName = "p1";
			RSA.findDs(file, n01, fileName);
		}

		// For the second part where digits number of n is known ...
		else {
			// do the second part here >>>>>
			System.out.print("plz enter n number of digits : ");
			int digitsNumber = sc.nextInt();
			File file = new File("p2.rsa");
			sc.close();

			String fileName = "p2";
			RSA.findNs(file, digitsNumber, fileName);
		}

	}

}