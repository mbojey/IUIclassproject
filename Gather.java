package foo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Gather {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Runtime.getRuntime()
		.exec("C:/Users/Ethan/Documents/OtherKinectStuff/TestStuff/bin/Release/JNECTSUCKS.exe");
		for (int m = 0; m < 25; m++) {
			double[] realValues = null, newValues = null;
			try {
				realValues = new double[10];
				int i = 0;
				File file = new File("REALONE.txt");
				Scanner scan = new Scanner(file);
				scan.useDelimiter(",");
				String line = null;
				while (scan.hasNext() && i < 10) {
					line = scan.next();
					realValues[i++] = Double.parseDouble(line);
					//System.out.print(line + ",");
				}
				//System.out.println();
				scan.close();
			} catch (FileNotFoundException e) {
				System.out.println("Didn't Work");
			}
			while (newValues == null) {
				if (System.nanoTime() % 10 == 0) {
					try {
						int i = 0;
						File file = new File("CURRENTONE.txt");
						file.deleteOnExit();
						Scanner scan = new Scanner(file);
						scan.useDelimiter(",");
						String line = null;
						newValues = new double[10];
						while (scan.hasNext() & i < 10) {
							line = scan.next();
							newValues[i++] = Double.parseDouble(line);
							System.out.print(line + ",");
						}
						System.out.println();
						scan.close();
						file.delete();
					} catch (FileNotFoundException e) {
						// System.out.println("Didn't Work");
					}
				}
			}
		}
	}

}
