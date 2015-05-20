package states;

import game.GameScreen;
import handlers.GameStateManager;
import handlers.MyContactListener;
import GUI.BeginScreen;
import backup.B2DVars;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import entities.Player;

public class Play extends GameState {

	private World world;
	private Box2DDebugRenderer b2dr;

	private OrthographicCamera b2dCam;
	public static MyContactListener cl;
	MapProperties mapProperties;

	private TiledMap tileMap;
	private float tileSize;
	private OrthogonalTiledMapRenderer tmr;
	public static Player player;
	public BitmapFont font;
	public TiledMapTileLayer mapTiles;

	public static Vector2 movement = new Vector2(1, 1);
	private float speed = 7, nitro = 0.6f;

	float x = cam.position.x - cam.viewportWidth;
	float y = cam.position.y - cam.viewportHeight;
	float width = cam.viewportWidth * GameScreen.WINDOW_WIDTH;
	float height = cam.viewportHeight * GameScreen.WINDOW_HEIGHT;

	public boolean keyPressed;
	boolean collisionX = false, collisionY = false;

	Contact contact;

	float desiredVelocity;
	private TiledMapTileLayer mapLoadLayer;
	private Cell cell;

	public Play(GameStateManager gsm) {

		super(gsm);

		// set up box2d stuff
		world = new World(new Vector2(0,0f), true);
		cl = new MyContactListener();
		world.setContactListener(cl);
		b2dr = new Box2DDebugRenderer();

		// create player
		createPlayer();

		// create tiles
		createTiles();

		// set up box2d cam
		b2dCam = new OrthographicCamera(width, height);
		b2dCam.setToOrtho(false, GameScreen.WINDOW_WIDTH / B2DVars.PPM,
				GameScreen.WINDOW_HEIGHT / B2DVars.PPM);

	}

	public void update(float dt) {

		// if (cl.isPlayerOnSpecialGround()) {
		// System.out.println("playerAction = E");
		// if (Gdx.input.isKeyPressed(Keys.E)) {
		// ((Game) Gdx.app.getApplicationListener())
		// .setScreen(new GameScreen());
		//
		// }
		//
		//
		// }
		// if (Gdx.input.isKeyPressed(Keys.NUM_1)) {
		// ((Game) Gdx.app.getApplicationListener())
		// .setScreen(new MainMenuScreen());
		//
		// }

		if (cl.isPlayerOnSpecialGround()) {
			System.out.println("playerAction = E");
			if (Gdx.input.isKeyPressed(Keys.E)) {

				if (cell.getTile().getProperties().containsKey("level")) {
					((Game) Gdx.app.getApplicationListener())
							.setScreen(new BeginScreen());
					System.out.println("Cell level loaded");
				}
			}

		}

		tmr.setView(cam.combined, x, y, width, height);

		desiredVelocity = Math.min(movement.x, player.getBody()
				.getLinearVelocity().x + nitro);

		if (movement.y > speed)
			movement.y = speed;
		else if (movement.y < -speed)
			movement.y = -speed;

		// check input
		handleInput();

		// set player speed
		player.getBody().setLinearVelocity(desiredVelocity, movement.y);

		// update box2d
		world.step(dt, 6, 2);

		player.update(dt);

	}

	public void render() {

		// clear screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		b2dCam.update();

		sb.begin();
		tmr.getBatch().begin();

		tmr.renderTileLayer((TiledMapTileLayer) tileMap.getLayers().get(
				"Ground"));
		tmr.renderTileLayer((TiledMapTileLayer) tileMap.getLayers().get("Deko"));
		tmr.renderTileLayer((TiledMapTileLayer) tileMap.getLayers().get(
				"Objekt"));
		tmr.renderTileLayer((TiledMapTileLayer) tileMap.getLayers().get(
				"Objekt2"));
		tmr.renderTileLayer((TiledMapTileLayer) tileMap.getLayers().get(
				"ActionBlock"));

		tmr.getBatch().end();

		sb.end();

		tmr.setView(cam);

		tmr.getMap().getProperties().get("level");

		// draw player

		player.render(sb);
		sb.setProjectionMatrix(cam.combined);

		sb.begin();
		tmr.getBatch().begin();
		// render player behind layer
		tmr.renderTileLayer((TiledMapTileLayer) tileMap.getLayers().get(
				"ActionBlock"));
		tmr.getBatch().end();
		sb.end();

		// draw box2d world
		b2dr.render(world, b2dCam.combined);
	}

