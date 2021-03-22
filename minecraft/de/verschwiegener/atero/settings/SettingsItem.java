package de.verschwiegener.atero.settings;

import java.util.ArrayList;

public class SettingsItem {
	
	String name, current, child, childselect;
	float minValue, maxValue, currentValue;
	ArrayList<String> modes;
	boolean state;
	Category category;
	
	public SettingsItem(String name, float minValue, float maxValue, float currentValue, String child) {
		this.name = name;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.currentValue = currentValue;
		this.child = child;
		this.category = Category.Slider;
	}
	public SettingsItem(String name, ArrayList<String> modes, String current, String child, String childselect) {
		this.name = name;
		this.modes = modes;
		this.current = current;
		this.child = child;
		this.childselect = childselect;
		this.category = Category.Combobox;
	}
	public SettingsItem(String name, boolean state, String child) {
		this.name = name;
		this.state = state;
		this.child = child;
		this.category = Category.Checkbox;
	}
	
	public String getName() {
		return name;
	}
	public Category getCategory() {
		return category;
	}
	public boolean isState() {
		return state;
	}
	public void toggleState() {
		state = !state;
	}
	
	public float getCurrentValue() {
		return currentValue;
	}
	public float getMaxValue() {
		return maxValue;
	}
	public float getMinValue() {
		return minValue;
	}
	public void setCurrentValue(float currentValue) {
		this.currentValue = currentValue;
	}
	public String getChild() {
		return child;
	}
	public ArrayList<String> getModes() {
		return modes;
	}
	public String getCurrent() {
		return current;
	}
	
	public enum Category{
		
		Slider, Combobox, Checkbox
		
	}

}
