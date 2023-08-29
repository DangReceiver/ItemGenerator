package de.cruelambition.cmd.user;

import de.cruelambition.language.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Info implements CommandExecutor {
	public boolean onCommand(CommandSender sen, Command cmd, String lab, String[] args) {
		Player p;
		Lang l = new Lang(null);

		if (sen instanceof Player) {
			p = (Player) sen;

		} else {
			sen.sendMessage(Lang.PRE + Lang.getMessage(l.getLang(null), "info"));
			return false;
		}

		p.sendMessage(Lang.PRE + Lang.getMessage(l.getLang(p), "info"));

		int i = 0, ping = 0;
		for (Player ap : Bukkit.getOnlinePlayers()) {
			ping += ap.getPing();
			i++;
		}

		double d = Math.round((ping / i * 10000));
		p.sendMessage(Lang.PRE + Lang.getMessage(l.getLang(p), "ping"));

		return false;
	}
}
