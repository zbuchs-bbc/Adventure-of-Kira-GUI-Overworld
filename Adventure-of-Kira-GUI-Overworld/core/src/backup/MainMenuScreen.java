package backup;

import backup.MyGdxGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenuScreen implements Screen {
	
	private Texture splashTexture;
	private Sprite splash;
	private TextureAtlas atlas;
	private TextureAtlas atlas2;
	private Skin skin;
	private Skin skin2;
	private Stage stage;
	private TextButtonStyle textButtonStyle;
	private TextButtonStyle textButtonStyle2;
	private TextButton Exit,Start;
	private BitmapFont font;
	private Table table;
	private SpriteBatch batch;
	private BitmapFont fontABC;
	
	@Override
	public void show() {
		stage = new Stage();
		batch = new SpriteBatch();
		splashTexture = new Texture("pics/Landscape.png");
		splash = new Sprite(splashTexture);
		splash.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() + 18);
		
		font = new BitmapFont(Gdx.files.internal("Bitmapfont/newfont.fnt"));
		
		atlas = new TextureAtlas("ui/buttonexit.pack");
		atlas2 = new TextureAtlas("ui/unitybutton.pack");
		skin = new Skin(atlas);
		skin2 = new Skin(atlas2);
		
		skin.addRegions(atlas);
		skin2.addRegions(atlas2);
		
		fontABC = new BitmapFont(Gdx.files.internal("BitmapFont/newfont.fnt"));
					
		
		
		textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.getDrawable("button.up");
		textButtonStyle.down = skin.getDrawable("button.down");
		textButtonStyle.font = font;
		
		textButtonStyle2 = new TextButtonStyle();
		textButtonStyle2.up = skin2.getDrawable("button.up");
		textButtonStyle2.down = skin2.getDrawable("button.down");
		textButtonStyle2.font = font;
		
		Exit = new TextButton("", textButtonStyle);
		Start = new TextButton("",textButtonStyle2);
		
		Gdx.input.setInputProcessor(stage);
		
		Start.setPosition(MyGdxGame.WINDOW_WIDTH*0.2f, MyGdxGame.WINDOW_HEIGHT*0.035f);
		Exit.setPosition(MyGdxGame.WINDOW_WIDTH*0.4f +200, MyGdxGame.WINDOW_HEIGHT*0.035f);
		
		Start.setWidth(MyGdxGame.WINDOW_WIDTH*0.2f);
		Exit.setWidth(MyGdxGame.WINDOW_WIDTH*0.2f);
		
		Start.setHeight(MyGdxGame.WINDOW_HEIGHT*0.1f);
		Exit.setHeight(MyGdxGame.WINDOW_HEIGHT*0.1f);
		
		stage.addActor(Start);
		stage.addActor(Exit);
		
		Start.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("");
            };
		});
		Exit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            };
		});
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		batch.begin();
		splash.draw(batch);
		fontABC.draw(batch, "Adventure of Kira",770, 840);
		batch.end();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

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
		skin.dispose();
		skin2.dispose();
		atlas.dispose();
		atlas2.dispose();
		stage.dispose();

	}

}
