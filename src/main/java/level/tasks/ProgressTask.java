package level.tasks;

import java.util.List;

import mario.agents.Agent;
import mario.tasks.Task;
import resource.tools.EvaluationInfo;
import resource.tools.EvaluationOptions;
import resource.tools.Evaluator;

public class ProgressTask implements Task {
	private EvaluationOptions options;

	public ProgressTask(EvaluationOptions evaluationOptions) {
		setOptions(evaluationOptions);
	}

	public double[] evaluate(Agent controller) {
		double distanceTravelled = 0;
		options.setAgent(controller);
		Evaluator evaluator = new Evaluator(options);
		List<EvaluationInfo> results = evaluator.evaluate();
		for (EvaluationInfo result : results) {
			distanceTravelled += result.computeDistancePassed();
		}
		distanceTravelled = distanceTravelled / results.size();
		return new double[] { distanceTravelled };
	}

	public void setOptions(EvaluationOptions options) {
		this.options = options;
	}

	public EvaluationOptions getOptions() {
		return options;
	}
}
