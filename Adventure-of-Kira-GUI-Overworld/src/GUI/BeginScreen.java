package GUI;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BeginScreen implements Screen {

	private Sprite splash;
	private SpriteBatch batch;
	private TweenManager tweenManager;
	private BitmapFont fontABC;
	Texture splashTexture;

	@Override
	public void show() {
		batch = new SpriteBatch();
		tweenManager = new TweenManager();
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());

		Tween.set(fontABC, SpriteAccessor.ALPHA).target(0).start(tweenManager);
		Tween.to(fontABC, SpriteAccessor.ALPHA, 2).target(1)
				.start(tweenManager);
		Tween.to(fontABC, SpriteAccessor.ALPHA, 2).target(0).delay(2)
				.start(tweenManager);

		splashTexture = new Texture("pics/Landscape.png");
		splash = new Sprite(splashTexture);
		splash.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() + 18);

		Tween.set(splash, SpriteAccessor.ALPHA).target(1f).start(tweenManager);
		Tween.to(splash, SpriteAccessor.ALPHA, 2).repeatYoyo(1, 0)
				.setCallback(new TweenCallback() {

					@Override
					public void onEvent(int type, BaseTween<?> source) {
						((Game) Gdx.app.getApplicationListener())
								.setScreen(new MainMenuScreen());
					}
				}).start(tweenManager);

		fontABC = new BitmapFont(Gdx.files.internal("Bitmapfont/newfont1.fnt"),
				Gdx.files.internal("Bitmapfont/newfont1.png"), false);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		tweenManager.update(delta);

		batch.begin();
		splash.draw(batch);
		fontABC.draw(batch, "@ ICT BerufsbildungsCenter", 450, 150);
		batch.end();

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		batch.dispose();
		splashTexture.dispose();

	}

}
