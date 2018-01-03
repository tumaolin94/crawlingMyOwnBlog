package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class WriteCSV {
	public static void write(String[] content) {
		try {
			File csv = new File("writers.csv"); // CSV Path

			BufferedWriter bw = new BufferedWriter(new FileWriter(csv, true)); // create
			for (int i = 0; i < 10; i++) {
				bw.write("test" + i + "," + "201" + i + ","); //write
				bw.newLine();// create new line
			}

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