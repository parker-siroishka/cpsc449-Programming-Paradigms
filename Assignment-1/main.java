package MachineTaskCalc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<String> forced = new ArrayList(Arrays.asList("A", "B", "C", "D", "X", "X", "X", "X"));
		ArrayList<String> forbidden = new ArrayList(Arrays.asList("3A", "3B", "4C", "5E"));
		ArrayList<String> toonear = new ArrayList(Arrays.asList("HF","EA","HA"));
		
		int[][] penalties = {
				{1, 1, 1, 1, 1, 1, 1, 1},
				{1, 1, 1, 1, 1, 1, 1, 1},
				{1, 1, 1, 1, 1, 1, 1, 1},
				{1, 1, 1, 1, 1, 1, 1, 1},
				{1, 1, 1, 1, 1, 1, 1, 1},
				{1, 1, 1, 1, 1, 1, 1, 1},
				{1, 1, 1, 1, 1, 1, 1, 1},
				{1, 1, 1, 1, 1, 1, 1, 1}};
		
		ArrayList<String> toonearPenal = new ArrayList(Arrays.asList("CD32","EF12","HF43"));
		
		ArrayList<String> allCombinations = new ArrayList();
		ArrayList<String> possible = new ArrayList();
		
		ArrayList<String> tasks = new ArrayList(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H"));
		
		calcCombinations(allCombinations, tasks, "");
		mainLoop:
		for(int i = 0; i<allCombinations.size(); i++) {
			String combo = allCombinations.get(i);
			for(int j = 0; j<forced.size(); j++) {
				String forcedTask = forced.get(j); 
				if(forcedTask!="X" && !forcedTask.equals(combo.substring(j,j+1))) {
					//System.out.println(j+ " "  + forcedTask + " " + combo + " " + combo.substring(j,j+1));
					continue mainLoop;
				}
			}
			for(int j = 0; j<forbidden.size(); j++) {
				int mach = Integer.parseInt(forbidden.get(j).substring(0,1));
				String task = forbidden.get(j).substring(1); 
				
				if(combo.substring(mach-1, mach).equals(task)) {
					//System.out.println(combo + " " + combo.substring(mach-1, mach) + " " + task);
					continue mainLoop;
				}
			}
			for(int j = 0; j<toonear.size(); j++) {
				String pair = toonear.get(j).substring(0,2);
				
				if(combo.contains(pair) || pair.equals(combo.substring(7,8) + combo.substring(0,1))) {
					//System.out.println(combo + " " + pair + " " + combo.substring(6,7) + combo.substring(0,1));
					continue mainLoop;
				}
			}
			
			possible.add(combo);
			
		}
		for(int x=0;x<possible.size(); x++) {
			System.out.println(possible.get(x));
		}
	}
	
	static void calcCombinations(ArrayList<String> a, ArrayList<String> b, String str) {
		
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