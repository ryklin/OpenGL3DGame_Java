package engineTester;

import org.lwjgl.opengl.Display;

import models.RawModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.Renderer;
import shaders.StaticShader;

public class MainGameLoop {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		Renderer renderer = new Renderer();
		StaticShader shader = new StaticShader();
		
		
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
		
		RawModel model = loader.loadToVAO(vertices, indices);
		
		while(!Display.isCloseRequested()) {	
			renderer.prepare();
			//game logic

			shader.start();
			renderer.render(model);
			shader.stop();
			DisplayManager.updateDisplay();
		}

		shader.cleanUp();
		loader.cleanUp();
		
		DisplayManager.closeDisplay();
	}

}
