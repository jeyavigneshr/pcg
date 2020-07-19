package parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import utility.Settings;

public class VGLCParser {

	static Map<Character, Integer> tiles = new HashMap<Character, Integer>();

	static {
		tiles.put('X', 0);
		tiles.put('S', 1);
		tiles.put('-', 2);
		tiles.put('?', 3);
		tiles.put('Q', 4);
		tiles.put('E', 5);
		tiles.put('<', 6);
		tiles.put('>', 7);
		tiles.put('[', 8);
		tiles.put(']', 9);
		tiles.put('o', 10);
		tiles.put('B', 11);
		tiles.put('b', 12);
	}

	static int targetWidth = 28;

	public static void main(String[] args) throws Exception {
		String dir = System.getProperty("user.dir");
		System.out.println("Working Directory = " + dir);
		String inputDirectory = dir + "/src/main/resources/levels/";
		String outputFile = dir + "/json_input/levels.json";
		String outputdir = dir + "/json_input/";

		ArrayList<int[][]> examples = new ArrayList<>();
		File file = new File(inputDirectory);
		String[] fileList = file.list();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		for (String inputFile : fileList) {
			try {
				System.out.println("Reading: " + inputFile);
				int[][] level = readLevel(new Scanner(new FileInputStream(inputDirectory + inputFile)));
				addData(examples, level);
				System.out.println(level);
				System.out.println("Read: " + inputFile);

				ArrayList<int[][]> examplesTmp = new ArrayList<>();
				addData(examplesTmp, level);
				String outTmp = gson.toJson(examplesTmp);
				System.out.println("Created JSON String");

				PrintWriter writerTmp = new PrintWriter(outputdir + "example" + inputFile + ".json");
				writerTmp.print(outTmp);
				writerTmp.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		String out = gson.toJson(examples);
		System.out.println("Created JSON String");

		PrintWriter writer = new PrintWriter(outputFile);
		writer.print(out);
		writer.close();
		System.out.println("Wrote file with " + examples.size() + " examples");
	}

	static void addData(ArrayList<int[][]> examples, int[][] level) {
		int h = level.length;

		for (int offset = 0; offset < level[0].length - 1 - targetWidth; offset++) {
			int[][] example = new int[h][targetWidth];
			for (int y = 0; y < h; y++) {
				for (int x = 0; x < targetWidth; x++) {
					example[y][x] = level[y][x + offset];
				}
			}
			examples.add(example);
		}
	}

	static int[] oneHot(int x) {
		int[] vec = new int[tiles.size()];
		vec[x] = 1;
		return vec;
	}

	static int[][] readLevel(Scanner scanner) throws Exception {
		String line;
		ArrayList<String> lines = new ArrayList<>();
		int width = 0;
		while (scanner.hasNext()) {
			line = scanner.nextLine();
			width = line.length();
			lines.add(line);
		}

		int[][] a = new int[lines.size()][width];
		System.out.println("Arrays length: " + a.length);
		for (int y = 0; y < lines.size(); y++) {
			System.out.println("Processing line: " + lines.get(y));
			for (int x = 0; x < width; x++) {
				try { // Added error checking to deal with unrecognized tile types
					a[y][x] = tiles.get(lines.get(y).charAt(x));
				} catch (Exception e) {
					System.out.println("Problem on ");
					System.out.println("\ty = " + y);
					System.out.println("\tx = " + x);
					System.out.println("\tlines.get(y).charAt(x) = " + lines.get(y).charAt(x));
					System.exit(1);
				}
			}
		}
		return a;
	}

	static String arrayToString(int[][] inputArray) {
		String outputStr = "";
		if (inputArray == null) {
			Settings.printWarnMsg("arrayToString: null argument passed.");
			return outputStr;
		}

		// Empty array
		int nbRows = inputArray.length;
		if (nbRows == 0) {
			Settings.printWarnMsg("arrayToString: input array is empty.");
			outputStr += "[]";
			return outputStr;
		}
		// Empty array
		int nbCols = inputArray[0].length;
		if (nbCols == 0) {
			Settings.printWarnMsg("arrayToString: input array is empty.");
			outputStr += "[";
			outputStr += "[]";
			for (int i = 1; i < nbRows; i++) {
				outputStr += ", []";
			}
			outputStr += "]";
			return outputStr;
		}

		outputStr += "["; // matrix starter
		int i = 0;
		outputStr += "["; // row starter
		outputStr += inputArray[i][0];
		for (int j = 1; j < nbCols - 1; j++) { // column
			outputStr += "," + inputArray[i][j];
		}
		outputStr += "]";
		for (i = 1; i < nbRows - 1; i++) { // loop rows
			outputStr += ", ["; // row starter
			outputStr += inputArray[i][0];
			for (int j = 1; j < nbCols - 1; j++) { // column
				outputStr += "," + inputArray[i][j];
			}
			outputStr += "]";
		}
		outputStr += "]";
		return outputStr;
	}
}
