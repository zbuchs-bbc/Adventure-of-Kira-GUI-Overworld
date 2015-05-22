package GUI;

import com.badlogic.gdx.Game;

public class MyGdxGame extends Game {

	// erstelle Instanzen

	public static final int WINDOW_WIDTH = 1920;
	public static final int WINDOW_HEIGHT = 1080;

	public void create() {
		setScreen(new BeginScreen());

	}

	public void render() {
		super.render();
	}

	public void resize(int width, int height) {
		super.resize(width, height);
	}

	public void update() {
	}

	public void pause() {

	}

	public void resume() {

	}

	public void dispose() {
		super.dispose();
	}

}
