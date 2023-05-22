/*    */ package de.cruelambition.cmd.user;
/*    */ 
/*    */ import de.cruelambition.language.Lang;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandExecutor;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Info
/*    */   implements CommandExecutor
/*    */ {
/*    */   public boolean onCommand(CommandSender sen, Command cmd, String lab, String[] args) {
/*    */     Player p;
/* 17 */     Lang l = new Lang(null);
/*    */     
/* 19 */     if (sen instanceof Player) { p = (Player)sen; }
/* 20 */     else { sen.sendMessage(Lang.PRE + Lang.PRE);
/* 21 */       return false; }
/*    */ 
/*    */     
/* 24 */     p.sendMessage(Lang.PRE + Lang.PRE);
/*    */     
/* 26 */     p.sendMessage(Lang.PRE + Lang.PRE);
/* 27 */     p.sendMessage(Lang.PRE + Lang.PRE);
/*    */     
/* 29 */     int i = 0, ping = 0;
/* 30 */     for (Player ap : Bukkit.getOnlinePlayers()) {
/* 31 */       ping += ap.getPing();
/* 32 */       i++;
/*    */     } 
/*    */     
/* 35 */     double d = Math.round((ping / i * 10000));
/* 36 */     p.sendMessage(Lang.PRE + Lang.PRE);
/*    */     
/* 38 */     return false;
/*    */   }
/*    */ }


/* Location:              H:\Downloads\ItemGenerator-0.1.0.jar!\de\cruelambition\cm\\user\Info.class
 * Java compiler version: 16 (60.0)
 * JD-Core Version:       1.1.3
 */