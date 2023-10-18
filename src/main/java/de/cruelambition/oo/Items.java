package de.cruelambition.oo;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Items {

	private int damage = 1;

	public static List<ItemStack> ITEMS = new ArrayList<>();

	public Items() {
		List<ItemStack> l = new ArrayList<>();

		ItemStack mini_jetpack = newItem("§6Mini Jetpack", "Click to bee boosted " +
				"in the air // every time you click"),
				sound = newItem("Sound", "Click to produce a sound"),
				eraser = newItem("Eraser", "click to remove a set of blocks"),
				crate = newItem("§cItem Crate", "Click to roll the lucky wheel");

		l.add(mini_jetpack);
		l.add(sound);
		l.add(eraser);
		l.add(crate);

//		for (Player ap : Bukkit.getOnlinePlayers()) for (ItemStack is : l) ap.getInventory().addItem(is);

	}

	public ItemStack newItem(String name, String lore) {
		ItemStack customItem = new ItemStack(Material.GOLDEN_HOE, 1, (short) damage);
		customItem.setDurability((short) damage);

		ItemMeta meta = customItem.getItemMeta();
		IB.name(customItem, name);
		IB.lore(customItem, lore);

		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);

		customItem.setItemMeta(meta);

		if (damage >= customItem.getType().getMaxDurability())
			throw new RuntimeException("Cannot create Item - out of Damage-Space!");

		damage++;
		return customItem;
	}
}
