package handlers;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class MyInputProcessor extends InputAdapter {

	
	/**
	 * called when keyDown
	 * 
	 * @author Tim Killenberger
	 */
	@Override
	public boolean keyDown(int k) {

		if (k == Keys.X) {
			MyInput.setKey(MyInput.BUTTON2, true);
		}
		if (k == Keys.A) {
			MyInput.setKey(MyInput.LEFT, true);
		}
		if (k == Keys.D) {
			MyInput.setKey(MyInput.RIGHT, true);
		}
		return true;
	}
	/**
	 * called when keyUp
	 * 
	 * @author Tim Killenberger
	 */
	@Override
	public boolean keyUp(int k) {

		if (k == Keys.X) {
			MyInput.setKey(MyInput.BUTTON2, false);
		}
		if (k == Keys.A) {
			MyInput.setKey(MyInput.LEFT, false);
		}
		if (k == Keys.D) {
			MyInput.setKey(MyInput.RIGHT, false);
		}
		return true;
	}
}