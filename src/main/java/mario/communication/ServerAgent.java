package mario.communication;

import java.io.IOException;

import mario.agents.Agent;
import mario.agents.BasicAIAgent;
import mario.environment.Environment;
import resource.tools.EvaluationInfo;

public class ServerAgent extends BasicAIAgent implements Agent {

	Server server = null;
	private int port;
	private TCP_MODE tcpMode = TCP_MODE.SIMPLE_TCP;

	public ServerAgent(int port, boolean enable) {
		super("ServerAgent");
		this.port = port;
		if (enable) {
			createServer(port);
		}
	}

	public ServerAgent(Server server, boolean isFastTCP) {
		super("ServerAgent");
		this.server = server;
		this.tcpMode = (isFastTCP) ? TCP_MODE.FAST_TCP : TCP_MODE.SIMPLE_TCP;
	}

	public String getName() {
		return this.name + ((server == null) ? "" : server.getClientName());
	}

	public void setFastTCP(boolean isFastTCP) {
		this.tcpMode = (isFastTCP) ? TCP_MODE.FAST_TCP : TCP_MODE.SIMPLE_TCP;
	}

	private void createServer(int port) {
		this.server = new Server(port, Environment.numberOfObservationElements, Environment.numberOfButtons);
	}

	public boolean isAvailable() {
		return (server != null) && server.isClientConnected();
	}

	public void reset() {
		action = new boolean[Environment.numberOfButtons];
		if (server == null)
			this.createServer(port);
	}

	private void sendRawObservation(Environment observation) {
		byte[][] mergedObs = observation.getCompleteObservation(/* 1, 0 */);
		String tmpData = "O " + observation.mayMarioJump() + " " + observation.isMarioOnGround();
		for (int x = 0; x < mergedObs.length; ++x) {
			for (int y = 0; y < mergedObs.length; ++y) {
				tmpData += " " + (mergedObs[x][y]);
			}
		}
		tmpData += " " + observation.getMarioFloatPos()[0] + " " + observation.getMarioFloatPos()[1];

		float[] enemiesFloatPoses = observation.getEnemiesFloatPos();
		for (int i = 0; i < enemiesFloatPoses.length; ++i)
			tmpData += " " + enemiesFloatPoses[i];

		server.sendSafe(tmpData);
	}

	private void sendObservation(Environment observation) {
		if (this.tcpMode == TCP_MODE.SIMPLE_TCP) {
			this.sendRawObservation(observation);
		} else if (this.tcpMode == TCP_MODE.FAST_TCP) {
			this.sendBitmapObservation(observation);
		}
	}

	private void sendBitmapObservation(Environment observation) {

		String tmpData = "E" + (observation.mayMarioJump() ? "1" : "0") + (observation.isMarioOnGround() ? "1" : "0")
				+ observation.getBitmapLevelObservation();
		int check_sum = 0;
		for (int i = 3; i < tmpData.length(); ++i) {
			char cur_char = tmpData.charAt(i);
			if (cur_char != 0) {
				check_sum += Integer.valueOf(cur_char);
			}
		}
		if (tmpData.length() != /* 125 - 61 */34)
			try {
				throw new Exception("Pipetz!");
			} catch (Exception e) {
				e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
				System.err.println(e.getMessage());
			}
		tmpData += " " + check_sum;
		server.sendSafe(tmpData);
	}

	public void integrateEvaluationInfo(EvaluationInfo evaluationInfo) {
		String fitnessStr = "FIT " + evaluationInfo.marioStatus + " " + evaluationInfo.computeDistancePassed() + " "
				+ evaluationInfo.timeLeft + " " + evaluationInfo.marioMode + " " + evaluationInfo.numberOfGainedCoins
				+ " ";
		server.sendSafe(fitnessStr);
	}

	private boolean[] receiveAction() throws IOException, NullPointerException {
		String data = server.recvSafe();
		if (data == null || data.startsWith("reset"))
			return null;
		boolean[] ret = new boolean[Environment.numberOfButtons];
		for (int i = 0; i < Environment.numberOfButtons; ++i) {
			ret[i] = (data.charAt(i) == '1');
		}
		return ret;
	}

	public boolean[] getAction(Environment observation) {
		try {
			sendObservation(observation);
			action = receiveAction();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("I/O Communication Error");
			reset();
		}
		return action;
	}

	public AGENT_TYPE getType() {
		return Agent.AGENT_TYPE.TCP_SERVER;
	}

}
