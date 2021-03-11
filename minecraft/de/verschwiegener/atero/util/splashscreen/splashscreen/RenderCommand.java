package de.verschwiegener.atero.util.splashscreen.splashscreen;

import java.awt.Point;

public class RenderCommand {

	RenderType type;
	float x1, y1, x2, y2, steps, diameter, widthoffset, heightoffset;
	Point[] points;
	boolean complete;
	int x = 30;
	int y = 200;

	public RenderCommand(float startx, float starty, float targetx, float targety, float steps) {
		type = RenderType.Line;
		/*this.x1 = startx;
		this.y1 = starty;
		this.x2 = targetx;
		this.y2 = targety;*/
		
		this.x1 = x + (startx * 10);
		this.y1 = y + (starty * 10);
		this.x2 = x + (targetx * 10);
		this.y2 = y + (targety * 10);
		
		float xoffset = x1 -x2;
		float yoffset = y1 - y2;
		
		int root =  (int) Math.round(Math.sqrt(normalize((xoffset * 2) + (yoffset * 2))));
		this.steps = root;
	}
	public RenderCommand(float x, float y, float widthoffset, float heightoffset) {
		type = RenderType.CustomCurve;
		this.x1 = this.x + (x * 10);
		this.y1 = this.y + (y * 10);
		this.widthoffset = widthoffset * 10;
		this.heightoffset = heightoffset * 10;
	}

	public RenderCommand(Point[] points) {
		type = RenderType.Curve;
		Point[] points2 = new Point[points.length];
		for(int i = 0; i < points.length;i++) {
			points2[i] = new Point(x + (points[i].x * 10), y + (points[i].y * 10));
		}
		this.points = points2;
	}
	public RenderCommand(float x, float y, float diameter) {
		type = RenderType.Curve;
		this.x1 = this.x + (x * 10);
		this.y1 = this.y + (y * 10);
		this.diameter = diameter;
	}

	public RenderType getType() {
		return type;
	}

	public float getX1() {
		return x1;
	}

	public float getY1() {
		return y1;
	}

	public float getX2() {
		return x2;
	}

	public float getY2() {
		return y2;
	}

	public Point[] getPoints() {
		return points;
	}
	public float getSteps() {
		return steps;
	}
	public float getHeightoffset() {
		return heightoffset;
	}
	public float getWidthoffset() {
		return widthoffset;
	}
	public void setComplete(boolean complete) {
		this.complete = complete;
	}
	public boolean isComplete() {
		return complete;
	}
	
	public double normalize(double in) {
		if(in < 0) {
			return in /-1;
		}
		return in;
	}

}
