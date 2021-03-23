package de.verschwiegener.atero.util.chat;

import java.awt.Color;
import java.util.ArrayList;

import de.verschwiegener.atero.Management;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class ChatUtil {
	
	public static void sendMessageWithPrefix(String message) {
		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§1" + Management.instance.CLIENT_NAME + ": §f" + message));
	}
	public static void sendMessage(String message) {
		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
	}
	public static void addIChatComponent(IChatComponent components) {
		Minecraft.getMinecraft().ingameGUI.getChatGUI().addChatLine(components);
	}
	
	public static void addLoadChat2() {
		ArrayList<IChatComponent> components = new ArrayList<>();
		IChatComponent ichatcomponent = new ChatComponentText("Test ");
		ichatcomponent.getChatStyle().setColor(EnumChatFormatting.GREEN);
		ichatcomponent.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.CLIENT, new ChatComponentText("test_test2_test3")));
		ichatcomponent.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.CLIENT, "bind_del_test"));
		components.add(ichatcomponent);
		
		IChatComponent ichatcomponent2 = new ChatComponentText("Test2");
		ichatcomponent2.getChatStyle().setColor(EnumChatFormatting.GREEN);
		ichatcomponent2.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.CLIENT, new ChatComponentText("test_test2_test4")));
		components.add(ichatcomponent2);
		
		IChatComponent test = new ChatComponentText("Test ");
		test.appendSibling(ichatcomponent);
		test.appendSibling(ichatcomponent2);
		
		Minecraft.getMinecraft().ingameGUI.getChatGUI().addChatLine(test);
	}
	public static void addBindsMessage(String module) {
		IChatComponent message = new ChatComponentText("§r" + module + ": ");
		
		IChatComponent componentremove = new ChatComponentText("Remove");
		componentremove.getChatStyle().setColor(EnumChatFormatting.RED);
		componentremove.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.CLIENT, "bind_del_" + module));
		message.appendSibling(componentremove);
		
		Minecraft.getMinecraft().ingameGUI.getChatGUI().addChatLine(message);
	}

}
