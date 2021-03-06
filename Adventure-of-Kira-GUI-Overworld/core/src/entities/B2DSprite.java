package entities;

import handlers.Animation;
import states.Play;
import backup.B2DVars;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class B2DSprite extends TextureRegion {
	
	protected Body body;
	protected Animation animation;
	protected float width;
	protected float height;
	protected Play play;


	
	public B2DSprite(Body body) {
		this.body = body;
		animation = new Animation();
	}
	/**
	 * set the Animation
	 * 
	 * @author Tim Killenberger
	 */
	public void setAnimation(TextureRegion[] sprites, float delay) {
		animation.setFrames(sprites, delay);
		width = sprites[0].getRegionWidth();
		height = sprites[0].getRegionHeight();
	}
	
	public void update(float dt) {
	animation.update(dt);	
	}
	
	/**
	 * animate the player
	 * 
	 * @author Tim Killenberger
	 */
	public void render(SpriteBatch sb) {
		sb.begin();
	if(body.getUserData().toString().contains("Player") == true) {
		sb.draw(animation.getFrame(), body.getPosition().x * B2DVars.PPM, body.getPosition().y *B2DVars.PPM - 15, animation.getFrame().getRegionWidth(), animation.getFrame().getRegionHeight());
	} else if (body.getUserData().toString().contains("Coin") == true) {
		sb.draw(animation.getFrame(), body.getPosition().x * B2DVars.PPM - 20, body.getPosition().y *B2DVars.PPM - 15, animation.getFrame().getRegionWidth() * 2, animation.getFrame().getRegionHeight() * 2);
	} else if (body.getUserData().toString().contains("Water") == true) {
		sb.draw(animation.getFrame(), body.getPosition().x * B2DVars.PPM - 20, body.getPosition().y *B2DVars.PPM - 15, animation.getFrame().getRegionWidth() * 2, animation.getFrame().getRegionHeight() * 2);
	} else {
		System.out.println("Fehler");
	}
	sb.end();

}

	
	public Body getBody() {
		return body;
	}
	
	public Vector2 getPosition() {
		return body.getPosition();
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
}