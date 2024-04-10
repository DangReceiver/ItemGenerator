package de.cruelambition.listener.function.items;

import de.cruelambition.itemgenerator.ItemGenerator;
import de.cruelambition.language.Lang;
import de.cruelambition.oo.utils.Items;
import de.cruelambition.oo.PC;
import de.cruelambition.oo.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CustomItems implements Listener {

	private List<Player> delay = new ArrayList<>();

	@EventHandler
	public void handle(PlayerInteractEvent e) {
		Player p = e.getPlayer();

		ItemStack item = e.getItem();
		if (item == null) return;

		if (p.hasCooldown(item.getType())) {
			e.setCancelled(true);
			return;
		}

		ItemMeta im = item.getItemMeta();
		if (!im.hasCustomModelData()) return;

		Action a = e.getAction();
		Block cb = e.getClickedBlock();

		Lang l = new Lang(p);
//		p.sendMessage(Items.ITEMS.get(im.getCustomModelData() - 1).displayName());

		if (item.equals(Items.ITEMS.get(0))) {
			e.setCancelled(true);
			PC pc = new PC(p);

			if(p.getWorld() == ItemGenerator.spawn) {
				p.sendMessage(Lang.PRE + String.format(l.getString("generator_wrong_world")));
				return;
			}

			if (!pc.mayGenerateItem()) {
				if (delay.contains(p)) return;

				p.sendMessage(Lang.PRE + String.format(l.getString("generator_on_delay"),
						ItemGenerator.getItemGenerator().getConfig().getInt("Generator.Delay")));

				delay(p);
				return;
			}

			pc.disallowItemGeneration();
			ItemGenerator.g.give(p);

			pc.savePCon();
			p.setCooldown(item.getType(), 160);

		} else if (item.equals(Items.ITEMS.get(7))) {
			e.setCancelled(true);
			p.sendMessage("§5Phantom - wavvyboi");

			if (a == Action.LEFT_CLICK_BLOCK) {
				if (cb.getType() != Material.JUKEBOX) return;

				for (Player ap : Bukkit.getOnlinePlayers())
					ap.stopSound(Sound.MUSIC_DISC_CAT);

				cb.getWorld().playSound(cb.getLocation(), "records/aa_itemgenerator_wavvyboi-Phantom",
						0.5f, 1);
				cb.getWorld().playSound(cb.getLocation(), "aa_itemgenerator_wavvyboi-Phantom",
						0.5f, 1);
//				cb.setBlockData(Bukkit.createBlockData(""));
			}

		} else if (item.equals(Items.ITEMS.get(6))) {
//			e.setCancelled(true);
			p.sendMessage("Baked Banana");

			if (a == Action.LEFT_CLICK_AIR) {

			}

		} else if (item.equals(Items.ITEMS.get(5))) {
//			e.setCancelled(true);
			p.sendMessage("Banana");

			if (a == Action.LEFT_CLICK_AIR) {

			}

		} else if (item.equals(Items.ITEMS.get(4))) {
			e.setCancelled(true);
			p.sendMessage("Crate");

			if (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) {
				p.sendMessage("some action idk yet");
			}

		} else if (item.equals(Items.ITEMS.get(3))) {
			e.setCancelled(true);
			p.sendMessage("Eraser");

			if (a == Action.LEFT_CLICK_BLOCK && cb != null) {

				erase(cb, cb.getWorld());
				p.setCooldown(item.getType(), 80);

			} else if (a == Action.RIGHT_CLICK_BLOCK && cb != null) {

				// needs to be reworked
				highlight(cb, cb.getWorld());
				p.setCooldown(item.getType(), 80);

			} else {

				p.sendMessage("need to left click a block");
			}

		} else if (item.equals(Items.ITEMS.get(2))) {
			e.setCancelled(true);

			if (a.toString().contains("RIGHT") || a.toString().contains("LEFT")) {
				Random r = new Random();

				Sound sound = Sound.values()[r.nextInt(Sound.values().length - 1)];
				p.playSound(p.getLocation(), sound, 0.8f, (float) (r.nextInt(21) / 10));

				p.sendTitle(Lang.PRE, String.format(l.getString("sound_playing_sound"),
						sound.toString().toLowerCase()), 20, 20, 10);
			}

		} else if (item.equals(Items.ITEMS.get(1))) {
			e.setCancelled(true);
			PC pc = new PC(p);

			if (!a.toString().contains("RIGHT")) return;
			if (pc.getJetpackUsage()) {

				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 0.5f, 0.75f);
				p.sendActionBar(Lang.PRE + l.getString("jetpack_in_use"));
				return;
			}

			if (p.getFallDistance() > 0) p.setFallDistance(p.getFallDistance() / 2);

			pc.setJetpackUsage(true);
			pc.savePCon();

			p.setVelocity(p.getLocation().getDirection().multiply(0.22).setY(0.2));
			Utils.particleOffset(p.getLocation(), Particle.FLAME, 1, 0.275);

			Bukkit.getScheduler().runTaskLater(ItemGenerator.getItemGenerator(), () -> {
				p.setVelocity(p.getLocation().getDirection().multiply(0.2).setY(0.2));

				Utils.particleOffset(p.getLocation(), Particle.SOUL_FIRE_FLAME, 2, 0.35);
				PC pc1 = new PC(p);

				pc1.setJetpackUsage(false);
				pc1.savePCon();

			}, 6);
			p.setCooldown(item.getType(), 6);
		}
	}

	public void delay(Player p) {
		delay.add(p);

		Bukkit.getScheduler().runTaskLater(ItemGenerator.getItemGenerator(), () -> {
			if (delay.contains(p)) delay.remove(p);
		}, 20);
	}

	public void erase(Block cb, World w) {
		int x = cb.getX(), y = cb.getY(), z = cb.getZ();

		for (int xt = x - 5; xt <= x + 5; xt++)
			for (int yt = y - 5; yt <= y + 5; yt++)
				for (int zt = z - 5; zt <= z + 5; zt++)

					if (w.getBlockAt(xt, yt, zt).getType() == cb.getType())
						cb.setType(Material.AIR);

	}

	public void highlight(Block cb, World w) {
		int x = cb.getX(), y = cb.getY(), z = cb.getZ();

		// FOR LINES X -> Y & X -> Z
		for (int i = 0; i <= 10; i++) {
			for (int yt = y - 5; yt <= y + 5; yt++) {
				w.spawnParticle(Particle.VILLAGER_HAPPY, w.getBlockAt(x, yt, z).getLocation(), 1);
				w.spawnParticle(Particle.VILLAGER_HAPPY, w.getBlockAt(x, yt, z).getLocation().add(0, 0.5, 0), 1);
			}

			for (int zt = z - 5; zt <= z + 5; zt++) {
				w.spawnParticle(Particle.VILLAGER_HAPPY, w.getBlockAt(x, y, zt).getLocation(), 1);
				w.spawnParticle(Particle.VILLAGER_HAPPY, w.getBlockAt(x, y, zt).getLocation().add(0, 0, 1), 1);
			}
		}

		// FOR LINES Y -> Z & Y -> X
		for (int i = 0; i <= 10; i++) {
			for (int xt = x - 5; xt <= x + 5; xt++) {
				w.spawnParticle(Particle.VILLAGER_HAPPY, w.getBlockAt(xt, y, z).getLocation(), 1);
				w.spawnParticle(Particle.VILLAGER_HAPPY, w.getBlockAt(xt, y, z).getLocation().add(0, 0.5, 0), 1);
			}

			for (int zt = z - 5; zt <= z + 5; zt++) {
				w.spawnParticle(Particle.VILLAGER_HAPPY, w.getBlockAt(x, y, zt).getLocation(), 1);
				w.spawnParticle(Particle.VILLAGER_HAPPY, w.getBlockAt(x, y, zt).getLocation().add(0, 0, 1), 1);
			}
		}

		// FOR LINES Z -> X & Z -> Y
		for (int i = 0; i <= 10; i++) {
			for (int xt = x - 5; xt <= x + 5; xt++) {
				w.spawnParticle(Particle.VILLAGER_HAPPY, w.getBlockAt(xt, y, z).getLocation(), 1);
				w.spawnParticle(Particle.VILLAGER_HAPPY, w.getBlockAt(xt, y, z).getLocation().add(0, 0.5, 0), 1);
			}

			for (int yt = y - 5; yt <= y + 5; yt++) {
				w.spawnParticle(Particle.VILLAGER_HAPPY, w.getBlockAt(x, yt, z).getLocation(), 1);
				w.spawnParticle(Particle.VILLAGER_HAPPY, w.getBlockAt(x, yt, z).getLocation().add(0, 0, 1), 1);
			}
		}

	}

	@EventHandler
	public void handle(PlayerItemConsumeEvent e) {
		Player p = e.getPlayer();
		ItemStack item = e.getItem();
//		ItemStack rep = e.getReplacement();

		if (item.equals(Items.ITEMS.get(5))) {
			item.getType().isEdible();
			p.sendMessage("Banana");

			p.setSaturation(p.getSaturation() - 1);
			p.setExhaustion(p.getExhaustion() + 2);

		} else if (item.equals(Items.ITEMS.get(6))) {
			item.getType().isEdible();
			p.sendMessage("Baked Banana");

			p.setSaturation(p.getSaturation() + 1);
			p.setExhaustion(p.getExhaustion() + 4);
		}

	}
}
