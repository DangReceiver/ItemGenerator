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

	private List<ItemStack> items = new ArrayList<>();

	public Items() {
		List<ItemStack> l = new ArrayList<>();

		ItemStack emerald_hoe = newItem("Emerald Hoe", "oki doki"),
				sound = newItem("Sound", "Click to produce a sound"),
				eraser = newItem("Eraser", "click to remove a set of blocks");

		l.add(emerald_hoe);
		l.add(sound);
		l.add(eraser);

		for (Player ap : Bukkit.getOnlinePlayers()) for (ItemStack is : l) ap.getInventory().addItem(is);

	}

	public ItemStack newItem(String name, String lore) {
		ItemStack customItem = new ItemStack(Material.GOLDEN_HOE, 1, (short) damage);
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
