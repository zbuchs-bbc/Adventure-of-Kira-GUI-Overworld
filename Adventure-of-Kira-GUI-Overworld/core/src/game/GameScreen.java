package game;

import handlers.Content;
import handlers.GameStateManager;
import handlers.MyInput;
import handlers.MyInputProcessor;
import states.Play;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import entities.Player;

public class GameScreen implements ApplicationListener, Screen {

	public static final int WINDOW_WIDTH = 1920;
	public static final int WINDOW_HEIGHT = 1080;
	public static final float STEP = 1 / 60f;
	private float accum;
	public static int score;
	public static String scoreName;
	public static BitmapFont bmf;
	private Stage stage;

	private SpriteBatch sb;
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;

	private GameStateManager gsm;

	public static Content ressources;

	public void dispose() {
		bmf.dispose();
		sb.dispose();

	}

	public SpriteBatch getSpriteBatch() {
		return sb;
	}

	public OrthographicCamera getCamera() {
		return cam;
	}

	public OrthographicCamera getHUDCamera() {
		return hudCam;
	}

	public void resize(int w, int h) {
		stage.getViewport().update(WINDOW_WIDTH, WINDOW_HEIGHT);
	}

	public void pause() {
	}

	public void resume() {
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(new MyInputProcessor());

		ressources = new Content();
		ressources.loadTexture("assets/images/player.png", "player");

		this.sb = new SpriteBatch();
		this.cam = new OrthographicCamera();
		this.cam.setToOrtho(false, WINDOW_WIDTH, WINDOW_HEIGHT);
		this.hudCam = new OrthographicCamera();
		this.hudCam.setToOrtho(false, WINDOW_WIDTH, WINDOW_HEIGHT);
		this.stage = new Stage();

		gsm = new GameStateManager(this);

		bmf = new BitmapFont();
	}

	@Override
	public void hide() {
	}

	@Override
	public void render() {
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		accum += Gdx.graphics.getDeltaTime();
		while (accum >= STEP) {
			accum -= STEP;
			gsm.update(STEP);
			gsm.render();
			MyInput.update();

			bmf.setScale(2);

			cam.position.set(Play.player.getPosition().x * 100 + 100,
					Play.player.getPosition().y * 100, 0);

			sb.setProjectionMatrix(cam.combined);
			cam.update();
		}

	}

	@Override
	public void create() {

	}

}
