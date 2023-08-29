package de.cruelambition.cmd.moderation;

import de.cruelambition.language.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Fly implements CommandExecutor {
	public boolean onCommand(CommandSender sen, Command cmd, String lab, String[] args) {
		Player p;
		Lang l = new Lang(null);

		if (sen instanceof Player) {
			p = (Player) sen;

		} else {
			sen.sendMessage(Lang.PRE + Lang.getMessage(null, "not_a_player"));
			return false;
		}

		if (p != null) l.setPlayer(p);

		if (args.length == 0) {
			p.setFlying(!p.isFlying());
			p.sendMessage(Lang.PRE + Lang.PRE);
		} else if (args.length != 1) {
			p.sendMessage(Lang.PRE + Lang.PRE);
			return false;
		}

		Player t = Bukkit.getPlayer(args[0]);
		if (t == null) {
			p.sendMessage(Lang.PRE + Lang.PRE);
			return false;
		}

		p.sendMessage(Lang.PRE + Lang.PRE);
		l.setPlayer(t);

		t.setFlying(!t.isFlying());
		t.sendMessage(Lang.PRE + Lang.PRE);

		return false;
	}
}