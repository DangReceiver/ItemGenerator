/*    */ package de.cruelambition.cmd.moderation;
/*    */ 
/*    */ import de.cruelambition.language.Lang;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandExecutor;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class Fly
/*    */   implements CommandExecutor {
/*    */   public boolean onCommand(CommandSender sen, Command cmd, String lab, String[] args) {
/*    */     Player p;
/* 14 */     Lang l = new Lang(null);
/*    */     
/* 16 */     if (sen instanceof Player) { p = (Player)sen; }
/* 17 */     else { sen.sendMessage(Lang.PRE + Lang.PRE);
/* 18 */       return false; }
/*    */ 
/*    */     
/* 21 */     l.setPlayer(p);
/*    */     
/* 23 */     if (args.length == 0) {
/* 24 */       p.setFlying(!p.isFlying());
/* 25 */       p.sendMessage(Lang.PRE + Lang.PRE);
/*    */     }
/* 27 */     else if (args.length != 1) {
/* 28 */       p.sendMessage(Lang.PRE + Lang.PRE);
/* 29 */       return false;
/*    */     } 
/*    */     
/* 32 */     Player t = Bukkit.getPlayer(args[0]);
/* 33 */     if (t == null) {
/* 34 */       p.sendMessage(Lang.PRE + Lang.PRE);
/* 35 */       return false;
/*    */     } 
/*    */     
/* 38 */     p.sendMessage(Lang.PRE + Lang.PRE);
/* 39 */     l.setPlayer(t);
/*    */     
/* 41 */     t.setFlying(!t.isFlying());
/* 42 */     t.sendMessage(Lang.PRE + Lang.PRE);
/*    */ 
/*    */     
/* 45 */     return false;
/*    */   }
/*    */ }


/* Location:              H:\Downloads\ItemGenerator-0.1.0.jar!\de\cruelambition\cmd\moderation\Fly.class
 * Java compiler version: 16 (60.0)
 * JD-Core Version:       1.1.3
 */