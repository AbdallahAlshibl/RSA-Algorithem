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
		long e = 0;
		long n = 0;
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

// ____________________________________Decrption method_________________________________________

	public static FileWriter decryption(File rsa, long d, long n) throws IOException {
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
		String fileName = getFileName(rsa);
		FileWriter output = new FileWriter(fileName + ".dec");

		// to write d in the first line ...
		output.write("d = " + d + '\n');

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
			for (int k = 0; k < blockSize; k += 2) {
				sendInteger = root.substring(k, k + 2);

// here the last step, which is sending each letter to the decrypted file to create a complete massage
				output.write(integerToAlphabet(Integer.parseInt(sendInteger)));
			}

		}

		output.close();
		return output;

	}

	public static FileWriter decryption2(File rsa, long d, long n) throws IOException {
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
		String fileName = getFileName(rsa);
		FileWriter output = new FileWriter(fileName + ".dec");

		// to write d in the first line ...
		output.write("d = " + d + "   n = " + n + '\n');

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
			for (int k = 0; k < blockSize; k += 2) {
				sendInteger = root.substring(k, k + 2);

// here the last step, which is sending each letter to the decrypted file to create a complete massage
				output.write(integerToAlphabet(Integer.parseInt(sendInteger)));
			}

		}

		output.close();
		System.out.println("The file is decrypted successfully ...");
		System.exit(0);
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

	// ____________________________________ Bonus methods
	// _________________________________________

	public static void checkPrimes(int n, boolean isPrime[]) {
		// Initialize all entries of boolean array
		// as true. A value in isPrime[i] will finally
		// be false if i is Not a prime, else true
		// bool isPrime[n+1];
		isPrime[0] = isPrime[1] = false;
		for (int i = 2; i <= n; i++)
			isPrime[i] = true;

		for (int p = 2; p * p <= n; p++) {
			// If isPrime[p] is not changed, then it is
			// a prime
			if (isPrime[p] == true) {
				// Update all multiples of p
				for (int i = p * 2; i <= n; i += p)
					isPrime[i] = false;
			}
		}
	}

	// Function to print a prime pair
	// with given product
	public static String findPrimePair(int n) {
		int flag = 0;

		// Generating primes using Sieve
		boolean[] isPrime = new boolean[n + 1];
		checkPrimes(n, isPrime);

		// Traversing all numbers to find first
		// pair
		for (int i = 2; i < n; i++) {
			int x = n / i;

			if (isPrime[i] && isPrime[x] && x != i && x * i == n) {
				System.out.println(i + " " + x);
				flag = 1;
				return i + " " + x;
			}
		}

		if (flag == 0)
			return "No such pair found";

		return "No such pair found";

	}

	// this method will give all the e's that between 1 < e <lcm(p-1)(q-1)
	public static ArrayList<Integer> findEs(int p, int q) {
		int pN = p;
		int qN = q;
		int n = p * q;
		ArrayList<Integer> es = new ArrayList<>();
		for (int e = 2; e < n; e++) {

			if (checkGCD(n, e)) {
				es.add(e);
			}
		}
		return es;
	}

	// this method for finding the d's by using
	// all the e's to bring the possible d's
	public static void findDs(File file, int n, String fileName) throws IOException {
		String strOfPrime = RSA.findPrimePair(n);

		int pN = Integer.parseInt(strOfPrime.substring(0, strOfPrime.indexOf(" ")));
		int qN = Integer.parseInt(strOfPrime.substring(strOfPrime.indexOf(" ") + 1, strOfPrime.length()));
		int p = pN - 1;
		int q = qN - 1;
		long N = p * q;
		BigInteger bigN = BigInteger.valueOf(N);

		ArrayList<Integer> es = new ArrayList<>();
		es = findEs(p, q);

		BigInteger possibleD = new BigInteger("0");
		BigInteger temp = new BigInteger("0");
		ArrayList<BigInteger> allD = new ArrayList<>();
		for (int i = 0; i < es.size(); i++) {
			temp = BigInteger.valueOf(es.get(i));
			possibleD = temp.modInverse(bigN);
			allD.add(possibleD);
		}
		allD = checkDs(allD, n, fileName);
		System.out.println("Possible d's = " + allD.size());
		long d = allD.get(0).longValue();
		decryption(file, d, n);
	}

	// this method for finding the d's by using
	// all the e's to bring the possible d's
	public static void findDs2(File file, int n, String fileName) throws IOException {
		String strOfPrime = RSA.findPrimePair(n);

		int pN = Integer.parseInt(strOfPrime.substring(0, strOfPrime.indexOf(" ")));
		int qN = Integer.parseInt(strOfPrime.substring(strOfPrime.indexOf(" ") + 1, strOfPrime.length()));
		int p = pN - 1;
		int q = qN - 1;
		long N = p * q;
		BigInteger bigN = BigInteger.valueOf(N);

		ArrayList<Integer> es = new ArrayList<>();
		es = findEs(p, q);

		BigInteger possibleD = new BigInteger("0");
		BigInteger temp = new BigInteger("0");
		ArrayList<BigInteger> allD = new ArrayList<>();
		for (int i = 0; i < es.size(); i++) {
			temp = BigInteger.valueOf(es.get(i));
			possibleD = temp.modInverse(bigN);
			allD.add(possibleD);
		}
		allD = checkDs(allD, n, fileName);
		System.out.println("Possible d's = " + allD.size());
		if (allD.size() > 0) {
			long d = allD.get(0).longValue();
			decryption2(file, d, n);
		}
	}

	// method to check D's ....
	public static ArrayList<BigInteger> checkDs(ArrayList<BigInteger> allD, long n, String fileName)
			throws IOException {

		ArrayList<BigInteger> checkedDs = new ArrayList<>();
		String decryptedText = "";
		boolean found = false;

		int blockSize = getBlockSize(n);
		int lengthOfN = String.valueOf(n).length();
		File file = new File(fileName + ".rsa");
		InputStream input = new FileInputStream(file);
		StringBuilder receivedString = new StringBuilder();
		try (BufferedReader bff = new BufferedReader(new InputStreamReader(input))) {
			String temp;
			while ((temp = bff.readLine()) != null) {

				receivedString.append(temp);
			}
		} catch (NoSuchElementException e) {
			System.out.println("Error: the file empty");
		}

		String temp = "";
		String root = "";
		BigInteger total;
		// this loop to decrypt each d ...
		for (int index = 0; index < allD.size(); index++) {
			BigInteger d = allD.get(index);

			// this loop help to finish each block' process individually
			for (int i = 0; i < receivedString.length(); i = i + lengthOfN) {

				// This string is to save each block
				temp = receivedString.substring(i, i + lengthOfN);
				BigInteger calc = new BigInteger(temp);

				// to find the digits after the equation
				total = calc.modPow(d, BigInteger.valueOf(n));

				// using fast modular exponentiation

				// to check if the block in the right size or not
				root = String.format("%0" + blockSize + "d", total);

				decryptedText += root;
			}
			// to split the string to blocks based on block size ...
			String[] splitedIntegerText = decryptedText.split("(?<=\\G.{" + 2 + "})");
			for (int x = 0; x < splitedIntegerText.length; x++) {
				int value = Integer.parseInt(splitedIntegerText[x]);
				if (value >= 79) {
					found = true;
					break;
				}
			}
			if (found) {
				System.out.println("Check d number : " + index + " ot of " + allD.size());
			} else {
				System.out.println("Check d number : " + index + " ot of " + allD.size() + " ... d is added");
				checkedDs.add(d);
				return checkedDs;
			}
			found = false;
			decryptedText = temp = root = "";
		}

		return checkedDs;
	}

	public static void findNs(File file, int digitSize, String fileName) throws IOException {
		String addDigit = "", addDigit2="";
		int final_number = 9, intial_number = 1;
		int qN = 0, pN = 0, p = 0, q = 0;

		// to get the biggest integer based on the digits number ....
		for (int i = 1; i < digitSize; i++) {
			addDigit = String.valueOf(final_number) + "9";
			final_number = Integer.parseInt(addDigit);
		}
		
		for (int i = 1; i < digitSize; i++) {
			addDigit2 = String.valueOf(intial_number) + "0";
			intial_number = Integer.parseInt(addDigit2);
		}

		String root="";
		String strOfPrime="";

		// findPrimePair(n)
		int the_Ns = 0;

		for (int i = final_number; i >= intial_number; i--) {
			the_Ns = i;
			root = String.format("%0" + digitSize + "d", the_Ns);

			strOfPrime = RSA.findPrimePair(Integer.parseInt(root));

			if (strOfPrime != "No such pair found") {
				findDs2(file, the_Ns, fileName);
			}
			root = strOfPrime = "";
		}
	}

}
