/*    */ package exceptions;
/*    */ 
/*    */ public class InvalidStringException
/*    */   extends RuntimeException
/*    */ {
/*    */   public InvalidStringException() {}
/*    */   
/*    */   public InvalidStringException(String message) {
/*  9 */     super(message);
/*    */   }
/*    */   
/*    */   public InvalidStringException(Throwable throwable) {
/* 13 */     super(throwable);
/*    */   }
/*    */ }


/* Location:              H:\Downloads\ItemGenerator-0.1.0.jar!\exceptions\InvalidStringException.class
 * Java compiler version: 16 (60.0)
 * JD-Core Version:       1.1.3
 */