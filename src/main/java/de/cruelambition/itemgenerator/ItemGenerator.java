package de.cruelambition.itemgenerator;

import de.cruelambition.cmd.moderation.*;
import de.cruelambition.cmd.user.Backpack;
import de.cruelambition.cmd.user.Info;
import de.cruelambition.cmd.user.Language;
import de.cruelambition.cmd.user.PlayTime;
import de.cruelambition.generator.Generator;
import de.cruelambition.language.Lang;
import de.cruelambition.listener.essential.CM;
import de.cruelambition.listener.essential.Chat;
import de.cruelambition.listener.function.*;
import de.cruelambition.oo.Items;
import de.cruelambition.oo.Recipes;
import de.cruelambition.oo.Sb;
import de.cruelambition.worlds.SpawnWorld;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public final class ItemGenerator extends JavaPlugin {
	private static ItemGenerator ig;
	public static String VERSION;
	private static Location ssl;
	public static Generator g;
	public static List<Recipe> rec;

	public void onEnable() {
		ig = this;

//		BukkitScheduler bs = Bukkit.getScheduler();
		ConsoleCommandSender cs = Bukkit.getConsoleSender();

		createFolder(getDataFolder() + "/languages");
		createFolder(getDataFolder() + "/players");

		Lang l = new Lang(null);
		l.loadingSequence();

		World spawn = Bukkit.getWorld("world");
		if (spawn == null) spawn.save();

		ssl = SpawnWorld.getSafeSpawnLocation();

		Objects.requireNonNull(getCommand("fly")).setExecutor(new Fly());
		Objects.requireNonNull(getCommand("info")).setExecutor(new Info());
		Objects.requireNonNull(getCommand("checkmessage")).setExecutor(new CheckMessage());
		Objects.requireNonNull(getCommand("invsee")).setExecutor(new InvSee());
		Objects.requireNonNull(getCommand("language")).setExecutor(new Language());
		Objects.requireNonNull(getCommand("playtime")).setExecutor(new PlayTime());
		Objects.requireNonNull(getCommand("chat")).setExecutor(new de.cruelambition.cmd.moderation.Chat());
		Objects.requireNonNull(getCommand("generatorfrequencies")).setExecutor(new GeneratorFrequencies());
		Objects.requireNonNull(getCommand("backpack")).setExecutor(new Backpack());
		Objects.requireNonNull(getCommand("arms")).setExecutor(new Arms());

		Objects.requireNonNull(getCommand("language")).setTabCompleter(new Language());
		Objects.requireNonNull(getCommand("generatorfrequencies")).setTabCompleter(new GeneratorFrequencies());

		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new CM(), this);
		pm.registerEvents(new Chat(), this);
		pm.registerEvents(new GameModeChange(), this);
		pm.registerEvents(new InvSee(), this);
		pm.registerEvents(new ItemDrop(), this);
		pm.registerEvents(new Timber(), this);
		pm.registerEvents(new Backpack(), this);
		pm.registerEvents(new Recipes(), this);
		pm.registerEvents(new AntiCreeper(), this);
		pm.registerEvents(new GlassShear(), this);
		pm.registerEvents(new KillDeath(), this);

		g = new Generator();
		List<Integer> f = g.getFrequencies();
		Generator.start(g, f.get(0), f.get(1), f.get(2), f.get(3));

		int i = new Random().nextInt(7);
		Bukkit.setMotd(l.getString("motd_" + i));
		cs.sendMessage(Lang.PRE + String.format(l.getString("using_motd_x"), i));

		Recipes r = new Recipes();
		rec = r.getRec();

		Bukkit.getScheduler().runTaskLater(this, () -> {
			for (Recipe recipe : rec) Bukkit.addRecipe(recipe);

			for (Player ap : Bukkit.getOnlinePlayers()) {
				cs.sendMessage(l.getString("player_receiving_recipe"));
				for (Recipe re : rec) if (re instanceof Keyed k) ap.discoverRecipe(k.getKey());
			}
		}, 5);

		Sb.setAllScoreBoards();
		Sb.timeLoop();

		Items items = new Items();
//		items.newItem("", "");

		VERSION = "0.1.1";
//		if (getVersion() != null) VERSION = getVersion();

		cs.sendMessage(Lang.PRE + Lang.getMessage(Lang.getServerLang(), "modules_success"));
		cs.sendMessage(Lang.PRE + String.format(Lang.getMessage(
				Lang.getServerLang(), "running_version"), VERSION));
	}


	public void onDisable() {
		if (g != null) {
			g.cancelCheck();
			g.cancelGenerator();
		}

		ConsoleCommandSender cs = Bukkit.getConsoleSender();
		Lang l = new Lang(null);

		if (l.getMissingKeys().isEmpty() || l.getMissingKeys() == null) return;
		cs.sendMessage(Lang.PRE + l.getString("listing_missing_keys"));

		for (Recipe recipe : rec)
			if (recipe instanceof Keyed k)
				Bukkit.removeRecipe(k.getKey());

		for (Player ap : Bukkit.getOnlinePlayers()) {
			cs.sendMessage(l.getString("player_removeRecipe_recipe"));

			for (Recipe re : rec)
				if (re instanceof Keyed k)
					ap.undiscoverRecipe(k.getKey());
		}

		for (String mk : l.getMissingKeys())
			cs.sendMessage(Lang.PRE + String.format(l.getString("list_missing_Keys"), mk));
	}


	/*
			Utils
	 */

	//toReturn: In case value hasn't been set yet
	public int getSafeInt(FileConfiguration c, String path, int setDefault, int toReturn) {
		if (c.isSet(path)) toReturn = c.getInt(path);
		else c.set(path, setDefault);

		saveConfig();
		return toReturn;
	}

	public static boolean createFolder(String path) {
		if (path == null)
			path = "plugins/ItemGenerator/languages";

		File f1 = new File(path);
		if (f1.exists()) return false;
		return f1.mkdir();
	}

	public static ItemGenerator getItemGenerator() {
		return ig;
	}

	public String getVersion() {
		Properties properties = new Properties();

		try {
			properties.load(getClassLoader().getResourceAsStream("project.properties"));

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return properties.getProperty("version");
	}
}