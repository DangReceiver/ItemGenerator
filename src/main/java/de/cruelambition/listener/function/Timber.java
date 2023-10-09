package de.cruelambition.listener.function;

import de.cruelambition.itemgenerator.ItemGenerator;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class Timber implements Listener {

	@EventHandler
	public void onTreeFarm(final BlockBreakEvent e) {
		if (e.isCancelled()) return;

		Block b = e.getBlock();
		Player p = e.getPlayer();

		if (!(p.isSneaking() || p.getGameMode() == GameMode.SURVIVAL || p.getGameMode() == GameMode.ADVENTURE)) return;

		ItemStack itemHand = p.getInventory().getItemInMainHand();
		if (!(itemHand.getType().toString().contains("_AXE") && e.getBlock().getType().toString().contains("LOG")))
			return;

		short d = 1;
		for (int t = 0; t <= 48; t++) {
			int eTimes = t;

			d = (short) (d + 2);
			if (!(e.getBlock().getLocation().add(0, (eTimes + 1), 0).getBlock().getType().equals(b.getType()))) return;

			Bukkit.getScheduler().runTaskLater( ItemGenerator.getItemGenerator(), () -> {
				if (!(p.getInventory().getItemInMainHand().getType().toString().contains("_AXE"))) return;

				itemHand.setDurability((short) (itemHand.getDurability() + 1));
				if (itemHand.getType().getMaxDurability() > itemHand.getDurability()) {

					Block above = e.getBlock().getLocation().add(0.0D, (eTimes + 1), 0.0D).getBlock();
					Bukkit.getServer().getWorld("world").playSound(above.getLocation(),
							Sound.BLOCK_WOOD_BREAK, 0.4F + (eTimes / 100), 0.65F + (eTimes / 100));

					above.breakNaturally(itemHand);

				} else {
					itemHand.setAmount(0);

					p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 0.4F, 0.75F);
					return;
				}
			}, d);
		}
	}
}
