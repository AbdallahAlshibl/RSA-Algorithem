import java.io.*;
import java.math.BigInteger;
import java.util.*;

public class RSA {
	private long n;
	private long e;
	private long d;
	private static File file;

	// three different constructor to handle all cases ... 
	public RSA() {
		
	}
	public RSA(long n, long e, File file) {
		this.n = n;
		this.e = e;
		this.file = file;
	}
	public RSA(File file, long n, long d) { 
		this.n = n;
		this.d = d;
		this.file = file;
	}
	
	public long getN() {
		return n;
	}

	public long getE() {
		return e;
	}

	public static RSA getObject(File file) {
		
		Scanner scanner;
		long e =0; long n = 0;
		try {
			scanner = new Scanner(file);
			e = scanner.nextLong();
			n = scanner.nextLong();
			scanner.close();
		} catch (FileNotFoundException s) {
			System.out.println("Error, file doesn't exist. Please add the file and try again ...");
			s.printStackTrace();
		}
		RSA rsa = new RSA(n, e, file);
		
		return rsa;
	}
	
	// method to get the original message from the file ...
	public static String getOriginalMsg(File file) {
		Scanner scanner;
		long e =0; long n = 0;
		String originalMsg = "";
		try {
			scanner = new Scanner(file);
			e = scanner.nextLong();
			n = scanner.nextLong();
			scanner.nextLine();
		      while (scanner.hasNextLine()) {
		    	  originalMsg += scanner.nextLine();
		    		  originalMsg += "\n";
		  
			      }
		      scanner.close();
		} catch (FileNotFoundException s) {
			System.out.println("Error, file doesn't exist. Please add the file and try again ...");
			s.printStackTrace();
		}
		return originalMsg;
	}
	
	// get block size method ...
	public static int getBlockSize(long n) {
		int blockSize = 0;
		String constant = "78";
		String str1 = "78";
		String str2 = "7878";
		BigInteger compareNum1 = new BigInteger(str1);
		BigInteger compareNum2 = new BigInteger(str2);
		BigInteger valueN = BigInteger.valueOf(n);
		Boolean reachDigits = false;
		int lengthOfN = valueN.toString().length();

		while (!reachDigits) {
			if (compareNum1.compareTo(valueN) < 0 && compareNum2.compareTo(valueN) > 0) {
				blockSize += 2;
				reachDigits = true;
			} else {
				blockSize += 2;
				str1 += constant;
				str2 += constant;
				compareNum1 = new BigInteger(str1);
				compareNum2 = new BigInteger(str2);
			}
		}

		return blockSize;
	}

