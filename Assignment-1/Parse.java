import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Parse {

	public static void main(String[] args) throws IOException {
		//Reads a text file line by line from the target file location.
		FileReader input = new FileReader("/Users/favian.silva/eclipse-workspace/A1/src/test.txt");
		BufferedReader bufRead = new BufferedReader(input);
		
		//Stores the buffer/.txt file line into a string.
		String myLine = null;
		
		//INIT block flags.
		boolean name_flag = false;
		boolean forced_flag = false;
		boolean forb_flag = false;
		boolean near_flag = false;
		boolean mach_flag = false;
		boolean toonear_flag = false;
		
		int i = 0;
		int j = 0;
		int k = 0;
		int m_row = 0;
		int p = 0;
		
		//Init String/Int arrays.
		String f_array[] = new String[8];
		String forb_array[] = new String[8];
		String near_array[] = new String[8];
		String mach_array[][] = new String[8][8];
		String toonear_array[] = new String[8];


		//Reads txt file line by line checking for flags. Once the flag is set for a hard or soft constraint the lines 
		//are appended to an array removing parenthesis and commas.
		
		//TODO check machine and task bounds before being apended into an array.
		while ((myLine = bufRead.readLine()) != null)
		{    
			//Sets name flag to check if there is a name in the following .txt line.
			if (myLine.contains("Name:")) {
					name_flag = true;
					continue;
			}
			//Outputs a parse error if no name is given in the .txt file.
			if (name_flag) {
				if("".equals(myLine.trim())) {
					System.out.println("Error while parsing input file");
					break;
				}
				name_flag = false;
			}
			
			//Sets flag to append forced partial assignment lines into array on next loop iteration.
			if (myLine.contains("forced partial assignment:")) {
				forced_flag = true;
				continue;
			}
			//Appends forced partial assignments into a String array of 8
			//Outputs a parse error if no name is given in text file.
			if (forced_flag) {
				if(i < 8 && "".equals(myLine.trim()) != true) {
					String str_format = myLine.toString().replace(",","").replace("(","").replace(")","").trim(); //removes , ( )
					if(true) { //validChar(str_format.substring(0,1))) { //Checks that it is a valid character A-H or 1-8. TODO fix error check. 
						//!!!true temp holder
						f_array[i] = str_format;
						i++;
						continue;
					}else {
						System.out.println("invalid machine/task");
						break;
					}

					
					//fills in the rest of the array with "null" without iterating through the file read loop.
				}else if ( "".equals(myLine.trim())){
					for (i = i; i < 8; i++){
						f_array[i] = "null";
					}
					forced_flag = false;
					continue;
				}
			}
			//forbiden flag check
			if (myLine.contains("forbidden machine:")) {
				forb_flag = true;
				continue;
			}
			
			if (forb_flag) {
				if(j < 8 && "".equals(myLine.trim()) != true) {
					String str_format = myLine.toString().replace(",","").replace("(","").replace(")","").trim();
					if(true) { //validChar(str_format.substring(0,1)) && validChar(str_format.substring(1))) attempted error check
						forb_array[j] = str_format;
						j++;
						continue;
					}else {
						System.out.println("invalid machine/task");
						break;
					}
					
				}else if ( "".equals(myLine.trim())){
					for (j = j; j < 8; j++){
						forb_array[j] = "null";
					}
					forb_flag = false;
					continue;
				}
			}
			//TOO NEAR TASK

			if (myLine.contains("too-near tasks:")) {
				near_flag = true;
				continue;
			}
			
			if (near_flag) {
				if(k < 8 && "".equals(myLine.trim()) != true) {
					String str_format = myLine.toString().replace(",","").replace("(","").replace(")","").trim();
					if(true) { //validChar(str_format.substring(0,1)) && validChar(str_format.substring(1))) attempted error check
						near_array[k] = str_format;
						k++;
						continue;
					}else {
						System.out.println("invalid machine/task");
						break;
					}
				//k is the number of items in an array too near task array	
				}else if ( "".equals(myLine.trim())){
					for (k = k; k < 8; k++){
						near_array[k] = "null";
					}
					near_flag = false;
					continue;
				}
			}
			
			
			//Machine penaltise
		    String array1[] = myLine.split(" "); //splits the string of numbers with whitespace " "
		    
		    //Sets the machine penalties flag.
			if (myLine.contains("machine penalties:")) {
				mach_flag = true;
				continue;
			}
			//Assignes machine penalties, breaking if finding more than 8 lines of machine penalties.
			if (mach_flag && m_row <8) {
			    String hold = new String();
			    for (int m_col = 0; m_col < array1.length; m_col++) {
			    	hold = array1[m_col];
			    	mach_array[m_row][m_col] = hold;		    	
			    }
			    m_row++;
			//checks if there is more than 8 lines for machine penaltys
			}else if(m_row == 8 && "".equals(myLine.trim())== true) {
				mach_flag = false;
				continue;
			}else if (mach_flag && m_row == 8 && "".equals(myLine.trim())!= true) {
				System.out.println("maching penalty errror");
				break;
			}
			
			if (myLine.contains("too-near penalities")) {
				toonear_flag = true;
				continue;
			}
			if (toonear_flag) {
				if(p < 8 && "".equals(myLine.trim()) != true) {
					String str_format = myLine.toString().replace(",","").replace("(","").replace(")","").trim();
					if(true) { //validChar(str_format.substring(0,1)) && validChar(str_format.substring(1))) attempted error check
						toonear_array[p] = str_format;
						p++;
						continue;
					}else {
						System.out.println("invalid machine/task");
						break;
					}
				//k is the number of items in an array too near task array	
				}else if ( "".equals(myLine.trim())){
					for (p = p; p < 8; p++){
						near_array[p] = "null";
					}
					toonear_flag = false;
					continue;
				}
			}
			
			
		
	    
		    
		    //TEST CASES
		    //System.out.println(Arrays.deepToString(array1));

		}
		    System.out.println(Arrays.deepToString(mach_array));
			System.out.println(Arrays.deepToString(f_array));
			System.out.println(Arrays.deepToString(forb_array));
			System.out.println(Arrays.deepToString(near_array));
			System.out.println(Arrays.deepToString(toonear_array));
			bufRead.close();
		
			
			
	}
	public static boolean validChar(String ch) {

		String valid[] = {"A","B","C","D","E","F","G","H","1","2","3","4","5","6","7","8"};
		boolean contains = Arrays.stream(valid).anyMatch("ch"::equals);
		if (contains) {
			return true;
		}else {
		return false;
		}		
	}

}