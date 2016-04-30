package skybox;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import models.RawModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;

public class SkyboxRenderer {

	private static final float SIZE = 1500f;
	
	private static final float[] VERTICES = {        
	    -SIZE,  SIZE, -SIZE,
	    -SIZE, -SIZE, -SIZE,
	    SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	    -SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE
	};
	
	private static String[] TEXTURE_FILES = {"Skybox/right", "Skybox/left", "Skybox/top", "Skybox/bottom", "Skybox/back", "Skybox/front"};
	private static String[] NIGHT_TEXTURE_FILES = {"Skybox/nightright", "Skybox/nightleft", "Skybox/nighttop", "Skybox/nightbottom", "Skybox/nightback", "Skybox/nightfront"};
	private RawModel cube;
	private int dayTextureID;
	private int nightTextureID;
	private SkyboxShader shader;
	private float time = 0;
	float delay = 0;	
	public SkyboxRenderer(Loader loader, Matrix4f projectionMatrix, float delayTime){
		cube = loader.loadToVAO(VERTICES, 3);
		dayTextureID = loader.loadCubeMap(TEXTURE_FILES);
		nightTextureID = loader.loadCubeMap(NIGHT_TEXTURE_FILES);
		delay = delayTime;
		shader = new SkyboxShader();
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void render(Camera camera, float r, float g, float b){
		shader.start();
		shader.loadViewMatrix(camera);
		shader.loadFogColour(r, g, b);
		GL30.glBindVertexArray(cube.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		bindTextures();
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	private void bindTextures(){
		
		time += DisplayManager.getFrameTimeSeconds() * 1000;
		
		int texture1;
		int texture2;
		float blendFactor;
		
		texture1 = nightTextureID;
		texture2 = dayTextureID;

		blendFactor = 1;//(float) ((float) (Math.sin(Math.toRadians(time/delay))+1.0)/2.0);
				
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture1);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture2);
		shader.loadBlendFactor(blendFactor);
	}
}
