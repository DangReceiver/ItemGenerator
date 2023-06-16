package de.cruelambition.worlds;

import java.util.Objects;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpawnGen extends ChunkGenerator {
  @NotNull
  public ChunkGenerator.ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkGenerator.BiomeGrid biome) {
/* 30 */     ChunkGenerator.ChunkData chunkData = createChunkData(world);
/* 31 */     for (int x = 0; x < 24; x++) {
/* 32 */       for (int z = 0; z < 24; z++)
/* 33 */         biome.setBiome(x, z, Biome.PLAINS); 
    } 
/* 35 */     return chunkData;
  }
  
  public static boolean checkExists() {
/* 39 */     World w = Bukkit.getWorld("world");
    
/* 41 */     if (w == null) {
/* 42 */       WorldCreator creator = new WorldCreator("world");
/* 43 */       creator.generator(new SpawnGen());
/* 44 */       creator.createWorld();
      
/* 46 */       setGameRules(Objects.<World>requireNonNull(w = Bukkit.getWorld("world")));
/* 47 */       return (w != null);
    } 
/* 49 */     return true;
  }
  
  @Nullable
  public static World getWorld() {
/* 54 */     return checkExists() ? Bukkit.getWorld("world") : null;
  }
  
  public static void setGameRules(World w) {
/* 58 */     w.setGameRule(GameRule.DO_WEATHER_CYCLE, Boolean.valueOf(false));
/* 59 */     w.setClearWeatherDuration(0);
/* 60 */     w.setWeatherDuration(2147483647);
    
/* 62 */     w.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, Boolean.valueOf(false));
/* 63 */     w.setGameRule(GameRule.DO_WEATHER_CYCLE, Boolean.valueOf(false));
/* 64 */     w.setGameRule(GameRule.DO_MOB_SPAWNING, Boolean.valueOf(false));
/* 65 */     w.setGameRule(GameRule.RANDOM_TICK_SPEED, Integer.valueOf(0));
  }
}


/* Location:              H:\Downloads\ItemGenerator-0.1.0.jar!\de\cruelambition\worlds\SpawnWorld$SpawnGen.class
 * Java compiler version: 16 (60.0)
 * JD-Core Version:       1.1.3
 */