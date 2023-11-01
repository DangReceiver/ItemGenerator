package de.cruelambition.listener.function.blocks;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class HoeUse implements Listener {

	@EventHandler
	public void handle(PlayerInteractEvent e) {
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

		Player p = e.getPlayer();
		ItemStack item = p.getInventory().getItemInMainHand();

		if (!(item != null && item.getType().toString().contains("_HOE"))) return;
		if (e.getClickedBlock().getType() != Material.COARSE_DIRT) return;

		e.setCancelled(true);
	}
}
