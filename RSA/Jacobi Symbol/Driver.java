import java.math.BigInteger;
import java.util.ArrayList;

public class Driver {

	public static void main(String[] args) {
		System.out.println(Jacobi(6278,9975));
	}
	
	//Jacobi Symbol
	public static int Jacobi(int a, int m) {
		int j = 1;
		//factors m
		ArrayList<Integer> factors = factor(m);
		//apply Legendre to each factor with a
		for(int i = 0; i < factors.size(); i++) {
			j = j * legendre(a, factors.get(i));
		}
		return j;
	}
	
	//Legendre Symbol
	public static int legendre(int a, int m) {
		int q = 0;
		if(m % 2 == 0) {
			//throws exception if m is not an odd prime
			throw new IllegalArgumentException("'m' must be an odd prime for legendre to apply");
		}else {
			//returns 0 if gcd is not equal to 1
			if(gcd(a,m) != 1) {
				return q;
			}else {
				//apply Legendre
				int l = (m - 1) / 2;
				q = euler(BigInteger.valueOf(a), BigInteger.valueOf(l), BigInteger.valueOf(m)).intValue();
				if(q == m - 1) {
					q = -1;
				}
				return q;
			}
		}
	}
	
	public static ArrayList<Integer> factor(int n){
		ArrayList<Integer> factors = new ArrayList<>();
		for(int i = 2; i <= n; i++) {
			while(n % i == 0) {
				factors.add(i);
				n = n / i;
			}
		}
		return factors;
	}
	
	public static int gcd(int x, int y) {
		if(y == 0) {
			return x;
		}else {
			return gcd(y, x%y);
		}
	}
	
	public static BigInteger euler(BigInteger b, BigInteger e, BigInteger m) {
		if(gcd(b,m).compareTo(BigInteger.ONE) != 0) {
			//zero means that the b and m are not relatively prime
			return BigInteger.ZERO;
		}else {
			BigInteger phiOfm = phi(m);
			BigInteger eMOD = e.mod(phiOfm);
			b = b.mod(m);

			ArrayList<BigInteger> eToBin = binary(eMOD);

			BigInteger result = BigInteger.ONE;
			
			//square and multiply 
			for(int i = 0; i < eToBin.size(); i++) {
				if(eToBin.get(i).compareTo(BigInteger.ONE) == 0) {
					result = result.multiply(b.pow((int) Math.pow(2, i)).mod(m));
				}
			}

			return result.mod(m);
		}
	}

	public static ArrayList<BigInteger> binary(BigInteger n){
		ArrayList<BigInteger> bits = new ArrayList<>();
		while(n.compareTo(BigInteger.ZERO) > 0) {
			bits.add(n.mod(BigInteger.TWO));
			n = n.divide(BigInteger.TWO);
		}
		return bits;
	}

	public static BigInteger phi(BigInteger n) {
		BigInteger count = BigInteger.ZERO;
		for(BigInteger i = BigInteger.ZERO; i.compareTo(n) < 0; i = i.add(BigInteger.ONE)) {
			if(gcd(i, n).equals(BigInteger.ONE)) {
				count = count.add(BigInteger.ONE);
			}
		}
		return count;
	}

	public static  BigInteger gcd(BigInteger x, BigInteger y) {
		if(y.equals(BigInteger.ZERO)) {
			return x;
		}else {
			return gcd(y, x.mod(y));
		}
	}
}
