import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import java.nio.file.Path;

/**
 * CPSC 449
 * @author Parker Siroishka
 * UCID: 30024936
 * 
 * Input file format:
 * Name:
 * name
 * 
 * forced partial assignment:  //List<String>
 * 
 * forbidden machine:  //List<String>
 * 
 * too-near tasks:  //List<String>
 * 
 * machine penalties:  //int[8][8]
 * 
 * too-near penalties:  //List<String>
 *
 */
		
public class HeaderCheck{
	
	Charset charset = Charset.forName("UTF-8");
	List<String> inputFile;
	Path filePath;
	String[] blockHeaders = new String[] {"Name:", "forced partial assignment:", "forbidden machine:",
											"too-near tasks:", "machine penalties:", "too-near penalities"};
	
	// Constructor for BlockParser. When creating BlockParser object, file path 
	// is set as parameter. Checks IO errors when creating file path object.
    public HeaderCheck(String filePathRequested) {
    	try{
    		filePath = Paths.get(filePathRequested);
    		inputFile = Files.readAllLines(filePath, charset);
    	} catch (IOException e) {}
    }
    
    /**
     * @param inputFile - List<String> object that represents file path and charset. 
     * @return String[] - String[] of all text in input file. Each index is a line of the .txt
     */
    public String[] inputFileToList(List<String> inputFile){
        String[] temp = new String[this.inputFile.size()];
        for (int i = 0; i < this.inputFile.size(); i++)
            temp[i] = inputFile.get(i).trim();
    	
        return temp;
    }
    
    /**
     * @param inputArr - String[] of input file. Each index is a line of the file
     * @return valid - If Block headers are in correct format return true, else false.
     */
    public boolean isValidFile(String[] inputArr) {
    	boolean valid = true;
    	int numOfHeaders = 0;

    	for(int i=0;i<this.blockHeaders.length;i++) {
    		for(int j=0;j<inputArr.length;j++ ) {
    			if(this.blockHeaders[i].equals(inputArr[j])) {
    				
    				numOfHeaders++;
    				// i++;
    				// j++;
    			}
    		}
    	}
    	
    	if(numOfHeaders != 6) {valid = false;} 
    	return valid;
    }
    
    
}






