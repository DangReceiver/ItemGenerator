package de.cruelambition.listener.function.blocks;

import de.cruelambition.language.Lang;
import de.cruelambition.language.Language;
import de.cruelambition.oo.MergeResult;
import de.cruelambition.oo.MergeType;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class Anvil implements Listener {

	@EventHandler
	public void onAnvilEvent(PrepareAnvilEvent e) {
		if (!(e.getView().getPlayer() instanceof Player p)) return;

		ItemStack leftItem = e.getInventory().getItem(0),
				rightItem = e.getInventory().getItem(1);

		if (leftItem == null || rightItem == null
//				|| e.getResult() == null || e.getResult().getType() == Material.AIR
		) return;

		int mrc = Math.min(2000, e.getInventory().getMaximumRepairCost() / 3);

		e.getInventory().setMaximumRepairCost(mrc);
		MergeResult mergeResult = getMergeResult(leftItem, rightItem);

		if (mergeResult == null) {
			e.setResult(null);
			return;
		}

		p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 0.5f, 0.85f);
		e.setResult(mergeResult.getItem());

		int repairCost = e.getInventory().getRepairCost() + mergeResult.getExtraRepairCost();
		e.getInventory().setRepairCost(repairCost);

		p.sendActionBar(Lang.PRE + Lang.getMessage(Language.getServerLang(), "ignore_too_expensive"));
		p.sendMessage(String.format(Language.getMessage(Language.getServerLang(),
					"can_repair_expensive"), e.getInventory().getMaximumRepairCost()));

//		else if (repairCost >= mrc) {
//			p.sendMessage(Language.getMessage(Language.getServerLang(),
//					"can_not_repair_expensive"));
//			e.setResult(null);
//		}
	}

	private MergeResult getMergeResult(ItemStack leftItem, ItemStack rightItem) {
		return switch (getMergeType(leftItem, rightItem)) {
			case BOOK_ON_BOOK -> mergeEnchantedBooks(leftItem, rightItem);
			case ITEM_ON_ITEM -> mergeEnchantedItems(leftItem, rightItem);
			case BOOK_ON_ITEM -> mergeBookAndItem(leftItem, rightItem);
		};
	}

	private MergeResult mergeEnchantedItems(ItemStack leftItem, ItemStack rightItem) {
		ItemStack resultItem = new ItemStack(leftItem.getType());
		MergeResult mergeResult = new MergeResult(resultItem);

		ItemMeta leftMeta = leftItem.getItemMeta();
		ItemMeta rightMeta = rightItem.getItemMeta();

		addLore(leftMeta, resultItem);
		addAttributes(leftMeta, resultItem);

		leftMeta.getEnchants().forEach((enchant, level) -> {
			int resultLevel = getResultLevel(level.intValue(), enchant, rightMeta.getEnchants());

			mergeResult.addRepairCost(calRepair(enchant, resultLevel) / 2);
			resultItem.addUnsafeEnchantment(enchant, resultLevel);
		});
		rightMeta.getEnchants().forEach((enchant, level) -> {
			if (!resultItem.getEnchantments().containsKey(enchant) &&
					!resultItem.getItemMeta().hasConflictingEnchant(enchant)) {

				mergeResult.addRepairCost(calRepair(enchant, level.intValue()) / 2);
				resultItem.addUnsafeEnchantment(enchant, level.intValue());
			}
		});
		return mergeResult;
	}

	private MergeResult mergeBookAndItem(ItemStack item, ItemStack book) {
		ItemStack resultItem = new ItemStack(item.getType());
		MergeResult mergeResult = new MergeResult(resultItem);

		EnchantmentStorageMeta bookMeta = (EnchantmentStorageMeta) book.getItemMeta();
		addLore(item.getItemMeta(), resultItem);
		addAttributes(item.getItemMeta(), resultItem);

		item.getEnchantments().forEach((enchant, level) -> {
			int resultLevel = getResultLevel(level.intValue(), enchant, bookMeta.getStoredEnchants());

			mergeResult.addRepairCost(calRepair(enchant, resultLevel) / 2);
			resultItem.addUnsafeEnchantment(enchant, resultLevel);
		});
		bookMeta.getStoredEnchants().forEach((enchant, level) -> {
			if (enchant.canEnchantItem(resultItem) && !resultItem.getEnchantments().containsKey(enchant)
					&& !resultItem.getItemMeta().hasConflictingEnchant(enchant)) {

				mergeResult.addRepairCost(calRepair(enchant, level.intValue()) / 2);
				resultItem.addUnsafeEnchantment(enchant, level.intValue());
			}
		});
		return mergeResult;
	}

	private MergeResult mergeEnchantedBooks(ItemStack leftBook, ItemStack rightBook) {
		ItemStack resultItem = new ItemStack(Material.ENCHANTED_BOOK);
		MergeResult mergeResult = new MergeResult(resultItem);

		EnchantmentStorageMeta resultMeta = (EnchantmentStorageMeta) resultItem.getItemMeta(),
				leftMeta = (EnchantmentStorageMeta) leftBook.getItemMeta(),
				rightMeta = (EnchantmentStorageMeta) rightBook.getItemMeta();

		leftMeta.getStoredEnchants().forEach((enchant, level) -> {
			int resultLevel = getResultLevel(level.intValue(), enchant, rightMeta.getStoredEnchants());
			mergeResult.addRepairCost(calRepair(enchant, resultLevel) / 2);
			resultMeta.addStoredEnchant(enchant, resultLevel, true);
		});

		rightMeta.getStoredEnchants().forEach((enchant, level) -> {
			if (!resultMeta.getStoredEnchants().containsKey(enchant) &&
					!resultMeta.hasConflictingStoredEnchant(enchant)) {
				mergeResult.addRepairCost(calRepair(enchant, level.intValue()) / 2);
				resultMeta.addStoredEnchant(enchant, level.intValue(), true);
			}
		});

		resultItem.setItemMeta(resultMeta);
		return mergeResult;
	}

	private int getResultLevel(int value, Enchantment enchant, Map<Enchantment, Integer> storedEnchants) {
		int resultLevel = value;

		if (storedEnchants.containsKey(enchant)
//				&& enchant.getMaxLevel() != 1
		) {
			int rightLevel = (storedEnchants.get(enchant)).intValue();

			if (rightLevel == value)
				resultLevel++;

			else if (rightLevel > value)
				resultLevel = rightLevel;
		}

		int enchantLimitation = 9;
		return (enchantLimitation == -1) ? resultLevel : Math.min(resultLevel, enchantLimitation);
	}

	private MergeType getMergeType(ItemStack leftItem, ItemStack rightItem) {
		if (leftItem.getType() != Material.ENCHANTED_BOOK && rightItem.getType() != Material.ENCHANTED_BOOK)
			return MergeType.ITEM_ON_ITEM;

		if (leftItem.getType() == Material.ENCHANTED_BOOK && rightItem.getType() == Material.ENCHANTED_BOOK)
			return MergeType.BOOK_ON_BOOK;

		return MergeType.BOOK_ON_ITEM;
	}

	private int calRepair(Enchantment enchant, int level) {
		return (int) (level <= enchant.getMaxLevel() ? 0 : (level - enchant.getMaxLevel() * 0.5));
	}

	private void addLore(ItemMeta leftMeta, ItemStack resultItem) {
		if (!leftMeta.hasLore()) return;
		ItemMeta resultMeta = resultItem.getItemMeta();

		resultMeta.setLore(leftMeta.getLore());
		resultItem.setItemMeta(resultMeta);
	}

	private void addAttributes(ItemMeta leftMeta, ItemStack resultItem) {
		if (!leftMeta.hasAttributeModifiers()) return;
		ItemMeta resultMeta = resultItem.getItemMeta();

		resultMeta.setAttributeModifiers(leftMeta.getAttributeModifiers());
		resultItem.setItemMeta(resultMeta);
	}

	private boolean hasEnhancedEnchant(ItemStack item) {
		boolean isEnhanced = false;

		if (item.getType() == Material.ENCHANTED_BOOK)
			for (Map.Entry<Enchantment, Integer> entry : ((EnchantmentStorageMeta)
					item.getItemMeta()).getStoredEnchants().entrySet()) {

				if ((entry.getKey()).getMaxLevel() < (entry.getValue()).intValue())
					isEnhanced = true;
			}

		else
			for (Map.Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet())
				if ((entry.getKey()).getMaxLevel() < (entry.getValue()).intValue()) isEnhanced = true;

		return isEnhanced;
	}
}
