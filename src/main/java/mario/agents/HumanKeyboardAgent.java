package mario.agents;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import mario.characters.Mario;
import mario.environment.Environment;

public class HumanKeyboardAgent extends KeyAdapter implements Agent {

	List<boolean[]> history = new ArrayList<boolean[]>();
	private boolean[] Action = null;
	private String Name = "HumanKeyboardAgent";

	public HumanKeyboardAgent() {
		this.reset();
	}

	public void reset() {
		Action = new boolean[Environment.numberOfButtons];
	}

	public boolean[] getAction(Environment observation) {
		float[] enemiesPos = observation.getEnemiesFloatPos();
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
		case KeyEvent.VK_LEFT:
			Action[Mario.KEY_LEFT] = isPressed;
			break;
		case KeyEvent.VK_RIGHT:
			Action[Mario.KEY_RIGHT] = isPressed;
			break;
		case KeyEvent.VK_DOWN:
			Action[Mario.KEY_DOWN] = isPressed;
			break;

		case KeyEvent.VK_S:
			Action[Mario.KEY_JUMP] = isPressed;
			break;
		case KeyEvent.VK_A:
			Action[Mario.KEY_SPEED] = isPressed;
			break;
		}
	}

	public List<boolean[]> getHistory() {
		return history;
	}

}
