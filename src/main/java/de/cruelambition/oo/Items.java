package de.cruelambition.oo;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Items {

	// cmd = CustomModelData
	private int cmd = 1;

	public static List<ItemStack> ITEMS = new ArrayList<>();

	public Items() {
		List<ItemStack> l = new ArrayList<>();

		ItemStack mini_jetpack = newItem("§6Mini Jetpack", "Click to bee boosted " +
				"in the air // every time you click"),
				sound = newItem("§eSound", "Click to produce a sound"),
				eraser = newItem("§cEraser", "click to remove a set of blocks"),
				crate = newItem("§cItem Crate", "Click to roll the lucky wheel");

		l.add(mini_jetpack);
		l.add(sound);
		l.add(eraser);
		l.add(crate);

		ITEMS.addAll(l);

//		for (Player ap : Bukkit.getOnlinePlayers()) for (ItemStack is : l) ap.getInventory().addItem(is);
	}

	public ItemStack newItem(String name, String lore) {
		ItemStack customItem = new ItemStack(Material.PAPER);

		IB.name(customItem, name);
		IB.lore(customItem, lore);
		IB.cmd(customItem, cmd);

		cmd++;
		return customItem;
	}
}
