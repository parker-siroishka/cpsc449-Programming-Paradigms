import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Parse {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		FileReader input = new FileReader("/Users/favian.silva/eclipse-workspace/A1/src/test.txt");
		BufferedReader bufRead = new BufferedReader(input);
		String myLine = null;
		
		boolean name_flag = false;
		boolean forced_flag = false;
		int i = 0;
		String f_array[] = new String[8];


		while ( (myLine = bufRead.readLine()) != null)
		{    
			//Checks if there is a name in the text file.
			if (myLine.contains("Name:")) {
					name_flag = true;
					continue;
			}
			//Outputs a parse error if no name is given in text file.
			if (name_flag) {
				if("".equals(myLine.trim())) {
					System.out.println("Error while parsing input file");
					break;
				}
				name_flag = false;
			}
			
			//Appends forced partial assignments into a String array of 8
			if (myLine.contains("forced partial assignment:")) {
				forced_flag = true;
				continue;
			}
			//Outputs a parse error if no name is given in text file.
			if (forced_flag) {
				if(i < 8 && "".equals(myLine.trim()) != true) {
					String str_format = myLine.toString().replace(",","").replace("(","").replace(")","").trim();
					f_array[i] = str_format;
					i++;
					continue;
					
				}else if ( "".equals(myLine.trim())){
					for (i = i; i < 8; i++){
						f_array[i] = "null";
					}
					forced_flag = false;
					continue;
				}else if (i == 8) {
					forced_flag = false;
					continue;
					
				}
			}

			
		    String[] array1 = myLine.split(" ");
		    
		    
		    //TEST CASES
		    System.out.println(Arrays.deepToString(array1));
		    System.out.println(array1.length);
		    System.out.println(myLine);
			//System.out.println("Error while parsing input file");
		}
		//error_close:
			System.out.println(Arrays.deepToString(f_array));
			bufRead.close();
		
			
			
	}

}
