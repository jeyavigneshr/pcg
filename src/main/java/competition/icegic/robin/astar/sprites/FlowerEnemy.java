package competition.icegic.robin.astar.sprites;

import competition.icegic.robin.astar.LevelScene;

public class FlowerEnemy extends Enemy {
	private int yStart;
	private int jumpTime = 0;

	public FlowerEnemy(LevelScene world, int x, float y, int mapX, int mapY, float currentY) {
		super(world, x, y, 1, ENEMY_SPIKY, false, mapX, mapY);
		kind = KIND_FLOWER_ENEMY;
		noFireballDeath = false;
		this.world = world;
		this.height = 12;
		this.width = 2;
		yStart = (int) y;
		ya = -8;
		this.y -= 1;
		this.layer = 0;

		for (int i = 0; i < 5; i++) {
			move();
		}

		yStart += (currentY - this.y);
		this.y = currentY;
	}

	public void move() {
		if (deadTime > 0) {
			deadTime--;
			if (deadTime == 0) {
				deadTime = 1;
				spriteContext.removeSprite(this);
			}
			x += xa;
			y += ya;
			ya *= 0.95;
			ya += 1;
			return;
		}

		if (y >= yStart) {
			y = yStart;
			int xd = (int) (Math.abs(world.mario.x - x));
			jumpTime++;
			if (jumpTime > 40 && xd > 24) {
				ya = -8;
			} else {
				ya = 0;
			}
		} else {
			jumpTime = 0;
		}

		y += ya;
		ya *= 0.9;
		ya += 0.1f;
	}
}