package engineTester;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer(shader);
		
		float[] vertices = { // vbo

			//left bottom triangle
			-0.5f, +0.5f, 0.0f, // v0 - x1, y1, z1
			-0.5f, -0.5f, 0.0f, // v1 - x2, y2, z2
			+0.5f, -0.5f, 0.0f, // v2 - x3, y3, z3
			+0.5f, +0.5f, 0.0f, // v3 - x4, y4, z4
		};
		
		
		int[] indices = {
				0,1,3, // top left triangle (v0, v1, v2)
				3,1,2 // bottom right triangle (v3 v1 v2)
		};
		
		float[] textureCoords = {
				0,0,
				0,1,
				1,1,
				1,0
		};
		
		RawModel model = loader.loadToVAO(vertices, textureCoords, indices);
		ModelTexture texture = new ModelTexture(loader.loadTexture("image"));
		TexturedModel texturedModel = new TexturedModel(model, texture);
		Entity entity = new Entity(texturedModel, new Vector3f(0,0,-1),0,0,0,1);  // warning, do not set the z value to 0 or else the image texture will not be rendered inside the viewing plane because the projectionMatrix has been initialized.
				
		while(!Display.isCloseRequested()) {
			entity.increasePosition(0, 0, -0.1f);
			renderer.prepare();
			//game logic
			shader.start();
			renderer.render(entity, shader);
			shader.stop();
			DisplayManager.updateDisplay();
		}

		shader.cleanUp();
		loader.cleanUp();
		
		DisplayManager.closeDisplay();
	}

}
