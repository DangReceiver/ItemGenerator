package de.cruelambition.cmd.moderation;

import de.cruelambition.language.Lang;
import de.cruelambition.language.Language;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CheckMessage implements CommandExecutor {


	@Override
	public boolean onCommand(CommandSender sen, Command cmd, String lab, String[] args) {
		Player p;
		Lang l = new Lang(null);

		if (sen instanceof Player) {
			p = (Player) sen;

			if (p != null) l.setPlayer(p);

			if (args.length != 1) {
				sen.sendMessage(Lang.PRE + Lang.getMessage(l.getLanguage(), "invalid_argument_length"));
				return false;
			}

			sen.sendMessage("Blup");

			if (args[0].equalsIgnoreCase("*")) {
				int i = 0;
				sen.sendMessage("Blup1");

				for (String s : Language.missingKeys) {
					sen.sendMessage(Lang.PRE + String.format(Lang.getMessage(l.getLanguage(),
							"list_missing_Strings"), s.replaceAll(";", " §8→ §7"), i));
					i++;
				}

			} else {
				sen.sendMessage("Blup2");
				sen.sendMessage(Lang.PRE + Lang.getMessageUnverified(l.getLanguage(), args[0]));
			}
		}
		return false;
	}
}