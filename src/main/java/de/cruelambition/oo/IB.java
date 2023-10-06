package de.cruelambition.oo;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class IB {
	public IB() {
	}

	public static ItemStack getFiller(Material m, boolean def, boolean glint, String name, String lore) {
		ItemStack i = new ItemStack(m);
		if (!def) {
			lore(name(i, name), lore);
		} else {
			lore(name(i, " §0 "), " §0 ");
		}

		if (glint) {
			flag(ench(i, Enchantment.DURABILITY, 0), ItemFlag.HIDE_ENCHANTS);
		}

		return i;
	}

	public static ItemStack lore(ItemStack item, String... lore) {
		ItemMeta itemM = item.getItemMeta();
		itemM.setLore(Arrays.asList(lore));
		item.setItemMeta(itemM);
		return item;
	}

	public static boolean loreContains(ItemStack item, String s) {
		return item.hasItemMeta() && item.getItemMeta().hasLore() && item.getItemMeta().getLore().toString().contains(s);
	}

	public static List<String> getLore(ItemStack item) {
		return item.getItemMeta().getLore();
	}

	public static ItemStack lore(ItemStack item, List<String> lore) {
		ItemMeta itemM = item.getItemMeta();
		itemM.setLore(lore);
		item.setItemMeta(itemM);
		return item;
	}

	public static ItemStack name(ItemStack item, String name) {
		ItemMeta itemM = item.getItemMeta();
		itemM.setDisplayName(name);
		item.setItemMeta(itemM);
		return item;
	}

	public static ItemStack addLore(ItemStack item, List<String> addedLore, List<String> loreBefore) {
		ItemMeta itemM = item.getItemMeta();
		if (loreBefore == null) {
			loreBefore = getLore(item);
		}

		loreBefore.addAll(addedLore);
		itemM.setLore(addedLore);
		item.setItemMeta(itemM);
		return item;
	}

	public static ItemStack ench(ItemStack item, Enchantment ench, int level) {
		ItemMeta itemM = item.getItemMeta();
		itemM.addEnchant(ench, level, true);
		item.setItemMeta(itemM);
		return item;
	}

	public static ItemStack disEnch(ItemStack item) {
		ItemMeta itemM = item.getItemMeta();
		for (Enchantment value : Enchantment.values()) itemM.removeEnchant(value);
		item.setItemMeta(itemM);
		return item;
	}

	public static boolean isEnch(ItemStack item) {
		return item.getItemMeta().hasEnchants();
	}

	public static ItemStack flag(ItemStack item, ItemFlag flag) {
		ItemMeta itemM = item.getItemMeta();
		itemM.addItemFlags(new ItemFlag[] {flag});
		item.setItemMeta(itemM);
		return item;
	}

	public static void invFiller(Inventory in, ItemStack filler) {
		int inventorySize = in.getSize() - 1;

		for (int times = 0; times <= inventorySize; ++times) {
			in.setItem(times, filler);
		}

	}
}
