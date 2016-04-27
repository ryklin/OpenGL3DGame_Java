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
import toolbox.MousePicker;

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
		TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader), new ModelTexture(loader.loadTexture("grassTexture")));
		TexturedModel flower = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader), new ModelTexture(loader.loadTexture("flower")));
		TexturedModel box = new TexturedModel(OBJLoader.loadObjModel("box", loader), new ModelTexture(loader.loadTexture("box")));
		TexturedModel bobble = new TexturedModel(OBJLoader.loadObjModel("lowPolyTree", loader), new ModelTexture(loader.loadTexture("lowPolyTree")));
		TexturedModel lamp = new TexturedModel(OBJLoader.loadObjModel("lamp", loader), new ModelTexture(loader.loadTexture("lamp")));

		ModelTexture fernTexture = new ModelTexture(loader.loadTexture("fern"));
		fernTexture.setNumberOfRows(2);
		TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader), fernTexture);


		tree.getTexture().setReflectivity(0);
		grass.getTexture().setHasTransparency(true);
		grass.getTexture().setUseFakeLighting(true);
		grass.getTexture().setReflectivity(0);
		flower.getTexture().setHasTransparency(true);
		flower.getTexture().setUseFakeLighting(true);
		flower.getTexture().setReflectivity(0);
		fern.getTexture().setHasTransparency(true);
		fern.getTexture().setUseFakeLighting(false);
		fern.getTexture().setReflectivity(0);
		
		lamp.getTexture().setReflectivity(0);
		lamp.getTexture().setUseFakeLighting(true);
		lamp.getTexture().setHasTransparency(true);

		List <Light> lights = new ArrayList<Light>();

		List<Entity> entities = new ArrayList<Entity>();

		float x=0, y=0, z=0;


		Random random = new Random();

		for (int i = 0; i < 100; i++) {
			if (i % 7 == 0) {
				x = random.nextFloat() * 800;
				z = random.nextFloat() * -600;
				y = terrain.getHeightOfTerrain(x, z);

				entities.add(new Entity(bobble, new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, 0.9f));
				
				entities.add(new Entity(fern, random.nextInt(4), new Vector3f(x-20, y, z), 0, random.nextFloat() * 360, 0, 1.5f));

				entities.add(new Entity(grass, new Vector3f(x, y, z), 0, 0, 0, 1.8f));

				entities.add(new Entity(flower, new Vector3f(x, y, z), 0, 0, 0, 2.3f));
			}

			if (i % 3 == 0) {
				x = random.nextFloat() * 800;
				z = random.nextFloat() * -600;
				y = terrain.getHeightOfTerrain(x, z);
				
				entities.add(new Entity(tree, new Vector3f(x, y, z), 0, 0, 0, random.nextFloat() * 1 + 1));

				x = random.nextFloat() * 800;
				z = random.nextFloat() * -600;
				y = terrain.getHeightOfTerrain(x, z)+5;

				entities.add(new Entity(box, new Vector3f(x, y, z), 0, 0, 0, random.nextFloat() * 1 + 4));

			}

		}

		lights.add(new Light(new Vector3f(0,1000,-7000), new Vector3f(0.4f,0.4f,0.4f))); // main sun light

		x = 400;
		z = -490;
		y = terrain.getHeightOfTerrain(x, z);
		float lampLight_yOffset = 15; // this is approximately the height of the lamp post which we add to the height of the light
		
		entities.add(new Entity(lamp, new Vector3f(x, y, z), 0, 0, 0, 1));
		lights.add(new Light(new Vector3f(x, y+lampLight_yOffset, z), new Vector3f(2, 0, 0), new Vector3f(1.0f, 0.01f, 0.002f)));

		x = +490;
		z = -400-60;
		y = terrain.getHeightOfTerrain(x, z);
		entities.add(new Entity(lamp, new Vector3f(x, y, z), 0, 0, 0, 1));
		lights.add(new Light(new Vector3f(x, y+lampLight_yOffset, z), new Vector3f(2, 2, 0), new Vector3f(1.0f, 0.01f, 0.002f)));

		x = +490;
		z = -350;
		y = terrain.getHeightOfTerrain(x, z);
		
		Entity lampEntity = new Entity(lamp, new Vector3f(x, y, z), 0, 0, 0, 1); 
		entities.add(lampEntity);
		Light light = new Light(new Vector3f(x, y+lampLight_yOffset, z), new Vector3f(0, 2, 0), new Vector3f(1.0f, 0.01f, 0.002f)); 
		lights.add(light);

		MasterRenderer renderer = new MasterRenderer(loader);
	
		TexturedModel avatar = new TexturedModel(OBJLoader.loadObjModel("player",  loader), new ModelTexture(loader.loadTexture("playerTexture")));
		
		Player player = new Player(avatar, new Vector3f(400,0,-400), 0,180,0,1);
		Camera camera = new Camera(player);
		
		MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);
		
		while(!Display.isCloseRequested()) {
			camera.move();
			player.move(terrain);
			picker.update();
			Vector3f terrainPoint = picker.getCurrentTerrainPoint();
			if (terrainPoint != null){
				lampEntity.setPosition(terrainPoint);;
				light.setPosition(new Vector3f(terrainPoint.x, terrainPoint.y+lampLight_yOffset, terrainPoint.z));
			}
//			System.out.println(picker.getCurrentRay());
			renderer.processEntity(player);
			renderer.processTerrain(terrain);
			
			for (Entity entity : entities){
				renderer.processEntity(entity);
			}
			renderer.render(lights, camera);
			
			DisplayManager.updateDisplay();
		}

		renderer.cleanUp();
		loader.cleanUp();
		
		DisplayManager.closeDisplay();
	}

}
