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
		
		
		int use_ind = 0;
		int use_ind2 = 0;
		int i = 0;
		int j = 0;
		int k = 0;
		int m_row = 0;
		int p = 0;
		
		//Init String/Int arrays.
		String f_array[] = new String[8];
		String forb_array[] = new String[8];
		String near_array[] = new String[8];
		int mach_array[][] = new int[8][8];
		String toonear_array[] = new String[8];
		
		String used[] = new String[16];
		String used2[] = new String[16];


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
					
					
					String first = str_format.substring(0,1);
					String second = str_format.substring(1,2);
					//PARTIALL ASSIGNMENT FLAG
					boolean partial_assflag = Arrays.stream(used).anyMatch(first::equals);
					
					boolean partial_assflag2 = Arrays.stream(used).anyMatch(second::equals);//Checks array for any pairs that are the same. Breaks if so and outputs partial assignment error.
					if (partial_assflag || partial_assflag2) {
						System.out.println("partial assignment error");
						break;
					}
					
					used[use_ind] = first;
					use_ind++;
					used[use_ind] = second;
					use_ind++;
					//END PARTIAL ASSIGNMENT FLAG ERROR
					
					if(validChar(str_format)) { //Checks that it is a valid character A-H or 1-8. TODO fix error check. 
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
						f_array[i] = null;
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
					
					String first = str_format.substring(0,1);
					String second = str_format.substring(1,2);

					//PARTIALL ASSIGNMENT FLAG
					boolean forb_assflag = Arrays.stream(used2).anyMatch(first::equals);
					
					boolean forb_assflag2 = Arrays.stream(used2).anyMatch(second::equals);//Checks array for any pairs that are the same. Breaks if so and outputs partial assignment error.
					if (forb_assflag || forb_assflag2) {
						System.out.println("partial assignment error");
						break;
					}
					
					used2[use_ind2] = first;
					use_ind2++;
					used2[use_ind2] = second;
					use_ind2++;
					//END PARTIAL ASSIGNMENT FLAG ERROR
					
					if(validChar(str_format)) { //validChar(str_format.substring(0,1)) && validChar(str_format.substring(1))) attempted error check
						forb_array[j] = str_format;
						j++;
						continue;
					}else {
						System.out.println("invalid machine/task");
						break;
					}
					
				}else if ( "".equals(myLine.trim())){
					for (j = j; j < 8; j++){
						forb_array[j] = null;
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
						near_array[k] = null;
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
			    if (array1.length != 8) { 							//Checks if the array length is less than 8. Breaks if so.
			    	System.out.println("machine penalty error");
			    	break;
			    }
			    for (int m_col = 0; m_col < array1.length; m_col++) {
			    	hold = array1[m_col];
			    	
			    	//Checks if the integer is less than 0.
			    	if(isInteger(hold) != true) {
				    	System.out.println("machine penalty error");
				    	break;
			    	}
			    	mach_array[m_row][m_col] = Integer.parseInt(hold);		    	
			    }
			    m_row++;
			//checks if there is more than 8 lines for machine penaltys
			}else if(m_row == 8 && "".equals(myLine.trim())== true) {
				mach_flag = false;
				continue;
			}else if (mach_flag && m_row == 8 && "".equals(myLine.trim())!= true) {
				System.out.println("machine penalty error");
				break;
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
						near_array[p] = null;
					}
					toonear_flag = false;
					continue;
				}
			}


		}
		    System.out.println(Arrays.deepToString(mach_array));
			System.out.println(Arrays.deepToString(f_array));
			System.out.println(Arrays.deepToString(forb_array));
			System.out.println(Arrays.deepToString(near_array));
			System.out.println(Arrays.deepToString(toonear_array));
			bufRead.close();
		
			
			
	}
	public static boolean validChar(String ch) {
		
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
	
	public static boolean validChar2(String ch) {
		
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
	
	
	
	
	public static boolean isInteger(String s) {
	    return isInteger(s,10);
	}

	//The sub function that takes a string enumerates through and and checks if the value is a int in base 10.
	public static boolean isInteger(String s, int radix) {
	    if(s.isEmpty()) return false;
	    for(int i = 0; i < s.length(); i++) {
	        if(i == 0 && s.charAt(i) == '-') {
	            if(s.length() == 1) {
	            	System.out.println("Invalid penalty");
	            	return false;
	            }else 
	            	continue;
	        }
	        if(Character.digit(s.charAt(i),radix) < 0) {
            	System.out.println("Invalid penalty");
	        	return false;
	        }
	    }
	    return true;
	}

}