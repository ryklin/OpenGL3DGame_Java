package engineTester;

import org.lwjgl.opengl.Display;

import models.RawModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.Renderer;

public class MainGameLoop {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		Renderer renderer = new Renderer();
	
		
		float[] vertices = { // vbo

			//left bottom triangle
			-0.5f, +0.5f, 0.0f, // v0 - x0, y0, z0
			-0.5f, -0.5f, 0.0f, // v1 - x1, y1, z1
			+0.5f, -0.5f, 0.0f, // v2 - x2, y2, z2
			
			+0.5f, -0.5f, 0.0f, // v3 - x3, y3, z3
			+0.5f, +0.5f, 0.0f, // v4 - x4, y4, z4
			-0.5f, +0.5f, 0.0f, // v5 - x5, y5, z5
		};
		
		RawModel model = loader.loadToVAO(vertices);
		
		while(!Display.isCloseRequested()) {	
			renderer.prepare();
			//game logic
			renderer.render(model);
			DisplayManager.updateDisplay();
		}

		loader.cleanUp();
		
		DisplayManager.closeDisplay();
	}

}
