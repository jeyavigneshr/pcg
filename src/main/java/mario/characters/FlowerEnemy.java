package mario.characters;

import mario.environment.LevelScene;

public class FlowerEnemy extends Enemy {
	private int tick;
	private int yStart;
	private int jumpTime = 0;

	public FlowerEnemy(LevelScene world, int x, int y, int mapX, int mapY) {
		super(world, x, y, 1, ENEMY_SPIKY, false, mapX, mapY);
		kind = KIND_ENEMY_FLOWER;
		noFireballDeath = false;
		this.world = world;
		this.xPic = 0;
		this.yPic = 6;
		this.yPicO = 24;
		this.height = 12;
		this.width = 2;
		yStart = y;
		ya = -8;
		this.y -= 1;
		this.layer = 0;

		for (int i = 0; i < 4; i++) {
			move();
		}
	}

	public void move() {
		if (deadTime > 0) {
			deadTime--;

			if (deadTime == 0) {
				deadTime = 1;
				for (int i = 0; i < 8; i++) {
					world.addSprite(new Sparkle(this.world, (int) (x + Math.random() * 16 - 8) + 4,
							(int) (y - Math.random() * 8) + 4, (float) (Math.random() * 2 - 1),
							(float) Math.random() * -1, 0, 1, 5));
				}
				this.world.removeSprite(this);
			}

			x += xa;
			y += ya;
			ya *= 0.95;
			ya += 1;

			return;
		}

		tick++;

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

		xPic = ((tick / 2) & 1) * 2 + ((tick / 6) & 1);
	}

}