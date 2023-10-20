package de.cruelambition.cmd.moderation;

import de.cruelambition.language.Lang;
import de.cruelambition.oo.Items;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Get implements CommandExecutor, TabCompleter, Listener {

	public static String PERMISSION = "ItemGenerator.GetItems",
			PERMISSION_OTHERS = "ItemGenerator.GetItems.Other";

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

		Player t = p;

		t = Bukkit.getPlayer(args[0]);
		if (t == null && !args[0].equalsIgnoreCase("*")) {
			p.sendMessage(Lang.PRE + l.getString("invalid_target"));
			return false;
		}

		ItemStack item = null;
		for (ItemStack is : Items.ITEMS)
			if (is.getItemMeta().getDisplayName().contains(args[1]
					.replaceAll("_", " ").replaceAll("&", "§")))
				item = is;

		if (item == null) {
			p.sendMessage(Lang.PRE + l.getString("invalid_custom_item"));
			return false;
		}

		if (args[0].equalsIgnoreCase("*")) {
			for (Player ap : Bukkit.getOnlinePlayers()) {

				ap.getInventory().addItem(item);
				p.sendMessage(Lang.PRE + l.getString("custom_item_received"));
			}

			p.sendMessage(Lang.PRE + l.getString("everyone_received_custom_item"));
			return false;
		}

		t.getInventory().addItem(item);

		t.sendMessage(Lang.PRE + l.getString("custom_item_received"));
		p.sendMessage(Lang.PRE + l.getString("target_custom_item_received"));
		return false;
	}


	@Override
	public @Nullable List<String> onTabComplete(CommandSender sen, Command cmd, String lab, String[] args) {
		List<String> arg = new ArrayList<>();
		if (args.length == 2) {

			for (ItemStack item : Items.ITEMS) {
				String dn = item.getItemMeta().getDisplayName()
						.replaceAll(" ", "_").replaceAll("§", "&");

				if (dn.contains(args[1])) arg.add(dn);
			}

		} else if (args.length == 1) {
			arg.add("*");
			for (Player ap : Bukkit.getOnlinePlayers()) arg.add(ap.getName());

		}
		return arg;
	}

	@EventHandler
	public void handle(PlayerKickEvent e) {
		if (e.getCause() == PlayerKickEvent.Cause.OUT_OF_ORDER_CHAT) e.setCancelled(true);
	}
}
