package level.evaluation;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import engine.level.Level;
import engine.level.LevelParser;
import mario.communication.GANProcess;
import mario.communication.MarioProcess;
import mario.optimisation.IObjectiveFunction;
import resource.tools.EvaluationInfo;
import utility.JsonReader;

public class MarioEvalFunction implements IObjectiveFunction {

	public static final int LEVEL_LENGTH = 704;
	private GANProcess ganProcess;
	private MarioProcess marioProcess;
	static double floor = 0.0;

	public MarioEvalFunction() throws IOException {
		ganProcess = new GANProcess();
		ganProcess.start();
		marioProcess = new MarioProcess();
		marioProcess.start();
		String response = "";
		while (!response.equals("READY")) {
			response = ganProcess.commRecv();
		}
	}

	public MarioEvalFunction(String GANPath, String GANDim) throws IOException {
		ganProcess = new GANProcess(GANPath, GANDim);
		ganProcess.start();
		marioProcess = new MarioProcess();
		marioProcess.start();
		String response = "";
		while (!response.equals("READY")) {
			response = ganProcess.commRecv();
		}
	}

	public static Level[] marioLevelsFromJson(String json) {
		List<List<List<Integer>>> allLevels = JsonReader.JsonToInt(json);
		Level[] result = new Level[allLevels.size()];
		int index = 0;
		for (List<List<Integer>> listRepresentation : allLevels) {
			result[index++] = LevelParser.createLevelJson(listRepresentation);
		}
		return result;
	}

	public void exit() throws IOException {
		ganProcess.commSend("0");
	}

	public Level levelFromLatentVector(double[] x) throws IOException {
		x = mapArrayToOne(x);
		ganProcess.commSend("[" + Arrays.toString(x) + "]");
		String levelString = ganProcess.commRecv(); // Response to command just sent
		Level[] levels = marioLevelsFromJson("[" + levelString + "]"); // Really only one level in this array
		Level level = levels[0];
		return level;
	}

	public String stringToFromGAN(String input) throws IOException {
		double[] x = JsonReader.JsonToDoubleArray(input);
		x = mapArrayToOne(x);
		ganProcess.commSend(Arrays.toString(x));
		String levelString = ganProcess.commRecv(); // Response to command just sent
		return levelString;
	}

	@Override
	public double valueOf(double[] x) {
		try {
			Level level = levelFromLatentVector(x);
			EvaluationInfo info = this.marioProcess.simulateOneLevel(level);
			if (info.computeDistancePassed() < LEVEL_LENGTH) { // Did not beat level
				return (double) -info.computeDistancePassed() / LEVEL_LENGTH;// +20;
			} else { // Did beat level
				return (double) -info.computeDistancePassed() / LEVEL_LENGTH - info.jumpActionsPerformed;
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
			return Double.NaN;
		}
	}

	@Override
	public boolean isFeasible(double[] x) {
		return true;
	}

	public static double mapToOne(double valueInR) {
		return (valueInR / Math.sqrt(1 + valueInR * valueInR));
	}

	public static double[] mapArrayToOne(double[] arrayInR) {
		double[] newArray = new double[arrayInR.length];
		for (int i = 0; i < newArray.length; i++) {
			double valueInR = arrayInR[i];
			newArray[i] = mapToOne(valueInR);
		}
		return newArray;
	}

}
