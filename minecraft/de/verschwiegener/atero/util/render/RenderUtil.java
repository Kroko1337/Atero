package de.verschwiegener.atero.util.render;

import java.awt.Color;
import java.awt.Point;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Vector;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2f;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.NVFloatBuffer;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class RenderUtil {
	
	static float t = 0;
	static Point[] points = {new Point(200, 200), new Point(300, 300), new Point(200, 500), new Point(600, 500)};
	static ArrayList<Point> points2 = new ArrayList<>();
	static float[] lastpos = new float[2];
	
	/**
	 * Draws a full circle
	 * @param x
	 * @param y
	 * @param diameter
	 * @param fill
	 * @param color
	 */
	public static void drawFullCircle(float x, float y, float diameter, boolean fill, Color color) {
        double twicePi = Math.PI * 2;
        int triageAmount = (int) Math.max(4, diameter * twicePi / 4);
        GL11.glPushMatrix();
        GL11.glPushAttrib(1048575);
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f((float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F,
                (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F);
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);

        for (int i = 0; i <= triageAmount; i++) {
            GL11.glVertex2d(
                    x + (diameter * Math.sin(i * twicePi / triageAmount)),
                    y + (diameter * Math.cos(i * twicePi / triageAmount)));
        }

        GL11.glEnd();
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glColor4d(1.0D, 1.0D, 1.0D, 1.0D);
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }
	
	/**
	 * Draws a Circle
	 * 
	 * @param x
	 * @param y
	 * @param diameter
	 * @param fill
	 * @param color
	 */
	public static void drawStringCircle(float x, float y, float diameter, boolean fill, Color color) {

        double twicePi = Math.PI * 2;

        int triageAmount = (int) Math.max(4, diameter * twicePi / 4);
        
       enable();
       
        GL11.glColor4f((float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F,
                (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F);
        GL11.glBegin(GL11.GL_LINE_LOOP);

        for (int i = 0; i <= triageAmount; i++) {
            GL11.glVertex2d(
                    x + (diameter * Math.sin(i * twicePi / triageAmount)),
                    y + (diameter * Math.cos(i * twicePi / triageAmount)));
        }

        disable();
    }
	
	public static void fillCircle(float x, float y, float diameter, boolean fill, Color color) {

        double twicePi = Math.PI * 2;

        int triageAmount = (int) Math.max(4, diameter * twicePi / 4);
        
       enable();
       
        GL11.glColor4f((float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F,
                (float) color.getBlue() / 255.0F, (float) color.getAlpha() / 255.0F);
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);

        for (int i = 0; i <= triageAmount; i++) {
            GL11.glVertex2d(
                    x + (diameter * Math.sin(i * twicePi / triageAmount)),
                    y + (diameter * Math.cos(i * twicePi / triageAmount)));
        }

        disable();
    }

	/**
	 * Draws a Circle with custom with an height
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param fill
	 * @param color
	 * @deprecated see drawStringCircle or fillCircle
	 */
	private static void drawCustomCircle(float x, float y, float width, float height, boolean fill, Color color) {
		final double twicePi = Math.PI * 2;
		final int triageAmount = (int) Math.max(4.0D, (Math.max(width, height) * twicePi) / 4.0D);
		
		GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F,
				color.getAlpha() / 255.0F);
		GL11.glBegin(fill ? GL11.GL_TRIANGLE_FAN : GL11.GL_LINE_LOOP);

		for (int i = 0; i <= triageAmount; ++i) {
			GL11.glVertex2d(x + (width * Math.cos((i * twicePi) / triageAmount)),
					y + (height * Math.sin((i * twicePi) / triageAmount)));
		}
	}
	public static void enable() {
		GL11.glPushMatrix();
		GL11.glPushAttrib(1048575);
		GL11.glDisable(2929);
		GL11.glDisable(3553);
		GL11.glEnable(2848);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
	}
	public static void disable() {
		GL11.glEnd();
		GL11.glDisable(3042);
		GL11.glEnable(3553);
		GL11.glEnable(2929);
		GL11.glDisable(2848);
		GL11.glDisable(3042);
		GL11.glPopAttrib();
		GL11.glPopMatrix();
	}
	public static void fillRect(int x, int y, int width, int height, Color color) {
		
		enable();
		
		GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
		
		GL11.glBegin(GL11.GL_QUADS);
		
		GL11.glVertex2f(x, y + height);
		GL11.glVertex2f(x + width, y + height);
		GL11.glVertex2f(x + width, y);
		GL11.glVertex2f(x, y);
		
		disable();

	}
	private static void drawCircle(float x, float y, float diameter, int start, int stop, Color color) {
		 double twicePi = Math.PI * 2;
		 
		 
		 GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
		 
        for (int i = start; i <= stop; i++) {
            GL11.glVertex2d(
                    x + (diameter * Math.sin(i * twicePi / stop)),
                    y + (diameter * Math.cos(i * twicePi / stop)));
        }
	}
	
	public static void drawCircleP(float x, float y, float diameter, int start, int stop, Color color) {
		 double twicePi = Math.PI * 2;
		 
		 GL11.glPushMatrix();
		 GL11.glLineWidth(5F);
		GL11.glScaled(0.5, 0.5, 0.5);
		 
		 GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
		 
		 
		 GL11.glBegin(GL11.GL_LINE_LOOP);
       for (int i = start; i <= stop; i++) {
           GL11.glVertex2d(
                   x + (diameter * Math.sin(i * twicePi / stop)),
                   y + (diameter * Math.cos(i * twicePi / stop)));
       }
       GL11.glEnd();
       GL11.glPopMatrix();
       Display.update();
	}

	public static void drawlefthalf(float x, float y, float thicknes, Color color) {
		float radius = 70;
		double twoPI = 2 * Math.PI;

		GL11.glPushMatrix();
		GL11.glScaled(0.5, 0.5, 0.5);
		GL11.glLineWidth(thicknes);
		GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);

		GL11.glBegin(GL11.GL_POINTS);
		
		for (double i = 0; i <= Math.PI; i += 0.001) {
			GL11.glVertex2d(x + (Math.sin(i) * radius), y + (Math.cos(i) * radius));
		}
		GL11.glEnd();
		GL11.glPopMatrix();
		//GL11.glFlush();
	}
	
	
	/**
	 * Draws a Rect with roundet edges after this principle 
	 * https://stackoverflow.com/questions/5369507/opengl-es-1-0-2d-rounded-rectangle
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param diameter
	 * @param color
	 */
	public static void drawRectRound(int x, int y, int width, int height, int diameter, Color color) {
		final double twicePi = Math.PI * 2;
		
		fillRect(x - diameter, y - diameter, width - (diameter *2), height- (diameter * 2), color);

		fillRect(x, y - (diameter * 2), width - (diameter * 4), diameter, color);

		fillRect(x, y + height - (diameter * 3), width - (diameter * 4), diameter, color);

		enable();
		
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		drawCircle(x, y + height - (diameter * 3), diameter, 0, 360, color);
		drawCircle(x, y - diameter, diameter, 0, 360, color);
		drawCircle(x + width - (diameter * 4), y - diameter, diameter, 0, 360, color);
		drawCircle(x + width - (diameter * 4), y + height - (diameter * 3), diameter, 0, 360, color);
		disable();
	}
	
	public static void drawBezierCurve() {
		float precision = 0.01F;
		
		if(t >= 1) {
			t = 0;
			points2.clear();
		}
		t += 0.01F;
		
		besierCurvePixel(t, points);
		
		//Wenn das nicht animiert sein soll
        //while(t <= 1){
			//besierCurvePixel(t, points);
        	//t += precision;
       // }
		
		//disable();
	}
		
	private static void besierCurvePixel(float t, Point[] points) {

		double bPoly[] = new double[points.length];

		for (int i = 0; i < points.length; i++) {
			bPoly[i] = bernstein(t, points.length - 1, i + 1);
		}

		float sumX = 0;
		float sumY = 0;

		for (int i = 0; i < points.length; i++) {
			sumX += bPoly[i] * points[i].x;
			sumY += bPoly[i] * points[i].y;
		}

		int x, y;
		x = (int) Math.round(sumX);
		y = (int) Math.round(sumY);
		enable();
		GL11.glScaled(0.25f, 0.25f, 0.25f);
		GL11.glLineWidth(5F);
		// Smoth Line
		// GL11.glEnable(GL11.GL_LINE_SMOOTH);
		// GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		// GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);

		GL11.glBegin(GL11.GL_LINES);
		for (int i = 0; i < points2.size(); i++) {
			if (points2.size() > 2) {
				if ((i - 1) > 0) {
					GL11.glVertex2f(points2.get(i - 1).x, points2.get(i - 1).y);
					GL11.glVertex2f(points2.get(i).x + 1, points2.get(i).y + 1);
				}
			}
		}
		disable();
		points2.add(new Point(x, y));

	}

	private static int fact(int n) {
		int fact = 1;
		for (int i = 1; i <= n; i++) {
			fact *= i;
		}
		return fact;
	}

	private static double bernstein(float t, int n, int i) {

		return (fact(n) / (fact(i) * fact(n - i))) * Math.pow(1 - t, n - i) * Math.pow(t, i);
	}
	

}
