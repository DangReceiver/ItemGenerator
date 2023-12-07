package de.cruelambition.cmd.moderation;

import de.cruelambition.language.Lang;
import de.cruelambition.oo.Recipes;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LearnRecipes implements CommandExecutor, TabCompleter {

	public static String PERMISSION = "ItemGenerator.Recipes",
			PERMISSION_OTHERS = "ItemGenerator.Recipes.Others";

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
			sen.sendMessage(Lang.PRE + String.format(l.getString("insufficient_permission"),
					PERMISSION));
			return false;
		}

		Bukkit.getConsoleSender().sendMessage(String.format(l.getString("player_receiving_recipe"),
				p.getName()));

		if (args.length == 0) {
			for (Recipe re : Recipes.rec)
				if (re instanceof Keyed k)
					p.discoverRecipe(k.getKey());
			return false;

		} else {
			Player t = p;
			if (args.length >= 2) t = Bukkit.getPlayer(args[1]);

			if (t == null) {
				p.sendMessage(Lang.PRE + l.getString("invalid_target"));
				return false;
			}

			if (args[0].equalsIgnoreCase("*")) {
				for (Recipe recipe : Recipes.rec) {

					if (!(recipe instanceof Keyed k)) {
						p.sendMessage(Lang.PRE + l.getString("recipe_list_invalid"));
						return false;
					}

					if (t.hasDiscoveredRecipe(k.getKey())) t.undiscoverRecipe(k.getKey());

					t.discoverRecipe(k.getKey());
				}
				t.sendMessage(Lang.PRE + l.getString("recipes_learned"));
				return false;

			} else {

				for (Recipe recipe : Recipes.rec) {
					if (!(recipe instanceof Keyed k)) {
						p.sendMessage(Lang.PRE + l.getString("recipe_list_invalid"));
						return false;
					}

					if (k.getKey().toString().equalsIgnoreCase(args[0])) {
						if (t.hasDiscoveredRecipe(k.getKey())) t.undiscoverRecipe(k.getKey());

						t.discoverRecipe(k.getKey());
						t.sendMessage(Lang.PRE + l.getString("recipe_learned"));
					}
				}
			}

			if (t != p) p.sendMessage(Lang.PRE + l.getString("target_learned_recipes"));
		}
		return false;
	}

	@Override
	public @Nullable List<String> onTabComplete(CommandSender sen, Command cmd, String lab, String[] args) {
		List<String> arg = new ArrayList<>();

		if (args.length == 2) {
			arg.add("*");

			for (Player ap : Bukkit.getOnlinePlayers())
				if (ap.getName().contains(args[1])) arg.add(ap.getName());

		} else if (args.length == 1)
			for (Recipe recipe : Recipes.rec) if (recipe instanceof Keyed k)
					if (k.getKey().toString().contains(args[0])) arg.add(k.getKey().toString());
		return arg;
	}
}
