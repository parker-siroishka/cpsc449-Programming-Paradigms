import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.io.PrintWriter;



public class Parse {

		HeaderCheck headerCheck;

		// Filepath for FileReader object created in Parse() method.
		String inputParam;
		String outputParam;

		//Init String/Int arrays.
		
		String[] blockHeaders = new String[] {"Name:", "forced partial assignment:", "forbidden machine:",
				"too-near tasks:", "machine penalties:", "too-near penalities"};
		
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
		
		boolean docustart = false;
		boolean docuend = false;
				
		int use_ind = 0;
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


//Line by line reads a .txt file.
		while ((myLine = bufRead.readLine()) != null){    

			//Sets name flag to check if there is a name in the following .txt line.
			if (myLine.contains("Name:")) {
				name_flag = true;
				docustart = true;
				continue;
			}
			
			if (docustart == false) {
				if ("".equals(myLine.trim())) {
					continue;
				}else {
					System.out.println("Error while parsing input file");
					writer.write("Error while parsing input file");
					writer.close();
					System.exit(1);
				}
			}

			//Outputs a parse error if no name is given in the .txt file.
			if (name_flag) {
				if ("".equals(myLine.trim())){
					continue;
				}
				if("".equals(myLine.trim()) != true) {
					if(myLine.contains("forced partial assignment:") || containsWhiteSpace(myLine)) {
						System.out.println("Error while parsing input file");
						writer.write("Error while parsing input file");
						writer.close();
						System.exit(1);
					}
				name_flag = false;
				continue;
				}
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
				
				if (myLine.contains("forbidden machine:")) {
					forced_flag = false;
					forb_flag = true;
					continue;
				}
			
				if("".equals(myLine.trim()) != true) {
					
					if(myLine.trim().length() != 5 || validLineFPA_FM(myLine.trim()) != true ) {
						System.out.println("invalid machine/task");
						writer.write("invalid machine/task");
						writer.close();
						System.exit(1);
					}
					if(i<=7) {		
					String str_format = myLine.toString().replace(",","").replace("(","").replace(")","").trim(); //removes , ( )
					String first = str_format.substring(0,1);
					String second = str_format.substring(1,2);

					//PARTIALL ASSIGNMENT ERROR FLAG
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
					//END PARTIAL ASSIGNMENT ERROR FLAG

					forcedpartial_array[i] = str_format;
					i++;	
					}else {
						System.out.println("Error while parsing input file");
						writer.write("Error while parsing input file");
						writer.close();
						System.exit(1);
					}
				}
				continue;
			}

//FORBIDDEN MACHINE
			if (forb_flag) {
				if (myLine.contains("too-near tasks:")) {
					forb_flag = false;
					near_flag = true;
					continue;
				}
				
				if(j < 56 && "".equals(myLine.trim()) != true) {
					
					if( myLine.trim().length() != 5 || validLineFPA_FM(myLine.trim()) != true ) {
						System.out.println("invalid machine/task");
						writer.write("invalid machine/task");
						writer.close();
						System.exit(1);
					}
					String str_format = myLine.toString().replace(",","").replace("(","").replace(")","").trim();		
					forbiddenmachine_array[j] = str_format;
					j++;
					}
				continue;
			}

//TOO NEAR TASK
			if (near_flag) {
				if (myLine.contains("machine penalties:")) {
					near_flag = false;
					mach_flag = true;
					continue;
				}
			
				if(k < 8 && "".equals(myLine.trim()) != true) {
					
					if( myLine.trim().length() != 5 || validLineTNT(myLine.trim()) != true ) {
						System.out.println("invalid machine/task");
						writer.write("invalid machine/task");
						writer.close();
						System.exit(1);
					}
					String str_format = myLine.toString().replace(",","").replace("(","").replace(")","").trim();
					neartask_array[k] = str_format;
					k++;
					continue;
					
				}else if ( "".equals(myLine.trim())){
					continue;
				}
			}			

//MACHINE PENALTIES
		    String array1[] = myLine.split(" "); //splits the string of numbers with whitespace " "		   
			//Assigns machine penalties, breaking if finding more than 8 lines of machine penalties.
			if (mach_flag) {
				if (myLine.contains("too-near penalities")) {
					mach_flag = false;
					toonear_flag = true;
					continue;
				}
				
				if(m_row < 8 && "".equals(myLine.trim()) != true) {		
			    String hold = new String();
			    if (array1.length != 8) { 						//Checks if the array length is less than 8. Breaks if so.
			    	System.out.println("machine penalty error");
			    	writer.write("machine penalty error");
			    	writer.close();
					System.exit(1);
			    }
			    for (int m_col = 0; m_col < array1.length; m_col++) {
			    	hold = array1[m_col];			    	

			    	//Checks if the number is an natural integer.
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
				}else if(m_row < 8 && "".equals(myLine.trim())== true) {
					continue;

				}else if (mach_flag && m_row == 8 && "".equals(myLine.trim())!= true) {
					System.out.println("machine penalty error");
					writer.write("machine penalty error");
					writer.close();
					System.exit(1);
				}
			}
			
//TOO NEAR PENALTIES

			if (toonear_flag) {
				if(p < 8 && "".equals(myLine.trim()) != true) {
					
					if(myLine.trim().length() != 7 || validLineTNP(myLine.trim()) != true ) {
						System.out.println("invalid task");
						writer.write("invalid task");
						writer.close();
						System.exit(1);
					}
					
					String str_format = myLine.toString().replace(",","").replace("(","").replace(")","").trim();
					
					toonearpenal_array[p] = str_format;
					p++;
					continue;

				}else if ( "".equals(myLine.trim())){
					toonear_flag = false;
					docuend = true;
					continue;
				}
			}
			
			if (docuend == true) {
				if ("".equals(myLine.trim())) {
					continue;
				}else {
					System.out.println("Error while parsing input file");
					writer.write("Error while parsing input file");
					writer.close();
					System.exit(1);
				}
			}
		}

			 System.out.println(Arrays.deepToString(forcedpartial_array));
			 System.out.println(Arrays.deepToString(forbiddenmachine_array));			
			 System.out.println(Arrays.deepToString(neartask_array));
			 System.out.println(Arrays.deepToString(mach_array));
			 System.out.println(Arrays.deepToString(toonearpenal_array));			
			bufRead.close();
			writer.close();
	}
	
