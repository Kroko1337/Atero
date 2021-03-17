package de.verschwiegener.atero.command.commands;

import java.util.ArrayList;

import de.verschwiegener.atero.Management;
import de.verschwiegener.atero.module.Module;
import de.verschwiegener.atero.util.chat.ChatUtil;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import de.verschwiegener.atero.command.Command;

public class BindsCommand extends Command{

	public BindsCommand() {
		super("Binds", "Lists all Binds", "");
	}
	
	@Override
	public void onCommand(String[] args) {
		ChatUtil.sendMessage("§6------§fBinds §6------");
		for(Module m : Management.instance.modulemgr.getModules()) {
			System.out.println("Key: " + m.getKey());
			if(m.getKey() != 0) {
				ArrayList<IChatComponent> components = new ArrayList<>();
				IChatComponent namecomp = new ChatComponentText(m.getName());
				namecomp.getChatStyle().setColor(EnumChatFormatting.GREEN);
				components.add(namecomp);
				
				IChatComponent bindcomp = new ChatComponentText("Remove");
				bindcomp.getChatStyle().setColor(EnumChatFormatting.RED);
				bindcomp.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.CLIENT, "bind_del_" + m.getName()));
				bindcomp.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.CLIENT, new ChatComponentText("bind_del_" + m.getName())));
				components.add(bindcomp);
				
				ChatUtil.addHoverEvent(components);
			}
		}
	}

}
