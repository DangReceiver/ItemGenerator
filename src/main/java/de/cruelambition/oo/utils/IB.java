package de.cruelambition.oo.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class IB {
	public IB() {
	}

	public boolean hasMaterial(Material m, Inventory in) {
		for (int t = 0; t < in.getSize(); ++t)
			if (in.getItem(t) != null && in.getItem(t).getType() == m) return true;
		return false;
	}

	public static void singleAdds(ItemStack item, Inventory inv, int amount) {
		for (int times = 0; times < amount; ++times) inv.addItem(item);
	}

	public static int getMaterialAmount(final Material mat, final Inventory in) {
		int amount = 0;
		for (int t = 0; t < in.getSize(); ++t)

			if (in.getItem(t) != null && in.getItem(t).getType() == mat)
				amount += in.getItem(t).getAmount();
		return amount;
	}

	public static void removeItems(final Material mat, int amount, final Inventory inv) {
		ItemStack item = new ItemStack(mat), air = new ItemStack(Material.AIR);

		for (int times = 0; times <= inv.getSize(); ++times) {
			if (!(inv.getItem(times) != null && inv.getItem(times).getType() == mat)) continue;

			if (inv.getItem(times).getAmount() >= amount) {
				final int amo = inv.getItem(times).getAmount() - amount;

				item.setAmount(amo);
				inv.setItem(times, item);
				return;
			}

			if (inv.getItem(times).getAmount() <= amount) {
				amount -= inv.getItem(times).getAmount();
				inv.setItem(times, air);
			}
		}
	}

	public static ItemStack getFiller(Material m, boolean def, boolean glint, String name, String lore) {
		ItemStack i = new ItemStack(m);

		if (!def) lore(name(i, name), lore);
		else lore(name(i, " §0 "), " §0 ");

		if (glint) flag(ench(i, Enchantment.DURABILITY, 0), ItemFlag.HIDE_ENCHANTS);
		return i;
	}

	public static ItemStack lore(ItemStack item, String... lore) {
		ItemMeta itemM = item.getItemMeta();
		itemM.setLore(Arrays.asList(lore));

		item.setItemMeta(itemM);
		return item;
	}

	public static boolean loreContains(ItemStack item, String s) {
		return item.hasItemMeta() && item.getItemMeta().hasLore() &&
				item.getItemMeta().getLore().toString().contains(s);
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

	public static ItemStack cmd(ItemStack item, int cmd) {
		ItemMeta itemM = item.getItemMeta();
		itemM.setCustomModelData(cmd);

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
		if (loreBefore == null) loreBefore = getLore(item);

		loreBefore.addAll(addedLore);
		itemM.setLore(addedLore);
		item.setItemMeta(itemM);

		return item;
	}

	public static ItemStack ench(ItemStack item, Enchantment ench, int level) {
		ItemMeta itemM = item.getItemMeta();

		if (!(itemM instanceof EnchantmentStorageMeta m)) {
			itemM.addEnchant(ench, level, true);
			item.setItemMeta(itemM);

		} else {
			m.addEnchant(ench, level, true);
			item.setItemMeta(m);
		}

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
		itemM.addItemFlags(flag);
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
