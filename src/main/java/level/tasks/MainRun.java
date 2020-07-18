package level.tasks;

import level.simulator.SimulationOptions;
import mario.agents.AStarAgent;
import mario.agents.Agent;
import mario.agents.AgentsPool;
import mario.agents.TimingAgent;
import resource.tools.CmdLineOptions;
import resource.tools.EvaluationInfo;
import resource.tools.EvaluationOptions;
import resource.tools.Evaluator;
import utility.JsonReader;
import utility.StatisticalSummary;

public class MainRun {

	final static int numberOfTrials = 1;
	final static boolean scoring = true;
	final static boolean quick = true;
	private static int killsSum = 0;
	private static int marioStatusSum = 0;
	private static int timeLeftSum = 0;
	private static int marioModeSum = 0;
	private static boolean detailedStats = true;

	public static void main(String[] args) {
		CmdLineOptions cmdLineOptions = new CmdLineOptions(args);
		cmdLineOptions.setTimeLimit(10);
		EvaluationOptions evaluationOptions = cmdLineOptions; // if none options mentioned, all defalults are used.
		createAgentsPool();

		if (scoring)
			scoreAllAgents(cmdLineOptions, quick);
		else {
			Evaluator evaluator = new Evaluator(evaluationOptions);
			evaluationOptions.setAgent(AgentsPool.getCurrentAgent());
			while (cmdLineOptions.getNumberOfTrials() >= SimulationOptions.currentTrial) {
				System.out.println("SimulationOptions.currentTrial = " + SimulationOptions.currentTrial);
				evaluator.evaluate();
			}
		}

		if (cmdLineOptions.isExitProgramWhenFinished())
			System.exit(0);
	}

	private static boolean calledBefore = false;

	public static void createAgentsPool() {
		if (!calledBefore) {
			calledBefore = true;
			AgentsPool.addAgent(new AStarAgent());
		}
	}

	public static void scoreAllAgents(CmdLineOptions cmdLineOptions, boolean quick) {
		int startingSeed = cmdLineOptions.getLevelRandSeed();
		for (Agent agent : AgentsPool.getAgentsCollection()) {
			if (quick) {
				quickScore(agent, cmdLineOptions);
			} else {
				score(agent, startingSeed, cmdLineOptions);
			}
		}
	}

	public static void quickScore(Agent agent, CmdLineOptions cmdLineOptions) {
		TimingAgent controller = new TimingAgent(agent);
		EvaluationOptions options = cmdLineOptions;
		options.setNumberOfTrials(1);
		double competitionScore = 0;
		competitionScore += testConfig(controller, options);
		System.out.println(competitionScore);
	}

	public static void score(Agent agent, int startingSeed, CmdLineOptions cmdLineOptions) {
		TimingAgent controller = new TimingAgent(agent);
		EvaluationOptions options = cmdLineOptions;
		options.setNumberOfTrials(1);
		System.out.println("\nScoring controller " + agent.getName() + " with starting seed " + startingSeed);
		double competitionScore = 0;
		killsSum = 0;
		marioStatusSum = 0;
		timeLeftSum = 0;
		marioModeSum = 0;
		competitionScore += testConfig(controller, options, startingSeed, 3, false);
		competitionScore += testConfig(controller, options, startingSeed, 5, false);
		competitionScore += testConfig(controller, options, startingSeed, 10, false);
		System.out.println("\nCompetition score: " + competitionScore + "\n");
		System.out.println("Number of levels cleared = " + marioStatusSum);
		System.out.println("Additional (tie-breaker) info: ");
		System.out.println("Total time left = " + timeLeftSum);
		System.out.println("Total kills = " + killsSum);
		System.out.println("Mario mode (small, large, fire) sum = " + marioModeSum);
		System.out.println("TOTAL SUM for " + agent.getName() + " = "
				+ (competitionScore + killsSum + marioStatusSum + marioModeSum + timeLeftSum));
	}

