package mario.tasks;

import mario.agents.Agent;
import resource.tools.EvaluationOptions;

public interface Task {

	public double[] evaluate(Agent controller);

	public void setOptions(EvaluationOptions options);

	public EvaluationOptions getOptions();

}
