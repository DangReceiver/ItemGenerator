/*    */ package de.cruelambition.listener.essential;
/*    */ 
/*    */ import de.cruelambition.language.Lang;
/*    */ import de.cruelambition.oo.PC;
/*    */ import java.util.List;
/*    */ import net.md_5.bungee.api.ChatColor;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.OfflinePlayer;
/*    */ import org.bukkit.Sound;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.player.AsyncPlayerChatEvent;
/*    */ 
/*    */ public class Chat
/*    */   implements Listener {
/*    */   @EventHandler
/*    */   public void handle(AsyncPlayerChatEvent e) {
/* 19 */     Player p = e.getPlayer();
/* 20 */     PC pc = new PC((OfflinePlayer)p);
/*    */     
/* 22 */     String f = "<prefix> §7‖ <counter_link><player_color><player_name><link> <message_color><message>";
/* 23 */     e.setFormat(f);
/*    */     
/* 25 */     List<Integer> dmc = pc.getDefaultMessageColor();
/* 26 */     ChatColor cc = Lang.colorFromRGB(((Integer)dmc.get(0)).intValue(), ((Integer)dmc.get(1)).intValue(), ((Integer)dmc.get(2)).intValue());
/*    */     
/* 28 */     f = f.replaceAll("<player_name>", p.getName()).replaceAll("<player_color>", pc.getPlayerColor());
/* 29 */     f = f.replaceAll("<counter_link>", pc.getCounterLink()).replaceAll("<link>", pc.getLink());
/*    */     
/* 31 */     f = f.replaceAll("<message_color>", "" + cc).replaceAll("<message>", e.getMessage());
/* 32 */     f = f.replaceAll("<prefix>", Lang.PRE);
/*    */     
/* 34 */     if (p.hasPermission("Savior.Chat.Color")) f = f.replaceAll("&", "§");
/*    */     
/* 36 */     if (p.hasPermission("Savior.Chat.Tagging")) {
/* 37 */       for (Player ap : Bukkit.getOnlinePlayers()) {
/* 38 */         if (f.contains(" " + ap.getName())) {
/* 39 */           f = f.replaceAll(" " + ap.getName(), " §" + Lang.colorFromRGB(20, 210, 80) + "§o@" + ap
/* 40 */               .getName() + cc);
/* 41 */           ap.playSound(ap.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 0.4F, 1.1F);
/*    */         } 
/*    */       } 
/*    */     }
/* 45 */     e.setFormat(f);
/*    */   }
/*    */ }


/* Location:              H:\Downloads\ItemGenerator-0.1.0.jar!\de\cruelambition\listener\essential\Chat.class
 * Java compiler version: 16 (60.0)
 * JD-Core Version:       1.1.3
 */