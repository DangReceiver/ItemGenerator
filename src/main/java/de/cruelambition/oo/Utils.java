package de.cruelambition.oo;

import de.cruelambition.itemgenerator.ItemGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

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
}
