package de.cruelambition.oo;

import de.cruelambition.language.Lang;
import de.cruelambition.language.Language;
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
		List<ItemStack> it = new ArrayList<>();
		Lang l = new Lang(null);
		l.setLocalLanguage(Language.getServerLang());

		ItemStack generator = newItem(l.getString("generator_item_name"), Lang.splitString(l.getString("generator_item_lore"))),
				mini_jetpack = newItem(l.getString("jetpack_item_name"), Lang.splitString("§oClick to be boosted " +
						"in the air every time you click")),
				sound = newItem(l.getString("sound_item_name"), Lang.splitString("§oClick to produce a random sound")),
				eraser = newItem(l.getString("eraser_item_name"), Lang.splitString("§oClick to remove a set of blocks" +
						"Comes with a 3 second delay")),
				crate = newHeadItem(l.getString("crate_item_name"), Lang.splitString("§oClick to roll the lucky wheel")),
				banana = newEdibleItem(l.getString("banana_item_name"), Lang.splitString("§e§oBanana!")),
				bakedBanana = newEdibleItem(l.getString("baked_banana_item_name"), Lang.splitString("§e§o Baked Banana o:")),
				disc = newDisc(l.getString("phantom_wavvy_disc_item_name"), Lang.splitString("§e§oSpiele ein Lied 💞"));

		it.add(generator);       // 0
		it.add(mini_jetpack);    // 1
		it.add(sound);           // 2
		it.add(eraser);          // 3
		it.add(crate);           // 4
		it.add(banana);          // 5
		it.add(bakedBanana);     // 6
		it.add(disc);            // 7

		for (ItemStack is : it) if (!mats.contains(is.getType())) mats.add(is.getType());
		amount = new int[mats.size()];

		for (int i = 0; i < mats.size(); i++) {
			amount[i] = amount[i] + 1;
			i++;
		}

		ITEMS.addAll(it);
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

	public ItemStack newItem(String name, List<String> lore) {
		ItemStack customItem = new ItemStack(Material.PAPER);

		IB.name(customItem, name);
		IB.lore(customItem, lore);
		IB.cmd(customItem, cmd);

		cmd++;
		return customItem;
	}

	public ItemStack newDamageableItem(String name, List<String> lore) {
		ItemStack customItem = new ItemStack(Material.IRON_HOE);

		IB.name(customItem, name);
		IB.lore(customItem, lore);
		IB.cmd(customItem, cmdi);

		cmdi++;
		return customItem;
	}

	public ItemStack newEdibleItem(String name, List<String> lore) {
		ItemStack customItem = new ItemStack(Material.APPLE);

		IB.name(customItem, name);
		IB.lore(customItem, lore);
		IB.cmd(customItem, cmde);

		cmde++;
		return customItem;
	}

	public ItemStack newHeadItem(String name, List<String> lore) {
		ItemStack customItem = new ItemStack(Material.PLAYER_HEAD);

		IB.name(customItem, name);
		IB.lore(customItem, lore);
		IB.cmd(customItem, cmdh);

		cmdh++;
		return customItem;
	}

	public ItemStack newDisc(String name, List<String> lore) {
		ItemStack customItem = new ItemStack(Material.MUSIC_DISC_CAT);

		IB.name(customItem, name);
		IB.lore(customItem, lore);
		IB.cmd(customItem, cmdd);

		cmdd++;
		return customItem;
	}
}
