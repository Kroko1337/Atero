package de.verschwiegener.atero.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Predicate;

import de.verschwiegener.atero.design.Design;
import de.verschwiegener.atero.module.modules.Test;
import de.verschwiegener.atero.module.modules.combat.Killaura;
import de.verschwiegener.atero.module.modules.combat.Target;
import de.verschwiegener.atero.module.modules.render.ClickGui;
import net.minecraft.client.Minecraft;

public class ModuleManager {
	
	public ArrayList<Module> modules = new ArrayList();
	
	public ModuleManager() {
		modules.add(new Test());
		modules.add(new ClickGui());
		modules.add(new Killaura());
		modules.add(new Target());
	}
	
	public Module getModuleByName(final String name) {
		return modules.stream().filter(new Predicate<Module>() {
			@Override
			public boolean test(Module module) {
				return module.getName().toLowerCase().equalsIgnoreCase(name.toLowerCase());
			}
		}).findFirst().orElse(null);
	}
	
	public Module getModulebyStartsWith(final String name) {
		return modules.stream().filter(new Predicate<Module>() {
			@Override
			public boolean test(Module module) {
				
				return module.getName().toLowerCase().startsWith(name.toLowerCase());
			}
		}).findFirst().orElse(null);
	}
	
	public void onKey(int key) {
		if(Minecraft.getMinecraft().currentScreen == null) {
			for(Module m : modules) {
				if(m.getKey() == key) {
					m.toggle();
				}
			}
		}
	}
	public void onUpdate() {
		for(Module m : modules) {
			if(m.isEnabled()) {
				m.onUpdate();
			}
		}
	}
	public ArrayList<Module> getModules() {
		return modules;
	}

}
