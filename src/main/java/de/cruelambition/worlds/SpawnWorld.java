package de.cruelambition.worlds;

import de.cruelambition.itemgenerator.ItemGenerator;
import de.cruelambition.language.Lang;
import java.util.Objects;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Biome;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpawnWorld
  implements Listener
{
  public static class SpawnGen
    extends ChunkGenerator
  {
    @NotNull
    public ChunkGenerator.ChunkData generateChunkData(@NotNull World world, @NotNull Random random,
                                                      int chunkX, int chunkZ, @NotNull ChunkGenerator.BiomeGrid biome) {
/*  30 */       ChunkGenerator.ChunkData chunkData = createChunkData(world);
/*  31 */       for (int x = 0; x < 24; x++) {
/*  32 */         for (int z = 0; z < 24; z++)
/*  33 */           biome.setBiome(x, z, Biome.PLAINS); 
      } 
/*  35 */       return chunkData;
    }
    
    public static boolean checkExists() {
/*  39 */       World w = Bukkit.getWorld("world");
      
/*  41 */       if (w == null) {
/*  42 */         WorldCreator creator = new WorldCreator("world");
/*  43 */         creator.generator(new SpawnGen());
/*  44 */         creator.createWorld();
        
/*  46 */         setGameRules(Objects.<World>requireNonNull(w = Bukkit.getWorld("world")));
/*  47 */         return (w != null);
      } 
/*  49 */       return true;
    }
    
    @Nullable
    public static World getWorld() {
/*  54 */       return checkExists() ? Bukkit.getWorld("world") : null;
    }
    
    public static void setGameRules(World w) {
/*  58 */       w.setGameRule(GameRule.DO_WEATHER_CYCLE, Boolean.valueOf(false));
/*  59 */       w.setClearWeatherDuration(0);
/*  60 */       w.setWeatherDuration(2147483647);
      
/*  62 */       w.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, Boolean.valueOf(false));
/*  63 */       w.setGameRule(GameRule.DO_WEATHER_CYCLE, Boolean.valueOf(false));
/*  64 */       w.setGameRule(GameRule.DO_MOB_SPAWNING, Boolean.valueOf(false));
/*  65 */       w.setGameRule(GameRule.RANDOM_TICK_SPEED, Integer.valueOf(0));
    }
  }
  
  public static Location getSafeSpawnLocation() {
/*  70 */     FileConfiguration c = ItemGenerator.getItemGenerator().getConfig();
/*  71 */     Location spawnLoc = c.getLocation("Locations.Spawn.Spawn");
    
/*  73 */     if (spawnLoc == null) {
/*  74 */       spawnLoc = new Location(Bukkit.getWorld("world"), 0.5D, 64.02, 0.5D);
      
/*  76 */       c.set("Locations.Spawn.Spawn", spawnLoc);
/*  77 */       c.set("Locations.Spawn.ButtonLocation", spawnLoc);
/*  78 */       ItemGenerator.getItemGenerator().saveConfig();
    } 
    
/*  81 */     isSpawnSafe();
    
/*  83 */     return spawnLoc;
  }
  
  public static boolean isSpawnSafe() {
/*  87 */     boolean b = true;
/*  88 */     if (!SpawnGen.checkExists()) b = false;
    
/*  90 */     Location c = ItemGenerator.getItemGenerator().getConfig().getLocation("Locations.Spawn.Spawn");
/*  91 */     ConsoleCommandSender cs = Bukkit.getConsoleSender();
    
/*  93 */     if (c == null || c.getWorld() == null) {
/*  94 */       Location sp = new Location(Bukkit.getWorld("world"), 0.5D, 64.02, 0.5D);
/*  95 */       ItemGenerator.getItemGenerator().getConfig().set("Locations.Spawn.Spawn", sp);
/*  96 */       ItemGenerator.getItemGenerator().saveConfig();
/*  97 */       c = sp.clone();
    } 
    
/* 100 */     if (c.add(0.0D, -1.0D, 0.0D).getBlock().getType() == Material.AIR) {
/* 101 */       b = false;
      
/* 103 */       c.getBlock().setType(Material.BEDROCK);
/* 104 */       Bukkit.getScheduler().runTaskLater((Plugin)ItemGenerator.getItemGenerator(), ()
              -> cs.sendMessage(Lang.PRE + Lang.PRE), 1L);

      
/* 107 */       c.add(0.0D, 1.0D, 0.0D);
    } 

    
/* 111 */     if (!((Boolean)c.getWorld().getGameRuleValue(GameRule.FALL_DAMAGE)).booleanValue()) {
/* 112 */       b = false;
      
/* 114 */       c.getWorld().setGameRule(GameRule.FALL_DAMAGE, Boolean.valueOf(false));
/* 115 */       Bukkit.getScheduler().runTaskLater((Plugin)ItemGenerator.getItemGenerator(),
              () -> cs.sendMessage(Lang.PRE + Lang.PRE), 1L);
    } 


    
/* 120 */     if (!b) {
/* 121 */       cs.sendMessage(Lang.PRE + Lang.PRE);
    }
    
/* 124 */     return b;
  }
}