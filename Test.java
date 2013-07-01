package foo;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Test {

	public static double isBoss = 0; 
	public static double isntBoss = 0;
	public static double getBoss(){
		return isBoss;
	}
	public static double getnotBoss(){
		return isntBoss;
	}
	public static void main(String[] args) throws IOException {
		Runtime.getRuntime()
				.exec("C:/Users/Ethan/Documents/OtherKinectStuff/TestStuff/bin/Release/JNECTSUCKS.exe");
		Bayes_net test = new Bayes_net();
		double[][] CPTMid = { { .7, .2, .1 }, { .1, .2, .7 } };
		double[][] CPTArms = { { .5, .3, .2 }, { .2, .3, .5 } };
		double[][] CPTLegs = { { .6, .25, .15 }, { .15, .25, .6 } };
		double[] initial = { 1 / 2.0, 1 / 2.0 };
		double[][]other = {{.8,.2},{.2,.8}};
		double[][]other1 = {{.7,.3},{.3,.7}};
		test.add_vertex("Bowen", 2, initial);
		test.add_vertex("Height", 2);
		test.add_vertex("BodyType", 2);
		test.add_vertex("Shoulder", 3);
		test.add_vertex("Hip", 3);
		test.add_vertex("LeftArm", 3);
		test.add_vertex("RightArm", 3);
		test.add_vertex("LeftThigh", 3);
		test.add_vertex("RightThigh", 3);
		test.add_vertex("Neck", 3);
		test.add_vertex("LeftTorso", 3);
		test.add_vertex("CenterTorso", 3);
		test.add_vertex("RightTorso", 3);		
		test.add_edge("Bowen", 2, "BodyType", 2, other);
		test.add_edge("Bowen", 2, "Height", 2, other1);
		test.add_edge("BodyType", 2, "Shoulder", 3, CPTMid);
		test.add_edge("BodyType", 2, "Hip", 3, CPTMid);
		test.add_edge("Height", 2, "LeftArm", 3, CPTArms);
		test.add_edge("Height", 2, "RightArm", 3, CPTArms);
		test.add_edge("Height", 2, "LeftThigh", 3, CPTLegs);
		test.add_edge("Height", 2, "RightThigh", 3, CPTLegs);
		test.add_edge("Height", 2, "Neck", 3, CPTMid);
		test.add_edge("Height", 2, "LeftTorso", 3, CPTMid);
		test.add_edge("Height", 2, "RightTorso", 3, CPTMid);
		test.add_edge("Height", 2, "CenterTorso", 3, CPTMid);
		JoinTree sample = test.convertToJoinTree();
		sample.makeConsistent();
		Vertex<String> Hip = new Vertex<String>("Hip", 3);
		Vertex<String> Shoulder = new Vertex<String>("Shoulder", 3);
		Vertex<String> LeftArm = new Vertex<String>("LeftArm", 3);
		Vertex<String> RightArm = new Vertex<String>("RightArm", 3);
		Vertex<String> LeftThigh = new Vertex<String>("LeftThigh", 3);
		Vertex<String> RightThigh = new Vertex<String>("RightThigh", 3);
		Vertex<String> Neck = new Vertex<String>("Neck", 3);
		Vertex<String> LeftTorso = new Vertex<String>("LeftTorso", 3);
		Vertex<String> CenterTorso = new Vertex<String>("CenterTorso", 3);
		Vertex<String> RightTorso = new Vertex<String>("RightTorso", 3);
		double[] realValues = null, newValues = null;
		for(int m = 0; m < 4; m++){
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
				System.out.print(line + ",");
			}
			System.out.println();
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
					while (scan.hasNext() & i <  10) {
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
		int[] evidence = new int[10];
		for (int i = 0; i < evidence.length; i++) {
			if (Math.abs(realValues[i] - newValues[i]) < 0.008)
				if (Math.abs(realValues[i] - newValues[i]) < 0.004)
					evidence[i] = 0;
				else
					evidence[i] = 1;
			else
				evidence[i] = 2;
		}
		sample.enter_evidence(Hip, evidence[3]);
		sample.enter_evidence(Shoulder, evidence[2]);
		sample.enter_evidence(LeftArm, evidence[0]);
		sample.enter_evidence(RightArm, evidence[1]);
		sample.enter_evidence(LeftThigh, evidence[5]);
		sample.enter_evidence(RightThigh, evidence[6]);
		sample.enter_evidence(Neck, evidence[4]);
		sample.enter_evidence(LeftTorso, evidence[7]);
		sample.enter_evidence(RightTorso, evidence[8]);
		sample.enter_evidence(CenterTorso, evidence[9]);
		Vertex<String> Bowen = new Vertex<String>("Bowen", 2);
		double[] result = sample.find_probability(Bowen);
		File file1 = new File("CURRENTONE.txt");
		file1.delete();
		isBoss = result[0];
		isntBoss = result[1];
		File file = new File("working.txt");
		Scanner scan = new Scanner(file);
		scan.useDelimiter(",");
		String line = null;
		newValues = new double[2];
		int i = 0;
		while (scan.hasNext() && i < 2) {
			line = scan.next();
			newValues[i++] = Double.parseDouble(line);
		}
		scan.close();
		double isworking = newValues[0];
		double isnotworking = newValues[1];
		newValues = null;
		System.out.println(result[0] + " " + result[1]+" "+ isworking+" "+isnotworking);
		Test.isBoss = 0;
		Test.isntBoss = 0;
		if (isworking != 0)
			expectedutility.determineaction(result[0], result[1], isworking,
					isnotworking);
		if (expectedutility.action){
			System.setErr(null);
			App ex = new App("http://stackoverflow.com/questions/17266383/check-if-the-user-name-is-entered-in-edittext-already-exists-in-the-sql-database");
		}
	}
	}

}
