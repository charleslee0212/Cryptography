import java.util.ArrayList;
import java.util.Scanner;

public class Driver {
	public static void main(String[] args) {
		//userInput
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Enter CipherText:");
		String message = keyboard.next();
		System.out.println(vigenereCryptanalysis(message));
	}

	public static String vigenereCryptanalysis(String message) {
		final double[] ENGLISH_FREQ = {
				0.082, /* a */
				0.015, /* b */
				0.028, /* c */
				0.043, /* d */ 
				0.127, /* e */
				0.022, /* f */
				0.020, /* g */
				0.061, /* h */
				0.070, /* i */
				0.002, /* j */
				0.008, /* k */
				0.040, /* l */
				0.024, /* m */
				0.067, /* n */
				0.075, /* o */
				0.019, /* p */
				0.001, /* q */
				0.060, /* r */
				0.063, /* s */
				0.091, /* t */
				0.028, /* u */
				0.010, /* v */
				0.023, /* w */
				0.001, /* x */
				0.020, /* y */
				0.001  /* z */
			};
		message.replaceAll("\\s", "");
		message.toUpperCase();
		int keyLength = keyLengthFinder(message);
		//divides the message according to the key length
		ArrayList<String> segments = new ArrayList<>();
		//lines up the segments on top of each other and finds all possible shifts to the according column 
		ArrayList<ArrayList<String>> allPossibleShifts = new ArrayList<ArrayList<String>>();
		String possibleKey = "";
		//initializing the segments
		for(int i = 0; i < keyLength; i++) {
			String str = "";
			for(int j = 0; j < message.length()/keyLength; j++) {
				if(j * keyLength + i < message.length()) {
					str = str + message.charAt(j * keyLength + i);
				}
			}
			segments.add(str);
		}
		
		//finding all possible shifts from 0 - 25
		for(int i = 0; i < segments.size(); i++) {
			allPossibleShifts.add(shiftX26(segments.get(i)));
		}
		
		//finds the maximum Mutual Index of Coincidence for each column and the highest number will most likely be the key of that column 
		for(int i = 0; i < allPossibleShifts.size(); i++) {
			double[] MICs = new double[allPossibleShifts.get(i).size()];
			for(int j = 0; j < allPossibleShifts.get(i).size(); j++) {
				MICs[j] = findMIC(ENGLISH_FREQ, frequencies(allPossibleShifts.get(i).get(j)));
			}
			String MICsTable = "[";
			for(int j = 0; j < MICs.length; j++) {
				MICsTable = MICsTable + MICs[j] + ", ";
			}
			MICsTable = MICsTable.substring(0, MICsTable.length() - 3);
			MICsTable = MICsTable + "]" + " Max: " + MICs[findMaxMIC(MICs)];
			System.out.println("This is the MICs for possible key " + i + ": ");
			System.out.println(MICsTable);
			int key = (-findMaxMIC(MICs)) % 25;
			if(key < 0) {
				key += 26;
			}
			int x = 0;
			x = x + key +'A';
			if(x > 'Z') {
				x = (x % 'Z') - 1; //minus one to shift not skip 'A'
				x = x + 'A';
			}
			possibleKey = possibleKey + (char) x;
		}
		
		//returns the plaintext message
		return "This is the possible key: " + possibleKey + "\n" + "PlainText: " + "\n" + Vigenere(message, possibleKey);
		
	}
	
	//Decipher
	public static String Vigenere(String message, String key) {
		message = message.toUpperCase();
		key = key.toUpperCase();
		message = message.replaceAll("\\s", "");
		String result = "";
		int[] keyINT = new int[key.length()];
		for(int i = 0; i < key.length(); i++) {
			keyINT[i] = key.charAt(i) - 'A';
		}
		for(int i = 0; i < message.length(); i++) {
			result = result + decipher(message.charAt(i), keyINT[i%key.length()]);
		}
		
		return result;
	}
	
	//helper for decipher
	public static char decipher(char letter, int key) {
		key = (26 - key) % 26;
		if(key < 0) {
			key =+ 26;
		}
		int x = letter - 'A';
		x = x + key +'a';
		if(x > 'z') {
			x = (x % 'z') - 1; //minus one to shift not skip 'A'
			x = x + 'a';
		}
		char result = (char) x;
		return result;
	}
	
