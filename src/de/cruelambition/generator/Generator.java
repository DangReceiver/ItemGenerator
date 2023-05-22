/*    */ package de.cruelambition.generator;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.bukkit.Material;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Generator
/*    */ {
/*    */   private List<Material> material;
/*    */   
/*    */   public Material getRandomMaterial() {
/* 16 */     return Material.AIR;
/*    */   }
/*    */   
/*    */   public Material getMaterialFromInt() {
/* 20 */     return Material.AIR;
/*    */   }
/*    */   
/*    */   public List<Material> getMaterialList() {
/* 24 */     return this.material;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void startLoop() {}
/*    */ 
/*    */   
/*    */   public boolean isForbiddenItem() {
/* 33 */     return false;
/*    */   }
/*    */ }


/* Location:              H:\Downloads\ItemGenerator-0.1.0.jar!\de\cruelambition\generator\Generator.class
 * Java compiler version: 16 (60.0)
 * JD-Core Version:       1.1.3
 */