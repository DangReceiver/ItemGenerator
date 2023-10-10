package de.cruelambition.cmd.user;

import de.cruelambition.language.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Info implements CommandExecutor {
	public boolean onCommand(CommandSender sen, Command cmd, String lab, String[] args) {
		Player p = null;
		Lang l = new Lang(null);

		if (sen instanceof Player) {
			p = (Player) sen;
			l.setPlayer(p);

		}

		sen.sendMessage(Lang.PRE + l.getString("info"));
		if (p == null) return false;

		int ping = 0;
		for (Player ap : Bukkit.getOnlinePlayers())
			ping += ap.getPing();

		double d = Math.round((ping / Bukkit.getOnlinePlayers().size() * 10000));
		p.sendMessage(Lang.PRE + String.format(l.getString("average_ping"), p.getPing(), d / 10000));
		return false;
	}
}
