package de.verschwiegener.atero.module.modules.render;

import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.callables.EventPostRender;
import com.darkmagician6.eventapi.events.callables.EventRender2D;

import de.verschwiegener.atero.module.Category;
import de.verschwiegener.atero.module.Module;

public class ESP extends Module {

    public ESP() {
	super("ESP", "ESP", Keyboard.KEY_NONE, Category.Render);
    }

    @EventTarget
    public void onPostRender(EventPostRender render) {
	
    }
    public void onRender2D(EventRender2D event) {
	
    }

}
