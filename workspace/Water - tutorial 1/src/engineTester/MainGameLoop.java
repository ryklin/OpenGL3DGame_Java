package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;


public class MainGameLoop {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		
		// *********TERRAIN TEXTURE STUFF***********
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("mossPath256"));

		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		Terrain terrain = new Terrain(0,-1,loader, texturePack, blendMap, "heightMap");
		// *****************************************
		
		TexturedModel tree = new TexturedModel(OBJLoader.loadObjModel("pine", loader), new ModelTexture(loader.loadTexture("pine")));

		ModelTexture fernTexture = new ModelTexture(loader.loadTexture("fern"));
		fernTexture.setNumberOfRows(2);
		TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader), fernTexture);

		tree.getTexture().setReflectivity(0);
		fern.getTexture().setHasTransparency(true);
		fern.getTexture().setUseFakeLighting(false);
		fern.getTexture().setReflectivity(0);
		
		List <Light> lights = new ArrayList<Light>();
		
		List<Entity> entities = new ArrayList<Entity>();

		float x=0, y=0, z=0;

		Random random = new Random();

		for (int i = 0; i < 100; i++) {
			if (i % 7 == 0) {
				x = random.nextFloat() * 800;
				z = random.nextFloat() * -600;
				y = terrain.getHeightOfTerrain(x, z);

				entities.add(new Entity(fern, random.nextInt(4), new Vector3f(x-20, y, z), 0, random.nextFloat() * 360, 0, 1.5f));

			}

			if (i % 3 == 0) {
				x = random.nextFloat() * 800;
				z = random.nextFloat() * -600;
				y = terrain.getHeightOfTerrain(x, z);
				
				entities.add(new Entity(tree, new Vector3f(x, y, z), 0, 0, 0, random.nextFloat() * 1 + 1));
			}
		}
		
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		List<GuiTexture> guis = new ArrayList<GuiTexture>();

		//GuiTexture gui = new GuiTexture(loader.loadTexture("socuwan"), new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
		//GuiTexture gui2 = new GuiTexture(loader.loadTexture("thinmatrix"), new Vector2f(0.30f, 0.58f), new Vector2f(0.4f, 0.4f));
		//GuiTexture gui3 = new GuiTexture(loader.loadTexture("health"), new Vector2f(-0.74f, 0.925f), new Vector2f(0.25f, 0.25f));
		//guis.add(gui);
		//guis.add(gui2);
		//guis.add(gui3);
		
		Light sun = new Light(new Vector3f(400,1000,-400), new Vector3f(1.0f,1.0f,1.0f));
		
		lights.add(sun); // main sun light

		TexturedModel avatar = new TexturedModel(OBJLoader.loadObjModel("player",  loader), new ModelTexture(loader.loadTexture("playerTexture")));
		
		Player player = new Player(avatar, new Vector3f(400,0,-400), 0,180,0,1);
		Camera camera = new Camera(player);
		
		MasterRenderer renderer = new MasterRenderer(loader,10);

		while(!Display.isCloseRequested()) {
			camera.move();
			player.move(terrain);


			renderer.renderScene(entities, terrain, lights, camera, player);
			guiRenderer.render(guis);
			DisplayManager.updateDisplay();
		}
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		
		DisplayManager.closeDisplay();
	}

}
