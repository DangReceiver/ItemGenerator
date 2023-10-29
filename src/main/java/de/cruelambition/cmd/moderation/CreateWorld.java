package de.cruelambition.cmd.moderation;

import de.cruelambition.itemgenerator.ItemGenerator;
import de.cruelambition.language.Lang;
import de.cruelambition.worlds.SpawnWorld;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class CreateWorld implements CommandExecutor {

	public static String PERMISSION = "ItemGenerator.CreateVoidWorld";

	boolean creation = false;
	int c = 0;

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


		if (args.length <= 1) {
			p.sendMessage(Lang.PRE + l.getString("args_length"));
			return true;
		}

		String s = args[1];
		boolean b;

		try {
			b = Boolean.parseBoolean(args[0]);
		} catch (Exception e) {
			p.sendMessage(Lang.PRE + String.format(l.getString("args_type"), 0, "boolean"));
			return true;
		}

		if (args.length > 2)
			for (int i = 2; i < args.length; i++) {
				s = s + "_" + args[i];
			}

		s = s.replace("true", "").replace("false", "");//.replace(args[1] + "_", "")

		if (s.equals("_") || s.contains("null")) {
			p.sendMessage(Lang.PRE + l.getString("invalid_world_name"));
			return true;
		}

		p.sendMessage(Lang.PRE + String.format(l.getString("creating_world"), s, b));
//			UseVoid.createVoidWorld(s, b);
		SpawnWorld.SpawnGen.checkExists(s, false);

		final String wName = s;
		checkCreationStatus(p, l, wName);
		return false;
	}

	public void checkCreationStatus(Player p, Lang l, String s) {
		Bukkit.getScheduler().runTaskLater(ItemGenerator.getItemGenerator(), () -> {

			if (Bukkit.getWorld(s) != null) {
				p.sendMessage(Lang.PRE + l.getString("world_created"));

			} else {
				c++;

				if (c >= 18) {
					p.sendMessage(Lang.PRE + l.getString("world_check_time_out"));
					return;
				}

				checkCreationStatus(p, l, s);
			}
		}, 10);
	}
}
