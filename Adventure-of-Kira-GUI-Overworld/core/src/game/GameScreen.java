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

	public static final String TITLE = "Adventures of Kira";
	public static final int WINDOW_WIDTH = 1920;
	public static final int WINDOW_HEIGHT = 1080;
	public static final int SCALE = 1;
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
	

	public void create() {



	}

	private void drawString(SpriteBatch sb, String s, float x, float y) {
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '/')
				c = 10;
			else if (c >= '0' && c <= '9')
				c -= '0';
			else
				continue;
			bmf.draw(sb, "Coins: " + Player.getNumCoins(),
					cam.position.x - 900, cam.position.y + 500);
		}
	}
	

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
		ressources.loadTexture("images/player.png", "player");
		ressources.loadTexture("images/coins3.png", "coin");
		ressources.loadTexture("images/coins3.png", "wasser");


		this.sb = new SpriteBatch();
		this.cam = new OrthographicCamera();
		this.cam.setToOrtho(false, WINDOW_WIDTH, WINDOW_HEIGHT);
		this.hudCam = new OrthographicCamera();
		this.hudCam.setToOrtho(false, WINDOW_WIDTH, WINDOW_HEIGHT);
		this.stage = new Stage();

		gsm = new GameStateManager(this);

		score = 0;
		scoreName = "score: " + score;
		bmf = new BitmapFont();
	}


	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
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
			sb.begin();
			drawString(sb, Player.getNumCoins() + " / 0", cam.position.x * 100+100,
					cam.position.y * 100);
			sb.end();

			cam.position.set(Play.player.getPosition().x * 100+100 ,
					Play.player.getPosition().y * 100, 0);
			
			sb.setProjectionMatrix(cam.combined);
			cam.update();
		}
		
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}

			
	}


