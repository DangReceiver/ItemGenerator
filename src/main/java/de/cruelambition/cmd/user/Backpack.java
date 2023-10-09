package de.cruelambition.cmd.user;

import de.cruelambition.language.Lang;
import de.cruelambition.oo.PC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class Backpack implements CommandExecutor, Listener {

	public static String PERMISSION = "ItemGenerator.Backpack";

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

		if (args.length != 0) {
			p.sendMessage(Lang.PRE + String.format(Lang.getMessage(l.getLanguage(), "arg_length_required"), 0));
			return false;
		}

		PC pc = new PC(p);
		p.openInventory(pc.getBackpack());

		return false;
	}

	@EventHandler
	public void handle(InventoryCloseEvent e) {
		HumanEntity he = e.getPlayer();
		if (he instanceof Player p) {

			PC pc = new PC(p);
			pc.setBackpack(e.getInventory());
			pc.savePCon();
		}
	}
}
