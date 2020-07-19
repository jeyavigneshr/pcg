package mario.engine;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import resource.tools.GameViewer;

public class GlobalOptions {

	public static boolean Labels = false;
	public static boolean MarioAlwaysInCenter = false;
	public static Integer FPS = 24;
	public static int InfiniteFPS = 100;
	public static boolean pauseWorld = false;
	public static boolean VisualizationOn = false;
	public static boolean GameVeiwerOn = false;
	public static boolean JsonAsString = true;
	private static MarioComponent marioComponent = null;
	private static GameViewer gameViewer = null;
	public static boolean TimerOn = true;
	public static boolean GameVeiwerContinuousUpdatesOn = false;
	public static boolean PowerRestoration;
	public static boolean StopSimulationIfWin;
	public static boolean isMarioInvulnerable;

	public static void registerMarioComponent(MarioComponent mc) {
		marioComponent = mc;
	}

	public static MarioComponent getMarioComponent() {
		return marioComponent;
	}

	public static void registerGameViewer(GameViewer gv) {
		gameViewer = gv;
	}

	public static void AdjustMarioComponentFPS() {
		marioComponent.adjustFPS();
	}

	public static void gameViewerTick() {
		if (gameViewer != null)
			gameViewer.tick();
	}

	public static String getDateTime(Long d) {
		DateFormat dateFormat = (d == null) ? new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:ms")
				: new SimpleDateFormat("HH:mm:ss:ms");
		if (d != null)
			dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date date = (d == null) ? new Date() : new Date(d);
		return dateFormat.format(date);
	}

}
