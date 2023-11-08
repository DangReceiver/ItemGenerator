package de.cruelambition.oo;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Items {

	// cmd = CustomModelData
	private int cmd = 1, cmdi = 1, cmde = 1, cmdh = 1, cmdd = 1;

	public static List<ItemStack> ITEMS = new ArrayList<>();
	public static List<Material> mats = new ArrayList<>();
	public static int[] amount;

	public Items() {
		List<ItemStack> l = new ArrayList<>();

		ItemStack mini_jetpack = newItem("§6Mini Jetpack", "Click to be boosted " +
				"in the air every time you click"),
				sound = newItem("§eSound", "Click to produce a sound"),
				eraser = newItem("§cEraser", "click to remove a set of blocks"),
				crate = newHeadItem("§cItem Crate", "Click to roll the lucky wheel"),
				banana = newEdibleItem("§eBanana", "§e§oBanana!"),
				bakedBanana = newEdibleItem("§eBaked Banana", "§e§o Baked Banana o:"),
				disc = newDisc("§5Phantom - wavvyboi", "§eSpiele ein Lied 💞");

		l.add(mini_jetpack);    // 0
		l.add(sound);           // 1
		l.add(eraser);          // 2
		l.add(crate);           // 3
		l.add(banana);          // 4
		l.add(bakedBanana);     // 5
		l.add(disc);            // 6

		for (ItemStack is : l) if (!mats.contains(is.getType())) mats.add(is.getType());
		amount = new int[mats.size()];

		for (int i = 0; i < mats.size(); i++) {
			amount[i] = amount[i] + 1;
			i++;
		}

		ITEMS.addAll(l);
//		for (Player ap : Bukkit.getOnlinePlayers()) for (ItemStack is : l) ap.getInventory().addItem(is);
	}

	public static ItemStack getCustomItem(Material m, int cmd) {
		int i = 0;
		for (ItemStack item : ITEMS) {

			if (item.getType() != m) continue;
			i++;

			if (i == cmd) return item;
		}
		return new ItemStack(m);
	}

	public ItemStack newItem(String name, String lore) {
		ItemStack customItem = new ItemStack(Material.PAPER);

		IB.name(customItem, name);
		IB.lore(customItem, lore);
		IB.cmd(customItem, cmd);

		cmd++;
		return customItem;
	}

	public ItemStack newDamageableItem(String name, String lore) {
		ItemStack customItem = new ItemStack(Material.IRON_HOE);

		IB.name(customItem, name);
		IB.lore(customItem, lore);
		IB.cmd(customItem, cmdi);

		cmdi++;
		return customItem;
	}

	public ItemStack newEdibleItem(String name, String lore) {
		ItemStack customItem = new ItemStack(Material.APPLE);

		IB.name(customItem, name);
		IB.lore(customItem, lore);
		IB.cmd(customItem, cmde);

		cmde++;
		return customItem;
	}

	public ItemStack newHeadItem(String name, String lore) {
		ItemStack customItem = new ItemStack(Material.PLAYER_HEAD);

		IB.name(customItem, name);
		IB.lore(customItem, lore);
		IB.cmd(customItem, cmdh);

		cmdh++;
		return customItem;
	}

	public ItemStack newDisc(String name, String lore) {
		ItemStack customItem = new ItemStack(Material.MUSIC_DISC_CAT);

		IB.name(customItem, name);
		IB.lore(customItem, lore);
		IB.cmd(customItem, cmdd);

		cmdd++;
		return customItem;
	}
}
