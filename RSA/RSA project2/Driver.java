import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Driver {
	public static ArrayList<ArrayList<BigInteger>> data = new ArrayList<>();
	public static ArrayList<BigInteger> attempts = new ArrayList<>();
	
	public static void main(String[] args) {

		//		BigInteger p = new BigInteger("50659892040561494463105420519378425366746854614073537070794729669135344476025659912463647599035041835067233306725687396411477591004740864080923660671117941531278241142588707248680512804139680087810747099969322188405129365813021601538148084029848111481697539074405473803025691499147513894326691897886589887159");
		//		BigInteger q = new BigInteger("146484460256877706579575981231172456016728317978922281171017016548801961146008060570608318229275353529656690321073520317838643159968089943638705621571178594923480915708587938553815404111393990579490583571901361034255043966515390903129813374428858278270575337783726674996326619399852009927981487556128162969331");

		BigInteger p = BigInteger.ZERO;
		BigInteger q = BigInteger.ZERO;
		BigInteger n = BigInteger.ZERO;
		BigInteger phi = BigInteger.ZERO;
		BigInteger e = BigInteger.ZERO;
		BigInteger d = BigInteger.ZERO;
		
		int bytes = 214;
		ArrayList<String> blocks = new ArrayList<>();

		//User Input
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Select one: \n1) Encrypt \n2) Decrypt");
		int selection = keyboard.nextInt();

		if(selection == 1) {

			Scanner keyboard1 = new Scanner(System.in);
			System.out.println("Would you like to generate a key?(yes/no): ");
			String answer = keyboard1.nextLine();
			answer = answer.toLowerCase();

			if(answer.equals("yes")) {
				
				Scanner input = new Scanner(System.in);
				System.out.println("How many times do you want to run the test? ('k'): ");
				int k = input.nextInt();
				
				//generating a prime number 
				//p = fermatPrime(1024, 10000);
				//q = fermatPrime(1024, 10000);
				
				p = ssprime(1024, k);
				q = ssprime(1024, k);
				
				double probability = 1 - (Math.log(1024) - 2) / (Math.log(1024) - 2 + Math.pow(2, k + 1));
				
				System.out.println(p);
				System.out.println(attempts.get(0) + " random numbers were tried before finding this prime");
				System.out.println("The test ran "  + k + " times to generate this prime number.");
				System.out.println("Probability that this is a prime: " + probability);
				
				System.out.println(q);
				System.out.println(attempts.get(1) + " random numbers were tried before finding this prime");
				System.out.println("The test ran "  + k + " times to generate this prime number.");
				System.out.println("Probability that this is a prime: " + probability);

				//n
				n = p.multiply(q);

				//phi of n
				phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

				//e
				//fix to generate random 'e'
				e = new BigInteger("12345678910111213141516171819202122232425");
				BigInteger gcdOfOne = BigInteger.ZERO;
				while(!gcdOfOne.equals(BigInteger.ONE)) {
					e = e.add(BigInteger.ONE);
					gcdOfOne = gcd(e,phi);
				}

				//d
				d = mInverse(e, phi);

				try {
					FileWriter writer = new FileWriter("PrivateKeys.txt");
					writer.write(d + System.lineSeparator());
					writer.write(p + System.lineSeparator());
					writer.write(q + System.lineSeparator());
					writer.write(phi + System.lineSeparator());
					writer.close();
				} catch (IOException e1) {
					System.out.println("ERROR: Unable to create file");
				}

				try {
					FileWriter writer = new FileWriter("PublicKeys.txt");
					writer.write(e + System.lineSeparator());
					writer.write(n + System.lineSeparator());
					writer.close();
				} catch (IOException e1) {
					System.out.println("ERROR: Unable to create file");
				}

				System.out.println("Here are your keys; " + "\nPublic: " + "\nn: " + n + "\ne: " + e + "\nPrivate: " + 
						"\np: " + p + "\nq: " + q + "\nphi: " + phi + "\nd: " + d);

			}else if(answer.equals("no")) {
				Scanner keyboard2 = new Scanner(System.in);
				System.out.println("Enter your public key file name: ");
				String filename = keyboard2.nextLine();

				try {
					File file = new File(filename);
					Scanner reader = new Scanner(file);
					
					e = new BigInteger(reader.nextLine());
					n = new BigInteger(reader.nextLine());

				} catch (IOException e1) {
					System.out.println("ERROR: Unable find file");
				}
			} else {
				throw new IllegalStateException("Enter only yes or no");
			}

			//Encryption
			Scanner keyboard3 = new Scanner(System.in);
			System.out.println("Enter your message: ");
			String message = keyboard3.nextLine();
			String blocksOfStrings = "";

			for(int i = 0; i < message.length(); i++) {
				if(i % bytes == bytes - 1) {
					blocksOfStrings = blocksOfStrings + message.charAt(i);
					blocks.add(blocksOfStrings);
					blocksOfStrings = "";
				}else {
					blocksOfStrings = blocksOfStrings + message.charAt(i);
				}
			}
			blocks.add(blocksOfStrings);

			ArrayList<String> encryption = new ArrayList<>();
			for(int i = 0; i < blocks.size(); i++) {
				encryption.add(Encrypt(blocks.get(i), e, n));
			}

			try {
				FileWriter writer = new FileWriter("Encryption.txt");
				for(String block : encryption) {
					writer.write(block + System.lineSeparator());
				}
				writer.close();
			} catch (IOException e1) {
				System.out.println("ERROR: Unable to create file");
			}


			System.out.println("Encryption: " + "\n" + encryption);

		}else if(selection == 2) {
			//Decryption
			Scanner keyboard4 = new Scanner(System.in);
			System.out.println("Enter your private filename: ");
			String filename = keyboard4.nextLine();

			try {
				File file = new File(filename);
				Scanner reader = new Scanner(file);

				d = new BigInteger(reader.nextLine());

				reader.close();
			} catch (IOException e1) {
				System.out.println("ERROR: Unable find file");
				System.exit(0);
			}

			Scanner keyboard5 = new Scanner(System.in);
			System.out.println("Enter your public filename: ");
			String filename1 = keyboard5.nextLine();

			try {
				File file = new File(filename1);
				Scanner reader = new Scanner(file);

				e = new BigInteger(reader.nextLine());
				n = new BigInteger(reader.nextLine());

				reader.close();
			} catch (IOException e1) {
				System.out.println("ERROR: Unable find file");
				System.exit(0);
			}

			Scanner keyboard6 = new Scanner(System.in);
			System.out.println("Enter your cipher filename: ");
			String filename2 = keyboard6.nextLine();
			ArrayList<String> encryption = new ArrayList<>();

			try {
				File file = new File(filename2);
				Scanner reader = new Scanner(file);

				while(reader.hasNext()) {
					encryption.add(reader.nextLine());
				}

				reader.close();
			} catch (IOException e1) {
				System.out.println("ERROR: Unable find file");
				System.exit(0);
			}

			String decryption = "";

			for(int i = 0; i < encryption.size(); i++) {
				decryption = decryption + Decrypt(encryption.get(i), d, n);
			}

			System.out.println("Decryption: " + "\n" +decryption);

		}else {
			throw new IllegalStateException("Enter only 1 or 2");
		}

	}

	//encrypt returns a string of hex values in order to maintain original number 
	public static String Encrypt(String message, BigInteger e, BigInteger n) {
		char[] ch = message.toCharArray();
		StringBuilder builder = new StringBuilder();
		for(char c : ch) {
			String hexCode=String.format("%H", c);
			builder.append(hexCode);
		}

		String hex = builder.toString();

		BigInteger dec = hexToDec(hex);

		BigInteger encryption = dec.modPow(e, n);

		String hexOfEncryption = encryption.toString(16);
		hexOfEncryption = hexOfEncryption.toUpperCase();

		//		String encryptM = "";
		//		
		//		for(int i = 0; i < hexOfEncryption.length(); i = i + 2) {
		//			if(i + 2 <= hexOfEncryption.length()) {
		//				String hexVal = "" + hexOfEncryption.charAt(i) + hexOfEncryption.charAt(i+1);
		//				encryptM = encryptM + (char) hexToDec(hexVal).intValueExact();
		//			}
		//		}

		return hexOfEncryption;

	}

	//takes in a hex as the String Cipher in order to maintain original hex
	public static String Decrypt(String cipher, BigInteger d, BigInteger n) {
		//		char[] ch = cipher.toCharArray();
		//		StringBuilder builder = new StringBuilder();
		//		for(char c : ch) {
		//			String hexCode=String.format("%H", c);
		//			builder.append(hexCode);
		//		}
		//		
		//		String hex = builder.toString();

		String hex = cipher;

		BigInteger dec = hexToDec(hex);

		BigInteger decryption = dec.modPow(d, n);

		String hexOfEncryption = decryption.toString(16);
		hexOfEncryption = hexOfEncryption.toUpperCase();

		String dencryptM = "";

		for(int i = 0; i < hexOfEncryption.length(); i = i + 2) {
			if(i + 2 <= hexOfEncryption.length()) {
				String hexVal = "" + hexOfEncryption.charAt(i) + hexOfEncryption.charAt(i+1);
				dencryptM = dencryptM + (char) hexToDec(hexVal).intValueExact();
			}
		}

		return dencryptM;

	}

	public static BigInteger fermatPrime(int bits, int k) {
		int count = 0;
		BigInteger prime = BigInteger.ZERO;
		while(count < k) {
			//generating a random number of said bits
			byte[] rngBIN = new byte[bits];
			for(int i = 0; i < bits; i++) {
				int oneOrZero = 0;
				Random rnd = new Random();
				oneOrZero = rnd.nextInt(2);
				if(i == bits - 1) {
					rngBIN[i] = 1;
				}else {
					rngBIN[i] = (byte) oneOrZero;
				}
			}
			//random generated BigInteger of said bits
			BigInteger rng = binToBig(rngBIN);

			BigInteger f = BigInteger.ONE;
			count = 0;
			while(f.equals(BigInteger.ONE)) {
				//random number in Z star of rng excluding 1 and -1
				BigInteger rngInZ = BigInteger.ONE;
				while(rngInZ.equals(BigInteger.ONE) || rngInZ.equals(rng.subtract(BigInteger.ONE)) || !gcd(rng, rngInZ).equals(BigInteger.ONE)) {
					Random rnd = new Random();
					rngInZ = new BigInteger(bits, rnd);
				}
				f = rngInZ.modPow(rng.subtract(BigInteger.ONE), rng);
				count++;
				if(count == k) {
					prime = rng;
					break;
				}
			}
		}

		return prime;

	}

	//Solvay Strassen
	public static BigInteger ssprime(int bits, int k) {
		boolean foundIt = false;
		BigInteger prime = BigInteger.ONE;
		BigInteger attempt = BigInteger.ZERO;

		while(!foundIt) {
			//generating a random number of said bits
			byte[] rngBIN = new byte[bits];
			for(int i = 0; i < bits; i++) {
				int oneOrZero = 0;
				Random rnd = new Random();
				oneOrZero = rnd.nextInt(2);
				if(i == bits - 1) {
					rngBIN[i] = 1;
				}else {
					rngBIN[i] = (byte) oneOrZero;
				}
			}
				
			attempt = attempt.add(BigInteger.ONE);
			
			//random generated BigInteger of said bits
			BigInteger rng = binToBig(rngBIN);

			int count = k;

			while(count > 0) {
				//generate random a 
				Random rnd = new Random();
				BigInteger a = new BigInteger(bits, rnd);

				BigInteger l = rng.subtract(BigInteger.ONE).divide(BigInteger.TWO);
				BigInteger e = a.modPow(l, rng);
				if(e.equals(rng.subtract(BigInteger.ONE))) {
					e = BigInteger.ONE.negate();
				}
				if(!gcd(a,rng).equals(BigInteger.ONE)) {
					break;
				}else if(!e.equals(BigInteger.ONE) && !e.equals(BigInteger.ONE.negate())) {
					break;
				}else if(BigInteger.valueOf(Jacobi(a,rng,1)).equals(e)) {
					count--;
				}
			}

			if(count == 0) {
				prime = rng;
				foundIt = true;
			}
		}
		
		attempts.add(attempt);

		return prime;
	}

	//Jacobi Symbol
	public static int Jacobi(BigInteger a, BigInteger m, int s) {
		a = a.mod(m);
		if(a.equals(BigInteger.ZERO)) {
			return s;
		}else {
			if(a.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
				a = a.divide(BigInteger.TWO);
				BigInteger mod8 = m.mod(BigInteger.valueOf(8));
				if(mod8.equals(BigInteger.valueOf(3)) || mod8.equals(BigInteger.valueOf(5))) {
					s = -s;
				}
			}

			BigInteger mod4a = a.mod(BigInteger.valueOf(4));
			BigInteger mod4m = m.mod(BigInteger.valueOf(4));

			if(mod4a.equals(BigInteger.valueOf(3)) && mod4m.equals(BigInteger.valueOf(3))) {
				s = -s;
				return Jacobi(m,a,s);
			}else {
				return Jacobi(m,a,s);
			}
		}
	}

	public static BigInteger binToBig(byte[] bin) {
		BigInteger n = BigInteger.ZERO;
		for(int i = 0; i < bin.length; i++) {
			if(bin[i] == 1) {
				n = n.add(BigInteger.TWO.pow(i));
			}
		}
		return n;
	}

	public static BigInteger hexToDec(String hex) {

		String digits = "0123456789ABCDEF";

		hex = hex.toUpperCase();

		BigInteger dec = BigInteger.ZERO;
		for(int i = 0; i < hex.length(); i++) {
			char c = hex.charAt(i);
			int d = digits.indexOf(c);
			dec = dec.multiply(new BigInteger("16")).add(new BigInteger(Integer.toString(d)));
		}

		return dec;
	}

	public static ArrayList<BigInteger> factor(BigInteger n){
		ArrayList<BigInteger> factors = new ArrayList<>();
		for(BigInteger i = BigInteger.TWO; i.compareTo(n) <= 0; i.add(BigInteger.ONE)) {
			while(n.mod(i).equals(BigInteger.ZERO)) {
				factors.add(i);
				n = n.divide(i);
			}
		}
		return factors;
	}

	public static  BigInteger gcd(BigInteger x, BigInteger y) {
		if(y.equals(BigInteger.ZERO)) {
			return x;
		}else {
			return gcd(y, x.mod(y));
		}
	}

	public static BigInteger mInverse(BigInteger n, BigInteger mod) {
		//check if n is greater than mod
		if(n.compareTo(mod) > 0) {
			n = n.mod(mod);
		}
		//check to see if n is a multiplicative inverse if not return -1
		//		if(!gcd(n, mod).equals(BigInteger.ONE)) {
		//			return BigInteger.ONE.negate();
		//		}

		ArrayList<BigInteger> arr = new ArrayList<>();
		BigInteger inverse = BigInteger.ZERO;
		gcdInverse(n,mod);
		for(int i = 0; i < data.size(); i++) {
			switch (i) {
			case 0:
				inverse = data.get(i).get(2);
				arr.add(inverse);
				break;
			case 1:
				inverse = inverse.multiply(data.get(i).get(2));
				if(data.get(i).contains(n)) {
					inverse = inverse.add(BigInteger.ONE);
				}
				arr.add(inverse);
				break;
			default:
				inverse = inverse.multiply(data.get(i).get(2));
				inverse = inverse.add(arr.get(i-2));
				arr.add(inverse);
			}
		}
		return inverse;
	}

	public static BigInteger gcdInverse(BigInteger x, BigInteger y) {
		ArrayList<BigInteger> remainderEQ = new ArrayList<>();
		if(y.equals(BigInteger.ZERO)) {
			return x;
		}else {
			if(y.compareTo(x) < 0 && !x.mod(y).equals(BigInteger.ZERO)) {	
				remainderEQ.add(x.mod(y));
				remainderEQ.add(x);
				BigInteger n = x.subtract(x.mod(y)).divide(y).negate();
				remainderEQ.add(n);
				remainderEQ.add(y);
				data.add(remainderEQ);
			}
			return gcdInverse(y, x.mod(y));
		}
	}
}
