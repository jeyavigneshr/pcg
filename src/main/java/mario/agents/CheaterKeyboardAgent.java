package mario.agents;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import mario.characters.Mario;
import mario.engine.GlobalOptions;
import mario.environment.Environment;

public class CheaterKeyboardAgent extends KeyAdapter implements Agent {

	private boolean Action[] = null;
	private String Name = "Instance of CheaterKeyboardAgent";
	private Integer prevFPS = 24;

	public CheaterKeyboardAgent() {
		reset();
	}

	public void reset() {
		Action = new boolean[16];
	}

	public boolean[] getAction(Environment observation) {
		return Action;
	}

	public AGENT_TYPE getType() {
		return AGENT_TYPE.HUMAN;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public void keyPressed(KeyEvent e) {
		toggleKey(e.getKeyCode(), true);
	}

	public void keyReleased(KeyEvent e) {
		toggleKey(e.getKeyCode(), false);
	}

	private void toggleKey(int keyCode, boolean isPressed) {
		switch (keyCode) {
		case KeyEvent.VK_D:
			if (isPressed)
				GlobalOptions.gameViewerTick();
			break;
		case KeyEvent.VK_V:
			if (isPressed)
				GlobalOptions.VisualizationOn = !GlobalOptions.VisualizationOn;
			break;
		case KeyEvent.VK_U:
			Action[Mario.KEY_LIFE_UP] = isPressed;
			break;
		case KeyEvent.VK_W:
			Action[Mario.KEY_WIN] = isPressed;
			break;
		case KeyEvent.VK_P:
			if (isPressed) {
				GlobalOptions.pauseWorld = !GlobalOptions.pauseWorld;
				Action[Mario.KEY_PAUSE] = GlobalOptions.pauseWorld;
			}
			break;
		case KeyEvent.VK_L:
			if (isPressed) {
				GlobalOptions.Labels = !GlobalOptions.Labels;
			}
			break;
		case KeyEvent.VK_C:
			if (isPressed) {
				GlobalOptions.MarioAlwaysInCenter = !GlobalOptions.MarioAlwaysInCenter;
			}
			break;
		case 61:
			if (isPressed) {
				GlobalOptions.AdjustMarioComponentFPS();
			}
			break;
		case 45:
			if (isPressed) {
				GlobalOptions.AdjustMarioComponentFPS();
			}
			break;
		case 56:
			if (isPressed) {
				int temp = prevFPS;
				prevFPS = GlobalOptions.FPS;
				GlobalOptions.FPS = (GlobalOptions.FPS == GlobalOptions.InfiniteFPS) ? temp : GlobalOptions.InfiniteFPS;
				GlobalOptions.AdjustMarioComponentFPS();
			}
			break;
		}
	}
}
