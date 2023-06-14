/*    */ package de.cruelambition.language;
/*    */ 
/*    */ import de.cruelambition.oo.PC;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import javax.annotation.Nullable;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class Pronouns
/*    */   extends Language {
/*    */   private PC pc;
/*    */   private Player p;
/*    */   private String current;
/*    */   private String last;
/*    */   private Lang l;
/*    */   public static List<String> REPLACEMENTS;
/*    */   public static List<List<String>> PRONOUNS;
/*    */   
/*    */   public Pronouns(PC pPc, @Nullable String pCurrent) {
/* 21 */     this.pc = pPc;
/* 22 */     this.p = this.pc.thisPlayer();
/*    */     
/* 24 */     this.current = pCurrent;
/* 25 */     this.last = null;
/*    */     
/* 27 */     this.l = new Lang(this.p);
/* 28 */     REPLACEMENTS = new ArrayList<>(Arrays.asList(new String[] { "@pc1 @pc2 @pc3 @name" }));
/*    */   }
/*    */   
/*    */   public String fromKey(String key) {
/* 32 */     this.last = this.current;
/* 33 */     return Lang.getMessage(this.l.getLang(this.p), key);
/*    */   }
/*    */   
/*    */   public void setFromKey(String key) {
/* 37 */     this.last = this.current;
/* 38 */     this.current = Lang.getMessage(this.l.getLang(this.p), key);
/*    */   }
/*    */   
/*    */   public void fromString(String string) {
/* 42 */     this.last = this.current;
/* 43 */     this.current = string;
/*    */   }
/*    */   
/*    */   public String getCurrent() {
/* 47 */     return this.current;
/*    */   }
/*    */   
/*    */   public boolean unset() {
/* 51 */     return (this.current == null);
/*    */   }
/*    */   
/*    */   public boolean hasPronounsReplacement() {
/* 55 */     if (unset()) return false;
/*    */     
/* 57 */     for (String s : REPLACEMENTS) {
/* 58 */       if (this.current.contains(s)) return true; 
/*    */     } 
/* 60 */     return false;
/*    */   }
/*    */   
/*    */   public List<String> getCustomPronouns() {
/* 64 */     PronounsSetup ps = new PronounsSetup(this.pc, this.current);
/* 65 */     return ps.getCustomPronouns();
/*    */   }
/*    */   
/*    */   public List<String> getPronouns() {
/* 69 */     return (new PronounsSetup(this.pc, this.current)).getPronouns();
/*    */   }
/*    */   
/*    */   public List<String> getUndefinedPronouns() {
/* 73 */     PronounsSetup ps = new PronounsSetup(this.pc, this.current);
/*    */     
/* 75 */     if (ps.isCustomPermitted() && ps.hasCustomPronouns()) {
/* 76 */       return ps.getCustomPronouns();
/*    */     }
/* 78 */     return ps.getPronouns();
/*    */   }
/*    */ 
/*    */   
/*    */   public void replace() {
/* 83 */     if (!hasPronounsReplacement())
/*    */       return; 
/* 85 */     PronounsSetup ps = new PronounsSetup(this.pc, this.current);
/* 86 */     List<String> pronouns = ps.hasCustomPronouns() ? ps.getCustomPronouns() : ps.getPronouns();
/*    */     
/* 88 */     int i = 0;
/* 89 */     for (String s : REPLACEMENTS) {
/* 90 */       if (this.current.contains(s))
/* 91 */         this.current.replaceAll(s, pronouns.get(i)); 
/* 92 */       i++;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              H:\Downloads\ItemGenerator-0.1.0.jar!\de\cruelambition\language\Pronouns.class
 * Java compiler version: 16 (60.0)
 * JD-Core Version:       1.1.3
 */