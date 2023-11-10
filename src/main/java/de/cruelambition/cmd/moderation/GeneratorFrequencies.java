package de.cruelambition.cmd.moderation;

import de.cruelambition.itemgenerator.ItemGenerator;
import de.cruelambition.language.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeneratorFrequencies implements CommandExecutor, TabCompleter {

	public static String PERMISSION = "ItemGenerator.Generator.Settings";

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

		if (args.length != 2) {
			p.sendMessage(Lang.PRE + String.format(l.getString("arg_length_required"), 2));
			return false;
		}

		List<Integer> f = ItemGenerator.g.getFrequencies();

		int csi = f.get(0), cf = f.get(1), gsi = f.get(2), gf = f.get(3);

		int input;
		try {
			input = Integer.parseInt(args[1]);

		} catch (Exception e) {
			p.sendMessage(Lang.PRE + String.format(l.getString("arg_invalid")));
			return false;
		}

		p.sendMessage(Lang.PRE + String.format(l.getString("generatorfrequencies_before"), csi, cf, gsi, gf));

		if (args[0].equalsIgnoreCase("CheckStartIn")) {
			csi = input;

		} else if (args[0].equalsIgnoreCase("CheckFrequency")) {
			cf = input;

		} else if (args[0].equalsIgnoreCase("GeneratorStartIn")) {
			gsi = input;

		} else if (args[0].equalsIgnoreCase("GeneratorFrequency")) {
			gf = input;

		} else {
			p.sendMessage(Lang.PRE + String.format(l.getString("arg_invalid"), args[0]));
			return false;
		}
		p.sendMessage(Lang.PRE + String.format(l.getString("generatorfrequencies_after"), csi, cf, gsi, gf));

		ItemGenerator.g.restartGeneratorLoop(gsi, gf);
		return false;
	}

	@Override
	public @Nullable List<String> onTabComplete(CommandSender sen, Command cmd, String lab, String[] args) {
		if (args.length != 1) return null;
		List<String> l = new ArrayList<>(Arrays.asList("CheckStartIn", "CheckFrequency",
				"GeneratorStartIn", "GeneratorFrequency"));

		for (int i = 0; i < l.size(); i++)
			if (!l.get(i).contains(args[0])) l.remove(i);

		return l;
	}
}
