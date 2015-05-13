package GUI;

import com.badlogic.gdx.Game;

public class MyGdxGame extends Game {

	// erstelle Instanzen

	public static final int WINDOW_WIDTH = 1920;
	public static final int WINDOW_HEIGHT = 1080;
	public static final double VERSION = 3.0;

	public void create() {
		setScreen(new BeginScreen());

	}

	public void resize(int width, int height) {
		super.resize(width, height);
	}

	public void render() {
		super.render();
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
