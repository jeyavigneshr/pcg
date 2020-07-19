package mario.environment;

public interface Environment {

	public static final int numberOfButtons = 5;
	public static final int numberOfObservationElements = 486 + 1;
	public static final int HalfObsWidth = 11;
	public static final int HalfObsHeight = 11;

	public byte[][] getCompleteObservation(); // default: ZLevelScene = 1, ZLevelEnemies = 0

	public byte[][] getEnemiesObservation(); // default: ZLevelEnemies = 0

	public byte[][] getLevelSceneObservation(); // default: ZLevelScene = 1

	public float[] getMarioFloatPos();

	public int getMarioMode();

	public float[] getEnemiesFloatPos();

	public boolean isMarioOnGround();

	public boolean mayMarioJump();

	public boolean isMarioCarrying();

	public byte[][] getMergedObservationZ(int ZLevelScene, int ZLevelEnemies);

	public byte[][] getLevelSceneObservationZ(int ZLevelScene);

	public byte[][] getEnemiesObservationZ(int ZLevelEnemies);

	public int getKillsTotal();

	public int getKillsByFire();

	public int getKillsByStomp();

	public int getKillsByShell();

	public boolean canShoot();

	public String getBitmapEnemiesObservation();

	public String getBitmapLevelObservation();

}
