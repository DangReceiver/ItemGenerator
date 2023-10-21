package de.cruelambition.listener.function;

import de.cruelambition.oo.Items;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

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

		if (item.equals(Items.ITEMS.get(3))) {
			e.setCancelled(true);
			p.sendMessage("Crate");

			if (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) {

			}

		} else if (item.equals(Items.ITEMS.get(2))) {
			e.setCancelled(true);
			p.sendMessage("eraser");

			if (a == Action.LEFT_CLICK_BLOCK && cb != null) {

				int i = 0;
				checkNextBlocks(cb, i);

			} else {
				p.sendMessage("need to left click a block");
			}

		} else if (item.equals(Items.ITEMS.get(1))) {
			e.setCancelled(true);
			p.sendMessage("sound");

			if (a.toString().contains("RIGHT")) {
				Random r = new Random();

				p.playSound(p.getLocation(), Sound.values()[r.nextInt(Sound.values().length - 1)],
						0.75f, (float) (r.nextInt(21) / 10));
			}

		} else if (item.equals(Items.ITEMS.get(0))) {
			e.setCancelled(true);
			p.sendMessage("Mini Jetpack");

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
}
