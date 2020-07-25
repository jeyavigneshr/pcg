package ch.idsia.ai.agents;

import ch.idsia.mario.environments.Environment;

public interface Agent {
	public enum AGENT_TYPE {
		AI, HUMAN, TCP_SERVER
	}

	public void reset();

	public boolean[] getAction(Environment observation);

	public AGENT_TYPE getType();

	public String getName();

	public void setName(String name);
}
