import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class main {

	public static void  main(String[] args){
//		ArrayList<String> forced = new ArrayList<String>(Arrays.asList("1A", "2B", "3C", "4D", "5E"));
//		ArrayList<String> forbidden = new ArrayList<String>(Arrays.asList("6F"));
//		ArrayList<String> toonear = new ArrayList<String>(Arrays.asList());
//		int[][] penalties = {
//				{1, 1, 1, 1, 1, 1, 1, 1},
//				{1, 1, 1, 1, 1, 1, 1, 1},
//				{1, 1, 1, 1, 1, 1, 1, 1},
//				{1, 1, 1, 1, 1, 1, 1, 1},
//				{1, 1, 1, 1, 1, 1, 1, 1},
//				{1, 1, 1, 1, 1, 1, 1, 1},
//				{1, 1, 1, 1, 1, 1, 1, 1},
//				{1, 1, 1, 1, 1, 1, 1, 1}};
//		ArrayList<String> toonearPenal = new ArrayList<String>(Arrays.asList("EG3","GH7","FH7", "HG9", "FG8"));
//		printBest(forced, forbidden, toonear, penalties, toonearPenal);
		

		// Create Parse object with desired input file & output file locations
		Parse parser = new Parse(args[0], args[1]);
		
		// Parse through each line of the input file and quit upon proccessing errors. Must put Parse opbject in try catch block as
		// methodn throws IOException.
		try {
			parser.parse();
			
			
			
			
		} catch (Exception e) {System.out.println("Error");}
		
		try {
			PrintWriter p = new PrintWriter(args[1]);
			ArrayList<String> forced = new ArrayList<String>(Arrays.asList(parser.forcedpartial_array));
			ArrayList<String> forbidden = new ArrayList<String>(Arrays.asList(parser.forbiddenmachine_array));
			ArrayList<String> toonear = new ArrayList<String>(Arrays.asList(parser.neartask_array));
			int[][] penalties = parser.mach_array;
			ArrayList<String> toonearPenal = new ArrayList<String>(Arrays.asList(parser.toonearpenal_array));
			forced.removeAll(Collections.singleton(null));
			forbidden.removeAll(Collections.singleton(null));
			toonear.removeAll(Collections.singleton(null));
			toonearPenal.removeAll(Collections.singleton(null));
			String output = printBest(forced, forbidden, toonear, penalties, toonearPenal);
			p.write(output);
			p.close();
			System.exit(0);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

		// To access arrays, use parser.forcedpartial_array, parser.forbiddenmachine_array, etc..
		// All these instance variables are listed at the top of the file.
	}
	static String printBest(ArrayList<String> forced, ArrayList<String> forbidden, ArrayList<String> toonear, int[][] penalties, ArrayList<String> toonearPenal) {

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
					System.out.println(total);
				}

			}
			possibleValues.add(total);
		}
		if(possibleValues.size() > 0) {
			int lowest = possibleValues.get(0);
			int lowestIndex = 0;
			for(int z =0; z<possibleValues.size(); z++) {
				if(possibleValues.get(z)<lowest) {
					lowest = possibleValues.get(z);
					lowestIndex= z;
				}
				
			}
			
			String best = possible.get(lowestIndex);
			String output = "Solution " + best.substring(0, 1) + " " + best.substring(1, 2) + " " + best.substring(2, 3) + " " + best.substring(3, 4) + " " + best.substring(4, 5) + " " + best.substring(5, 6) + " " + best.substring(6, 7) + " " + best.substring(7, 8) + "; Quality: " + lowest;
			return output;
		}else {
			return "No valid solution possible!";
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