	//shifts the given message
	public static String shift(String message, int key) {
		message = message.replaceAll("\\s", "");
		message = message.toUpperCase();
		key = key % 25;
		if(key < 0) {
			key += 26;
		}
		String result = "";
		for(int i = 0; i < message.length(); i++) {
			int x = message.charAt(i) - 'A';
			x = x + key +'A';
			if(x > 'Z') {
				x = (x % 'Z') - 1; //minus one to shift not skip 'A'
				x = x + 'A';
			}
			result = result + (char) x;
		}
		return result;
	}
	
	//finds the max Mutual Index of Coincidence
	public static int findMaxMIC(double[] arr) {
		double max = arr[0];
		int maxIndex = 0;
		for(int i = 0; i < arr.length; i++) {
			if(max < arr[i]) {
				max = arr[i];
				maxIndex = i;
			}
		}
		return maxIndex;
	}
	
	//shifts the given message 26 times
	public static ArrayList<String> shiftX26(String message){
		ArrayList<String> shifts = new ArrayList<>();
		for(int i = 0; i < 26; i++) {
			shifts.add(shift(message, i));
		}
		return shifts;
	}
	
	//finds the Mutual Index of Coincidence
	public static double findMIC(double[] english, double[] cipher) {
		double MIC = 0;
		for(int i = 0; i < english.length; i++) {
			MIC = MIC + (english[i] * cipher[i]);
		}
		return MIC;
	}
	
	//finds key length
	public static int keyLengthFinder(String message) {
		//two arrays to hold the actual gcd values and their frequency in the corresponding index
		ArrayList<Integer> gcds = new ArrayList<>();
		ArrayList<Integer> frequencies = new ArrayList<>();
		ArrayList<Integer> repetitions = repetitionCount(message);
		for(int i = 0; i < repetitions.size(); i++) {
			if(i + 1 < repetitions.size()) {
				int gcdofPair = gcd(repetitions.get(i), repetitions.get(i+1));
				if(gcds.contains(gcdofPair)) {
					//adds 1 to the frequency if already contains
					frequencies.set(gcds.indexOf(gcdofPair), frequencies.get(gcds.indexOf(gcdofPair)) + 1);
				}else {
					gcds.add(gcdofPair);
					frequencies.add(1);
				}
			}
		}
		int keyLength = gcds.get(max(frequencies));
		System.out.println("GCDs: " + gcds);
		System.out.println("Frequcies: " + frequencies);
		System.out.println("Max number of frequent gcd: " + keyLength + " therefore this is the possible key length");
		return keyLength;
	}
	//counts repetition
	public static ArrayList<Integer> repetitionCount(String message){
		ArrayList<Integer> frequency = new ArrayList<>();
		for(int i = 0; i < message.length(); i++) {
			if(i + 3 < message.length()) {
				String str = message.substring(i, i + 3);
				for(int j = i + 1; j < message.length(); j++) {
					if(j + 3 < message.length()) {
						if(str.equals(message.substring(j, j + 3))) {
							frequency.add(j - i);
						}
					}
				}
			}
		}
		return frequency;
	}
	//finds gcd
	public static int gcd(int x, int y) {
		if(y == 0) {
			return x;
		}else {
			return gcd(y, x%y);
		}
	}
	
	//finds max for the given ArrayList
	public static int max(ArrayList<Integer> arr) {
		int max = arr.get(0);
		int maxIndex = 0;
		for(int i = 0; i < arr.size(); i++) {
			if(max < arr.get(i)) {
				max = arr.get(i);
				maxIndex = i;
			}
		}
		return maxIndex;
	}
	
	//finds the frequency if letters in a given message
	public static double[] frequencies(String message) {
		double[] frequency = new double[26];
		for(int i = 0; i < message.length(); i++) {
			int x = message.charAt(i) - 'A';
			frequency[x]++;
		}
		for(int i = 0; i < frequency.length; i++) {
			frequency[i] = frequency[i]/message.length();
		}
		
		return frequency;
	}
}
