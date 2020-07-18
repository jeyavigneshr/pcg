package mario.tasks;

import mario.agents.Agent;
import resource.tools.EvaluationOptions;

//import ch.idsia.ai.agents.Agent;
//import ch.idsia.tools.EvaluationOptions;

public interface Task {

    public double[] evaluate (Agent controller);

    public void setOptions (EvaluationOptions options);

    public EvaluationOptions getOptions ();


}
