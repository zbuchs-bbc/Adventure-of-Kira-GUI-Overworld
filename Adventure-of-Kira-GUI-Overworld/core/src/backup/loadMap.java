package backup;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class loadMap extends Game {
	
	public loadMap() {
		world = new World(new Vector2(0, -9.81f), true);
		
		// create player
		createTiles();
	}
	
	public TiledMap tileMap;
	public World world;
	public OrthogonalTiledMapRenderer tmr;
	float tileSize;
	OrthographicCamera cam;
	
	public void createTiles() {	
        // load tile map
     //   tileMap = new TmxMapLoader().load("overworld/overworld.tmx");
      //  tmr = new OrthogonalTiledMapRenderer(tileMap);
       // tileSize = tileMap.getProperties().get("tilewidth", Integer.class);
        
        TiledMapTileLayer layer;
        
		//layer = (TiledMapTileLayer) tileMap.getLayers().get("Wasser");
		//createLayer(layer, B2DVars.BIT_GROUND);
    }
    
	private void createLayer(TiledMapTileLayer layer, short bits) {
		
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		
		// go through all the cells in the layer
		for(int row = 0; row < layer.getHeight(); row++) {
			for(int col = 0; col < layer.getWidth(); col++) {
				
				// get cell
				Cell cell = layer.getCell(col, row);
				
				// check if cell exists
				if(cell == null) continue;
				if(cell.getTile() == null) continue;
				
				// create a body + fixture from cell
				bdef.type = BodyType.StaticBody;
				bdef.position.set(
					(col + 0.5f) * tileSize / 100,
					(row + 0.5f) * tileSize / 100
				);
				
				ChainShape cs = new ChainShape();
				Vector2[] v = new Vector2[3];
				v[0] = new Vector2(
					-tileSize / 2 / 100, -tileSize / 2 / 100);
				v[1] = new Vector2(
					-tileSize / 2 / 100, tileSize / 2 / 100);
				v[2] = new Vector2(
					tileSize / 2 / 100, tileSize / 2 / 100);
				cs.createChain(v);
				fdef.friction = 0;
				fdef.shape = cs;
				fdef.filter.categoryBits = bits;
				fdef.isSensor = false;
				world.createBody(bdef).createFixture(fdef);
				
				
			}
		}
	}

	public void update(float dt) {
		world.step(dt, 6, 2);
	}
	
	public void render() {
		
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//tmr.setView(cam);
		//tmr.render();
		world.step(1, 6, 2);
	}
	
	@Override
	public void create() {
		// TODO Auto-generated method stub
		
	}
}
