package entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private float distanceFromPlayer = 1100;
	private float angleAroundPlayer = 45;
	
	private Vector3f position = new Vector3f(0, 50, 0);
	private float pitch = 20;
	private float yaw = 0;
	private float roll;

	private Player player;
	
	public Camera(Player player) {
		this.player = player;
	}

	public void move(){
		calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance);
		this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
	}

	public void invertPitch()
	{
		this.pitch = -pitch;
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
	
	private void calculateCameraPosition(float horizontalDistance, float verticalDistance) {
		float theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		position.y = player.getPosition().y + verticalDistance+10;

		//System.out.println("player position : (" + player.getPosition().x + "," + player.getPosition().y + "," + player.getPosition().z + ") camera distance :" + distanceFromPlayer +  " camera angle : " + angleAroundPlayer + " camera pitch : " + pitch);
	}
	
	private float calculateHorizontalDistance(){
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}

	private float calculateVerticalDistance(){
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}

	private void calculateZoom(){
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel;
		if (distanceFromPlayer < 5) {
			distanceFromPlayer  = 5;
		} else if (distanceFromPlayer > 1100){
			distanceFromPlayer = 1100;
		}
	}
	
	private void calculatePitch(){
		if (Mouse.isButtonDown(0)){
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch -= pitchChange;
//			if (pitch <= 5 && distanceFromPlayer > 35)
//				pitch = 5;
//			else if (pitch > 90)
//				pitch = 90;
		}
	}
	
	private void calculateAngleAroundPlayer(){
		if (Mouse.isButtonDown(0)){
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
		}
	}
}
