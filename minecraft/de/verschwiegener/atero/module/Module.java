package de.verschwiegener.atero.module;

import com.darkmagician6.eventapi.EventManager;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.util.Wrapper;

public abstract class Module implements Wrapper{

	Category category;
	boolean enabled;
	int key;
	public final Minecraft mc = Minecraft.getMinecraft();

	String name, description;

	/**
	 * @param name
	 * @param description
	 * @param key
	 * @param category
	 */
	public Module(String name, String description, int key, Category category) {
		this.name = name;
		this.description = description;
		this.key = key;
		this.category = category;
		
		setup();
	}

	public Category getCategory() {
		return category;
	}

	public String getDescription() {
		return description;
	}

	public int getKey() {
		return key;
	}

	public String getName() {
		return name;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
	    this.enabled = enabled;
	}

	public void setKey(int key) {
		this.key = key;
	}
	public void setup() {}
	public void onRender() {}

	public void onDisable() {
		EventManager.unregister(this);
	}

	public void onEnable() {
		EventManager.register(this);
	}

	public void onUpdate() {}

	public void toggle() {
	    enabled = !enabled;
	    if (enabled)
		onEnable();
	    else
		onDisable();
	    Management.instance.modulechange = true;
	}
	public void toggle(boolean state) {
	    enabled = state;
	    if (enabled)
		onEnable();
	    else
		onDisable();
	    Management.instance.modulechange = true;
	}

}
