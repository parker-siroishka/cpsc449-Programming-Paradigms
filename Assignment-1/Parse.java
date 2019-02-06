import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.BufferedWriter;



public class Parse {

		HeaderCheck headerCheck;

		// Filepath for FileReader object created in Parse() method.
		String inputParam;
		String outputParam;

		//Init String/Int arrays.
		String forcedpartial_array[] = new String[8];
		String forbiddenmachine_array[] = new String[56];
		String neartask_array[] = new String[8];
		int mach_array[][] = new int[8][8];
		String toonearpenal_array[] = new String[8];
		String used[] = new String[16];
		String used2[] = new String[16];

		// Constructor for Parse object. Enter customized filepath when instatiating object
		// i.e. Parse parser = new Parse("input_file.txt", "output_file.txt");
		public Parse(String inputPath, String outputPath){
			inputParam = inputPath;
			outputParam = outputPath;
			headerCheck = new HeaderCheck(inputParam);
		}



	public void parse() throws IOException {

			//Reads a text file line by line from the target file location.
			FileReader input = new FileReader(this.inputParam);
			//FileWriter fw = new FileWriter(this.outputParam);
			//BufferedWriter writer = new BufferedWriter(fw);
			BufferedReader bufRead = new BufferedReader(input);
			PrintWriter writer = new PrintWriter(this.outputParam);

		//Stores the buffer/.txt file line into a string.
		String myLine = null;

		//INIT block flags.
		boolean name_flag = false;
		boolean forced_flag = false;
		boolean forb_flag = false;
		boolean near_flag = false;
		boolean mach_flag = false;
		boolean toonear_flag = false;
				
		int use_ind = 0;
		int use_ind2 = 0;
		int i = 0;
		int j = 0;
		int k = 0;
		int m_row = 0;
		int p = 0;

		String[] inputArr = headerCheck.inputFileToList(headerCheck.inputFile);
		if(!headerCheck.isValidFile(inputArr)){
			System.out.println("Error while parsing input file");
			writer.write("Error while parsing input file");
			writer.close();
			System.exit(1);
		}

		
		//TODO check machine and task bounds before being apended into an array.
		while ((myLine = bufRead.readLine()) != null){    

			//Sets name flag to check if there is a name in the following .txt line.
			if (myLine.contains("Name:")) {
				name_flag = true;
				continue;
			}

			//Outputs a parse error if no name is given in the .txt file.
			if (name_flag) {
				if("".equals(myLine.trim())) {
					System.out.println("Error while parsing input file");
					writer.write("Error while parsing input file");
					writer.close();
					System.exit(1);
				}
				name_flag = false;
			}

			
			//FORCED PARTIAL ASSIGNMENT
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
					String first = str_format.substring(0,1);
					String second = str_format.substring(1,2);

					//PARTIALL ASSIGNMENT FLAG
					boolean partial_assflag = Arrays.stream(used).anyMatch(first::equals);
					boolean partial_assflag2 = Arrays.stream(used).anyMatch(second::equals);//Checks array for any pairs that are the same. Breaks if so and outputs partial assignment error.

					if (partial_assflag || partial_assflag2) {
						System.out.println("partial assignment error");
						writer.write("partial assignment error");
						writer.close();
						System.exit(1);
					}

					used[use_ind] = first;
					use_ind++;
					used[use_ind] = second;
					use_ind++;

					//END PARTIAL ASSIGNMENT FLAG ERROR

					if(validChar(str_format)) { //Checks that it is a valid character A-H or 1-8. TODO fix error check. 

						//!!!true temp holder
						forcedpartial_array[i] = str_format;
						i++;
						continue;
					}else {
						System.out.println("invalid machine/task");
						writer.write("invalid machine/task");
						writer.close();
						System.exit(1);
					}					

					//fills in the rest of the array with "null" without iterating through the file read loop.
				}else if ( "".equals(myLine.trim())){
					for (i = i; i < 8; i++){
						forcedpartial_array[i] = null;
					}
					forced_flag = false;
					continue;
				}

			}

			//FORBIDDEN MACHINE
			if (myLine.contains("forbidden machine:")) {
				forb_flag = true;
				continue;
			}

