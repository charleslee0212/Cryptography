import java.util.ArrayList;

public class Driver {
	public static void main(String[] args) {
		//System.out.println(Affine(3,9,"JOHNNY How can they say this about me I dont believe it Lisa has been unfaithful and that woman her mother cares more about her life threatening cancer than she does about me HE MAKES QUOTATION SIGNS WITH HIS FINGERS AS HE SAYS LIFE THREATENING Ill show them Ill record everything JOHNNY WALKS DOWN THE STAIRCASE AND OVER TO THE TABLE WHERE THE PHONE IS AND SITS DOWN HE PULLS OUT A TAPE AND PLACES IT INTO THE TAPE RECORDER HE INSTALLS THE RECORDING DEVICE INTO THE PHONE AND HIDES IT UNDER THE TABLE JOHNNY THEN WALKS AWAY AND GOES UPSTAIRS TO THE BEDROOM END SCENE"));
		//System.out.println(Affine(3,9,"t"));
		System.out.println(AffineDecipher(7,5,"LUFSALUBAPUBEHRZOSPLMHUOZPUGUJWHIAUJWHDHUHGUZPAIZBFRICFIICHRDHUHGHUOHTIERSZULFEICFSXRZPWHURLPTCICHRDHUHICHEFBIGHZGEHRZPAHKGHTIIZMHJSWZEWHAJSFSRICJSVBIUFSVHZULRBIHUJZPBMHTFPBHICHRQPBIAJASICZEADJICBPTCSZSBHSBHLUAPUBEHRDFBICHAJUHTIZUZOFOJULTFEEHAVUPSSJSVBDCJTCLFAHAUJEEBCHDFBFMJVMHHORLFSDJICCFUAERFSRSHTXFEICZPVCCHAJACFWHFWHUREFUVHLPBIFTCHLUBAPUBEHRDFBICJSFSAMEZSAHFSACFASHFUERIDJTHICHPBPFEFLZPSIZOSHTXDCJTCTFLHJSWHURPBHOPEFBBCHBGHSIBZLPTCZOCHUIJLHTUFSJSVZWHUVFUAHSOHSTHBBGRJSVZSICHSHJVCMZUBICHAPUBEHRBCFAFBLFEEBZSTFEEHAAPAEHRFSAJSICHJUZGJSJZSICHUHDFBSZOJSHUMZRFSRDCHUHICHAPUBEHRBCFAHWHURICJSVICHRDFSIHAMPIICHRFEBZCFAFBHTUHIFSAICHJUVUHFIHBIOHFUDFBICFIBZLHMZARDZPEAAJBTZWHUJIICHRAJASIICJSXICHRTZPEAMHFUJIJOFSRZSHOZPSAZPIFMZPIICHGZIIHUBLUBGZIIHUDFBLUBAPUBEHRBBJBIHUMPIICHRCFASILHIOZUBHWHUFERHFUBJSOFTILUBAPUBEHRGUHIHSAHABCHAJASICFWHFBJBIHUMHTFPBHCHUBJBIHUFSACHUVZZAOZUSZICJSVCPBMFSADHUHFBPSAPUBEHRJBCFBJIDFBGZBBJMEHIZMHICHAPUBEHRBBCPAAHUHAIZICJSXDCFIICHSHJVCMZUBDZPEABFRJOICHGZIIHUBFUUJWHAJSICHBIUHHIICHAPUBEHRBXSHDICFIICHGZIIHUBCFAFBLFEEBZSIZZMPIICHRCFASHWHUHWHSBHHSCJLICJBMZRDFBFSZICHUVZZAUHFBZSOZUXHHGSVICHGZIIHUBFDFRICHRAJASIDFSIAPAEHRLJKJSVDJICFTCJEAEJXHICFI"));
	}
	
	public static String Affine(int a, int b, String message) {
		message = message.replaceAll("\\s", "");
		message = message.toLowerCase();
		String result = "";
		ArrayList<Integer> inverses = multiplicationInverse(26);
		if(inverses.contains(a) || inverses.contains(b)) {
			for(int i = 0; i < message.length(); i++) {
				int n = message.charAt(i) - 'a';
				int cipherInt = (a*n + b) % 26;
				cipherInt = cipherInt + 'A';
				if(cipherInt > 'Z') {
					cipherInt = (cipherInt % 'Z') - 1; //minus one to shift not skip 'A'
					cipherInt = cipherInt + 'A';
				}
				result = result + (char) cipherInt;
			}
			return result;
		}else {
			return "ERROR: key is not an multiplicative inverse";
		}
	}
	
	public static String AffineDecipher(int a, int b, String message) {
		message = message.replaceAll("\\s", "");
		message = message.toUpperCase();
		String result = "";
		ArrayList<Integer> inverses = multiplicationInverse(26);
		if(inverses.contains(a) || inverses.contains(b)) {
			for(int i = 0; i < message.length(); i++) {
				int n = message.charAt(i) - 'A';
				while(!((n-b)%a == 0)) {
					b -= 26;
				}
				int decipherInt = ((n - b)/a) % 26;
				decipherInt = decipherInt + 'a';
				if(decipherInt > 'z') {
					decipherInt = (decipherInt % 'z') - 1; //minus one to shift not skip 'A'
					decipherInt = decipherInt + 'a';
				}
				result = result + (char) decipherInt;
			}
			return result;
		}else {
			return "ERROR: key is not an multiplicative inverse";
		}
	}
	
	//creates the multiplication table
		public static ArrayList<Integer> multiplicationInverse(int n){
			int[][] table = new int[n][n];
			for(int i = 0; i < table.length; i++) {
				for(int j = 0; j < table[i].length; j++) {
					int x = (i * j) % n;
					table[i][j] = x;
				}
			}
			
			return inverseTable(table);
		}
		
		//count all the ones
		public static ArrayList<Integer> inverseTable(int[][] table) {
			int c = 0;
			ArrayList<Integer> inverses = new ArrayList<>();
			for(int i = 0; i < table.length; i++) {
				for(int j = 0; j < table[i].length; j++) {
					if(table[i][j] == 1) {
						inverses.add(i);
					}
				}
			}
			return inverses;
		}
}
