package de.cruelambition.listener.function.blocks;

import de.cruelambition.itemgenerator.ItemGenerator;
import de.cruelambition.oo.PC;
import de.cruelambition.oo.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

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
		if (l.getBlock().getType() != Material.AIR && l.getBlock().getType() != cb.getType()) return;

		e.setCancelled(true);
		PC pc = new PC(p);

		if (pc.isSet("Temp.CaneCactus")) return;
		pc.set("Temp.CaneCactus", true);
		pc.savePCon();

		if (l.getBlock().getType() != cb.getType()) l.getBlock().setType(cb.getType());
		else if (!extendGrowable(cb, cb.getLocation().clone())) return;

		if (hand.getAmount() > 1) hand.setAmount(hand.getAmount() - 1);
		else p.getInventory().setItemInHand(new ItemStack(Material.AIR));

		p.playSound(p.getLocation(), Sound.ITEM_BONE_MEAL_USE, 0.4f, 1.15f);
		Utils.particleOffset(l.clone().add(0.5, 0.5, 0.5), Particle.VILLAGER_HAPPY, 4, 0.65);

		Bukkit.getConsoleSender().sendMessage(e.getHandlers().toString());

		Bukkit.getScheduler().runTaskLater(ItemGenerator.getItemGenerator(), () -> {
			PC pc1 = new PC(p);

			pc1.set("Temp.CaneCactus", null);
			pc1.savePCon();
		}, 6);
	}

	public boolean extendGrowable(Block cb, Location loc) {

		while (loc.getBlock().getType() != Material.AIR && loc.getBlock().getType() == cb.getType()) {
			if (loc.getBlock().getType() != Material.AIR && loc.getBlock().getType() != cb.getType()) return false;

			loc.add(0, 1, 0);
		}
		loc.getBlock().setType(cb.getType());

		return (loc.getBlock().getType() == cb.getType());
	}
}