	public static void metrics(Agent agent, int startingSeed, CmdLineOptions cmdLineOptions) {
		TimingAgent controller = new TimingAgent(agent);
		EvaluationOptions options = cmdLineOptions;
		options.setNumberOfTrials(1);
		System.out.println("\nScoring controller " + agent.getName() + " with starting seed " + startingSeed);
		double competitionScore = 0;
		killsSum = 0;
		marioStatusSum = 0;
		timeLeftSum = 0;
		marioModeSum = 0;
		competitionScore += testConfig(controller, options, startingSeed, 3, false);
		competitionScore += testConfig(controller, options, startingSeed, 5, false);
		competitionScore += testConfig(controller, options, startingSeed, 10, false);
		System.out.println("\nCompetition score: " + competitionScore + "\n");
		System.out.println("Number of levels cleared = " + marioStatusSum);
		System.out.println("Additional (tie-breaker) info: ");
		System.out.println("Total time left = " + timeLeftSum);
		System.out.println("Total kills = " + killsSum);
		System.out.println("Mario mode (small, large, fire) sum = " + marioModeSum);
		System.out.println("TOTAL SUM for " + agent.getName() + " = "
				+ (competitionScore + killsSum + marioStatusSum + marioModeSum + timeLeftSum));
	}

	public static double testConfig(TimingAgent controller, EvaluationOptions options) {
		double distanceCovered = 0;
		options.setNumberOfTrials(numberOfTrials);
		options.resetCurrentTrial();
		JsonReader reader = new JsonReader(options.getLevelFile());
		for (int counter = 0; counter < reader.getNumber(); counter++) {
			options.setLevelIndex(counter);
			for (int i = 0; i < numberOfTrials; i++) {
				controller.reset();
				options.setAgent(controller);
				Evaluator evaluator = new Evaluator(options);
				EvaluationInfo result = evaluator.evaluate().get(0);
				distanceCovered += result.computeDistancePassed();
			}
		}
		return distanceCovered;
	}

	public static double testConfig(TimingAgent controller, EvaluationOptions options, int seed, int levelDifficulty,
			boolean paused) {
		options.setLevelDifficulty(levelDifficulty);
		options.setPauseWorld(paused);
		StatisticalSummary ss = test(controller, options, seed);
		double averageTimeTaken = controller.averageTimeTaken();
		System.out.printf("Difficulty %d score %.4f (avg time %.4f)\n", levelDifficulty, ss.mean(), averageTimeTaken);
		return ss.mean();
	}

	public static StatisticalSummary test(Agent controller, EvaluationOptions options, int seed) {
		StatisticalSummary ss = new StatisticalSummary();
		int kills = 0;
		int timeLeft = 0;
		int marioMode = 0;
		int marioStatus = 0;

		options.setNumberOfTrials(numberOfTrials);
		options.resetCurrentTrial();
		for (int i = 0; i < numberOfTrials; i++) {
			options.setLevelRandSeed(seed + i);
			options.setLevelLength(200 + (i * 128) + (seed % (i + 1)));
			options.setLevelType(i % 3);
			controller.reset();
			options.setAgent(controller);
			Evaluator evaluator = new Evaluator(options);
			EvaluationInfo result = evaluator.evaluate().get(0);
			kills += result.computeKillsTotal();
			timeLeft += result.timeLeft;
			marioMode += result.marioMode;
			marioStatus += result.marioStatus;
			ss.add(result.computeDistancePassed());
		}

		if (detailedStats) {
			System.out.println(
					"\n===================\nStatistics over " + numberOfTrials + " trials for " + controller.getName());
			System.out.println("Total kills = " + kills);
			System.out.println("marioStatus = " + marioStatus);
			System.out.println("timeLeft = " + timeLeft);
			System.out.println("marioMode = " + marioMode);
			System.out.println("===================\n");
		}

		killsSum += kills;
		marioStatusSum += marioStatus;
		timeLeftSum += timeLeft;
		marioModeSum += marioMode;

		return ss;
	}
}
