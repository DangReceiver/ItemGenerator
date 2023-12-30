package de.cruelambition.listener.function.entities;

import de.cruelambition.itemgenerator.ItemGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Void implements Listener {

	private final int bound = ItemGenerator.voidOffset();

	@EventHandler
	public void handle(EntityDamageEvent e) {
		if (e.getCause() != EntityDamageEvent.DamageCause.VOID) return;

		Entity en = e.getEntity();
		if (!(en instanceof Player p)) return;

		Location l = p.getLocation();
		if (l.getWorld() == ItemGenerator.spawn) return;

		e.setDamage(e.getDamage() + (e.getDamage() / 2));
		if (p.getHealth() <= 0) return;

		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,
				10, 0, false, false, false));

		int x = offset(), z = offset();
		p.teleport(l.add(x, 850, z));

		Bukkit.getScheduler().runTaskLater(ItemGenerator.getItemGenerator(), () -> blink(p), 4);
	}

	public int offset() {
		return new Random().nextInt(bound * 2 + 1) - bound;
	}

	public void blink(Player p) {
		if (p.hasPotionEffect(PotionEffectType.GLOWING))
			p.removePotionEffect(PotionEffectType.GLOWING);

		else
			p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,
					Integer.MAX_VALUE, 0, false, false, false));

		if (nearGround(p)) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,
					Integer.MAX_VALUE, 0, false, false, false));
			return;
		}

		Bukkit.getScheduler().runTaskLater(ItemGenerator.getItemGenerator(), () -> blink(p), 10);
	}

	public boolean nearGround(Player p) {
		Location l = p.getLocation();
		for (int i = 0; i >= -3; i--) {

			Material type = l.clone().add(0, i, 0).getBlock().getType();
			if (type != Material.AIR && type != Material.VOID_AIR) return true;

		}
		return false;
	}
}