	public boolean validLineFPA_FM(String st) {
		if("".equals(st.trim())){
			return true;
		}
		
		String first = st.substring(0,1);
		String second = st.substring(1,2);
		String third = st.substring(2,3);
		String fourth = st.substring(3,4);
		String fifth = st.substring(4,5);
		
		
		String valid_syn[] = {"("};
		String valid_num[] = {"1","2","3","4","5","6","7","8"};
		String valid_com[] = {","};
		String valid_char[] = {"A","B","C","D","E","F","G","H"};
		String valid_syn2[] = {")"};
		
		if (st.length() == 5 ) {
			boolean contains1 = Arrays.stream(valid_syn).anyMatch(first::equals);
			boolean contains2 = Arrays.stream(valid_num).anyMatch(second::equals);
			boolean contains3 = Arrays.stream(valid_com).anyMatch(third::equals);
			boolean contains4 = Arrays.stream(valid_char).anyMatch(fourth::equals);		
			boolean contains5 = Arrays.stream(valid_syn2).anyMatch(fifth::equals);			
			if (contains1 && contains2 && contains3 && contains4 && contains5) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;}
	}
	
	public boolean validLineTNT(String st) {
		
		if("".equals(st.trim())){
			return true;
		}
		
		String first = st.substring(0,1);
		String second = st.substring(1,2);
		String third = st.substring(2,3);
		String fourth = st.substring(3,4);
		String fifth = st.substring(4,5);
		
		String valid_syn[] = {"("};
		String valid_com[] = {","};
		String valid_char[] = {"A","B","C","D","E","F","G","H"};
		String valid_syn2[] = {")"};
		
		if (st.length() == 5 ) {
			boolean contains1 = Arrays.stream(valid_syn).anyMatch(first::equals);
			boolean contains2 = Arrays.stream(valid_char).anyMatch(second::equals);
			boolean contains3 = Arrays.stream(valid_com).anyMatch(third::equals);
			boolean contains4 = Arrays.stream(valid_char).anyMatch(fourth::equals);		
			boolean contains5 = Arrays.stream(valid_syn2).anyMatch(fifth::equals);			
			if (contains1 && contains2 && contains3 && contains4 && contains5) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;}
	}
	
	public boolean validLineTNP(String st) {
		
		if("".equals(st.trim())){
			return true;
		}
		
		String first = st.substring(0,1);
		String second = st.substring(1,2);
		String third = st.substring(2,3);
		String fourth = st.substring(3,4);
		String fifth = st.substring(4,5);
		String sixth = st.substring(5,6);
		String seventh = st.substring(6,7);
		
		String valid_syn[] = {"("};
		String valid_com[] = {","};
		String valid_char[] = {"A","B","C","D","E","F","G","H"};
		String valid_syn2[] = {")"};
		
		if (st.length() == 7 ) {
			boolean contains1 = Arrays.stream(valid_syn).anyMatch(first::equals);
			boolean contains2 = Arrays.stream(valid_char).anyMatch(second::equals);
			boolean contains3 = Arrays.stream(valid_com).anyMatch(third::equals);
			boolean contains4 = Arrays.stream(valid_char).anyMatch(fourth::equals);		
			boolean contains5 = Arrays.stream(valid_com).anyMatch(fifth::equals);
			boolean contains6 = isInteger(sixth);
			boolean contains7 = Arrays.stream(valid_syn2).anyMatch(seventh::equals);
			
			if (contains1 && contains2 && contains3 && contains4 && contains5 && contains6 && contains7) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;}
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
	            		System.out.println("invalid penalty");
	            		writer2.write("invalid penalty");
	            		writer2.close();
						System.exit(1);
	            	}else 
	            		continue;
	        	}

	        	if(Character.digit(s.charAt(i),radix) < 0) {
            		System.out.println("invalid penalty");
	        		writer2.write("invalid penalty");
	        		writer2.close();
					System.exit(1);
	        	}
	    	}
	    }catch (Exception e){System.exit(1);}
	    return true;
	}
	
	public static boolean containsWhiteSpace(final String st){
	     for(int i = 0; i < st.length(); i++){
	         if(Character.isWhitespace(st.charAt(i))){
	             return true;
	         }
	     }
	    return false;
	}


	// Example utilization of class 
	public static void main(String[] args) {
	
		// Create Parse object with desired input file & output file locations
		Parse parser = new Parse("/Users/favian.silva/eclipse-workspace/A1/src/test.txt", 
										"/Users/favian.silva/eclipse-workspace/A1/src/myoutput.txt");

		// Parse through each line of the input file and quit upon proccessing errors. Must put Parse opbject in try catch block as
		// methodn throws IOException.
		try {
			parser.parse();
		} catch (Exception e) {e.printStackTrace(System.out);}

		// To access arrays, use parser.forcedpartial_array, parser.forbiddenmachine_array, etc..
		// All these instance variables are listed at the top of the file.

	}



}