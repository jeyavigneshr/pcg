package level.simulator;

import engine.level.Level;
import mario.agents.Agent;
import mario.agents.AgentsPool;
//import ch.idsia.ai.agents.Agent;
//import ch.idsia.ai.agents.AgentsPool;
//import ch.idsia.mario.engine.level.Level;
//import ch.idsia.mario.simulation.SimulationOptions;
import utility.ParameterContainer;

public class SimulationOptions extends ParameterContainer {

	protected Agent agent;
	protected Level level = null; // default to null
	public static int currentTrial = 1;
	protected SimulationOptions() {
		super();
	}

	public SimulationOptions getSimulationOptionsCopy() {
		SimulationOptions ret = new SimulationOptions();
		ret.setAgent(getAgent());
		ret.setLevel(getLevel());
		ret.setLevelDifficulty(getLevelDifficulty());
		ret.setLevelLength(getLevelLength());
		ret.setLevelRandSeed(getLevelRandSeed());
		ret.setLevelType(getLevelType());
		ret.setLevelFile(getLevelFile());
		ret.setLevelIndex(getLevelIndex());
		ret.setVisualization(isVisualization());
		ret.setPauseWorld(isPauseWorld());
		ret.setPowerRestoration(isPowerRestoration());
		ret.setNumberOfTrials(getNumberOfTrials());
		ret.setMarioMode(getMarioMode());
		ret.setTimeLimit(getTimeLimit());
		ret.setZLevelEnemies(getZLevelEnemies());
		ret.setZLevelMap(getZLevelMap());
		ret.setMarioInvulnerable(isMarioInvulnerable());
		return ret;
	}

	public Level getLevel() {
		return level;
	}

	public Agent getAgent() {
		if (agent == null) {
			System.out.println("Info: Agent not specified. Default " + AgentsPool.getCurrentAgent().getName()
					+ " has been used instead");
			agent = AgentsPool.getCurrentAgent();
		}
		return agent;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public void setAgent(String agentWOXorClassName) {
		this.agent = AgentsPool.load(agentWOXorClassName);
	}

	public int getLevelType() {
		return i(getParameterValue("-lt"));
	}

	public void setLevelType(int levelType) {
		setParameterValue("-lt", s(levelType));
	}

	public int getLevelIndex() {
		return i(getParameterValue("-li"));
	}

	public void setLevelIndex(int levelIndex) {
		setParameterValue("-li", s(levelIndex));
	}

	public int getLevelDifficulty() {
		return i(getParameterValue("-ld"));
	}

	public void setLevelDifficulty(int levelDifficulty) {
		setParameterValue("-ld", s(levelDifficulty));
	}

	public int getLevelLength() {
		return i(getParameterValue("-ll"));
	}

	public void setLevelLength(int levelLength) {
		setParameterValue("-ll", s(levelLength));
	}

	public String getLevelFile() {
		return s2(getParameterValue("-lf"));
	}

	public void setLevelFile(String levelFile) {
		setParameterValue("-lf", s2(levelFile));
	}

	public int getLevelRandSeed() {
		return i(getParameterValue("-ls"));
	}

	public void setLevelRandSeed(int levelRandSeed) {
		setParameterValue("-ls", s(levelRandSeed));
	}

	public boolean isVisualization() {
		return b(getParameterValue("-vis"));
	}

	public void setVisualization(boolean visualization) {
		setParameterValue("-vis", s(visualization));
	}

	public void setPauseWorld(boolean pauseWorld) {
		setParameterValue("-pw", s(pauseWorld));
	}

	public Boolean isPauseWorld() {
		return b(getParameterValue("-pw"));
	}

	public Boolean isPowerRestoration() {
		return b(getParameterValue("-pr"));
	}

	public void setPowerRestoration(boolean powerRestoration) {
		setParameterValue("-pr", s(powerRestoration));
	}

	public Boolean isStopSimulationIfWin() {
		return b(getParameterValue("-ssiw"));
	}

	public void setStopSimulationIfWin(boolean stopSimulationIfWin) {
		setParameterValue("-ssiw", s(stopSimulationIfWin));
	}

	public int getNumberOfTrials() {
		return i(getParameterValue("-not"));
	}

	public void setNumberOfTrials(int numberOfTrials) {
		setParameterValue("-not", s(numberOfTrials));
	}

	public int getMarioMode() {
		return i(getParameterValue("-mm"));
	}

	private void setMarioMode(int marioMode) {
		setParameterValue("-mm", s(marioMode));
	}

	public int getZLevelMap() {
		return i(getParameterValue("-zm"));
	}

	public void setZLevelMap(int zLevelMap) {
		setParameterValue("-zm", s(zLevelMap));
	}

	public int getZLevelEnemies() {
		return i(getParameterValue("-ze"));
	}

	public void setZLevelEnemies(int zLevelEnemies) {
		setParameterValue("-ze", s(zLevelEnemies));
	}

	public int getTimeLimit() {
		return i(getParameterValue("-tl"));
	}

	public void setTimeLimit(int timeLimit) {
		setParameterValue("-tl", s(timeLimit));
	}

	public boolean isMarioInvulnerable() {
		return b(getParameterValue("-i"));
	}

	public void setMarioInvulnerable(boolean invulnerable) {
		setParameterValue("-i", s(invulnerable));
	}


	public void resetCurrentTrial() {
		currentTrial = 1;
	}
}
