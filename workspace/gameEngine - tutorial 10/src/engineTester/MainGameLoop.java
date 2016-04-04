package engineTester;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.OBJLoader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer(shader);
				
		RawModel model = OBJLoader.loadObjModel("stall", loader);
		
		TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("stallTexture")));
		
		Entity entity = new Entity(staticModel, new Vector3f(0,0,-50),0,0,0,1);
		
		Camera camera = new Camera();
		
		while(!Display.isCloseRequested()) {
			entity.increaseRotation(0, 1, 0);
			camera.move();
			renderer.prepare();
			//game logic
			shader.start();
			shader.loadViewMatrix(camera);
			renderer.render(entity, shader);
			shader.stop();
			DisplayManager.updateDisplay();
		}

		shader.cleanUp();
		loader.cleanUp();
		
		DisplayManager.closeDisplay();
	}

}
