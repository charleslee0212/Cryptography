import java.math.BigInteger;

public class Driver {

	public static void main(String[] args) {
		//System.out.println(Vigenere("WCUDJZIFVLKADACJOOCDWWKDXWVAPLEOFIZARWXKJOOPQWBWTKDOMAVXESQDATGUDUILHOENYHSNELDGOMSADPOYTLETKUSPLAILHETDVUOXULDEYGGQNVIZABWTGBCGJJOEXAAASSCQSAQQWYAMHWXEWHVKNLRSNEOWSPHMBSJSLWAJKAHAIPGHKEBDTQALDDOXLSRCUEDAGSBDPUGYWADKWTASYKQTDHOOQDWCCDGDBDPUGAFSQALDDOHELHKDACTAPUHPDETLAW", "BEESS"));
		BigInteger x = new BigInteger("7411");
		BigInteger y = new BigInteger("9283");
		System.out.println(gcd(x,y));
	}
	
	public static String Vigenere(String message, String key) {
		message = message.toLowerCase();
		key = key.toUpperCase();
		message = message.replaceAll("\\s", "");
		String result = "";
		int[] keyINT = new int[key.length()];
		for(int i = 0; i < key.length(); i++) {
			keyINT[i] = key.charAt(i) - 'A';
		}
		for(int i = 0; i < message.length(); i++) {
			result = result + Encipher(message.charAt(i), keyINT[i%key.length()]);
		}
		
		return result;
	}
	
	public static char Encipher(char letter, int key) {
		key = key % 25;
		if(key < 0) {
			key =+ 25;
		}
		int x = letter - 'a';
		x = x + key +'A';
		if(x > 'Z') {
			x = (x % 'Z') - 1; //minus one to shift not skip 'A'
			x = x + 'A';
		}
		char result = (char) x;
		return result;
	}
	
	public static  BigInteger gcd(BigInteger x, BigInteger y) {
		if(y.equals(BigInteger.ZERO)) {
			return x;
		}else {
			return gcd(y, x.mod(y));
		}
	}

}
