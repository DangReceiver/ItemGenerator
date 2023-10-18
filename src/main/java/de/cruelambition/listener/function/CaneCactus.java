package de.cruelambition.listener.function;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.codehaus.plexus.util.ReflectionUtils;

public class CaneCactus implements Listener {

	@EventHandler
	public void handle(PlayerInteractEvent e) {
		if (e.isCancelled()) return;

		if (e.getClickedBlock() == null) return;
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

		Block cb = e.getClickedBlock();
		if (!(cb.getType() == Material.SUGAR_CANE || cb.getType() == Material.CACTUS)) return;

		Player p = e.getPlayer();
		ItemStack hand = p.getInventory().getItemInMainHand();
		if (hand.getType() != Material.BONE_MEAL) return;

		Location l = cb.getLocation().clone().add(0, 1, 0);
		while (l.clone().add(0, 1, 0).getBlock().getType() == cb.getType())
			l.add(0, 1, 0);
		l.add(0, 1, 0);

		if (l.getBlock().getType() != Material.AIR) return;
		e.setCancelled(true);
		l.getBlock().setType(cb.getType());

		if (hand.getAmount() > 1) hand.setAmount(hand.getAmount() - 1);
		else p.getInventory().setItemInHand(new ItemStack(Material.AIR));

		p.playSound(p.getLocation(), Sound.ITEM_BONE_MEAL_USE, 0.4f, 1.15f);
		p.spawnParticle(Particle.VILLAGER_HAPPY, l, 6);
	}
}
