package de.verschwiegener.atero.module;

import java.util.ArrayList;

import de.verschwiegener.atero.module.modules.Test;
import de.verschwiegener.atero.module.modules.combat.Antibots;
import de.verschwiegener.atero.module.modules.combat.Killaura;
import de.verschwiegener.atero.module.modules.combat.Target;
import de.verschwiegener.atero.module.modules.combat.Velocity;
import de.verschwiegener.atero.module.modules.movement.*;
import de.verschwiegener.atero.module.modules.render.CapeManager;
import de.verschwiegener.atero.module.modules.render.ClickGui;
import de.verschwiegener.atero.module.modules.render.ESP;
import de.verschwiegener.atero.module.modules.world.*;
import net.minecraft.client.Minecraft;

public class ModuleManager {

    public ArrayList<Module> modules = new ArrayList();

    public ModuleManager() {
	modules.add(new Test());
	modules.add(new Antibots());
	modules.add(new ClickGui());
	modules.add(new Killaura());
	modules.add(new Target());
	modules.add(new Scaffold());
	modules.add(new Sprint());
	modules.add(new Speed());
	modules.add(new Fly());
	modules.add(new ESP());
	modules.add(new Cheststealer());
	modules.add(new Velocity());
	modules.add(new HighJump());
	modules.add(new CapeManager());
	modules.add(new TargetStrafe());
	modules.add(new Nofall());
	modules.add(new Disabler());
	modules.add(new TEst());
    }

    public Module getModuleByName(final String name) {
	return modules.stream().filter(module -> module.getName().toLowerCase().equalsIgnoreCase(name.toLowerCase()))
		.findFirst().orElse(null);
    }

    public Module getModulebyStartsWith(final String name) {
	return modules.stream().filter(module -> module.getName().toLowerCase().startsWith(name.toLowerCase()))
		.findFirst().orElse(null);
    }

    public ArrayList<Module> getModules() {
	return modules;
    }

    public void onKey(final int key) {
	if (Minecraft.getMinecraft().currentScreen == null) {
	    for (final Module m : modules) {
		if (m.getKey() == key) {
		    m.toggle();
		}
	    }
	}
    }

    public void onRender() {
	for (final Module m : modules) {
	    if (m.isEnabled()) {
		m.onRender();
	    }
	}
    }

    public void onUpdate() {
	for (final Module m : modules) {
	    if (m.isEnabled()) {
		try {
		    m.onUpdate();
		}catch(Exception e) {
		    
		}
	    }
	}
    }

}
