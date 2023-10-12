package de.cruelambition.listener.function;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class GlassShear implements Listener {

	@EventHandler
	public void handle(PlayerInteractEvent e) {
		Player p = e.getPlayer();

		Block cb = e.getClickedBlock();
		Action a = e.getAction();

		if (a != Action.RIGHT_CLICK_BLOCK) return;
		if (!cb.getType().toString().contains("_GLASS") || cb.getType().toString().contains("_GLASS_PANE")) return;

		ItemStack iimh = p.getInventory().getItemInMainHand();
		if (iimh.getType() != Material.SHEARS) return;
		iimh.setDurability((short) (iimh.getDurability() - 1));

		cb.getWorld().dropItemNaturally(cb.getLocation().add(0, 0.5, 0), new ItemStack(Material.GLASS));

		cb.setType(Material.AIR);
		cb.getWorld().playSound(cb.getLocation(), Sound.BLOCK_GLASS_BREAK, 0.5f, 1.3f);
		e.setCancelled(true);
	}
}
