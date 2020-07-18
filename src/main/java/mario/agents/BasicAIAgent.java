package mario.agents;

import mario.environment.Environment;

public class BasicAIAgent implements Agent {

	protected boolean action[] = new boolean[Environment.numberOfButtons];
	protected String name = "Instance_of_BasicAIAgent._Change_this_name";

	public BasicAIAgent(String s) {
		setName(s);
	}

	public void reset() {
		action = new boolean[Environment.numberOfButtons];// Empty action
	}

	public boolean[] getAction(Environment observation) {
		return new boolean[Environment.numberOfButtons]; // Empty action
	}

	public AGENT_TYPE getType() {
		return Agent.AGENT_TYPE.AI;
	}

	public String getName() {
		return name;
	}

	public void setName(String Name) {
		this.name = Name;
	}

}
