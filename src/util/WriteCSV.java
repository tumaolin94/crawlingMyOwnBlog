package util;
/**
 * @author Maolin Tu
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class WriteCSV {
	public static void write(String content1, String content2, String fileName) {
		try {
			String[] strs = new String[2];
			strs[0] = content1;
			strs[1] = content2;
			write(strs,fileName);
		}catch(Exception e) {
			
		}

		
		
	}
	public static void write(String[] contents, String fileName) {
		try {
			File csv = new File(fileName+".csv"); // CSV Path

			BufferedWriter bw = new BufferedWriter(new FileWriter(csv, true)); // create

			for(String content: contents) {
				bw.write(content+","); //write
			}
			bw.newLine();// create new line
			bw.close();

		} catch (FileNotFoundException e) {
			// Exception while creating File Objects
			e.printStackTrace();
		} catch (IOException e) {
			// Exception while closing File Objects
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		
	}
}
