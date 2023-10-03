package de.cruelambition.cmd.user;

import de.cruelambition.language.Lang;
import de.cruelambition.oo.PC;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

		if (args.length == 0) {
			PC pc = new PC(p);

			long tpt = pc.getTotalPlayTime();
			tpt = tpt + (System.currentTimeMillis() - pc.getJoinTime());

			p.sendMessage(Lang.PRE + String.format(l.getString("playtime"), tpt));
			return false;
		}

		if (args.length == 1) {

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
				p.sendMessage(Lang.PRE + l.getString("target_hasnt_played_before"));
				return false;
			}

			PC pc = new PC(t);
			long tpt = pc.getTotalPlayTime();

			if (t.isOnline()) tpt = tpt + (System.currentTimeMillis() - pc.getJoinTime());

			p.sendMessage(Lang.PRE + String.format(l.getString("targets_play_time"), tpt));
		}
		return false;
	}
}
