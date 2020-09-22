import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Driver {
	public static ArrayList<Integer> primes = new ArrayList<>();

	public static void main(String[] args) {
		
		try {
			File file = new File("primes1.txt");
			Scanner reader = new Scanner(file);
			
			while(reader.hasNext()) {
				int prime = reader.nextInt();
				primes.add(prime);
			}

		} catch (IOException e) {
			System.out.println("ERROR: Unable find file");
		}
		
		final long startTime = System.currentTimeMillis();
		for(int i = 0; i < 50; i++) {
			System.out.println("Sophie Prime: " + Sophie(1024,55));
		}
		final long endTime = System.currentTimeMillis();

		System.out.println("Total execution time: " + (endTime - startTime));

	}
	
	public static BigInteger Sophie(int bits, int k) {
		
		BigInteger SophiePrime = BigInteger.ZERO;
		boolean notFound = true;
		
		final long startTime = System.currentTimeMillis();
		while(notFound) {
			BigInteger prime = ssprime(bits, k);
			BigInteger temp = prime.multiply(BigInteger.TWO).add(BigInteger.ONE);
			if(primeTest(temp, bits, k)) {
				notFound = false;
				SophiePrime = prime;
			}
		}
		final long endTime = System.currentTimeMillis();

		System.out.println("Sophie Prime execution time: " + (endTime - startTime));
		
		return SophiePrime;
	}
	
	public static boolean primeTest(BigInteger num, int bits, int k) {
		int count = k;
		while(count > 0) {
			//generate random a 
			boolean founda = false;
			Random rnd = new Random();
			BigInteger a = BigInteger.ZERO;
			
			while(!founda) {
				a = new BigInteger(bits, rnd);
				if(gcd(a,num).equals(BigInteger.ONE)) {
					founda = true;
				}
			}

			BigInteger l = num.subtract(BigInteger.ONE).divide(BigInteger.TWO);
			
			BigInteger e = a.modPow(l, num);
			
			if(e.equals(num.subtract(BigInteger.ONE))) {
				e = BigInteger.ONE.negate();
			}
			if(!e.equals(BigInteger.ONE) && !e.equals(BigInteger.ONE.negate())) {
				break;
			}else if(BigInteger.valueOf(Jacobi(a,num,1)).equals(e)) {
				count--;
			}
		}
		
		if(count == 0) {
			return true;
		}else {
			return false;
		}
	}
	
	//Solvay Strassen
		public static BigInteger ssprime(int bits, int k) {
			boolean foundIt = false;
			BigInteger prime = BigInteger.ONE;

			while(!foundIt) {
				//generating a random number of said bits
				byte[] rngBIN = new byte[bits];
				for(int i = 0; i < bits; i++) {
					int oneOrZero = 0;
					Random rnd = new Random();
					oneOrZero = rnd.nextInt(2);
					if(i == bits - 1 || i == 0) {
						rngBIN[i] = 1;
					}else {
						rngBIN[i] = (byte) oneOrZero;
					}
				}
				//random generated BigInteger of said bits
				BigInteger rng = binToBig(rngBIN);
				int i;
				for(i = 0; i < primes.size(); i++) {
					//BigInteger p = new BigInteger(primes.get(i));
					if(!gcd(rng,BigInteger.valueOf((primes.get(i)))).equals(BigInteger.ONE)) {
						break;
					}
				}
				
				if(i < primes.size()) {
					continue;
				}
				
				if(primeTest(rng, bits, k)) {
					prime = rng;
					foundIt = true;
				}
			}

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
		
		public static  BigInteger gcd(BigInteger x, BigInteger y) {
			if(y.equals(BigInteger.ZERO)) {
				return x;
			}else {
				return gcd(y, x.mod(y));
			}
		}

}
