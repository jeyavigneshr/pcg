package competition.icegic.robin.astar.level;

import competition.icegic.robin.astar.sprites.Sprite;

public class SpriteTemplate implements Cloneable {
	public int lastVisibleTick = -1;
	public Sprite sprite;
	public boolean isDead = false;
	private int type;
	
	public int getType() {
		return type;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public SpriteTemplate(int type, boolean winged) {
		this.type = type;
	}

}