	public void handleInput() {
		Gdx.input.setInputProcessor(new InputProcessor() {

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer,
					int button) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer,
					int button) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean scrolled(int amount) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean mouseMoved(int screenX, int screenY) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean keyUp(int keycode) {
				switch (keycode) {
				case Keys.A:
					player.changeRegion(2, 10000);
					// Sprite für Stehenbleiben ändern
					movement.x = 0;
					System.out.println("Links losgelassen pos: "
							+ player.getPosition().x);
					break;
				case Keys.D:
					player.changeRegion(3, 10000); // Sprite für Stehenbleiben
													// ändern (1/12f) = delay
													// für Animationen
					movement.x = 0;
					System.out.println("Rechts losgelassen pos: "
							+ player.getPosition().x);
					break;
				case Keys.SHIFT_LEFT:
					if (Gdx.input.isKeyPressed(Keys.A)) {
						movement.x = -speed;
						System.out.println("Links mit Shift losgelassen: "
								+ movement.x);
					} else if (Gdx.input.isKeyPressed(Keys.D)) {
						movement.x = speed;
						System.out.println("Rechts mit Shift losegalssen: : "
								+ movement.x);
					}

				case Keys.W:
					player.changeRegion(1, 10000);
					movement.y = 0;
					System.out.println("Oben: " + movement);
					break;

				case Keys.S:
					player.changeRegion(0, 10000);
					movement.y = 0;
					System.out.println("Unten: " + movement);
					break;

				}

				return true;
			}

			@Override
			public boolean keyTyped(char character) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean keyDown(int keycode) {
				switch (keycode) {
				case Keys.SPACE:
					if (cl.isPlayerOnGround()) {
						// movement.y = speed * 1.2f;
						if (cl.isInWater()) {
							System.out.println("Ground + Water!");
							movement.y = speed * 1.2f;
						}
					} else if (cl.isInWater()) {
						System.out.println("Water!");
						movement.y = speed * 1.2f;
						player.changeRegion(0, 1 / 8);
					}
					MyContactListener.water = false;
					break;

				case Keys.A:
					if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
						movement.x = -speed;
						System.out.println("A +Shift" + player.getPosition().x);
						System.out.println("A mit Shift: " + movement.x);
					}
					player.changeRegion(2, 1 / 12f);
					movement.x = -speed;
					System.out.println("Links: " + movement);
					System.out.println("Links pos: " + player.getPosition().x);
					break;

				case Keys.D:
					if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
						movement.x = speed;
						System.out.println("D + Shift" + player.getPosition().x);
						System.out.println("D mit Shift: " + movement.x);
					}
					player.changeRegion(3, 1 / 12f);
					movement.x = speed;
					System.out.println("Rechts: " + movement);
					System.out.println("Rechts pos: " + player.getPosition().x);
					break;

				case Keys.W:
					player.changeRegion(1, 1 / 12f);
					movement.y = speed / 2;
					System.out.println("Oben: " + movement);
					break;

				case Keys.S:
					player.changeRegion(0, 1 / 12f);
					movement.y = -speed / 2;
					System.out.println("Unten: " + movement);
					break;

				case Keys.SHIFT_LEFT:
					if (Gdx.input.isKeyPressed(Keys.A)) {
						player.changeRegion(0, 1 / 18f);
						movement.x = -speed * 1.6f;
						System.out.println("Shift + Links"
								+ player.getPosition().x);
						System.out.println("Links mit Shift: " + movement.x);
					} else if (Gdx.input.isKeyPressed(Keys.D)) {
						player.changeRegion(3, 1 / 18f);
						movement.x = speed * 1.6f;
						System.out.println("Shift + Rechts: "
								+ player.getPosition().x);
						System.out.println("Rechts mit Shift: " + movement.x);
					}
					break;
				}
				return true;
			}
		});
	}

	public void dispose() {
	}

	private void createPlayer() {

		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();

		// create player
		bdef.position.set(2000 / B2DVars.PPM, 2000 / B2DVars.PPM);
		bdef.type = BodyType.DynamicBody;
		Body body = world.createBody(bdef);

		shape.setAsBox(8 / B2DVars.PPM, 14 / B2DVars.PPM);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_GROUND;

		body.createFixture(fdef).setUserData("player");
		

		// create foot sensor
		shape.setAsBox(7.5f / B2DVars.PPM, 0.2f / B2DVars.PPM, new Vector2(0,
				-15 / B2DVars.PPM), 0);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_GROUND | B2DVars.BIT_BACKGROUND;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("foot");

		// create ActionBlock sensor
		shape.setAsBox(16 / B2DVars.PPM, 28 / B2DVars.PPM, new Vector2(0, 0), 0);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_ACTIONBLOCK;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("ActionBlock");

		// create player
		player = new Player(body);
		body.setUserData(player);
	}

	private void createTiles() {

		// load tile map
		tileMap = new TmxMapLoader().load("Overworld/überwelt.tmx");
		tmr = new OrthogonalTiledMapRenderer(tileMap, 1 * 3f);
		tileSize = tileMap.getProperties().get("tilewidth", Integer.class);

		TiledMapTileLayer layer;

		layer = (TiledMapTileLayer) tileMap.getLayers().get("Ground");
		createLayer(layer, B2DVars.BIT_INSEL);

		layer = (TiledMapTileLayer) tileMap.getLayers().get("Deko");
		createLayer(layer, B2DVars.BIT_BACKGROUND);

		layer = (TiledMapTileLayer) tileMap.getLayers().get("Objekt");
		createLayer(layer, B2DVars.BIT_GROUND);

		layer = (TiledMapTileLayer) tileMap.getLayers().get("Objekt2");
		createLayer(layer, B2DVars.BIT_GROUND);

		layer = (TiledMapTileLayer) tileMap.getLayers().get("ActionBlock");
		createLayer(layer, B2DVars.BIT_ACTIONBLOCK);
	}

	private void createLayer(TiledMapTileLayer layer, short bits) {

		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();

		// go through all the cells in the layer
		for (int row = 0; row < layer.getHeight(); row++) {
			for (int col = 0; col < layer.getWidth(); col++) {

				// get cell
				Cell cell = layer.getCell(col, row);

				// check if cell exists
				if (cell == null)
					continue;
				if (cell.getTile() == null)
					continue;

				// create a body + fixture from cell
				bdef.type = BodyType.StaticBody;
				bdef.position.set((col * 3) * tileSize / B2DVars.PPM, (row * 3)
						* tileSize / B2DVars.PPM);

				ChainShape cs = new ChainShape();
				Vector2[] v = new Vector2[3];
				// Ecke unten Links
				v[0] = new Vector2(-tileSize / B2DVars.PPM * 1.5f, -tileSize
						/ B2DVars.PPM * 1.5f);
				// Ecke unten Rechts
				v[1] = new Vector2(-tileSize / B2DVars.PPM * 1.5f, tileSize
						/ B2DVars.PPM * 1.5f);
				// Ecke oben Links
				v[2] = new Vector2(tileSize / B2DVars.PPM * 1.5f, tileSize
						/ B2DVars.PPM * 1.5f);
				// // Ecke oben Rechts
				// v[3] = new Vector2(
				// (tileSize / 2 / B2DVars.PPM) / 2, (-tileSize / 2 /
				// B2DVars.PPM) / 2);
				cs.createChain(v);
				fdef.friction = 0;
				fdef.shape = cs;
				fdef.filter.categoryBits = bits;
				fdef.filter.maskBits = B2DVars.BIT_PLAYER;
				fdef.isSensor = false;
				world.createBody(bdef).createFixture(fdef);

			}
		}

	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public Box2DDebugRenderer getB2dr() {
		return b2dr;
	}

	public void setB2dr(Box2DDebugRenderer b2dr) {
		this.b2dr = b2dr;
	}

	public OrthographicCamera getB2dCam() {
		return b2dCam;
	}

	public void setB2dCam(OrthographicCamera b2dCam) {
		this.b2dCam = b2dCam;
	}

	public MyContactListener getCl() {
		return cl;
	}

	public void setCl(MyContactListener cl) {
		Play.cl = cl;
	}

	public TiledMap getTileMap() {
		return tileMap;
	}

	public void setTileMap(TiledMap tileMap) {
		this.tileMap = tileMap;
	}

	public float getTileSize() {
		return tileSize;
	}

	public void setTileSize(float tileSize) {
		this.tileSize = tileSize;
	}

	public OrthogonalTiledMapRenderer getTmr() {
		return tmr;
	}

	public void setTmr(OrthogonalTiledMapRenderer tmr) {
		this.tmr = tmr;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		Play.player = player;
	}

}