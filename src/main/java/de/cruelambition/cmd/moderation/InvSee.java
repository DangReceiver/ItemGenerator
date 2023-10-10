package de.cruelambition.cmd.moderation;

import de.cruelambition.language.Lang;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;

public class InvSee implements CommandExecutor, Listener {

	public static String PERMISSION = "ItemGenerator.InvSee", PERMISSION_MOVE = "ItemGenerator.InvSee.Move";

	@Override
	public boolean onCommand(CommandSender sen, Command cmd, String lab, String[] args) {
		Lang l = new Lang(null);

		if (!(sen instanceof Player p)) {
			sen.sendMessage(Lang.PRE + l.getString("not_a_player"));
			return false;
		}
		l.setPlayer(p);

		if (!p.hasPermission(PERMISSION)) {
			sen.sendMessage(Lang.PRE + String.format(l.getString("insufficient_permission"), PERMISSION));
			return false;
		}

		if (args.length != 1) {
			sen.sendMessage(Lang.PRE + String.format(l.getString("argument_length"), 1));
			return false;
		}

		Player t = Bukkit.getPlayer(args[0]);
		if (t == null) {
			sen.sendMessage(Lang.PRE + String.format(l.getString("target_invalid"), args[0]));
			return false;
		}

		PlayerInventory in = t.getInventory();
		p.openInventory(in);

		p.sendMessage(Lang.PRE + String.format(l.getString("invsee_target"), t.getName()));
		return false;
	}

	@EventHandler
	public void handle(InventoryClickEvent e) {
		HumanEntity he = e.getWhoClicked();

		if (!(he instanceof Player p)) return;
		if (e.getClickedInventory() == null) return;

		if (e.getClickedInventory().getType() == InventoryType.PLAYER &&
				e.getClickedInventory() == e.getWhoClicked().getInventory()) return;

		if (!p.hasPermission(PERMISSION_MOVE)) {

			e.setCancelled(true);
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 0.4f, 0.65f);
		}
	}
}
