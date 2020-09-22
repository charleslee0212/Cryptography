
public class Driver {

	public static void main(String[] args) {
		//System.out.println(ShiftCryptanalysis("NVRIVLEUVIJZVXVKYVCVXZFEZJZEXIRMVUREXVISVNRIVKYVVEVDPZJTCFJZEXZEFEPFL"));
		System.out.println(shift("If he had anything confidential to say he wrote it in cipher that is by so changing the order of the letters of the alphabet that not a word could be made out.", 20));
		//System.out.println(frequency("GHPPATMATIIXGLTLDXWMAXFTGBGUETVDPXYTVXXTVAHMAXKTLZHWBGMXGWXWYXSSBDLTBWGHMKBVDLGHPXTIHGLLDBEETZTBGLMLDBEETEHGXRHNFXTGRHNEEINMWHPGRHNKKHVDTGWBEEINMWHPGFRLPHKWTGWPXEEMKRMHDBEEXTVAHMAXKEBDXVBOBEBSXWIXHIEXBLMATMBM"));
		String str = "FORYOURINFORMATIONTHERESALOTMORETOORGESTHANPEOPLETHINKEXAMPLEEXAMPLEOKAYUMORGESARELIKEONIONSSNIFFSTHEYSTINKYESNOTHEYMAKEYOUCRYNOYOULEAVETHEMINTHESUNTHEYGETALLBROWNSTARTSPROUTINLITTLEWHITEHAIRSNOLAYERSONIONSHAVELAYERSORGESHAVELAYERSONIONSHAVELAYERSYOUGETITWEBOTHHAVELAYERSSIGHSOHYOUBOTHHAVELAYERSOHSNIFFSYOUKNOWNOTEVERYBODYLIKESONIONSCAKEEVERYBODYLOVESCAKESCAKESHAVELAYERSIDONTCAREWHATEVERYONELIKESORGESARENOTLIKECAKESYOUKNOWWHATELSEEVERYBODYLIKESPARFAITSHAVEYOUEVERMETAPERSONYOUSAYLETSGETSOMEPARFAITTHEYSAYNOIDONTLIKENOPARFAITPARFAITSAREDELICIOUSNOYOUDENSEIRRITATINGMINIATUREBEASTOFBURDENORGESARELIKEONIONSANDOFSTORYBYEBYESEEYALATERPARFAITSMAYBETHEMOSTDELICIOUSTHINGONTHEWHOLEDAMNPLANETYOUKNOWITHINKIPREFERREDYOURHUMMINGDOYOUHAVEATISSUREORSOMETHINGIMMAKINGAMESSJUSTTHEWORDPARFAITMAKEMESTARTSLOBBERING";
		//String str = "XVICYCJJNJGYDEDQGOTLWYVWKTGUMSJLKSYZYFSXZHETOWHMEXZPEOOFSNPPWLOEWXDFOOSFLQYZYFSEJLCMUMGOISFZJRSNXTTLWFJXSVCZEWFVKLOGEBKIQVLGBGFPYSMSVEFMLIEQAUKLOAMOTLWFXIDIDMBVGDEWDIJUSTJVLXSVDJTXDLNLSBWIAMJZESVIQFRWGUZSXAZBVIDHPIBAGSGIKORZOTSZEVKVEMYVKIAZWSRCOZKZOYYLKMDEWCOXZORZOTSZEVKZZKRAGIYSMIFXRPSWEPSFVVCWZTNMXMJCYCCOOAFVKIFMJZBSVFCMUMKPNMGUJGKSWFVIJFSSNGDPVIKJROOAUBKIKORZOTSZEVKPUSXBUBRIOORXODWSYSFLCMUMKPRKWZRVOVGULMCLTEUMKZOYCUFAGPSUEPKLVZOZQCOHQSZOOAHBRJSPKWRINFYSMLMIBUWUATWYJSXGGVSEQSVXCOWUSSELGEBNSJTXZLPWKGFPIHGUKPSSWOOTSYWESBHBRJSPKWKZWEEPAJZSEAFPYSMKVRCMASRMLHKMXOEJNMSALVOJWBSXGMSYBLWOOVYLJEBMDJKIGUZSXASODSXZKSBGTZEFQLJIOGSMAXWYGEBNSJTWEHPFOBZFMSKAUIVQUJOYKAYMXOGOTLWDYSVMVBMRHSRROBQPUOFVNMDPAOKMHYVJOZJFDCGBILEUEJNKVVPSEPSWEELPJWEZWPRWGTVXRQFHIQEHBMXOSNEWKQLWDBZFWSJKGEBNSJTQSRVQOALBRXKSFFLMJJNK";
		String str1 = "";
		for(int i = 0; i < str.length()/10; i++) {
			str1 = str1 + str.charAt(i*10 + 3);
		}
		System.out.println(str1);
		System.out.println(frequency(str1));
	}
	
	public static String Encipher(String message, int key) {
		message = message.replaceAll("\\s", "");
		message = message.toLowerCase();
		key = key % 25;
		if(key < 0) {
			key =+ 25;
		}
		String result = "";
		for(int i = 0; i < message.length(); i++) {
			int x = message.charAt(i) - 'a';
			x = x + key +'A';
			if(x > 'Z') {
				x = (x % 'Z') - 1; //minus one to shift not skip 'A'
				x = x + 'A';
			}
			result = result + (char) x;
		}
		return result;
	}
	
	public static String shift(String message, int key) {
		message = message.replaceAll("\\s", "");
		message = message.toUpperCase();
		key = key % 25;
		if(key < 0) {
			key += 26;
		}
		String result = "";
		for(int i = 0; i < message.length() - 2; i++) {
			int x = message.charAt(i) - 'A';
			x = x + key +'A';
			if(x > 'Z') {
				x = (x % 'Z') - 1; //minus one to shift not skip 'A'
				x = x + 'A';
				System.out.println(message.charAt(i));
			}
			result = result + (char) x;
		}
		return result;
	}
	
	public static String ShiftCryptanalysis(String s) {
		System.out.println("ShiftCipher: " + s);
		s = s.replaceAll("\\s", "");
		s = s.toUpperCase();
		String results = "";
		for(int i = 0; i < 26; i++) {
			String shifted = "";
			for(int j = 0; j < s.length(); j++) {
				int x = s.charAt(j) - 'A';
				x = x + i + 'a';
				if(x > 'z') {
					x = (x % 'z') - 1; //minus one to shift not skip 'a'
					x = x + 'a';
				}
				shifted = shifted + (char) x;
			}
			String key = "key " + (26-i)%26 + ": ";
			results = results + key + shifted + "\n";
		}
		return results;
	}
	
	public static String frequency(String message) {
		message = message.replaceAll("\\s", "");
		message = message.toUpperCase();
		String results = "";
		double IC = 0;
		double[] frequency = new double[26];
		for(int i = 0; i < message.length(); i++) {
			int x = message.charAt(i) - 'A';
			frequency[x]++;
		}
		for(int i = 0; i < frequency.length; i++) {
			char c = (char) (i + 'A');
			results = results + c + ": " + frequency[i]/message.length() + "\n";
			IC = IC + Math.pow(frequency[i]/message.length(), 2);
		}
		
		return results + "\n" + "Index of coinidence: " +IC;
	}

}
