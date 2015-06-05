package states;

import game.GameScreen;
import handlers.GameStateManager;
import handlers.MyContactListener;
import GUI.MainMenuScreen;
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
	public Contact contact;

	public static Vector2 movement = new Vector2(1, 1);
	private float speed = 7, nitro = 0.6f;
	private TiledMapTileLayer mapLoadLayer;
	private Cell cell;

	float x = cam.position.x - cam.viewportWidth;
	float y = cam.position.y - cam.viewportHeight;
	float width = cam.viewportWidth * GameScreen.WINDOW_WIDTH;
	float height = cam.viewportHeight * GameScreen.WINDOW_HEIGHT;
	float desiredVelocity;

	public boolean keyPressed;
	boolean collisionX = false, collisionY = false;

	public Play(GameStateManager gsm) {

		super(gsm);

		// set up box2d stuff
		world = new World(new Vector2(0, 0f), true);
		cl = new MyContactListener();
		world.setContactListener(cl);
		b2dr = new Box2DDebugRenderer();

		// create player
		createPlayer();

		// create tiles
		createTiles();

		// set up box2d cam
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, GameScreen.WINDOW_WIDTH / B2DVars.PPM,
				GameScreen.WINDOW_HEIGHT / B2DVars.PPM);
	}

	public void update(float dt) {

		/**
		 * check if player is on level
		 * 
		 * @author Simon Buchli
		 */

		//
		if (cl.isPlayerOnSpecialGround()) {
			System.out.println("playerAction = E");
			if (Gdx.input.isKeyPressed(Keys.E)) {
				((Game) Gdx.app.getApplicationListener())
						.setScreen(new GameScreen());
			}
		}
		if (Gdx.input.isKeyPressed(Keys.NUM_1)) {
			((Game) Gdx.app.getApplicationListener())
					.setScreen(new MainMenuScreen());
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

		/**
		 * player can't move out of edge
		 * 
		 * @author Kurt Blaser, Simon Buchli
		 */
		if (cam.position.x > 2793) {
			cam.position.x = 2793;
		}

		if (cam.position.x < 2021) {
			cam.position.x = 2021;
		}

		if (cam.position.y > 3275) {
			cam.position.y = 3275;
		}
		if (cam.position.y < 1720) {
			cam.position.y = 1720;
		}

		cam.update();

	}

	public void render() {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		b2dCam.update();

		sb.begin();
		tmr.getBatch().begin();

		// render the maplayers
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

		tmr.render();

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

	/**
	 * check the keyInput
	 * 
	 * @author Tim Killenberger
	 */
	public void handleInput() {
		Gdx.input.setInputProcessor(new InputProcessor() {

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer,
					int button) {
				return false;
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				return false;
			}

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer,
					int button) {
				return false;
			}

			@Override
			public boolean scrolled(int amount) {
				return false;
			}

			@Override
			public boolean mouseMoved(int screenX, int screenY) {
				return false;
			}

			/**
			 * Player-Sprite movement
			 * 
			 * @author Tim Killenberger
			 */
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

					player.changeRegion(3, 10000);
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
				return false;
			}

			/**
			 * Player WASD movement
			 * 
			 * @author Tim Killenberger, Simon Buchli
			 */
			@Override
			public boolean keyDown(int keycode) {
				switch (keycode) {
				case Keys.A:

					player.changeRegion(2, 1 / 12f);
					movement.x = -speed;
					System.out.println("Links: " + movement);
					System.out.println("Links pos: " + player.getPosition().x);
					break;

				case Keys.D:

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
				}
				return true;
			}
		});
	}

	public void dispose() {
		b2dr.dispose();
		font.dispose();
		game.dispose();
		tileMap.dispose();
	}

	/**
	 * creates the Player with all sensors
	 * 
	 * @author Tim Killenberger, Simon Buchli
	 */
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

	/**
	 * load the tile map and creates the layers
	 * 
	 * @author Tim Killenberger, Simon Buchli
	 */
	private void createTiles() {

		// load tile map
		tileMap = new TmxMapLoader().load("assets/Overworld/überwelt.tmx");
		tmr = new OrthogonalTiledMapRenderer(tileMap, 1 * 3f);
		tileSize = tileMap.getProperties().get("tilewidth", Integer.class);

		TiledMapTileLayer layer;

		// create the layers
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

	/**
	 * create the layer which the player can move on
	 * 
	 * @author Tim Killenberger, Simon Buchli
	 */
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
				bdef.position.set((col * 3.05f) * tileSize / B2DVars.PPM,
						(row * 3) * tileSize / B2DVars.PPM);

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

	// Getter & Setter
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

	public static Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		Play.player = player;
	}

	public MapProperties getMapProperties() {
		return mapProperties;
	}

	public void setMapProperties(MapProperties mapProperties) {
		this.mapProperties = mapProperties;
	}

	public BitmapFont getFont() {
		return font;
	}

	public void setFont(BitmapFont font) {
		this.font = font;
	}

	public TiledMapTileLayer getMapTiles() {
		return mapTiles;
	}

	public void setMapTiles(TiledMapTileLayer mapTiles) {
		this.mapTiles = mapTiles;
	}

	public static Vector2 getMovement() {
		return movement;
	}

	public static void setMovement(Vector2 movement) {
		Play.movement = movement;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getNitro() {
		return nitro;
	}

	public void setNitro(float nitro) {
		this.nitro = nitro;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public boolean isKeyPressed() {
		return keyPressed;
	}

	public void setKeyPressed(boolean keyPressed) {
		this.keyPressed = keyPressed;
	}

	public boolean isCollisionX() {
		return collisionX;
	}

	public void setCollisionX(boolean collisionX) {
		this.collisionX = collisionX;
	}

	public boolean isCollisionY() {
		return collisionY;
	}

	public void setCollisionY(boolean collisionY) {
		this.collisionY = collisionY;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public float getDesiredVelocity() {
		return desiredVelocity;
	}

	public void setDesiredVelocity(float desiredVelocity) {
		this.desiredVelocity = desiredVelocity;
	}

	public TiledMapTileLayer getMapLoadLayer() {
		return mapLoadLayer;
	}

	public void setMapLoadLayer(TiledMapTileLayer mapLoadLayer) {
		this.mapLoadLayer = mapLoadLayer;
	}

	public Cell getCell() {
		return cell;
	}

	public void setCell(Cell cell) {
		this.cell = cell;
	}

}