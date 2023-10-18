package de.cruelambition.worlds;

import de.cruelambition.itemgenerator.ItemGenerator;
import de.cruelambition.language.Lang;

import java.util.Objects;
import java.util.Random;

import de.cruelambition.language.Language;
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
		implements Listener {
	public static class SpawnGen
			extends ChunkGenerator {
		@NotNull
		public ChunkGenerator.ChunkData generateChunkData(@NotNull World world, @NotNull Random random,
														  int chunkX, int chunkZ, @NotNull ChunkGenerator.BiomeGrid biome) {

			ChunkGenerator.ChunkData chunkData = createChunkData(world);
			for (int x = 0; x < 24; x++)
				for (int z = 0; z < 24; z++)
					biome.setBiome(x, z, Biome.PLAINS);
			return chunkData;
		}

		public static boolean checkExists() {
			World w = Bukkit.getWorld("world");
			if (w != null) return true;

			WorldCreator creator = new WorldCreator("world");
			creator.generator(new SpawnGen());
			creator.createWorld();

			setGameRules(Objects.<World>requireNonNull(w = Bukkit.getWorld("world")));
			return (w != null);

		}

		@Nullable
		public static World getWorld() {
			return checkExists() ? Bukkit.getWorld("world") : null;
		}

		public static void setGameRules(World w) {
			w.setClearWeatherDuration(0);
			w.setWeatherDuration(2147483647);

			w.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, Boolean.valueOf(false));
			w.setGameRule(GameRule.DO_WEATHER_CYCLE, Boolean.valueOf(false));
			w.setGameRule(GameRule.DO_MOB_SPAWNING, Boolean.valueOf(false));
			w.setGameRule(GameRule.RANDOM_TICK_SPEED, Integer.valueOf(0));
		}
	}

	public static Location getSafeSpawnLocation() {
		FileConfiguration c = ItemGenerator.getItemGenerator().getConfig();
		Location spawnLoc = c.getLocation("Locations.Spawn.Spawn");

		if (spawnLoc == null) {
			spawnLoc = new Location(Bukkit.getWorld("world"), 0.5D, 64.02, 0.5D);

			c.set("Locations.Spawn.Spawn", spawnLoc);
			c.set("Locations.Spawn.ButtonLocation", spawnLoc);
			ItemGenerator.getItemGenerator().saveConfig();
		}

		isSpawnSafe();

		return spawnLoc;
	}

	public static boolean isSpawnSafe() {
		boolean b = SpawnGen.checkExists();

		Location c = ItemGenerator.getItemGenerator().getConfig().getLocation("Locations.Spawn.Spawn");
		ConsoleCommandSender cs = Bukkit.getConsoleSender();

		if (c == null || c.getWorld() == null) {
			Location sp = new Location(Bukkit.getWorld("world"), 0.5D, 64.02, 0.5D);
			ItemGenerator.getItemGenerator().getConfig().set("Locations.Spawn.Spawn", sp);

			ItemGenerator.getItemGenerator().saveConfig();
			c = sp.clone();
		}

		if (c.add(0.0D, -1.0D, 0.0D).getBlock().getType() == Material.AIR) {
			b = false;

			c.getBlock().setType(Material.BEDROCK);
			Bukkit.getScheduler().runTaskLater(ItemGenerator.getItemGenerator(), ()
					-> cs.sendMessage(Lang.PRE + Lang.getMessage(Lang.getServerLang(),
					"spawnworld_spawn_secured")), 1L);


			c.add(0.0D, 1.0D, 0.0D);
		}

		if (!c.getWorld().getGameRuleValue(GameRule.FALL_DAMAGE)) {
			b = false;

			c.getWorld().setGameRule(GameRule.FALL_DAMAGE, Boolean.valueOf(false));
			Bukkit.getScheduler().runTaskLater(ItemGenerator.getItemGenerator(), ()
					-> cs.sendMessage(Lang.PRE + Lang.getMessage(Lang.getServerLang(),
					"spawnworld_spawn_fall_damage_disabled")), 1L);
		}

		if (!b) {
			Bukkit.getScheduler().runTaskLater(ItemGenerator.getItemGenerator(), ()
					-> cs.sendMessage(Lang.PRE + Lang.getMessage(Lang.getServerLang(),
					"spawnworld_spawn_created")), 1L);
		}

		return b;
	}
}