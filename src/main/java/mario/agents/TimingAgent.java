package mario.agents;

import mario.environment.Environment;

//import ch.idsia.ai.agents.Agent;
//import ch.idsia.ai.agents.Agent.AGENT_TYPE;
//import ch.idsia.mario.environments.Environment;

public class TimingAgent implements Agent{


    private Agent agent;
    private long timeTaken = 0;
    private int evaluations = 0;

    public TimingAgent (Agent agent) {
        this.agent = agent;
    }
    
    public void reset() {
        agent.reset ();
    }

    public boolean[] getAction(Environment observation) {
        long start = System.currentTimeMillis();
        boolean[] action = agent.getAction (observation);
        timeTaken += (System.currentTimeMillis() - start);
        evaluations++;
        //compute all metrics
        return action;
    }

    public AGENT_TYPE getType() {
        return agent.getType ();
    }

    public String getName() {
        return agent.getName ();
    }

    public void setName(String name) {
        agent.setName (name);
    }

    public double averageTimeTaken () {
        double average = ((double) timeTaken) / evaluations;
        timeTaken = 0;
        evaluations = 0;
        return average;
    }

}