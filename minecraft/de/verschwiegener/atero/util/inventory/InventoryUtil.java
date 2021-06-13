package de.verschwiegener.atero.util.inventory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;

public class InventoryUtil {
    
    //private static Integer[][] inventory = new Integer[2][36];
    static Minecraft mc = Minecraft.getMinecraft();
    //public static ArrayList<Integer> trashInventory = new ArrayList<>();
    private static ArrayList<Group> groups = new ArrayList<>();
    public static int count;
    public static int count2;
    
    public static void addGroups() {
	groups.add(new Group("Blocks", 0, 3, 0, new Integer[] {1, 2, 3, 4, 5, 24, 35, 41, 42, 45, 48, 46, 57, 79, 80, 95}));
	groups.add(new Group("Weapon", 1, 1, 1, new Integer[] {267, 268, 272, 276, 283}));
	groups.add(new Group("Helmet", 2, 0, 1, new Integer[] {298,302,306,310,314}));
	groups.add(new Group("Leggings", 2, 0, 1, new Integer[] {304, 308, 312, 316}));
	groups.add(new Group("Chestplate", 2, 0, 1, new Integer[] {303, 307, 311, 315}));
	groups.add(new Group("Boots", 2, 0, 1, new Integer[] {301, 305,309,313,317}));
	groups.add(new Group("GApple", 0, 9, 0, new Integer[] {322, 317}));
	
    }

    public static ArrayList<Integer> getGarbageItems(boolean onlyhotbar) {
	groups.forEach(group -> group.reset());
	ArrayList<Integer> garbage = new ArrayList<>();
	int startvalue = (onlyhotbar ? 36 : 0);
	int maxValue = (onlyhotbar ? 45 : mc.thePlayer.inventoryContainer.inventorySlots.size());
	for (int i = startvalue; i < maxValue; i++) {
	    ItemStack item = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
	    if (item != null) {
		int id = Item.getIdFromItem(item.getItem());
		boolean trash = true;
		for (Group g : groups) {
		    if (g.getItemIDs().contains(id)) {
			g.getInventoryIDs().add(i);
			trash = false;
		    }
		}
		if (trash) {
		    garbage.add(i);
		}
	    }
	}
	for (Group g : groups) {
	    garbage.addAll(g.getBest());
	}
	return garbage;
    }
    
    public static ArrayList<Integer> getneedItems(ContainerChest chest, boolean intelligent) {
	groups.forEach(group -> group.reset());
	ArrayList<Integer> need = new ArrayList<>();
	for(int i = 0; i < chest.getLowerChestInventory().getSizeInventory();i++) {
	    ItemStack item = chest.getLowerChestInventory().getStackInSlot(i);
	    if (item != null) {
		if (intelligent) {
		    int id = Item.getIdFromItem(item.getItem());
		    for (Group g : groups) {
			if (g.getItemIDs().contains(id) && !need.contains(i)) {
			    need.add(i);
			}
		    }
		}else {
		    need.add(i);
		}
	    }
	}
	return need;
    }
    
    public static boolean compareItem(ItemStack itemchest) {
	groups.forEach(group -> group.reset());
	int startvalue = 0;
	int maxValue = mc.thePlayer.inventoryContainer.inventorySlots.size();
	for (int i = startvalue; i < maxValue; i++) {
	    int id = 0;
	    ItemStack item = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
	    if (item != null) {
		for (Group g : groups) {
		    if (g.getItemIDs().contains(Item.getIdFromItem(item.getItem()))) {
			g.getInventoryIDs().add(i);
		    }
		}
	    }
	}
	boolean needItem = false;
	for(Group g : groups) {
	    System.out.println("Item: " + Item.getIdFromItem(itemchest.getItem()));
	    if(g.getItemIDs().contains(Item.getIdFromItem(itemchest.getItem()))) {
		if(g.compareBestItems(itemchest)) {
		   needItem = true; 
		}
	    }
	}
	return needItem;
    }
    
    public static void putArmor(boolean hotbar) {
	count++;
	if(count + 1 > groups.size()) count = 0;
	
	groups.get(count).putArmor(hotbar);
    }
    public static void putInBestSlot(boolean hotbar) {
	count2++;
	if(count2 + 1 > groups.size()) count2 = 0;
	
	groups.get(count2).putInBestSlot(hotbar);
    }
    

}
