package de.cruelambition.itemgenerator;

import de.cruelambition.cmd.moderation.Fly;
import de.cruelambition.cmd.user.Info;
import de.cruelambition.generator.Generator;
import de.cruelambition.language.Language;
import de.cruelambition.listener.essential.CM;
import de.cruelambition.listener.essential.Chat;
import de.cruelambition.worlds.SpawnWorld;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public final class ItemGenerator extends JavaPlugin {
    private static ItemGenerator ig;
    public static String VERSION;
    private static Location ssl;

    public void onEnable() {
        ig = this;
        BukkitScheduler bs = Bukkit.getScheduler();
        ConsoleCommandSender cs = Bukkit.getConsoleSender();

        Language.loadMessages();

        World spawn = Bukkit.getWorld("world");
        if (spawn == null) spawn.save();

        ssl = SpawnWorld.getSafeSpawnLocation();

        Objects.requireNonNull(getCommand("Fly")).setExecutor(new Fly());
        Objects.requireNonNull(getCommand("Info")).setExecutor(new Info());

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new CM(), this);
        pm.registerEvents(new Chat(), this);

        Generator g = new Generator();
        g.syncForbiddenItems();
        g.removeAllForbiddenItemsFromMaterialList();

        g.checkForForbiddenItemsLoop(15, 60);
        g.startGeneratorLoop(12, 12);

        VERSION = "0.0.1";
    }


    public void onDisable() {
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