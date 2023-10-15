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

import java.util.Random;

public class CustomItems implements Listener {

	@EventHandler
	public void handle(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		ItemStack item = e.getItem();

		Action a = e.getAction();
		Block cb = e.getClickedBlock();

		if (item == Items.ITEMS.get(3)) {
			e.setCancelled(true);
			p.sendMessage("Crate");

			if (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) {

			}

		} else if (item == Items.ITEMS.get(2)) {
			e.setCancelled(true);
			p.sendMessage("eraser");

			if (a == Action.LEFT_CLICK_BLOCK && cb != null) {

				int i = 0;
				checkNextBlocks(cb, i);

			} else {
				p.sendMessage("need to click a block");
			}

		} else if (item == Items.ITEMS.get(1)) {
			e.setCancelled(true);
			p.sendMessage("sound");

			if (a.toString().contains("LEFT")) {
				Random r = new Random();

				p.playSound(p.getLocation(), Sound.values()[r.nextInt(Sound.values().length - 1)],
						0.65f, (float) (r.nextInt(21) / 10));
			}

		} else if (item == Items.ITEMS.get(0)) {
			e.setCancelled(true);
			p.sendMessage("emerald hoe");

		}
	}

	public void checkNextBlocks(Block cb, int i) {
		Material type = cb.getType();
		cb.getWorld().dropItemNaturally(cb.getLocation().add(0, 0.5, 0), new ItemStack(type));

		if (i >= 3) return;

		Block xm = cb.getLocation().add(-1, 0, 0).getBlock();
		if (type == xm.getType()) {
			xm.setType(Material.AIR);
			checkNextBlocks(xm, i + 1);
		}

		Block xp = cb.getLocation().add(1, 0, 0).getBlock();
		if (type == xp.getType()) {
			xp.setType(Material.AIR);
			checkNextBlocks(xp, i + 1);
		}

		Block ym = cb.getLocation().add(0, -1, 0).getBlock();
		if (type == ym.getType()) {
			ym.setType(Material.AIR);
			checkNextBlocks(ym, i + 1);
		}

		Block yp = cb.getLocation().add(0, 1, 0).getBlock();
		if (type == yp.getType()) {
			yp.setType(Material.AIR);
			checkNextBlocks(yp, i + 1);
		}

		Block zm = cb.getLocation().add(0, 0, -1).getBlock();
		if (type == zm.getType()) {
			zm.setType(Material.AIR);
			checkNextBlocks(zm, i + 1);
		}

		Block zp = cb.getLocation().add(0, 0, 1).getBlock();
		if (type == zp.getType()) {
			zp.setType(Material.AIR);
			checkNextBlocks(zp, i + 1);
		}

	}
}
