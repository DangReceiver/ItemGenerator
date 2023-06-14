/*    */ package de.cruelambition.language;
/*    */ 
/*    */ import de.cruelambition.oo.PC;
/*    */ import java.util.List;
/*    */ import org.jetbrains.annotations.Nullable;
/*    */ 
/*    */ public class PronounsSetup
/*    */   extends Pronouns
/*    */ {
/*    */   private PC pc;
/*    */   private boolean customPermitted;
/*    */   
/*    */   public PronounsSetup(PC pPc, @Nullable String pCurrent) {
/* 14 */     super(pPc, pCurrent);
/*    */     
/* 16 */     this
/* 17 */       .customPermitted = (this.pc.isSet("preferences.pronouns.custom.permitted") && this.pc.getBoolean("preferences.pronouns.custom.permitted"));
/*    */   }
/*    */   
/*    */   public boolean isCustomPermitted() {
/* 21 */     return this.customPermitted;
/*    */   }
/*    */   
/*    */   public void setCustomPermitted(boolean pPermitted) {
/* 25 */     this.pc.set("preferences.pronouns.custom.permitted", Boolean.valueOf(this.customPermitted = pPermitted));
/*    */   }
/*    */   
/*    */   public int getPronounNum() {
/* 29 */     return this.pc.getInt("preferences.pronouns.used");
/*    */   }
/*    */   
/*    */   public List<String> getPronouns() {
/* 33 */     return PRONOUNS.get(getPronounNum());
/*    */   }
/*    */   
/*    */   public void setPronounNum(int num) {
/* 37 */     this.pc.set("preferences.pronouns.used", Integer.valueOf(num));
/*    */   }
/*    */   
/*    */   public boolean hasCustomPronouns() {
/* 41 */     if (!isCustomPermitted()) return false; 
/* 42 */     return this.pc.getBoolean("preferences.pronouns.custom.isSet");
/*    */   }
/*    */   
/*    */   public void setCustomPronouns(List<String> pronouns) {
/* 46 */     this.pc.set("preferences.pronouns.custom.used", pronouns);
/*    */   }
/*    */   
/*    */   public List<String> getCustomPronouns() {
/* 50 */     return (hasCustomPronouns() && isCustomPermitted()) ? 
/* 51 */       this.pc.getStringList("preferences.pronouns.custom.used") : null;
/*    */   }
/*    */ }


/* Location:              H:\Downloads\ItemGenerator-0.1.0.jar!\de\cruelambition\language\PronounsSetup.class
 * Java compiler version: 16 (60.0)
 * JD-Core Version:       1.1.3
 */