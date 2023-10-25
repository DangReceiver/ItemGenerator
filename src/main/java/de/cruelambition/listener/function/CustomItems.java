package de.cruelambition.listener.function;

import de.cruelambition.itemgenerator.ItemGenerator;
import de.cruelambition.language.Lang;
import de.cruelambition.oo.Items;
import de.cruelambition.oo.PC;
import de.cruelambition.oo.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.rmi.server.RemoteRef;
import java.util.Random;

public class CustomItems implements Listener {

	@EventHandler
	public void handle(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		ItemStack item = e.getItem();
		if (item == null) return;

		Action a = e.getAction();
		Block cb = e.getClickedBlock();

		Lang l = new Lang(p);

		if (item.equals(Items.ITEMS.get(5))) {
			e.setCancelled(true);
			p.sendMessage("Baked Banana");

			if (a == Action.LEFT_CLICK_AIR) {

			}

		} else if (item.equals(Items.ITEMS.get(4))) {
			e.setCancelled(true);
			p.sendMessage("Banana");

			if (a == Action.LEFT_CLICK_AIR) {

			}

		} else if (item.equals(Items.ITEMS.get(3))) {
			e.setCancelled(true);
			p.sendMessage("Crate");

			if (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) {
				p.sendMessage("some action idk yet");
			}

		} else if (item.equals(Items.ITEMS.get(2))) {
			e.setCancelled(true);
			p.sendMessage("eraser");

			if (a == Action.LEFT_CLICK_BLOCK && cb != null) {

				int i = 0;
				// needs to be reworked
				checkNextBlocks(cb, i);

			} else {
				p.sendMessage("need to left click a block");
			}

		} else if (item.equals(Items.ITEMS.get(1))) {
			e.setCancelled(true);

			if (a.toString().contains("RIGHT") || a.toString().contains("LEFT")) {
				Random r = new Random();
				p.sendTitle(Lang.PRE, l.getString("sound_playing_sound"), 20, 20, 10);

				p.playSound(p.getLocation(), Sound.values()[r.nextInt(Sound.values().length - 1)],
						0.8f, (float) (r.nextInt(21) / 10));
			}

		} else if (item.equals(Items.ITEMS.get(0))) {
			e.setCancelled(true);
			PC pc = new PC(p);

			if (a.toString().contains("RIGHT")) {
				if (pc.getJetpackUsage()) {

					p.sendTitle(Lang.PRE, "§7" + l.getString("customitems_listener_jetpack_in_use"));
					return;
				}

				pc.setJetpackUsage(true);

				p.setVelocity(p.getVelocity().add(new Vector(0, 0.125, 0)).multiply(1.12f));
				Utils.particleOffset(p.getLocation(), Particle.FLAME, 2, 0.25);

				Bukkit.getScheduler().runTaskLater(ItemGenerator.getItemGenerator(), () -> {
					p.setVelocity(p.getVelocity().add(new Vector(0, 0.1f, 0)).multiply(1.2f));

					Utils.particleOffset(p.getLocation(), Particle.FLAME, 3, 0.3);
					pc.setJetpackUsage(false);
				}, 4);
			}
		}
	}

	public void checkNextBlocks(Block cb, int i) {
		Material type = cb.getType();
		cb.getWorld().dropItemNaturally(cb.getLocation().add(0, 0.5, 0), new ItemStack(type));

		if (i >= 4) return;

		Block xm = cb.getLocation().add(-1, 0, 0).getBlock();
		if (type == xm.getType()) {
			checkNextBlocks(xm, i + 1);
			xm.setType(Material.AIR);
		}

		Block xp = cb.getLocation().add(1, 0, 0).getBlock();
		if (type == xp.getType()) {
			checkNextBlocks(xp, i + 1);
			xp.setType(Material.AIR);
		}

		Block ym = cb.getLocation().add(0, -1, 0).getBlock();
		if (type == ym.getType()) {
			checkNextBlocks(ym, i + 1);
			ym.setType(Material.AIR);
		}

		Block yp = cb.getLocation().add(0, 1, 0).getBlock();
		if (type == yp.getType()) {
			checkNextBlocks(yp, i + 1);
			yp.setType(Material.AIR);
		}

		Block zm = cb.getLocation().add(0, 0, -1).getBlock();
		if (type == zm.getType()) {
			checkNextBlocks(zm, i + 1);
			zm.setType(Material.AIR);
		}

		Block zp = cb.getLocation().add(0, 0, 1).getBlock();
		if (type == zp.getType()) {
			checkNextBlocks(zp, i + 1);
			zp.setType(Material.AIR);
		}
	}

	@EventHandler
	public void handle(PlayerItemConsumeEvent e) {
		Player p = e.getPlayer();
		ItemStack item = e.getItem();
//		ItemStack rep = e.getReplacement();

		if (item.equals(Items.ITEMS.get(4))) {
			item.getType().isEdible();

			p.setSaturation(p.getSaturation() - 1);
			p.setExhaustion(p.getExhaustion() + 2);

		} else if (item.equals(Items.ITEMS.get(5))) {
			item.getType().isEdible();

			p.setSaturation(p.getSaturation() + 1);
			p.setExhaustion(p.getExhaustion() + 4);

		}

	}
}
