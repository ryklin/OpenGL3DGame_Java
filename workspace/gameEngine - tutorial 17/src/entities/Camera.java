package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	private Vector3f position = new Vector3f(0, 5, 0);
	private float pitch = 10;
	private float yaw;
	private float roll;

	public Camera(){}

	public void move(){
		if (Keyboard.isKeyDown(Keyboard.KEY_W)){
			position.z-=0.2f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)){ // I've taken the liberty to add my own zoom in handler
			position.z+=0.2f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)){
			position.x+=0.2f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)){
			position.x-=0.2f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)){
			position.x-=0.2f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_C)){
			position.y-=0.2f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_E)){
			position.y+=0.2f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_U)){
			pitch-=0.2f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_J)){
			pitch+=0.2f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_H)){
			yaw-=0.2f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_K)){
			yaw+=0.2f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_N)){
			roll-=0.2f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_M)){
			roll+=0.2f;
		}
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}

}
