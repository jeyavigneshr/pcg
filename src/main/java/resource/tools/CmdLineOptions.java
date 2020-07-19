package resource.tools;

import java.util.Map;

import mario.engine.GlobalOptions;

public class CmdLineOptions extends EvaluationOptions {

	public CmdLineOptions(String[] args) {
		super();
		if (args.length > 1 && !args[0].startsWith("-") /* starts with a path to agent then */) {
			this.setAgent(args[0]);

			String[] shiftedargs = new String[args.length - 1];
			System.arraycopy(args, 1, shiftedargs, 0, args.length - 1);
			this.setUpOptions(shiftedargs);
		} else
			this.setUpOptions(args);

		if (isEcho()) {
			System.out.println("\nOptions have been set to:");
			for (Map.Entry<String, String> el : optionsHashMap.entrySet())
				System.out.println(el.getKey() + ": " + el.getValue());
		}
		GlobalOptions.GameVeiwerContinuousUpdatesOn = isGameViewerContinuousUpdates();
	}

	public Boolean isToolsConfigurator() {
		return b(getParameterValue("-tc"));
	}

	public Boolean isGameViewer() {
		return b(getParameterValue("-gv"));
	}

	public Boolean isGameViewerContinuousUpdates() {
		return b(getParameterValue("-gvc"));
	}

	public Boolean isEcho() {
		return b(getParameterValue("-echo"));
	}

}