	// method to get the file name as a string without the extension ...
	public static String getFileName(File file) {
		String fileName = "";

		try {
			if (file != null && file.exists()) {
				String name = file.getName();
				fileName = name.replaceFirst("[.][^.]+$", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
			fileName = "";
		}

		return fileName;

	}

	// method to encrypt the text ...
	public static String encryption(long n, long e, String integerText) {
		int blockSize = getBlockSize(n);
		int lengthN = getDigitCount(n);
		String encryptedText = "";

		// to split the string to blocks based on block size ...
		String[] splitedIntegerText = integerText.split("(?<=\\G.{" + blockSize + "})");
		// to check the size of last block to add dummy chars ...
		String lastBlock = splitedIntegerText[splitedIntegerText.length - 1];
		while (lastBlock.length() < blockSize) {
			lastBlock += "62"; // padding with dot char
		}
		splitedIntegerText[splitedIntegerText.length - 1] = lastBlock;

		// encrypt each block ...
		for (int i = 0; i < splitedIntegerText.length; i++) {
			encryptedText += getC(n, e, splitedIntegerText[i]);
		}

		String fileName = getFileName(file);

		// to write the encrypted message into new file ...
		try {
			FileWriter myWriter = new FileWriter(fileName+".rsa");
			myWriter.write(encryptedText);
			myWriter.close();
		} catch (IOException e1) {
			System.out.println("Error occurred when create encrypted file");
			e1.printStackTrace();
		}
		return encryptedText;
	}

	// method to calculate c (to encrypt the block) ...
	public static String getC(long n, long e, String block) {
		BigInteger valueN = BigInteger.valueOf(n);
		BigInteger valueE = BigInteger.valueOf(e);
		BigInteger valueM = new BigInteger(block);
		BigInteger valueC;
		int lengthN = getDigitCount(n);
		
		valueC = valueM.modPow(valueE, valueN);
		String newBlock = valueC.toString();
		while (newBlock.length() < lengthN) {
			newBlock = "0" + newBlock;
		}
		return newBlock;
	}

	// To check the GCD == 1 or not ....
	public static boolean checkGCD(long n, long e) {
		BigInteger gcd;
		BigInteger valueN = BigInteger.valueOf(n);
		BigInteger valueE = BigInteger.valueOf(e);
		gcd = valueN.gcd(valueE);
		if (gcd.intValue() == 1)
			return true;
		return false;
	}

	// get number of decimal digits from long ...
	public static int getDigitCount(long number) {
		int length = 0;
		long temp = 1;
		while (temp <= number) {
			length++;
			temp *= 10;
		}
		return length;
	}

	// get number of decimal digits from bigInteger ...
	public static int getDigitCount(BigInteger number) {
		double factor = Math.log(2) / Math.log(10);
		int digitCount = (int) (factor * number.bitLength() + 1);
		if (BigInteger.TEN.pow(digitCount - 1).compareTo(number) > 0) {
			return digitCount - 1;
		}
		return digitCount;
	}

	// Convert the message to integers ...
	public static String alphabetToInteger(String str) {
		String msgToString = "";

		for (int i = 0; i < str.length(); i++) {
			char temp = str.charAt(i);
			int x = (int) temp;

			// capital letters
			if (x >= 65 && x <= 90) {
				if (x >= 65 && x <= 74) {
					msgToString += "0";
				}
				msgToString += String.valueOf(x - 65);
			}
			// small letters
			if (x >= 97 && x <= 122) {
				msgToString += String.valueOf(x - 71);
			}
			// Numbers
			if (x >= 48 && x <= 57) {
				msgToString += String.valueOf(x + 4);
			}
			// period .
			if (x == 46) {
				x = 62;
				msgToString += String.valueOf(x);
			}
			// question mark ?
			if (x == 63) {
				x = 63;
				msgToString += String.valueOf(x);
			}
			// exclamation point !
			if (x == 33) {
				x = 64;
				msgToString += String.valueOf(x);
			}
			// comma,
			if (x == 44) {
				x = 65;
				msgToString += String.valueOf(x);
			}
			// semicolon;
			if (x == 59) {
				x = 66;
				msgToString += String.valueOf(x);
			}
			// colon:
			if (x == 58) {
				x = 67;
				msgToString += String.valueOf(x);
			}
			// hyphen-
			if (x == 45) {
				x = 68;
				msgToString += String.valueOf(x);
			}
			// left parentheses (
			if (x == 40) {
				x = 69;
				msgToString += String.valueOf(x);
			}
			// right parentheses )
			if (x == 41) {
				x = 70;
				msgToString += String.valueOf(x);
			}
			// left brackets[
			if (x == 91) {
				x = 71;
				msgToString += String.valueOf(x);
			}
			// right brackets]
			if (x == 93) {
				x = 72;
				msgToString += String.valueOf(x);
			}
			// left braces{
			if (x == 123) {
				x = 73;
				msgToString += String.valueOf(x);
			}
			// right braces}
			if (x == 125) {
				x = 74;
				msgToString += String.valueOf(x);
			}
			// apostrophe'
			if (x == 39 || x == 96 ) { // x == 39 || x == 69
				x = 75;
				msgToString += String.valueOf(x);
			}
			// quotation marks"
			if (x == 34) {
				x = 76;
				msgToString += String.valueOf(x);
			}
			// the space ' '
			if (x == 32) {
				x = 77;
				msgToString += String.valueOf(x);
			}
			// New Line '\n'
			if (x == '\n') {
				x = 78;
				msgToString += String.valueOf(x);
			}
		}
		return msgToString;
	}

// ____________________________________Decrption method_________________________________________

	public FileWriter decryption(File rsa, long d, long n) throws IOException {
		int blockSize = getBlockSize(n);
		int lengthOfN = String.valueOf(n).length();

// ---------------------This block code is for storing the file content in a string-------------------------
		InputStream input = new FileInputStream(rsa);
		StringBuilder receivedString = new StringBuilder();
		try (BufferedReader bff = new BufferedReader(new InputStreamReader(input))) {
			String temp;
			while ((temp = bff.readLine()) != null) {

				receivedString.append(temp);
			}
		} catch (NoSuchElementException e) {
			System.out.println("Error: the file empty");
			// TODO: handle exception
		}
		

// ------------------------This block code is to write the the result of decryption in a new file----------------------
		String fileName = getFileName(file);
		FileWriter output = new FileWriter(fileName+".dec");

// -------------------------------This block code is to divide the string into equally parts----------------------------
		String sendInteger;

		// this loop help to finish each block' process individually
		for (int i = 0; i < receivedString.length(); i = i + lengthOfN) {

			// This string is to save each block
			String temp = receivedString.substring(i, i + lengthOfN);

			BigInteger calc = new BigInteger(temp);

			BigInteger total;

			// to find the digits after the equation
			total = calc.modPow(BigInteger.valueOf(d), BigInteger.valueOf(n));
			// using fast modular exponentiation

			// to check if the block in the right size or not
			String root = String.format("%0" + blockSize + "d", total);

// ------------------This block is to send the integers to integerToAlphabet Method to receive the alphabet-----------

			// to translate the numbers into characters
			for (int k = 0; k < blockSize ; k += 2) {
				sendInteger = root.substring(k, k + 2);
				
// here the last step, which is sending each letter to the decrypted file to create a complete massage
				output.write(integerToAlphabet(Integer.parseInt(sendInteger)));
			}

		}

		output.close();
		return output;

	}

// ___________________________________This method for converting the integers into alphabet________________________
	public static String integerToAlphabet(int code) {

		if (code >= 0 && code <= 25) {

			return String.valueOf((char) (code + 65));
		}
		if (code >= 26 && code <= 51) {
			return String.valueOf((char) (code + 71));
		}
		if (code >= 52 && code <= 61) {
			return String.valueOf((char) (code - 4));
		}
		// period .
		if (code == 62) {
			code = 46;
			return String.valueOf((char) code);
		}
		// question mark ?
		if (code == 63) {
			code = 63;
			return String.valueOf((char) code);
		}
		// exclamation point !
		if (code == 64) {
			code = 33;
			return String.valueOf((char) code);
		}
		// comma,
		if (code == 65) {
			code = 44;
			return String.valueOf((char) code);
		}
		// semicolon;
		if (code == 66) {
			code = 59;
			return String.valueOf((char) code);
		}
		// colon:
		if (code == 67) {
			code = 58;
			return String.valueOf((char) code);
		} else
		// hyphen-
		if (code == 68) {
			code = 45;
			return String.valueOf((char) code);
		}
		// left parentheses (
		if (code == 69) {
			code = 40;
			return String.valueOf((char) code);
		}
		// right parentheses )
		if (code == 70) {
			code = 41;
			return String.valueOf((char) code);
		}
		// left brackets[
		if (code == 71) {
			code = 91;
			return String.valueOf((char) code);
		}
		// right brackets]
		if (code == 72) {
			code = 93;
			return String.valueOf((char) code);
		}
		// left braces{
		if (code == 73) {
			code = 123;
			return String.valueOf((char) code);
		}
		// right braces}
		if (code == 74) {
			code = 125;
			return String.valueOf((char) code);
		}
		// apostrophe'
		if (code == 75) {
			code = 39;
			return String.valueOf((char) code);
		}
		// quotation marks"
		if (code == 76) {
			code = 34;
			return String.valueOf((char) code);
		}
		// the space
		if (code == 77) {
			code = 32;
			return String.valueOf((char) code);
		}
		// the newline char
		if (code == 78) {

			return String.valueOf('\n');
		}

		return "";

	}

}
