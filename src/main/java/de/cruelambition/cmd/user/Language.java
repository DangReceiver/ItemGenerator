package de.cruelambition.cmd.user;

import de.cruelambition.itemgenerator.ItemGenerator;
import de.cruelambition.language.Lang;
import de.cruelambition.oo.PC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Language implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(CommandSender sen, Command cmd, String lab, String[] args) {
		Lang l = new Lang(null);

		if (!(sen instanceof Player p)) {
			sen.sendMessage(Lang.PRE + l.getString("not_a_player"));
			return false;
		}
		l.setPlayer(p);

		if (args.length != 1) {
			p.sendMessage(Lang.PRE + String.format(l.getString("argument_length"), 1));
			return false;
		}

		if (!Lang.isLangFile(args[0])) {
			p.sendMessage(Lang.PRE + l.getString("not_a_lang_file"));
			return false;
		}

		PC pc = new PC(p);
		File lf = Lang.getLangFile(args[0]);

		if(lf == null){
			p.sendMessage(Lang.PRE + l.getString("lang_file_invalid"));
			return false;
		}

		if(!pc.setLanguage(lf)) p.sendMessage(l.getString("lang_change_failed_input"));
		l.setPlayerLanguage(lf);

//		p.sendMessage("§b" + l.getLang(p) + " || " + l.getLanguage());

		pc.savePCon();
		p.sendMessage(Lang.PRE + String.format(l.getString("language_changed"),
				lf.getName().split(".yml")[0]));
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sen, Command cmd, String lab, String[] args) {
		if (args.length != 1) return null;

		List<File> langFiles = new ArrayList<>(Arrays.asList(Objects.requireNonNull(new File(
				ItemGenerator.getItemGenerator().getDataFolder() + "/languages").listFiles())));
		List<String> arg = new ArrayList<>();

		for (File lf : langFiles) arg.add(lf.getName().split(".yml")[0]);
		return arg;
	}
}
