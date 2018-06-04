package meas_data_logger_v10;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class file_saver {

public static int[] input_array = new int[kommunikacio.MAXIMUM_DATA_IDX];

public file_saver (){

}

public static void set_file_name(){
	
}

public static void save_file(){
	
	StringBuilder sb = new StringBuilder();
	sb.append("adattipus:"+kommunikacio.komm_data_code);
	sb.append(";");
	for (int element : input_array) {
		 sb.append(Integer.toString(element));
		 sb.append(";");
	}
	
	BufferedWriter br;
	try {
		br = new BufferedWriter(new FileWriter("myfile.csv",true));
		br.append(sb.toString());
		br.append("\n");
		br.close();	
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
	    // your code


} //save_file
	
} //class
	

