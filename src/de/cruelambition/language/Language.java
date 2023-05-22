/*     */ package de.cruelambition.language;
/*     */ 
/*     */ import de.cruelambition.itemgenerator.ItemGenerator;
/*     */ import exceptions.InvalidStringException;
/*     */ import java.io.File;
/*     */ import java.io.InputStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.command.ConsoleCommandSender;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
/*     */ import org.bukkit.configuration.file.YamlConfiguration;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ 
/*     */ public class Language {
/*     */   private static File sLang;
/*  23 */   private final Map<Player, File> settings = new HashMap<>(); private static File df; private String lp;
/*  24 */   private static final Map<File, Map<String, String>> messages = new HashMap<>();
/*     */   
/*     */   public Language() {
/*  27 */     df = ItemGenerator.getItemGenerator().getDataFolder();
/*  28 */     this.lp = "" + df + "/Languages";
/*     */     
/*  30 */     FileConfiguration c = ItemGenerator.getItemGenerator().getConfig();
/*  31 */     sLang = getLangFile(c.isSet("Lang") ? c.getString("Lang") : setDefaultLang());
/*     */   }
/*     */   
/*     */   public String setDefaultLang() {
/*  35 */     FileConfiguration c = ItemGenerator.getItemGenerator().getConfig();
/*  36 */     c.set("Lang", "en");
/*     */     
/*  38 */     ItemGenerator.getItemGenerator().saveConfig();
/*  39 */     return c.getString("Lang", "en");
/*     */   }
/*     */   
/*     */   public static void setSLang(File serverLang) {
/*  43 */     sLang = serverLang;
/*     */   }
/*     */   
/*     */   public File getDf() {
/*  47 */     return df;
/*     */   }
/*     */   
/*     */   public static boolean isValid(File lang, String key) {
/*  51 */     return (messages.get(lang) != null && ((Map)messages.get(lang)).get(key) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String invalidString(String key) {
/*     */     try {
/*  57 */       String s = (messages != null && messages.get(getServerLang()) != null) ? (String)((Map)messages.get(getServerLang())).get(key) : "§oString not loaded yet!";
/*  58 */       s = s.split("a")[1];
/*     */     }
/*  60 */     catch (InvalidStringException ex) {
/*  61 */       ex.printStackTrace();
/*     */     } 
/*     */     
/*  64 */     return (messages != null && messages.get(getServerLang()) != null) ? 
/*  65 */       String.format((String)((Map)messages.get(getServerLang())).get("string_not_found"), new Object[] { key }) : "§oString not loaded yet!";
/*     */   }
/*     */   
/*     */   public File getLang(Player p) {
/*  69 */     return (!this.settings.isEmpty() && this.settings.get(p) != null) ? this.settings.get(p) : getServerLang();
/*     */   }
/*     */   
/*     */   public static File getServerLang() {
/*  73 */     return sLang;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getLanguages() {
/*  83 */     List<String> langFiles = new ArrayList<>();
/*  84 */     File folder = new File("" + df + "/languages");
/*     */     
/*  86 */     if (folder.listFiles() == null) throw new RuntimeException("§cLanguage folder is empty!!"); 
/*  87 */     for (File file : folder.listFiles()) langFiles.add(file.getName());
/*     */     
/*  89 */     return langFiles;
/*     */   }
/*     */   
/*     */   public void setServerLanguage(File language) {
/*  93 */     ItemGenerator itemGenerator = ItemGenerator.getItemGenerator();
/*  94 */     FileConfiguration c = itemGenerator.getConfig();
/*  95 */     List<String> langFiles = getLanguages();
/*     */     
/*  97 */     if (language == null)
/*  98 */     { if (!c.isSet("Language")) {
/*  99 */         c.set("Language", "en");
/* 100 */         itemGenerator.saveConfig();
/*     */       } 
/*     */       
/* 103 */       if (!langFiles.contains(c.getString("Language") + ".yml"))
/* 104 */         return;  sLang = new File("" + df + "/languages", c.getString("Language") + ".yml"); }
/*     */     
/* 106 */     else if (langFiles.contains(language.getName())) { sLang = language; }
/*     */   
/*     */   }
/*     */   public void setPlayerLang(Player p, File file) {
/* 110 */     removePlayer(p);
/* 111 */     this.settings.put(p, getLangFile("en"));
/*     */     
/* 113 */     if (!file.exists())
/* 114 */       return;  this.settings.put(p, file);
/*     */   }
/*     */   
/*     */   public static boolean isLangFile(String lang) {
/* 118 */     return (new File("" + df + "/languages", lang + ".yml")).exists();
/*     */   }
/*     */   
/*     */   public static File getLangFile(String lang) {
/* 122 */     File file = new File("" + df + "languages", lang + ".yml");
/* 123 */     if (file == null) throw new RuntimeException("The resulting lang file is not existing!"); 
/* 124 */     return file;
/*     */   }
/*     */   
/*     */   public void removePlayer(Player p) {
/* 128 */     this.settings.remove(p);
/*     */   }
/*     */   
/*     */   public static File getLangFolder() {
/* 132 */     return new File("" + df + "/languages");
/*     */   }
/*     */   
/*     */   public static void loadResources() {
/* 136 */     ItemGenerator itemGenerator = ItemGenerator.getItemGenerator();
/* 137 */     List<File> resources = new ArrayList<>(List.of(getLangFile("en.yml")));
/*     */     
/* 139 */     for (File f : resources) {
/* 140 */       Path target = f.toPath();
/* 141 */       String s = f.getName();
/*     */       
/* 143 */       InputStream in = itemGenerator.getResource(s);
/* 144 */       ConsoleCommandSender cs = Bukkit.getConsoleSender();
/*     */       
/* 146 */       if (in == null) {
/* 147 */         Bukkit.getScheduler().runTaskLaterAsynchronously((Plugin)itemGenerator, () -> cs.sendMessage(getMessage(getServerLang(), "error") + getMessage(getServerLang(), "error")), 0L);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/*     */       try {
/* 153 */         Files.copy(in, getCache().toPath(), new java.nio.file.CopyOption[0]);
/* 154 */         File tempFile = new File("" + getCache().toPath() + getCache().toPath());
/*     */         
/* 156 */         if (tempFile.length() <= getLangFile(s).length()) {
/* 157 */           tempFile.delete();
/*     */           
/*     */           continue;
/*     */         } 
/* 161 */         Files.copy(in, target, new java.nio.file.CopyOption[0]);
/* 162 */         Bukkit.getScheduler().runTaskLaterAsynchronously((Plugin)itemGenerator, () -> cs.sendMessage(getMessage(getServerLang(), "error") + getMessage(getServerLang(), "error")), 0L);
/*     */       }
/* 164 */       catch (Exception ex) {
/* 165 */         ex.printStackTrace();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static File getCache() {
/* 171 */     return new File("" + df + "/cache");
/*     */   }
/*     */   
/*     */   public static void loadMessages() {
/* 175 */     File langF = getLangFolder();
/*     */     
/* 177 */     if (getServerLang() == null) setSLang(new File(langF, "en.yml")); 
/* 178 */     loadCustomLanguages(langF);
/*     */   }
/*     */   
/*     */   public static String getMessage(File lang, String key) {
/* 182 */     return isValid(lang, key) ? (String)((Map)messages.get(lang)).get(key) : (isValid(getServerLang(), key) ? 
/* 183 */       (String)((Map)messages.get(getServerLang())).get(key) : (isValid(lang, "string_not_found") ? 
/* 184 */       String.format((String)((Map)messages.get(lang)).get("string_not_found"), new Object[] { key }) : invalidString(key)));
/*     */   }
/*     */ 
/*     */   
/*     */   public static void loadCustomLanguages(File langF) {
/* 189 */     if (langF == null || langF.listFiles() == null || (langF.listFiles()).length == 0) {
/* 190 */       loadResources();
/*     */       
/*     */       return;
/*     */     } 
/* 194 */     Bukkit.getConsoleSender().sendMessage("0");
/*     */     
/* 196 */     for (File file : langF.listFiles()) {
/* 197 */       Map<String, String> lm = new HashMap<>();
/* 198 */       YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
/*     */       
/* 200 */       Bukkit.getConsoleSender().sendMessage("1");
/* 201 */       for (String key : yamlConfiguration.getKeys(false)) {
/* 202 */         for (String messName : yamlConfiguration.getConfigurationSection(key).getKeys(false)) {
/*     */           
/* 204 */           String message = ChatColor.translateAlternateColorCodes('§', yamlConfiguration.getString(key + "." + key));
/* 205 */           lm.put(messName, message);
/*     */         } 
/*     */       } 
/*     */       
/* 209 */       Bukkit.getConsoleSender().sendMessage("2");
/* 210 */       messages.put(file, lm);
/* 211 */       if (isLangFile(file.getName().split(".yml")[0])) {
/*     */         
/* 213 */         Bukkit.getConsoleSender().sendMessage("3");
/* 214 */         Bukkit.getConsoleSender().sendMessage("msg: " + ((messages == null) ? 1 : 0));
/* 215 */         Bukkit.getConsoleSender().sendMessage(Lang.PRE + Lang.PRE);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              H:\Downloads\ItemGenerator-0.1.0.jar!\de\cruelambition\language\Language.class
 * Java compiler version: 16 (60.0)
 * JD-Core Version:       1.1.3
 */