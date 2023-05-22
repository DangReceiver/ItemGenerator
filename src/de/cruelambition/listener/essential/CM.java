/*    */ package de.cruelambition.listener.essential;
/*    */ 
/*    */ import de.cruelambition.language.Lang;
/*    */ import de.cruelambition.oo.PC;
/*    */ import de.cruelambition.worlds.SpawnWorld;
/*    */ import java.io.File;
/*    */ import java.util.Random;
/*    */ import org.bukkit.GameMode;
/*    */ import org.bukkit.OfflinePlayer;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.player.PlayerJoinEvent;
/*    */ import org.bukkit.event.player.PlayerQuitEvent;
/*    */ 
/*    */ public class CM
/*    */   implements Listener
/*    */ {
/*    */   @EventHandler
/*    */   public void handle(PlayerJoinEvent e) {
/* 21 */     Player p = e.getPlayer();
/* 22 */     PC pc = new PC((OfflinePlayer)p);
/*    */     
/* 24 */     if (pc.getLanguageString() == null) {
/* 25 */       pc.setLanguage(Lang.getServerLang());
/*    */     }
/* 27 */     pc.setJoinTime(System.currentTimeMillis());
/* 28 */     pc.savePCon();
/*    */     
/* 30 */     Lang l = new Lang(p);
/* 31 */     File lf = pc.getLanguage();
/* 32 */     l.setPlayerLanguage(lf);
/*    */     
/* 34 */     e.setJoinMessage(null);
/* 35 */     if (!p.hasPlayedBefore())
/* 36 */     { Lang.broadcastArg("player_first_join", new String[] { p.getName() });
/* 37 */       p.teleport(SpawnWorld.getSafeSpawnLocation());
/*    */        }
/*    */     
/* 40 */     else if (pc.getJoinTime() - pc.getQuitTime() <= 20000L)
/* 41 */     { Lang.broadcastArg("player_rejoin_" + (new Random())
/* 42 */           .nextInt(4), new String[] { p.getName() }); }
/* 43 */     else { Lang.broadcastArg("player_join_" + (new Random())
/* 44 */           .nextInt(17), new String[] { p.getName() }); }
/*    */ 
/*    */     
/* 47 */     p.setGameMode(GameMode.ADVENTURE);
/*    */     
/* 49 */     p.sendTitle(Lang.PRE, String.format(Lang.getMessage(lf, "welcome_back"), new Object[] { p.getName() }), 30, 50, 50);
/*    */   }
/*    */   
/*    */   @EventHandler
/*    */   public void handle(PlayerQuitEvent e) {}
/*    */ }


/* Location:              H:\Downloads\ItemGenerator-0.1.0.jar!\de\cruelambition\listener\essential\CM.class
 * Java compiler version: 16 (60.0)
 * JD-Core Version:       1.1.3
 */