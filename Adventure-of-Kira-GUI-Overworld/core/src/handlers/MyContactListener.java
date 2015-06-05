package handlers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class MyContactListener implements ContactListener {

	private int numFootContacts;
	private boolean specialContact;

	public MyContactListener() {
		super();
	}

	/**
	 * called when two fixtures start to collide
	 * 
	 * @author Tim Killenberger
	 */

	public void beginContact(Contact c) {

		Fixture fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();

		if (fa == null || fb == null)
			return;
		if (fa.getUserData() != null && fa.getUserData().equals("ActionBlock")) {
			specialContact = true;
		}
		if (fb.getUserData() != null && fb.getUserData().equals("ActionBlock")) {
			specialContact = true;
		}
	}

	/**
	 * called when two fixtures no longer collide
	 * 
	 * @author Tim Killenberger
	 */
	public void endContact(Contact c) {

		Fixture fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();

		if (fa == null || fb == null)
			return;

		if (fa.getUserData() != null && fa.getUserData().equals("foot")) {
			numFootContacts--;
		}

		if (fb.getUserData() != null && fb.getUserData().equals("foot")) {
			numFootContacts--;
		}
		if (fa.getUserData() != null && fa.getUserData().equals("ActionBlock")) {
			specialContact = false;
		}
		if (fb.getUserData() != null && fb.getUserData().equals("ActionBlock")) {
			specialContact = false;
		}

	}
	/**
	 * check if player is on Ground
	 * 
	 * @author Tim Killenberger
	 */
	public boolean isPlayerOnGround() {
		return numFootContacts > 0;
	}
	/**
	 * check if player is on Level
	 * 
	 * @author Simon Buchli
	 */
	public boolean isPlayerOnSpecialGround() {
		return specialContact;

	}

	public void preSolve(Contact c, Manifold m) {
	}

	public void postSolve(Contact c, ContactImpulse ci) {
	}

}