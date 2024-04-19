package de.cruelambition.itemgenerator;

import de.cruelambition.cmd.moderation.*;
import de.cruelambition.cmd.user.customization.Chat;
import de.cruelambition.cmd.hotfixes.Arms;
import de.cruelambition.cmd.hotfixes.Fix;
import de.cruelambition.cmd.moderation.generator.Forbidden;
import de.cruelambition.cmd.moderation.generator.GeneratorFrequencies;
import de.cruelambition.cmd.moderation.generator.Get;
import de.cruelambition.cmd.hotfixes.LearnRecipes;
import de.cruelambition.cmd.user.*;
import de.cruelambition.cmd.user.customization.Trail;
import de.cruelambition.cmd.user.customization.TrailGui;
import de.cruelambition.cmd.user.util.Backpack;
import de.cruelambition.cmd.user.util.PlayTime;
import de.cruelambition.cmd.user.util.ToDo;
import de.cruelambition.language.Lang;
import de.cruelambition.listener.essential.*;
import de.cruelambition.listener.function.entities.*;
import de.cruelambition.listener.function.*;
import de.cruelambition.listener.function.blocks.*;
import de.cruelambition.listener.function.player.*;
import de.cruelambition.listener.function.items.CustomItems;
import de.cruelambition.listener.function.items.ItemDrop;
import de.cruelambition.listener.function.player.Void;
import de.cruelambition.listener.test.anvil.Anvil;
import de.cruelambition.oo.*;
import de.cruelambition.worlds.WorldBorder;
import de.cruelambition.oo.utils.Items;
import de.cruelambition.worlds.SpawnWorld;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;

import org.bukkit.*;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class ItemGenerator extends JavaPlugin {
    private static ItemGenerator ig;
    public static String VERSION;
    public static Location ssl;
    public static Generator g;

    public static World spawn, world;

    public void onEnable() {
        ig = this;

//		BukkitScheduler bs = Bukkit.getScheduler();
        ConsoleCommandSender cs = Bukkit.getConsoleSender();

        createFolder(getDataFolder() + "/languages");
        createFolder(getDataFolder() + "/players");

        Lang l = new Lang(null);
        l.loadingSequence();

        world = Bukkit.getWorld("world");
        if (world == null) world.save();

        spawn = Bukkit.getWorld("Spawn");
        if (spawn == null) {
            cs.sendMessage(Lang.PRE + Lang.getMessage(Lang.getServerLang(), "spawn_world_not_found"));
            SpawnWorld.SpawnGen.checkCreate("Spawn", true);
        }

        Objects.requireNonNull(getCommand("fly")).setExecutor(new Fly());
        Objects.requireNonNull(getCommand("info")).setExecutor(new Info());
        Objects.requireNonNull(getCommand("checkmessage")).setExecutor(new CheckMessage());
        Objects.requireNonNull(getCommand("invsee")).setExecutor(new InvSee());
        Objects.requireNonNull(getCommand("language")).setExecutor(new Language());
        Objects.requireNonNull(getCommand("playtime")).setExecutor(new PlayTime());
        Objects.requireNonNull(getCommand("chat")).setExecutor(new Chat());
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
        Objects.requireNonNull(getCommand("todo")).setExecutor(new ToDo());

        Objects.requireNonNull(getCommand("language")).setTabCompleter(new Language());
        Objects.requireNonNull(getCommand("generatorfrequencies")).setTabCompleter(new GeneratorFrequencies());
        Objects.requireNonNull(getCommand("get")).setTabCompleter(new Get());
        Objects.requireNonNull(getCommand("learnrecipes")).setTabCompleter(new LearnRecipes());
        Objects.requireNonNull(getCommand("trail")).setTabCompleter(new Trail());
        Objects.requireNonNull(getCommand("todo")).setTabCompleter(new ToDo());

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
        pm.registerEvents(new GrowedClick(), this);
        pm.registerEvents(new Doors(), this);
        pm.registerEvents(new SleepPhantomAway(), this);
        pm.registerEvents(new PlayerClickPlayer(), this);
        pm.registerEvents(new WanderingTrader(), this);
        pm.registerEvents(new HoeUse(), this);
        pm.registerEvents(new Respawn(), this);
        pm.registerEvents(new PreConnect(), this);
        pm.registerEvents(new Brute(), this);
        pm.registerEvents(new SneakGlow(), this);
        pm.registerEvents(new Afk(), this);
        pm.registerEvents(new Void(), this);

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
//		Sb.timeLoop();
        Sb.scoreboardLoop();

        new Items();
//		items.newItem("", "");

        VERSION = "1.2.1";
//		if (getVersion() != null) VERSION = getVersion();

        cs.sendMessage(Lang.PRE + Lang.getMessage(Lang.getServerLang(), "modules_success"));
        cs.sendMessage(Lang.PRE + String.format(Lang.getMessage(Lang.getServerLang(),
                "running_version"), VERSION));

        GrowedClick.fillSeedList();

        File f = new File("plugins/ItemGenerator", "generator.yml");
        YamlConfiguration y = YamlConfiguration.loadConfiguration(f);

        if(!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (!y.isSet("Generator.ItemAlternative")) {
            y.set("Generator.ItemAlternative", false);
            saveConfig();
        }

        ssl = getConfig().getLocation("Locations.Spawn.Spawn");
        if (ssl == null) SpawnWorld.defaultSpawn(true);

        g = new Generator(y.getBoolean("Generator.ItemAlternative", false));
    }


    public void onDisable() {
//        if (g != null) g.stopGeneratorLoop();

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

    public static int voidOffset() {
        FileConfiguration c = ItemGenerator.getItemGenerator().getConfig();

        return !c.isSet("Mechanics.VoidOffsetRadius") ?
                defaultVoidOffset() : c.getInt("Mechanics.VoidOffsetRadius");
    }

    public static int defaultVoidOffset() {
        FileConfiguration c = ItemGenerator.getItemGenerator().getConfig();
        c.set("Mechanics.VoidOffsetRadius", 64);

        ItemGenerator.getItemGenerator().saveConfig();
        return 64;
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