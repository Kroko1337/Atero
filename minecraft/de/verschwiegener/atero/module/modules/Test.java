package de.verschwiegener.atero.module.modules;

import org.lwjgl.input.Keyboard;

import de.verschwiegener.atero.module.Category;
import de.verschwiegener.atero.module.Module;

public class Test extends Module {

	public Test() {
		super("Test", "Test", Keyboard.KEY_NONE, Category.Combat);
	}

}
