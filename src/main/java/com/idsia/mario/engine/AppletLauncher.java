package com.idsia.mario.engine;

import javax.swing.*;

import com.idsia.ai.agents.Agent;
import com.idsia.ai.agents.human.HumanKeyboardAgent;

public class AppletLauncher extends JApplet {
	private static final long serialVersionUID = -2238077255106243788L;

	private MarioComponent mario;
	private boolean started = false;

	public void init() {
		showStatus("Applet Loaded");
	}

	public String getAppletInfo() {
		return "MarioInfinite AI Framework by Sergey Karakovskiy";
	}

	public void start() {
		if (!started) {
			started = true;
			Agent hka = new HumanKeyboardAgent();
			hka.reset();
			mario = new MarioComponent(getWidth(), getHeight());
			setContentPane(mario);
			setFocusable(false);
			mario.setFocusCycleRoot(true);

			mario.start();
//            addKeyListener(mario);
//            addFocusListener(mario);
		}
	}

	public void stop() {
		if (started) {
			started = false;
//            removeKeyListener(mario);
			mario.stop();
//            removeFocusListener(mario);
		}
	}
}