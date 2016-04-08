package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		
		RawModel model = OBJLoader.loadObjModel("dragon", loader);
		
		TexturedModel cubeModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("white")));
		ModelTexture texture = cubeModel.getTexture();
		texture.setShineDamper(10);
		texture.setReflectivity(1);
		
		Light light = new Light(new Vector3f(3000, 2000, 3000), new Vector3f(1,1,1));
		Camera camera = new Camera();
		
		List<Entity> allCubes = new ArrayList<Entity>();
		Random random = new Random();
		
		for (int i = 0; i < 200; i++){
			float x = random.nextFloat() * 100 -50;
			float y = random.nextFloat() * 100 -50;
			float z = random.nextFloat() * -300;
			allCubes.add(new Entity(cubeModel, new Vector3f(x,y,z), 
					random.nextFloat() * 180f,
					random.nextFloat() * 180f, 
					random.nextFloat() * 0f, 
					1f));
		}
		
		MasterRenderer renderer = new MasterRenderer();
		
		while(!Display.isCloseRequested()) {
			//entity.increaseRotation(0, 1, 0);
			camera.move();
		
			for (Entity cube : allCubes){
				cube.move();// I added this function, it does not exist in the tutorial
				renderer.processEntity(cube);
			}
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		}

		renderer.cleanUp();
		loader.cleanUp();
		
		DisplayManager.closeDisplay();
	}

}
