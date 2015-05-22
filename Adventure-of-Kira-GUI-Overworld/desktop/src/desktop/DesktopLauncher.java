package desktop;

import GUI.MyGdxGame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {

	public static void main(String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();

		cfg.title = "Adventures of Kira";
		cfg.width = MyGdxGame.WINDOW_WIDTH;
		cfg.height = MyGdxGame.WINDOW_HEIGHT;
		cfg.vSyncEnabled = false;

		new LwjglApplication(new MyGdxGame(), cfg);
	}
}
