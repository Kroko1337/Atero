package de.verschwiegener.atero.module.modules.combat;



import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.callables.EventCancellable;
import com.darkmagician6.eventapi.events.callables.EventReceivedPacket;
import de.verschwiegener.atero.util.Util;
import net.minecraft.client.Minecraft;

import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import org.lwjgl.input.Keyboard;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.module.Category;
import de.verschwiegener.atero.module.Module;
import de.verschwiegener.atero.util.TimeUtils;
import god.buddy.aot.BCompiler;

import java.awt.*;

public class Velocity extends Module {
    TimeUtils timeUtils;
    public Velocity() {
        super("Velocity", "Velocity", Keyboard.KEY_NONE, Category.Combat);
    }

    public void onEnable() {
        super.onEnable();
    }

    public void onDisable() {
        super.onDisable();
    }
    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @EventTarget
    public void onEvent(EventReceivedPacket ppe) {
        if (this.isEnabled()) {
            try{
                Packet p = ppe.getPacket();
                if (p instanceof S12PacketEntityVelocity) {
                    S12PacketEntityVelocity packet = (S12PacketEntityVelocity) p;
                    if (packet.getEntityID() == Minecraft.thePlayer.getEntityId())
                        ppe.setCancelled(true);
                    System.out.println("ppe.setCancelled(true)");
                }
                if (p instanceof net.minecraft.network.play.server.S27PacketExplosion)
                    ppe.setCancelled(true);


            }catch (NullPointerException e) {

            }
        }
    }
}
