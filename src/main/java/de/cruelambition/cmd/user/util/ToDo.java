package de.cruelambition.cmd.user.util;

import de.cruelambition.itemgenerator.ItemGenerator;
import de.cruelambition.language.Lang;
import de.cruelambition.oo.PC;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

public class ToDo implements CommandExecutor, TabCompleter {
	public static String PERMISSION = "ItemGenerator.ToDo", PERMISSION_OTHERS = "ItemGenerator.ToDo.Others";

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
			p.sendMessage(Lang.PRE + String.format(l.getString("insufficient_permission"), PERMISSION));
			return false;
		}

		if (args.length == 0) {
			p.sendMessage(Lang.PRE + l.getString("todo_usage"));
			return false;
		}

		PC pc = new PC(p);
		if (args.length == 1) {

			if (args[0].equalsIgnoreCase("list")) {
				p.sendMessage(Lang.PRE + l.getString("check_todos_pre"));

				if (pc.getToDos().size() <= 0) {
					p.sendMessage(Lang.PRE + l.getString("todo_list_empty"));
					return false;
				}

				int i = 1;
				for (String toDo : pc.getToDos()) {
					p.sendMessage(String.format(l.getString("todos_listing"), i, toDo));
					i++;
				}
				return false;

			} else {
				p.sendMessage(Lang.PRE + l.getString("todo_usage"));
				return false;
			}
		}

		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("check")) {

				int i = -1;
				try {
					i = Integer.parseInt(args[1]);

				} catch (NumberFormatException ex) {
					p.sendMessage(Lang.PRE + l.getString("invalid_argument"));
				}

				if (i <= 0 || i >= pc.getToDos().size()) {
					p.sendMessage(Lang.PRE + l.getString("todo_check_number"));
					return false;
				}

				pc.checkToDo(i);
				p.sendMessage(Lang.PRE + l.getString("todo_checked"));

				pc.savePCon();
				return false;

			} else if (args[0].equalsIgnoreCase("remove")) {

				int i = -1;
				try {
					i = Integer.parseInt(args[1]);

				} catch (NumberFormatException ex) {
					p.sendMessage(Lang.PRE + l.getString("invalid_argument"));
				}

				if (i <= 0 || i >= pc.getToDos().size()) {
					p.sendMessage(Lang.PRE + l.getString("todo_removal_number"));
					return false;
				}

				p.sendMessage(Lang.PRE + String.format(l.getString("todo_removed"), pc.getToDo(i)));
				pc.removeToDo(i);

				pc.savePCon();
				return false;

			} else if (args[0].equalsIgnoreCase("list")) {

				if (!p.hasPermission(PERMISSION_OTHERS)) {
					p.sendMessage(Lang.PRE + String.format(l.getString("insufficient_permission"),
							PERMISSION_OTHERS));
					return false;
				}

				Player t = Bukkit.getPlayer(args[1]);

				if (t == null) {
					p.sendMessage(Lang.PRE + l.getString("invalid_target"));
					return false;
				}

				p.sendMessage(Lang.PRE + String.format(l.getString("check_todos_pre_other"), t));
				pc.updatePlayer(t);

				if (pc.getToDos().isEmpty()) {
					p.sendMessage(Lang.PRE + l.getString("todo_list_empty"));
					return false;
				}

				int i = 1;
				for (String toDo : pc.getToDos()) {

					p.sendMessage("  " + i + "§8: §7" + l.getString("todos_listing"), toDo);
					i++;
				}
				return false;
			}
		}

		if (args[0].equalsIgnoreCase("add")) {
			String entry = "";

			boolean sw = false;
			for (String arg : args) {

				if (arg.contains("\"")) sw = !sw;
				entry = entry + arg.replaceAll("\"", "");

				if (!sw) {
					pc.addToDo(entry = entry.replaceAll("&", "§"));
					p.sendMessage(Lang.PRE + String.format(l.getString("adding_todo"), entry));

					pc.savePCon();
					return false;
				}
			}
			return false;
		}

		p.sendMessage(Lang.PRE + l.getString("invalid_argument"));

//        if (!p.hasPermission(PERMISSION_OTHERS)) {
//            sen.sendMessage(Lang.PRE + String.format(l.getString("insufficient_permission"),
//            PERMISSION_OTHERS));
//            return false;
//        }
		return false;
	}


	@Override
	public @Nullable List<String> onTabComplete(CommandSender sen, Command cmd, String lab, String[] args) {
		List<String> arg = new ArrayList<>();

		if (args.length == 1) {
			for (String s : new ArrayList<>(Arrays.asList("list", "check", "remove", "add")))
				if (s.contains(args[0]) || args[0].isEmpty()) arg.add(s);

		} else if (args.length == 2 && args[0].equalsIgnoreCase("check")) {
			Collection<? extends Player> op = Bukkit.getOnlinePlayers();
			for (Player ap : op) if (ap.getName().contains(args[1])) arg.add(ap.getName());

		} else if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
			PC pc = new PC((Player) sen);

			if (pc.getToDos().isEmpty()) return null;
			for (int i = 1; i <= pc.getToDos().size(); i++) arg.add(i + "");

		} else if (args.length == 2 && args[0].equalsIgnoreCase("add")) {
			arg.add("\"");

		} else if (args.length >= 3 && args[0].equalsIgnoreCase("add")
				&& args[1].equalsIgnoreCase("\"")) {
			arg.add("\"");
		}

		return arg;
	}
}
