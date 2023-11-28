package de.cruelambition.cmd.user;

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

import java.util.List;

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

                int i = 1;
                for (String toDo : pc.getToDos()) {
                    p.sendMessage("  " + i + "§8: §7" + l.getString("todos_listing"), toDo);
                    i++;
                }
            }
            return false;
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

                pc.removeToDo(i);
                p.sendMessage(Lang.PRE + l.getString("todo_removed"));

            } else if (args[0].equalsIgnoreCase("list")) {
                Player t = Bukkit.getPlayer(args[1]);
                if(t == null) {
                    p.sendMessage(Lang.PRE + l.getString("invalid_target"));
                    return false;
                }

                p.sendMessage(Lang.PRE + String.format(l.getString("check_todos_pre_other"), t));

                //USaGE INVALID
                pc.getPlayer(t.getName());
                int i = 1;
                for (String toDo : pc.getToDos()) {
                    p.sendMessage("  " + i + "§8: §7" + l.getString("todos_listing"), toDo);
                    i++;
                }
            }

            return false;
        }

        if (args[0].equalsIgnoreCase("add")) {
            String entry = "";

            boolean sw = false;
            for (String arg : args) {

                if (arg.contains("\"")) sw = !sw;
                if (sw) entry = entry + arg.replaceAll("\"", "");
            }

            pc.addToDo(entry = entry.replaceAll("&", "§"));
            p.sendMessage(Lang.PRE + String.format(l.getString("adding_todo"), entry));
        }

//        if (!p.hasPermission(PERMISSION_OTHERS)) {
//            sen.sendMessage(Lang.PRE + String.format(l.getString("insufficient_permission"), PERMISSION_OTHERS));
//            return false;
//        }
        return false;
    }


    @Override
    public @Nullable List<String> onTabComplete(CommandSender sen, Command cmd, String lab, String[] args) {


        return null;
    }
}
