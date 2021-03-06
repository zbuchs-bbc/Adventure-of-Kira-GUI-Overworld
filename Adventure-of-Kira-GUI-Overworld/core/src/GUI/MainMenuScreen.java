package GUI;

import game.GameScreen;
import aurelienribon.tweenengine.Tween;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenuScreen implements Screen {

	private SpriteBatch batch;
	private Sprite sprite;
	private Stage stage;
	private BitmapFont font, font2;
	private Skin skin, skin2;
	private TextureAtlas textureAtlas, textureAtlas2;
	private TextButtonStyle textButtonStyle, textButtonStyle2;
	private BitmapFont fontABC;
	private Texture menuTexture;

	public TextButton button, button1, button2;
	public MyGdxGame game;

	@Override
	public void show() {

		batch = new SpriteBatch();

		Tween.registerAccessor(Sprite.class, new SpriteAccessor());

		menuTexture = new Texture("assets/pics/Landscape.png");
		sprite = new Sprite(menuTexture);
		sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		font = new BitmapFont();
		font2 = new BitmapFont();
		skin = new Skin();
		skin2 = new Skin();
		textureAtlas = new TextureAtlas(
				Gdx.files.internal("ui/unitybutton.pack"));
		textureAtlas2 = new TextureAtlas(
				Gdx.files.internal("ui/buttonexit.pack"));
		skin.addRegions(textureAtlas);
		skin2.addRegions(textureAtlas2);

		// create the Button
		textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = font;
		textButtonStyle.up = skin.getDrawable("button.up");
		textButtonStyle.down = skin.getDrawable("button.down");

		textButtonStyle2 = new TextButtonStyle();
		textButtonStyle2.font = font2;
		textButtonStyle2.up = skin2.getDrawable("button.up");
		textButtonStyle2.down = skin2.getDrawable("button.down");

		button = new TextButton("", textButtonStyle2);
		button.setPosition(MyGdxGame.WINDOW_WIDTH * 0.6f,
				MyGdxGame.WINDOW_HEIGHT * 0.05f);
		button.setSize(0.2f * stage.getWidth(), 0.1f * stage.getWidth()
				* button.getHeight() / button.getWidth());

		button1 = new TextButton("", textButtonStyle);
		button1.setPosition(MyGdxGame.WINDOW_WIDTH * 0.2f,
				MyGdxGame.WINDOW_HEIGHT * 0.05f);
		button1.setSize(0.2f * stage.getWidth(), 0.1f * stage.getWidth()
				* button1.getHeight() / button1.getWidth());

		/**
		 * ClickListener für die Buttons
		 * 
		 * @author Simon Buchli
		 */
		button.addListener(new ClickListener() {
			public void touchUp(InputEvent e, float x, float y, int point,
					int button) {
				Gdx.app.exit();
			}
		});
		button1.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

				((Game) Gdx.app.getApplicationListener())
						.setScreen(new GameScreen());

			};

		});

		fontABC = new BitmapFont(
				Gdx.files.internal("assets/Bitmapfont/newfont.fnt"),
				Gdx.files.internal("assets/Bitmapfont/newfont.png"), false);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		sprite.draw(batch);
		batch.draw(menuTexture, 0, 0, MyGdxGame.WINDOW_WIDTH,
				MyGdxGame.WINDOW_HEIGHT);
		fontABC.draw(batch, "Adventure of Kira",
				MyGdxGame.WINDOW_WIDTH * 0.41f, MyGdxGame.WINDOW_HEIGHT - 240);
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 20, 1050);

		batch.end();

		// Add Buttons
		stage.addActor(button);
		stage.addActor(button1);

		stage.draw();

	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
		batch.dispose();
		sprite.getTexture().dispose();
		stage.dispose();

	}

}