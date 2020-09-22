import java.math.BigInteger;
import java.util.Random;

public class Driver {

	public static void main(String[] args) {
		System.out.println(blumShub(10));
	}
	
	public static int blumShub(int bits) {
		BigInteger n = ssprime(1024,55).multiply(ssprime(1024,55));
		
		BigInteger x = ssprime(1024,55);
		
		BigInteger[] xs = new BigInteger[bits];
		byte[] bin = new byte[bits];
		
		//initial seed
		xs[0] = x.modPow(BigInteger.TWO, n);
		if(xs[0].and(BigInteger.ONE).equals(BigInteger.ONE)) {
			bin[0] = 1;
		}else {
			bin[0] = 0;
		}
		
		for(int i = 1; i < bits; i++) {
			xs[i] = xs[i-1].modPow(BigInteger.TWO, n);
			if(xs[i].and(BigInteger.ONE).equals(BigInteger.ONE)) {
				bin[i] = 1;
			}else {
				bin[i] = 0;
			}
		}
		
		BigInteger random = binToBig(bin);
		
		return random.intValue();
	}
	
	//rng
	public static byte[] rng(int bits, boolean odd) {
		byte[] rngBIN = new byte[bits];
		for(int i = 0; i < bits; i++) {
			int oneOrZero = 0;
			Random rnd = new Random();
			oneOrZero = rnd.nextInt(2);
			if(i == bits - 1 || (odd && i == 0)) {
				rngBIN[i] = 1;
			}else {
				rngBIN[i] = (byte) oneOrZero;
			}
		}
		return rngBIN;
	}

	//Solvay Strassen
		public static BigInteger ssprime(int bits, int k) {
			boolean foundIt = false;
			BigInteger prime = BigInteger.ONE;

			while(!foundIt) {
				//generating a random number of said bits
				byte[] rngBIN = rng(bits,true);
				
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
