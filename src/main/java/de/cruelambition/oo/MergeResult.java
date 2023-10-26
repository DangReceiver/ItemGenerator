package de.cruelambition.oo;

import de.cruelambition.itemgenerator.ItemGenerator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

public class MergeResult {
	private final ItemStack item;

	private int extraRepairCost;

	private static FileConfiguration c = ItemGenerator.getItemGenerator().getConfig();

	public MergeResult(ItemStack item) {
		this.item = item;
		this.extraRepairCost = 0;
	}

	public ItemStack getItem() {
		return this.item;
	}

	public int getExtraRepairCost() {
		return this.extraRepairCost;
	}

	public void addRepairCost(int cost) {
		this.extraRepairCost += cost;
	}


	public static boolean mergeBookOnBook() {
		return c.getBoolean("merge_book_on_book");
	}

	public static boolean mergeBookOnItem() {
		return c.getBoolean("merge_book_on_item");
	}

	public static boolean mergeItemOnItem() {
		return c.getBoolean("merge_item_on_item");
	}
}
