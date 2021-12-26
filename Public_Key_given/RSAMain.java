import java.io.*;
import java.util.*;

public class RSAMain {

	public static void main(String[] args) throws IOException {
/* 		the file name with .txt extension should be entered 
		before run the program based on the file that want to encrypt
*/
		File file = new File("testCase03.txt");
		
		String fileName = file.getName();
		fileName = fileName.substring(0, fileName.indexOf('.'));
		
		String originalMsg = RSA.getOriginalMsg(file);
		System.out.println("The encryption & decryption will be done for "+fileName+" file\n");
		RSA rsa = RSA.getObject(file);
		
		System.out.println("The file "+fileName+" will be encrypted now ...\n");
		
//		System.out.println(originalMsg);
		String integerText = RSA.alphabetToInteger(originalMsg);
//		System.out.println(integerText);
		String encryptedMsg = rsa.encryption(rsa.getN(), rsa.getE(), integerText);
//		System.out.println(encryptedMsg);
		
		System.out.println("The file "+fileName+" has been encrypted successfully...\n");

		System.out.println("The decryption phase will start now ...\n");
		
		Scanner sc = new Scanner(System.in);
		long d, n;

//-------------------------------Ask the user to enter the private key------------------------------
		 System.out.println("Plz eneter the private key values {d} and {n} :\nThe values must be in range between [-9,223,372,036,854,775,808] and [9,223,372,036,854,775,807]");
		 System.out.print("d = ");
		 d = sc.nextLong();
		 System.out.print("n = ");
		 n = sc.nextLong();
		 sc.close();
//-----------------------------this block is to send the private key to decrypt the file-----------------------------
		
		RSA rs = new RSA();
		File encryptedFile = new File(fileName+".rsa");
		rs.decryption(encryptedFile, d, n);	//the file should came from the encryption method.
		
		System.out.println("\nThe file "+fileName+" has been decrypted successfully...");
	}

}