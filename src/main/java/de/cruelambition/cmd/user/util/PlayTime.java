package de.cruelambition.cmd.user.util;

import de.cruelambition.language.Lang;
import de.cruelambition.oo.PC;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;

public class PlayTime implements CommandExecutor {

	public static String PERMISSION_OTHERS = "ItemGenerator.PlayTime.Others";

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

		if (args.length != 0 && args.length != 1) {
			sen.sendMessage(Lang.PRE + String.format(l.getString("argument_range"), 0, 1));
			return false;
		}

		if (args.length == 0) {
			PC pc = new PC(p);

			p.sendMessage(Lang.PRE + String.format(l.getString("playtime"), convertTime(pc.getCurrentPlayTime())));
			return false;
		}


		if (!p.hasPermission(PERMISSION_OTHERS)) {
			sen.sendMessage(Lang.PRE + String.format(l.getString("insufficient_permission"), PERMISSION_OTHERS));
			return false;
		}

		OfflinePlayer t = Bukkit.getOfflinePlayer(args[0]);
		if (t == null) {
			p.sendMessage(Lang.PRE + l.getString("target_invalid"));
			return false;
		}

		if (!t.hasPlayedBefore()) {
			p.sendMessage(Lang.PRE + String.format(l.getString("target_hasnt_yet_played"), t.getName()));
			return false;
		}

		PC pc = new PC(t);
		long tpt = pc.getTotalPlayTime();

		if (t.isOnline()) tpt = tpt + (System.currentTimeMillis() - pc.getJoinTime());
		p.sendMessage(Lang.PRE + String.format(l.getString("targets_play_time"), t.getName(), convertTime(tpt)));

		return false;
	}

	public String convertTime(long time) {
		Date date = new Date(time);
		Format format = new SimpleDateFormat("MM dd HH:mm:ss:SSS");
		return format.format(date);
	}
}
