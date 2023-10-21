package de.cruelambition.cmd.moderation;

import de.cruelambition.language.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Fly implements CommandExecutor {

	public static String PERMISSION = "ItemGenerator.Fly", PERMISSION_OTHERS = "ItemGenerator.Fly.Others";

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

		if (args.length == 0) {
			p.setAllowFlight(!p.getAllowFlight());
			p.setFlying(p.getAllowFlight());

			p.sendMessage(Lang.PRE + Lang.getMessage(l.getLanguage(), "flight_updated"));
			return false;

		} else if (args.length != 1) {
			p.sendMessage(Lang.PRE + String.format(Lang.getMessage(l.getLanguage(), "argument_range"), 0, 1));
			return false;
		}

		if (!p.hasPermission(PERMISSION_OTHERS)) {
			sen.sendMessage(Lang.PRE + String.format(l.getString("insufficient_permission"), PERMISSION_OTHERS));
			return false;
		}

		Player t = Bukkit.getPlayer(args[0]);
		if (t == null) {
			p.sendMessage(Lang.PRE + Lang.getMessage(l.getLanguage(), "target_invalid"));
			return false;
		}

		p.sendMessage(Lang.PRE + Lang.getMessage(l.getLanguage(), "flight_updated_target"));
		l.setPlayer(t);

		t.setAllowFlight(!p.isFlying());
		t.setFlying(!t.isFlying());

		t.sendMessage(Lang.PRE + Lang.getMessage(l.getLanguage(), "flight_updated"));
		return false;
	}
}