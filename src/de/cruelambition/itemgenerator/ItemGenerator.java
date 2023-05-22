/*    */ package de.cruelambition.itemgenerator;
/*    */ 
/*    */ import de.cruelambition.cmd.moderation.Fly;
/*    */ import de.cruelambition.cmd.user.Info;
/*    */ import de.cruelambition.language.Language;
/*    */ import de.cruelambition.listener.essential.CM;
/*    */ import de.cruelambition.listener.essential.Chat;
/*    */ import de.cruelambition.worlds.SpawnWorld;
/*    */ import java.io.IOException;
/*    */ import java.util.Objects;
/*    */ import java.util.Properties;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.World;
/*    */ import org.bukkit.command.CommandExecutor;
/*    */ import org.bukkit.command.ConsoleCommandSender;
/*    */ import org.bukkit.command.PluginCommand;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.plugin.Plugin;
/*    */ import org.bukkit.plugin.PluginManager;
/*    */ import org.bukkit.plugin.java.JavaPlugin;
/*    */ import org.bukkit.scheduler.BukkitScheduler;
/*    */ 
/*    */ public final class ItemGenerator extends JavaPlugin {
/*    */   private static ItemGenerator ig;
/*    */   public static String VERSION;
/*    */   private static Location ssl;
/*    */   
/*    */   public void onEnable() {
/* 30 */     ig = this;
/* 31 */     BukkitScheduler bs = Bukkit.getScheduler();
/* 32 */     ConsoleCommandSender cs = Bukkit.getConsoleSender();
/*    */     
/* 34 */     Language.loadMessages();
/*    */     
/* 36 */     World spawn = Bukkit.getWorld("world");
/* 37 */     if (spawn == null) spawn.save();
/*    */     
/* 39 */     ssl = SpawnWorld.getSafeSpawnLocation();
/*    */     
/* 41 */     ((PluginCommand)Objects.<PluginCommand>requireNonNull(getCommand("Fly"))).setExecutor((CommandExecutor)new Fly());
/* 42 */     ((PluginCommand)Objects.<PluginCommand>requireNonNull(getCommand("Info"))).setExecutor((CommandExecutor)new Info());
/*    */     
/* 44 */     PluginManager pm = Bukkit.getPluginManager();
/* 45 */     pm.registerEvents((Listener)new CM(), (Plugin)this);
/* 46 */     pm.registerEvents((Listener)new Chat(), (Plugin)this);
/*    */     
/* 48 */     VERSION = "0.1.1";
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {}
/*    */ 
/*    */   
/*    */   public static ItemGenerator getItemGenerator() {
/* 56 */     return ig;
/*    */   }
/*    */   
/*    */   public String getVersion() {
/* 60 */     Properties properties = new Properties();
/*    */     
/*    */     try {
/* 63 */       properties.load(getClassLoader().getResourceAsStream("project.properties"));
/*    */     }
/* 65 */     catch (IOException e) {
/* 66 */       throw new RuntimeException(e);
/*    */     } 
/*    */     
/* 69 */     return properties.getProperty("version");
/*    */   }
/*    */ }


/* Location:              H:\Downloads\ItemGenerator-0.1.0.jar!\de\cruelambition\itemgenerator\ItemGenerator.class
 * Java compiler version: 16 (60.0)
 * JD-Core Version:       1.1.3
 */