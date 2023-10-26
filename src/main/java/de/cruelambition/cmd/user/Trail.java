package de.cruelambition.cmd.user;

import de.cruelambition.itemgenerator.ItemGenerator;
import de.cruelambition.language.Lang;
import de.cruelambition.oo.PC;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Trail implements CommandExecutor, TabCompleter {

	private static List<String> trail = new ArrayList<>();
	private static List<Particle> directional = new ArrayList<>(Arrays.asList(Particle.EXPLOSION_NORMAL,
			Particle.FIREWORKS_SPARK, Particle.WATER_BUBBLE, Particle.WATER_WAKE, Particle.CRIT, Particle.CRIT_MAGIC,
			Particle.SMOKE_NORMAL, Particle.SMOKE_LARGE, Particle.PORTAL, Particle.ENCHANTMENT_TABLE,
			Particle.FLAME, Particle.CLOUD, Particle.DRAGON_BREATH, Particle.END_ROD, Particle.DAMAGE_INDICATOR,
			Particle.TOTEM, Particle.SPIT, Particle.SQUID_INK, Particle.BUBBLE_POP, Particle.BUBBLE_COLUMN_UP,
			Particle.NAUTILUS, Particle.CAMPFIRE_COSY_SMOKE, Particle.CAMPFIRE_SIGNAL_SMOKE, Particle.SOUL_FIRE_FLAME,
			Particle.SOUL, Particle.REVERSE_PORTAL, Particle.SMALL_FLAME, Particle.ELECTRIC_SPARK,
			Particle.SCRAPE, Particle.WAX_OFF, Particle.WAX_ON));

	public static String PERMISSION = "ItemGenerator.Trail";

	@Override
	public boolean onCommand(CommandSender sen, Command cmd, String lab, String[] args) {
		Player p;
		Lang l = new Lang(null);

		if (sen instanceof Player) {
			p = (Player) sen;
			l.setPlayer(p);

		} else {
			sen.sendMessage(Lang.PRE + Lang.getMessage(null, "not_a_player"));
			return false;
		}

		if (!p.hasPermission(PERMISSION)) {
			sen.sendMessage(Lang.PRE + String.format(l.getString("insufficient_permission"), PERMISSION));
			return false;
		}

		PC pc = new PC(p);

		if (!pc.isSet("Temp.particle") && args.length != 1) {
			p.sendMessage(Lang.PRE + String.format(l.getString("args_length_exact"), 1));
			return true;
		}

		if (pc.isSet("Temp.particle")) {
			pc.set("Temp", null);
			pc.savePCon();

			trail.remove(p.getName());
			p.sendMessage(" \n" + Lang.PRE + l.getString("trail_stopped"));

			if (args.length <= 0) return false;
		}

		Particle par = Particle.valueOf(args[0]);

		if (par == null) {
			p.sendMessage(Lang.PRE + String.format(l.getString("arg_null"), 0));
			return true;
		}

		Bukkit.getScheduler().runTaskLater(ItemGenerator.getItemGenerator(), () -> {

			if (!trail.contains(p.getName())) trail.add(p.getName());
			loop(p, par);

			pc.set("Temp.particle", par);
			pc.savePCon();
		}, 5);

		p.sendMessage(Lang.PRE + String.format(l.getString("particle_spawning"), par.toString().toLowerCase()));
		return false;
	}

	public void loop(Player p, Particle par) {
		if (!trail.contains(p.getName())) return;

		String s = par.getDataType().toString();
		if (s.contains("Void")) {
			if (isDirectional(par)) p.getLocation().getWorld().spawnParticle(par, p.getLocation().add(0, 1.3, 0),
					0, 0.1, 0.2, 0.2, 0.15);
			else p.getLocation().getWorld().spawnParticle(par, p.getLocation().add(0, 1.3, 0), 1);
		} else if (s.contains("BlockData"))
			p.getLocation().getWorld().spawnParticle(par, p.getLocation().add(0, 1.3, 0), 1,
					Material.GOLD_BLOCK.createBlockData());
		else if (s.contains("MaterialData"))
			p.getLocation().getWorld().spawnParticle(par, p.getLocation().add(0, 1.3, 0), 1,
					new MaterialData(Material.GOLD_INGOT));
		else if (s.contains("DustTransition"))
			p.getLocation().getWorld().spawnParticle(par, p.getLocation().add(0, 1.3, 0), 1,
					new Particle.DustTransition(Color.YELLOW, Color.ORANGE, 0.2f));
		else if (s.contains("ItemStack"))
			p.getLocation().getWorld().spawnParticle(par, p.getLocation().add(0, 1.3, 0), 1,
					new ItemStack(Material.GOLD_INGOT));
		else if (s.contains("DustOptions"))
			p.getLocation().getWorld().spawnParticle(par, p.getLocation().add(0, 1.3, 0), 1,
					new Particle.DustOptions(Color.YELLOW, 0.1f));
		else {
			p.sendMessage(par.getDataType().toString());
			return;
		}
		Bukkit.getScheduler().runTaskLater(ItemGenerator.getItemGenerator(), () -> loop(p, par), 3);
	}

	@Override
	public @Nullable List<String> onTabComplete(CommandSender sen, Command cmd, String lab, String[] args) {
		if (args.length != 1) return null;
		List<String> particles = new ArrayList<>();

		for (Particle s : Particle.values()) {
			if (!s.toString().contains(args[0].toUpperCase())) continue;

			if (!args[0].isEmpty()) {
				if ((s.toString().charAt(0) + "").equalsIgnoreCase(args[0].charAt(0) + ""))
					particles.add(s.toString());

			} else particles.add(s.toString());
		}
		return particles;
	}

	public boolean isDirectional(Particle p) {
		return directional.contains(p);
	}
}
