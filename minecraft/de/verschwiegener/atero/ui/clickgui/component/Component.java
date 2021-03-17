package de.verschwiegener.atero.ui.clickgui.component;

public class Component {
	
	String name;
	int y;
	PanelExtendet pe;
	
	public Component(String name, int y, PanelExtendet pe) {
		this.name = name;
		this.y = y;
		this.pe = pe;
	}
	
	public String getName() {
		return name;
	}
	public int getY() {
		return y;
	}
	public PanelExtendet getPe() {
		return pe;
	}
	
	public void drawComponent(int x, int y) {}
	public void onMouseClicked(int x, int y, int button) {}

}
