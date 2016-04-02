package renderEngine;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import models.RawModel;

public class Loader {
	
	//private List vaos = new ArrayList<Integer>();  // Note : Eclipse Version: Mars.2 Release (4.5.2) reports an error: type mismatch cannot convert from ArrayList<Integer> to List
	private ArrayList<Integer> vaos = new ArrayList<Integer>();
	private ArrayList<Integer> vbos = new ArrayList<Integer>();
	
	public RawModel loadToVAO(float[] positions){
		
		int vaoID = createVAO();
		storeDataInAttributeList(0, positions);
		unbindVAO();
		return new RawModel(vaoID, positions.length/3);
	}
		
	public void cleanUp(){
		for(int vao:vaos){
			GL30.glDeleteVertexArrays(vao);
		}
		
		for(int vbo:vbos){
			GL15.glDeleteBuffers(vbo);
		}
	}
	private int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;		
	}
	
	private void storeDataInAttributeList(int attributeNumber, float[] data)
	{
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, 3, GL11.GL_FLOAT, false, 0,0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	private void unbindVAO(){
		GL30.glBindVertexArray(0);
	}
	private FloatBuffer storeDataInFloatBuffer(float[] data){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

}
