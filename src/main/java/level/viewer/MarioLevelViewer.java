package level.viewer;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import engine.level.Level;
import engine.level.LevelParser;
import level.evaluation.MarioEvalFunction;
import level.tasks.ProgressTask;
import mario.engine.LevelRenderer;
import resource.tools.CmdLineOptions;
import resource.tools.EvaluationOptions;
import utility.JsonReader;
import utility.Settings;

public class MarioLevelViewer {

	public static final int BLOCK_SIZE = 16;
	public static final int LEVEL_HEIGHT = 14;

	public static BufferedImage getLevelImage(Level level, boolean excludeBufferRegion) {
		EvaluationOptions options = new CmdLineOptions(new String[0]);
		ProgressTask task = new ProgressTask(options);
		options.setLevel(level);
		task.setOptions(options);
		int relevantWidth = (level.width - (excludeBufferRegion ? 2 * LevelParser.BUFFER_WIDTH : 0)) * BLOCK_SIZE;
		BufferedImage image = new BufferedImage(relevantWidth, LEVEL_HEIGHT * BLOCK_SIZE, BufferedImage.TYPE_INT_RGB);
		LevelRenderer.renderArea((Graphics2D) image.getGraphics(), level, 0, 0,
				excludeBufferRegion ? LevelParser.BUFFER_WIDTH * BLOCK_SIZE : 0, 0, relevantWidth,
				LEVEL_HEIGHT * BLOCK_SIZE);
		return image;
	}

	public static void saveLevel(Level level, String name, boolean clipBuffer) throws IOException {
		BufferedImage image = getLevelImage(level, clipBuffer);
		File file = new File(name + ".jpg");
		ImageIO.write(image, "jpg", file);
		System.out.println("File saved: " + file);
	}

	public static void main(String[] args) throws IOException {
		Settings.setPythonProgram();
		MarioEvalFunction eval = new MarioEvalFunction();
		Level level;
		String strLatentVector = "";
		if (args.length > 0) {
			StringBuilder builder = new StringBuilder();
			for (String str : args) {
				builder.append(str);
			}
			strLatentVector = builder.toString();
			Settings.printInfoMsg("Passed vector(s): " + strLatentVector);
			if (strLatentVector.subSequence(0, 2).equals("[[")) {
				strLatentVector = strLatentVector.substring(1, strLatentVector.length() - 1);
				String levels = "";
				while (strLatentVector.length() > 0) {
					int end = strLatentVector.indexOf("]") + 1;
					String oneVector = strLatentVector.substring(0, end);
					System.out.println("ONE VECTOR: " + oneVector);
					levels += eval.stringToFromGAN(oneVector); // Use the GAN
					strLatentVector = strLatentVector.substring(end); // discard processed vector
					if (strLatentVector.length() > 0) {
						levels += ",";
						strLatentVector = strLatentVector.substring(1); // discard leading comma
					}
				}
				levels = "[" + levels + "]"; // Put back in brackets
				System.out.println(levels);
				List<List<List<Integer>>> allLevels = JsonReader.JsonToInt(levels);
				ArrayList<List<Integer>> oneLevel = new ArrayList<List<Integer>>();
				for (List<Integer> row : allLevels.get(0)) { // Look at first level (assume all are same size)
					System.out.println(row);
					oneLevel.add(new ArrayList<Integer>()); // Empty row
				}
				for (List<List<Integer>> aLevel : allLevels) {
					int index = 0;
					for (List<Integer> row : aLevel) { // Loot at each row
						oneLevel.get(index++).addAll(row);
					}
				}
				level = LevelParser.createLevelJson(oneLevel);
			} else { // Otherwise, there must be a single latent vector, and thus a single level
				double[] latentVector = JsonReader.JsonToDoubleArray(strLatentVector);
				level = eval.levelFromLatentVector(latentVector);
			}
		} else {
			System.out.println("Generating level with default vector");
			level = eval.levelFromLatentVector(new double[] { 0.9881835842209917, -0.9986077315374948,
					0.9995512051242508, 0.9998643432807639, -0.9976165917284504, -0.9995247114230822,
					-0.9997001909358728, 0.9995694511739592, -0.9431036754879115, 0.9998155541290887,
					0.9997863689962382, -0.8761392912669269, -0.999843833016589, 0.9993230720045649, 0.9995470247917402,
					-0.9998847606084427, -0.9998322053148382, 0.9997707200294411, -0.9998905141832997,
					-0.9999512510490688, -0.9533512808031753, 0.9997703088007039, -0.9992229823819915,
					0.9953917828622341, 0.9973473366437476, 0.9943030781608361, 0.9995290290713732, -0.9994945079679955,
					0.9997109900652238, -0.9988379572928884, 0.9995070647543864, 0.9994132207570211 });
		}

		saveLevel(level, "LevelClipped", true);
		saveLevel(level, "LevelFull", false);
		eval.exit();
		System.exit(0);
	}

}
