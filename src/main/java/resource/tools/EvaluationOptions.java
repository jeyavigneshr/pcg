package resource.tools;

import java.awt.Point;

import level.simulator.SimulationOptions;
import mario.engine.GlobalOptions;

public class EvaluationOptions extends SimulationOptions {

	public EvaluationOptions() {
		super();
	}

	public void setUpOptions(String[] args) {
		for (int i = 0; i < args.length - 1; i += 2)
			try {
				setParameterValue(args[i], args[i + 1]);
			} catch (ArrayIndexOutOfBoundsException e) {
				System.err.println("Error: Wrong number of input parameters");
			}
		GlobalOptions.VisualizationOn = isVisualization();
		GlobalOptions.FPS = (isMaxFPS()) ? GlobalOptions.InfiniteFPS : 24 /* GlobalOptions.FPS */;
		GlobalOptions.pauseWorld = isPauseWorld();
		GlobalOptions.PowerRestoration = isPowerRestoration();
		GlobalOptions.StopSimulationIfWin = isStopSimulationIfWin();
		GlobalOptions.TimerOn = isTimer();
	}

	public Boolean isExitProgramWhenFinished() {
		return b(getParameterValue("-ewf"));
	}

	public void setExitProgramWhenFinished(boolean exitProgramWhenFinished) {
		setParameterValue("-ewf", s(exitProgramWhenFinished));
	}

	public String getMatlabFileName() {
		return getParameterValue("-m");
	}

	public void setMatlabFileName(String matlabFileName) {
		setParameterValue("-m", matlabFileName);
	}

	public Point getViewLocation() {
		int x = i(getParameterValue("-vlx"));
		int y = i(getParameterValue("-vly"));
		return new Point(x, y);
	}

	public Boolean isViewAlwaysOnTop() {
		return b(getParameterValue("-vaot"));
	}

	public void setMaxFPS(boolean isMaxFPS) {
		setParameterValue("-maxFPS", s(isMaxFPS));
		GlobalOptions.FPS = (isMaxFPS()) ? GlobalOptions.InfiniteFPS : 24;
	}

	public Boolean isMaxFPS() {
		return b(getParameterValue("-maxFPS"));
	}

	public String getAgentName() {
		return getParameterValue("-ag");
	}

	public Integer getServerAgentPort() {
		setNumberOfTrials(-1);
		String value = optionsHashMap.get("-port");
		if (value == null) {
			if (getAgentName().startsWith("ServerAgent")) {
				if (getAgentName().split(":").length > 1) {
					return Integer.parseInt(getAgentName().split(":")[1]);
				}
			}
		}
		return Integer.parseInt(defaultOptionsHashMap.get("-port"));
	}

	public boolean isServerAgentEnabled() {
		return getAgentName().startsWith("ServerAgent");
	}

	public boolean isServerMode() {
		return b(getParameterValue("-server"));
	}

	public boolean isFastTCP() {
		return b(getParameterValue("-fastTCP"));
	}

	public boolean isTimer() {
		return b(getParameterValue("-t"));
	}

}
