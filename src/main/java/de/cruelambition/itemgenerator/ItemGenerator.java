package de.cruelambition.itemgenerator;

import de.cruelambition.cmd.moderation.*;
import de.cruelambition.cmd.moderation.Chat;
import de.cruelambition.cmd.user.*;
import de.cruelambition.language.Lang;
import de.cruelambition.listener.essential.*;
import de.cruelambition.listener.function.entities.*;
import de.cruelambition.listener.function.*;
import de.cruelambition.listener.function.blocks.*;
import de.cruelambition.listener.function.items.CustomItems;
import de.cruelambition.listener.function.items.ItemDrop;
import de.cruelambition.oo.*;
import de.cruelambition.oo.WorldBorder;
import de.cruelambition.worlds.SpawnWorld;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;

import org.bukkit.*;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class ItemGenerator extends JavaPlugin {
	private static ItemGenerator ig;
	public static String VERSION;
	private static Location ssl;
	public static Generator g;

	public void onEnable() {
		ig = this;

//		BukkitScheduler bs = Bukkit.getScheduler();
		ConsoleCommandSender cs = Bukkit.getConsoleSender();

		createFolder(getDataFolder() + "/languages");
		createFolder(getDataFolder() + "/players");

		World world = Bukkit.getWorld("world");
		if (world == null) world.save();

		World spawn = Bukkit.getWorld("Spawn");
		if (spawn == null) SpawnWorld.SpawnGen.checkCreate("Spawn", true);

		Lang l = new Lang(null);
		l.loadingSequence();

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
		Objects.requireNonNull(getCommand("get")).setExecutor(new Get());
		Objects.requireNonNull(getCommand("spawn")).setExecutor(new Spawn());
		Objects.requireNonNull(getCommand("learnrecipes")).setExecutor(new LearnRecipes());
		Objects.requireNonNull(getCommand("fix")).setExecutor(new Fix());
		Objects.requireNonNull(getCommand("forbidden")).setExecutor(new Forbidden());
		Objects.requireNonNull(getCommand("rare")).setExecutor(new Rare());
		Objects.requireNonNull(getCommand("trail")).setExecutor(new Trail());
		Objects.requireNonNull(getCommand("trailgui")).setExecutor(new TrailGui());

		Objects.requireNonNull(getCommand("language")).setTabCompleter(new Language());
		Objects.requireNonNull(getCommand("generatorfrequencies")).setTabCompleter(new GeneratorFrequencies());
		Objects.requireNonNull(getCommand("get")).setTabCompleter(new Get());
		Objects.requireNonNull(getCommand("learnrecipes")).setTabCompleter(new LearnRecipes());
		Objects.requireNonNull(getCommand("trail")).setTabCompleter(new Trail());

		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new CM(), this);
		pm.registerEvents(new Chat(), this);
		pm.registerEvents(new de.cruelambition.listener.essential.Chat(), this);
		pm.registerEvents(new GameModeChange(), this);
		pm.registerEvents(new InvSee(), this);
		pm.registerEvents(new ItemDrop(), this);
		pm.registerEvents(new Timber(), this);
		pm.registerEvents(new Backpack(), this);
		pm.registerEvents(new Recipes(), this);
		pm.registerEvents(new AntiCreeper(), this);
		pm.registerEvents(new GlassShear(), this);
		pm.registerEvents(new KillDeath(), this);
		pm.registerEvents(new CaneCactus(), this);
		pm.registerEvents(new SpawnWorld(), this);
		pm.registerEvents(new Get(), this);
		pm.registerEvents(new WorldChange(), this);
		pm.registerEvents(new CustomItems(), this);
		pm.registerEvents(new Furnace(), this);
		pm.registerEvents(new Anvil(), this);
		pm.registerEvents(new TrailGui(), this);
		pm.registerEvents(new WorldBorder(), this);
		pm.registerEvents(new ClickGrowed(), this);
		pm.registerEvents(new Doors(), this);
		pm.registerEvents(new SleepPhantomAway(), this);
		pm.registerEvents(new PlayerClickPlayer(), this);
		pm.registerEvents(new WanderingTrader(), this);
		pm.registerEvents(new HoeUse(), this);
		pm.registerEvents(new Respawn(), this);
		pm.registerEvents(new PreConnect(), this);
		pm.registerEvents(new Brute(), this);
		pm.registerEvents(new SneakGlow(), this);
//		pm.registerEvents(new Afk(), this);

		TrailGui.fillParList();
		WorldBorder wb = new WorldBorder();
		wb.syncWb();

		int i = new Random().nextInt(15);
		Bukkit.setMotd(l.getString("motd_" + i));
		cs.sendMessage(Lang.PRE + String.format(l.getString("using_motd_x"), i));

		Recipes r = new Recipes();
		List<Recipe> rec = r.getRec();

		if (rec.isEmpty()) cs.sendMessage(Lang.PRE + l.getString("empty_recipe_list"));

		removeRecipes();
		for (Recipe recipe : rec) Bukkit.addRecipe(recipe, true);

		for (Player ap : Bukkit.getOnlinePlayers()) {
			cs.sendMessage(l.getString("player_removeRecipe_recipe"));

			for (Recipe recipe : Recipes.rec) {
				if (!(recipe instanceof Keyed k)) {

					ap.sendMessage(Lang.PRE + l.getString("recipe_list_invalid"));
					continue;
				}

				if (ap.hasDiscoveredRecipe(k.getKey())) ap.undiscoverRecipe(k.getKey());
				ap.discoverRecipe(k.getKey());
			}
		}

		Sb.setAllScoreBoards();
		Sb.timeLoop();
		Sb.scoreboardLoop();

		new Items();
//		items.newItem("", "");

		VERSION = "1.1.0";
//		if (getVersion() != null) VERSION = getVersion();

		cs.sendMessage(Lang.PRE + Lang.getMessage(Lang.getServerLang(), "modules_success"));
		cs.sendMessage(Lang.PRE + String.format(Lang.getMessage(Lang.getServerLang(),
				"running_version"), VERSION));

		ClickGrowed.fillSeedList();

		g = new Generator();
		List<Integer> f = g.getFrequencies();

		Generator.initiate(g, f.get(0), f.get(1), f.get(2), f.get(3));
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

		for (Player ap : Bukkit.getOnlinePlayers()) {
			cs.sendMessage(l.getString("player_removeRecipe_recipe"));

			for (Recipe re : Recipes.rec)
				if (re instanceof Keyed k) ap.undiscoverRecipe(k.getKey());
		}

		removeRecipes();
		de.cruelambition.language.Language.saveMissingKeys();

		for (Player ap : Bukkit.getOnlinePlayers()) {
			PC pc = new PC(ap);
			pc.setJetpackUsage(false);

			pc.set("Temp", null);
			pc.savePCon();
		}
	}

	public void removeRecipes() {
		for (Recipe recipe : Recipes.rec) if (recipe instanceof Keyed k) Bukkit.removeRecipe(k.getKey());
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