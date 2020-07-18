package level.simulator;

import mario.agents.Agent;
import mario.engine.GlobalOptions;
import mario.engine.MarioComponent;
import resource.tools.EvaluationInfo;

public class BasicSimulator implements Simulation {

	SimulationOptions simulationOptions = null;
	private MarioComponent marioComponent;

	public BasicSimulator(SimulationOptions simulationOptions) {
		GlobalOptions.VisualizationOn = simulationOptions.isVisualization();
		this.marioComponent = GlobalOptions.getMarioComponent();
		this.setSimulationOptions(simulationOptions);
	}

	private MarioComponent prepareMarioComponent() {
		Agent agent = simulationOptions.getAgent();
		agent.reset();
		marioComponent.setAgent(agent);
		return marioComponent;
	}

	public void setSimulationOptions(SimulationOptions simulationOptions) {
		this.simulationOptions = simulationOptions;
	}

	public EvaluationInfo simulateOneLevel() {
		prepareMarioComponent();
		marioComponent.setZLevelScene(simulationOptions.getZLevelMap());
		marioComponent.setZLevelEnemies(simulationOptions.getZLevelEnemies());
		if (simulationOptions.getLevel() != null) { // Added by us: means a Level instance was directly bundled in the
			marioComponent.startLevel(simulationOptions.getLevelRandSeed(), simulationOptions.getLevelDifficulty(),
					simulationOptions.getLevelType(), simulationOptions.getLevelLength(),
					simulationOptions.getTimeLimit(), simulationOptions.getLevel());
		} else if (simulationOptions.getLevelFile().equals("null")) { // The original default behavior: Randomly
			marioComponent.startLevel(simulationOptions.getLevelRandSeed(), simulationOptions.getLevelDifficulty(),
					simulationOptions.getLevelType(), simulationOptions.getLevelLength(),
					simulationOptions.getTimeLimit());
		} else { // Added by us: A json file containing levels has been included in the
			marioComponent.startLevel(simulationOptions.getLevelRandSeed(), simulationOptions.getLevelDifficulty(),
					simulationOptions.getLevelType(), simulationOptions.getLevelLength(),
					simulationOptions.getTimeLimit(), simulationOptions.getLevelFile(),
					simulationOptions.getLevelIndex());
		}
		marioComponent.setPaused(simulationOptions.isPauseWorld());
		marioComponent.setZLevelEnemies(simulationOptions.getZLevelEnemies());
		marioComponent.setZLevelScene(simulationOptions.getZLevelMap());
		return marioComponent.run1(SimulationOptions.currentTrial++, simulationOptions.getNumberOfTrials());
	}
}
