package foo;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class DataCollection {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		Runtime.getRuntime()
				.exec("C:/Users/Ethan/Documents/OtherKinectStuff/TestStuff/bin/Release/CAPTURE.exe");
		double[][] realValues = new double[5][12];
		int counter = 0;
		while (counter < 5) {
			if (System.nanoTime() % 10000 == 0) {
				try {
					File file = new File("REALONE.txt");
					Scanner scan = new Scanner(file);
					scan.useDelimiter(",");
					String line = null;
					realValues[counter] = new double[12];
					int j = 0;
					while (scan.hasNext()) {
						line = scan.next();
						realValues[counter][j++] = Double.parseDouble(line);
						System.out.print(line + ",");
					}
					System.out.println();
					scan.close();
					file.delete();
					counter++;
				} catch (FileNotFoundException e) {
					// System.out.println("Didn't Work");
				}
			}
		}
		double[] result = new double[12];
		for (int i = 0; i < realValues[0].length; i++) {
			for (int j = 0; j < realValues.length; j++)
				result[i] += realValues[j][i];
		}
		for (int i = 0; i < result.length; i++)
			result[i] = (result[i] / 5);
		BufferedWriter out = new BufferedWriter(new FileWriter("REALONE.txt"));
		for (int i = 0; i < 12; i++)
			out.write(result[i] + ",");
		out.close();
	}

}
