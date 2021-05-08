package de.verschwiegener.atero.module;

import java.util.ArrayList;

import de.verschwiegener.atero.module.modules.Test;
import de.verschwiegener.atero.module.modules.combat.Killaura;
import de.verschwiegener.atero.module.modules.combat.Target;
import de.verschwiegener.atero.module.modules.movement.Fly;
import de.verschwiegener.atero.module.modules.movement.Speed;
import de.verschwiegener.atero.module.modules.movement.Sprint;
import de.verschwiegener.atero.module.modules.render.ClickGui;
import de.verschwiegener.atero.module.modules.render.ESP;
import de.verschwiegener.atero.module.modules.world.Cheststealer;
import de.verschwiegener.atero.module.modules.world.Scaffold;
import net.minecraft.client.Minecraft;

public class ModuleManager {

    public ArrayList<Module> modules = new ArrayList();

    public ModuleManager() {
	modules.add(new Test());
	modules.add(new ClickGui());
	modules.add(new Killaura());
	modules.add(new Target());
	modules.add(new Scaffold());
	modules.add(new Sprint());
	modules.add(new Speed());
	modules.add(new Fly());
	modules.add(new ESP());
	modules.add(new Cheststealer());
    }

    public Module getModuleByName(final String name) {
	return modules.stream().filter(module -> module.getName().toLowerCase().equalsIgnoreCase(name.toLowerCase()))
		.findFirst().orElse(null);
    }

    public Module getModulebyStartsWith(final String name) {
	return modules.stream().filter(module -> module.getName().toLowerCase().startsWith(name.toLowerCase())).findFirst().orElse(null);
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

    public void onUpdate() {
	for (final Module m : modules) {
	    if (m.isEnabled()) {
		m.onUpdate();
	    }
	}
    }

}
