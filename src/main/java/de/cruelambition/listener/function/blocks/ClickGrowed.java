package de.cruelambition.listener.function.blocks;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class ClickGrowed implements Listener {

	@EventHandler
	public void interactGrowed(final PlayerInteractEvent e) {
		if (e.isCancelled()) return;
		Player p = e.getPlayer();

		if (e.getAction() == Action.RIGHT_CLICK_BLOCK && p.getGameMode() != GameMode.SPECTATOR) {
			if (e.getClickedBlock().getBlockData().toString().contains("[age=7]")) {

				switch (e.getClickedBlock().getType()) {
					default:
						return;

					case WHEAT, CARROT, POTATOES: {
						e.setCancelled(true);
						for (final ItemStack i : e.getClickedBlock().getDrops())
							p.getWorld().dropItemNaturally(e.getClickedBlock().getLocation(), i);
						e.getClickedBlock().setType(e.getClickedBlock().getType());
					}
				}
				p.playSound(p.getLocation(), Sound.ITEM_CROP_PLANT, 0.5f, 1.1f);

			} else if (e.getClickedBlock().getBlockData().toString().contains("[age=3]")) {

				switch (e.getClickedBlock().getType()) {
					default:
						return;

					case NETHER_WART, BEETROOT: {
						e.setCancelled(true);
						for (final ItemStack i : e.getClickedBlock().getDrops())
							p.getWorld().dropItemNaturally(e.getClickedBlock().getLocation(), i);
						e.getClickedBlock().setType(e.getClickedBlock().getType());
					}
				}
				p.playSound(p.getLocation(), Sound.ITEM_CROP_PLANT, 0.5f, 1f);

			}

			Random r = new Random();
			if (r.nextInt(17) == 1)
				for (int i = 0; i < r.nextInt(3); i++)
					p.getWorld().spawnEntity(p.getLocation(), EntityType.THROWN_EXP_BOTTLE);
		}
	}
}
