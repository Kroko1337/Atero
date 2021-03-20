package de.verschwiegener.atero.settings;

import java.util.ArrayList;

public class SettingsItem {
	
	String name, current, parent;
	float minValue, maxValue, currentValue;
	ArrayList<String> modes;
	boolean state;
	Category category;
	
	public SettingsItem(String name, float minValue, float maxValue, float currentValue, String parent) {
		this.name = name;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.currentValue = currentValue;
		this.parent = parent;
		this.category = Category.Slider;
	}
	public SettingsItem(String name, ArrayList<String> modes, String current, String parent) {
		this.name = name;
		this.modes = modes;
		this.current = current;
		this.parent = parent;
		this.category = Category.Combobox;
	}
	public SettingsItem(String name, boolean state, String parent) {
		this.name = name;
		this.state = state;
		this.parent = parent;
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
	public String getParent() {
		return parent;
	}
	
	public enum Category{
		
		Slider, Combobox, Checkbox
		
	}

}
