package de.cruelambition.cmd.moderation;

import de.cruelambition.generator.Generator;
import de.cruelambition.language.Lang;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

public class Fix implements CommandExecutor {

	public static String PERMISSION = "ItemGenerator.Fix";

	@Override
	public boolean onCommand(CommandSender sen, Command cmd, String lab, String[] args) {
		Player p;
		Lang l = new Lang(null);

		if (sen instanceof Player) {
			p = (Player) sen;
			l.setPlayer(p);

		} else {
			sen.sendMessage(Lang.PRE + Lang.getMessage(null, "not_a_player"));
			return false;
		}

		if (!p.hasPermission(PERMISSION)) {
			sen.sendMessage(Lang.PRE + String.format(l.getString("insufficient_permission"), PERMISSION));
			return false;
		}

		ItemStack item = p.getInventory().getItemInMainHand();

		if (isFixed(item)) {
			p.sendMessage(Lang.PRE + l.getString("already_fixed"));
			return false;
		}

		if (item.getType() == Material.ENCHANTED_BOOK) {
			Generator.enchant(item);
			return false;
		}

		if (item.getType().toString().contains("POTION") || item.getType().toString().contains("TIPPED_ARROW")) {
			Generator.effect(item);
			return false;
		}

		return false;
	}

	public boolean isFixed(ItemStack item) {
		boolean b = true;

		if (item.getItemMeta() != null) b = (b && (!item.getItemMeta().getEnchants().isEmpty()));
		return (b && ((item.getItemMeta() instanceof PotionMeta pm) && pm.hasCustomEffects()));
	}
}
