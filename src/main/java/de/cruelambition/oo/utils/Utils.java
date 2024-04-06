package de.cruelambition.oo.utils;

import de.cruelambition.itemgenerator.ItemGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Utils {

	public static void oneByOne(Player p, org.bukkit.Sound s, int times, float startAtPitch, float pitch, boolean up,
								float volume, long delay, int current) {

		float offset = pitch * -(up ? 2 * pitch * (times + 1) : 0);
		p.playSound(p.getLocation(), s, volume, (startAtPitch + offset >= 2 ? 2 : startAtPitch + offset));

		if (times >= current) return;
		Bukkit.getScheduler().runTaskLater(ItemGenerator.getItemGenerator(), () ->
				oneByOne(p, s, times, startAtPitch, pitch, up, volume, delay, current + 1), delay);
	}

	public static void particleOffset(Location l, Particle par, int amount, double area) {
		for (int i = 0; i < amount; i++) {

			Location loc = l.clone();
			Random r = new Random();

			loc.add(r.nextDouble(area * 2) - area,
					r.nextDouble(area * 2) - area, r.nextDouble(area * 2) - area);

			l.getWorld().spawnParticle(par, loc, 1);
		}
	}

	public static void particleOffsetDelayed(Player p, Particle par, int amount, int count, double area, int delay) {
		if (count >= amount) return;

		Location loc = p.getLocation().clone().add(0, 0.5, 0);
		Random r = new Random();

		loc.add(r.nextDouble(area * 2) - area,
				r.nextDouble(area * 2) - area,
				r.nextDouble(area * 2) - area);

		p.getLocation().add(0, 0.5, 0).getWorld().spawnParticle(par, loc, 1);

		Bukkit.getScheduler().runTaskLater(ItemGenerator.getItemGenerator(), () ->
				particleOffsetDelayed(p, par, amount, count + 1, area, delay), delay);
	}
}
