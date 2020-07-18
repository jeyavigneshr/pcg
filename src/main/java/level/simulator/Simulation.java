package level.simulator;

import resource.tools.EvaluationInfo;

public interface Simulation {
	public void setSimulationOptions(SimulationOptions simulationOptions);
	public EvaluationInfo simulateOneLevel();
}
