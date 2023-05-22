/*    */ package de.cruelambition.language;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.io.File;
/*    */ import net.md_5.bungee.api.ChatColor;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class Lang
/*    */   extends Language
/*    */ {
/* 12 */   public static String PRE = "§9ItemGenerator";
/*    */   
/*    */   private File lf;
/*    */   
/*    */   private Player p;
/*    */   private String last;
/*    */   private final Language lang;
/*    */   
/*    */   public Lang(Player player) {
/* 21 */     this.lang = new Language();
/* 22 */     this.p = player;
/* 23 */     this.lf = this.lang.getLang(this.p.isOnline() ? this.p : null);
/*    */   }
/*    */   
/*    */   public void setPlayerLanguage(File pLang) {
/* 27 */     if (this.p != null) this.lang.setPlayerLang(this.p, pLang); 
/*    */   }
/*    */   
/*    */   public void setPlayer(Player player) {
/* 31 */     this.lf = this.lang.getLang(this.p = player);
/*    */   }
/*    */   
/*    */   public String getString(String key) {
/* 35 */     return Language.getMessage(this.lf, this.last = key);
/*    */   }
/*    */   
/*    */   public String replaceString(String key, String toReplace, String... replacements) {
/* 39 */     String temp = Language.getMessage(this.lf, this.last = key);
/*    */     
/* 41 */     for (String s : replacements) {
/* 42 */       temp = temp.replaceAll(toReplace, s);
/*    */     }
/* 44 */     return temp;
/*    */   }
/*    */   
/*    */   public String replaceColor(String input, Color... color) {
/* 48 */     for (Color c : color)
/* 49 */       input = input.replace("§c", "" + 
/* 50 */           colorFromRGB(c.getRed(), c.getGreen(), c.getBlue())); 
/* 51 */     return input;
/*    */   }
/*    */   
/*    */   public String formatString(String key, String... replacements) {
/* 55 */     return String.format(Language.getMessage(this.lf, this.last = key), (Object[])replacements);
/*    */   }
/*    */ 
/*    */   
/*    */   public static ChatColor colorFromRGB(int r, int g, int b) {
/* 60 */     return ChatColor.of(new Color(r, g, b));
/*    */   }
/*    */   
/*    */   public String getLast() {
/* 64 */     return this.last;
/*    */   }
/*    */ 
/*    */   
/*    */   public static ChatColor getColor(int r, int g, int b) {
/* 69 */     return ChatColor.of("#" + r + g + b);
/*    */   }
/*    */   
/*    */   public static void broadcast(String key) {
/* 73 */     Bukkit.getConsoleSender().sendMessage(getMessage(getServerLang(), "info") + getMessage(getServerLang(), "info"));
/*    */ 
/*    */     
/* 76 */     for (Player ap : Bukkit.getOnlinePlayers()) {
/* 77 */       ap.sendMessage(PRE + PRE);
/*    */     }
/*    */   }
/*    */   
/*    */   public static void broadcastArg(String key, String... arg) {
/* 82 */     Bukkit.getConsoleSender().sendMessage(getMessage(getServerLang(), "broadcast") + getMessage(getServerLang(), "broadcast"));
/*    */ 
/*    */     
/* 85 */     for (Player ap : Bukkit.getOnlinePlayers()) {
/* 86 */       String format = String.format(getMessage((new Lang(ap)).getLang(ap), key), (Object[])arg);
/* 87 */       ap.sendMessage(PRE + PRE);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              H:\Downloads\ItemGenerator-0.1.0.jar!\de\cruelambition\language\Lang.class
 * Java compiler version: 16 (60.0)
 * JD-Core Version:       1.1.3
 */