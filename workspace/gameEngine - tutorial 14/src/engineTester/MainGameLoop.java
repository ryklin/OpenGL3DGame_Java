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
import terrains.Terrain;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		
		RawModel model = OBJLoader.loadObjModel("tree", loader);
		
		TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("tree")));
		ModelTexture texture = staticModel.getTexture();
		texture.setShineDamper(10);
		texture.setReflectivity(1);
		
		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random();
		
		for (int i = 0; i < 500; i++){
			entities.add(new Entity(staticModel, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),0,0,0,3));
		}
		Light light = new Light(new Vector3f(20000, 20000, 2000), new Vector3f(1,1,1));
		
		Terrain terrain = new Terrain(0,0,loader,new ModelTexture(loader.loadTexture("grass")));
		Terrain terrain2 = new Terrain(1,0,loader,new ModelTexture(loader.loadTexture("grass")));
		terrain.getTexture().setShineDamper(10);
		terrain.getTexture().setReflectivity(1);
		terrain2.getTexture().setShineDamper(10);
		terrain2.getTexture().setReflectivity(1);
		
		Camera camera = new Camera();
		
	
		
		MasterRenderer renderer = new MasterRenderer();
		
		while(!Display.isCloseRequested()) {
			//entity.increaseRotation(0, 1, 0);
			camera.move();
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);
			
			for (Entity entity : entities){
				entity.move();// I added this function, it does not exist in the tutorial
				renderer.processEntity(entity);
			}
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		}

		renderer.cleanUp();
		loader.cleanUp();
		
		DisplayManager.closeDisplay();
	}

}
