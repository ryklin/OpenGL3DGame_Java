package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
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
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy3"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("mossPath256"));

		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		Terrain terrain = new Terrain(0,0,loader, texturePack, blendMap, "heightMap");
		Terrain terrain2 = new Terrain(1,0,loader,texturePack, blendMap, "heightMap");
		// *****************************************
		
		TexturedModel tree = new TexturedModel(OBJLoader.loadObjModel("tree", loader), new ModelTexture(loader.loadTexture("tree")));
		TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader), new ModelTexture(loader.loadTexture("grassTexture")));
		TexturedModel flower = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader), new ModelTexture(loader.loadTexture("flower")));
		TexturedModel box = new TexturedModel(OBJLoader.loadObjModel("box", loader), new ModelTexture(loader.loadTexture("box")));
		TexturedModel mytree = new TexturedModel(OBJLoader.loadObjModel("mytree", loader), new ModelTexture(loader.loadTexture("tree")));
		TexturedModel bobble = new TexturedModel(OBJLoader.loadObjModel("lowPolyTree", loader), new ModelTexture(loader.loadTexture("lowPolyTree")));
		//TexturedModel lamp = new TexturedModel(OBJLoader.loadObjModel("lamp", loader), new ModelTexture(loader.loadTexture("lamp")));

		ModelTexture fernTexture = new ModelTexture(loader.loadTexture("fern"));
		TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader), fernTexture);


		grass.getTexture().setHasTransparency(true);
		grass.getTexture().setUseFakeLighting(true);
		grass.getTexture().setReflectivity(0);
		flower.getTexture().setHasTransparency(true);
		flower.getTexture().setUseFakeLighting(true);
		flower.getTexture().setReflectivity(0);
		fern.getTexture().setHasTransparency(true);
		fern.getTexture().setUseFakeLighting(true);
		fern.getTexture().setReflectivity(0);
		
		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random();

		for (int i = 0; i < 100; i++) {
			if (i % 7 == 0) {
				float x = random.nextFloat() * 800 - 400;
				float z = random.nextFloat() * -600;
				float y = 0;

				entities.add(new Entity(bobble, new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, 0.9f));
				
				entities.add(new Entity(fern, new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, 1.5f));

				entities.add(new Entity(grass, new Vector3f(x, y, z), 0, 0, 0, 1.8f));

				entities.add(new Entity(flower, new Vector3f(x, y, z), 0, 0, 0, 2.3f));
			}

			if (i % 3 == 0) {
				float x = random.nextFloat() * 800 - 400;
				float z = random.nextFloat() * -600;
				float y = 0;

				x = random.nextFloat() * 800 - 400;
				z = random.nextFloat() * -600;
				y = 0;
				
				entities.add(new Entity(tree, new Vector3f(x, y, z), 0, 0, 0, random.nextFloat() * 1 + 4));

				x = random.nextFloat() * 800 - 400;
				z = random.nextFloat() * -600;
				y = 5;
				entities.add(new Entity(box, new Vector3f(x, y, z), 0, 0, 0, random.nextFloat() * 1 + 4));
			}
		}

		Light light = new Light(new Vector3f(20000, 20000, 2000), new Vector3f(1,1,1));
		
		TexturedModel avatar = new TexturedModel(OBJLoader.loadObjModel("player",  loader), new ModelTexture(loader.loadTexture("playerTexture")));
		
		Player player = new Player(avatar, new Vector3f(100,0,-50), 0,180,0,1);
		Camera camera = new Camera(player);
		MasterRenderer renderer = new MasterRenderer();
		
		while(!Display.isCloseRequested()) {
			camera.move();
			player.move();
			renderer.processEntity(player);
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);
			
			for (Entity entity : entities){
				//entity.increaseRotation(0, 1, 0);
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
