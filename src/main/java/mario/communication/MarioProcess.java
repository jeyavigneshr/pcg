package mario.communication;

import engine.level.Level;
import level.simulator.BasicSimulator;
import level.simulator.Simulation;
import mario.agents.AStarAgent;
import mario.agents.AgentsPool;
import mario.agents.HumanKeyboardAgent;
import resource.tools.CmdLineOptions;
import resource.tools.EvaluationInfo;
import resource.tools.EvaluationOptions;
import resource.tools.ToolsConfigurator;

public class MarioProcess extends Comm {

	private EvaluationOptions evaluationOptions;
	private Simulation simulator;

	public MarioProcess() {
		super();
		this.threadName = "MarioProcess";
	}

	/**
	 * Default mario launcher does not have any command line parameters
	 */
	public void launchMario() {
		String[] options = new String[] { "" };
		launchMario(options, false);
	}

	/**
	 * This version of launching Mario allows for several parameters
	 * 
	 * @param options     General command line options (currently not really used)
	 * @param humanPlayer Whether a human is playing rather than a bot
	 */
	public void launchMario(String[] options, boolean humanPlayer) {
		this.evaluationOptions = new CmdLineOptions(options); // if none options mentioned, all defaults are used.
		// set agents
		createAgentsPool(humanPlayer);
		// Short time for evolution, but more for human
		if (!humanPlayer)
			evaluationOptions.setTimeLimit(20);
		// TODO: Make these configurable from commandline?
		evaluationOptions.setMaxFPS(!humanPlayer); // Slow for human players, fast otherwise
		evaluationOptions.setVisualization(true); // Set true to watch evaluations
		// Create Mario Component
		ToolsConfigurator.CreateMarioComponentFrame(evaluationOptions);
		evaluationOptions.setAgent(AgentsPool.getCurrentAgent());
		System.out.println(evaluationOptions.getAgent().getClass().getName());
		// set simulator
		this.simulator = new BasicSimulator(evaluationOptions.getSimulationOptionsCopy());
	}

	/**
	 * Set the agent that is evaluated in the evolved levels
	 */
	public static void createAgentsPool(boolean humanPlayer) {
		// Could still generalize this more
		if (humanPlayer) {
			AgentsPool.setCurrentAgent(new HumanKeyboardAgent());
		} else {
			AgentsPool.setCurrentAgent(new AStarAgent());
		}
	}

	public void setLevel(Level level) {
		evaluationOptions.setLevel(level);
		this.simulator.setSimulationOptions(evaluationOptions);
	}

	/**
	 * Simulate a given level
	 * 
	 * @return
	 */
	public EvaluationInfo simulateOneLevel(Level level) {
		setLevel(level);
		EvaluationInfo info = this.simulator.simulateOneLevel();
		return info;
	}

	public EvaluationInfo simulateOneLevel() {
		evaluationOptions.setLevelFile("sample_1.json");
		EvaluationInfo info = this.simulator.simulateOneLevel();
		return info;
	}

	@Override
	public void start() {
		this.launchMario();
	}

	@Override
	public void initBuffers() {

	}

}
