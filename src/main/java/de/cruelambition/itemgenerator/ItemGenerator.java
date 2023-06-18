package de.cruelambition.itemgenerator;

import de.cruelambition.cmd.moderation.Fly;
import de.cruelambition.cmd.user.Info;
import de.cruelambition.generator.Generator;
import de.cruelambition.language.Lang;
import de.cruelambition.language.Language;
import de.cruelambition.listener.essential.CM;
import de.cruelambition.listener.essential.Chat;
import de.cruelambition.worlds.SpawnWorld;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public final class ItemGenerator extends JavaPlugin {
	private static ItemGenerator ig;
	public static String VERSION;
	private static Location ssl;
	private Generator g;

	public void onEnable() {
		ig = this;
		BukkitScheduler bs = Bukkit.getScheduler();
		ConsoleCommandSender cs = Bukkit.getConsoleSender();

		createFolder(getDataFolder() + "/languages");
		createFolder(getDataFolder() + "/players");

		new Language().loadMessages();

		Language.printAllMessages(Lang.getLangFile("en"));

		World spawn = Bukkit.getWorld("world");
		if (spawn == null) spawn.save();

		ssl = SpawnWorld.getSafeSpawnLocation();

		Objects.requireNonNull(getCommand("fly")).setExecutor(new Fly());
		Objects.requireNonNull(getCommand("info")).setExecutor(new Info());

		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new CM(), this);
		pm.registerEvents(new Chat(), this);

		FileConfiguration c = getConfig();

		int csi = getSafeInt(c, "Loops.Check.StartIn", 50, 50),
				cf = getSafeInt(c, "Loops.Check.Frequency", 60, 60),
				gsi = getSafeInt(c, "Loops.Generator.StartIn", 5, 5),
				gf = getSafeInt(c, "Loops.Generator.Frequency", 20, 20);

		g = new Generator();
		g.fillList();
		g.syncForbiddenItems();
		g.removeAllForbiddenItemsFromMaterialList();

		g.checkForForbiddenItemsLoop(csi, cf);
		g.startGeneratorLoop(gsi, gf);

		VERSION = "0.0.1";
	}

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
		if (!f1.mkdir()) return false;

		return true;
	}

	public void onDisable() {
		if (g != null) {
			g.cancelCheck();
			g.cancelGenerator();
		}
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