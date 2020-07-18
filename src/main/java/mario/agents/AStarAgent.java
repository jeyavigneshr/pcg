package mario.agents;

import mario.characters.Mario;
import mario.environment.Environment;

public class AStarAgent extends BasicAIAgent implements Agent {

	protected boolean action[] = new boolean[Environment.numberOfButtons];
	protected String name = "AStarAgent";
	private AStarSimulator sim;
	private int tickCounter = 0;
	private float lastX = 0;
	private float lastY = 0;

	public AStarAgent() {
		super("AStarAgent");
	}

	public void reset() {
		action = new boolean[Environment.numberOfButtons];// Empty action
		sim = new AStarSimulator();
	}

	public void printLevel(byte[][] levelScene) {
		for (int i = 0; i < levelScene.length; i++) {
			for (int j = 0; j < levelScene[i].length; j++) {
				System.out.print(levelScene[i][j] + "\t");
			}
			System.out.println("");
		}
	}

	public boolean[] getAction(Environment observation) {
		tickCounter++;
		String s = "Fire";
		if (!sim.levelScene.mario.fire)
			s = "Large";
		if (!sim.levelScene.mario.large)
			s = "Small";
		if (sim.levelScene.verbose > 0)
			System.out.println("Next action! Tick " + tickCounter + " Simulated Mariosize: " + s);

		boolean[] ac = new boolean[5];
		ac[Mario.KEY_RIGHT] = true;
		ac[Mario.KEY_SPEED] = true;

		byte[][] scene = observation.getLevelSceneObservation();
		float[] enemies = observation.getEnemiesFloatPos();

		if (sim.levelScene.verbose > 2)
			System.out.println("Simulating using action: " + sim.printAction(action));
		sim.advanceStep(action);

		if (sim.levelScene.verbose > 5)
			System.out.println("Simulated sprites: ");
		if (sim.levelScene.verbose > 5)
			sim.levelScene.dumpSprites();

		sim.setLevelPart(scene, enemies);
		float[] f = observation.getMarioFloatPos();
		if (sim.levelScene.verbose > 5)
			System.out.println("Sim Mario Pos: " + sim.levelScene.mario.x + " " + sim.levelScene.mario.y + " " + " a: "
					+ sim.levelScene.mario.xa + " " + sim.levelScene.mario.ya);
		if (sim.levelScene.mario.x != f[0] || sim.levelScene.mario.y != f[1]) {
			if (f[0] == lastX && f[1] == lastY)
				return ac;
			if (sim.levelScene.verbose > 0)
				System.out.println("INACURATEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE!");
			if (sim.levelScene.verbose > 0)
				System.out.println(
						"Real: " + f[0] + " " + f[1] + " Est: " + sim.levelScene.mario.x + " " + sim.levelScene.mario.y
								+ " Diff: " + (f[0] - sim.levelScene.mario.x) + " " + (f[1] - sim.levelScene.mario.y));
			sim.levelScene.mario.x = f[0];
			sim.levelScene.mario.y = f[1];
			sim.levelScene.mario.xa = (f[0] - lastX) * 0.89f;
			sim.levelScene.mario.ya = (f[1] - lastY) * 0.85f + 3f;
		}
		lastX = f[0];
		lastY = f[1];
		action = sim.optimise();

		if (sim.levelScene.verbose > 1)
			System.out.println("Returning action: " + sim.printAction(action));
		return action;
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
