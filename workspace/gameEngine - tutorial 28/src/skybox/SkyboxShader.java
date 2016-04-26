package skybox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import renderEngine.DisplayManager;
import shaders.ShaderProgram;
import toolbox.Maths;

public class SkyboxShader extends ShaderProgram{

	private static final String VERTEX_FILE = "src/skybox/skyboxVertexShader.txt";
	private static final String FRAGMENT_FILE = "src/skybox/skyboxFragmentShader.txt";
	
	private static final float ROTATE_SPEED = 1f; // 1 degree per second

	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_fogColour;
	private int location_cubeMap1;
	private int location_cubeMap2;
	private int location_blendFactor;
	
	private float rotation = 0; // current rotation
	
	public SkyboxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix){
		super.loadMatrix(location_projectionMatrix, matrix);
	}

	public void loadViewMatrix(Camera camera){
		Matrix4f matrix = Maths.createViewMatrix(camera);
		// in a 4x4 matrix the last column determines the translation
		matrix.m30 = 0;
		matrix.m31 = 0;
		matrix.m32 = 0;
		
		rotation += ROTATE_SPEED * DisplayManager.getFrameTimeSeconds(); // this is how you convert to degrees per second
		Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0,1,0), matrix, matrix);
		
		super.loadMatrix(location_viewMatrix, matrix);
	}
	
	public void loadFogColour(float r, float g, float b){
		
		super.loadVector(location_fogColour,  new Vector3f(r,g,b) ) ;
	}
	
	public void connectTextureUnits(){
		super.loadInt(location_cubeMap1, 0);
		super.loadInt(location_cubeMap2, 1);
	}
	public void loadBlendFactor(float value){
		
		super.loadFloat(location_blendFactor,  value ) ;
	}
	
	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_fogColour = super.getUniformLocation("fogColour");
		location_cubeMap1 = super.getUniformLocation("cubeMap1");
		location_cubeMap2 = super.getUniformLocation("cubeMap2");
		location_blendFactor = super.getUniformLocation("blendFactor");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
