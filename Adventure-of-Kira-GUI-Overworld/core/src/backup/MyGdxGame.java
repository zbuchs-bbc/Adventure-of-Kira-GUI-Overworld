package backup;

import ch.bbc.zkillt.Gui.BeginScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class MyGdxGame extends Game {

	// erstelle Instanzen

	public static final int WINDOW_WIDTH = 1920;
	public static final int WINDOW_HEIGHT = 1080;
	public static final double VERSION = 3.0;

	public SpriteBatch batch;
	public BitmapFont font;

	public void create() {
		setScreen(new BeginScreen());
		batch = new SpriteBatch();
		font = new BitmapFont();
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
		super.pause();
	}

	public void resume() {
		super.resume();
	}

	public void dispose() {
		super.dispose();
	}

}
