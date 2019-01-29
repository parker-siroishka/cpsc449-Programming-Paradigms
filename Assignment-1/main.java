import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class main {



	public static void main(String[] args) {
		
		ArrayList<String> forced = new ArrayList<String>(Arrays.asList("1A", "2B", "3C", "4D", "5E"));
		ArrayList<String> forbidden = new ArrayList<String>(Arrays.asList("7F"));
		ArrayList<String> toonear = new ArrayList<String>(Arrays.asList("EF","FE"));
		int[][] penalties = {
				{1, 1, 1, 1, 1, 1, 1, 1},
				{1, 1, 1, 1, 1, 1, 1, 1},
				{1, 1, 1, 1, 1, 1, 1, 1},
				{1, 1, 1, 1, 1, 1, 1, 1},
				{1, 1, 1, 1, 1, 1, 1, 1},
				{1, 1, 1, 1, 1, 1, 10, 20},
				{1, 1, 1, 1, 1, 1, 10, 30},
				{1, 1, 1, 1, 1, 10, 1, 1}};
		ArrayList<String> toonearPenal = new ArrayList<String>(Arrays.asList());

		ArrayList<String> allCombinations = new ArrayList<String>();
		ArrayList<String> possible = new ArrayList<String>();
		
		ArrayList<String> tasks = new ArrayList<String>(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H"));

		
		// Calculates all the possible combinations and adds them to the ArrayList allCombinations
		calcCombinations(allCombinations, tasks, "");

		//Iterates through all the possible combinations
		mainLoop:
		for(int i = 0; i<allCombinations.size(); i++) {

			String combo = allCombinations.get(i);

			// Eliminates all non-possible combinations that violate forced tasks
			for(int j = 0; j<forced.size(); j++) {
				
				int forcedMachine = Integer.parseInt(forced.get(j).substring(0, 1));
				String forcedTask = forced.get(j).substring(1,2); 

				if(forcedTask!="X" && !forcedTask.equals(combo.substring(forcedMachine-1,forcedMachine))) {

					//System.out.println(j+ " "  + forcedTask + " " + combo + " " + combo.substring(j,j+1));
					continue mainLoop;

				}

			}
			
			// Eliminates all non-possible combinations that violate forbidden tasks
			for(int j = 0; j<forbidden.size(); j++) {

				int mach = Integer.parseInt(forbidden.get(j).substring(0,1));
				String task = forbidden.get(j).substring(1); 

				if(combo.substring(mach-1, mach).equals(task)) {

					//System.out.println(combo + " " + combo.substring(mach-1, mach) + " " + task);
					continue mainLoop;

				}

			}
			
			// Eliminates all non-possible combinations that violate too-near tasks
			for(int j = 0; j<toonear.size(); j++) {

				String pair = toonear.get(j).substring(0,2);

				if(combo.contains(pair) || pair.equals(combo.substring(7,8) + combo.substring(0,1))) {

					//System.out.println(combo + " " + pair + " " + combo.substring(6,7) + combo.substring(0,1));
					continue mainLoop;

				}

			}
			
			// adds the possible task to the ArrayList
			possible.add(combo);
		}
		ArrayList<Integer> possibleValues = new ArrayList<Integer>();
		
		// Prints out all possible combinations
		for(int x=0;x<possible.size(); x++) {
			String combo = possible.get(x);
			int total = 0;
			//assign penalty scores
			for(int y=0; y<combo.length(); y++) {
				char c = combo.charAt(y);
				int index = (int)c - 65;
				int value = penalties[index][y];
				total = value + total;
			}
			
			//assign too near penalties
			for(int j = 0; j<toonearPenal.size(); j++) {

				String pair = toonearPenal.get(j).substring(0,2);

				if(combo.contains(pair) || pair.equals(combo.substring(7,8) + combo.substring(0,1))) {
					total+=Integer.parseInt(toonearPenal.get(j).substring(2));
				}

			}
			possibleValues.add(total);
		}
		if(possibleValues.size() > 0) {
			int lowest = possibleValues.get(0);
			int lowestIndex = 0;
			for(int z =0; z<possibleValues.size(); z++) {
				if(possibleValues.get(z)>lowest) {
					lowest = possibleValues.get(z);
				}
				
			}
			
			String best = possible.get(lowestIndex);
			String output = "Solution " + best.substring(0, 1) + " " + best.substring(1, 2) + " " + best.substring(2, 3) + " " + best.substring(3, 4) + " " + best.substring(4, 5) + " " + best.substring(5, 6) + " " + best.substring(6, 7) + " " + best.substring(7, 8);
			System.out.println(output + "; Quality: " + lowest);
		}else {
			System.out.println("No valid solution possible!");
		}
		
	}

	
	// Calculates all possible combinations
	// a = return arrayList
	// b = a
	static void calcCombinations(ArrayList<String> a, ArrayList<String> b, String str) {
		// base case
		if(b.size() == 0) a.add(str);
		else {
			for(int i=0; i<b.size(); i++) {
				ArrayList<String> c = new ArrayList<>(b);
				String temp = str + c.get(i);
				c.remove(i);
				calcCombinations(a, c, temp);

			}
		}
	}
}
