package de.verschwiegener.atero.util.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import net.minecraft.client.Minecraft;

import net.minecraft.client.gui.ScaledResolution;

public class ShaderRenderer {
	private int glProgram;
	
	
	public ShaderRenderer(String fragmentShader, String vertexShader) {
		glProgram = GL20.glCreateProgram();
		initShader(fragmentShader, vertexShader);
	}
	
	private void initShader(String fragmentShader, String vertexShader) {
		int vertex = GL20.glCreateShader(35633);
		int fragmet = GL20.glCreateShader(35632);
		GL20.glShaderSource(vertex, vertexShader);
		GL20.glShaderSource(fragmet, fragmentShader);
		GL20.glValidateProgram(glProgram);
		GL20.glCompileShader(vertex);
		GL20.glCompileShader(fragmet);
		GL20.glAttachShader(glProgram, vertex);
		GL20.glAttachShader(glProgram, fragmet);
		GL20.glLinkProgram(glProgram);
	}
	public void render() {
		
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		
		GL20.glUseProgram(glProgram);
		
		GL20.glUniform2f(GL20.glGetUniformLocation(this.glProgram, "resolution"), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
		
		GL11.glEnable(3042);
	    GL11.glBlendFunc(770, 771);
	    GL11.glBegin(7);
	    GL11.glEnd();
	    GL20.glUseProgram(0);	
	}

}
