package de.verschwiegener.atero.util.splashscreen;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import de.verschwiegener.atero.util.TimeUtils;
import de.verschwiegener.atero.util.splashscreen.splashscreen.RenderCommand;

public class BezierCurveRenderer {
	
	float t = 0;
	Point[] points;
	ArrayList<Point> points2 = new ArrayList<>();
	TimeUtils timer = new TimeUtils();
	
	private void setValues(Point[] points) {
		points2.clear();
		this.points = points;

	}
	public void setValues(RenderCommand rc) {
		t = 0;
		setValues(rc.getPoints());
	}
	
	
	public void drawBezierCurveAnimated(Color color, float thicknes) {
		float precision = 0.01F;
		
		if(t > 1) {
			points2.clear();
		}
		t += 0.01F;
		while(t <= 1){
			if(timer.hasReached(10)) {
				System.out.println("Draw");
				timer.reset();
				besierCurvePixel(t, points, thicknes, color);
				t += precision;
			}
		}
		drawBezierCurve(color, thicknes);
	}
	
	public void drawBezierCurve(Color color, float thicknes) {
		float precision = 0.01F;
		
		if(t >= 1) {
			t = 0;
			points2.clear();
		}
		t += 0.01F;
		
        while(t <= 1){
			besierCurvePixel(t, points, thicknes, color);
        	t += precision;
        }
	}
		
	private void besierCurvePixel(float t, Point[] points, float thicknes, Color color) {

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
		//RenderUtil.enable();
		//GL11.glScaled(0.25f, 0.25f, 0.25f);
		GL11.glPushMatrix();
		
		GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
		GL11.glLineWidth(thicknes);
		// Smoth Line
		// GL11.glEnable(GL11.GL_LINE_SMOOTH);
		// GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		// GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
		
		GL11.glScaled(0.5, 0.5, 0.5);
		
		GL11.glBegin(GL11.GL_LINES);
		//GL11.glColor3d(100, 100, 100);
		for (int i = 0; i < points2.size(); i++) {
			if (points2.size() > 2) {
				if(points2.get(i).x > points[0].x && points2.get(i).y > points[0].y) {
					if ((i - 1) > 0) {
						System.out.println("Points");
						GL11.glVertex2f(points2.get(i - 1).x, points2.get(i - 1).y);
						GL11.glVertex2f(points2.get(i).x + 1, points2.get(i).y + 1);
					}
				}
			}
		}
		GL11.glEnd();
		points2.add(new Point(x, y));
		GL11.glPopMatrix();
		Display.update();
		//RenderUtil.disable();

	}

	private int fact(int n) {
		int fact = 1;
		for (int i = 1; i <= n; i++) {
			fact *= i;
		}
		return fact;
	}

	private double bernstein(float t, int n, int i) {
		return (fact(n) / (fact(i) * fact(n - i))) * Math.pow(1 - t, n - i) * Math.pow(t, i);
	}

}
