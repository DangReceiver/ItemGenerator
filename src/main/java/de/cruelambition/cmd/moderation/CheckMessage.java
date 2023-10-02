package de.cruelambition.cmd.moderation;

import de.cruelambition.language.Lang;
import de.cruelambition.language.Language;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CheckMessage implements CommandExecutor {

	public static String PERMISSION = "ItemGenerator.CheckMessage";

	@Override
	public boolean onCommand(CommandSender sen, Command cmd, String lab, String[] args) {
		Lang l = new Lang(null);
		if (sen instanceof Player) l.setPlayer((Player) sen);

		if (!sen.hasPermission(PERMISSION)) {
			sen.sendMessage(Lang.PRE + l.getString("insufficient_permission"));
			return false;
		}

		if (args.length > 1) {
			sen.sendMessage(Lang.PRE + l.getString("invalid_argument_length"));
			return false;
		}

		if (l.getMissingKeys() == null || Language.missingKeys == null) {

			sen.sendMessage(Lang.PRE + l.getString("no_missing_keys"));
			return false;
		}

		if (args.length == 0) {
			sen.sendMessage(Lang.PRE + l.getString("listing_missing_keys"));

			for (String s : l.getMissingKeys())
				sen.sendMessage(Lang.PRE + String.format(
						l.getString("missing_strings"), s));
			return false;

		} else if (args.length == 1) {
			if (args[0].equalsIgnoreCase("*")) {

				int i = 0;
				sen.sendMessage(Lang.PRE + l.getString("listing_missing_keys"));

				for (String s : Language.missingKeys) {
					sen.sendMessage(Lang.PRE + String.format(Lang.getMessage(l.getLanguage(),
							"list_missing_Strings"), s.replaceAll(";", " §8→ §7"), i));
					i++;
				}

			} else
				sen.sendMessage(Lang.PRE + l.getString(args[0]));
		}
		return false;
	}
}