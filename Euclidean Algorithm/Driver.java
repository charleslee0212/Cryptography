import java.util.ArrayList;

public class Driver {
	public static ArrayList<ArrayList<Integer>> data = new ArrayList<>();
	
	public static void main(String[] args) {
		System.out.println(mInverse(1237,120));
		System.out.println(data);
	}
	
	public static int mInverse(int n, int mod) {
		//check if n is greater than mod
		if(n > mod) {
			n = n % mod;
		}
		//check to see if n is a multiplicative inverse if not return -1
		ArrayList<Integer> mInverses = multiplicationInverse(mod);
		if(!mInverses.contains(n)) {
			return 0;
		}
		ArrayList<Integer> arr = new ArrayList<>();
		int inverse = 0;
		gcd(n,mod);
		for(int i = 0; i < data.size(); i++) {
			switch (i) {
			case 0:
				inverse = data.get(i).get(2);
				arr.add(inverse);
				break;
			case 1:
				inverse *= data.get(i).get(2);
				if(data.get(i).contains(n)) {
					inverse += 1;
				}
				arr.add(inverse);
				break;
			default:
				inverse *= data.get(i).get(2);
				inverse += arr.get(i-2);
				arr.add(inverse);
			}
		}
		return inverse;
	}
	
	public static int gcd(int x, int y) {
		ArrayList<Integer> remainderEQ = new ArrayList<>();
		if(y == 0) {
			return x;
		}else {
			if(y < x && x%y != 0) {
				remainderEQ.add(x%y);
				remainderEQ.add(x);
				int n = -(x-(x%y))/y;
				remainderEQ.add(n);
				remainderEQ.add(y);
				data.add(remainderEQ);
			}
			return gcd(y, x%y);
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