			if (forb_flag) {
				if(j < 56 && "".equals(myLine.trim()) != true) {
					String str_format = myLine.toString().replace(",","").replace("(","").replace(")","").trim();		

					if(validChar(str_format)) { //error check for A- H, 1 - 2
						forbiddenmachine_array[j] = str_format;
						j++;
						continue;
					}else {
						System.out.println("invalid machine/task");
						writer.write("invalid machine/task");
						writer.close();
						System.exit(1);
					}	

				}else if ( "".equals(myLine.trim())){
					for (j = j; j < 8; j++){
						forbiddenmachine_array[j] = null;
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
					if(validChar2(str_format)) {
						neartask_array[k] = str_format;
						k++;
						continue;
					}else {
						System.out.println("invalid machine/task");
						writer.write("invalid machine/task");
						writer.close();
						System.exit(1);
					}

				//k is the number of items in an array too near task array
				}else if ( "".equals(myLine.trim())){
					for (k = k; k < 8; k++){
						neartask_array[k] = null;
					}
					near_flag = false;
					continue;
				}
			}			

			//Machine penalties
		    String array1[] = myLine.split(" "); //splits the string of numbers with whitespace " "		    

		    //Sets the machine penalties flag.
			if (myLine.contains("machine penalties:")) {
				mach_flag = true;
				continue;
			}

			//Assignes machine penalties, breaking if finding more than 8 lines of machine penalties.
			if (mach_flag && m_row <8) {
			    String hold = new String();
			    if (array1.length != 8) { 						//Checks if the array length is less than 8. Breaks if so.
			    	System.out.println("machine penalty error");
			    	writer.write("machine penalty error");
			    	writer.close();
					System.exit(1);
			    }

			    for (int m_col = 0; m_col < array1.length; m_col++) {
			    	hold = array1[m_col];			    	

			    	//Checks if the integer is less than 0.
			    	if(isInteger(hold) != true) {
				    	System.out.println("machine penalty error");
				    	writer.write("machine penalty error");
				    	writer.close();
						System.exit(1);
			    	}

			    	mach_array[m_row][m_col] = Integer.parseInt(hold);
			    }

			    m_row++;

			//checks if there is more than 8 lines for machine penaltiess
			}else if(m_row == 8 && "".equals(myLine.trim())== true) {
				mach_flag = false;
				continue;

			}else if (mach_flag && m_row == 8 && "".equals(myLine.trim())!= true) {
				System.out.println("machine penalty error");
				writer.write("machine penalty error");
				writer.close();
				System.exit(1);
			}

			

			

			//Too near penalties
			if (myLine.contains("too-near penalities")) {
				toonear_flag = true;
				continue;
			}

			if (toonear_flag) {
				if(p < 8 && "".equals(myLine.trim()) != true) {
					String str_format = myLine.toString().replace(",","").replace("(","").replace(")","").trim();
					if(validChar(str_format)) {
						toonearpenal_array[p] = str_format;
						p++;
						continue;

					}else {
						System.out.println("invalid machine/task");
						writer.write("invalid machine/task");
						writer.close();
						System.exit(1);
					}

				//k is the number of items in an array too near task array
				}else if ( "".equals(myLine.trim())){
					for (p = p; p < 8; p++){
						neartask_array[p] = null;
					}

					toonear_flag = false;
					continue;
				}

			}

		}

			// System.out.println(Arrays.deepToString(forcedpartial_array));
			// System.out.println(Arrays.deepToString(forbiddenmachine_array));			
			// System.out.println(Arrays.deepToString(toonearpenal_array));						
			// System.out.println(Arrays.deepToString(mach_array));
			// System.out.println(Arrays.deepToString(neartask_array));
			bufRead.close();
			writer.close();
	}

	public boolean validChar(String ch) { // used for forced partial assignment, forbidden machine		

		String first = ch.substring(0,1);
		String second = ch.substring(1,2);
		String valid_num[] = {"1","2","3","4","5","6","7","8"};
		String valid_char[] = {"A","B","C","D","E","F","G","H"};

		if (ch.length()> 2 ) {
			boolean contains0 = Arrays.stream(valid_char).anyMatch(first::equals);
			boolean contains1 = Arrays.stream(valid_char).anyMatch(second::equals);			

			String third = ch.substring(2);			

			if (contains0 && contains1 && isInteger(third)) {
				return true;
			}else {
				return false;
			}
		}

		boolean contains0 = Arrays.stream(valid_num).anyMatch(first::equals);
		boolean contains1 = Arrays.stream(valid_char).anyMatch(second::equals);		

		if (contains0 && contains1) {
			return true;
		}else {
		return false;
		}		
	}	

	public boolean validChar2(String ch) { // for too-near tasks: block only		

		String first = ch.substring(0,1);
		String second = ch.substring(1,2);
		String valid_char[] = {"A","B","C","D","E","F","G","H"};		

		boolean contains0 = Arrays.stream(valid_char).anyMatch(first::equals);
		boolean contains1 = Arrays.stream(valid_char).anyMatch(second::equals);

		if (ch.length()> 2 ) {
				return false;
		}

		if (contains0 && contains1) {
			return true;
		}else {
				return false;
		}
	}


	public boolean isInteger(String s) {return isInteger(s,10);}



	//The sub function that takes a string enumerates through and and checks if the value is a int in base 10.
	public boolean isInteger(String s, int radix) {  // for machine penalties, too-near penalities 

		try {
			PrintWriter writer2 = new PrintWriter(this.outputParam);

	    	if(s.isEmpty()) return false;

	    	for(int i = 0; i < s.length(); i++) {
	        	if(i == 0 && s.charAt(i) == '-') {
	            	if(s.length() == 1) {
	            		System.out.println("Invalid penalty");
	            		writer2.write("Invalid penalty");
	            		writer2.close();
						System.exit(1);
	            	}else 
	            		continue;
	        	}

	        	if(Character.digit(s.charAt(i),radix) < 0) {
            		System.out.println("Invalid penalty");
	        		writer2.write("Invalid penalty");
	        		writer2.close();
					System.exit(1);
	        	}
	    	}
	    }catch (Exception e){System.exit(1);}
	    return true;
	}


	// Example utilization of class 
	public static void main(String[] args) {
	
		// Create Parse object with desired input file & output file locations
		Parse parser = new Parse("C:\\Users\\parke\\Desktop\\Projects\\Repos\\cpsc449-Programming-Paradigms\\Test Files\\wrongnumbermachine.txt", 
										"C:\\Users\\parke\\Desktop\\CPSC 449\\myoutput.txt");

		// Parse through each line of the input file and quit upon proccessing errors. Must put Parse opbject in try catch block as
		// methodn throws IOException.
		try {
			parser.parse();
		} catch (Exception e) {System.out.println("Error");}

		// To access arrays, use parser.forcedpartial_array, parser.forbiddenmachine_array, etc..
		// All these instance variables are listed at the top of the file.

	}



}
