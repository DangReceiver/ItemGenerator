package de.cruelambition.worlds;

import de.cruelambition.itemgenerator.ItemGenerator;
import de.cruelambition.language.Lang;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import de.cruelambition.language.Language;
import de.cruelambition.oo.PC;
import de.cruelambition.oo.Utils;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpawnWorld implements Listener {

	public static List<Biome> biomeHolder;

	public static class SpawnGen extends ChunkGenerator {

		@NotNull
		public ChunkGenerator.ChunkData generateChunkData(@NotNull World world, @NotNull Random
				random, int chunkX, int chunkZ, @NotNull ChunkGenerator.BiomeGrid biome) {

			ChunkGenerator.ChunkData chunkData = createChunkData(world);
			for (int x = 0; x < 24; x++)
				for (int z = 0; z < 24; z++)
					biome.setBiome(x, z, Biome.PLAINS);
			return chunkData;
		}

		public Biome getRandomBiome() {
			if (biomeHolder.isEmpty()) fillBiomeList();
			return biomeHolder.get(new Random().nextInt(biomeHolder.size() - 1));

		}

		public static void fillBiomeList() {
			biomeHolder.addAll(Arrays.asList(Biome.values()));
		}

		public static boolean checkExists(String name) {
			World w = Bukkit.getWorld(name);
			return w != null;
		}

		public static void checkCreate(String name, boolean gameRules) {
			World w = Bukkit.getWorld(name);
			if (w != null) return;

			WorldCreator creator = new WorldCreator(name);
			creator.generator(new SpawnGen());
			creator.createWorld();

			if (gameRules)
				setGameRules(Objects.requireNonNull(w = Bukkit.getWorld(name)));
		}

		@Nullable
		public static World getWorld(String name) {
			return checkExists(name) ?
					Bukkit.getWorld(name) : null;
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
			spawnLoc = new Location(Bukkit.getWorld("Spawn"), 0.5D, 64.52, 0.5D);

			c.set("Locations.Spawn.Spawn", spawnLoc);
			c.set("Locations.Spawn.ButtonLocation", spawnLoc.clone().add(-0.5, -0.52, +8.5));
			ItemGenerator.getItemGenerator().saveConfig();
		}

//		isSpawnSafe();
		return spawnLoc.clone().add(0, 0.5, 0);
	}

	public static boolean isSpawnSafe() {
		boolean b = SpawnGen.checkExists("Spawn");

		Location c = ItemGenerator.getItemGenerator().getConfig()
				.getLocation("Locations.Spawn.Spawn");
		ConsoleCommandSender cs = Bukkit.getConsoleSender();

		if (c == null || c.getWorld() == null) {
			Location sp = new Location(Bukkit.getWorld("Spawn"), 0.5D, 65.02, 0.5D);
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

	@EventHandler
	public void handle(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		Block cb = e.getClickedBlock();

		if (cb == null) return;
		if (cb.getWorld() != Bukkit.getWorld("Spawn")) return;

		if (!cb.getType().toString().contains("BUTTON")) return;
		FileConfiguration c = ItemGenerator.getItemGenerator().getConfig();

		if (!c.isSet("Locations.Spawn.ButtonLocation")) return;
		if (!cb.equals(c.getLocation("Locations.Spawn.ButtonLocation").getBlock())) return;

		PC pc = new PC(p);

		if (pc.hasLogoutLocation()) p.teleport(pc.getLogoutLocation());
		else p.teleport(new Location(Bukkit.getWorld("world"), 0.5, 65.02, 0.5));

		int i = 0;
		Utils.oneByOne(p, Sound.BLOCK_NOTE_BLOCK_BASS, 3, 0.85f, 0,
				false, 0.35f, 4, i);

		p.setGameMode(GameMode.SURVIVAL);
	}

	@EventHandler
	public void handle(EntityDamageEvent e) {
		Entity en = e.getEntity();
		if (!(en instanceof Player p)) return;

		if (p.getWorld() != Bukkit.getWorld("Spawn")) return;

		if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
			e.setCancelled(true);
			return;
		}
		if (e.getCause() != EntityDamageEvent.DamageCause.VOID) return;

		e.setCancelled(true);
		p.teleport(getSafeSpawnLocation().clone().add(0, 0.5, 0));

		Bukkit.getScheduler().runTaskLater(ItemGenerator.getItemGenerator(), () ->
				Utils.oneByOne(p, Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 4, 0.85f,
						0.05f, true, 0.5f, 2, 0), 3);
	}
}