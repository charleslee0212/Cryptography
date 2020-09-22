import java.math.BigInteger;
import java.util.Random;

public class Driver {
	public static void main(String[] args) {
		System.out.println(ssprime(1024, 55));
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
				if(i == bits - 1) {
					rngBIN[i] = 1;
				}else {
					rngBIN[i] = (byte) oneOrZero;
				}
			}
			
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
		
		public static  BigInteger gcd(BigInteger x, BigInteger y) {
			if(y.equals(BigInteger.ZERO)) {
				return x;
			}else {
				return gcd(y, x.mod(y));
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
		
}
